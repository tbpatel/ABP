/*
 * TechnicalErrorControllerTest.java 24 Dec 2008
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent are
 * strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */


package com.aircell.abp.web.controller;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**.
 * Unit test for <code>TechnicalErrorController</code> Specifically, this is to
 * check if the ModelView Return is null or not .
 * @author madhusudan.
 */
public class TechnicalErrorControllerTest extends MockObjectTestCase {
    /**. . HttpServletRequest */
    private HttpServletRequest mockRequest;
    /**. . HttpServletResponse */
    private HttpServletResponse mockResponse;
    /**. . TechnicalErrorController */
    private TechnicalErrorController underTest;
    /**. . ModelAndView */
    private ModelAndView mv;

    /**.
     * . Override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        underTest = new TechnicalErrorController();
        underTest.setAbpWebappHome("abpWebappHome");
        underTest.setGbpWebappHome("gbpWebappHome");
        underTest.getAbpWebappHome();
        underTest.getGbpWebappHome();
    }

    /**. . To test */
    public void testhandlerreturnnotnull() {

        mv = underTest.handler(mockRequest, mockResponse);
        assertNotNull(mv);
    }

}
