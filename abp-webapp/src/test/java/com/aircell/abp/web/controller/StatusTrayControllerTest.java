package com.aircell.abp.web.controller;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.web.form.StatusTrayCommand;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


public class StatusTrayControllerTest extends TestCase {
    StatusTrayController statusTrayControllerUnderTest;
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    StatusTrayCommand command;
    BindException errors;
    private ModelAndView mv;
    private AirPassenger passenger;
    private LoginDetails loginDetails;

    protected void setUp() throws Exception {
        super.setUp();
        statusTrayControllerUnderTest = new StatusTrayController();
        passenger = new AirPassenger();
        loginDetails = new LoginDetails();
        loginDetails.setEmailAdress("gogo@excela.com");
        loginDetails.setDomain("xxxx");
        loginDetails.setIpAddress("10.90.1.1");
        loginDetails.setPassword("firstmlast");
        loginDetails.setTermsofuse(true);
        loginDetails.setUsername("firstmlast");
        passenger.setLoginDetails(loginDetails);
        FlightInformation flightInfo = new FlightInformation();
        flightInfo.setAbpVersionNo("2.455");
        flightInfo.setAirlineCode("VX");
        Flight flight = new Flight();
        flight.setFlightInformation(flightInfo);
        statusTrayControllerUnderTest.setFlight(flight);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        command = new StatusTrayCommand();
        command.setUsername("firstmlast");
        errors = new BindException(statusTrayControllerUnderTest, "exception");
        mv = new ModelAndView();
    }

    public void testhandler() {
        try {
            mv = statusTrayControllerUnderTest.handler(
            request, response, command, errors
            );
            assertNotNull(mv);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
