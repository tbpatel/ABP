/*
 * JmsTextMessageCreatorTest.java 16 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**. @author miroslav.miladinovic at AKQA Inc. */

public class JmsTextMessageCreatorTest extends MockObjectTestCase {

    private JmsTextMessageCreator underTest;
    private Session sessionMock;
    private TextMessage textMessageMock;
    private final String ipAddresssPayload = "127.0.0.1";
    private final String propertyKeyToSet = "Message";
    private final String propertyValueToSet = "TerminateUserSession";

    protected void setUp() throws Exception {
        super.setUp();
        sessionMock = mock(Session.class);
        textMessageMock = mock(TextMessage.class);
    }

    public void testCreateMessageWithNullArguments() throws JMSException {
        underTest = new JmsTextMessageCreator("test");
        try {
            underTest.createMessage(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testCreateMessageWithNoPropertyToSet() throws JMSException {
        underTest = new JmsTextMessageCreator(ipAddresssPayload);
        checking(
        new Expectations() {{
            {
                one(sessionMock).createTextMessage();
                will(returnValue(textMessageMock));
            }
            {
                one(textMessageMock).setText(with(equal(ipAddresssPayload)));
            }
        }}
        );

        final Message msgCreated = underTest.createMessage(sessionMock);
        assertNotNull("msg created should not be null.", msgCreated);
        assertEquals(
        "message created object should be the same " + "as the one mocked up.",
        textMessageMock, msgCreated
        );

    }

    public void testCreateMessageWithPropertyToSet() throws JMSException {
        underTest = new JmsTextMessageCreator(
        ipAddresssPayload, propertyKeyToSet, propertyValueToSet
        );
        checking(
        new Expectations() {{
            {
                one(sessionMock).createTextMessage();
                will(returnValue(textMessageMock));
            }
            {
                one(textMessageMock).setText(with(equal(ipAddresssPayload)));
                one(textMessageMock).setStringProperty(
                with(equal(propertyKeyToSet)), with(equal(propertyValueToSet))
                );
            }
        }}
        );

        final Message msgCreated = underTest.createMessage(sessionMock);
        assertNotNull("msg created should not be null.", msgCreated);
        assertEquals(
        "message created object should be the same " + "as the one mocked up.",
        textMessageMock, msgCreated
        );
    }

}
