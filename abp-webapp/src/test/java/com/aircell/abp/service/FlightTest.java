package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.FlightStatus;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Date;

/**.
 * Unit tests for Flight integration with the Flight Services
 * @author jon.boydell
 */
public class FlightTest extends MockObjectTestCase {

    /**. Is the IAException thrown for no flight service */
    public void testFlightInfoServiceNotSet() {
        final Flight flight = new Flight();

        try {
            flight.checkDependenciesSet();
            fail("IllegalArgumentException incorrectly not thrown");
        } catch (final IllegalArgumentException ex) {
            assertTrue("IllegalArgumentException caught correctly", true);
        }
    }

    /**. Is the IAException thrown for no system service */
    public void testFlightSystemServiceNotSet() {
        final FlightInfoService flightInfoService =
        mock(FlightInfoService.class);
        final Flight flight = new Flight();
        flight.setFlightInfoService(flightInfoService);

        try {
            flight.checkDependenciesSet();
            fail("IllegalArgumentException incorrectly not thrown");
        } catch (final IllegalArgumentException ex) {
            assertTrue("IllegalArgumentException caught correctly", true);
        }
    }

    /**. Is the IAException incorrectly thrown? */
    public void testFlightDependenciesHappyPath() {
        final FlightInfoService flightInfoService =
        mock(FlightInfoService.class);
        final FlightSystemService flightSystemService =
        mock(FlightSystemService.class);
        final Flight flight = new Flight();
        flight.setFlightInfoService(flightInfoService);
        flight.setFlightSystemService(flightSystemService);

        try {
            flight.checkDependenciesSet();
        } catch (final IllegalArgumentException ex) {
            fail("IllegalArgumentException caught incorrectly");
        }
    }

    private static final String TAIL_NUMBER = "BA29X";
    private static final String AIRLINE_CODE = "BA";
    private static final String DEPART_CODE = "LHR";
    private static final String DEST_CODE = "LGW";
    private static final Date ARRIVAL_DATE = new Date();
    private static final String FLIGHT_NO = "BA232";
    private static final float ALTITUDE = 10000.0f;
    private static final float H_SPEED = 323.5f;
    private static final float LATITUDE = 0.0f;
    private static final Date UTC_TIME = new Date();
    private static final float LONGITUDE = 43.0f;
    private static final String LOCAL_TIME = "12:33";
    private static final float V_SPEED = 90.4f;

    /**. Test the returned flight status */
    public void testFlightStatus() {
        FlightStatus tempFlightStatus = new FlightStatus();
        tempFlightStatus.setAltitude(ALTITUDE);
        tempFlightStatus.setHSpeed(H_SPEED);
        tempFlightStatus.setLatitude(LATITUDE);
        tempFlightStatus.setLocalTime(LOCAL_TIME);
        tempFlightStatus.setLongitude(LONGITUDE);
        tempFlightStatus.setUtcTime(UTC_TIME);
        tempFlightStatus.setVSpeed(V_SPEED);

        final Flight tempFlight = new Flight();
        final FlightInformation flightInfo = new FlightInformation();
        tempFlight.setFlightInformation(flightInfo);
        flightInfo.setAircraftTailNumber(TAIL_NUMBER);
        flightInfo.setAirlineCode(AIRLINE_CODE);
        flightInfo.setDepartureAirportCode(DEPART_CODE);
        flightInfo.setDestinationAirportCode(DEST_CODE);
        flightInfo.setExpectedArrival(ARRIVAL_DATE);
        flightInfo.setFlightNumber(FLIGHT_NO);
        flightInfo.setFlightStatus(tempFlightStatus);

        final FlightInfoService flightInfoService =
        mock(FlightInfoService.class);

        checking(
        new Expectations() {{
            one(flightInfoService).getCurrentStatus();
            will(returnValue(tempFlight));
        }}
        );

        final Flight flight = new Flight();
        flight.setFlightInfoService(flightInfoService);

        FlightStatus response = flight.getFlightStatus();

        assertNotNull(response);
        assertEquals(ALTITUDE, response.getAltitude());
        assertEquals(H_SPEED, response.getHSpeed());
        assertEquals(LATITUDE, response.getLatitude());
        assertEquals(LOCAL_TIME, response.getLocalTime());
        assertEquals(LONGITUDE, response.getLongitude());
        assertEquals(UTC_TIME, response.getUtcTime());
        assertEquals(V_SPEED, response.getVSpeed());
    }

}
