package com.aircell.abp.model.rest;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "Response")
public class AvailableServicesWrapper {
    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;

	private int statusCode = 200;
    private ArrayList <AvailableService> availableServices;

	@XmlElement
	public int getStatusCode(){
		return this.statusCode;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	@XmlElement
    public ArrayList<AvailableService> getAvailableServices() {
        return availableServices;
    }
    public void setAvailableServices(ArrayList<AvailableService> availableServices) {
        this.availableServices = availableServices;
    }
}
