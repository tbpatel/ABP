package com.aircell.abp.model.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Service")
public class AvailableService {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;

	private String name;
	private String URL;

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getURL(){
		return URL;
	}
	public void setURL(String URL){
		this.URL = URL;
	}
}
