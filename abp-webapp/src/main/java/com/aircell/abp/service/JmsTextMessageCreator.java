/*
 * JmsTextMessageCreator.java 16 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**.
 * Spring JMS message creator implementation. Capable of creating a JMS
 * <code>TextMessage</code> with specific String pay load. Optionally a single
 * String property can be set on this TextMessage.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public class JmsTextMessageCreator implements MessageCreator {

    private final String propertyKey;
    private final String propertyValue;
    private final String payload;

    /**.
     * Creates this <code>TextMessage</code> creator.
     * @param payload the pay load for the message to be created
     * @param propertyKey String property key to be set on the message
     * @param propertyValue String property value to be set on the message
     */
    public JmsTextMessageCreator(
    final String payload, final String propertyKey, final String propertyValue
    ) {
        this.payload = payload;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    /**.
     * Creates this <code>TextMessage</code> creator.
     * No String properties will be set on the message.
     *
     * @param payload the payload for this message
     */
    public JmsTextMessageCreator(
    final String payload
    ) {
        this(payload, null, null);
    }

    /**.
     * Creates a <code>TextMessage</code> with the String pay load specified.
     * If the <code>propertyKey</code> is not blank it is set on this message
     * as a string property.
     * @param session JMS session to be used to create the message
     * @return the <code>TextMessage</code>
     * @throws JMSException if the underlying JMS throws the exception
     * @throws IllegalArgumentException if the session object is null
     */
    public Message createMessage(final Session session)
    throws JMSException, IllegalArgumentException {
        if (session == null) {
            throw new IllegalArgumentException();
        }
        final TextMessage msg = session.createTextMessage();
        if (StringUtils.isNotBlank(getPropertyKey())) {
            msg.setStringProperty(getPropertyKey(), getPropertyValue());
        }
        msg.setText(getPayload());
        return msg;
    }

    /**.
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**.
     * @return the propertyKey
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**.
     * @return the propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**.
     * Uses <code>org.apache.commons.lang.builder.ToStringBuilder</code>
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
