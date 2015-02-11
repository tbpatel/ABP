package com.aircell.abp.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abp.web.servlet.session.AircellSessionManager;

public class CookieErrorControllerTest extends MockObjectTestCase {

    private ModelAndView modelAndView;
    private CookieErrorController underTest;

    private MockHttpServletRequest request;
    private HttpServletResponse response;
    private MockHttpSession mockSession;
    private Flight flight;
    private FlightInformation flightInformation;
    private String airlineCode = "airlineCode";
    private String airlineName = "airlineName";
    private String aircraftTailNumber = "aircraftTailNumber";
    private String departureAirportCode = "departureAirportCode";
    private String destinationAirportCode = "destinationAirportCode";

    protected void setUp() throws Exception {
        super.setUp();

        modelAndView = new ModelAndView();
        underTest = new CookieErrorController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        mockSession = new MockHttpSession();
        request.setSession(mockSession);
        final AircellSessionManager manager = new AircellSessionManager();
        mockSession.setAttribute(AircellServletUtils.SESSION_MANAGER, manager);
        flightInformation = new FlightInformation();
        flight = new Flight();
        underTest.setFlight(flight);

    }

    public void testHandler_NotNull() {
        flight.setFlightInformation(flightInformation);
        flightInformation.setAirlineCode(airlineCode);
        flightInformation.setFlightNumber(airlineName);
        flightInformation.setDepartureAirportCode(departureAirportCode);
        flightInformation.setDestinationAirportCode(destinationAirportCode);
        modelAndView = underTest.handler(request, response);
        assertNotNull(modelAndView);
    }

    public void testHandler_Null() {
        mockSession = null;
        request.setSession(mockSession);
        flight.setFlightInformation(null);
        modelAndView = underTest.handler(request, response);
        assertNull(modelAndView);
    }

}
