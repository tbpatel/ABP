package com.aircell.abp.service;

import com.aircell.abp.model.LoginDetails;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

import javax.jms.JMSException;


public class JmsLoginServiceTest extends TestCase {

    @SuppressWarnings("all")
    private class MockAuthRequestData extends AuthRequestData {
        public MockAuthRequestData(String s, String s1, String s2) {
            super(s, s1, s2);
        }

        public boolean equals(Object object) {
            if (object instanceof AuthRequestData) {
                AuthRequestData data = (AuthRequestData) object;
                return getUsername().equals(data.getUsername())
                && getPassword().equals(data.getPassword()) && getIpAddress()
                .equals(data.getIpAddress());
            }
            return false;
        }
    }

    private JmsLoginService underTest;
    private final String successUsername = "success";
    private final String failureUsername = "failure";
    private final String password = "password";
    private final String ipAddress = "ip-address";
    private JmsSynchOperations jmsService;
    private static final String SERVICE_MESSAGE_KEY = "Message";
    private static final String SERVICE_MESSAGE_VALUE = "AuthenticationRequest";

    private Mockery context;

    public static Test suite() {
        return new TestSuite(JmsLoginServiceTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void setUp() throws JMSException {
        underTest = new JmsLoginService();
        context = new Mockery();
        jmsService = context.mock(JmsSynchOperations.class);
    }

    public void tearDown() {
        context.assertIsSatisfied();
        underTest = null;
    }

    private void setupExpectations(final boolean successPath)
    throws JMSException {
        final AuthResponseData responseData =
        new AuthResponseData("", "", false);
        if (successPath) {
            responseData.setStatus(true);
        } else {
            responseData.setStatus(false);
            responseData.setErrorCode(AcpuErrorCode.AUTHENTICATION_REJECTED);
        }

        final MockAuthRequestData authRequestData =
        new MockAuthRequestData("", "", "");
        authRequestData.setPassword(password);
        authRequestData.setIpAddress(ipAddress);
        if (successPath) {
            authRequestData.setUsername(successUsername);
        } else {
            authRequestData.setUsername(failureUsername);
        }
        authRequestData.setPassword(password);
        authRequestData.setIpAddress(ipAddress);

        context.checking(
        new Expectations() {
            {
                allowing(jmsService).exchangeObjMsgOverTempQueue(
                with(equal(authRequestData)), with(equal(SERVICE_MESSAGE_KEY)),
                with(equal(SERVICE_MESSAGE_VALUE))
                );
                will(returnValue(responseData));
            }        }
        );

        underTest.setJmsSyncService(jmsService);
    }

    public void testSuccessfulLogin() throws JMSException {
        setupExpectations(true);

        final LoginDetails loginDetails =
        new LoginDetails(successUsername, password);
        loginDetails.setIpAddress(ipAddress);

        final ServiceResponse loginResult = underTest.login(loginDetails);
        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
    }


    public void testFailedLogin() throws JMSException {
        setupExpectations(false);

        final LoginDetails loginDetails =
        new LoginDetails(failureUsername, password);
        loginDetails.setIpAddress(ipAddress);

        final ServiceResponse loginResult = underTest.login(loginDetails);
        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
        assertEquals(
        AcpuErrorCode.AUTHENTICATION_REJECTED, loginResult.getErrorCode()
        );
    }

    public void testCheckDependenciesSet() {
        underTest.setJmsSyncService(jmsService);
        try {
            underTest.checkDependenciesSet();
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was NOT expected");
        }

        // the jmsLoginService property
        underTest.setJmsSyncService(null);
        try {
            underTest.checkDependenciesSet();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        } finally {
            underTest.setJmsSyncService(jmsService);
        }

    }

}



