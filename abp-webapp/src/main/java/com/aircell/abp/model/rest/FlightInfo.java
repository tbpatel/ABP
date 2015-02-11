package com.aircell.abp.model.rest;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.aircell.abp.model.FlightStatus;

@XmlRootElement(name = "FlightInfo")
public class FlightInfo {
    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;

    private String airlineName;
    private String airlineCode;
    private String airlineCodeIata;
    private String tailNumber;
    private String flightNumber;
    private String flightNumberAlpha;
    private String flightNumberNumeric;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String departureAirportCodeIata;
    private String destinationAirportCodeIata;
    private String departureAirportCodeFaa;
    private String destinationAirportCodeFaa;
	private String departureCity;
    private String destinationCity;
    private Date expectedArrival;
    private String abpVersion;
    private String acpuVersion;
    private boolean videoService;
    private FlightStatus flightStatus;
    private String mediaCount;
    private String mediaTrailerCount;

	public String getAirlineName(){
		return airlineName;
	}
	public void setAirlineName(String airlineName){
		this.airlineName = airlineName;
	}
	public String getAirlineCode (){
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode){
		this.airlineCode = airlineCode;
	}
	public String getAirlineCodeIata(){
		return airlineCodeIata;
	}
	public void setAirlineCodeIata(String airlineCodeIata){
		this.airlineCodeIata = airlineCodeIata;
	}
	public String getTailNumber(){
		return tailNumber;
	}
	public void setTailNumber(String tailNumber){
		this.tailNumber=tailNumber;
	}
	public String getFlightNumber(){
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber){
		this.flightNumber=flightNumber;
	}
	public String getFlightNumberAlpha(){
		return flightNumberAlpha;
	}
	public void setFlightNumberAlpha(String flightNumberAlpha){
		this.flightNumberAlpha=flightNumberAlpha;
	}
	public String getFlightNumberNumeric(){
		return flightNumberNumeric;
	}
	public void setFlightNumberNumeric(String flightNumberNumeric){
		this.flightNumberNumeric=flightNumberNumeric;
	}
	public String getDepartureAirportCode(){
		return departureAirportCode;
	}
	public void setDepartureAirportCode(String departureAirportCode){
		this.departureAirportCode = departureAirportCode;
	}
	public String getDestinationAirportCode(){
		return destinationAirportCode;
	}
	public void setDestinationAirportCode(String destinationAirportCode){
		this.destinationAirportCode = destinationAirportCode;
	}
	public String getDepartureAirportCodeIata(){
		return departureAirportCodeIata;
	}
	public void setDepartureAirportCodeIata(String departureAirportCodeIata){
		this.departureAirportCodeIata = departureAirportCodeIata;
	}
	public String getDestinationAirportCodeIata(){
		return destinationAirportCodeIata;
	}
	public void setDestinationAirportCodeIata(String destinationAirportCodeIata){
		this.destinationAirportCodeIata = destinationAirportCodeIata;
	}
	public String getDepartureAirportCodeFaa(){
		return departureAirportCodeFaa;
	}
	public void setDepartureAirportCodeFaa(String departureAirportCodeFaa){
		this.departureAirportCodeFaa = departureAirportCodeFaa;
	}
	public String getDestinationAirportCodeFaa(){
		return destinationAirportCodeFaa;
	}
	public void setDestinationAirportCodeFaa(
					String destinationAirportCodeFaa){
		this.destinationAirportCodeFaa = destinationAirportCodeFaa;
	}
	public String getDepartureCity(){
		return departureCity;
	}
	public void setDepartureCity(String departureCity){
		this.departureCity = departureCity;
	}
	public String getDestinationCity(){
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity){
		this.destinationCity = destinationCity;
	}
	public Date getExpectedArrival (){
		return expectedArrival;
	}
	public void setExpectedArrival(Date expectedArrival){
		this.expectedArrival = expectedArrival;
	}
	public String getAbpVersion(){
		return abpVersion;
	}
	public void setAbpVersion(String abpVersion){
		this.abpVersion = abpVersion;
	}
	public String getAcpuVersion(){
		return acpuVersion;
	}
	public void setAcpuVersion(String acpuVersion){
		this.acpuVersion = acpuVersion;
	}
	public boolean getVideoService(){
		return videoService;
	}
	public void setVideoService(boolean videoService){
		this.videoService = videoService;
	}

	public FlightStatus getFlightStatus(){
		return flightStatus;
	}

	public void setFlightStatus(FlightStatus flightStatus){
		this.flightStatus = flightStatus;
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
