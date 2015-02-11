package com.aircell.abp.service;

import com.aircell.abp.model.FlightSystem;

/**.
 * Service interface for returning the current status of the critical on-board
 * system, specifically the status of the ATG Link and the status of the ABS
 * Service.
 * @author jon.boydell at AKQA Ltd.
 */
public interface FlightSystemService {

    /**.
     * Returns the current status of the on board systems
     * @return On board systems status
     */
    FlightSystem getCurrentStatus();


}
