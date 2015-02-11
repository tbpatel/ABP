package com.aircell.abp.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/** . This class provides the flight information */

@XmlRootElement
public class FlightInformation implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;
    /**. private class variable to hold airlineCode */
    private String airlineCode;
    /**. private class variable to hold airlineName */
    private String airlineName;
    /**. private class variable to hold aircraftTailNumber */
    private String aircraftTailNumber;
    /**. private class variable to hold departureAirportCode */
    private String departureAirportCode;
    /**. private class variable to hold destinationAirportCode */
    private String destinationAirportCode;
    /**. private class variable to hold expectedArrival */
    private Date expectedArrival;
    /**. private class variable to hold flightNumber */
    private String flightNumber;
    /**. private class variable to hold noOfActiveSubscribers */
    private String noOfActiveSubscribers;
    /**. private class variable to hold flightStatus */
    private FlightStatus flightStatus;
    /**. private class variable to hold abpVersionNo */
    private String abpVersionNo;
    /** . private class variable to hold videoServiceAvailability */
    private String videoServiceAvailability;
    private String mediaCount;
    
   private String mediaTrailerCount;
   
    public FlightInformation(){}
    
    /**.
     * Gets AbpVersionNo
     * @return String abpVersionNo
     */
    public String getAbpVersionNo() {
        return abpVersionNo;
    }

    /**.
     * Sets AbpVersionNo
     * @param abpVersionNo abp Version
     */
    public void setAbpVersionNo(String abpVersionNo) {
        this.abpVersionNo = abpVersionNo;
    }

    /**.
     * Gets AcpuVersionNo
     * @return String acpuVersionNo
     */
    public String getAcpuVersionNo() {
        return acpuVersionNo;
    }

    /**.
     * Sets AcpuVersionNo
     * @param acpuVersionNo acpu Version
     */
    public void setAcpuVersionNo(String acpuVersionNo) {
        this.acpuVersionNo = acpuVersionNo;
    }

    /**. private class variable to hold acpuVersionNo */
    private String acpuVersionNo;

    /** . private class variable to hold absWhitelistVersionNo */
    private String absWhitelistVersionNo;  

    /**. IATA Airline Code for the flight received from Passur. */
    private String airlineCodeIata;
    /**. IATA code for the departure airport as obtained from PASSUR */
    private String departureAirportCodeIata;
    /**. FAA code for the departure airport as obtained from PASSUR */
    private String departureAirportCodeFaa;
    /**. departure city name as obtained from PASSUR */
    private String departureCity;
    /**. Alpha part of Flight Number as received from PASSUR */
    private String destinationAirportCodeIata;
    /**. FAA code for the destination airport as obtained from PASSUR */
    private String destinationAirportCodeFaa;
    /**. destination city name as obtained from PASSUR */
    private String destinationCity;
    /**. private class variable to hold flightNumberAlpha */
    private String flightNumberAlpha;
    /**. Numeric part of Flight Number as received from PASSUR */
    private String flightNumberNumeric;


    /**.
     * Gets AircraftTailNumber
     * @return Aircrafts tail (registration) number
     */
    public String getAircraftTailNumber() {
        return aircraftTailNumber;
    }

    /**.
     * Sets AircraftTailNumber
     * @param aircraftTailNumber AircraftTailNumber
     */
    public void setAircraftTailNumber(final String aircraftTailNumber) {
        this.aircraftTailNumber =
        aircraftTailNumber == null ? aircraftTailNumber
                : aircraftTailNumber.trim();
    }

    /**.
     * Gets AirlineCode
     * @return Two character airline code
     */
    public String getAirlineCode() {
        return airlineCode;
    }

    /**.
     * Sets AirlineCode
     * @param airlineCode AirlineCode
     */
    public void setAirlineCode(final String airlineCode) {
        this.airlineCode =
        airlineCode == null ? airlineCode : airlineCode.trim();
    }

    /**.
     * Gets AirlineName
     * @return the airlineName
     */
    public String getAirlineName() {
        return airlineName;
    }

    /**.
     * Sets AirlineName
     * @param airlineName the airlineName to set
     */
    public void setAirlineName(String airlineName) {
        this.airlineName =
        airlineName == null ? airlineName : airlineName.trim();
    }

    /**.
     * Gets DepartureAirportCode
     * @return Three letter departure airport code
     */
    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    /**.
     * Sets DepartureAirportCode
     * @param departureAirportCode DepartureAirportCode
     */
    public void setDepartureAirportCode(final String departureAirportCode) {
        this.departureAirportCode =
        departureAirportCode == null ? departureAirportCode
                :  departureAirportCode.trim();
    }

    /**.
     * Gets DestinationAirportCode
     * @return Three letter destination airport code
     */
    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    /**.
     * Sets DestinationAirportCode
     * @param destinationAirportCode DestinationAirportCode
     */
    public void setDestinationAirportCode(final String destinationAirportCode) {
        this.destinationAirportCode =
        destinationAirportCode == null ? destinationAirportCode
                : destinationAirportCode.trim();
    }

    /**.
     * Gets ExpectedArrival
     * @return Time and date of expected arrival
     */
    public Date getExpectedArrival() {
        return expectedArrival;
    }

    /**.
     * Sets ExpectedArrival
     * @param expectedArrival ExpectedArrival
     */
    public void setExpectedArrival(final Date expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    /**.
     * Gets FlightNumber
     * @return Flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**.
     * Sets FlightNumber
     * @param flightNumber FlightNumber
     */
    public void setFlightNumber(final String flightNumber) {
        this.flightNumber =
        flightNumber == null ? flightNumber : flightNumber.trim();
    }

    /**.
     * Gets FlightStatus
     * @return FlightStatus
     */
    public FlightStatus getFlightStatus() {
        return this.flightStatus;
    }

    /**.
     * Sets FlightStatus
     * @param flightStatus FlightStatus
     */
    public void setFlightStatus(final FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    /**.
     * Gets AirlineCodeIata
     * @return IATA Airline Code for the flight received from Passur.
     */
    public String getAirlineCodeIata() {
        return this.airlineCodeIata;
    }

    /**.
     * Sets AirlineCodeIata
     * @param airlineCodeIata to set IATA Airline Code
     */
    public void setAirlineCodeIata(String airlineCodeIata) {
        this.airlineCodeIata =
        airlineCodeIata == null ? airlineCodeIata : airlineCodeIata.trim();
    }

    /**.
     * Gets DepartureAirportCodeIata
     * @return departureAirportCodeIata IATA code for the departure airport
     */
    public String getDepartureAirportCodeIata() {
        return this.departureAirportCodeIata;
    }

    /**.
     * Sets DepartureAirportCodeIata
     * @paran departureAirportCodeIata to set IATA
     * code for the departure airport
     */
    public void setDepartureAirportCodeIata(String departureAirportCodeIata) {
        this.departureAirportCodeIata =
        departureAirportCodeIata == null ? departureAirportCodeIata
                :  departureAirportCodeIata.trim();
    }

    /**.
     * Gets DepartureAirportCodeFaa
     * @return departureAirportCodeFaa FAA code for the departure airport
     */
    public String getDepartureAirportCodeFaa() {
        return this.departureAirportCodeFaa;
    }

    /**.
     * Sets DepartureAirportCodeFaa
     * @paran departureAirportCodeFaa to set FAA code for the departure airport
     */
    public void setDepartureAirportCodeFaa(String departureAirportCodeFaa) {
        this.departureAirportCodeFaa =
        departureAirportCodeFaa == null ? departureAirportCodeFaa :
        departureAirportCodeFaa.trim();
    }

    /**. @return departureCity name as obtained from PASSUR */
    public String getDepartureCity() {
        return this.departureCity;
    }

    /**. @param departureCity to set departure city */
    public void setDepartureCity(String departureCity) {
        this.departureCity =
        departureCity == null ? departureCity : departureCity.trim();
    }

    /**.
     * @return destinationAirportCodeIata IATA code for the destination airport
     */
    public String getDestinationAirportCodeIata() {
        return this.destinationAirportCodeIata;
    }

    /**.
     * @paran destinationAirportCodeIata to set IATA
     *  code for the destination airport
     */
    public void setDestinationAirportCodeIata(
    String destinationAirportCodeIata
    ) {
        this.destinationAirportCodeIata =
        destinationAirportCodeIata == null ? destinationAirportCodeIata
                : destinationAirportCodeIata.trim();
    }

    /**.
     * @return destinationAirportCodeFaa FAA code for the destination airport
     */
    public String getDestinationAirportCodeFaa() {
        return this.destinationAirportCodeFaa;
    }

    /**.
     * @paran destinationAirportCodeFaa to set FAA
     *  code for the destination airport
     */
    public void setDestinationAirportCodeFaa(String destinationAirportCodeFaa) {
        this.destinationAirportCodeFaa =
        destinationAirportCodeFaa == null ? destinationAirportCodeFaa :
        destinationAirportCodeFaa.trim();
    }

    /**. @return destinationCity name as obtained from PASSUR */
    public String getDestinationCity() {
        return this.destinationCity;
    }

    /**. @param destinationCity to set destination city */
    public void setDestinationCity(String destinationCity) {
        this.destinationCity =
        destinationCity == null ? destinationCity : destinationCity;
    }

    /**.
     * @return flightNumberAlpha Alpha part of Flight Number as received from
     *         PASSUR
     */
    public String getFlightNumberAlpha() {
        return this.flightNumberAlpha;
    }

    /**. @param flightNumberAlpha to set flightNumberAlpha */
    public void setFlightNumberAlpha(String flightNumberAlpha) {
        this.flightNumberAlpha =
        flightNumberAlpha == null ? flightNumberAlpha : flightNumberAlpha;
    }

    /**.
     * @return flightNumberNumeric Numeric part of Flight Number as received
     *         from PASSUR
     */
    public String getFlightNumberNumeric() {
        return this.flightNumberNumeric;
    }

    /**. @param flightNumberNumeric to set flightNumberNumeric */
    public void setFlightNumberNumeric(String flightNumberNumeric) {
        this.flightNumberNumeric =
        flightNumberNumeric == null ? flightNumberNumeric : flightNumberNumeric;
    }

    /**.
     * Overrides superclass method
     * @return String String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**.
     * Gets NoOfActiveSubscribers
     * @return String NoOfActiveSubscribers
     */
    public String getNoOfActiveSubscribers() {
        return this.noOfActiveSubscribers;
    }

    /**.
     * Gets NoOfActiveSubscribers
     * @param noOfActiveSubscribers No Of Active Subscribers
     */
    public void setNoOfActiveSubscribers(String noOfActiveSubscribers) {
        this.noOfActiveSubscribers = noOfActiveSubscribers;
    }
    /**
     * @return the videoServiceAvailability
     */
    public String getVideoServiceAvailability() {
        return videoServiceAvailability;
    }

    /**
     * @param videoServiceAvailability the videoServiceAvailability to set
     */
    public void setVideoServiceAvailability(String videoServiceAvailability) {
        this.videoServiceAvailability = videoServiceAvailability;
    }    
    /**
     * Get Video AbsWhitelistVersionNo.
     * @return the absWhitelistVersionNo
     */
	public String getAbsWhitelistVersionNo() {
		return absWhitelistVersionNo;
	}

	 /**
     * Set AbsWhitelistVersionNo
     * @param absWhitelistVersionNo the absWhitelistVersionNo to set
     */
	public void setAbsWhitelistVersionNo(String absWhitelistVersionNo) {
		this.absWhitelistVersionNo = absWhitelistVersionNo;
	}

	public String getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(String mediaCount) {
		this.mediaCount = mediaCount;
	}

	public String getMediaTrailerCount() {
		return mediaTrailerCount;
	}

	public void setMediaTrailerCount(String mediaTrailerCount) {
		this.mediaTrailerCount = mediaTrailerCount;
	}
}
