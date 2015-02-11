/*
 * JmsLoginServiceIntegrationTest.java 19 Sep 2007
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
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**. @author miroslav.miladinovic at AKQA Inc. */

public class JmsLoginServiceIntegrationTest
extends AbstractDependencyInjectionSpringContextTests {

    private LoginService loginService;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IP_ADDRESS = "213.213.212.132";

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public void testLoginToAir() {

        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);
        loginDetails.setIpAddress(IP_ADDRESS);

        final ServiceResponse response = loginService.login(loginDetails);

        assertTrue(response.isSuccess());
    }


    @Override
    protected String[] getConfigLocations() {
        return new String[]{
        "com/aircell/abp/service/login-jms-int-test-spring-cfg.xml"
        };
    }

}
