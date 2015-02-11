package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import com.aircell.abs.acpu.common.FlightAbsCoverageCode;
import com.aircell.abs.acpu.common.GpsData;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

import java.util.Date;


public class FlightInfoTest extends TestCase {

    public static final float LAT = 10.0f;
    public static final float LONG = 20.0f;
    public static final float ALT = 30.0f;
    public static final float HVEL = 40.0f;
    public static final float VVEL = 50.0f;
    public static final String LOCAL_TIME = "Local Time";
    public static final Date UTC = new Date();
    public static final String MESSAGE_KEY = "Message";
    public static final String MESSAGE_VALUE = "Value";

    public void testHappyGetFlightInfo() {

        final FlightInfoJmsImpl t = new FlightInfoJmsImpl();
        final Mockery context = new Mockery();
        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);
        final com.aircell.abs.acpu.common.FlightInfo mockReturn =
        new com.aircell.abs.acpu.common.FlightInfo("", "", "");
        mockReturn.setArrivalAirport("KJFK");
        mockReturn.setDepartureAirport("KIAD");
        mockReturn.setExpectedArrivalTime(new Date());
        mockReturn.setFlightAbsCoverage(
        FlightAbsCoverageCode.FLIGHT_ABS_COVERAGE_COMPLETE
        );
        mockReturn.setFlightNo("AA123");

        final GpsData gps = new GpsData(
        LAT, LONG, ALT, HVEL, VVEL, UTC, LOCAL_TIME, 10,
        GpsData.GpsHealth.WORKING, GpsData.GpsState.ENABLED
        );
        mockReturn.setGpsFeed(gps);

        t.setFlightInfoJmsPropertyKey(MESSAGE_KEY);
        t.setFlightInfoJmsPropertyValue(MESSAGE_VALUE);
        t.setJmsSyncService(mockSyncService);

        t.checkDependenciesSet();

        context.checking(
        new Expectations() {{
            try {
                one(mockSyncService)
                .exchangeObjMsgOverTempQueue(null, MESSAGE_KEY, MESSAGE_VALUE);
                will(returnValue(mockReturn));
            } catch (Exception e) {
            }
        }}
        );

        //invoking method under test
        final Flight response = t.getCurrentStatus();

        assertNotNull(response);
        assertNotNull(response.getFlightStatus());
        assertEquals(
        "AA123", response.getFlightInformation().getFlightNumber()
        );
        assertEquals(
        LAT, response.getFlightInformation().getFlightStatus().getLatitude()
        );
    }

    public void testreturnNullGetFlightInfo() {

        final FlightInfoJmsImpl t = new FlightInfoJmsImpl();
        final Mockery context = new Mockery();

        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);

        //set dependencies
        t.setFlightInfoJmsPropertyKey(MESSAGE_KEY);
        t.setFlightInfoJmsPropertyValue(MESSAGE_VALUE);
        t.setJmsSyncService(mockSyncService);

        t.checkDependenciesSet();

        context.checking(
        new Expectations() {{
            try {
                one(mockSyncService)
                .exchangeObjMsgOverTempQueue(null, MESSAGE_KEY, MESSAGE_VALUE);
                will(returnValue(null));
            } catch (Exception e) {
            }
        }}
        );

        Flight response = null;
        //invoking method under test
        try {
            response = t.getCurrentStatus();
        } catch (ServiceException e) {
            assertNotNull(e);
        }
        assertNull(response);

    }

    public void testExceptionThrownFlightInfo() throws Exception {

        final FlightInfoJmsImpl t = new FlightInfoJmsImpl();
        final Mockery context = new Mockery();
        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);

        //set dependencies
        t.setFlightInfoJmsPropertyKey(MESSAGE_KEY);
        t.setFlightInfoJmsPropertyValue(MESSAGE_VALUE);
        t.setJmsSyncService(mockSyncService);

        t.checkDependenciesSet();

        context.checking(
        new Expectations() {
            {
                one(mockSyncService)
                .exchangeObjMsgOverTempQueue(null, MESSAGE_KEY, MESSAGE_VALUE);
                will(throwException(new ServiceException("An exception")));
            }    }
        );

        Flight response = null;
        //invoking method under test
        try {
            response = t.getCurrentStatus();
        } catch (ServiceException e) {
            assertNotNull(e);
        }
        assertNull(response);

    }

    public void testCheckDependenciesSet() {

        final FlightInfoJmsImpl t = new FlightInfoJmsImpl();
        final Mockery context = new Mockery();
        final JmsSynchOperations mockSyncService =
        context.mock(JmsSynchOperations.class);

        try {
            t.checkDependenciesSet();
            fail(
            "Illegal argument exception should have "
            + "been thrown for null JmsSynchService"
            );
        } catch (final IllegalArgumentException ex) {
        }

        t.setJmsSyncService(mockSyncService);

        try {
            t.checkDependenciesSet();
            fail(
            "Illegal argument exception should have been thrown "
            + "for null flightInfoJmsPropertyKey"
            );
        } catch (final IllegalArgumentException ex) {
        }

        t.setFlightInfoJmsPropertyKey(MESSAGE_KEY);

        try {
            t.checkDependenciesSet();
            fail(
            "Illegal argument exception should have been thrown for "
            + "null flightInfoJmsPropertyValue"
            );
        } catch (final IllegalArgumentException ex) {
        }

        t.setFlightInfoJmsPropertyValue(MESSAGE_VALUE);

        try {
            t.checkDependenciesSet();
        } catch (final IllegalArgumentException ex) {
            fail("Illegal argument exception should not have been thrown");
        }

    }

}
