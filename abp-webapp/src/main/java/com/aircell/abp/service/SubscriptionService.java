/*
 * SubscriptionService.java 2 Aug 2007
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
import com.aircell.abp.model.Subscription;
import com.aircell.bss.ws.TrackingType;

/**. @author miroslav.miladinovic at AKQA Inc. */
public interface SubscriptionService {

    /**.
     * @param username username
     * @param Locale Locale
     * @param tracking TrackingType
     * @param deviceType mobile or laptop
     * @return ServiceResponse ServiceResponse
     */
    ServiceResponse<Subscription[]> getAvailableSubscriptions(
    final String username, final String Locale, final TrackingType tracking,
    final String deviceType
    );

    /**.
     * @param username username
     * @param Locale Locale
     * @param deviceType mobile or laptop
     * @param flight FlightInformation
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     */
    ServiceResponse<Subscription[]> getAvailableSubscriptions(
    final String username, final FlightInformation flight, final String Locale,
    final TrackingType tracking, final String deviceType
    );

    /**.
     * @param username username
     * @param subscription Subscription
     * @param DeviceType mobile or laptop
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     */
    ServiceResponse activateSubscription(
    final String username, final Subscription subscription,
    final String DeviceType, final TrackingType tracking
    );


}
