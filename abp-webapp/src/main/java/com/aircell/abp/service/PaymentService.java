/*
 * PaymentService.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import com.aircell.abp.model.CreditCardDetails;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.OrderLineItem;
import com.aircell.bss.ws.TrackingType;

import java.util.Date;
import java.util.Set;

/**.
 * Abstraction over the BSS payment services. Defines services available to both
 * the ground and in-flight based components.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public interface PaymentService {

    /**.
     * Pays for the order line items given. This version takes flight
     * information as well and therefore should be used for in-flight
     * payment requests.
     *
     * @param loginDetails LoginDetails object containing login details
     * @param cardDetails CardDetails object containg credit card details
     * @param lineItems read only set
     * @param flight FlightInformation
     * @param purchaseTime purchaseTime
     * @param totalDue totalDue
     * @param saveCardDetails boolean checking if card details are saved
     * @param DeviceType DeviceType
     * @param tracking TrackingType object
     * @return <code>ServiceResponse</code> object with BSS order reference
     * as the payload. Error codes are of
     * {@link com.aircell.abp.service.BSSErrorCode}
     * type.
     * @throws IllegalArgumentException if any required arguments are null.
     */
    ServiceResponse<String> purchaseItems(
    final LoginDetails loginDetails, final CreditCardDetails cardDetails,
    final Set<OrderLineItem> lineItems, final FlightInformation flight,
    final Date purchaseTime, final double totalDue,
    final boolean saveCardDetails, final String DeviceType,
    final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Pays for the order line items given. This method serves payment requests
     * from the ground based dot com portal.
     *
     * @param loginDetails LoginDetails object containing login details
     * @param cardDetails CardDetails object containg credit card details
     * @param lineItems as ready only set
     * @param purchaseTime purchaseTime
     * @param totalDue totalDue
     * @param saveCardDetails boolean checking if card details are saved
     * @param DeviceType DeviceType
     * @param tracking TrackingType object
     * @return <code>ServiceResponse</code> object with BSS order reference
     * as the payload. Error codes are of
     * {@link com.aircell.abp.service.BSSErrorCode}
     * type.
     * @throws IllegalArgumentException if any required arguments are null.
     */
    ServiceResponse<String> purchaseItems(
    final LoginDetails loginDetails, final CreditCardDetails cardDetails,
    final Set<OrderLineItem> lineItems, final Date purchaseTime,
    final double totalDue, final boolean saveCardDetails,
    final String DeviceType, final TrackingType tracking
    ) throws IllegalArgumentException;


    /**.
     * Calculates tax and discount information for a set of order line items.
     * This version takes into account flight information and therefore
     * can be used for in-flight order calculation requests.
     *
     * @param loginDetails LoginDetails object
     * @param lineItems read only set
     * @param flight FlightInformation object
     * @param purchaseTime purchaseTime
     * @param tracking  TrackingType
     * @return <code>ServiceResponse</code> object with tax and discount info
     * for this order.Error codes are of
     *  {@link com.aircell.abp.service.BSSErrorCode}
     * type.
     * @throws IllegalArgumentException if any required arguments are null.
     */
    ServiceResponse<TaxDiscountAmountPair> calculateTaxAndPromotionForItems(
    final LoginDetails loginDetails, final Set<OrderLineItem> lineItems,
    final FlightInformation flight, final Date purchaseTime,
    final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Calculates tax and discount information for a set of order line items.
     * This version does not take flight information into account. Therefore
     * this method can be used for requests coming from the ground based
     * dot com portal.
     *
     * @param loginDetails LoginDetails object
     * @param lineItems read-only set
     * @param purchaseTime purchaseTime
     * @param tracking TrackingType
     * @return <code>ServiceResponse</code> object with tax and discount info
     * for this order.Error codes are of
     * {@link com.aircell.abp.service.BSSErrorCode}
     * type.
     * @throws IllegalArgumentException if any required arguments are null.
     */
    ServiceResponse<TaxDiscountAmountPair> calculateTaxAndPromotionForItems(
    final LoginDetails loginDetails, final Set<OrderLineItem> lineItems,
    final Date purchaseTime, final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Gets all the stored credit card details from the BSS
     * @param username The user whose details are being returned
     * @return Array of Credit Cards
     * @param tracking TrackingType
     * @throws IllegalArgumentException
     */
    ServiceResponse<CreditCardDetails[]> retrieveStoredCreditCards(
    final String username, final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Gets the credit card details for the user and card id specified
     * @param cardId Unique Id for the credit card
     * @param username User that the card belongs to
     * @return An array of CreditCardDetails
     * @param tracking TrackingType
     * @throws IllegalArgumentException exception for illegal argument
     */
    ServiceResponse<CreditCardDetails[]> getCreditCardDetails(
    final String cardId, final String username, final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Adds a credit card for the username  with the details in ccDetails
     * @param username User registered to the card
     * @param ccDetails New Credit Card details
     * @param tracking TrackingType
     * @return Service Response with isSuccess set true if
     *  the service call was OK, false otherwise
     */

    ServiceResponse<CreditCardDetails[]> addCreditCardDetails(
    final String username, final CreditCardDetails ccDetails,
    final TrackingType tracking
    ) throws IllegalArgumentException;


    /**.
     * Updates the CCard specified by cardId and username
     * with the details in ccDetails
     * @param cardId Id of the card to be removed
     * @param username User registered to the card
     * @param ccDetails New Credit Card details
     * @param tracking TrackingType
     * @return Service Response with isSuccess set true if the
     * service call was OK, false otherwise
     */
    ServiceResponse<CreditCardDetails[]> updateCreditCardDetails(
    final String cardId, final String username,
    final CreditCardDetails ccDetails, final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Removes the specified CCard from the BSS
     * @param cardId Id of the card to be removed
     * @param username User registered to the card
     * @param tracking TrackingType
     * @return Service Response with isSuccess set true if
     * the service call was OK, false otherwise
     */
    ServiceResponse<CreditCardDetails[]> removeCreditCardDetails(
    final String cardId, final String username, final TrackingType tracking
    ) throws IllegalArgumentException;
}
