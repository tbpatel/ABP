package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**.
 * PrepareToLandAlertListener class is to listen a message posted on topic to
 * prepare to land. The alert message used to display in status tray to prepare
 * for landing.
 * @author gajender.vattem
 */
public class PrepareToLandAlertListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Flight flight;
    private String prepareToLandMessagePropertyKey;
    private String prepareToLandMessage;

    /**.
     * Creates a new instance of PrepareToLandAlertListener with default values:
     * Message property key = "Message"
     */
    public PrepareToLandAlertListener() {
        logger.info(
        "Initialising PrepareToLandAlertListener",
        PrepareToLandAlertListener.class
        );
        this.prepareToLandMessagePropertyKey = "Message";
        this.prepareToLandMessage = "PrepareToLand";
    }

    /**.
     * Checks whether the instance of this class is in a usable state
     * @throws IllegalArgumentException If the flight property set is null
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (flight == null) {
            throw new IllegalArgumentException(
            "Flight property must be set"
            );
        }
    }

    /**.
     * sets the prepare to land alert message
     * @param message ACPU message
     */
    public void onMessage(final Message message) {
        logger.debug("PrepareToLandAlertListener.onMessage: start");
        if (!(message instanceof TextMessage)) {
            logger.error(
            "PrepareToLandAlertListener.onMessage : ACPU Message is not a "
            + "TextMessge, can only consume TextMessage"
            );
            throw new IllegalArgumentException(
            "Can only consume " + TextMessage.class.toString()
            );
        }

        try {
            final String acpuMessage =
            message.getStringProperty(prepareToLandMessagePropertyKey);
            if (prepareToLandMessage.equalsIgnoreCase(acpuMessage)) {
                flight.setPrepareToLandAlert(prepareToLandMessage);
                logger.info(
                "PrepareToLandAlertListener.onMessage : received prepare "
                + "to land alert message"
                );
            } else {
                logger.error(
                "PrepareToLandAlertListener.onMessage : received ACPU message "
                + "but not prepare to land alert message : "
                + acpuMessage
                );
            }
        } catch (final JMSException ex) {
            logger.error(
            "PrepareToLandAlertListener.onMessage: JMS provider "
            + "failed to get property "
            + prepareToLandMessagePropertyKey + ".", ex
            );
        }
        logger.debug("PrepareToLandAlertListener.onMessage: end");
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    /**.
     * Set the flight object to be modified by this listener
     * @param flight The current flight
     */
    public void setFlight(final Flight flight) {
        this.flight = flight;
    }

    /**. Returns the current flight */
    public Flight getFlight() {
        return this.flight;
    }
}
