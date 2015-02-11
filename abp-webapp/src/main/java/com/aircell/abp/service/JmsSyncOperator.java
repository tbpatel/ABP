/*
 * JmsSyncOperator.java 31 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.io.Serializable;

/**.
 * Implementation of a Synchronised JMS service that uses a temporary JMS queue
 * to exchange messages
 * @author jon.boydell
 */
public class JmsSyncOperator implements JmsSynchOperations {

    /**.
     * Default timeout for receive operations: -1 indicates a blocking receive
     * without timeout.
     */
    public static final int DEFAULT_RECEIVE_TIMEOUT = -1;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ConnectionFactory connectionFactory;
    private Destination destination;
    private long receiveTimeout;

    /**.
     * Default constructor. Initialises the
     * <code>receiveTimeout</code> property to
     * the <code>DEFAULT_RECEIVE_TIMEOUT</code>.
     */
    public JmsSyncOperator() {
        this.receiveTimeout = DEFAULT_RECEIVE_TIMEOUT;
    }

    /**.
     * Init method for this bean. Checks if all dependencies required have been
     * set.
     * @throws IllegalArgumentException if some of the dependencies required by
     * this bean have not been set.
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (destination == null) {
            throw new IllegalArgumentException(
            "A non-null Destination must "
            + "be provided for the destination property."
            );
        }
        if (connectionFactory == null) {
            throw new IllegalArgumentException(
            "A non-null ConnectionFactory must "
            + "be provided for the connectionFactory property."
            );
        }
    }

    /**. @see com.aircell.abp.service.JmsSynchOperations#
     * exchangeObjMsgOverTempQueue(Serializable) */
    public Serializable exchangeObjMsgOverTempQueue(
    final Serializable requestData
    ) {
        return exchangeObjMsgOverTempQueue(requestData, null, null);
    }

    /**.
     * @see com.aircell.abp.service.JmsSynchOperations#
     * exchangeObjMsgOverTempQueue(Serializable,
     *      String, String)
     */
    public Serializable exchangeObjMsgOverTempQueue(
    final Serializable requestData, final String msgPropertyKey,
    final String msgPropertyValue
    ) {
        Connection connection = null;
        Session session = null;
        Message response;
        Object responseData = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination temporaryQueue = null;

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            temporaryQueue = session.createTemporaryQueue();

            final ObjectMessage request =
            session.createObjectMessage(requestData);
            request.setJMSReplyTo(temporaryQueue);
            if (StringUtils.isNotBlank(msgPropertyKey)) {
                request.setStringProperty(msgPropertyKey, msgPropertyValue);
            }

            producer = session.createProducer(destination);
            consumer = session.createConsumer(temporaryQueue);
            producer.send(request);
            log.debug(
            "JMS data sent payload = key={}, value={}", msgPropertyKey,
            msgPropertyValue
            );

            connection.start();
            response = consumer.receive(getReceiveTimeout());
            connection.stop();

            if (response != null && (response instanceof ObjectMessage)) {
                responseData = ((ObjectMessage) response).getObject();
            }
            if (responseData != null && responseData instanceof ObjectMessage) {
                final String objectString = ObjectUtils.toString(
                ((ObjectMessage) responseData).getObjectProperty("Message")
                );
                log.debug("JMS data recieved payload = {}", objectString);
            }

            log.debug("Exchanged message over temporary queue successfully");
        } catch (final JMSException ex) {
            log.error("Trying to send and recieve JMS message.", ex);
            throw new ServiceException(
            "Could not send and receive JMS " + "message over temporary queue",
            ex
            );
        } finally {
            /*    try{
                   if(producer != null){
                       producer.close();
                   }
               } catch (final Exception ex){
                   log.error("Exception while closing the producer : " + ex);
               }*/
            try {
                if (consumer != null) {
                    consumer.close();
                }
            } catch (final Exception ex) {
                log.error("Exception while closing the consumer : " + ex);
            }
            try {
                if (temporaryQueue != null) {
                    ((TemporaryQueue) temporaryQueue).delete();
                }
            } catch (final Exception ex) {
                log.error(
                "Exception while trying to delete Temporary Queue : " + ex
                );
            }
            try {
                if (session != null) {
                    session.close();
                }
            } catch (final JMSException ex) {
                log.error("Trying to close JMS Session.", ex);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (final JMSException ex) {
                log.error("Trying to close JMS Connection.", ex);
            }


        }
        return (Serializable) responseData;
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    /**. @param destination */
    public void setDestination(final Destination destination) {
        this.destination = destination;
    }

    /**. @return  */
    Destination getDestination() {
        return this.destination;
    }

    /**. @param connectionFactory */
    public void setConnectionFactory(
    final ConnectionFactory connectionFactory
    ) {
        this.connectionFactory = connectionFactory;
    }

    /**. @return  */
    ConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    /**. @return the receiveTimeout */
    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    /**. @param receiveTimeout the receiveTimeout to set */
    public void setReceiveTimeout(final long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

}
