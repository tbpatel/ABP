package com.aircell.abp.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class UserFlightSession implements Serializable {

    private static final long serialVersionUID = -5543083627635612815L;

    private String userName;
    private String ip;
    private long remainingTime;
    private Boolean authenticated;
    private Boolean activated;
    /** user MAC address. */
    private String userMac;    

    /**.
     * Gets userName
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**.
     * Sets userName
     * @param userName userName to be set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**.
     * Gets ip
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**.
     * Sets ip
     * @param ip ip to be set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**.
     * Gets remainingTime
     * @return remainingTime
     */
    public long getRemainingTime() {
        return remainingTime;
    }

    /**.
     * Sets remainingTime
     * @param remainingTime remainingTime to be set
     */
    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**.
     * Gets a boolean after checking authentication
     * @return boolean after checking authentication
     */
    public Boolean isAuthenticated() {
        return authenticated;
    }

    /**.
     * Sets a boolean after checking authentication
     * @param authenticated boolean value to be set
     */
    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**.
     * Gets a boolean after checking if activated
     * @return boolean after checking if activated
     */
    public Boolean isActivated() {
        return activated;
    }

    /**.
     * Sets a boolean after checking if activated
     * @param activated boolean value to be set
     */
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    /**.
     * Gets userMac
     * @return userMac
     */
    public String getUserMac() {
        return this.userMac;
    }

    /**.
     * Sets userMac
     * @param userMac to be set
     */
    public void setUserMac(String userMac) {
        this.userMac = userMac;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
