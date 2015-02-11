package com.aircell.abp.model;

import com.aircell.abp.service.ATGSessionService;
import com.aircell.abp.service.LoginService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.SessionInfoService;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;


public class AirPassengerTest extends MockObjectTestCase {

    /**. private class variable to hold object
     * reference of the class to be tested */
    private AirPassenger underTest;
    /**. private class variable to hold mocked LoginService object */
    private LoginService loginServiceMock;
    /**. private class variable to hold mocked ATGSessionService object */
    private ATGSessionService sessionServiceMock;
    /**. private class variable to hold mocked SessionInfoService object */
    private SessionInfoService sessionInfoServiceMock;

    /**. private class variable to hold ServiceResponse */
    private ServiceResponse loginOkResponse;
    /**. private class variable to hold invalid ServiceResponse  */
    private ServiceResponse invalidDetailsResponse;
    /**. private class variable to hold USERNAME */
    private final String USERNAME = "joebloggs";
    /**. private class variable to hold invalid */
    private final String PASSWORD = "snail";
    /**. private class variable to hold IP_ADDRESS */
    private final String IP_ADDRESS = "10.2.30.100";

    protected void setUp() throws Exception {
        super.setUp();
        loginServiceMock = mock(LoginService.class);
        sessionServiceMock = mock(ATGSessionService.class);
        sessionInfoServiceMock = mock(SessionInfoService.class);

        underTest =
        new AirPassenger(new LoginDetails(USERNAME, PASSWORD), IP_ADDRESS);
        underTest.setLoginService(loginServiceMock);
        underTest.setAtgSessionService(sessionServiceMock);
        underTest.setSessionInfoService(sessionInfoServiceMock);

        loginOkResponse = new ServiceResponse(true);

        invalidDetailsResponse = new ServiceResponse(false);
        invalidDetailsResponse
        .setErrorCode(AcpuErrorCode.AUTHENTICATION_REJECTED);

    }

    public void testLoginHappyPath() {
        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);
        loginDetails.setIpAddress(IP_ADDRESS);

        checking(
        new Expectations() {{
            one(loginServiceMock).login(loginDetails);
            will(returnValue(loginOkResponse));
        }}
        );

        final ServiceResponse retval = underTest.login();
        assertTrue(retval.isSuccess());

    }

    public void testLoginInvalidUser() {
        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);
        loginDetails.setIpAddress(IP_ADDRESS);

        checking(
        new Expectations() {{
            one(loginServiceMock).login(loginDetails);
            will(returnValue(invalidDetailsResponse));
        }}
        );

        final ServiceResponse retval = underTest.login();
        assertFalse(retval.isSuccess());
        assertEquals(
        AcpuErrorCode.AUTHENTICATION_REJECTED, retval.getErrorCode()
        );
    }

    public void testStartAtgHappy() {
        final UserFlightSession mockSession = new UserFlightSession();
        mockSession.setActivated(false);
        mockSession.setAuthenticated(false);

        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);
        loginDetails.setIpAddress(IP_ADDRESS);

        checking(
        new Expectations() {{
            one(sessionInfoServiceMock).getSession(USERNAME, IP_ADDRESS);
            will(returnValue(mockSession));
            one(loginServiceMock).login(loginDetails);
            will(returnValue(loginOkResponse));
            one(sessionServiceMock).sendStartATGSessionMsg(
            new LoginDetails(USERNAME, PASSWORD), IP_ADDRESS
            );
            will(returnValue(new ServiceResponse(true)));
        }}
        );

        ServiceResponse result = underTest.startATGSession(true);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    public void testEndAtgHappy() {
        final UserFlightSession mockSession = new UserFlightSession();
        mockSession.setActivated(true);

        checking(
        new Expectations() {{
            one(sessionInfoServiceMock).getSession(USERNAME, IP_ADDRESS);
            will(returnValue(mockSession));
            one(sessionServiceMock).sendEndATGSessionMsg(IP_ADDRESS);
            will(returnValue(new ServiceResponse(true)));
        }}
        );
        try {
            underTest.endATGSession();
        } catch (Exception e) {
            fail();
        }
    }
}

