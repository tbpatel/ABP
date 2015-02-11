package com.aircell.abp.model.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class FlightInfoWrapper {

	private int statusCode = 200;
	private FlightInfo flightInfo;

	@XmlElement
	public int getStatusCode(){
		return this.statusCode;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}
	@XmlElement
    public FlightInfo getFlightInfo(){
		return this.flightInfo;
    }

    public void setFlightInfo(FlightInfo flightInfo){
		this.flightInfo = flightInfo;
	}
}

