package com.aircell.abp.model.rest;

public class ServiceInfo {
    private String service;
    private long remaining;
    private String quality;
    private  String[] alerts;
    
    public String getService(){
        return service;        
    }
    public void setService(String service){
        this.service = service;
    }
    public long getRemaining (){
        return remaining;
    }
    public void setRemaining(long remaining){
        this.remaining = remaining;
    }
    public String getQuality(){
        return quality;        
    }
    public void setQuality(String quality){
        this.quality = quality;
    }
    public String[] getAlerts(){
        return alerts;
    }
    public void setAlerts(String[] alerts){
        this.alerts = alerts;
    }
    
}
