package com.aircell.abp.service;

import com.aircell.abp.model.UserFlightSession;
import com.aircell.abs.acpu.common.SessionInfo;
import com.aircell.abs.acpu.common.SessionInfoRequest;
import com.aircell.abs.acpu.common.SessionStatusCodes;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;


public class SessionInfoTest extends TestCase {

    public void testHappyGetSessionInfo() {
        SessionInfoJmsImpl t = new SessionInfoJmsImpl();

        Mockery context = new Mockery();

        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);
        final SessionInfo mockReturn = new SessionInfo(
        "ip", "user", SessionStatusCodes.USER_AUTHENTICATED);//, 0, 0, 1800);
        final SessionInfoRequest mockRequest =
        new SessionInfoRequestWithEquals("ip", "user");

        //set dependencies
        t.setSessionInfoJmsPropertyKey("Message");
        t.setSessionInfoJmsPropertyValue("value");
        t.setJmsSyncService(mockSyncService);

        t.checkDependenciesSet();


        context.checking(
        new Expectations() {{
            try {
                one(mockSyncService)
                .exchangeObjMsgOverTempQueue(mockRequest, "Message", "value");
                will(returnValue(mockReturn));
            } catch (Exception e) {
            }
        }}
        );

        //invoking method under test
        UserFlightSession response = t.getSession("user", "ip");

        assertNotNull(response);
        assertEquals(Boolean.TRUE, response.isAuthenticated());
        assertEquals("user", response.getUserName());
    }


    public void testExceptionThrownSessionInfo() {
        SessionInfoJmsImpl t = new SessionInfoJmsImpl();

        Mockery context = new Mockery();

        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);
        final SessionInfoRequest mockRequest =
        new SessionInfoRequestWithEquals("ip", "user");

        //set dependencies
        t.setSessionInfoJmsPropertyKey("Message");
        t.setSessionInfoJmsPropertyValue("value");
        t.setJmsSyncService(mockSyncService);

        t.checkDependenciesSet();

        context.checking(
        new Expectations() {{
            try {
                allowing(mockSyncService)
                .exchangeObjMsgOverTempQueue(mockRequest, "Message", "value");
                will(throwException(new ServiceException("An exception")));
            } catch (Exception e) {
            }
        }}
        );
        UserFlightSession response = null;
        //invoking method under test
        try {
            response = t.getSession("user", "ip");
        } catch (ServiceException e) {
            assertNotNull(e);
        }
        assertNull(response);

    }

    class SessionInfoRequestWithEquals extends SessionInfoRequest {
        SessionInfoRequestWithEquals(String ip, String user) {
            super(ip, user);
        }

        public boolean equals(Object o) {
            if (
            ((SessionInfoRequest) o).getIpAddress().equals(this.getIpAddress())
            && ((SessionInfoRequest) o).getUserName()
            .equals(this.getUserName())) {
                return true;
            }

            return false;

        }

    }
}
