package com.aircell.abp.model.rest;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.aircell.abp.model.FlightStatus;

@XmlRootElement(name = "FlightInfo")
public class StatusTrayFlightInfo {
    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;

    private String logo;
    private String airlineName;
    private String airlineCode;
    private String airlineCodeIata;
    private String tailNumber;
    private String flightNumberInfo;    //flightNumber;
    private String flightNumberAlpha;
    private String flightNumberNumeric;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String departureAirportCodeIata;
    private String destinationAirportCodeIata;
    private String origin;  //departureAirportCodeFaa;
    private String destination; //destinationAirportCodeFaa;
	private String departureCity;
    private String destinationCity;
    private Date expectedArrival;
    private String abpVersion;
    private String acpuVersion;
    private boolean videoService;
    private float hSpeed;
    private float vSpeed;
    private float latitude;
    private float longitude;
    private float altitude;
    private String localTime;
    private Date utcTime;
    
    
/*
    origin
    destination   
    flightNumberInfo    
*/    

    public String getLogo(){
        return logo;
    }
    public void setLogo(String logo){
        this.logo = logo;
    }
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
	public String getFlightNumberInfo(){
		return flightNumberInfo;
	}
	public void setFlightNumberInfo(String flightNumberInfo){
		this.flightNumberInfo=flightNumberInfo;
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
	public String getOrigin(){
		return origin;
	}
	public void setOrigin(String origin){
		this.origin = origin;
	}
	public String getDestination(){
		return destination;
	}
	public void setDestination(String destination){
		this.destination = destination;
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

    public Date getUtcTime() {
        return utcTime;
    }
 
    public void setUtcTime(final Date utcTime) {
        this.utcTime = utcTime;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(final String localTime) {
        this.localTime = localTime;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(final float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(final float longitude) {
        this.longitude = longitude;
    }

    public float getVSpeed() {
        return vSpeed;
    }

    public void setVSpeed(final float vSpeed) {
        this.vSpeed = vSpeed;
    }

    public float getHSpeed() {
        return hSpeed;
    }

    public void setHSpeed(final float hSpeed) {
        this.hSpeed = hSpeed;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(final float altitude) {
        this.altitude = altitude;
    }	
	
}
