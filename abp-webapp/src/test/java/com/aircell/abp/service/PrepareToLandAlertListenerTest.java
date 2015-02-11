package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

public class PrepareToLandAlertListenerTest extends MockObjectTestCase {

    private static final String PROPERTY_KEY = "Message";

    private PrepareToLandAlertListener underTest;

    @Override
    protected void setUp() throws Exception {
        underTest = new PrepareToLandAlertListener();
    }

    /**.
     * Tests whether or not an exception isn't
     * thrown if the service dependencies
     * are correctly set
     */
    public void testCheckDependenciesSet() throws JMSException {

        final PrepareToLandAlertListener underTest =
        new PrepareToLandAlertListener();
        final Flight currentFlight = new Flight();
        underTest.setFlight(currentFlight);

        try {
            underTest.checkDependenciesSet();
        } catch (final IllegalArgumentException ex) {
            fail("Illegal argument exception thrown for checkDependenciesSet");
        }
    }

    /**.
     * Tests whether or not an exception is
     * thrown if the service dependencies are
     * not correctly set
     */
    public void testCheckDependenciesUnSet() throws JMSException {

        final PrepareToLandAlertListener underTest =
        new PrepareToLandAlertListener();
        try {
            underTest.checkDependenciesSet();
            fail(
            "Illegal argument exception not thrown "
            + "for failed checkDependenciesSet"
            );
        } catch (final IllegalArgumentException ex) {
        }
    }

    /**. Tests whether a link up message is handled correctly */
    public void testLinkUpMessage() throws JMSException {
        // Set ATG Link down
        final Flight currentFlight = new Flight();
        final TextMessage mockLinkUpMessage = mock(TextMessage.class);

        checking(
        new Expectations() {
            {
                one(mockLinkUpMessage).getStringProperty(PROPERTY_KEY);
                will(returnValue("PrepareToLand"));
            }        }
        );

        final PrepareToLandAlertListener underTest =
        new PrepareToLandAlertListener();
        underTest.setFlight(currentFlight);
        underTest.onMessage(mockLinkUpMessage);
        assertTrue(
        currentFlight.getPrepareToLandAlert().equals("PrepareToLand")
        );
    }

    /**.
     * Tests whether or not an exception is thrown
     * when a non TextMessage is passed
     * to onMessage
     */
    public void testWrongMessageType() {

        final ObjectMessage mockMessage = mock(ObjectMessage.class);

        try {
            underTest.onMessage(mockMessage);
            fail("Illegal argument exception not thrown for non-TextMessage");
        } catch (IllegalArgumentException e) {}
    }

}
