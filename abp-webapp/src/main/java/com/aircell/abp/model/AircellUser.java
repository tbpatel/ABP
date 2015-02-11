/*
 * AircellUser.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.model.SecurityChallengeDetails.SecurityChallengeType;
import com.aircell.abp.service.LoginService;
import com.aircell.abp.service.PaymentService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.SubscriptionService;
import com.aircell.abp.service.UserDetailsService;
import com.aircell.bss.ws.TrackingType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;

@Configurable
public class AircellUser implements Serializable {

    /*different declaration, go to this class bottom to see
     public enum State {
         NOT_LOGGED_IN,
         LOGGED_IN,
         ACCEPTED_TERMS,
         PASSWORD_RESET_CHALLENGE_DONE;
     }
     */
    // TODO miro: revert back to using non-named State.
    /**.
     * Enum to hold user state
     */
    public enum State implements Serializable {
        /**. User is not logged in state */
        NOT_LOGGED_IN("NOT_LOGGED_IN"),
        /**. User is logged in state */
        LOGGED_IN("LOGGED_IN"),
        /**. Password challenge successful state */
        PASSWORD_RESET_CHALLENGE_DONE("PASSWORD_RESET_CHALLENGE_DONE");
        /**. Local variable */
        private String message;

        /**.
         * Enum constructor
         * @param st User state
         */
        State(String st){
            this.message = st;
        }
        /**.
         * Gets User state
         * @return String User state
         */
        String getState() {
            return message;
        }
    }
    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 1999722929648977060L;

    // *** properties
    /**. private class variable to hold personalDetails */
    private PersonalDetails personalDetails;
    /**. private class variable to hold addressDetails */
    private AddressDetails addressDetails;
    /**. private class variable to hold loginDetails */
    private LoginDetails loginDetails;
    /**. private class variable to hold emailAddress */
    private String emailAddress;
    /**. private class variable to hold phoneNumber */
    private String phoneNumber;
    /**. private class variable to hold ipAddress */
    private String ipAddress;
    /** . private class variable to hold userMac */
    private String userMac;
    /**. private class variable to hold activateError */
    private String activateError;
    /**. private class variable to hold captchaPassed */
    private boolean captchaPassed;
    /**. private class variable to hold passwordChallenge */
    private SecurityChallengeDetails passwordChallenge;
    /**. private class variable to hold usernameChallenge */
    private SecurityChallengeDetails usernameChallenge;
    /**. private class variable to hold cardNumberChallenge */
    private SecurityChallengeDetails cardNumberChallenge;
    /**. private class variable to hold addressChallenge */
    private SecurityChallengeDetails addressChallenge;
    /**. private class variable to hold order */
    private Order order;
    /**. private class variable to hold state */
    private State state;
    /**. private class variable to hold stateStatus */
    private String stateStatus;
    //Roaming Partner Changes
    /**. private class variable to hold how many times 
     * the smart client has polled the captcha page    */
    private int captchaPollRetryCount=-1;
    /**. private class variable to hold Captcha
     * validation status
     */
    private String captchaValidationStatus="CAPTCHA_VALIDATION_NA";
    private int smartClientActivation=-1;
    //Roaming Partner Changes ends

    // *** logger
    /**. logger variable */
    protected final transient Logger logger =
    LoggerFactory.getLogger(getClass());

    // *** Spring dependency injected services, weaved in using AOP.
    /**. private class variable to hold loginService */
    private transient LoginService loginService;
    /**. private class variable to hold userDetailsService */
    private transient UserDetailsService userDetailsService;
    /**. private class variable to hold subscriptionService */
    private transient SubscriptionService subscriptionService;
    /**. private class variable to hold paymentService */
    private transient PaymentService paymentService;

    /**. Default, empty constructor */
    public AircellUser() {
        this.setState(State.NOT_LOGGED_IN);
        this.captchaPassed = false;
        loginDetails = new LoginDetails();
    }

    // ********************************************************************
    // ********* Business methods *****************************************
    // ********************************************************************

    /**.
     * Logs the user into the system. Additionally it updates the user's
     * accepted terms and conditions version with the system.
     * This object must be either in one of the following states: <ul>
     * <li><code>State.NOT_LOGGED_IN</code> ie just after the object has been
     * created</li> <li><code>State.PASSWORD_CHALLENGE_DONE</code> ie after
     * completing the retrieve forgotted password</li> </ul>
     * If the return value indicates a success, then this object will be in the
     * following state: <code>State.LOGGED_IN</code>.
     * The <code>ServiceResponse</code> contains error codes of type {@link
     * com.aircell.abp.service.BSSErrorCode}.
     * @param loginDetails loginDetails object
     * @param ipAddress User IP address
     * @param tsCsVersion tsCsVersion
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object with no payload
     * @throws IllegalArgumentException exception thrown for
     * illegal argument
     * @throws IllegalStateException if call not in <code>State.LOGGED_IN</code>
     */
    public ServiceResponse loginToGround(
    final LoginDetails loginDetails, final String ipAddress,
    final String tsCsVersion, final TrackingType tracking
    ) throws IllegalArgumentException, IllegalStateException{
        if (loginDetails == null
        || StringUtils.isBlank(loginDetails.getUsername())
        || StringUtils.isBlank(loginDetails.getPassword())
        || StringUtils.isBlank(ipAddress) || StringUtils.isBlank(tsCsVersion)) {
            throw new IllegalArgumentException(
            "None of the args " + "for the login() can be null"
            );
        }
        assertState(
        new State[]{State.NOT_LOGGED_IN, State.PASSWORD_RESET_CHALLENGE_DONE}
        );

        final ServiceResponse retval = getLoginService().login(loginDetails);

        if (retval.isSuccess()) {
            this.loginDetails.setUsername(loginDetails.getUsername());
            this.loginDetails.setPassword(loginDetails.getPassword());
            this.ipAddress = ipAddress;
            logger.debug(
            "user: {} logged in with ip: {}", loginDetails.getUsername(),
            ipAddress
            );
            setState(State.LOGGED_IN);
            final ServiceResponse r =
            acceptTermsAndConditions(tsCsVersion, tracking);
            if (!r.isSuccess()) {
                logger.warn(
                "Accepted TsCs version update for user '{}' failed. "
                + "Response '{}'", this, r
                );
            }

        } else {
            logger.info(
            "Login for aircell user '{}' failed. Response '{}'", this, retval
            );
            setState(State.NOT_LOGGED_IN);
        }
        return retval;
    }

    /**.
     * Logs the user out of the system. Sets the user state to
     * <code>State.NOT_LOGGED_IN</code>, user must be logged in to be logged
     * out
     *  <code>State.LOGGED_IN</code>
     */
    public void logout() {
        this.setState(State.NOT_LOGGED_IN);
        this.personalDetails = null;
        this.addressDetails = null;
        this.loginDetails = null;
        this.emailAddress = null;
        this.ipAddress = null;
    }

    /**.
     * Updates the version of terms and conditions this user accepted. This
     * method has to be called if this object is in one of the following states:
     * <ul> <li><code>State.LOGGED_IN</code></li> </ul>
     * The <code>ServiceResponse</code> contains error codes of type {@link
     * com.aircell.abp.service.BSSErrorCode} or
     *  {@link com.aircell.abp.service.AAAErrorCode}.
     * @param tsCsVersionTag tsCsVersionTag
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object indicating outcome of
     *         this call
     * @throws IllegalArgumentException exception thrown for
     * illegal argument
     */
    protected ServiceResponse acceptTermsAndConditions(
    final String tsCsVersionTag, final TrackingType tracking
    ) throws IllegalArgumentException{
        if (StringUtils.isBlank(tsCsVersionTag)) {
            throw new IllegalArgumentException("tsCsVersion cannot be null");
        }
        assertState(new State[]{State.LOGGED_IN });
        logger
        .debug("attempting to set terms and conditions for {}", getUsername());
        final ServiceResponse retval = getUserDetailsService()
        .setUserTermsAndConditions(getUsername(), tsCsVersionTag, tracking);
        return retval;
    }

    /**.
     * Registers the new user with the system. User's address details are
     * required for registeration.
     * This method has to be called only if this object is in the following
     * state: <code>State.NOT_LOGGED_IN</code>.
     * In case of successful outcome, the payload of the
     * <code>ServiceResponse</code> object will contain the internal user ID of
     * the newly registered user. Additionally this user will also be logged
     * into the system.
     * The <code>ServiceResponse</code> contains error codes of type {@link
     * com.aircell.abp.service.BSSErrorCode} or
     * {@link com.aircell.abp.service.AAAErrorCode}.
     * @param loginDetails cannot be null
     * @param emailAddress cannot be null
     * @param personalDetails cannot be null
     * @param usernameChallenge cannot be null
     * @param passwordChallenge cannot be null
     * @param addressDetails cannot be null
     * @param ipAddress cannot be null
     * @param tsCsVersionAccepted cannot be null
     * @param tracking TrackingType
     * @param marketingOptIn boolean value
     * @param telephoneNumber telephoneNumber
     * @return the <code>ServiceResponse</code> object for this call.
     * @throws IllegalStateException exception thrown for
     * illegal state
     * @throws IllegalArgumentException exception thrown for
     * illegal argument
     */
    public ServiceResponse<Integer> register(
    final LoginDetails loginDetails, final String emailAddress,
    final PersonalDetails personalDetails,
    final SecurityChallengeDetails usernameChallenge,
    final SecurityChallengeDetails passwordChallenge,
    final AddressDetails addressDetails, final String telephoneNumber,
    final String ipAddress, final String tsCsVersionAccepted,
    final boolean marketingOptIn, final TrackingType tracking
    ) throws IllegalArgumentException, IllegalStateException{
        assertRegisterPreconditions(
        loginDetails, emailAddress, personalDetails, usernameChallenge,
        passwordChallenge, addressDetails, ipAddress, tsCsVersionAccepted
        );
        assertState(State.NOT_LOGGED_IN);

        usernameChallenge.setType(SecurityChallengeType.USERNAME_CHALLENGE);
        passwordChallenge.setType(SecurityChallengeType.PASSWORD_CHALLENGE);

        final ServiceResponse<Integer> retval =
        getUserDetailsService().registerNewUser(
        loginDetails, emailAddress, personalDetails, addressDetails,
        telephoneNumber,
        new SecurityChallengeDetails[]{usernameChallenge, passwordChallenge },
        tsCsVersionAccepted, marketingOptIn, tracking
        );

        if (retval.isSuccess()) {
            this.loginDetails.setUsername(loginDetails.getUsername());
            this.loginDetails.setPassword(loginDetails.getPassword());
            this.ipAddress = ipAddress;
            // set the user as logged in
            logger.debug(
            "user: {} logged in with ip: {}", loginDetails.getUsername(),
            ipAddress
            );
            setState(State.LOGGED_IN);
        }
        return retval;
    }

    /**.
     * Asserts if all the conditions satisfies
     * prior to registration.
     * @param loginDetails LoginDetails object
     * @param emailAddress EmailAddress
     * @param personalDetails PersonalDetails object
     * @param usernameChallenge username security question
     * @param passwordChallenge password security question
     * @param addressDetails AddressDetails object
     * @param ipAddress User ipAddress
     * @param tsCsVersionAccepted tsCsVersionAccepted
     * @throws IllegalArgumentException exception thrown for
     * illegal argument
     */
    protected void assertRegisterPreconditions(
    final LoginDetails loginDetails, final String emailAddress,
    final PersonalDetails personalDetails,
    final SecurityChallengeDetails usernameChallenge,
    final SecurityChallengeDetails passwordChallenge,
    final AddressDetails addressDetails, final String ipAddress,
    final String tsCsVersionAccepted
    ) throws IllegalArgumentException{
        if (loginDetails == null
        || StringUtils.isBlank(loginDetails.getUsername()) || StringUtils
        .isBlank(loginDetails.getPassword())) {
            throw new IllegalArgumentException(
            "Login details invalid:" + loginDetails
            );
        }
        if (StringUtils.isBlank(emailAddress)) {
            throw new IllegalArgumentException(
            "Email address invalid:" + emailAddress
            );
        }
        if (personalDetails == null) {
            throw new IllegalArgumentException(
            "Personal details invalid:" + personalDetails
            );
        }
        if (usernameChallenge == null) {
            throw new IllegalArgumentException(
            "Username security challenge invalid: " + usernameChallenge
            );
        }
        if (passwordChallenge == null) {
            throw new IllegalArgumentException(
            "Password security challenge invalid:" + passwordChallenge
            );
        }
        if (addressDetails == null) {
            throw new IllegalArgumentException(
            "Address details challenge invalid:" + addressDetails
            );
        }
        if (StringUtils.isBlank(ipAddress)) {
            throw new IllegalArgumentException(
            "IP address invalid:" + ipAddress
            );
        }
        if (StringUtils.isBlank(tsCsVersionAccepted)) {
            throw new IllegalArgumentException(
            "TsCs version invalid:" + tsCsVersionAccepted
            );
        }
    }

    /**.
     * Updates the password of this <code>AircellUser</code> object. This method
     * exists to support synching of password changes between external systems
     * and the Portal web application. This method should only be called in the
     * <code>LOGGED_IN</code>. In other words, only logged in users can sync
     * their passwords.
     * The <code>ServiceResponse</code> contains error codes of type {@link
     * com.aircell.abp.service.AAAErrorCode}.
     * @param passwordUpdate Updated password
     * @return ServiceResponse
     * @throws IllegalArgumentException exception thrown for
     * illegal argument
     */
    public ServiceResponse updatePassword(final String passwordUpdate){
        if (StringUtils.isBlank(passwordUpdate)) {
            throw new IllegalArgumentException(
            "Password update argument " + "cannot be null."
            );
        }
        assertState(State.LOGGED_IN);

        final LoginDetails newLoginDetails =
        new LoginDetails(getUsername(), passwordUpdate);

        final ServiceResponse retval = getLoginService().login(newLoginDetails);

        if (retval.isSuccess()) {
            this.loginDetails.setPassword(passwordUpdate);
            logger.info(
            "Password successfully synched for user '{}' following "
            + "change in BSS.", this
            );
        } else {
            logger.info(
            "Password NOT successfull synched for user '{}' following "
            + "change in the BSS. The user supplied incorrect password", this
            );
        }
        return retval;
    }

    /**.
     * Returns the current value of the security question the password.
     * Please make sure you call this method <strong>after</strong> either
     * {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenUsername(String)}
     * or {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenPassword(String)}.
     * This will make the security challenge details become available to this
     * object.
     * @return the current value of the security question or null of the
     *         security question for password has not been retrieved
     */
    public String getSecurityQuestionPassword() {
        return getPasswordChallenge() != null
        ? getPasswordChallenge().getQuestion() : null;
    }

    /**.
     * Returns the current value of the security question the username.
     * Please make sure you call this method <strong>after</strong> either
     * {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenUsername(String)}
     * or {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenPassword(String)}.
     * This will make the security challenge details become available to this
     * object.
     * @return the current value of the security question or null of the
     *         security question for username has not been retrieved
     */
    public String getSecurityQuestionUsername() {
        return getUsernameChallenge() != null
        ? getUsernameChallenge().getQuestion() : null;
    }

    /**.
     * Returns the current value of the card number security question.
     * Please make sure you call this method <strong>after</strong> either
     * {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenUsername(String)}
     * or {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenPassword(String)}.
     * This will make the security challenge details become available to this
     * object.
     * If the user doesn't have card details saved with the system then he will
     * not be required to answer this security question. In that case this
     * method will return null.
     * @return the current value of the security question or null if this user
     *         is not required to answer card related security question.
     */
    public String getSecurityQuestionCardNumber() {
        return getCardNumberChallenge() != null
        ? getCardNumberChallenge().getQuestion() : null;
    }

    /**.
     * Returns the current value of the card number security question.
     * Please make sure you call this method <strong>after</strong> either
     * {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenUsername(String)}
     * or {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenPassword(String)}.
     * This will make the security challenge details become available to this
     * object.
     * If the user doesn't have card details saved with the system then he will
     * not be required to answer this security question. In that case this
     * method will return null.
     * @return the current value of the security question or null if this user
     *         is not required to answer card related security question.
     */
    public String getSecurityQuestionAddress() {
        return getAddressChallenge() != null
        ? getAddressChallenge().getQuestion() : null;
    }

    /**.
     * Returns security challenge details options for the username remainder.
     * The user can then choose which question to use as the challenge for
     * username remainder.
     * @return list of <code>SecurityChallengeDetails</code> objects or null if
     *         there are no options available.
     * @param locale locale
     * @param tracking TrackingType
     */
    public SecurityChallengeDetails[] getSecurityChallengeOptionsUsername(
    String locale, TrackingType tracking
    ) {
        final ServiceResponse<SecurityChallengeDetails[]> response =
        getUserDetailsService().getSecurityQuestionOptions(
        SecurityChallengeType.USERNAME_CHALLENGE, locale, tracking
        );
        if (response.isSuccess()) {
            return response.getPayload();
        } else {
            logger.error(
            "Unable to obtain list of options for username security challenge. "
            + "Error code '{}', service response '{}'", response.getErrorCode(),
            response
            );
            return null;
        }
    }

    /**.
     * Returns security challenge details options for the password remainder.
     * The user can then choose which question to use as the challenge for
     * username remainder.
     * @return list of <code>SecurityChallengeDetails</code> objects or null if
     *         there are no options available.
     * @param locale locale
     * @param tracking TrackingType
     */
    public SecurityChallengeDetails[] getSecurityChallengeOptionsPassword(
    String locale, TrackingType tracking
    ) {
        final ServiceResponse<SecurityChallengeDetails[]> response =
        getUserDetailsService().getSecurityQuestionOptions(
        SecurityChallengeType.PASSWORD_CHALLENGE, locale, tracking
        );
        if (response.isSuccess()) {
            return response.getPayload();
        } else {
            logger.error(
            "Unable to obtain list of options for password security challenge. "
            + "Error code '{}', service response '{}'", response.getErrorCode(),
            response
            );
            return null;
        }
    }

    /**.
     * Retreives forgotten username for a given email address.
     * If the outcome of this call is successfull (indicated by the
     * <code>ServiceResponse</code> object returned), then the the following
     * properties of this object will be set: <ul> <li><code>emailAddress</code>
     * will be set to the value of the emailAddress parameter supplied.</li>
     * <li><code>username</code> will be set to the username retrieved for the
     * email address given.</li> <li><code>usernameChallenge</code> will be set
     * to the challenge for this user, but only the <code>id</code> and
     * <code>question</code> properties</li> <li><code>passwordChallenge</code>
     * will be set to the challenge for this user, but only the <code>id</code>
     * and <code>question</code> properties</li> </ul>
     * This call might fail because the emailAddress supplied by the user might
     * not one of a registered user.
     * @param emailAddress to retrieve the forgotten username for
     * @param locale locale
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object with a boolean flag as
     *         the payload indicating correct answer
     * @throws IllegalArgumentException if the emailAddress parameter was blank
     * state
     */
    public ServiceResponse retrieveForgottenUsername(
    final String emailAddress, final String locale, final TrackingType tracking
    ) throws IllegalArgumentException{
        if (StringUtils.isBlank(emailAddress)) {
            throw new IllegalStateException(
            "Unable to retrieve forgotten username "
            + "before email address has been set."
            );
        }
        assertState(State.NOT_LOGGED_IN);

        final ServiceResponse<String> retrieveResponse = getUserDetailsService()
        .retrieveUsernameForEmailAddress(emailAddress, tracking);

        if (retrieveResponse.isSuccess()) {
            final String username = retrieveResponse.getPayload();
            final ServiceResponse<SecurityChallengeDetails[]>
            challengeResponse = getUserDetailsService()
            .getSecurityQuestions(username, locale, tracking, emailAddress);

            if (challengeResponse.isSuccess()) {
                this.loginDetails.setUsername(username);
                this.emailAddress = emailAddress;
                setAllSecurityChallenges(challengeResponse.getPayload());
            }
            return challengeResponse;
        } else {
            return retrieveResponse;
        }
    }

    /**.
     * Validates the <code>securityAnswer</code> for forgotten username. The
     * <code>emailAddress</code> and <code>usernameChallenge</code> must be set
     * prior to making this call; otherwise an exception will be thrown. This
     * means that the {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenUsername(String)}
     * method must be called on this object beforehand.
     * Please don't confuse the <code>success</code> property of the
     * <code>ServiceResponse</code> object returned indicating success of
     * <strong>this call</strong> for the <code>payload</code> flag which
     * indicates if the security answer was answered correctly or not.
     * If the security question was answered correctly then this object will
     * have the <code>username</code> property set. Use this property to get
     * hold of the username forgotten.
     * @param securityAnswer answer to the username security question.
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object with a boolean flag as
     *         the payload indicating correct answer
     * security challenge details previously set.
     */
    public ServiceResponse<Boolean> validateForgottenUsername(
    final String securityAnswer, final TrackingType tracking
    ) {
        if (StringUtils.isBlank(securityAnswer)) {
            throw new IllegalArgumentException(
            "security answer cannot be null"
            );
        }
        final SecurityChallengeDetails usernameChallenge =
        getUsernameChallenge();
        if (StringUtils.isBlank(getUsername()) || usernameChallenge == null) {
            throw new IllegalStateException(
            "username and username security challenge"
            + "have to be previously set."
            );
        }
        usernameChallenge.setAnswer(securityAnswer);
        final ServiceResponse<Boolean> retval = getUserDetailsService()
        .checkSecurityQuestions(getUsername(), usernameChallenge, tracking);
        return retval;
    }

    /**.
     * Retreives forgotten password for a given username.
     * If the outcome of this call is successfull (indicated by the
     * <code>ServiceResponse</code> object returned), then the the following
     * properties of this object will be set: <ul> <li><code>username</code>
     * will be set to the value of the username argument supplied.</li>
     * <li><code>usernameChallenge</code> will be set to the challenge for this
     * user, but only the <code>id</code> and <code>question</code>
     * properties</li> <li><code>passwordChallenge</code> will be set to the
     * challenge for this user, but only the <code>id</code> and
     * <code>question</code> properties</li> </ul>
     * This call might fail because the username supplied by the user might not
     * one of a registered user.
     * @param username to retrieve the forgotten password for
     * @param locale local
     * @param tracking TrackingType
     * @param emailAddress emailAddress
     * @return the <code>ServiceResponse</code> object with a boolean flag as
     *         the payload indicating correct answer
     * @throws IllegalArgumentException if the username parameter was blank
     * @throws IllegalStateException if called in non <code>NOT_LOGGED_IN</code>
     * state
     */
    public ServiceResponse retrieveForgottenPassword(
    final String username, final String locale, final TrackingType tracking,
    final String emailAddress
    ) throws IllegalArgumentException, IllegalStateException{
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException(
            "username argument cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);

        final ServiceResponse<SecurityChallengeDetails[]> retval =
        getUserDetailsService()
        .getSecurityQuestions(username, locale, tracking, emailAddress);
        if (retval.isSuccess()) {
            this.loginDetails.setUsername(username);
            setAllSecurityChallenges(retval.getPayload());
        }
        return retval;
    }

    /**.
     * Validates the <code>securityAnswer</code> for forgotten password. The
     * <code>username</code> and <code>passwordChallenge</code> must be set
     * prior to making this call; otherwise an exception will be thrown. This
     * means that the {@link com.aircell.abp.model.AircellUser#
     * retrieveForgottenPassword(String)}
     * method must be called on this object beforehand.
     * Please don't confuse the <code>success</code> property of the
     * <code>ServiceResponse</code> object returned indicating success of
     * <strong>this call</strong> for the <code>payload</code> flag which
     * indicates if the security answer was answered correctly or not.
     * If the security question was answered correctly then this object will
     * have the following: <ul> <li><code>username</code> property set</li>
     * <li><code>state</code> set to
     * <code>State.PASSWORD_RESET_CHALLENGE_DONE</code></li>
     * </ul>
     * @param securityAnswer answer to the username security question.
     * @param lastCardDigits last four Card Digits
     * @param zipCode zipCode
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object with a boolean flag as
     *         the payload indicating correct answer
     * @throws IllegalStateException if this object doesn't have username or
     * security challenge details previously set, or if this user is not in the
     * <code>NOT_LOGGED_IN</code> state.
     * @throws IllegalArgumentException if any of the arguments are blank
     */
    public ServiceResponse<Boolean> validateForgottenPassword(
    final String securityAnswer, final String lastCardDigits,
    final String zipCode, final TrackingType tracking
    ) throws IllegalArgumentException, IllegalStateException{
        if (StringUtils.isBlank(securityAnswer)
        || StringUtils.isBlank(lastCardDigits) || StringUtils
        .isBlank(zipCode)) {
            throw new IllegalArgumentException("None of the args can be null");
        }
        assertState(State.NOT_LOGGED_IN);

        if (StringUtils.isBlank(getUsername()) || passwordChallenge == null
        || cardNumberChallenge == null || addressChallenge == null) {
            throw new IllegalStateException(
            "Username and the challenge objects has "
            + "to be set on this object before "
            + "calling this method. Check your workflow.");
        }

        passwordChallenge.setAnswer(securityAnswer);
        if (!SecurityChallengeType.PASSWORD_CHALLENGE
        .equals(passwordChallenge.getType())) {
            logger.warn(
            "The current password challenge object "
            + "doesn't have correct type attribute '{}'. Check your workflow.",
            passwordChallenge.getType()
            );
            passwordChallenge.setType(SecurityChallengeType.PASSWORD_CHALLENGE);
        }

        cardNumberChallenge.setAnswer(lastCardDigits);
        if (!SecurityChallengeType.CARD_NUMBER_CHALLENGE
        .equals(cardNumberChallenge.getType())) {
            logger.warn(
            "The current card challenge object "
            + "doesn't have correct type attribute '{}'. Check your workflow.",
            cardNumberChallenge.getType()
            );
            cardNumberChallenge
            .setType(SecurityChallengeType.CARD_NUMBER_CHALLENGE);
        }

        addressChallenge.setAnswer(zipCode);
        if (!SecurityChallengeType.ADDRESS_CHALLENGE
        .equals(addressChallenge.getType())) {
            logger.warn(
            "The current address challenge object "
            + "doesn't have correct type attribute '{}'. Check your workflow.",
            addressChallenge.getType()
            );
            addressChallenge.setType(SecurityChallengeType.ADDRESS_CHALLENGE);
        }

        final SecurityChallengeDetails[] answers =
        new SecurityChallengeDetails[]{
        passwordChallenge, cardNumberChallenge, addressChallenge
        };

        return validateForgottenPasswordAnswers(answers, tracking);
    }

    /**.
     * This version of the method does not require answers to the card number
     * and address security challenge.
     * This method is used for cases where the user wants to retrieve forgotten
     * password but haven't saved his card details with the system and therefore
     * does not have card details.
     * @param securityAnswer
     * @param tracking
     * @return ServiceResponse
     * @see com.aircell.abp.model.AircellUser#validateForgottenPassword(String,
     *      String, String)
     */
    public ServiceResponse<Boolean> validateForgottenPassword(
    final String securityAnswer, final TrackingType tracking
    ) {
        // TODO miro: this method and the
        // TODO one above could be refactored into a common one.
        if (StringUtils.isBlank(securityAnswer)) {
            throw new IllegalArgumentException("None of the args can be null");
        }
        assertState(State.NOT_LOGGED_IN);

        if (StringUtils.isBlank(getUsername()) || passwordChallenge == null) {
            throw new IllegalStateException(
            "Username and the challenge objects has "
            + "to be set on this object before calling this method."
            + " Check your workflow.");
        }

        passwordChallenge.setAnswer(securityAnswer);
        if (!SecurityChallengeType.PASSWORD_CHALLENGE
        .equals(passwordChallenge.getType())) {
            logger.warn(
            "The current password challenge object "
            + "doesn't have correct type attribute '{}'. Check your workflow.",
            passwordChallenge.getType()
            );
            passwordChallenge.setType(SecurityChallengeType.PASSWORD_CHALLENGE);
        }

        final SecurityChallengeDetails[] answers =
        new SecurityChallengeDetails[]{passwordChallenge };

        return validateForgottenPasswordAnswers(answers, tracking);
    }

    /**.
     * Calls the user details service to validate supplied answers to security
     * questions.
     * Updates the state of this object to <code>
     * State.PASSWORD_RESET_CHALLENGE_DONE</code>
     * if the response from the user details service is affirmative.
     * @param answers <code>SecurityChallengeDetails</code> objects with
     * <code>answer</code> property populated
     * @param tracking TrackingType
     * @return response from the user details service
     * @throws IllegalArgumentException
     */
    protected ServiceResponse<Boolean> validateForgottenPasswordAnswers(
    final SecurityChallengeDetails[] answers, final TrackingType tracking
    ) throws IllegalArgumentException{
        if (answers == null) {
            throw new IllegalArgumentException(
            "securityChallengeDetails "
            + "answers array argument cannot be null"
            );
        }
        final ServiceResponse<Boolean> retval = getUserDetailsService()
        .checkSecurityQuestions(getUsername(), answers, tracking);

        if (retval.isSuccess() && Boolean.TRUE.equals(retval.getPayload())) {
            setState(State.PASSWORD_RESET_CHALLENGE_DONE);
        }
        return retval;
    }

    /**.
     * Resets the password for this user following a successful completion of
     * the password challenge.
     * This method should only be called if the
     *  {@link com.aircell.abp.model.AircellUser#
     * validateForgottenPassword(String,
     * String, String)} has returned a successfull outcome, otherwise an
     * exception will be thrown.
     * After this call, this object will be returned back into the
     * <code>State.NOT_LOGGED_IN</code> state. Regarless of the outcome of this
     * call. The implication of this is that the user journey to reset the
     * password should be started over-again with a new AircellUser object. And
     * discard this one rather than re-use it.
     * @param newPassword the password to be set for this user.
     * @param tracking TrackingType
     * @return the <code>ServiceResponse</code> object indicating the outcome of
     *         this operation
     * @throws IllegalArgumentException if the newPassword is blank
     * @throws IllegalStateException if this object is not in the state of
     * <code>State.PASSWORD_RESET_CHALLENGE_DONE</code>
     */
    public ServiceResponse resetPasswordFollowingChallenge(
    final String newPassword, final TrackingType tracking
    ) throws IllegalArgumentException, IllegalStateException{
        if (StringUtils.isBlank(newPassword)) {
            throw new IllegalArgumentException(
            "newPassword argument " + "cannot be null"
            );
        }
        if (StringUtils.isBlank(getUsername())) {
            throw new IllegalStateException(
            "Cannot reset password - username " + "has not been set"
            );
        }
        assertState(State.PASSWORD_RESET_CHALLENGE_DONE);

        // resets the state of this object regardless
        // if this call failed or not.
        // the implication is that the user journey
        //  should be restarted if the call
        // fails
        setState(State.NOT_LOGGED_IN);
        final ServiceResponse retval = getUserDetailsService()
        .resetPassword(getUsername(), newPassword, tracking);
        return retval;
    }

    /**.
     * Utility method to set the security challenge
     *  properties given an array of
     * these objects.
     * This method amends the <code>usernamChallenge</code> and
     * <code>passwordChallenge</code> properties.
     * @param challenges Security question's answers
     * @throws IllegalArgumentException if
     */
    protected void setAllSecurityChallenges(
    final SecurityChallengeDetails[] challenges
    ) throws IllegalArgumentException{
        boolean uChlng = false;
        boolean pChlng = false;
        if (challenges != null && challenges.length > 0) {
            for (SecurityChallengeDetails q : challenges) {
                final SecurityChallengeType t = q != null ? q.getType() : null;
                if (SecurityChallengeType.USERNAME_CHALLENGE.equals(t)) {
                    this.usernameChallenge = q;
                    uChlng = true;
                } else if (SecurityChallengeType.PASSWORD_CHALLENGE.equals(t)) {
                    this.passwordChallenge = q;
                    pChlng = true;
                } else if (SecurityChallengeType.ADDRESS_CHALLENGE.equals(t)) {
                    this.addressChallenge = q;
                } else if (SecurityChallengeType.CARD_NUMBER_CHALLENGE
                .equals(t)) {
                    this.cardNumberChallenge = q;
                } else {
                    throw new IllegalArgumentException(
                    "Unsupported SecurityChallengeType: " + t
                    );
                }
            }
        }
        if (!uChlng || !pChlng) {
            throw new IllegalArgumentException(
            "Not all required security challenge details present: " + challenges
            );
        }
    }

    /**.
     * Retrieves a stored creditcard (including billing address) for use in the
     * purchase path. It does only return data for display purpose and for using
     * the card, the card id is the key. Note: in this phase only one card is
     * stored even though the service used is capable of more.
     * @param tracking TrackingType
     * @return null (no stored  card or not available) or stored card.
     */
    public CreditCardDetails getStoredCreditCardDetails(TrackingType tracking){
        ServiceResponse<CreditCardDetails[]> response =
        paymentService.retrieveStoredCreditCards(getUsername(), tracking);
        if (null != response && response.isSuccess()
        && null != response.getPayload() && response.getPayload().length > 0) {
            CreditCardDetails cardDetails = response.getPayload()[ 0 ];
            cardDetails.setSavedCardFlag(true);
            return cardDetails;
        } else {
            return null;
        }
    }

    /**.
     * Retrieves the specified credit card for update purposes
     * @param cardId The BSS card identifier
     * @param tracking is a unique id to follow transaction with ESB
     * @return null (No specified card or not available) or specified card
     */
    public CreditCardDetails getCreditCardDetails(
    final String cardId, final TrackingType tracking
    ) {
        final ServiceResponse<CreditCardDetails[]> response =
        paymentService.getCreditCardDetails(cardId, getUsername(), tracking);
        if (null != response && response.isSuccess()
        && null != response.getPayload() && response.getPayload().length > 0) {
            return response.getPayload()[ 0 ];
        } else {
            return null;
        }
    }

    /**.
     * Adds the  credit card (specified) with the specified credit card details
     * @param ccDetails The updated credit card details
     * @param tracking user tracking
     * @return response the <code>ServiceResponse</code> object
     * indicating the outcome of
     *         this operation
     */
    public ServiceResponse<CreditCardDetails[]> addCreditCardDetails(
    final CreditCardDetails ccDetails, final TrackingType tracking
    ) {
        final ServiceResponse<CreditCardDetails[]> response =
        paymentService.addCreditCardDetails(getUsername(), ccDetails, tracking);
        return response;
    }

    /**.
     * Updates the existing credit card (specified) with the specified credit
     * card details
     * @param cardId The existing credit card BSS id
     * @param ccDetails The updated credit card details
     * @param tracking user tracking
     * @return response the <code>ServiceResponse</code> object
     */
    public ServiceResponse<CreditCardDetails[]> updateCreditCardDetails(
    final String cardId, final CreditCardDetails ccDetails,
    final TrackingType tracking
    ) {
        return paymentService
        .updateCreditCardDetails(cardId, getUsername(), ccDetails, tracking);
    }

    /**.
     * Removes the credit card specified from the BSS
     * @param cardId The credit card BSS id to be removed
     * @param tracking TrackingType
     * @return  response the <code>ServiceResponse</code> object
     */
    public ServiceResponse<CreditCardDetails[]> removeCreditCardDetails(
    final String cardId, final TrackingType tracking
    ) {
        return paymentService.removeCreditCardDetails(cardId,
            getUsername(), tracking);
    }

    /**.
     * Gets the available Subscriptions
     * @param Locale Locale
     * @param tracking TrackingType
     * @param deviceType deviceType, mobile or laptop
     * @return the <code>ServiceResponse</code> object indicating the outcome of
     *         this operation
     */
    public ServiceResponse<Subscription[]> getAvailableSubscriptions(
    String Locale, TrackingType tracking, String deviceType
    ) {
        assertState(State.LOGGED_IN);
        final ServiceResponse<Subscription[]> retval =
        getSubscriptionService().getAvailableSubscriptions(
        getLoginDetails().getUsername(), Locale, tracking, deviceType
        );
        return retval;
    }

    /**.
     * Creates new Order
     * @return Order <code>Order</code> object
     */
    public Order createNewOrder() {
        assertState(State.LOGGED_IN);
        final SimpleOrder order = new SimpleOrder(this);
        setOrder(order);
        return getOrder();
    }

    // ********************************************************
    // *** getters/setters ************************************
    // ********************************************************
    /**.
     * Checks if terms and conditions accepted or not
     * @return boolean
     */
    public boolean isAcceptedTerms() {
        return isLoggedIn();
    }

    /**.
     * Checks if user is logged in or not
     * @return boolean
     */
    public boolean isLoggedIn() {
        return State.LOGGED_IN.equals(getState());
    }
    /**.
     * Checks if entered captcha is correct or not
     * @return boolean
     */
    public boolean isCaptchaPassed() {
        return this.captchaPassed;
    }

    /**.
     * Sets <code>true</code> if entered captcha is correct.
     * and <code>false</code> if entered captcha is not correct.
     * @param captchaPassed true if captcha is correct, or else false
     */
    public void setCaptchaPassed(final boolean captchaPassed){
        if (captchaPassed) {
            assertState(State.LOGGED_IN);
            this.captchaPassed = true;
        } else {
            this.captchaPassed = false;
        }

    }

    /**.
     * Checks if there is any activation error
     * @return boolean
     */
    public boolean hasActivateError() {
        return (this.activateError != null);
    }

    /**.
     * Gets Acrivation error
     * @return String Acrivation
     */
    public String getActivateError() {
        return this.activateError;
    }

    /**.
     * Sets activation error
     * @param activateError activateError
     */
    public void setActivateError(String activateError){
        this.activateError = activateError;
    }

    /**.
     * Gets email address
     * @return String Email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**.
     * Sets email address
     * @param emailAddress Email address
     */
    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**.
     * Gets phone number
     * @return String Phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**.
     * Sets the phone number
     * @param phoneNumber Phone number
     */
    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**.
     * Gets password
     * @return String Password
     */
    public String getPassword() {
        return loginDetails != null ? loginDetails.getPassword() : null;
    }

    /**.
     * Sets the password. This method exists so this object can be used as a
     * command or form backing object in Spring MVC. Password for this object
     * should be updated through the <code>login</code> or
     * <code>resetPasswordFollowingChallenge</code> methods.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param password must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setPassword(final String password)
    throws IllegalArgumentException, IllegalStateException{
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException(
            "Password argument cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        loginDetails.setPassword(password);
    }

    /**.
     * Gets User name
     * @return String User name
     */
    public String getUsername() {
        return loginDetails != null ? loginDetails.getUsername() : null;
    }

    /**.
     * Sets the username. This method exists so this object can be used as a
     * command or form backing object in Spring MVC. Password for this object
     * should be updated through the <code>login</code> or
     * <code>retrieveForgottenUsername</code> methods.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param username must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setUsername(final String username)
    throws IllegalArgumentException, IllegalStateException{
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException(
            "Username argument cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        loginDetails.setUsername(username);
    }

    /**.
     * Gets IP address
     * @return String IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**.
     * Sets IP address
     * @param ipAddress IP address
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * . Gets userMac
     * @return String userMac
     */
    public String getUserMac() {
        return this.userMac;
    }

    /**
     * . Sets userMac
     * @param userMac
     */
    public void setUserMac(final String userMac) {
        this.userMac = userMac;
    } 
    
    /**
     * Gets the state stastus
     * @return String stateStatus
     */
    public String getStateStatus() {

        return stateStatus;
    }
    /**.
     * Sets Statestatus
     * @param s Statestatus
     */
    public void setStateStatus(String s){
        this.stateStatus = s;
    }

    /**.
     * Gets <code>State</code> object
     * @return State <code>State</code> object
     */
    public State getState() {
        return state;
    }

    /**.
     * Sets State
     * @param state <code>State</code> object
     */
    public void setState(final State state) {
        setStateStatus(state.getState());
        this.state = state;
    }

    /**.
     * Sets State
     * @param state State
     */
    public void setState(final String state) {
        if (state == null) {
            return;
        }
        boolean validValue = false;
        if (State.valueOf(state).equals(State.LOGGED_IN)) {
            this.state = State.LOGGED_IN;
            validValue = true;
        } else if (State.valueOf(state).equals(State.NOT_LOGGED_IN)) {
            this.state = State.NOT_LOGGED_IN;
            validValue = true;
        } else if (State.valueOf(state)
        .equals(State.PASSWORD_RESET_CHALLENGE_DONE)) {
            this.state = State.PASSWORD_RESET_CHALLENGE_DONE;
            validValue = true;
        }
        if (validValue) {
            setStateStatus(state);
        }

    }

    /**.
     * Gets <code>AddressDetails</code> bean
     * @return AddressDetails <code>AddressDetails</code> bean
     */
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    /**.
     * Sets <code>AddressDetails</code> bean
     * @param addressDetails <code>AddressDetails</code> bean
     */
    public void setAddressDetails(final AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    /**.
     * Gets <code>PersonalDetails</code> bean
     * @return PersonalDetails <code>PersonalDetails</code> bean
     */
    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    /**.
     * Sets <code>PersonalDetails</code> bean
     * @param personalDetails <code>PersonalDetails</code> bean
     */
    public void setPersonalDetails(final PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    /**.
     * Gets <code>PasswordChallenge</code> bean
     * @return SecurityChallengeDetails
     */
    public SecurityChallengeDetails getPasswordChallenge() {
        return passwordChallenge;
    }

    /**.
     * Sets the password challenge. This method exists so this object can be
     * used as a command or form backing object in Spring MVC.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param passwordChallenge must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setPasswordChallenge(
    final SecurityChallengeDetails passwordChallenge
    ) {
        if (passwordChallenge == null) {
            throw new IllegalArgumentException(
            "passwordChallenge argument " + "cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        this.passwordChallenge = passwordChallenge;
    }

    /**.
     * Gets UsernameChallenge questions
     * @return SecurityChallengeDetails
     */
    public SecurityChallengeDetails getUsernameChallenge() {
        return usernameChallenge;
    }

    /**.
     * Sets the username challenge. This method exists so this object can be
     * used as a command or form backing object in Spring MVC.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param usernameChallenge must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setUsernameChallenge(
    final SecurityChallengeDetails usernameChallenge
    ) throws IllegalArgumentException, IllegalStateException{
        if (usernameChallenge == null) {
            throw new IllegalArgumentException(
            "usernameChallenge argument  " + "cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        this.usernameChallenge = usernameChallenge;
    }

    /**.
     * Gets AddressChallenge
     * @return addressChallenge
     */
    public SecurityChallengeDetails getAddressChallenge() {
        return addressChallenge;
    }

    /**.
     * Sets the address challenge. This method exists so this object can be used
     * as a command or form backing object in Spring MVC.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param addressChallenge must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setAddressChallenge(
    final SecurityChallengeDetails addressChallenge
    ) throws IllegalArgumentException, IllegalStateException{
        if (addressChallenge == null) {
            throw new IllegalArgumentException();
        }
        assertState(State.NOT_LOGGED_IN);
        this.addressChallenge = addressChallenge;
    }

    /**.
     * Gets CardNumberChallenge
     * @return cardNumberChallenge
     */
    public SecurityChallengeDetails getCardNumberChallenge() {
        return cardNumberChallenge;
    }

    /**.
     * Sets the card number challenge. This method exists so this object can be
     * used as a command or form backing object in Spring MVC.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param cardNumberChallenge must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setCardNumberChallenge(
    final SecurityChallengeDetails cardNumberChallenge
    ) throws IllegalStateException, IllegalArgumentException{
        if (cardNumberChallenge == null) {
            throw new IllegalArgumentException(
            "cardNumberChallenge argument " + "cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        this.cardNumberChallenge = cardNumberChallenge;
    }

    /**.
     * Gets <code>LoginDetails</code> bean
     * @return LoginDetails
     */
    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    /**.
     * Sets the login details for this object. This method exists so this object
     * can be used as a command or form backing object in Spring MVC.
     * Must only be called in <code>State.NOT_LOGGED_IN</code> state. In other
     * words just after this object was created.
     * @param loginDetails must not be null
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setLoginDetails(final LoginDetails loginDetails)
    throws IllegalArgumentException, IllegalStateException{
        if (loginDetails == null) {
            throw new IllegalArgumentException(
            "LoginDetails argument cannot be null"
            );
        }
        assertState(State.NOT_LOGGED_IN);
        this.loginDetails = loginDetails;
    }

    /**.
     * Sets the login details for this object. This method exists so this object
     * can be used as a command or form backing object in Spring MVC. This
     * method will be used only in the forgot password flow of forced
     * authentication for my account. So this method may be called in
     * State.LOGGED_IN state.
     * @param loginDetails must not be null
     * @param forcedAuth forced authentication
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public void setLoginDetails(
    final LoginDetails loginDetails, boolean forcedAuth
    ) throws IllegalArgumentException, IllegalStateException{
        if (loginDetails == null) {
            throw new IllegalArgumentException(
            "LoginDetails argument cannot be null"
            );
        }

        if (!forcedAuth) {
            assertState(State.NOT_LOGGED_IN);
        }
        this.loginDetails = loginDetails;
    }

    /**.
     * Gets <code>Order</code> bean
     * @return Order
     */
    public Order getOrder() {
        return order;
    }

    /**.
     * Sets Order
     * @param order order
     */
    public void setOrder(final Order order){
        this.order = order;
    }

    // ********************************************************************
    // *** Spring depenency injection via AOP
    // ********************************************************************

    /**.
     * Gets LoginService
     * @return LoginService
     */
    LoginService getLoginService() {
        return loginService;
    }

    /**.
     * Sets LoginService
     * @param loginService loginService object
     */
    public void setLoginService(final LoginService loginService){
        this.loginService = loginService;
    }

    /**.
     * Gets UserDetailsService
     * @return UserDetailsService
     */
    UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    /**.
     * Sets UserDetailsService
     * @param userDetailsService userDetailsService object
     */
    public void setUserDetailsService(
    final UserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    /**.
     * Gets SubscriptionService
     * @return SubscriptionService
     */
    SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    /**.
     * Sets SubscriptionService
     * @param subscriptionService subscriptionService object
     */
    public void setSubscriptionService(
    final SubscriptionService subscriptionService
    ) {
        this.subscriptionService = subscriptionService;
    }

    /**.
     * Gets PaymentService
     * @return PaymentService
     */
    public PaymentService getPaymentService() {
        return paymentService;
    }

    /**.
     * Sets PaymentService
     * @param paymentService paymentService object
     */
    public void setPaymentService(final PaymentService paymentService){
        this.paymentService = paymentService;
    }

    /**.
     * Asserts if State is null or not a state of list
     * @param state state
     * @throws IllegalStateException
     */
    protected void assertState(
    final State state
    ) throws IllegalStateException{
        if (state == null) {
            throw new IllegalArgumentException(
            "the state argument is required"
            );
        }
        if (!state.equals(getState())) {
            throw new IllegalStateException(
            "Aircell user is not in the " + "required state of '"
            + state.getState() + "', but is in the state of '" + getState()
            + "'."
            );
        }
    }

    /**.
     * Asserts if State is null or an unallowed state
     * @param allowedStates allowed states
     */
    protected void assertState(final State[] allowedStates){
        if (allowedStates == null) {
            throw new IllegalArgumentException(
            "States argument cannot be null"
            );
        }
        boolean inOneOfTheStates = false;
        for (State s : allowedStates) {
            if (s.equals(getState())) {
                inOneOfTheStates = true;
            }
        }
        if (!inOneOfTheStates) {
            throw new IllegalStateException(
            "Aircell user is not in the " + "required state of '"
            + allowedStates + "', but is in the state of '" + getState() + "'."
            );
        }
    }

    /**.
     * Methode to override superclass method
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**.
     * Validates the username
     * @param userName Username
     * @param tracking user tracking
     * @return the <code>ServiceResponse</code> object indicating the outcome of
     *         this operation
     */
    public ServiceResponse<Boolean> validateUsername(
    final String userName, final TrackingType tracking
    ) {

        if (StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException(
            "security answer is not available"
            );
        }
        final ServiceResponse<Boolean> retval =
        getUserDetailsService().validateUser(userName, tracking);
        return retval;
    }


    /**.
     * Retrieves User Terms Conditions
     * @param username Username
     * @param tracking user tracking
     * @return the <code>ServiceResponse</code> object indicating the outcome of
     *         this operation
     * @throws IllegalArgumentException thrown when illegal argument passed
     */
    public ServiceResponse retrieveUserTermsandConditions(
    final String username, final TrackingType tracking
    ) throws IllegalArgumentException{
        if (StringUtils.isBlank(username)) {
            throw new IllegalStateException(
            "Unable to retrieve Terms and Conditions "
            + "before User Name has been set."
            );
        }
        final ServiceResponse<String> retrieveResponse =
        getUserDetailsService().getUserTermsAndConditions(username, tracking);
        return retrieveResponse;
    }
    //Roaming Partner Changes
	/**Gets the count of how many times how many times the 
	 * smart client has polled the captcha page
	 * @return captchaPollRetryCount
	 */
	public int getCaptchaPollRetryCount() {
		return captchaPollRetryCount;
	}

	/**Sets the captcha poll retry count
	 * @param captchaPollRetryCount captchaPollRetryCount
	 */
	public void setCaptchaPollRetryCount(int captchaPollRetryCount) {
		this.captchaPollRetryCount = captchaPollRetryCount;
	}

	/**Gets Captcha Validation Status
	 * @return captchaValidationStatus
	 */
	public String getCaptchaValidationStatus() {
		return captchaValidationStatus;
	}

	/**Sets captchaValidationStatus
	 * @param captchaValidationStatus captchaValidationStatus
	 */
	public void setCaptchaValidationStatus(String captchaValidationStatus) {
		this.captchaValidationStatus = captchaValidationStatus;
	}

	/**Gets Smart Client Activation Value.
	 * @return smartClientActivation
	 */
	public int getSmartClientActivation() {
		return smartClientActivation;
	}

	/**Sets Smart Client Activation Value.
	 * @param smartClientActivation smartClientActivation.
	 */
	public void setSmartClientActivation(int smartClientActivation) {
		this.smartClientActivation = smartClientActivation;
	}
	
	
	//Roaming Partner Changes ends
    
}
