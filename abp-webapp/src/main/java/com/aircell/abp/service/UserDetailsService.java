/*
 * UserDetailsService.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import com.aircell.abp.model.AddressDetails;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.PersonalDetails;
import com.aircell.abp.model.SecurityChallengeDetails;
import com.aircell.abp.model.SecurityChallengeDetails.SecurityChallengeType;
import com.aircell.bss.ws.TrackingType;

/**.
 * Operations to deal with user details: terms and conditions, registering a new
 * user, re-setting terms and conditions etc.
 * @author miroslav.miladinovic
 */
public interface UserDetailsService {

    /**.
     * Retrieves the version number of terms and conditions this registered user
     * accepted.
     * @param username user id of the registered user.
     * @param tracking TrackingType
     * @return the service response with the terms and conditions version tag if
     *         this service request was successfull.
     */
    ServiceResponse<String> getUserTermsAndConditions(
    final String username, final TrackingType tracking
    );

    /**.
     * Registers a new user with the system. The user is making this call from
     * the ground based dot com Portal and is therefore required to supply
     * address details.
     * @param loginDetails LoginDetails object
     * @param email email address
     * @param personalDetails PersonalDetails object
     * @param address AddressDetails object
     * @param question SecurityChallengeDetails
     * @param tscsVersionTag tscs Version
     * @param marketingOptIn boolean value
     * @param tracking TrackingType
     * @param telephoneNumber telephone Number
     * @return the service response object with the internal user Id as the
     *         payload object if this request was success.
     * @throws IllegalArgumentException
     */
    ServiceResponse<Integer> registerNewUser(
    final LoginDetails loginDetails, final String email,
    final PersonalDetails personalDetails, final AddressDetails address,
    final String telephoneNumber, final SecurityChallengeDetails[] question,
    final String tscsVersionTag, final boolean marketingOptIn,
    final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Registers a new user with the system. The user is making a call from
     * in-flight and is therefore not required to supply address details.
     * @param loginDetails LoginDetails object
     * @param email email address
     * @param personalDetails PersonalDetails object
     * @param question SecurityChallengeDetails
     * @param tscsVersionTag tscs Version
     * @param marketingOptIn boolean value
     * @param tracking TrackingType
     * @return the service response object with the internal user Id as the
     *         payload object if this request was success.
     * @throws IllegalArgumentException
     */
    ServiceResponse<Integer> registerNewUser(
    final LoginDetails loginDetails, final String email,
    final PersonalDetails personalDetails,
    final SecurityChallengeDetails[] question, final String tscsVersionTag,
    final boolean marketingOptIn, final TrackingType tracking
    ) throws IllegalArgumentException;


    /**.
     * Sets the new version of terms and conditions accepted by this registered
     * user.
     * @param username username
     * @param version version
     * @param tracking TrackingType
     * @return the service response object
     */
    ServiceResponse setUserTermsAndConditions(
    final String username, final String version, final TrackingType tracking
    );

    /**.
     * Retrieves the username for the given email address. The emailAddress
     * given should be on of an already registered user.
     * The <code>ServiceResponse</code> object returned should indicate failure
     * in the following cases: <ul> <li>emailAddress doesn't exist </ul>
     * @param emailAddress emailAddress
     * @param tracking TrackingType
     * @return the service response object with the username set as the payload
     */
    ServiceResponse<String> retrieveUsernameForEmailAddress(
    final String emailAddress, final TrackingType tracking
    );

    /**.
     * Retrieves all security questions for the registered user. The
     * <code>SecurityChallengeDetails</code> objects will not be populated with
     * answers, but only with the <code>id</code> and <code>question</code>.
     * The <code>ServiceResponse</code> object returned should indicate failure
     * in the following cases: <ul> <li>username doesn't exist </ul>
     * @param username the username of the registered user
     * @param locale Locale
     * @param tracking TrackingType
     * @param emailAddress emailAddress
     * @return the service response object with an array of security challenge
     *         objects as the payload, if this request was success.
     * @throws IllegalArgumentException if the username is null or blank
     */
    ServiceResponse<SecurityChallengeDetails[]> getSecurityQuestions(
    final String username, final String locale, final TrackingType tracking,
    final String emailAddress
    ) throws IllegalArgumentException;

    /**.
     * Retrieves all security question options. These options can be presented
     * to the user to choose as the remainder questions for cases like forgotten
     * username or forgotten password.
     * The <code>SecurityChallengeDetails</code> objects will not be populated
     * with answers, but only with the <code>id</code> and
     * <code>question</code>.
     * @param Locale Locale
     * @param tracking TrackingType
     * @return the service response object with an array of security challenge
     *         objects as the payload, if this request was success.
     */
    ServiceResponse<SecurityChallengeDetails[]> getSecurityQuestionOptions(
    String Locale, TrackingType tracking
    );

    /**.
     * Retrieves security question options related to the specific type.
     * The <code>SecurityChallengeDetails</code> objects will not be populated
     * with answers, but only with the <code>id</code> and
     * <code>question</code>.
     * @param optionsType Security Challenge Type
     * @param Locale Locale
     * @param tracking TrackingType
     * @return the service response object with an array of security challenge
     *         objects as the payload, if this request was success.
     * @throws IllegalArgumentException if the type of questions requested is
     * not supported.
     */
    ServiceResponse<SecurityChallengeDetails[]> getSecurityQuestionOptions(
    SecurityChallengeType optionsType, String Locale, TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Checks if the security answers match the security questions for this
     * registered user.
     * Note: please don't confuse the success flag of this request/response with
     * the flag carried in the payload which denotes if the questions were
     * answered correctly!
     * The security challenge details objects given shuold at very least have
     * the <code>id</code> and <code>answer</code> properties set.
     * @param username the username of the registered user
     * @param tracking TrackingType
     * @param questionsAndAnswers the array of answers for security questions.
     * @return the service response object with the answers correct flag as the
     *         payload, if response was success.
     * @throws IllegalArgumentException if the username is null, or if the array
     * if security questions and answers doesn't have required data set.
     */
    ServiceResponse<Boolean> checkSecurityQuestions(
    final String username, final SecurityChallengeDetails[] questionsAndAnswers,
    final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     * Overloaded method for convenience. Checks a single security challenge.
     * @param username username
     * @param questionAndAnswer security question answer
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     * @see com.aircell.abp.service.UserDetailsService#
     * checkSecurityQuestions(String,
     *      SecurityChallengeDetails[])
     */
    ServiceResponse<Boolean> checkSecurityQuestions(
    final String username, final SecurityChallengeDetails questionAndAnswer,
    final TrackingType tracking
    );

    /**.
     * Resets the password for a registered user.
     * Caution: it is the caller's responsibility to call this service in a
     * secure and safe way.  This service will not check if the security
     * challenge has actually been done against this user.
     * @param username the username of the registered user
     * @param newPassword the new to be set for this user
     * @param tracking TrackingType
     * @return the service response object indicating the outcome of this call
     * @throws IllegalArgumentException if the username or the new password are
     * null or blank.
     */
    ServiceResponse resetPassword(
    final String username, final String newPassword, final TrackingType tracking
    ) throws IllegalArgumentException;

    /**.
     *
     * @param username username
     * @param tracking TrackingType
     * @return ServiceResponse ServiceResponse
     */
    ServiceResponse<Boolean> validateUser(
    final String username, final TrackingType tracking
    );
}
