/*
 * JmsSubscriptionService.java 31 Jul 2007
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
import com.aircell.abs.acpu.common.SubscriptionStartRequestData;
import com.aircell.abs.acpu.common.SubscriptionStartResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import java.io.Serializable;

/**.
 * Service implementation for working with the ATGSession as implemented over a
 * JMS queue
 * @author jon.boydell
 */
public class JmsATGSessionService implements ATGSessionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private JmsSynchOperations jmsSyncService;
    private JmsTemplate jmsTemplate;
    private String startATGSessionMsgPropertyKey = "Message";
    private String startATGSessionMsgPropertyValue = "SubscriptionRequest";
    private String endATGSessionQueueName;
    private String endATGSessionMsgPropertyKey;
    private String endATGSessionMsgPropertyValue;

    /**.
     * Checks that all the dependencies are set for the class to work
     * @throws IllegalArgumentException
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (jmsSyncService == null) {
            throw new IllegalArgumentException(
            "jmsSyncService property must be set"
            );
        }
        if (jmsTemplate == null) {
            throw new IllegalArgumentException(
            "jmsTemplate property must be set"
            );
        }
        if (StringUtils.isBlank(endATGSessionQueueName)) {
            throw new IllegalArgumentException(
            "endATGSessionQueueName must not be blank"
            );
        }
        if (StringUtils.isBlank(endATGSessionMsgPropertyKey)) {
            throw new IllegalArgumentException(
            "endATGSessionMsgPropertyKey must not be blank"
            );
        }
        if (StringUtils.isBlank(endATGSessionMsgPropertyValue)) {
            throw new IllegalArgumentException(
            "endATGSessionMsgPropertyValue must not be blank"
            );
        }
        if (StringUtils.isBlank(startATGSessionMsgPropertyKey)) {
            throw new IllegalArgumentException(
            "startATGSessionMsgPropertyKey must not be blank"
            );
        }
        if (StringUtils.isBlank(startATGSessionMsgPropertyValue)) {
            throw new IllegalArgumentException(
            "startATGSessionMsgPropertyValue must not be blank"
            );
        }

    }

    /**.
     * Should probably be renamed to startATGSession
     * @param loginDetails User's login details
     * @param ipAddress User's client IP address
     * @throws IllegalArgumentException if the user name,
     * password or IP address is
     * blank
     */
    public ServiceResponse sendStartATGSessionMsg(
    final LoginDetails loginDetails, final String ipAddress
    ) {
        logger.debug("JmsATGSessionService.sendStartATGSessionMsg: entered");
        if (loginDetails == null
        || StringUtils.isBlank(loginDetails.getUsername())
        || StringUtils.isBlank(loginDetails.getPassword()) || StringUtils
        .isBlank(ipAddress)) {
            throw new IllegalArgumentException(
            "username, password " + "and ipAddress are all required"
            );
        }

        final SubscriptionStartRequestData requestData =
        new SubscriptionStartRequestData(
        ipAddress, loginDetails.getUsername(), loginDetails.getPassword()
        );

        if (!StringUtils.isBlank(loginDetails.getDomain())) {
            requestData.setDomain(loginDetails.getDomain());
        }

        final ServiceResponse retval = new ServiceResponse();
        retval.setSuccess(false);

        logger.debug(
        "JmsATGSessionService.sendStartATGSessionMsg: Sending Start ATG "
        + "Sesson Message username={} ipaddress={} ",
        loginDetails.getUsername(), ipAddress
        );

        final Serializable responseObj =
        getJmsSyncService().exchangeObjMsgOverTempQueue(
        requestData, startATGSessionMsgPropertyKey,
        startATGSessionMsgPropertyValue
        );

        if ((responseObj != null)
        && (responseObj instanceof SubscriptionStartResponseData)) {
            final SubscriptionStartResponseData s =
            (SubscriptionStartResponseData) responseObj;
            if (s.getStatus()) {
                retval.setSuccess(true);
            } else {
                retval.setSuccess(false);
                retval.setErrorCode(s.getErrorCode());
                retval.setErrorText(s.getFailureReason());
            }
        } else {
            logger.error(
            "JmsATGSessionService.sendStartATGSessionMsg: "
            + "Unexpected response object received from "
            + "the 'SubscriptionStart' JMS call to ACPU: '{}'", responseObj
            );
            retval.setSuccess(false);
        }
        logger.debug("JmsATGSessionService.sendStartATGSessionMsg: exiting");
        return retval;
    }

    /**.
     * Should probably be renamed to endATGSession
     * Always assumes that the session is successfully ended
     * @param ipAddress IP Address of the browser/application that the session
     * belongs to
     */
    public ServiceResponse sendEndATGSessionMsg(final String ipAddress) {
        logger.debug("JmsATGSessionService.sendEndATGSessionMsg: entered");
        if (StringUtils.isBlank(ipAddress)) {
            throw new IllegalArgumentException("ipAddress is required");
        }

        final MessageCreator creator = new JmsTextMessageCreator(
        ipAddress, getEndATGSessionMsgPropertyKey(),
        getEndATGSessionMsgPropertyValue()
        );
        logger.debug(
        "JmsATGSessionService.sendEndATGSessionMsg: "
        + "sending message : ipAddress ="
        + ipAddress
        );
        getJmsTemplate().send(getEndATGSessionQueueName(), creator);
        return new ServiceResponse(true);
    }

    // *****************************************
    // *** Spring Injected properties
    // *****************************************
    /**. @return the jmsSyncService */
    public JmsSynchOperations getJmsSyncService() {
        return jmsSyncService;
    }

    /**. @param jmsSyncService the jmsSyncService to set */
    public void setJmsSyncService(final JmsSynchOperations jmsSyncService) {
        this.jmsSyncService = jmsSyncService;
    }

    /**. @return the jmsTemplate */
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    /**. @param jmsTemplate the jmsTemplate to set */
    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    /**. @return the endUserSessionQueueName */
    public String getEndATGSessionQueueName() {
        return endATGSessionQueueName;
    }

    /**. @param endUserSessionQueueName the endUserSessionQueueName to set */
    public void setEndATGSessionQueueName(
    final String endUserSessionQueueName
    ) {
        this.endATGSessionQueueName = endUserSessionQueueName;
    }

    /**. @return the endUserSessionMsgPropertyKey */
    String getEndATGSessionMsgPropertyKey() {
        return endATGSessionMsgPropertyKey;
    }

    /**. @param endUserSessionMsgPropertyKey the
     * endUserSessionMsgPropertyKey to set */
    public void setEndATGSessionMsgPropertyKey(
    final String endUserSessionMsgPropertyKey
    ) {
        this.endATGSessionMsgPropertyKey = endUserSessionMsgPropertyKey;
    }

    /**. @return the endUserSessionMsgPropertyValue */
    String getEndATGSessionMsgPropertyValue() {
        return endATGSessionMsgPropertyValue;
    }

    /**.
     * @param endUserSessionMsgPropertyValue the
     * endUserSessionMsgPropertyValue to
     * set
     */
    public void setEndATGSessionMsgPropertyValue(
    final String endUserSessionMsgPropertyValue
    ) {
        this.endATGSessionMsgPropertyValue = endUserSessionMsgPropertyValue;
    }

    public void setStartATGSessionMsgPropertyValue(
    final String startATGSessionMsgPropertyValue
    ) {
        this.startATGSessionMsgPropertyValue = startATGSessionMsgPropertyValue;
    }

    public void setStartATGSessionMsgPropertyKey(
    final String startATGSessionMsgPropertyKey
    ) {
        this.startATGSessionMsgPropertyKey = startATGSessionMsgPropertyKey;
    }

}
