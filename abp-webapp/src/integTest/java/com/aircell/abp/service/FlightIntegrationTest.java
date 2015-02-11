package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightStatus;
import com.aircell.abp.model.FlightSystem;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**.
 * Integration test for end-to-end testing of the whole Flight, including flight
 * info, ATG link state and flight service state
 */
public class FlightIntegrationTest extends TestCase {

    static final String appCtxLocation =
    "com/aircell/abp/service/flight-int-test-spring-cfg.xml";

    private ApplicationContext ctx;

    /**. Executes the flight integration test under ideal circumstances */
    public void testHappyPath() {
        final Flight flight = (Flight) ctx.getBean("flight");
        final FlightStatus flightStatus = flight.getFlightStatus();

        assertNotNull(flightStatus);
        assertEquals(
        "KJFK", flight.getFlightInformation().getDestinationAirportCode()
        );
        assertEquals(
        "KIAD", flight.getFlightInformation().getDepartureAirportCode()
        );
        assertEquals("AA123", flight.getFlightInformation().getFlightNumber());

        final FlightSystem flightSystem = flight.getFlightSystem();

        assertNotNull(flightSystem);
        assertEquals(
        AtgLinkStatusCodes.ATG_LINK_UP, flightSystem.getAtgLinkStatus()
        );
        assertEquals(
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE,
        flightSystem.getAbsServiceStatus()
        );
    }

    /**. Creates the Spring Context */
    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(appCtxLocation);
    }

}
