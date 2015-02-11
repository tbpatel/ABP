package com.aircell.acpu.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AlternateJmsRunner implements Runnable {

    private static final Logger logger =
    LoggerFactory.getLogger(AlternateJmsRunner.class);
    private JmsMessageSender jmsMessageSender;

    private String message;
    private String alternateMessage;
    private String destinationName;

    public AlternateJmsRunner(
    String message, String alternateMessage, String destinationName
    ) {
        this.message = message;
        this.alternateMessage = alternateMessage;
        this.destinationName = destinationName;

    }


    private boolean sendMessage = true;

    public void run() {
        while (true) {
            if (sendMessage) {
                jmsMessageSender.sendMessage(message, destinationName);
                sendMessage = false;
            } else {
                jmsMessageSender.sendMessage(alternateMessage, destinationName);
                sendMessage = true;
            }
            try {
                logger.info("Sleeping for 15 seconds");
                Thread.sleep(15000);
            } catch (final InterruptedException ex) {
                logger
                .warn("Exception whilst waiting for next message send", ex);
            }
        }
    }

    public void setJmsMessageSender(JmsMessageSender jmsMessageSender) {
        this.jmsMessageSender = jmsMessageSender;
    }

}