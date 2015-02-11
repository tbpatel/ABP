package com.aircell.abp.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**.
 * Represents the current status of the flight, that is; it's position, speed,
 * etc. This class, by comparison to the Flight class represents a snapshot of
 * the progress of a Flight
 * @author jon.boydell at AKQA Inc.
 */

/** . This class provides the flight information */

@XmlRootElement
public class FlightStatus implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 5470326518696884386L;
    /**. private class variable to hold latitude */
    private float latitude;
    /**. private class variable to hold longitude */
    private float longitude;
    /**. private class variable to hold vSpeed */
    private float vSpeed;
    /**. private class variable to hold hSpeed */
    private float hSpeed;
    /**. private class variable to hold altitude */
    private float altitude;
    /**. private class variable to hold utcTime */
    private Date utcTime;
    /**. private class variable to hold localTime */
    private String localTime;

    public FlightStatus(){}
    
    /**.
     * Gets UtcTime
     * @return Date
     */
    public Date getUtcTime() {
        return utcTime;
    }

    /**.
     * Sets UtcTime
     * @param utcTime
     */
    public void setUtcTime(final Date utcTime) {
        this.utcTime = utcTime;
    }

    /**.
     * Gets LocalTime
     * @return String LocalTime
     */
    public String getLocalTime() {
        return localTime;
    }

    /**.
     * Sets LocalTime
     * @param localTime LocalTime
     */
    public void setLocalTime(final String localTime) {
        this.localTime = localTime;
    }

    /**.
     * Gets Latitude
     * @return float
     */
    public float getLatitude() {
        return latitude;
    }

    /**.
     * Sets Latitude
     * @param latitude Latitude
     */
    public void setLatitude(final float latitude) {
        this.latitude = latitude;
    }

    /**.
     * Gets Longitude
     * @return float
     */
    public float getLongitude() {
        return longitude;
    }

    /**.
     * Sets Longitude
     * @param longitude Longitude
     */
    public void setLongitude(final float longitude) {
        this.longitude = longitude;
    }

    /**.
     * Gets VSpeed
     * @return float
     */
    public float getVSpeed() {
        return vSpeed;
    }

    /**.
     * Sets VSpeed
     * @param vSpeed VSpeed
     */
    public void setVSpeed(final float vSpeed) {
        this.vSpeed = vSpeed;
    }

    /**.
     * Gets HSpeed
     * @return float
     */
    public float getHSpeed() {
        return hSpeed;
    }

    /**.
     * Sets HSpeed
     * @param hSpeed HSpeed
     */
    public void setHSpeed(final float hSpeed) {
        this.hSpeed = hSpeed;
    }

    /**.
     * Gets Altitude
     * @return float
     */
    public float getAltitude() {
        return altitude;
    }

    /**.
     * Sets Altitude
     * @param altitude Altitude
     */
    public void setAltitude(final float altitude) {
        this.altitude = altitude;
    }
}
