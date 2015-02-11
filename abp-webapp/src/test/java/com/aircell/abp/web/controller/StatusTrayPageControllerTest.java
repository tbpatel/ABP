package com.aircell.abp.web.controller;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


public class StatusTrayPageControllerTest extends MockObjectTestCase {

    private ModelAndView modelAndView;
    private StatusTrayPageController underTest;

    private MockHttpServletRequest request;
    private HttpServletResponse response;
    // private AirLineInformation airlineInfo;
    private Flight flight;
    private FlightInformation flightInfo;

    protected void setUp() throws Exception {
        super.setUp();

        modelAndView = new ModelAndView();
        underTest = new StatusTrayPageController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        flight = new Flight();
        underTest.setFlight(flight);
        flightInfo = new FlightInformation();
        flight.setFlightInformation(flightInfo);
        flightInfo.setDestinationAirportCode("destinationAirportCode");
        flightInfo.setDepartureAirportCode("departureAirportCode");
        flightInfo.setFlightNumber("flightNumber");
        flightInfo.setAirlineCode("airlineCode");
        request.setParameter("username", "username");
        modelAndView.setViewName("statusTray");

    }

    public void testHandlerSuccess() {

        modelAndView = underTest.handler(request, response);
        assertNotNull(modelAndView);

    }

}