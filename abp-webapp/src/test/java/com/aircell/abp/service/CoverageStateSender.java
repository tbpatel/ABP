package com.aircell.abp.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**.
 * Integration test class for sending CoverageState messages into a specified
 * JMS Queue for a listener to pick up
 * @author jon.boydell
 */
public class CoverageStateSender {

    private JmsTemplate jmsTemplate;
    private Destination destination;

    private static final String MESSAGE_KEY = "Message";
    private static final String ENTER_COVER = "EnterCoverageArea";
    private static final String EXIT_COVER = "ExitingCoverageArea";

    /**. Sends a dummy Entering Coverage message to a JMS queue */
    public void sendEnteringCoverageMessage() {
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.send(
        new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                final Message m = session.createTextMessage();
                m.setStringProperty(MESSAGE_KEY, ENTER_COVER);
                return m;
            }
        }
        );
    }

    /**. Sends a dummy Exiting Coverage message to a JMS queue */
    public void sendExitingCoverageMessage() {
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.send(
        new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                final Message m = session.createTextMessage();
                m.setStringProperty(MESSAGE_KEY, EXIT_COVER);
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
