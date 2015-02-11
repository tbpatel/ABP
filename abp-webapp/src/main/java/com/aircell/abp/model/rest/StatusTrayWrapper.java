package com.aircell.abp.model.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class StatusTrayWrapper {

    private int status;
    private StatusTrayFlightInfo flightInfo;
    private String gogoFacts;
    private ServiceInfo serviceInfo;
  
    public int getStatus(){
        return status;
    }    
    public void setStatus(int status){
        this.status = status;
    }

    public StatusTrayFlightInfo getFlightInfo(){
        return flightInfo;
    }
    public void setFlightInfo(StatusTrayFlightInfo flightInfo) {
        this.flightInfo = flightInfo;
    }
    
    public String getGogoFacts(){
        return gogoFacts;
    }
    public void setGogoFacts(String gogoFacts){
        this.gogoFacts = gogoFacts;
    }  
    public ServiceInfo getServiceInfo(){
        return serviceInfo;
    }
    public void setServiceInfo(ServiceInfo serviceInfo){
        this.serviceInfo = serviceInfo;
    }
}
