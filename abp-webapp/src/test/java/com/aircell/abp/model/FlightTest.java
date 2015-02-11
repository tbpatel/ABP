package com.aircell.abp.model;

import com.aircell.abp.service.FlightInfoService;
import com.aircell.abp.service.FlightSystemService;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Date;

public class FlightTest extends MockObjectTestCase {

    /**. private class variable to hold mocked FlightInfoService */
    private final FlightInfoService mockFlightInfoService =
    mock(FlightInfoService.class);
    /**. private class variable to hold mocked FlightSystemService */
    private final FlightSystemService mockFlightSystemService =
    mock(FlightSystemService.class);
    /**. private class variable to hold Flight object */
    private final Flight currentFlight = new Flight();
    /**. private class variable to hold Flight object */
    private final Flight updatedFlight = new Flight();
    /**. private class variable to hold FlightInformation object */
    private final FlightInformation currentFlightInfo = new FlightInformation();
    /**. private class variable to hold updated FlightInformation object */
    private final FlightInformation updatedFlightInfo = new FlightInformation();
    /**. private class variable to hold FlightSystem object */
    private final FlightSystem currentFlightSystem = new FlightSystem();
    /**. private class variable to hold updated FlightSystem object */
    private final FlightSystem updatedFlightSystem = new FlightSystem();
    /**. private class variable to hold date */
    private final Date date = new Date();

    public void testCheckDependenciesSet() {
        final Flight flight = new Flight();

        try {
            flight.checkDependenciesSet();
            fail("IllegalArgumentException not correctly thrown");
        } catch (final IllegalArgumentException ex) {
            ex.getMessage();
        }

        flight.setFlightInfoService(mockFlightInfoService);

        try {
            flight.checkDependenciesSet();
            fail("IllegalArgumentException not correctly thrown");
        } catch (final IllegalArgumentException ex) {
            ex.getMessage();
        }

        flight.setFlightSystemService(mockFlightSystemService);

        try {
            flight.checkDependenciesSet();
        } catch (final IllegalArgumentException ex) {
            fail("IllegalArgumentException incorrectly thrown");
        }
    }

    public void testFlightInfoService() {
        final Flight underTest = new Flight();
        underTest.setFlightInfoService(mockFlightInfoService);

        checking(
        new Expectations() {
            {
                one(mockFlightInfoService).getCurrentStatus();
                will(returnValue(currentFlight));
            }}
        );

        final FlightInformation retVal = underTest.getFlightInformation();
        assertNotNull(retVal);
        assertEquals("BA", retVal.getAirlineCode());
        assertEquals(10.f, retVal.getFlightStatus().getLatitude());
    }

