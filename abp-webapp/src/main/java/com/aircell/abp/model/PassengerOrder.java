/*
 * PassengerOrder.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.TaxDiscountAmountPair;
import com.aircell.bss.ws.TrackingType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**. @author miroslav.miladinovic at AKQA Inc. */
@Configurable
public class PassengerOrder extends SimpleOrder implements Serializable {

    /**.
     * static final varable containing serialVersionUID
     */
    private static final long serialVersionUID = 3253786740227636638L;

    /**.
     * Logger object
     */
    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    /**.
     * FlightIformation object
     */
    private FlightInformation flight;

    /**.
     * Constructor
     */
    private PassengerOrder() {}

    /**.
     * Constructor - Initialize member variables
     * @param owner Passenger object
     * @param flight FlightInformation object
     */
    public PassengerOrder(
    final Passenger owner, final FlightInformation flight
    ) {
        super(owner);
        if (flight == null) {
            throw new IllegalArgumentException(
            "flight argument cannot " + "be null."
            );
        }
        this.flight = flight;
    }

    /**.
     * overrides superclass method
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     */
    @Override
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
            getFlight(), new Date(System.currentTimeMillis()), tracking
            );
        }
        if (!result.isSuccess()) {
            final TaxDiscountAmountPair info = new TaxDiscountAmountPair();
            final Set<OrderLineItem> lineItems = getOrderLineItemsReadOnly();
            for (OrderLineItem item : lineItems) {
                logger
                .info("Total :" + item.getPriceExTax() + item.getTaxAmount());
                info.setDiscountAmount(item.getDiscountAmount());
                info.setTaxAmount(item.getTaxAmount());
                info.setTotalAmount(item.getPriceExTax() + item.getTaxAmount());
                result.setPayload(info);
            }
            logger.error(
            "Unable to determine total amount due for this order '{}'"
            + ",service error '{}'", this, result
            );

        } else {
            this.taxDiscountAmount = result.getPayload();
            setCalculated(Boolean.TRUE.booleanValue());
        }
        return result;
    }

    /**.
     * Makes the call to the payment system.
     * This method supplies flight information.
     * @param creditCard CreditCardDetails object
     * @param purchaseTime PurchaseTime
     * @param totalDue Total Due
     * @param saveCardDetails True if card details already saved
     * @param tracking TrackingType
     * @param DeviceType mobile or laptop
     * @return the payload contains order reference number. The error code is of
     *         type <code>BSSErrorCode</code>
     */
    @Override
    protected ServiceResponse<String> executePaymentCall(
    final CreditCardDetails creditCard, final Date purchaseTime,
    final double totalDue, final boolean saveCardDetails,
    final String DeviceType, final TrackingType tracking
    ) {
        logger.info(
        "Passenger '{}' is requesting payment for items '{}' "
        + "using credit card '{}', saveCardDetails '{}', total due '{}', "
        + "purchaseTime '{}', flight '{}'", new Object[]{
        getOwner(), getOrderLineItemsReadOnly(), creditCard, saveCardDetails,
        totalDue, purchaseTime, getFlight()
        }
        );

        final ServiceResponse<String> response =
        getPaymentService().purchaseItems(
        getOwner().getLoginDetails(), creditCard, getOrderLineItemsReadOnly(),
        getFlight(), purchaseTime, totalDue, saveCardDetails, DeviceType,
        tracking
        );

        logger.info(
        "Passenger '{}' received response from payment service: '{}' "
        + "for order '{}', total due was '{}', purchaseTime '{}', "
        + "flight '{}'", new Object[]{
        getOwner(), response, this, totalDue, purchaseTime, getFlight()
        }
        );

        return response;
    }

    // *********************************
    // *** Getters/Setters
    // *********************************

    /**.
     * Gets FlightInformation
     * @return FlightInformation
     */
    public FlightInformation getFlight() {
        return flight;
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
