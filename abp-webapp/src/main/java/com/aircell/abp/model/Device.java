/**..
 *
 */
package com.aircell.abp.model;

/**.
 * .
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class Device {
    /**. . name of the device * */
    private String name;
    /**. . min version of the device * */
    private Integer minVersion;
    /**. . whether or not this device is a specifically supported m
     * obile device * */
    private boolean supported;
    /**. . whether or not this device is a known mobile one * */
    private boolean mobile;

    /**. . @return the name */
    public String getName() {
        return name;
    }

    /**. . @param name the name to set */
    public void setName(String name) {
        this.name = name;
    }

    /**. . @return the version */
    public Integer getMinVersion() {
        return minVersion;
    }

    /**. . @param version the version to set */
    public void setMinVersion(Integer version) {
        this.minVersion = version;
    }

    /**. . @return the supported */
    public boolean isSupported() {
        return supported;
    }

    /**. . @param supported the supported to set */
    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    /**. . @return the mobile */
    public boolean isMobile() {
        return mobile;
    }

    /**. . @param mobile the mobile to set */
    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
}
