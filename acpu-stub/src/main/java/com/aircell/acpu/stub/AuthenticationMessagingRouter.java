package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import com.aircell.abs.acpu.common.FlightAbsCoverageCode;
import com.aircell.abs.acpu.common.GpsData;
import com.aircell.abs.acpu.common.SessionInfo;
import com.aircell.abs.acpu.common.SessionInfoRequest;
import com.aircell.abs.acpu.common.SessionStatusCodes;
import com.aircell.abs.acpu.common.SubscriptionStartRequestData;
import com.aircell.abs.acpu.common.SubscriptionStartResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.util.Date;

public class AuthenticationMessagingRouter implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JmsTemplate jmsTemplate;
    private AuthenticationService authenticationService;

    public void onMessage(final Message msg) throws JmsException {
        logger.info("AuthenticationMessagingRouter.onMessage:"
                   + " Received async JMS message '{}'", msg);
        if (msg instanceof ObjectMessage) {
            handleObjectMessage((ObjectMessage) msg);
        } else if (msg instanceof TextMessage) {
            handleTextMessage((TextMessage) msg);
        } else {
            logger.warn("AuthenticationMessagingRouter.onMessage:"
            + "Async message received is not of supported type. "
            + "Ignoring the message. '{}'", msg
            );
        }
    }

    public void handleObjectMessage(final ObjectMessage msg) {
        try {
            final Object payload = msg.getObject();
            final String messageProperty = msg.getStringProperty("Message");
            // TODO use some other way to determine which object it is
            // maybe use Class.isAssignableFrom
            // because as it stands SubscriptionStartResponseData
            // extends AuthResponseData
            // and thereofre the order of if statements is important.
            if (payload instanceof SubscriptionStartRequestData) {
                handleSubscriptionStartRequest((ObjectMessage) msg);
            } else if (payload instanceof AuthRequestData) {
                handleAuthenticationRequest((ObjectMessage) msg);
            } else if (messageProperty.equals("FlightInfoRequest")) {
                handleFlightInfo(msg);
            } else if (messageProperty.equals("SessionInfoRequest")) {
                handleSessionInfo(msg);
            } else {
                logger.error("AuthenticationMessagingRouter"
                     + ".handleObjectMessage:"
                + "Object message received doesn't have "
                + "supported payload type '{}'. Ignoring the message", payload
                );
            }
        } catch (JMSException e) {
            JmsUtils.convertJmsAccessException(e);
        }
    }

    private void handleFlightInfo(ObjectMessage msg) {
        final com.aircell.abs.acpu.common.FlightInfo flightInfo =
        new com.aircell.abs.acpu.common.FlightInfo("AW123", "AA", "747-400");
        flightInfo.setArrivalAirport("KJFK");
        flightInfo.setDepartureAirport("KIAD");
        flightInfo.setExpectedArrivalTime(new Date());
        flightInfo.setFlightAbsCoverage(
        FlightAbsCoverageCode.FLIGHT_ABS_COVERAGE_COMPLETE
        );
        flightInfo.setFlightNo("AA123");
        final GpsData gps = new GpsData(
        10.0f, 20.0f, 30.0f, 40.0f, 50.0f, new Date(), "Local Time", 9,
        GpsData.GpsHealth.WORKING, GpsData.GpsState.ENABLED
        );
        flightInfo.setGpsFeed(gps);
        Destination replyTo = null;
        try {
            replyTo = msg.getJMSReplyTo();
            getJmsTemplate().convertAndSend(replyTo, flightInfo);
        } catch (JMSException e) {
            logger.error("AuthenticationMessagingRouter.handleFlightInfo: "
            + "Unexpected JMSException while replying to Flight Info", msg
            );
        }
    }

    public void handleSessionInfo(ObjectMessage msg) {
        SessionInfoRequest req = null;
        try {
            req = (SessionInfoRequest) msg.getObject();
        } catch (JMSException e) {
            logger.error("AuthenticationMessagingRouter.handleSessionInfo: "
            + "Unexpected JMSException while trying to"
            + " get message from Session Info call", msg);
        }
        String ip = "";
        String username = "";
        if (req != null) {
            ip = req.getIpAddress();
            username = req.getUserName();
        }
        SessionInfo returnObj = new SessionInfo(
        ip, username, SessionStatusCodes.USER_SUBSCRIBED
        );

        Destination replyTo = null;
        try {
            replyTo = msg.getJMSReplyTo();
            getJmsTemplate().convertAndSend(replyTo, returnObj);
        } catch (JMSException e) {
            logger.error("AuthenticationMessagingRouter.handleSessionInfo: "
            + "Unexpected JMSException while replying to Session Info", msg
            );
        }

    }

    public void handleTextMessage(final TextMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException("text message cannot be null");
        }
        logger.debug("AuthenticationMessagingRouter.handleTextMessage:"
                  + " Text message received '{}'", msg);
    }

    public void handleAuthenticationRequest(final ObjectMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException(
            "Object message argument cannot be null."
            );
        }

        try {
            final AuthRequestData data = (AuthRequestData) msg.getObject();
            final AuthResponseData responseData =
            getAuthenticationService().authenticateUser(data);
            final Destination replyTo = msg.getJMSReplyTo();

            if (replyTo == null) {
                throw new RuntimeException(
                "Unable to send authentication reply message back. "
                + "The jmsReplyTo attribute wasn't "
                + "set on the incoming request message ok!");
            }
            getJmsTemplate().convertAndSend(replyTo, responseData);
            logger.info("AuthenticationMessagingRouter.handleTextMessage: "
            + "Object '{}' sent ok to dest '{}' ",
            new Object[]{responseData, replyTo }
            );
        } catch (JMSException e) {
            JmsUtils.convertJmsAccessException(e);
        }
    }

    public void handleSubscriptionStartRequest(final ObjectMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException(
            "Object message argument cannot be null."
            );
        }

        try {
            final SubscriptionStartRequestData data =
            (SubscriptionStartRequestData) msg.getObject();
            final SubscriptionStartResponseData responseData =
            new SubscriptionStartResponseData(
            data.getIpAddress(), data.getUsername(), true
            );
            final Destination replyTo = msg.getJMSReplyTo();

            if (replyTo == null) {
                throw new RuntimeException(
                "Unable to send subscriptionStart reply message back. "
                + "The jmsReplyTo attribute wasn't "
                + "set on the incoming request message ok!");
            }
            getJmsTemplate().convertAndSend(replyTo, responseData);
            logger.info("AuthenticationMessagingRouter"
                      + ".handleSubscriptionStartRequest: "
            + "Object '{}' sent ok to dest '{}' ",
            new Object[]{responseData, replyTo }
            );
        } catch (JMSException e) {
            JmsUtils.convertJmsAccessException(e);
        }
    }

    // *** Spring injected dependencies;
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthenticationService(
    final AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
