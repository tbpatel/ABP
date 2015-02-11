/*
 * CreditCardDetailsTest.java 21 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Calendar;

/**.
 * Unit tests for the CreditCardDetails object
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class CreditCardDetailsTest extends MockObjectTestCase {
    /**. The CreditCardDetails object to perform tests on */
    private CreditCardDetails details;

    /**. @see junit.framework.TestCase#setUp()
     * @throws Exception Exception */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**. @see junit.framework.TestCase#tearDown()
     * @throws Exception Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsExpired() {
        details = new CreditCardDetails();
        details.setExpiryMonth(Integer.toString(12));
        details.setExpiryYear(2039);
        assertFalse("The card should not be expired", details.isExpired());
        details.setExpiryMonth(Integer.toString(5));
        details.setExpiryYear(2006);
        assertTrue("The card should be expired", details.isExpired());
        Calendar now = Calendar.getInstance();
        details.setExpiryMonth(
        Integer.toString(now.get(Calendar.MONTH))
        + CreditCardDetails.MONTH_JAVA_OFFSET
        );
        details.setExpiryYear(now.get(Calendar.YEAR));
        assertFalse("The card should not be expired", details.isExpired());
        details.setExpiryMonth(Integer.toString(now.get(Calendar.MONTH)));
        details.setExpiryYear(now.get(Calendar.YEAR));
        assertTrue("The card should be expired", details.isExpired());
    }

    public void testParseExpiry() {
        details = new CreditCardDetails();
        details.parseExpiryDate("0907");
        //assertEquals(new Integer(9),details.getExpiryMonth());
        // assertEquals(new Integer(7),details.getExpiryYear());
        try {
            details.parseExpiryDate("LLKK");
            fail();
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
}
