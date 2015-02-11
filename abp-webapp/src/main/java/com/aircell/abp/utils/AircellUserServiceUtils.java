/*
 * This source file is Proprietary and Confidential.
 * 
 * Redistribution and use in source and binary forms, without consent 
 * are strictly forbidden. 
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.AircellUser;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.bss.ws.TrackingType;

/**.
 * .
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class AircellUserServiceUtils {
    /**.
     * .
     * @param user
     * @param username
     * @param errors
     * @return
     */
    public static ServiceResponse retrieveForgottenPassword(
    AircellUser user, String username, Errors errors, String locale,
    final TrackingType tracking, final String emailAddress
    ) {
        ServiceResponse response;
        try {
            response = user.retrieveForgottenPassword(
            username, locale, tracking, emailAddress
            );
            AircellServiceUtils.errorsFromErrorCodes(response, errors, "");
        } catch (IllegalArgumentException iae) {
            response = new ServiceResponse();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotPassword.username.empty");
        }
        return response;
    }

    /**.
     * .
     * @param user
     * @param passwordAnswer
     * @param cardAnswer
     * @param addressAnswer
     * @param errors
     * @return
     */
    public static ServiceResponse<Boolean> validateForgotPasswordAnswer(
    AircellUser user, String passwordAnswer, String cardAnswer,
    String addressAnswer, Errors errors, TrackingType tracking
    ) {
        ServiceResponse<Boolean> response;
        try {
            String cardQuestion = user.getSecurityQuestionCardNumber();
            String addressQuestion = user.getSecurityQuestionAddress();
            if (StringUtils.isNotBlank(cardQuestion) && StringUtils
            .isNotBlank(addressQuestion)) {
                response = user.validateForgottenPassword(
                passwordAnswer, cardAnswer, addressAnswer, tracking
                );
            } else {
                response =
                user.validateForgottenPassword(passwordAnswer, tracking);
            }
            AircellServiceUtils.errorsFromErrorCodes(response, errors, "");
        } catch (IllegalArgumentException iae) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotPassword.answer.empty");
        } catch (IllegalStateException ise) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(ise);
            errors.reject("errors.forgot.username.empty");
        }
        return response;
    }

    /**.
     * .
     * @param user
     * @param email
     * @param errors
     * @return
     */
    public static ServiceResponse retrieveForgottenUsername(
    AircellUser user, String email, Errors errors, String locale,
    TrackingType tracking
    ) {
        ServiceResponse response;
        try {
            response = user.retrieveForgottenUsername(email, locale, tracking);
            AircellServiceUtils.errorsFromErrorCodes(response, errors, "");
        } catch (IllegalArgumentException iae) {
            response = new ServiceResponse();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotPassword.email.empty");
        } catch (IllegalStateException iae) {
            response = new ServiceResponse();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotPassword.email.empty");
        }
        return response;
    }

    /**.
     * .
     * @param user
     * @param answer
     * @param errors
     * @return
     */
    public static ServiceResponse<Boolean> validateForgotUsernameAnswer(
    AircellUser user, String answer, Errors errors, TrackingType tracking
    ) {
        ServiceResponse<Boolean> response;
        try {
            response = user.validateForgottenUsername(answer, tracking);
            AircellServiceUtils.errorsFromErrorCodes(response, errors, "");
        } catch (IllegalArgumentException iae) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotUsername.answer.empty");
        } catch (IllegalStateException ise) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(ise);
            errors.reject("errors.forgot.username.empty");
        }
        return response;
    }

    /**.
     * .
     * @param user
     * @return
     */
    public static boolean hasTickingService(AircellUser user) {
        LoginDetails loginDetails = user.getLoginDetails();
        AirPassenger airPassenger =
        new AirPassenger(loginDetails, user.getIpAddress());
        UserFlightSession flightSession = airPassenger.getSessionInfoService()
        .getSession(loginDetails.getUsername(), user.getIpAddress());
        boolean hasTickingService = flightSession.isActivated();
        return hasTickingService;
    }

    public static ServiceResponse<Boolean> validateUsername(
    AircellUser user, String username, Errors errors, TrackingType tracking
    ) {
        ServiceResponse<Boolean> response;
        try {
            response = user.validateUsername(username, tracking);
            //AircellServiceUtils.errorsFromErrorCodes(response, errors, "");
        } catch (IllegalArgumentException iae) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(iae);
            errors.reject("errors.forgotUsername.answer.empty");
        } catch (IllegalStateException ise) {
            response = new ServiceResponse<Boolean>();
            response.setSuccess(false);
            response.setErrorCause(ise);
            errors.reject("errors.forgot.username.empty");
        }
        return response;
    }
}
