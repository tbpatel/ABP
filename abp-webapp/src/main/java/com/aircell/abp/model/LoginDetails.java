/*
 * LoginDetails.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import java.io.Serializable;
import java.util.Random;

/**.
 * Represents all that the user needs to be able to log into the AirCell
 * solution. The service that this object is eventually passed to is responsible
 * for working out whether this object is valid.
 * @author jon.boydell
 */
public class LoginDetails implements Serializable {

    /**. class variable to hold serialVersionUID */
    private static final long serialVersionUID = -4989280799841805936L;
    /**. class variable to hold username */
    private String username;
    /**. class variable to hold password */
    private String password;
    /**. class variable to hold rememberme */
    private boolean rememberme;
    /**. class variable to hold termsofuse */
    private boolean termsofuse;
    /**. class variable to hold ipAddress */
    private String ipAddress;
    /**. class variable to hold domain */
    private String domain;
    /**. class variable to hold locale */
    private String locale;
    /**. class variable to hold emailAddress */
    private String emailAddress;
    /**. class variable to hold trackingId */
    private String trackingId;
    /**The Realm value can be IPASS or AT&T from dropdown.*/
    private String realm;
	/**Gets the Realm Value.
	 * @return String Realm
	 */
	public String getRealm() {
		return realm;
	}
	/**Sets the Realm value.
	 * @param realm realm
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}
    /**.
     * Default constructor
     *
     */
    public LoginDetails() {
    }

    /**.
     * Parameterized constructor
     * @param username Username
     */
    public LoginDetails(final String username) {
        setUsername(username);
    }

    /**.
     * Parameterized constructor
     * @param username Username
     * @param password Password
     */
    public LoginDetails(final String username, final String password) {
        setUsername(username);
        setPassword(password);
    }

    /**.
     * Gets Password
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    /**.
     * Sets Password
     * @param password Password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**.
     * Gets Username
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**.
     * Sets Username
     * @param username Username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**.
     * Gets IpAddress
     * @return String ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }
    /**.
     * Sets IpAddress
     * @param ipAddress user IP address
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**.
     * Gets Domain
     * @return String domain
     */
    public String getDomain() {
        return domain;
    }

    /**.
     * Sets Domain
     * @param domain domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    //Added - Mahi - New ESB Changes for MAPA
    /**.
     * Gets Local
     * @return  String locale
     */
    public String getLocale() {
        return locale;
    }

    /**.
     * Sets Locale
     * @param locale Local
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**.
     * Gets EmailAddress
     * @return String emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**.
     * sets EmailAddress
     * @param emailAddress email address
     */
    public void setEmailAdress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**.
     * Gets Tracking
     * @return String trackingId
     */
    public String getTracking() {
        trackingId = getTrackingID();
        return trackingId;
    }

    /**.
     * Sets Tracking
     * @param trackingId user tracking ID
     */
    public void setTracking(final String trackingId) {
        this.trackingId = trackingId;
    }

    /**.
     * Gets TrackingID
     * @return String trackNo
     */
    public String getTrackingID() {
        String trackNo = getUsername();
        Random generator = new Random();
        int rand = generator.nextInt();
        trackNo = trackNo + Integer.toString(rand);
        return trackNo;
    }

    /**.
     * Overrides superclass
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[LoginDetails: username '");
        sb.append(getUsername());
        sb.append("', password '");
        sb.append("<hidden>");
        sb.append("', ipAddress '");
        sb.append(getIpAddress());
        sb.append("', domain '");
        sb.append(getDomain());
        sb.append("']");
        return sb.toString();
    }

    /**.
     * Overrides superclass
     * @return boolean
     * @param o Object
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoginDetails)) {
            return false;
        }
        LoginDetails det = (LoginDetails) o;
        if (null != det.getUsername()) {
            if (!det.getUsername().equals(getUsername())) {
                return false;
            }
        } else if (null != getUsername()) {
            return false;
        }
        if (null != det.getPassword()) {
            if (!det.getPassword().equals(getPassword())) {
                return false;
            }
        } else if (null != getPassword()) {
            return false;
        }
        if (null != det.getDomain()) {
            if (!det.getDomain().equals(getDomain())) {
                return false;
            }
        } else if (null != getDomain()) {
            return false;
        }

        return true;
    }

    /**.
     * This method checks if the rememberme  checkbox in SignIn jsp is checked
     * in or not
     * @return if rememberme is true or false
     */
    public boolean isRememberme() {
        return rememberme;
    }

    /**.
     * This method is used to set the rememberme as true or false after checking
     * the cookies
     * @param rememberme is set to true if the rememberme checkbox in SignIn jsp
     * is checked in
     */
    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }

    /**.
     * This method checks if the termsofuse checkbox in SignIn jsp is checked in
     * or not
     * @return if termsofuse is true or false
     */
    public boolean isTermsofuse() {
        return termsofuse;
    }

    /**.
     * This method is used to set the termsofuse as true or false after checking
     * the cookies
     * @param termsofuse is set to true if the termsofuse checkbox in SignIn jsp
     * is checked in
     */
    public void setTermsofuse(boolean termsofuse) {
        this.termsofuse = termsofuse;
    }
}
