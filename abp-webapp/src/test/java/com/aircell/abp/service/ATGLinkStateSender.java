package com.aircell.abp.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**.
 * Integration test class for sending ATG Link State messages into a JMS Queue
 * for a listener to pick up
 * @author jon.boydell
 */
public class ATGLinkStateSender {

    private JmsTemplate jmsTemplate;
    private Destination destination;

    private static final String MESSAGE_KEY = "Message";
    private static final String ATG_UP = "ATGLinkUp";
    private static final String ATG_DOWN = "ATGLinkDown";

    /**. Sends a dummy ATG Link Up message to a JMS queue */
    public void sendATGLinkUpMessage() {
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.send(
        new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                final Message m = session.createTextMessage();
                m.setStringProperty(MESSAGE_KEY, ATG_UP);
                return m;
            }
        }
        );
    }

    /**. Sends a dummy ATG Link Down message to a JMS queue */
    public void sendATGLinkDownMessage() {
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.send(
        new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                final Message m = session.createTextMessage();
                m.setStringProperty(MESSAGE_KEY, ATG_DOWN);
                return m;
            }
        }
        );
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(final Destination destination) {
        this.destination = destination;
    }
}
