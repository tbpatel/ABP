/*
 * Order.java 3 Aug 2007
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

import java.io.Serializable;
import java.util.Set;

/**. @author miroslav.miladinovic at AKQA Inc. */
public interface Order extends Serializable {

    /**.
     * Returns the id
     * @return id in a String variable
     */
    String getId();

    /**.
     * Add an orderLineItem
     * @param item OrderLineItem object
     */
    void addOrderLineItem(final OrderLineItem item);

    /**.
     * Removes orderLineItem
     * @param item OrderLineItem object
     * @return boolean specifying the result of removal
     */
    boolean removeOrderLineItem(final OrderLineItem item);

    /**.
     * Gets OrderLineItemsReadOnly
     * @return Set containing OrderLineItem
     */
    Set<OrderLineItem> getOrderLineItemsReadOnly();

    /**.
     * Returns a boolean specifying if the order is empty
     * @return boolean specifying if the order is empty
     */
    boolean isEmpty();

    /**.
     * @param tracking TrackingType object
     * @return double value containing total inclusive of tax
     * @deprecated in favour of getOrderTotalInfo
     */
    double getOrderTotalIncTax(TrackingType tracking);

    /**.
     * @param tracking TrackingType object
     * @return double value containg total discount
     * @deprecated in favour of getOrderTotalInfo
     */
    double getOrderTotalDiscount(TrackingType tracking);

    /**.
     * @param tracking TrackingType object
     * @return double value containing total tax
     * @deprecated in favour of getOrderTotalInfo
     */
    double getOrderTotalTax(TrackingType tracking);


    /**.
     * Gets total info of the order
     * @param tracking TrackingType object
     * @return ServiceResponse containing TaxDiscountAmountPair
     */
    ServiceResponse<TaxDiscountAmountPair> getOrderTotalInfo(
    TrackingType tracking
    );

    /**.
     * Sets promo code
     * @param promoCode String containing promo code
     */
    void setPromoCode(final String promoCode);

    /**.
     * Gets promo code
     * @return String containing promo code
     */
    String getPromoCode();

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
     * @param creditCard can be null if order total due is zero
     * @param saveCardDetails if true credit card details will be saved with the
     * system.
     * @param DeviceType DeviceType
     * @param tracking TrackingType object
     * @return the payload contains order reference number. The error code is of
     *         type <code>BSSErrorCode</code>
     * order total due is greater than zero
     * @throws IllegalStateException if the order is empty or if it is not in
     * the <code>State.UNPAID_FOR</code> state
     */
    ServiceResponse<String> makePayment(
    final CreditCardDetails creditCard, final boolean saveCardDetails,
    final String DeviceType, final TrackingType tracking
    ) throws IllegalStateException;

    /**.
     * Returns boolean specifying if order has line
     *  item of type Class given as param
     * @param clazz Class object
     * @return boolean specifying if order has line
     *  item of type Class given as param
     */
    boolean hasLineItemOfType(final Class clazz);
}
