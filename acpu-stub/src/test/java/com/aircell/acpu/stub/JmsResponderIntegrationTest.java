package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.NamingException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JmsResponderIntegrationTest extends TestCase {
    private JmsTemplate jmsTemplate;
    private Destination destination;
    private QueueConnectionFactory connectionFactory;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static Test suite() {
        return new TestSuite(JmsResponderIntegrationTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void setUp() throws NamingException, JMSException {
        Properties properties = new Properties();
        properties.put(
        "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory"
        );
        properties.put("java.naming.provider.url", "127.0.0.1");
        properties.put(
        "java.naming.factory.url.pkgs", "org.jnp.interfaces:org.jboss.naming"
        );

        JndiTemplate jndiTemplate = new JndiTemplate(properties);

        JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
        jndiFactory.setJndiTemplate(jndiTemplate);
        jndiFactory.setJndiName("UIL2ConnectionFactory");
        jndiFactory.afterPropertiesSet();

        JndiObjectFactoryBean jndiDestination = new JndiObjectFactoryBean();
        jndiDestination.setJndiTemplate(jndiTemplate);
        jndiDestination.setJndiName("queue/A");
        jndiDestination.afterPropertiesSet();

        destination = (Destination) jndiDestination.getObject();

        connectionFactory = (QueueConnectionFactory) jndiFactory.getObject();
        jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.afterPropertiesSet();
    }

    public void testRawJmsResponse() throws JMSException {
        AuthRequestData requestData = new AuthRequestData("", "", "");
        Connection connection = null;
        Session session = null;
        ObjectMessage response = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination temporaryQueue = session.createTemporaryQueue();
            logger.debug("QNAME" + temporaryQueue.toString());

            ObjectMessage request = session.createObjectMessage();
            request.setObject(requestData);
            request.setJMSReplyTo(temporaryQueue);

            MessageProducer producer = session.createProducer(destination);
            MessageConsumer consumer = session.createConsumer(temporaryQueue);

            logger.debug("Sending message");
            producer.send(request);
            logger.debug("Message sent");

            logger.debug("Waiting for message");
            connection.start();
            response = (ObjectMessage) consumer.receive();
            logger.debug("Response received");
            connection.stop();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        assertNotNull(response);
        AuthResponseData data =
        (AuthResponseData) ((ObjectMessage) response).getObject();
        assertNotNull(data);
    }
}
