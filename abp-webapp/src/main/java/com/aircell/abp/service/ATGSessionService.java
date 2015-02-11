/*
 * ATGSessionService.java 14 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import com.aircell.abp.model.LoginDetails;

/**. @author miroslav.miladinovic at AKQA Inc. */
public interface ATGSessionService {

    /**.
     * @param loginDetails LoginDetails object
     * @param ipAddress ipAddress
     * @return ServiceResponse object
     */
    ServiceResponse sendStartATGSessionMsg(
    final LoginDetails loginDetails, final String ipAddress
    );

    /**.
     * @param ipAddress ipAddress
     * @return ServiceResponse object
     * @throws ServiceException
     */
    ServiceResponse sendEndATGSessionMsg(final String ipAddress);
}
