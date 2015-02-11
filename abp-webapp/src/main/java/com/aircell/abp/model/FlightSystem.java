package com.aircell.abp.model;

import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;

/**.
 * Class meant to represent the current status of the critical on-board systems,
 * namely the ATG Link and the ABS Service
 * @author jon.boydell
 */
public class FlightSystem {

    /**. private class variable to hold atgLinkStatus */
    private AtgLinkStatusCodes atgLinkStatus;
    /**. private class variable to hold absServiceStatus */
    private AbsServiceStatusCodes absServiceStatus;
    /**. private class variable to hold noOfActiveSubscribers */
    private int noOfActiveSubscribers;

    private String acpuApplicationVersion;
    /**. private class variable to hold absWhitelistVersion */
    private String absWhitelistVersion;
    /**.
     * Gets AcpuApplicationVersion
     * @return String acpuApplicationVersion
     */
    public String getAcpuApplicationVersion() {
        return acpuApplicationVersion;
    }

    /**.
     * Sets AcpuApplicationVersion
     * @param acpuApplicationVersion AcpuApplication Version
     */
    public void setAcpuApplicationVersion(String acpuApplicationVersion) {
        this.acpuApplicationVersion = acpuApplicationVersion;
    }
    /**. private class variable to hold acpuApplicationVersion */
    /**.
     * Gets absWhitelistVersion
     * @return String absWhitelistVersion
     */
    public String getAbsWhitelistVersion() {
		return absWhitelistVersion;
	}

    /**.
     * Sets absWhitelistVersion
     * @param absWhitelistVersion absWhitelistVersion
     */
	public void setAbsWhitelistVersion(String absWhitelistVersion) {
		this.absWhitelistVersion = absWhitelistVersion;
	}

    /**. Gets AtgLinkStatus
     * @return AtgLinkStatusCodes
     */
    public AtgLinkStatusCodes getAtgLinkStatus() {
        return atgLinkStatus;
    }

    /**.
     * Sets AtgLinkStatus
     * @param atgLinkStatus Atg Link Status
     */
    public void setAtgLinkStatus(final AtgLinkStatusCodes atgLinkStatus) {
        this.atgLinkStatus = atgLinkStatus;
    }

    /**.
     * Gets AbsServiceStatus
     * @return AbsServiceStatusCodes
     */
    public AbsServiceStatusCodes getAbsServiceStatus() {
        return absServiceStatus;
    }

    /**.
     * Sets AbsServiceStatus
     * @param absServiceStatus AbsServiceStatus
     */
    public void setAbsServiceStatus(
    final AbsServiceStatusCodes absServiceStatus
    ) {
        this.absServiceStatus = absServiceStatus;
    }

    /**. @return noOfActiveSubscribers */
    public int getNoOfActiveSubscribers() {
        return this.noOfActiveSubscribers;
    }

    /**. @param noOfActiveSubscribers to set noOfActiveSubscribers */
    public void setNoOfActiveSubscribers(final int noOfActiveSubscribers) {
        this.noOfActiveSubscribers = noOfActiveSubscribers;
    }
}