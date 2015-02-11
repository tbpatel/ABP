package com.aircell.abp.web.controller;


import com.aircell.abp.model.Flight;

public abstract class ABPAbstractCommandController
extends AircellCommandController {


    String paramName = null;

    String methodName = null;

    String httpPostCookieName = null;

    String httpPostDomain = null;

    String httpClientGbpPage = null;

    String httpPath = null;

    int cookieMaxAge = -1;

    boolean cookieSecure = false;

    Flight flight = null;

    public String getParamName() {
        return paramName;
    }


    public void setParamName(String st) {
        this.paramName = st;
    }

    public String getMethodName() {
        return methodName;
    }


    public void setMethodName(String st) {
        this.methodName = st;
    }

    public String getHttpPostCookieName() {
        return httpPostCookieName;
    }


    public void setHttpPostCookieName(String st) {
        this.httpPostCookieName = st;
    }

    public String getHttpPostDomain() {
        return httpPostDomain;
    }


    public void setHttpPostDomain(String st) {
        this.httpPostDomain = st;
    }

    public String getHttpClientGbpPage() {
        return httpClientGbpPage;
    }


    public void setHttpClientGbpPage(String st) {
        this.httpClientGbpPage = st;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getHttpPath() {
        return httpPath;
    }


    public void setHttpPath(String st) {
        this.httpPath = st;
    }

    public int getCookieMaxAge() {
        return this.cookieMaxAge;
    }

    public void setCookieMaxAge(int age) {
        this.cookieMaxAge = age;
    }

    public boolean isCookieSecure() {
        return this.cookieSecure;
    }

    public void setCookieSecure(boolean cookieSecurity) {
        this.cookieSecure = cookieSecurity;
    }
}
