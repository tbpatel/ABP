/*
 * Order.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.service.PaymentService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.TaxDiscountAmountPair;
import com.aircell.bss.ws.TrackingType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**.
 * Order cannot be changed once its been paid for.
 * @author miroslav.miladinovic at AKQA Inc.
 */
@Configurable
public class SimpleOrder implements Order, Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 5211504391195195897L;

    /**.
     *
     * Enum for payment status
     *
     */
    public enum State {
        UNPAID_FOR,
        REQUESTING_PAYMENT,
        PAYMENT_CLEARED;
    }

    /**. private class variable to hold paymentService object */
    private transient PaymentService paymentService;
    /**. variable for logging */
    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    /**. private class variable to hold owner */
    private AircellUser owner;
    /**. private class variable to hold orderLineItems */
    private Set<OrderLineItem> orderLineItems;
    /**. private class variable to hold state */
    private State state;
    /**. private class variable to hold promoCode */
    private String promoCode;
    /**. private class variable to hold purchaseTime */
    private Date purchaseTime;
    /**. private class variable to hold id */
    private String id;
    /**. private class variable to hold taxDiscountAmount */
    protected TaxDiscountAmountPair taxDiscountAmount;
    /**. private class variable to hold true/false */
    private boolean calculated;

    /**.
     * Constructor
     *
     */
    protected SimpleOrder() {}

    /**.
     * Constructor - Initialize member variables
     * @param owner AircellUser object
     */
    public SimpleOrder(final AircellUser owner) {
        if (owner == null) {
            throw new IllegalArgumentException(
            "Aircell user " + "arg is required"
            );
        }
        // linkedHashSet gives us ordered set.
        this.orderLineItems = new LinkedHashSet<OrderLineItem>();
        this.owner = owner;
        this.state = State.UNPAID_FOR;
    }

    // *** Business methods
    /**.
     * Add orderLineItem
     * @param item OrderLineItem
     * @throws IllegalStateException
     */
    public void addOrderLineItem(final OrderLineItem item)
    throws IllegalStateException {
        if (!State.UNPAID_FOR.equals(getState())) {
            throw new IllegalStateException(
            "Cannot modify order " + "once it has been paid for."
            );
        }
        logger.debug("SimpleOrder.addOrderLineItem: "
        + "Aircell user {} is adding order item {}", getOwner(), item
        );
        orderLineItems.add(item);
        setCalculated(Boolean.FALSE.booleanValue());
    }

    /**.
     * Removes orderLineItem
     * @param item OrderLineItem
     * @return true/false
     * @throws IllegalStateException
     */
    public boolean removeOrderLineItem(final OrderLineItem item)
    throws IllegalStateException {
        if (!State.UNPAID_FOR.equals(getState())) {
            throw new IllegalStateException(
            "Cannot modify order " + "once it has been paid for."
            );
        }
        logger.debug("SimpleOrder.removeOrderLineItem: "
        + "Aircell user {} is removing order item {}", getOwner(), item
        );
        setCalculated(Boolean.FALSE.booleanValue());
        return orderLineItems.remove(item);
    }

    /**.
     * Checks orderLineItems contains any order or not
     * @return true/false
     */
    public boolean isEmpty() {
        return orderLineItems == null || orderLineItems.isEmpty();
    }

    /**.
     * Gets Order total Information
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     */
    public ServiceResponse<TaxDiscountAmountPair> getOrderTotalInfo(
    TrackingType tracking
    ) {
        if (isEmpty()) {
            final ServiceResponse<TaxDiscountAmountPair> zero =
            new ServiceResponse<TaxDiscountAmountPair>();
            zero.setSuccess(true);
            zero.setPayload(new TaxDiscountAmountPair());
            zero.getPayload().setDiscountAmount(0.00);
            zero.getPayload().setTaxAmount(0.00);
            zero.getPayload().setTotalAmount(0.00);
            return zero;
        }
        if (getPaymentService() == null) {
            throw new IllegalStateException(
            "paymentService not supplied. Check your configuration."
            );
        }
        final ServiceResponse<TaxDiscountAmountPair> result;
        if (isCalculated()) {
            result = new ServiceResponse<TaxDiscountAmountPair>();
            result.setSuccess(true);
            result.setPayload(this.taxDiscountAmount);
        } else {
            applyPromoCode();
            result = getPaymentService().calculateTaxAndPromotionForItems(
            getOwner().getLoginDetails(), getOrderLineItemsReadOnly(),
            new Date(System.currentTimeMillis()), tracking
            );
        }
        if (!result.isSuccess()) {
            logger.error("SimpleOrder.getOrderTotalInfo:"
            + "Unable to determine total amount due for this order '{}'"
            + ",service error '{}'", this, result
            );
        } else {
            this.taxDiscountAmount = result.getPayload();
            setCalculated(Boolean.TRUE.booleanValue());
        }
        return result;
    }

    /**.
     * Assumes that there can be only one promotion code per basket and that it
     * is applied to all items in the basket
     */
    protected void applyPromoCode() {
        if (!isEmpty() && !StringUtils.isBlank(getPromoCode())) {
            for (OrderLineItem item : orderLineItems) {
                item.setPromoCode(getPromoCode());
            }
        }
    }

    /**.
     *  @deprecated in favour of getOrderTotalInfo()
     *  @param tracking TrackingType
     *  @return double total amount including tax
     */
    public double getOrderTotalIncTax(TrackingType tracking) {
        final ServiceResponse<TaxDiscountAmountPair> retval =
        getOrderTotalInfo(tracking);
        return retval.isSuccess() ? retval.getPayload().getTotalAmount() : 0.00;
    }

    /**.
     * @deprecated in favour of getOrderTotalInfo()
     * @param tracking TrackingType
     * @return double total discount ammount
     */
    public double getOrderTotalDiscount(TrackingType tracking) {
        final ServiceResponse<TaxDiscountAmountPair> retval =
        getOrderTotalInfo(tracking);
        return retval.isSuccess() ? retval.getPayload().getDiscountAmount() :
        0.00;
    }

    /**.
     * @deprecated in favour of getOrderTotalInfo()
     * @param tracking TrackingType
     * @return doublr total tax ammount
     */
    public double getOrderTotalTax(TrackingType tracking) {
        final ServiceResponse<TaxDiscountAmountPair> retval =
        getOrderTotalInfo(tracking);
        return retval.isSuccess() ? retval.getPayload().getTaxAmount() : 0.00;
    }

    /**.
     * Makes the payment for this order. This method should only be called if
     * order is not empty.
     * Credit card details are ignored if total due of this order is zero (free
     * order).
     * If the outcome of this call is successful then this object will change
     * its state to <code>State.PAYMENT_CLEARED</code>.
     * Note: care should be taken by the caller to synchronize (or serialize)
     * the call to this method. This call does use any transaction management
     * itself. This means that if the user makes subsequent requests (re-POSTs
     * for example) they will fail as the object will move out of the
     * <code>State.UNPAID_FOR</code>
     * @param DeviceType mobile or laptop
     * @param tracking TrackingType
     * @param creditCard can be null if order total due is zero
     * @param saveCardDetails if true credit card details will be saved with the
     * system.
     * @return the payload contains order reference number. The error code is of
     *         type <code>BSSErrorCode</code>
     * @throws IllegalArgumentException if credit card details are null and
     * order total due is greater than zero
     * @throws IllegalStateException if the order is empty or if it is not in
     * the <code>State.UNPAID_FOR</code> state
     */
    public ServiceResponse<String> makePayment(
    final CreditCardDetails creditCard, final boolean saveCardDetails,
    final String DeviceType, final TrackingType tracking
    ) throws IllegalStateException, IllegalArgumentException {
        if (getPaymentService() == null) {
            throw new IllegalStateException(
            "paymentService not injected. Check your configuration."
            );
        }
        if (isEmpty()) {
            throw new IllegalStateException("Order is currently empty");
        }
        if (!State.UNPAID_FOR.equals(getState())) {
            throw new IllegalStateException(
            "Can only pay for order " + "that has not been paid for"
            );
        }
        getOwner().assertState(AircellUser.State.LOGGED_IN);

        double totalDue = 0.00;
        final ServiceResponse<TaxDiscountAmountPair> total =
        getOrderTotalInfo(tracking);
        if (total.isSuccess()) {
            totalDue = total.getPayload().getTotalAmount();
        } else {
            logger.error("SimpleOrder.makePayment: "
            + "Unable to pay for order. There was an error determining "
            + "order total due. Order '{}', user '{}', response '{}'",
            new Object[]{this, getOwner(), total }
            );
            final ServiceResponse<String> s = new ServiceResponse<String>();
            s.setSuccess(false);
            s.setErrorCode(total.getErrorCode());
            s.setErrorText(total.getErrorText());
            s.setErrorCause(total.getErrorCause());
            return s;
        }
        setState(State.REQUESTING_PAYMENT);
        final Date purchaseTime = new Date(System.currentTimeMillis());

        final ServiceResponse<String> response = executePaymentCall(
        creditCard, purchaseTime, totalDue, saveCardDetails, DeviceType,
        tracking
        );

        if (response.isSuccess()) {
            setPurchaseTime(purchaseTime);
            setState(State.PAYMENT_CLEARED);
            setId(response.getPayload());
        } else {
            setState(State.UNPAID_FOR);
            }
        response.setPurchaseTime(purchaseTime.toString());
        return response;
    }

    /**.
     * Makes the call to the payment system.
     * @param creditCard CreditCardDetails object
     * @param purchaseTime purchaseTime
     * @param totalDue totalDue
     * @param DeviceType mobile or laptop
     * @param tracking TrackingType
     * @param saveCardDetails true if card details is saved
     * @return the payload contains order reference number. The error code is of
     *         type <code>BSSErrorCode</code>
     */
    protected ServiceResponse<String> executePaymentCall(
    final CreditCardDetails creditCard, final Date purchaseTime,
    final double totalDue, final boolean saveCardDetails,
    final String DeviceType, final TrackingType tracking
    ) {
        logger.info(
        "Aircell user '{}' is requesting payment for items '{}' "
        + "using credit card '{}', saveCardDetails '{}', total due '{}', "
        + "purchaseTime '{}'", new Object[]{
        getOwner(), getOrderLineItemsReadOnly(), creditCard, saveCardDetails,
        totalDue, purchaseTime
        }
        );

        final ServiceResponse<String> response =
        getPaymentService().purchaseItems(
        getOwner().getLoginDetails(), creditCard, getOrderLineItemsReadOnly(),
        purchaseTime, totalDue, saveCardDetails, DeviceType, tracking
        );

        logger.info(
        "Aircell user '{}' received response from payment service: '{}' "
        + "for order '{}', total due was '{}', purchaseTime '{}'",
        new Object[]{getOwner(), response, this, totalDue, purchaseTime }
        );

        return response;
    }

    /**.
     * Checks if this order has at least one order line item of given type. Note
     * that it will return true if an order has a subclass of a given class.
     * @param clazz Class
     * @return boolean true if this order has at least one order line item
     */
    @SuppressWarnings("unchecked")
    public boolean hasLineItemOfType(final Class clazz) {
        // TODO miro: make better - something is smelly here!
        if (clazz == null) {
            return false;
        }
        boolean retval = false;
        for (OrderLineItem item : getOrderLineItemsReadOnly()) {
            retval |= clazz.isAssignableFrom(item.getClass());
        }
        return retval;
    }

    // *** general getters/setters

    /**.
     * Gets state of order
     * @return state of order in a State object
     */
    public State getState() {
        return state;
    }

    /**.
     * Sets state of order
     * @param state state of order to be set
     */
    void setState(final State state) {
        this.state = state;
    }

    /**.
     * Gets promoCode
     * @return promoCode
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**.
     * Sets promoCode
     * @param promoCode promoCode to be set
     */
    public void setPromoCode(final String promoCode) {
        this.promoCode = promoCode;
        setCalculated(Boolean.FALSE.booleanValue());
    }

    /**.
     * Gets owner AircellUser object
     * @return owner AircellUser object
     */
    public AircellUser getOwner() {
        return owner;
    }

    /**.
     * Gets id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**.
     * Sets id
     * @param id id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    // *** Spring injected things
    /**.
     * Gets paymentService PaymentService object
     * @return paymentService PaymentService object
     */
    public PaymentService getPaymentService() {
        return paymentService;
    }

    /**.
     * Sets paymentService PaymentService object
     * @param paymentService PaymentService object to be set
     */
    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**.
     * Gets purchaseTime as a Date object
     * @return purchaseTime as a Date object
     */
    public Date getPurchaseTime() {
        return purchaseTime;
    }

    /**.
     * Gets purchaseTime as a String
     * @return purchaseTime as a String
     */
    public String getPurchaseTimeString() {
        String purchaseTimeString = "";
        if (purchaseTime != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            purchaseTimeString = df.format(purchaseTime);
        }
        return purchaseTimeString;
    }

    /**.
     * Sets purchaseTime as a Date object
     * @param date purchaseTime as a Date object to be set
     */
    void setPurchaseTime(final Date date) {
        this.purchaseTime = date;
    }

    /**.
     * Getter method
     * @return OrderLineItem
     */
    public Set<OrderLineItem> getOrderLineItemsReadOnly() {
        // looking at the source code of this method, it is inexpensive.
        return Collections.unmodifiableSet(orderLineItems);
    }

    /**.
     * Gets a boolean value after checking if order is calculated
     * @return the calculated */
    public boolean isCalculated() {
        return calculated;
    }

    /**.
     * Sets a boolean value after checking if order is calculated
     * @param calculated the calculated to set */
    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    /**.
     * overrides superclass method
     * @return String String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
