package com.aircell.abp.model;



/**
 * The <code>AirlineInfomation</code> bean class is used to hold the  airline
 *  information
 * and the Client browser Session id from the test environment Start.jsp
 * the bean values are furthur used in the Abp-test-service package
 * - com.aircell.abp.service.FlightInfoServiceTestImpl for fetching the
 * multiline airline information.
 *
 * @author Suresh Kumar Govindan
 */


public class AirLineInformation {

    /** The session id. */
    private String sessionId;

    /** The Airline code. */
    private String AirlineCode;


    /**
     * Gets the session id.
     *
     * @return the session id
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session id.
     *
     * @param sessionId the new session id
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the airline code.
     *
     * @return the airline code
     */
    public String getAirlineCode() {
        return AirlineCode;
    }

    /**
     * Sets the airline code.
     *
     * @param airlineCode the new airline code
     */
    public void setAirlineCode(String airlineCode) {
        AirlineCode = airlineCode;
    }


}
