package com.aircell.abp.model;

import com.aircell.abp.model.AircellUser.State;
import com.aircell.abp.model.SecurityChallengeDetails.SecurityChallengeType;
import com.aircell.abp.service.BSSErrorCode;
import com.aircell.abp.service.LoginService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.UserDetailsService;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.bss.ws.TrackingType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

/**.
 * Tests the Aircell domain object with focus on its state behaviour
 * @author miroslav.miladinovic
 */
public class AircellUserTest extends MockObjectTestCase {
    @SuppressWarnings("all")
    private class MockSecurityChallengeDetails
    extends SecurityChallengeDetails {
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SecurityChallengeDetails) {
                final SecurityChallengeDetails that =
                (SecurityChallengeDetails) obj;
                return (this.getId() == that.getId())
                && (StringUtils.equals(this.getQuestion(), that.getQuestion()))
                && (StringUtils.equals(this.getAnswer(), that.getAnswer()));
            } else {
                return false;
            }

        }
    }

    /**. private class variable to hold emali address */
    private static final String EMAIL_ADDRESS = "test@akqa.com";
    /**. private class variable to hold USERNAME */
    private static final String USERNAME = "joebloggs";
    /**. private class variable to hold PASSWORD */
    private static final String PASSWORD = "snail";
    /**. private class variable to hold IP_ADDRESS */
    private static final String IP_ADDRESS = "10.2.30.100";
    /**. private class variable to hold TS_CS_VERSION */
    private static final String TS_CS_VERSION = "VER1";
    /**. private class variable to hold MARKETING */
    private static final boolean MARKETING = false;
    /**. private class variable to hold FIRSTNAME */
    private static final String FIRSTNAME = "firstName";
   
    /*private static final String MIDDLENAME = "middleName";*/
    /**. private class variable to hold LASTNAME */
    private static final String LASTNAME = "lastName";
    /**. private class variable to hold TITLE */
    private static final String TITLE = "Mr";
    /**. private class variable to hold PHONE_NUMBER */
    private static final String PHONE_NUMBER = "0898272727";
    /**. private class variable to hold ZIP_CODE */
    private static final String ZIP_CODE = "11111";
    /**. private class variable to hold CCNUMBER */
    private static final String CCNUMBER = "1234";
    /**. private class variable to hold BLANK_STRING */
    private static final String BLANK_STRING = "";
    /**. private class variable to hold locale */
    private static final String locale = "en_US";

    /**. private class variable to hold loginDetails object */
    private LoginDetails loginDetails;
    /**. private class variable to hold personalDetails object */
    private PersonalDetails personalDetails;
    /**. private class variable to hold addressDetails object */
    private AddressDetails addressDetails;
    /**. private class variable to hold tracking */
    private TrackingType tracking;

    /**. private class variable to hold object of the class to be tested */
    private AircellUser underTest;

    /**. private class variable to hold mocked LoginService */
    private LoginService loginServiceMock;
    /**. private class variable to hold mocked UserDetailsService */
    private UserDetailsService userDetailsServiceMock;
    /**. private class variable to hold ServiceResponse */
    private ServiceResponse loginOkResponse;
    /**. private class variable to hold  invalid ServiceResponse */
    private ServiceResponse invalidDetailsResponse;
    /**. private class variable to hold ServiceResponse */
    private ServiceResponse termsChangedResponse;
    /**. private class variable to hold SecurityChallengeDetails for username */
    private SecurityChallengeDetails usernameChallenge;
    /**. private class variable to hold SecurityChallengeDetails for password */
    private SecurityChallengeDetails passwordChallenge;
    /**. private class variable to hold SecurityChallengeDetails for address */
    private SecurityChallengeDetails addressChallenge;
    /**. private class variable to hold emali addres for credit cacrd */
    private SecurityChallengeDetails cardChallenge;
    /**. private class variable to hold security questions */
    private SecurityChallengeDetails[] questions;


    /**.
     * Setup method, to assign the references
     * @throws Exception Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loginServiceMock = mock(LoginService.class);
        userDetailsServiceMock = mock(UserDetailsService.class);

        underTest = new AircellUser();
        underTest.setLoginService(loginServiceMock);
        underTest.setUserDetailsService(userDetailsServiceMock);

        loginOkResponse = new ServiceResponse(true);

        loginDetails = new LoginDetails();
        loginDetails.setUsername(USERNAME);
        loginDetails.setPassword(PASSWORD);

        personalDetails = new PersonalDetails();
        personalDetails.setFirstname(FIRSTNAME);
        personalDetails.setLastname(LASTNAME);
        /* personalDetails.setMiddlename(MIDDLENAME);*/
        personalDetails.setTitle(TITLE);

        addressDetails = new AddressDetails();
        addressDetails.setAddress1("Address1");
        addressDetails.setAddress2("Address2");
        addressDetails.setCity("City");
        addressDetails.setUsStateCode("NY");
        addressDetails.setUsZipCode("7676");

        invalidDetailsResponse = new ServiceResponse(false);
        invalidDetailsResponse
        .setErrorCode(AcpuErrorCode.AUTHENTICATION_REJECTED);

        termsChangedResponse = new ServiceResponse(false);
        termsChangedResponse.setErrorCode(BSSErrorCode.TERMS_CHANGED);

        usernameChallenge = new SecurityChallengeDetails();
        usernameChallenge.setId(1);
        usernameChallenge.setQuestion("this is username challenge");
        usernameChallenge.setType(SecurityChallengeType.USERNAME_CHALLENGE);

        passwordChallenge = new SecurityChallengeDetails();
        passwordChallenge.setId(2);
        passwordChallenge.setQuestion("this is password challenge");
        passwordChallenge.setType(SecurityChallengeType.PASSWORD_CHALLENGE);

        addressChallenge = new SecurityChallengeDetails();
        addressChallenge.setId(3);
        addressChallenge.setQuestion("this is address challenge");
        addressChallenge.setType(SecurityChallengeType.ADDRESS_CHALLENGE);

        cardChallenge = new SecurityChallengeDetails();
        cardChallenge.setId(4);
        cardChallenge.setQuestion("this is card challenge");
        cardChallenge.setType(SecurityChallengeType.CARD_NUMBER_CHALLENGE);

        questions = new SecurityChallengeDetails[]{
        usernameChallenge, passwordChallenge, addressChallenge, cardChallenge
        };

        tracking = new TrackingType();
        tracking
        .setTransactionId("1212SESSIONID8e383738::01/01/2009 10:10:22:234");
    }

    /**.
     * Test happy path login
     *
     */
    public void testLoginHappyPath() {
        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);

        checking(
        new Expectations() {
            {
                one(loginServiceMock).login(loginDetails);
                will(returnValue(loginOkResponse));
            }

            {
                // terms and conditions is not relevant for this test.
                allowing(userDetailsServiceMock).setUserTermsAndConditions(
                with(any(String.class)), with(any(String.class)),
                with(any(TrackingType.class))
                );
                will(returnValue(new ServiceResponse(true)));
            }}
        );

        final ServiceResponse retval = underTest
        .loginToGround(loginDetails, IP_ADDRESS, TS_CS_VERSION, tracking);
        assertTrue(retval.isSuccess());
        assertTrue(underTest.isLoggedIn());
        assertTrue(underTest.isAcceptedTerms());
        assertEquals(USERNAME, underTest.getUsername());
        assertEquals(PASSWORD, underTest.getPassword());
        assertEquals(IP_ADDRESS, underTest.getIpAddress());
    }

    /**.
     * Test assertState as logged in user
     *
     */
    public void testAssertState_HappyPath() {
        underTest.setState(State.LOGGED_IN);
        try {
            underTest.assertState(
            new State[]{
            State.NOT_LOGGED_IN, State.PASSWORD_RESET_CHALLENGE_DONE
            }
            );
            fail("an IllegalStateException should have been raised");
        } catch (IllegalStateException expected) {
            assertTrue(true);
        }
    }

    /**.
     * Test assertState as not logged in user
     *
     */
    public void testAssertState_StateMatches() {
        underTest.setState(State.NOT_LOGGED_IN);
        try {
            underTest.assertState(
            new State[]{
            State.NOT_LOGGED_IN, State.PASSWORD_RESET_CHALLENGE_DONE
            }
            );
        } catch (IllegalStateException expected) {
            fail("an IllegalStateException should NOT have been raised");
        }
    }

    /**.
     * Test login as invalid user
     *
     */
    public void testLoginInvalidUser() {
        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);

        checking(
        new Expectations() {
            {
                one(loginServiceMock).login(loginDetails);
                will(returnValue(invalidDetailsResponse));
            }

            {
                allowing(userDetailsServiceMock).setUserTermsAndConditions(
                with(any(String.class)), with(any(String.class)),
                with(any(TrackingType.class))
                );
            }

        }
        );

        final ServiceResponse retval = underTest
        .loginToGround(loginDetails, IP_ADDRESS, TS_CS_VERSION, tracking);
        assertFalse(retval.isSuccess());
        assertEquals(
        AcpuErrorCode.AUTHENTICATION_REJECTED, retval.getErrorCode()
        );
        assertFalse(underTest.isLoggedIn());
        assertFalse(underTest.isAcceptedTerms());
        assertNull(underTest.getUsername());
        assertNull(underTest.getPassword());
        assertNull(underTest.getIpAddress());
    }

    /**.
     * Tests login with null value
     *
     */
    public void testLoginWithNulls() {
        try {
            underTest.loginToGround(null, null, null, null);
            fail("Expected to see illegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    /**.
     * Test forgot user name
     *
     */
    public void testRetrieveForgottenUsernameHappyPath() {
        final ServiceResponse<String> usernameOkResponse =
        new ServiceResponse<String>();
        usernameOkResponse.setSuccess(true);
        usernameOkResponse.setPayload(USERNAME);


        final ServiceResponse<SecurityChallengeDetails[]> questionsResponse =
        new ServiceResponse<SecurityChallengeDetails[]>();
        questionsResponse.setSuccess(true);
        questionsResponse.setPayload(questions);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).retrieveUsernameForEmailAddress(
                with(equal(EMAIL_ADDRESS)), with(equal(tracking))
                );
                will(returnValue(usernameOkResponse));
            }
            {
                one(userDetailsServiceMock).getSecurityQuestions(
                with(equal(USERNAME)), with(equal(locale)),
                with(equal(tracking)), with(equal(EMAIL_ADDRESS))
                );
                will(returnValue(questionsResponse));
            }

        }
        );
        final AircellUser.State stateB4 = underTest.getState();
        final ServiceResponse response =
        underTest.retrieveForgottenUsername(EMAIL_ADDRESS, locale, tracking);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertEquals(
        "username challenge should have been changed", usernameChallenge,
        underTest.getUsernameChallenge()
        );
        assertEquals(
        "password challenge should have been changed", passwordChallenge,
        underTest.getPasswordChallenge()
        );
        assertEquals(
        "emailAddresss shuold have been changed", EMAIL_ADDRESS,
        underTest.getEmailAddress()
        );
        assertEquals(
        "username should have been changed", USERNAME, underTest.getUsername()
        );
        assertEquals(
        "state should not have changed", stateB4, underTest.getState()
        );
    }

    /**.
     * Tests forgot username in negative scenario
     *
     */
    public void testRetrieveForgottenUsername_UsernameLookupFails() {
        final ServiceResponse<String> usernameFailsResponse =
        new ServiceResponse<String>();
        usernameFailsResponse.setSuccess(false);
        usernameFailsResponse
        .setErrorCode(BSSErrorCode.EMAIL_ADDRESS_DOES_NOT_EXIST);

        final ServiceResponse<SecurityChallengeDetails[]> questionsResponse =
        new ServiceResponse<SecurityChallengeDetails[]>();
        questionsResponse.setSuccess(true);
        questionsResponse.setPayload(questions);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).retrieveUsernameForEmailAddress(
                with(equal(EMAIL_ADDRESS)), with(equal(tracking))
                );
                will(returnValue(usernameFailsResponse));
            }
            {
                allowing(userDetailsServiceMock).getSecurityQuestions(
                with(equal(USERNAME)), with(equal(locale)),
                with(equal(tracking)), with(equal(EMAIL_ADDRESS))
                );
                will(returnValue(questionsResponse));
            }

        }
        );
        final AircellUser.State stateB4 = underTest.getState();
        final ServiceResponse response =
        underTest.retrieveForgottenUsername(EMAIL_ADDRESS, locale, tracking);

        assertNotNull(response);
        assertFalse(
        "failure outcome expected in this call", response.isSuccess()
        );
        assertNull(
        "username challenge should not have been changed",
        underTest.getUsernameChallenge()
        );
        assertNull(
        "password challenge should not have been changed",
        underTest.getPasswordChallenge()
        );
        assertNull(
        "emailAddresss shuold have been changed", underTest.getEmailAddress()
        );
        assertNull(
        "username should have been changed", underTest.getUsername()
        );
        assertEquals(
        "state should not have changed", stateB4, underTest.getState()
        );
    }

    /**.
     * Tests forgot username with not logged state
     *
     */
    public void testRetrieveForgottenUsername_InWrongState() {
        checking(
        new Expectations() {{
            ignoring(userDetailsServiceMock);
        }}
        );
        for (AircellUser.State state : AircellUser.State.values()) {
            if (!AircellUser.State.NOT_LOGGED_IN.equals(state)) {
                underTest.setState(state);
                try {
                    underTest
                    .retrieveForgottenUsername(EMAIL_ADDRESS, locale, tracking);
                    fail(
                    "expected illegalstateexception for any state "
                    + "other than not_logged_in"
                    );
                } catch (IllegalStateException expected) {
                    assertTrue(true);
                }
            }
        }
    }

    public void testRetrieveForgottenUsername_SecurityChallengeLookupFails() {
        final ServiceResponse<String> usernameOkResponse =
        new ServiceResponse<String>();
        usernameOkResponse.setSuccess(true);
        usernameOkResponse.setPayload(USERNAME);


        final ServiceResponse<SecurityChallengeDetails[]>
        questionsFailsResponse =
        new ServiceResponse<SecurityChallengeDetails[]>();
        questionsFailsResponse.setSuccess(false);
        questionsFailsResponse
        .setErrorCode(BSSErrorCode.USERNAME_DOES_NOT_EXIST);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).retrieveUsernameForEmailAddress(
                with(equal(EMAIL_ADDRESS)), with(equal(tracking))
                );
                will(returnValue(usernameOkResponse));
            }
            {
                one(userDetailsServiceMock).getSecurityQuestions(
                with(equal(USERNAME)), with(equal(locale)),
                with(equal(tracking)), with(equal(EMAIL_ADDRESS))
                );
                will(returnValue(questionsFailsResponse));
            }

        }
        );
        final AircellUser.State stateB4 = underTest.getState();
        final ServiceResponse response =
        underTest.retrieveForgottenUsername(EMAIL_ADDRESS, locale, tracking);

        assertNotNull(response);
        assertFalse(
        "failure outcome expected in this call", response.isSuccess()
        );
        assertNull(
        "username challenge should have been changed",
        underTest.getUsernameChallenge()
        );
        assertNull(
        "password challenge should NOT have been changed",
        underTest.getPasswordChallenge()
        );
        assertNull(
        "emailAddresss shuold NOT have been changed",
        underTest.getEmailAddress()
        );
        assertNull(
        "username should NOT have been changed", underTest.getUsername()
        );
        assertEquals(
        "state should not have changed", stateB4, underTest.getState()
        );
    }

    public void testRetreiveForgottenPassword_WithNullArgs() {
        try {
            underTest.retrieveForgottenPassword(" ", " ", tracking, " ");
            fail("expected illegalargument exceoption");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testRetreiveForgottenPassword_CalledInInvalidState() {
        checking(
        new Expectations() {{
            ignoring(userDetailsServiceMock);
        }}
        );
        for (AircellUser.State state : AircellUser.State.values()) {
            if (!AircellUser.State.NOT_LOGGED_IN.equals(state)) {
                underTest.setState(state);
                try {
                    underTest.retrieveForgottenPassword(
                    USERNAME, locale, tracking, EMAIL_ADDRESS
                    );
                    fail(
                    "expected illegalstateexception for any state "
                    + "other than not_logged_in"
                    );
                } catch (IllegalStateException expected) {
                    assertTrue(true);
                }
            }
        }
    }

    public void testRetrieveForgottenPassword_WithBSSError() {
        final ServiceResponse<SecurityChallengeDetails[]>
        questionsFailsResponse =
        new ServiceResponse<SecurityChallengeDetails[]>();
        questionsFailsResponse.setSuccess(false);
        questionsFailsResponse
        .setErrorCode(BSSErrorCode.USERNAME_DOES_NOT_EXIST);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).getSecurityQuestions(
                with(equal(USERNAME)), with(equal(locale)),
                with(equal(tracking)), with(equal(EMAIL_ADDRESS))
                );
                will(returnValue(questionsFailsResponse));
            }

        }
        );
        final AircellUser.State stateB4 = underTest.getState();
        final ServiceResponse response = underTest
        .retrieveForgottenPassword(USERNAME, locale, tracking, EMAIL_ADDRESS);

        assertNotNull(response);
        assertFalse(
        "failure outcome expected in this call", response.isSuccess()
        );
        assertNull(
        "username challenge should have been changed",
        underTest.getUsernameChallenge()
        );
        assertNull(
        "password challenge should NOT have been changed",
        underTest.getPasswordChallenge()
        );
        assertEquals(
        "state should not have changed", stateB4, underTest.getState()
        );
    }

    public void testRetrieveForgottenPassword_HappyPath() {

        final ServiceResponse<SecurityChallengeDetails[]> questionsResponse =
        new ServiceResponse<SecurityChallengeDetails[]>();
        questionsResponse.setSuccess(true);
        questionsResponse.setPayload(questions);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).getSecurityQuestions(
                with(equal(USERNAME)), with(equal(locale)),
                with(equal(tracking)), with(equal(EMAIL_ADDRESS))
                );
                will(returnValue(questionsResponse));
            }

        }
        );
        final AircellUser.State stateB4 = underTest.getState();
        final ServiceResponse response = underTest
        .retrieveForgottenPassword(USERNAME, locale, tracking, EMAIL_ADDRESS);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertEquals(
        "username challenge should have been changed", usernameChallenge,
        underTest.getUsernameChallenge()
        );
        assertEquals(
        "password challenge should have been changed", passwordChallenge,
        underTest.getPasswordChallenge()
        );
        assertEquals(
        "state should not have changed", stateB4, underTest.getState()
        );
    }

    public void testResetPasswordFollowingChallenge_WithNullArgs() {
        try {
            underTest.resetPasswordFollowingChallenge(" ", null);
            fail("IllegalArgumentException expected for blank argument");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testResetPasswordFollowingChallenge_CalledInInvalidState() {
        checking(
        new Expectations() {{
            ignoring(userDetailsServiceMock);
        }}
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(new SecurityChallengeDetails());
        for (AircellUser.State state : AircellUser.State.values()) {
            if (!AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE
            .equals(state)) {
                underTest.setState(state);
                try {
                    underTest
                    .resetPasswordFollowingChallenge(PASSWORD, tracking);
                    fail(
                    "expected illegalstateexception for any state "
                    + "other than not_logged_in"
                    );
                } catch (IllegalStateException expected) {
                    assertTrue(true);
                }
            }
        }
    }

    public void testResetPasswordFollowingChallenge_HappyPath() {
        final ServiceResponse passwordResetOkResponse = new ServiceResponse();
        passwordResetOkResponse.setSuccess(true);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).resetPassword(
                with(equal(USERNAME)), with(equal(PASSWORD)),
                with(equal(tracking))
                );
                will(returnValue(passwordResetOkResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);

        underTest.setState(AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE);
        final ServiceResponse response =
        underTest.resetPasswordFollowingChallenge(PASSWORD, tracking);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertFalse(
        "The AircellUser object should not stay "
        + "in the password_challenge_done state after the "
        + "password has been reset",
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
        assertNull(
        "The password shuold not have been retained "
        + "by the aircell user object", underTest.getPassword()
        );
    }

    public void
    testRestPasswordFollowingChappenge_UsernameLookupFails_BSSError() {
        final ServiceResponse passwordResetOkResponse = new ServiceResponse();
        passwordResetOkResponse.setSuccess(false);
        passwordResetOkResponse
        .setErrorCode(BSSErrorCode.USERNAME_DOES_NOT_EXIST);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).resetPassword(
                with(any(String.class)), with(any(String.class)),
                with(equal(tracking))
                );
                will(returnValue(passwordResetOkResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);

        underTest.setState(AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE);
        final ServiceResponse response =
        underTest.resetPasswordFollowingChallenge(PASSWORD, tracking);

        assertNotNull(response);
        assertFalse(
        "failure outcome expected in this call", response.isSuccess()
        );
        assertFalse(
        "The AircellUser object should not stay "
        + "in the password_challenge_done state after the "
        + "password has been reset",
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
        assertNull(
        "The password shuold not have been retained "
        + "by the aircell user object", underTest.getPassword()
        );
    }

    public void validateForgottenPasswordHappyPathTester(
    final boolean correctAnswer
    ) {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(true);
        checkSecurityQuestionResponse.setPayload(correctAnswer);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).checkSecurityQuestions(
                with(equal(USERNAME)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(tracking))
                );
                will(returnValue(checkSecurityQuestionResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setAddressChallenge(addressChallenge);
        underTest.setCardNumberChallenge(cardChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);
        final ServiceResponse<Boolean> response =
        underTest.validateForgottenPassword("AKQA", "7236", "4BM", tracking);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertEquals(
        "answer should have been asnwered correctly",
        Boolean.valueOf(correctAnswer), response.getPayload()
        );
        assertEquals(
        "The aircell user object should have changed the state", correctAnswer,
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
    }

    public void testValidateForgottenPassword_HappyPath() {
        validateForgottenPasswordHappyPathTester(true);
    }

    public void testValidateForgottenPassword_HappyPathButIncorrectAnswer() {
        validateForgottenPasswordHappyPathTester(false);
    }

    public void testValidateForgottenPassword_WithNullArgs() {
        try {
            underTest.validateForgottenPassword(" ", " ", " ", null);
            fail("IllegalArgumentException expected for blank argument");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testValidateForgottenPassword_CalledInInvalidState() {
        checking(
        new Expectations() {{
            allowing(userDetailsServiceMock).checkSecurityQuestions(
            with(any(String.class)), with(any(SecurityChallengeDetails.class)),
            with(equal(tracking))
            );
            will(returnValue(new ServiceResponse(false)));
        }}
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(new SecurityChallengeDetails());
        for (AircellUser.State state : AircellUser.State.values()) {
            if (!AircellUser.State.NOT_LOGGED_IN.equals(state)) {
                underTest.setState(state);
                try {
                    underTest.validateForgottenPassword(
                    "AKQA", "6565", "7867", tracking
                    );
                    fail(
                    "expected illegalstateexception for any state "
                    + "other than not_logged_in"
                    );
                } catch (IllegalStateException expected) {
                    assertTrue(true);
                }
            }
        }
    }

    public void testValidateForgottenPassword_BSSReturnsError() {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(false);
        checkSecurityQuestionResponse
        .setErrorCode(BSSErrorCode.USERNAME_DOES_NOT_EXIST);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).checkSecurityQuestions(
                with(equal(USERNAME)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(tracking))
                );
                will(returnValue(checkSecurityQuestionResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setAddressChallenge(addressChallenge);
        underTest.setCardNumberChallenge(cardChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);
        final ServiceResponse<Boolean> response =
        underTest.validateForgottenPassword("AKQA", "7236", "4BM", tracking);

        assertNotNull(response);
        assertFalse(
        "failure outcome expected in this call", response.isSuccess()
        );
        assertFalse(
        "The aircell user object should NOT have changed the state",
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
    }

    public void
    validateForgottenPassword_NoCardChallengeVersion_HappyPathTester(
    final boolean correctAnswer
    ) {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(true);
        checkSecurityQuestionResponse.setPayload(correctAnswer);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).checkSecurityQuestions(
                with(equal(USERNAME)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(tracking))
                );
                will(returnValue(checkSecurityQuestionResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);
        final ServiceResponse<Boolean> response =
        underTest.validateForgottenPassword("AKQA", tracking);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertEquals(
        "answer should have been asnwered correctly",
        Boolean.valueOf(correctAnswer), response.getPayload()
        );
        assertEquals(
        "The aircell user object should have changed the state", correctAnswer,
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
    }

    public void
    testValidateForgottenPassword_NoCardChallengeVersion_HappyPath() {
        validateForgottenPassword_NoCardChallengeVersion_HappyPathTester(true);
    }

    public void
    testValidateForgottenPassword__NoCardChallengeVersion_HappyPathButIncorrectAnswer() {
        validateForgottenPassword_NoCardChallengeVersion_HappyPathTester(false);
    }

    public void validateForgottenPassword_HappyPathTester(
    final boolean correctAnswer
    ) {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(true);
        checkSecurityQuestionResponse.setPayload(correctAnswer);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).checkSecurityQuestions(
                with(equal(USERNAME)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(tracking))
                );
                will(returnValue(checkSecurityQuestionResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setCardNumberChallenge(cardChallenge);
        underTest.setAddressChallenge(addressChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);
        final ServiceResponse<Boolean> response =
        underTest.validateForgottenPassword(PASSWORD,
            "1234", "11111", tracking);

        assertNotNull(response);
        assertTrue(
        "success outcome expected in this call", response.isSuccess()
        );
        assertEquals(
        "answer should have been asnwered correctly",
        Boolean.valueOf(correctAnswer), response.getPayload()
        );
        assertEquals(
        "The aircell user object should have changed the state", correctAnswer,
        AircellUser.State.PASSWORD_RESET_CHALLENGE_DONE.equals(
        underTest.getState()
        )
        );
    }

    /**.
     * Tests that the validateForgottenPassword is ok using card & zip details
     * details provided are correct
     */
    public void testValidateForgottenUsername_HappyPath() {
        validateForgottenPassword_HappyPathTester(true);
    }

    /**.
     * Tests that the validateForgottenPassword is ok using card & zip details
     * but that the details provided are incorrect
     */
    public void testValidateForgottenUsername_HappyPathButIncorrectAnswer() {
        validateForgottenPassword_HappyPathTester(false);
    }

    /**. Tests the arguments passed to validateForgottenUsername */
    public void testValidateForgottenUsername_InvalidArgs() {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(true);
        checkSecurityQuestionResponse.setPayload(true);

        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setCardNumberChallenge(cardChallenge);
        underTest.setAddressChallenge(addressChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);

        try {
            final ServiceResponse<Boolean> response = underTest
            .validateForgottenPassword(
            BLANK_STRING, CCNUMBER, ZIP_CODE, tracking
            );
            fail(
            "Username was blank, "
            + "should have thrown an IllegalArgumentException");
        } catch (final IllegalArgumentException ex) {
           ex.getMessage();
        }

        try {
            final ServiceResponse<Boolean> response = underTest
            .validateForgottenPassword(
            PASSWORD, BLANK_STRING, ZIP_CODE, tracking
            );
            fail(
            "Card number was blank, "
            + "should have thrown an IllegalArgumentException");
        } catch (final IllegalArgumentException ex) {
            ex.getMessage();
        }

        try {
            final ServiceResponse<Boolean> response = underTest
            .validateForgottenPassword(
            PASSWORD, CCNUMBER, BLANK_STRING, tracking
            );
            fail(
            "ZIP Code was blank, should have thrown an IllegalArgumentException"
            );
        } catch (final IllegalArgumentException ex) {
            ex.getMessage();
        }
    }

    /**.
     * Checks to see that an exception is thrown if the user state isn't not
     * logged in
     */
    public void testValidateForgottenUsername_CalledInInvalidState() {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(true);
        checkSecurityQuestionResponse.setPayload(true);

        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setCardNumberChallenge(cardChallenge);
        underTest.setAddressChallenge(addressChallenge);

        underTest.setState(AircellUser.State.LOGGED_IN);

        try {
            final ServiceResponse<Boolean> response = underTest
            .validateForgottenPassword(PASSWORD, CCNUMBER, ZIP_CODE, tracking);
            fail(
            "IllegalStateException should have "
            + "been thrown here for invalid user state");
        } catch (final IllegalStateException ex) {
            ex.getMessage();
        }
    }

    /**. Tests whether a BSS error code is correctly trapped and returned */
    public void testValidateForgottenUsername_BSSReturnsErrorCode() {
        final ServiceResponse<Boolean> checkSecurityQuestionResponse =
        new ServiceResponse<Boolean>();
        checkSecurityQuestionResponse.setSuccess(false);
        checkSecurityQuestionResponse
        .setErrorCause(new IllegalArgumentException("An error!"));
        checkSecurityQuestionResponse
        .setErrorCode(BSSErrorCode.MANDATORY_DATA_NOT_SUPPLIED);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).checkSecurityQuestions(
                with(equal(USERNAME)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(tracking))
                );
                will(returnValue(checkSecurityQuestionResponse));
            }

        }
        );
        underTest.setUsername(USERNAME);
        underTest.setPasswordChallenge(passwordChallenge);
        underTest.setCardNumberChallenge(cardChallenge);
        underTest.setAddressChallenge(addressChallenge);

        underTest.setState(AircellUser.State.NOT_LOGGED_IN);
        final ServiceResponse<Boolean> response = underTest
        .validateForgottenPassword(PASSWORD, CCNUMBER, ZIP_CODE, tracking);

        assertNotNull(response);
        assertFalse(
        "BSS failure cuases response to be unsuccessful", response.isSuccess()
        );
        assertTrue(response.getErrorCause() instanceof Throwable);
        assertEquals(
        BSSErrorCode.MANDATORY_DATA_NOT_SUPPLIED, response.getErrorCode()
        );
    }

    public void testSetAllSecurityChallengesHappyPath() {
        try {
            underTest.setAllSecurityChallenges(questions);
            assertEquals(
            "this method shoudl have set username challenge correctly",
            usernameChallenge, underTest.getUsernameChallenge()
            );
            assertEquals(
            "this method shoudl have set password challenge correctly",
            passwordChallenge, underTest.getPasswordChallenge()
            );
            assertEquals(
            "this method shoudl have set address challenge correctly",
            addressChallenge, underTest.getAddressChallenge()
            );
            assertEquals(
            "this method shoudl have set card challenge correctly",
            cardChallenge, underTest.getCardNumberChallenge()
            );
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException not expected");
        }
    }

    public void testSetAllSecurityChallenges_WithNull() {
        try {
            underTest.setAllSecurityChallenges(null);
            fail("IllegalArgumentException expected as null arg passed");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testSetAllSecurityChallenges_WithMissingUsernameChallenge() {
        try {
            questions = (SecurityChallengeDetails[]) ArrayUtils
            .removeElement(questions, questions[ 0 ]);
            underTest.setAllSecurityChallenges(questions);
            fail(
            "IllegalArgumentException "
            + "expected as incomplete set of challenges passed");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testSetAllSecurityChallenges_WithMissingPasswordChallenge() {
        try {
            questions = (SecurityChallengeDetails[]) ArrayUtils
            .removeElement(questions, questions[ 1 ]);
            underTest.setAllSecurityChallenges(questions);
            fail(
            "IllegalArgumentException expected as "
            + "incomplete set of challenges passed");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testSetAllSecurityChallenges_WithMissingAddressChallenge() {
        try {
            questions = (SecurityChallengeDetails[]) ArrayUtils
            .removeElement(questions, questions[ 2 ]);
            underTest.setAllSecurityChallenges(questions);
        } catch (IllegalArgumentException expected) {
            fail(
            "IllegalArgumentException not "
            + "expected as address challenge is optional");
        }
    }

    public void testSetAllSecurityChallenges_WithMissingCardChallenge() {
        try {
            questions = (SecurityChallengeDetails[]) ArrayUtils
            .removeElement(questions, questions[ 3 ]);
            underTest.setAllSecurityChallenges(questions);
        } catch (IllegalArgumentException expected) {
            fail(
            "IllegalArgumentException not "
            + "expected as card challenge is optional");
        }
    }

    public void testAcceptTermsAndConditions_HappyPath() {
        final ServiceResponse tsCsServiceResponse = new ServiceResponse(true);
        final LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername(USERNAME);
        loginDetails.setPassword(PASSWORD);
        underTest.setLoginDetails(loginDetails);
        underTest.setState(State.LOGGED_IN);
        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock)
                .setUserTermsAndConditions(USERNAME, TS_CS_VERSION, tracking);
                will(returnValue(tsCsServiceResponse));
            }
        }
        );

        underTest.acceptTermsAndConditions(TS_CS_VERSION, tracking);
    }

    public void testAcceptTermsAndConditions_CalledInInvalidState() {
        underTest.setState(State.NOT_LOGGED_IN);
        try {
            underTest.acceptTermsAndConditions("1", tracking);
            fail("IllegalArgumetnException expected");
        } catch (IllegalStateException expected) {
            expected.getMessage();
        }
    }


    public void testAssertRegisterPreconditions() {
        underTest.assertRegisterPreconditions(
        loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
        passwordChallenge, addressDetails, IP_ADDRESS, TS_CS_VERSION
        );
        // loginDetail argument
        try {
            underTest.assertRegisterPreconditions(
            null, EMAIL_ADDRESS, personalDetails, usernameChallenge,
            passwordChallenge, addressDetails, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // emailAddress argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, null, personalDetails, usernameChallenge,
            passwordChallenge, addressDetails, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // personalDetails argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, null, usernameChallenge,
            passwordChallenge, addressDetails, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // usernameChallenge argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, personalDetails, null,
            passwordChallenge, addressDetails, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // passwordChallenge argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
            null, addressDetails, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // addressDetails argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
            passwordChallenge, null, IP_ADDRESS, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // ipAddress argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
            passwordChallenge, addressDetails, null, TS_CS_VERSION
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

        // tsCsVersion argument
        try {
            underTest.assertRegisterPreconditions(
            loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
            passwordChallenge, addressDetails, IP_ADDRESS, null
            );
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            expected.getMessage();
        }

    }

    public void testRegisterNewUser_HappyPath() {

        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).registerNewUser(
                with(equal(loginDetails)), with(equal(EMAIL_ADDRESS)),
                with(equal(personalDetails)), with(equal(addressDetails)),
                with(equal(PHONE_NUMBER)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(TS_CS_VERSION)), with(equal(MARKETING)),
                with(equal(tracking))
                );
                will(returnValue(new ServiceResponse<Integer>(true)));
            }
        }
        );

        final ServiceResponse<Integer> retval = underTest.register(
        loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
        passwordChallenge, addressDetails, PHONE_NUMBER, IP_ADDRESS,
        TS_CS_VERSION, MARKETING, tracking
        );
        assertNotNull(retval);
        assertTrue("successful outcome expected", retval.isSuccess());
        assertEquals(
        "the user is expected it be in the logged in state", State.LOGGED_IN,
        underTest.getState()
        );
    }

    public void testRegisterNewUser_RegisterFails() {
        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).registerNewUser(
                with(equal(loginDetails)), with(equal(EMAIL_ADDRESS)),
                with(equal(personalDetails)), with(equal(addressDetails)),
                with(equal(PHONE_NUMBER)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(TS_CS_VERSION)), with(equal(MARKETING)),
                with(equal(tracking))
                );
                will(returnValue(new ServiceResponse<Integer>(false)));
            }
            {
                never(loginServiceMock).login(with(any(LoginDetails.class)));
            }
            {
                never(userDetailsServiceMock).setUserTermsAndConditions(
                with(any(String.class)), with(any(String.class)),
                with(any(TrackingType.class))
                );
            }
        }
        );

        final ServiceResponse<Integer> retval = underTest.register(
        loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
        passwordChallenge, addressDetails, PHONE_NUMBER, IP_ADDRESS,
        TS_CS_VERSION, MARKETING, tracking
        );
        assertNotNull(retval);
        assertFalse("un-successful outcome expected", retval.isSuccess());
        assertFalse(
        "the user is expected it be in the logged in state",
        State.LOGGED_IN.equals(underTest.getState())
        );
    }

    public void testRegisterNewUser_TsCsUpdateFails() {

        final LoginDetails loginDetails = new LoginDetails(USERNAME, PASSWORD);

        checking(
        new Expectations() {
            {
                one(userDetailsServiceMock).registerNewUser(
                with(equal(loginDetails)), with(equal(EMAIL_ADDRESS)),
                with(equal(personalDetails)), with(equal(addressDetails)),
                with(equal(PHONE_NUMBER)),
                with(any(SecurityChallengeDetails[].class)),
                with(equal(TS_CS_VERSION)), with(equal(MARKETING)),
                with(equal(tracking))
                );
                will(returnValue(new ServiceResponse<Integer>(true)));
            }
        }
        );

        final ServiceResponse<Integer> retval = underTest.register(
        loginDetails, EMAIL_ADDRESS, personalDetails, usernameChallenge,
        passwordChallenge, addressDetails, PHONE_NUMBER, IP_ADDRESS,
        TS_CS_VERSION, MARKETING, tracking
        );
        assertNotNull(retval);
        assertTrue("successful outcome expected", retval.isSuccess());
        assertEquals(
        "the user is expected it be in the logged in "
        + "state eventho update of terms and conditons failed.",
        State.LOGGED_IN, underTest.getState()
        );
    }

    public void testSetUsername_calledInWrongState() {
        try {
            underTest.setUsername(USERNAME);
        } catch (IllegalStateException e) {
            fail(
            "IllegalStateException not expected "
            + "if called in NOT_LOGGED_IN_STATE");
        }

        underTest.setState(State.LOGGED_IN);
        try {
            underTest.setUsername(USERNAME);
            fail("Should have raised IllegalStateException");
        } catch (IllegalStateException expected) {
            assertTrue(true);
        }
    }

    public void testSetPassword_calledInWrongState() {
        try {
            underTest.setPassword(PASSWORD);
        } catch (IllegalStateException e) {
            fail(
            "IllegalStateException not "
            + "expected if called in NOT_LOGGED_IN_STATE");
        }

        underTest.setState(State.LOGGED_IN);
        try {
            underTest.setUsername(PASSWORD);
            fail("Should have raised IllegalStateException");
        } catch (IllegalStateException expected) {
            assertTrue(true);
        }
    }

    public void testUpdatePassword_HappyPath() {
        underTest.setUsername(USERNAME);
        underTest.setPassword(PASSWORD);
        underTest.setState(State.LOGGED_IN);
        final String UPDATED_PASSWORD = "UPDATED_PWD";

        final LoginDetails updatedLoginDetails =
        new LoginDetails(USERNAME, UPDATED_PASSWORD);

        checking(
        new Expectations() {
            {
                one(loginServiceMock).login(with(equal(updatedLoginDetails)));
                will(returnValue(new ServiceResponse(true)));
            }
        }
        );

        final ServiceResponse retval =
        underTest.updatePassword(UPDATED_PASSWORD);
        assertTrue("successfull outcome expected", retval.isSuccess());
        assertEquals(
        "the new password value should have "
        + "been set on the aircell user object", UPDATED_PASSWORD,
        underTest.getPassword()
        );
    }

    public void testUpdatePassword_CalledInWrongState() {
        underTest.setUsername(USERNAME);
        underTest.setPassword(PASSWORD);

        final String UPDATED_PASSWORD = "UPDATED_PWD";
        checking(
        new Expectations() {
            {
                never(loginServiceMock).login(with(any(LoginDetails.class)));
            }
        }
        );
        for (State s : State.values()) {
            if (!State.LOGGED_IN.equals(s)) {
                try {
                    underTest.setState(s);
                    underTest.updatePassword(UPDATED_PASSWORD);
                    fail(
                    "Aircell.updatePassword can only"
                    + " be called in the LOGGED_IN state.");
                } catch (IllegalStateException e) {
                    assertTrue(true); // expected
                    assertEquals(
                    "Password of the AircellUser should not have changed",
                    PASSWORD, underTest.getPassword()
                    );
                }
            }
        }

    }

    public void testUpdatePassword_CalledWithNullArgs() {
        underTest.setUsername(USERNAME);
        underTest.setPassword(PASSWORD);
        underTest.setState(State.NOT_LOGGED_IN);
        checking(
        new Expectations() {
            {
                never(loginServiceMock).login(with(any(LoginDetails.class)));
            }
        }
        );

        try {
            underTest.updatePassword("  ");
            fail("Aircell.updatePassword should only take null args");
        } catch (IllegalArgumentException e) {
            assertTrue(true); // expected
        }
        assertEquals(
        "Password of the AircellUser should not have changed", PASSWORD,
        underTest.getPassword()
        );
    }

    public void testLogoutUserHappyPath() {

        underTest.setUsername(USERNAME);
        underTest.setPassword(PASSWORD);
        underTest.setAddressDetails(new AddressDetails());
        underTest.setLoginDetails(new LoginDetails());
        underTest.setPersonalDetails(new PersonalDetails());
        underTest.setIpAddress(IP_ADDRESS);
        underTest.setEmailAddress(EMAIL_ADDRESS);
        underTest.setState(State.LOGGED_IN);

        underTest.logout();

        assertEquals(State.NOT_LOGGED_IN, underTest.getState());
        assertNull(underTest.getPersonalDetails());
        assertNull(underTest.getAddressDetails());
        assertNull(underTest.getLoginDetails());
        assertNull(underTest.getEmailAddress());
        assertNull(underTest.getIpAddress());
    }

    public void testCaptchaUnhappyPath() {
        assertFalse(underTest.isCaptchaPassed());
        try {
            underTest.setCaptchaPassed(true);
            fail("Should have thrown an IllegalStateException here");
        } catch (final IllegalStateException ex) {
            ex.getMessage();

        }
        assertFalse(underTest.isCaptchaPassed());
    }

    public void testCaptchaHappyPath() {
        underTest.setState(State.LOGGED_IN);
        assertFalse(underTest.isCaptchaPassed());
        underTest.setCaptchaPassed(true);
        assertTrue(underTest.isCaptchaPassed());
    }
}