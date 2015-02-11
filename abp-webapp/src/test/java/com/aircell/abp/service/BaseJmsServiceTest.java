/*
 * BaseJmsServiceTest.java 13 Aug 2007
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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.io.Serializable;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class BaseJmsServiceTest extends MockObjectTestCase {

    private final long jmsReceiveTimeout = 100;

    private JmsSyncOperator underTest;
    private ConnectionFactory connectionFactoryMock;
    private Connection connectionMock;
    private Session sessionMock;
    private Destination destinationMock;
    private TemporaryQueue tempQueueMock;

    private MessageConsumer messageConsumerMock;
    private MessageProducer messageProducerMock;
    private ObjectMessage responseMessageMock;
    private ObjectMessage requestMessageMock;

    protected void setUp() {
        underTest = new JmsSyncOperator();
        connectionFactoryMock = mock(ConnectionFactory.class);
        connectionMock = mock(Connection.class);
        sessionMock = mock(Session.class);
        tempQueueMock = mock(TemporaryQueue.class);
        messageConsumerMock = mock(MessageConsumer.class);
        messageProducerMock = mock(MessageProducer.class);
        responseMessageMock = mock(ObjectMessage.class, "response");
        requestMessageMock = mock(ObjectMessage.class, "request");
        destinationMock = mock(Destination.class);

        underTest.setConnectionFactory(connectionFactoryMock);
        underTest.setDestination(destinationMock);
        underTest.setReceiveTimeout(jmsReceiveTimeout);
    }

    /**.
     * Tests that if the JMS queue returns an
     * <code>ObjectMessage</code> with the
     * null payload, no exceptions are thrown
     * and that a null object is returned.
     * @throws JMSException
     */
    public void testExchangeObjMsgOverTempQueueResponseMessageObjectNull()
    throws JMSException {
        // make the MessageConsumer return an ObjectMessage with null payload
        checking(
        new Expectations() {{
            {
                allowing(connectionFactoryMock).createConnection();
                will(returnValue(connectionMock));
                ignoring(connectionFactoryMock);
            }
            {
                allowing(connectionMock)
                .createSession(with(any(boolean.class)), with(any(int.class)));
                will(returnValue(sessionMock));
                ignoring(connectionMock);
            }
            {
                allowing(sessionMock)
                .createConsumer(with(any(TemporaryQueue.class)));
                will(returnValue(messageConsumerMock));
                ignoring(sessionMock);
            }
            {
                one(messageConsumerMock).receive(with(any(int.class)));
                will(returnValue(responseMessageMock));
                ignoring(messageConsumerMock);
            }
            {
                one(responseMessageMock).getObject();
                will(returnValue(null));
            }
        }}
        );

        final Serializable retval = underTest.exchangeObjMsgOverTempQueue(null);
        assertNull(retval);
    }

    /**.
     * Tests that the JMS <code>Connection</code> and <code>Session</code> have
     * been closed whatever happens in the method.
     * @throws JMSException
     */
    public void testExchangeObjMsgOverTempQueueClosesResources()
    throws JMSException {
        // make the responseMessageMock.getObject() throw an Exception
        // and then check that the connection and session have been closed.
        checking(
        new Expectations() {{
            {
                allowing(connectionFactoryMock).createConnection();
                will(returnValue(connectionMock));
            }
            {
                allowing(connectionMock)
                .createSession(with(any(boolean.class)), with(any(int.class)));
                will(returnValue(sessionMock));
            }
            {
                allowing(connectionMock).start();
                allowing(connectionMock).stop();
            }
            {
                allowing(sessionMock)
                .createConsumer(with(any(TemporaryQueue.class)));
                will(returnValue(messageConsumerMock));
            }
            {
                allowing(sessionMock).createTemporaryQueue();
                allowing(sessionMock)
                .createObjectMessage(with(any(Serializable.class)));
                allowing(sessionMock)
                .createProducer(with(any(Destination.class)));
            }
            {
                one(messageConsumerMock).receive(with(any(int.class)));
                will(returnValue(responseMessageMock));
            }
            {
                one(responseMessageMock).getObject();
                final RuntimeException e = new RuntimeException("oh dear");
                will(throwException(e));
            }
            // the following two ensure that the close()
            //  methods on session and connection have been called
            {
                one(messageConsumerMock).close();
            }
            {
                one(connectionMock).close();
            }
            {
                one(sessionMock).close();
            }
        }}
        );
        try {
            underTest.exchangeObjMsgOverTempQueue(null);
        } catch (Exception expected) {
        }
    }

    /**.
     * tests that the payload object is correctly passed down do the
     * <code>MessageProducer</code> in the <code>ObjectMessage</code>
     */
    public void
    testExchangeObjMsgOverTempQueuePayloadObjectPassedToTheRequestMsgOk()
    throws JMSException {

        final Integer samplePayloadRequest = 1;
        final Integer samplePayloadResponse = 99;
        checking(
        new Expectations() {{
            {
                allowing(connectionFactoryMock).createConnection();
                will(returnValue(connectionMock));
                ignoring(connectionFactoryMock);
            }
            {
                allowing(connectionMock)
                .createSession(with(any(boolean.class)), with(any(int.class)));
                will(returnValue(sessionMock));
                ignoring(connectionMock).start();
                ignoring(connectionMock).stop();
                ignoring(connectionMock).close();
            }
            {
                one(sessionMock).createConsumer(with(equal(tempQueueMock)));
                will(returnValue(messageConsumerMock));
            }
            {
                one(sessionMock).createTemporaryQueue();
                will(returnValue(tempQueueMock));
            }
            {
                one(sessionMock).createProducer(with(equal(destinationMock)));
                will(returnValue(messageProducerMock));
            }
            {
                one(sessionMock)
                .createObjectMessage(with(equal(samplePayloadRequest)));
                will(returnValue(requestMessageMock));
                ignoring(sessionMock).close();
            }
            {
                one(requestMessageMock)
                .setJMSReplyTo(with(equal(tempQueueMock)));
            }
            {
                one(messageProducerMock).send(with(equal(requestMessageMock)));
            }
            {
                one(responseMessageMock).getObject();
                will(returnValue(samplePayloadResponse));
            }
            {
                one(messageConsumerMock)
                .receive(with(equal(jmsReceiveTimeout)));
                will(returnValue(responseMessageMock));
                ignoring(messageConsumerMock);
            }
            {
                one(tempQueueMock).delete();
            }
        }}
        );

        final Serializable retval =
        underTest.exchangeObjMsgOverTempQueue(samplePayloadRequest);

        assertNotNull(
        "the exchangeObjMsgOverTempQueue should "
        + "not have returned null as we've asked it to return "
        + "a payload object.", retval
        );

        assertEquals(
        "the payload of the response " + "message received should be the same "
        + "as set up in the expectations.", samplePayloadResponse, retval
        );
    }

    /**. Tests that the JMS string properties
     * have been set on the message ok. */
    public void testExchangeObjMsgOverTempQueuePropertiesSetOk()
    throws JMSException {
        final String msgPropertyKey = "Message";
        final String msgPropertyValue = "AuthenticationMessage";
        checking(
        new Expectations() {{
            {
                allowing(connectionFactoryMock).createConnection();
                will(returnValue(connectionMock));
                ignoring(connectionFactoryMock);
            }
            {
                allowing(connectionMock)
                .createSession(with(any(boolean.class)), with(any(int.class)));
                will(returnValue(sessionMock));
                ignoring(connectionMock);
            }
            {
                one(sessionMock)
                .createObjectMessage(with(any(Serializable.class)));
                will(returnValue(requestMessageMock));
                ignoring(sessionMock);
            }
            {
                one(requestMessageMock).setStringProperty(
                with(equal(msgPropertyKey)), with(equal(msgPropertyValue))
                );
                ignoring(requestMessageMock);
            }
        }}
        );
        // the main point of this test is that the msgPropertyKey and
        // msgPropertyValue have been set correctly.
        final Serializable retval = underTest.exchangeObjMsgOverTempQueue(
        "whatever", msgPropertyKey, msgPropertyValue
        );

        assertNull(
        "the exchangeObjMsgOverTempQueue should "
        + "have returned null as we've asked it to return "
        + "through not setting response message expectations.", retval
        );
    }


    /**.
     * tests that the init method of this bean throws
     * <code>IllegalArgumentException</code>
     * if not all dependencies of this bean have been set.
     */
    public void testCheckDependenciesSet() {
        // check the following properties:
        // - connectionFactory
        // - destination
        underTest.setConnectionFactory(null);
        try {
            underTest.checkDependenciesSet();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }

        underTest.setDestination(null);
        try {
            underTest.checkDependenciesSet();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }
}
