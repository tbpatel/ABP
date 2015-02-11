/*
 * LoginService.java 23 Jul 2007
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


/**.
 * Defines login operations a user can perform. It is up to the implementing
 * class to determine how the user will be logged in and to work out if the
 * supplied login details are invalid.
 * @author miroslav.miladinovic
 */
public interface LoginService {

    /**.
     * Logs a user into some part of the AirCell solution
     * @param loginDetails User's login details
     * @return the <code>ServiceResponse</code> object indicating outcome of the
     *         call
     * @throws IllegalArgumentException If the supplied LoginDetails are null or
     * invalid.
     */
    ServiceResponse login(LoginDetails loginDetails)
    throws IllegalArgumentException;

}
