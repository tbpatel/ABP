package com.aircell.abp.web.controller;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.FlightSystem;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


public class SubscribersCapControllerTest extends MockObjectTestCase {

    private ModelAndView modelAndView;
    private SubscribersCapController underTest;

    private MockHttpServletRequest request;
    private HttpServletResponse response;
    // private AirLineInformation airlineInfo;
    private Flight flight;
    private FlightInformation flightInfo;
    private Object command;
    private BindException errors;
    private FlightSystem flightSystem;
    private String maxNoOfActiveSubscribers = null;

    protected void setUp() throws Exception {
        super.setUp();

        modelAndView = new ModelAndView();
        underTest = new SubscribersCapController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        command = new Object();
        errors = new BindException(command, "test");

        flight = new Flight();
        underTest.setFlight(flight);
        flightSystem = new FlightSystem();
        flight.setFlightSystem(flightSystem);
        flightSystem.setNoOfActiveSubscribers(40);

    }

    public void testHandler_NotNull() {
        maxNoOfActiveSubscribers = "50";
        request.setParameter(
        "maxNoOfActiveSubscribers", maxNoOfActiveSubscribers
        );
        modelAndView = underTest.handler(request, response, command, errors);
        assertNotNull(modelAndView);
    }

    public void testnoOfActiveSubscribers_Greater_maxSubscribersAllowed() {
        request.setParameter("maxNoOfActiveSubscribers", "30");
        flightSystem.setNoOfActiveSubscribers(60);
        modelAndView = underTest.handler(request, response, command, errors);
        assertNotNull(modelAndView);
    }

    public void testHandler_Null() {
        request.setParameter(
        "maxNoOfActiveSubscribers", maxNoOfActiveSubscribers
        );
        modelAndView = underTest.handler(request, response, command, errors);
        assertNotNull(modelAndView);
    }

}