    public void testFlightServiceAvailablityServiceDown() {
        final Flight underTest = new Flight();
        underTest.setFlightSystemService(mockFlightSystemService);

        currentFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_INITIALIZING);
        currentFlightSystem
        .setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_FAILURE);

        checking(
        new Expectations() {
            {
                allowing(mockFlightSystemService).getCurrentStatus();
                will(returnValue(currentFlightSystem));
            }}
        );

        final FlightSystem currentRet = underTest.getFlightSystem();
        assertNotNull(currentRet);
        assertFalse(underTest.isServiceAvailable());
    }

    public void testFlightServiceAvailablityLinkDown() {
        final Flight underTest = new Flight();
        underTest.setFlightSystemService(mockFlightSystemService);

        currentFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE);
        currentFlightSystem
        .setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_FAILURE);

        checking(
        new Expectations() {
            {
                allowing(mockFlightSystemService).getCurrentStatus();
                will(returnValue(currentFlightSystem));
            }}
        );

        final FlightSystem currentRet = underTest.getFlightSystem();
        assertNotNull(currentRet);
        assertFalse(underTest.isServiceAvailable());
    }

    public void testFlightServiceAvailablityABSDown() {
        final Flight underTest = new Flight();
        underTest.setFlightSystemService(mockFlightSystemService);

        currentFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_INITIALIZING);
        currentFlightSystem.setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_UP);

        checking(
        new Expectations() {
            {
                allowing(mockFlightSystemService).getCurrentStatus();
                will(returnValue(currentFlightSystem));
            }}
        );

        underTest.setRefresh(1);

        final FlightSystem currentRet = underTest.getFlightSystem();
        assertNotNull(currentRet);
        assertFalse(underTest.isServiceAvailable());
    }

    public void testFlightServiceAvailablityHappyPath() {
        final Flight underTest = new Flight();
        underTest.setFlightSystemService(mockFlightSystemService);

        currentFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE);
        currentFlightSystem.setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_UP);

        checking(
        new Expectations() {
            {
                allowing(mockFlightSystemService).getCurrentStatus();
                will(returnValue(currentFlightSystem));
            }}
        );

        final FlightSystem currentRet = underTest.getFlightSystem();
        assertNotNull(currentRet);
        assertTrue(underTest.isServiceAvailable());
    }

    public void testFlightAtgLinkChange() {
        final Flight underTest = new Flight();
        underTest.setFlightSystemService(mockFlightSystemService);

        currentFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_INITIALIZING);
        currentFlightSystem
        .setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_FAILURE);

        updatedFlightSystem
        .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE);
        updatedFlightSystem.setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_UP);

        checking(
        new Expectations() {
            {
                one(mockFlightSystemService).getCurrentStatus();
                will(returnValue(currentFlightSystem));
            }

            {
                one(mockFlightSystemService).getCurrentStatus();
                will(returnValue(updatedFlightSystem));
            }}
        );

        // only refresh if flight contents are
        // null OR atgLink/coverage change state happens
        underTest.setRefresh(1000000000);

        final FlightSystem currentRet = underTest.getFlightSystem();
        assertNotNull(currentRet);
        assertEquals(
        AbsServiceStatusCodes.ABS_SERVICE_INITIALIZING,
        currentRet.getAbsServiceStatus()
        );
        assertEquals(
        AtgLinkStatusCodes.ATG_LINK_FAILURE, currentRet.getAtgLinkStatus()
        );
        assertFalse(underTest.isServiceAvailable());

        underTest.setAtgLinkUp(true);

        final FlightSystem updatedRet = underTest.getFlightSystem();
        assertNotNull(updatedRet);
        assertEquals(
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE,
        updatedRet.getAbsServiceStatus()
        );
        assertEquals(
        AtgLinkStatusCodes.ATG_LINK_UP, updatedRet.getAtgLinkStatus()
        );
        assertTrue(underTest.isServiceAvailable());
    }


    public void setUp() {
        currentFlight.setFlightInformation(currentFlightInfo);
        currentFlightInfo.setAircraftTailNumber("BXY2828");
        currentFlightInfo.setAirlineCode("BA");
        currentFlightInfo.setAirlineName("BRITISH AIRWAYS");
        currentFlightInfo.setDepartureAirportCode("LON");
        currentFlightInfo.setDestinationAirportCode("LAX");
        currentFlightInfo.setExpectedArrival(date);
        currentFlightInfo.setFlightNumber("BA0023");
        final FlightStatus currentFlightStatus = new FlightStatus();
        currentFlightStatus.setAltitude(100.0f);
        currentFlightStatus.setHSpeed(200.0f);
        currentFlightStatus.setLatitude(10.f);
        currentFlightStatus.setLocalTime("LOCAL");
        currentFlightStatus.setLongitude(20.f);
        currentFlightStatus.setUtcTime(date);
        currentFlightStatus.setVSpeed(30.f);
        currentFlightInfo.setFlightStatus(currentFlightStatus);

        updatedFlight.setFlightInformation(updatedFlightInfo);
        updatedFlightInfo.setAircraftTailNumber("BXY2828");
        updatedFlightInfo.setAirlineCode("BA");
        updatedFlightInfo.setAirlineName("BRITISH AIRWAYS");
        updatedFlightInfo.setDepartureAirportCode("LON");
        updatedFlightInfo.setDestinationAirportCode("LAX");
        updatedFlightInfo.setExpectedArrival(date);
        updatedFlightInfo.setFlightNumber("BA0023");
        final FlightStatus updatedFlightStatus = new FlightStatus();
        updatedFlightStatus.setAltitude(120.0f);
        updatedFlightStatus.setHSpeed(220.0f);
        updatedFlightStatus.setLatitude(12.f);
        updatedFlightStatus.setLocalTime("LOCAL");
        updatedFlightStatus.setLongitude(22.f);
        updatedFlightStatus.setUtcTime(date);
        updatedFlightStatus.setVSpeed(32.f);
        updatedFlightInfo.setFlightStatus(updatedFlightStatus);

    }
    public void testCheckServiceAvailability(){
    	System.setProperty("catalina.base", "/target/test-classes/com/aircell/abp/model/");
    	 final Flight underTest = new Flight();
    	 underTest.setVideoConfigFilePath("summary.xml");
    	 underTest.setMediaPath("");
    	 underTest.setMediaFileType(".xml");
    	 underTest.checkVideoServiceAvailability();
    	
    }
}
