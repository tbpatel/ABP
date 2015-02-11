/*
 * ProductCatalogService.java 21 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.SubscriptionProduct;
import com.aircell.bss.ws.TrackingType;

/**.
 * Service for getting the available subscription products from the air or from
 * the ground
 * @author miroslav.miladinovic at AKQA Inc.
 */
public interface ProductCatalogService {

    /**.
     * Calls the subscription products web service and get returns an array of
     * possible subscriptions It is assumed that the following mappings are
     * true: - ProductType.productCode -> SubscriptionProduct.productCode -
     *               name -> name -                   price -> priceExTax -
     *         expiryDate -> expiryDate -        shortDescription ->
     * shortDescription -         longDescription -> longDescription
     * @param flight The current flight information
     * @param Locale Locale
     * @param DeviceType mobile or laptop
     * @param tracking TrackingType
     * @return service response object containing an array of available
     *         subscription types
     */
    ServiceResponse<SubscriptionProduct[]> getSubscriptionProducts(
    FlightInformation flight, String Locale, String DeviceType,
    TrackingType tracking
    );

    /**.
     * Returns all the subscription products available from the ground (ie. no
     * flight info)
     * @return ServiceResponse with payload of Array of Products
     */
    ServiceResponse<SubscriptionProduct[]> getSubscriptionProducts();
}
