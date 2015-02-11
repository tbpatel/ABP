package com.aircell.abp.service;

import com.aircell.abp.model.FlightSystem;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;
import com.aircell.abs.acpu.common.SystemStatus;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

public class FlightSystemTest extends MockObjectTestCase {

    public static final String JMS_PROPERTY_KEY = "Message";
    public static final String JMS_PROPERTY_VALUE = "SystemStatusRequest";

    public void testCheckDepJmsSyncService() {
        final FlightSystemServiceImpl underTest = new FlightSystemServiceImpl();
        try {
            underTest.checkDependenciesSet();
            fail("Illegal Argument Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    public void testCheckDepPropertyKey() {
        final JmsSynchOperations mockJmsSyncService =
        mock(JmsSynchOperations.class);
        final FlightSystemServiceImpl underTest = new FlightSystemServiceImpl();
        underTest.setJmsSyncService(mockJmsSyncService);
        try {
            underTest.checkDependenciesSet();
            fail("Illegal Argument Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    public void testCheckDepPropertyValue() {
        final JmsSynchOperations mockJmsSyncService =
        mock(JmsSynchOperations.class);
        final FlightSystemServiceImpl underTest = new FlightSystemServiceImpl();
        underTest.setJmsSyncService(mockJmsSyncService);
        underTest.setFlightSystemJmsPropertyKey(JMS_PROPERTY_KEY);
        try {
            underTest.checkDependenciesSet();
            fail("Illegal Argument Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            assertTrue(true);
        }
    }


    /**.
     * Tests the exchange of data between the response
     * from the JMS service and the
     * response from the ABSSystemInfo...
     */
    public void testHappyPath() {

        final JmsSynchOperations mockJmsSyncService =
        mock(JmsSynchOperations.class);

        final AtgLinkStatusCodes atgLinkStatus = AtgLinkStatusCodes.ATG_LINK_UP;
        final AbsServiceStatusCodes absServiceStatus =
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE;
        final SystemStatus systemStatus =
        new SystemStatus(atgLinkStatus, absServiceStatus, 10000, 10); //, "test");

        checking(
        new Expectations() {
            {
                one(mockJmsSyncService).exchangeObjMsgOverTempQueue(
                null, JMS_PROPERTY_KEY, JMS_PROPERTY_VALUE
                );
                will(returnValue(systemStatus));
            }        }
        );

        final FlightSystemServiceImpl underTest = new FlightSystemServiceImpl();
        underTest.setFlightSystemJmsPropertyKey(JMS_PROPERTY_KEY);
        underTest.setFlightSystemJmsPropertyValue(JMS_PROPERTY_VALUE);
        underTest.setJmsSyncService(mockJmsSyncService);

        try {
            underTest.checkDependenciesSet();
            assertTrue(true);
        } catch (final IllegalArgumentException ex) {
            fail("Illegal Argument Exception incorrectly thrown");
        }

        final FlightSystem response = underTest.getCurrentStatus();

        assertNotNull(response);
        assertEquals(
        AtgLinkStatusCodes.ATG_LINK_UP, response.getAtgLinkStatus()
        );
        assertEquals(
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE,
        response.getAbsServiceStatus()
        );
    }

    /**. Tests whether null is returned during the JMS message exchange */
    public void testNullJmsReply() {
        final JmsSynchOperations mockJmsSyncService =
        mock(JmsSynchOperations.class);

        checking(
        new Expectations() {
            {
                one(mockJmsSyncService).exchangeObjMsgOverTempQueue(
                null, JMS_PROPERTY_KEY, JMS_PROPERTY_VALUE
                );
                will(returnValue(null));
            }        }
        );

        final FlightSystemServiceImpl underTest = new FlightSystemServiceImpl();
        underTest.setFlightSystemJmsPropertyKey(JMS_PROPERTY_KEY);
        underTest.setFlightSystemJmsPropertyValue(JMS_PROPERTY_VALUE);
        underTest.setJmsSyncService(mockJmsSyncService);

        try {
            underTest.getCurrentStatus();
            fail("ServiceException is never thrown");
        } catch (final ServiceException ex) {
            assertTrue(true);
        }
    }


}
