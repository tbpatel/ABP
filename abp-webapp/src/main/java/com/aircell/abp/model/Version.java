package com.aircell.abp.model;

import java.io.Serializable;


public class Version implements Serializable {
    private String abpVersionNo;
    private String acpuVersion;

    /**.
     * Gets acpuVersion
     * @return acpuVersion
     */
    public String getAcpuVersion() {
        return acpuVersion;
    }

    /**.
     * Sets acpuVersion
     * @param acpuVersion acpuVersion to be set
     */
    public void setAcpuVersion(String acpuVersion) {
        this.acpuVersion = acpuVersion;
    }

    /**.
     * Gets acpuVersionNo
     * @return acpuVersionNO
     */
    public String getAbpVersionNo() {
        return abpVersionNo;
    }

    /**.
     * Sets acpuVersionNO
     * @param abpVersionNo acpuVersionNo to be set
     */
    public void setAbpVersionNo(String abpVersionNo) {
        this.abpVersionNo = abpVersionNo;
    }
}