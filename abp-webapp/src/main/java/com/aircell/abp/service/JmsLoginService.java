/*
 * JmsLoginService.java 31 Jul 2007
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
import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**.
 * This class uses a synchronous JMS request with a temporary queue to perform
 * login requests.
 */
public class JmsLoginService implements LoginService {

    private static final Logger logger =
    LoggerFactory.getLogger(JmsLoginService.class);

    private JmsSynchOperations jmsSyncService;
    private String serviceMessageKey = "Message";
    private String serviceMessageValue = "AuthenticationRequest";

    /**.
     * Tests whether the properties this class depends on are set correctly
     * @throws IllegalArgumentException if the
     * properties this class depends on are
     * null
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (jmsSyncService == null) {
            throw new IllegalArgumentException(
            "A non-null jmsService property must" + "be provided"
            );
        }

        if (StringUtils.isEmpty(serviceMessageKey)) {
            throw new IllegalArgumentException(
            "A non-null login service message key must be provided"
            );
        }

        if (StringUtils.isEmpty(serviceMessageValue)) {
            throw new IllegalArgumentException(
            "A non-null login service message value must be provided"
            );
        }
    }

    /**.
     * Perform the login operation for the provided user credentials.
     * @param loginDetails The user to log in
     * @return a ServiceResponse depending on login success / failure
     */
    public ServiceResponse login(final LoginDetails loginDetails) {
        logger.debug("JmsLoginService.login: entered");
        final AuthRequestData requestData = new AuthRequestData("", "", "");
        requestData.setUsername(loginDetails.getUsername());
        requestData.setPassword(loginDetails.getPassword());
        requestData.setIpAddress(loginDetails.getIpAddress());
        if (StringUtils.isNotBlank(loginDetails.getDomain())) {
            requestData.setDomain(loginDetails.getDomain());
        }
        logger.debug(
        "JmsLoginService.login: delegating to jmsLogin : username = "
        + loginDetails.getUsername() + " - ipaddress = " + loginDetails
        .getIpAddress()
        );
        final AuthResponseData authenticationResponse = jmsLogin(requestData);
        final ServiceResponse retval = new ServiceResponse(false);
        if (authenticationResponse.getStatus()) {
            retval.setSuccess(true);            
        } else {
            retval.setSuccess(false);
            retval.setErrorCode(authenticationResponse.getErrorCode());
            retval.setErrorText(authenticationResponse.getFailureReason());
        }
        logger.debug("JmsLoginService.login: exiting");
        return retval;
    }

    /**.
     * Performs the JMS based login
     * @param requestData The wrapper for the user login data
     * @return Response from the JMS login service
     */
    public AuthResponseData jmsLogin(final AuthRequestData requestData) {
        logger.debug("JmsLoginService.jmsLogin: entered");
        AuthResponseData responseData = null;
        logger.debug("JmsLoginService.jmsLogin: sending jms message");
        final Serializable responseObj =
        getJmsSyncService().exchangeObjMsgOverTempQueue(
        requestData, serviceMessageKey, serviceMessageValue
        );

        if (responseObj != null && (responseObj instanceof AuthResponseData)) {
            responseData = (AuthResponseData) responseObj;
            logger.debug(
            "JmsLoginService.jmsLogin: login response received successfully"
            );
        } else {
            logger.error(
            "JmsLoginService.jmsLogin: Unexpected response message received "
            + "for AuthenticationRequest JMS request:", responseObj
            );
            responseData = new AuthResponseData("", "", false);
            responseData.setUsername(requestData.getUsername());
            responseData.setIpAddress(requestData.getIpAddress());
            responseData.setStatus(false);
        }
        logger.debug("JmsLoginService.jmsLogin: exiting");
        return responseData;
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    /**. Getter for the JMS message exchange service */
    JmsSynchOperations getJmsSyncService() {
        return jmsSyncService;
    }

    /**.
     * Setter for the JMS message exchange service
     * @param jmsService
     */
    public void setJmsSyncService(final JmsSynchOperations jmsService) {
        this.jmsSyncService = jmsService;
    }

    /**.
     * Key used to identify the JMS auth message
     * @param serviceMessageKey
     */
    public void setServiceMessageKey(final String serviceMessageKey) {
        this.serviceMessageKey = serviceMessageKey;
    }

    /**.
     * Value (for the Key) used to identify the JMS auth message
     * @param serviceMessageValue
     */
    public void setServiceMessageValue(final String serviceMessageValue) {
        this.serviceMessageValue = serviceMessageValue;
    }
}
