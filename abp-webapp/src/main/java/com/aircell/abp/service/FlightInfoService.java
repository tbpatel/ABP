package com.aircell.abp.service;

import com.aircell.abp.model.Flight;

/**.
 * Classes implementing this interface return the current Flight object, that
 * is, the current status of the aircraft
 */
public interface FlightInfoService {

    /**.
     * Gets the status of the Flight, it is down to those classes that implement
     * this method whether or not this is the Flight at the current moment in
     * time or some time in the past
     * @return Current Flight status
     */
    public Flight getCurrentStatus();

}
