/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import java.io.Serializable;

/**.
 * This class is used for storing / manupulating the
 * username,password amd ipaddress for a user.
 */
public class UserCredentials implements Serializable {

    /**.. Static variable for declaring the KEY as usercredentials*/
    public final static String KEY = "userCredentials";

    /**.. Static variable declared to store serialVersion UID*/
    private final static long serialVersionUID = 1L;

    /**.. Static variable declared to store ipadress*/
    private String ipAddress;
    /**.. Static variable declared to store mac address*/
    private String macAddress;    
    /**.. Static variable declared to store loginid*/
    private String loginId;
    /**.. Static variable declared to store password*/
    private String password;

    /**.
     * non paramaterized constructor
     */
    public UserCredentials() {
    }

    /**.
     * parameterised constructor get the username, ipaddress, password
     * and sets then in this class.
     * @param ipAddress ipaddress
     * @param loginId loginid
     * @param password password
     */
    public UserCredentials(String ipAddress, String loginId, String password) {
        this.ipAddress = ipAddress;
        this.loginId = loginId;
        this.password = password;
    }

    /**.
     * Getter Method
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**.
     * Getter Method
     * @return loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**.
     * Getter Method
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**.
     * Setter Method
     * @param ipAddress ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**.
     * Setter Method
     * @param loginId loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**.
     * Setter Method
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**.
     * Getter Method
     * @return macAddress String
     */
    public String getMacAddress() {
        return this.macAddress;
    }

   /**.
     * Setter Method
     * @param macAddress String
     */
    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }
}
