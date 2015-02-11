/*
 * JmsSyncExchanger.java 15 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import java.io.Serializable;

/**.
 * Defines synchronous JMS operations.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public interface JmsSynchOperations {

    /**.
     * Exchanges JMS <code>ObjectMessage</code> over a JMS
     * <code>TemporaryQueue</code>.
     * @param requestData the payload object
     * @return the payload object of the response message
     * @throws ServiceException service exception
     *
     */
    Serializable exchangeObjMsgOverTempQueue(final Serializable requestData)
    throws ServiceException;


    /**.
     * Exchanges JMS <code>ObjectMessage</code> over a JMS
     * <code>TemporaryQueue</code>. Additionally, sets the JMS String property
     * on the outgoing message.
     * @param requestData the payload object
     * @param msgPropKey JMS String property key to set
     * @param msgPropValue JMS String property value to set
     * @return the payload object of the response message
     * @throws ServiceException
     */
    Serializable exchangeObjMsgOverTempQueue(
    final Serializable requestData, final String msgPropKey,
    final String msgPropValue
    ) throws ServiceException;
}
