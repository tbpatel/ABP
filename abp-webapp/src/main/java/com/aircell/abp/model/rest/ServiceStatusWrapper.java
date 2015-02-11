package com.aircell.abp.model.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class ServiceStatusWrapper {

	private int statusCode = 200;
    private ServiceStatus serviceStatus;

	@XmlElement
	public int getStatusCode(){
		return this.statusCode;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	@XmlElement
    public ServiceStatus getServiceStatus(){
		return this.serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus){
		this.serviceStatus = serviceStatus;
	}
}

