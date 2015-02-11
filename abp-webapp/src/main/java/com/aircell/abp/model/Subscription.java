/*
 * Subscription.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import java.util.Date;


/**.
 * This interface represents all the types of subscriptions that aircell offers
 * @author miroslav.miladinovic at AKQA Inc.
 */
public interface Subscription {
    int getId();

    boolean isValid();

    Date getDateActivated();

    String getName();

    String getDescription();

    String getProductCode();

    double getCostWhenPurchased();

    int getDurationRemainingHours();

    int getDurationRemainingMinutes();
}
