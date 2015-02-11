package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**. Integration test for end-to-end testing of Flight Info services */
public class FlightInfoIntegrationTest extends TestCase {

    static final String appCtxLocation =
    "com/aircell/abp/service/flightinfo-jms-int-test-spring-cfg.xml";

    private ApplicationContext ctx;

    /**. Executes the flight info integration test under ideal circumstances */
    public void testHappyPath() {
        final FlightInfoJmsImpl underTest =
        (FlightInfoJmsImpl) ctx.getBean("flightInfoService");
        final Flight response = underTest.getCurrentStatus();
        assertNotNull(response);
        assertEquals(
        "KJFK", response.getFlightInformation().getDestinationAirportCode()
        );
        assertEquals(
        "KIAD", response.getFlightInformation().getDepartureAirportCode()
        );
        assertEquals(
        "AA123", response.getFlightInformation().getFlightNumber()
        );
    }

    /**. Creates the Spring Context */
    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(appCtxLocation);
    }

}
