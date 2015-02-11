package com.aircell.abp.model.utils;

import java.util.Map;

public class IctoToIataAirlineCodeMapping {

	private Map<String, String> airlineCodeTwoChar;

    /**
     * @param airlineCodeTwoChar the airlineCodeTwoChar to set
     */
    public void setAirlineCodeTwoChar(Map<String, String> airlineCodeTwoChar) {
        this.airlineCodeTwoChar = airlineCodeTwoChar;
    }

    /**
     * @return the airlineCodeTwoChar
     */
    public Map<String, String> getAirlineCodeTwoChar() {
        return airlineCodeTwoChar;
    }


}
