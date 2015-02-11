package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.SystemStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class ABSStatusMessagingRouter implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JmsTemplate jmsTemplate;
    private ABSStatusService absStatusService;

    public void onMessage(final Message message) {
        logger.info("ABSStatusMessagingRouter.onMessage: Received"
                + " async JMS message '{}'", message);

        if (message instanceof ObjectMessage) {
            handleSystemStatusRequest((ObjectMessage) message);
        } else {
            logger.warn(
            "Async message received is not of supported type. "
            + "Ignoring the message. '{}'", message
            );
        }
    }

    public void handleSystemStatusRequest(final ObjectMessage message) {
        if (message == null) {
            throw new IllegalArgumentException(
            "Object" + " message argument cannot be null."
            );
        }

        try {
            if ("SystemStatusRequest"
            .equals(message.getStringProperty("Message"))) {
                final Destination replyTo = message.getJMSReplyTo();
                final SystemStatus responseData =
                getAbsStatusService().getSystemStatus();
                if (replyTo == null) {
                    throw new RuntimeException(
                    "Unable to send ABS System Status reply message back. "
                    + "The jmsReplyTo attribute wasn't set on "
                    + "the incoming request message ok!"
                    );
                }
                getJmsTemplate().convertAndSend(replyTo, responseData);
                logger.info(
                "Object '{}' sent ok to dest '{}' ",
                new Object[]{responseData, replyTo }
                );
            } else {
            }
        } catch (JMSException e) {
            JmsUtils.convertJmsAccessException(e);
        }
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public ABSStatusService getAbsStatusService() {
        return absStatusService;
    }

    public void setAbsStatusService(ABSStatusService absStatusService) {
        this.absStatusService = absStatusService;
    }

}
