package com.aircell.abp.model.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceStatus {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 8295107940644581961L;

    private boolean serviceStatus;
    private boolean linkStatus;
    private boolean serviceCoverage;


    public boolean isServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(boolean serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public boolean isLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    public boolean isServiceCoverage() {
        return serviceCoverage;
    }

    public void setServiceCoverage(boolean serviceCoverage) {
        this.serviceCoverage = serviceCoverage;
    }
}
