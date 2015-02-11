/*
 * JmsATGSessionService.java 14 Aug 2007
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
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.abs.acpu.common.SubscriptionStartRequestData;
import com.aircell.abs.acpu.common.SubscriptionStartResponseData;
import org.apache.commons.lang.StringUtils;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class JmsATGSessionServiceTest extends MockObjectTestCase {

    private JmsATGSessionService underTest;
    private JmsSynchOperations jmsSyncServiceMock;
    private JmsTemplateMock jmsTemplateMock;
    private final String endSessionQueueName = "queue/endSession";
    private final String endSessionMsgPropertyKey = "Message";
    private final String endSessionMsgPropertyValue = "Terminate-Session";
    private final String usernameSuccess = "successUsername";
    private final String passwordSuccess = "passwordSuccess";
    private final String ipAddressSuccess = "ipAddressSuccess";
    private final String usernameFailure = "failureUsername";
    private final String passwordFailure = "passwordFailure";
    private final String ipAddressFailure = "ipAddressFailure";

    private static final String SUBSCRIPTION_REQUEST_KEY = "Message";
    private static final String SUBSCRIPTION_REQUEST_VALUE =
    "SubscriptionRequest";

    private SubscriptionStartRequestData requestForSuccess;
    private SubscriptionStartRequestData requestForFailure;
    private SubscriptionStartResponseData responseSuccess;
    private SubscriptionStartResponseData responseFailure;

    protected void setUp() throws Exception {
        super.setUp();
        underTest = new JmsATGSessionService();
        jmsTemplateMock = new JmsTemplateMock();
        jmsSyncServiceMock = mock(JmsSynchOperations.class);

        underTest.setEndATGSessionQueueName(endSessionQueueName);
        underTest.setEndATGSessionMsgPropertyKey(endSessionMsgPropertyKey);
        underTest.setEndATGSessionMsgPropertyValue(endSessionMsgPropertyValue);
        underTest.setJmsSyncService(jmsSyncServiceMock);
        underTest.setJmsTemplate(jmsTemplateMock);

        requestForSuccess = new MockSubscriptionStartRequestData(
        ipAddressSuccess, usernameSuccess, passwordSuccess
        );
        requestForFailure = new MockSubscriptionStartRequestData(
        ipAddressFailure, usernameFailure, passwordFailure
        );

        responseSuccess = new SubscriptionStartResponseData(
        ipAddressSuccess, usernameSuccess, true
        );

        responseFailure = new SubscriptionStartResponseData(
        ipAddressFailure, usernameFailure, false, AcpuErrorCode.ATG_LINK_DOWN,
        "the atg link is down"
        );
    }

    /**.
     * Tests that that the <code>testCheckDependenciesSet</code> method throws
     * <code>java.lang.IllegalArgumentException</code>
     * if dependencies of this bean
     * have not been set correctly
     */
    public void testCheckDependenciesSet() {
        underTest.setJmsTemplate(null);
        underTest.setEndATGSessionQueueName(null);
        underTest.setEndATGSessionMsgPropertyKey(null);
        underTest.setEndATGSessionMsgPropertyValue(null);
        underTest.setJmsSyncService(null);

        try {
            underTest.checkDependenciesSet();
            fail(
            "IllegalArgumentException for jmsSyncService expected at this point"
            );
        } catch (IllegalArgumentException expected) {
        }

        underTest.setJmsSyncService(jmsSyncServiceMock);

        try {
            underTest.checkDependenciesSet();
            fail(
            "IllegalArgumentException for jmsTemplate expected at this point"
            );
        } catch (IllegalArgumentException expected) {
        }

        underTest.setJmsTemplate(jmsTemplateMock);

        try {
            underTest.checkDependenciesSet();
            fail("IllegalArgumentException for Qname expected at this point");
        } catch (IllegalArgumentException expected) {
        }

        underTest.setEndATGSessionQueueName("QNAME");

        try {
            underTest.checkDependenciesSet();
            fail(
            "IllegalArgumentException for property key expected at this point"
            );
        } catch (IllegalArgumentException expected) {
        }

        underTest.setEndATGSessionMsgPropertyKey("KEY");

        try {
            underTest.checkDependenciesSet();
            fail(
            "IllegalArgumentException for property value expected at this point"
            );
        } catch (IllegalArgumentException expected) {
        }

        underTest.setEndATGSessionMsgPropertyValue("VALUE");

        try {
            underTest.checkDependenciesSet();
        } catch (IllegalArgumentException expected) {
            fail("IllegalArgumentException NOT expected at this point");
        }

    }

    /**. Tests for invalid arguments passed into the method */
    public void testSendStartATGSessionMsgInvalidArguments() {
        try {
            underTest.sendStartATGSessionMsg(null, " ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    /**.
     * Tests that when a subscritpion is started
     * successfully the success response
     * message is received back.
     * the message received back should not be altered by the
     * <code>sendStartATGSessionMsg</code> method.
     */
    public void testSendStartATGSessionMsgSuccessfullMsg() {
        checking(
        new Expectations() {{
            {
                one(jmsSyncServiceMock).exchangeObjMsgOverTempQueue(
                with(equal(requestForSuccess)),
                with(equal(SUBSCRIPTION_REQUEST_KEY)),
                with(equal(SUBSCRIPTION_REQUEST_VALUE))
                );
                will(returnValue(responseSuccess));
            }
        }}
        );
        final LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername(usernameSuccess);
        loginDetails.setPassword(passwordSuccess);
        final ServiceResponse response =
        underTest.sendStartATGSessionMsg(loginDetails, ipAddressSuccess);
        assertNotNull(
        "sendStartATGSessionMsg method should never return null", response
        );
        assertTrue("success status expected", response.isSuccess());
    }

    public void testSendStartATGSessionMsgUnssuccessfullMsg() {
        checking(
        new Expectations() {{
            {
                one(jmsSyncServiceMock).exchangeObjMsgOverTempQueue(
                with(equal(requestForFailure)),
                with(equal(SUBSCRIPTION_REQUEST_KEY)),
                with(equal(SUBSCRIPTION_REQUEST_VALUE))
                );
                will(returnValue(responseFailure));
            }
        }}
        );
        final LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername(usernameFailure);
        loginDetails.setPassword(passwordFailure);

        final ServiceResponse response =
        underTest.sendStartATGSessionMsg(loginDetails, ipAddressFailure);
        assertNotNull(
        "sendStartATGSessionMsg method should never return null", response
        );
        assertFalse("failure status expected", response.isSuccess());
    }

    /**.
     * test that even if the JMS service
     * returns the response message with the
     * object payload of the incorrect type,
     * our subscriptionService does not throw
     * an exception nor returns null. it should
     * return the response object with the
     * status set to <code>false</code>
     */
    public void testSendStartATGSessionMsgWrongResponseObjectTypeReturned() {
        checking(
        new Expectations() {{
            {
                one(jmsSyncServiceMock).exchangeObjMsgOverTempQueue(
                with(any(SubscriptionStartRequestData.class)),
                with(any(String.class)), with(any(String.class))
                );
                // return incorrect type of response message object payload
                will(returnValue(Integer.valueOf(100)));
            }
        }}
        );
        final LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername(usernameSuccess);
        loginDetails.setPassword(passwordSuccess);
        final ServiceResponse response =
        underTest.sendStartATGSessionMsg(loginDetails, ipAddressSuccess);
        assertNotNull(
        "sendStartATGSessionMsg method should never return null "
        + "even if the jms response message contains a payload object of "
        + "incorrect type", response
        );
        assertFalse(
        "the status returned should be false", response.isSuccess()
        );

    }

    /**.
     * test that even if the JMS service
     * returns the response message with the null
     * object payload, our subscriptionService
     * does not throw an exception nor
     * returns null. it should return the response
     * object with the status set to
     * <code>false</code>
     */
    public void testSendStartATGSessionMsgNoResponseObjectReturned() {
        checking(
        new Expectations() {{
            {
                one(jmsSyncServiceMock).exchangeObjMsgOverTempQueue(
                with(any(SubscriptionStartRequestData.class)),
                with(any(String.class)), with(any(String.class))
                );
                will(returnValue(null));
            }
        }}
        );
        final LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername(usernameSuccess);
        loginDetails.setPassword(passwordSuccess);
        final ServiceResponse response =
        underTest.sendStartATGSessionMsg(loginDetails, ipAddressSuccess);
        assertNotNull(
        "sendStartATGSessionMsg method should never return null", response
        );
        assertFalse(
        "the status returned should be false", response.isSuccess()
        );
    }

    /**.
     * Tests the that the sendEndATGSessionMsg method cannto take null or emtpy
     * string args
     */
    public void testSendEndATGSessionMsgInvalidArgs() {
        try {
            underTest.sendEndATGSessionMsg(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertTrue(true); // expected
        }
    }

    public void testSendEndATGSessionMsgSuccessMsg() {
        underTest.sendEndATGSessionMsg(ipAddressSuccess);
        assertEquals(jmsTemplateMock, underTest.getJmsTemplate());
        assertEquals(
        "the queue name the end session text message "
        + "sent to should be the same one as the on set up on "
        + "the subscription service object", endSessionQueueName,
        jmsTemplateMock.destinationNameUsed
        );
        assertNotNull(jmsTemplateMock.messageCreatorUsed);
        assertTrue(
        "JmsTextMessageCreator should have been used. "
        + "Change the test if otherwise",
        jmsTemplateMock.messageCreatorUsed instanceof JmsTextMessageCreator
        );
        final JmsTextMessageCreator creator =
        (JmsTextMessageCreator) jmsTemplateMock.messageCreatorUsed;
        // ideally this test should not be dependent on JmsTextMessageCreator
        // but we have to test to make sure
        // that the payload and message properties
        // are passed down from the service to the message creator
        assertEquals(
        "the payload specified shuold have "
        + "been passed down to the message creator", ipAddressSuccess,
        creator.getPayload()
        );

        assertEquals(
        "the text message property key should have "
        + "been passed down to the message creator", endSessionMsgPropertyKey,
        creator.getPropertyKey()
        );
        assertEquals(
        "the text message property value should have"
        + "been passed down to the message creator", endSessionMsgPropertyValue,
        creator.getPropertyValue()
        );

    }

    // ****************************************************************
    // *** Inner classes for support of this test case
    // ****************************************************************
    public class JmsTemplateMock extends JmsTemplate {
        public String destinationNameUsed;
        public MessageCreator messageCreatorUsed;

        @Override
        public void send(
        final String destinationName, final MessageCreator messageCreator
        ) throws JmsException {
            this.destinationNameUsed = destinationName;
            this.messageCreatorUsed = messageCreator;
        }

    }

    @SuppressWarnings("all")
    public class MockSubscriptionStartRequestData
    extends SubscriptionStartRequestData {

        public MockSubscriptionStartRequestData(
        String ipAddress, String username, String password
        ) {
            super(ipAddress, username, password);
        }

        /* (non-Javadoc)
           * @see java.lang.Object#equals(java.lang.Object)
           */
        @Override
        public boolean equals(final Object obj) {
            boolean retval = false;
            if (obj instanceof SubscriptionStartRequestData) {
                final SubscriptionStartRequestData that =
                (SubscriptionStartRequestData) obj;
                retval =
                StringUtils.equals(this.getUsername(), that.getUsername())
                && StringUtils.equals(this.getPassword(), that.getPassword())
                && StringUtils.equals(this.getIpAddress(), that.getIpAddress())
                && StringUtils.equals(this.getDomain(), that.getDomain());

            }
            return retval;
        }
    }

}
