package com.aircell.abp.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* ****************************************************************************
* File Name     : AircellUtils.java
* Author        : Geethu Sriramulu
* Owner         : Excelacom Technologies
* Creation Date : Apr 13, 2009
* Last Modified :
* Usage         : This constant class is for abp-model package
* *****************************************************************************
* Revision History
* Version        Date           Author                         Description
*
* 1.00.00       Apr 13, 2009  Geethu Sriramulu        Initial development
* *****************************************************************************
*/
public class AircellUtils {
	/**. Logger object */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**. String ENTERPRISE */
	public static final String ENTERPRISE = "ENTERPRISE";
	/**. String MOBILE */
	public static final String MOBILE = "mobile";
	/**. String MB */
	public static final String MB = "MB";
	/**. String AIRLINE_CODE */
	public static final String AIRLINE_CODE = "airlineCode";
	/**. String ACPU_VERSION_KU */
	public static final String ACPU_VERSION_KU = "6.3.0";
	/**. String FLIGHT_TYPE_ATG */
	public static final String FLIGHT_TYPE_ATG = "ATG";
	/**. String FLIGHT_TYPE_KU */
	public static final String FLIGHT_TYPE_KU = "KU";
	/**. String FLIGHT_TYPE_STR */
	public static final String FLIGHT_TYPE_STR = "flightType";
	
	/**. static String acpuVersionAvailable */
    private static boolean acpuVersionAvailable = false;
    /**. static String flightType */
    private static String flightType = FLIGHT_TYPE_ATG;
    
	
	   /** . Gets the version of ACPU  and
     *  sets the flight type to KU (if version > =6.3.0) or ATG
     * @param filePath
     * @return flightType String
     */
    public String getFlightType(String acpuVersion) {
        logger.debug("ABPDefaultController.getAcpuVersion :: enters with acpuVersionAvailable - "
                + acpuVersionAvailable + " and flightType - " + flightType);
        if(!acpuVersionAvailable) {
            flightType = FLIGHT_TYPE_ATG;
            acpuVersionAvailable = true;
            try {
                 logger.debug("AircellUtils.getAcpuVersion :: acpuVersion - " + acpuVersion);
                    if(acpuVersion != null) {
                        int version = compareVersion(acpuVersion, ACPU_VERSION_KU);
                        if(version >= 0) {
                            flightType = FLIGHT_TYPE_KU;
                            logger.debug("AircellUtils.getAcpuVersion :: flightType ku - " + flightType);
                        } else {
                            flightType = FLIGHT_TYPE_ATG;
                            logger.debug("AircellUtils.getAcpuVersion :: flightType atg - " + flightType);
                        }
                    }
                } catch (Exception e) {
                logger.error("Exception in AircellUtils.getAcpuVersion - " + e +"for version "+ acpuVersion);
                return flightType;
            }
        }
        logger.debug("ABPDefaultController.getAcpuVersion :: exits with flightType - " + flightType);
        return flightType;
    } 
    
    /** . Compares two version Strings and returns 0 if ver1 = ver2.
     *  . Returns -1 if ver1 < ver2
     *  . Returns 1 if ver1 > ver2
     * @param ver1
     * @param ver2
     * @return Integer value
     */
    private Integer compareVersion(String ver1, String ver2) { 
		String[] array1 = ver1.split("\\.");
		String[] array2 = ver2.split("\\.");
        Integer v1;  
        Integer v2;  
        try { 
	        for (int i = 0; i < array1.length; i++) { 
	        	v1 = Integer.valueOf(array1[i]);
	        	v2 = Integer.valueOf(array2[i]);
	            if (v1 != v2) { 
	                return v1.compareTo(v2);  
	            }  
	        }
        } catch (Exception e) {  
            logger.error("Exception in ABPDefaultController.compareVersion while comaring two string versions - " + e);
            return -1;
        }    
        return 0;  
    }
}
