/*
 * ProductCatalog.java 30 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.service.ProductCatalogService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.bss.ws.TrackingType;
import org.springframework.beans.factory.annotation.Configurable;

/**.
 * Represents an in-flight product catalog. Provides means of accessing products
 * available for sale on the current flight. This domain class is dependent on
 * the product catalog and the config service to obtain the actual product list
 * and flight data respectively. Objects of this class should not generally be
 * cached as they are fairly stateless and lightweight.
 * @author miroslav.miladinovic at AKQA Inc.
 */
@Configurable()
public class InFlightProductCatalog {

    private transient ProductCatalogService productCatalogService;

    // ********************************************
    // *** business methods
    // ********************************************

    /**.
     * Returns currently available subsription for sale on the current
     * flight.
     * @param flight FlightInformation object
     * @param Locale Locale
     * @param DeviceType mobile or laptop
     * @param tracking TrackingType
     * @return the service response object with an array of SubscriptionProduct
     * objects as payload
     * @throws IllegalStateException if the flight data could not be obtained.
     */
    public ServiceResponse<SubscriptionProduct[]> getSubscriptionsForSale(
    final FlightInformation flight, String Locale, String DeviceType,
    TrackingType tracking
    ) throws IllegalStateException {
        if (flight == null) {
            throw new IllegalStateException(
            "This catalog has to have " + "current flight data avialable"
            );
        }
        return getProductCatalogService()
        .getSubscriptionProducts(flight, Locale, DeviceType, tracking);
    }

    // *********************************************
    // *** Spring dependency injection
    // *********************************************

    /**.
     * @return the productCatalogService
     */
    public ProductCatalogService getProductCatalogService() {
        return productCatalogService;
    }

    /**.
     * @param productCatalogService the productCatalogService to set
     */
    public void setProductCatalogService(
    final ProductCatalogService productCatalogService
    ) {
        this.productCatalogService = productCatalogService;
    }
}
