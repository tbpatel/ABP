package com.aircell.acpu.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class JmsMessageSender {

    private static final Logger logger =
    LoggerFactory.getLogger(JmsMessageSender.class);

    private JmsTemplate jmsTemplate;

    public void sendMessage(
    final String messagePropertyValue, final String destinationName
    ) {
        logger.info("JmsMessageSender.sendMessage: "
        + "Sending message {} on destination {} ", messagePropertyValue,
        destinationName
        );
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.send(
        destinationName, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message m = session.createTextMessage();
                m.setStringProperty("Message", messagePropertyValue);
                return m;
            }
        }
        );
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
