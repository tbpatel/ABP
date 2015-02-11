/*
 * FlightDetails.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.aircell.abp.service.FlightInfoService;
import com.aircell.abp.service.FlightSystemService;
import com.aircell.abp.service.ServiceException;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;

/**.
 * A flight represents everything that is important about the plane, it current
 * status and the status of the airborne services Call getFlightStatus() to get
 * the current state of the aircraft Call getFlightSystem() to get the object
 * that represents the ABS system
 * @author miroslav.miladinovic at AKQA Inc.
 * @author jon.boydell at AKQA Inc.
 */
public class Flight implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 5385811345586074737L;
    /**. private class variable to hold flightInfo */
    private FlightInformation flightInfo;
    /**. private class variable to hold flightSystem */
    private FlightSystem flightSystem;
    
    private String airlineCode;
    /**. private class variable to hold atgLinkUp */
    private final AtomicBoolean atgLinkUp = new AtomicBoolean(false);
    /**. private class variable to hold inCoverage */
    private final AtomicBoolean inCoverage = new AtomicBoolean(false);
    /**. private class variable to hold FlightInfoService */
    private transient FlightInfoService flightInfoService;
    /**. private class variable to hold FlightSystemService */
    private transient FlightSystemService flightSystemService;
    /**. private class variable to hold refreshMap */
    private Map<Class, AtomicLong> refreshMap;
    /**. private class variable to hold refresh */
    private long refresh = 0;
    /**. private class variable to hold testMode */
    private boolean testMode = false;
    /**. private class variable to hold logger */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**. private class variable to hold prepareToLandAlert */
    private String prepareToLandAlert;
    /**.
     * Video summary configuration file path in the tomcat path
     */
    private String videoConfigFilePath;
    /**.
     * Video framework configuration file path in the tomcat path
     */
    private String videoFrameFilePath;
    /**.
     * variable to hold video service availability flag
     */
    private static boolean videoServiceAvailable = false;
    /**.
     * variable to hold videoServiceInitialed flag.
     */
    private boolean videoServiceInitialized = false;
    
    private static String mediaCount ="0";
    /**.
     * variable to hold mediaPath.
     */
    private String mediaPath;
    /**.
     * variable to hold videoPluginCommand.
     */
	private String videoPluginPath;
    /**.
     * variable to hold mediaFileType.
     */
    private String mediaFileType;
    /**.
     * variable to hold trailerFileType.
     */
    private String trailerFileType;    
    /**.
     * variable to hold boxArtSmallFileType.
     */
    private String boxArtSmallFileType;
    /**.
     * variable to hold boxArtMediumFileType.
     */
    private String boxArtMediumFileType;
    /**.
     * variable to hold boxArtLargeFileType.
     */
    private String boxArtLargeFileType;
    /**.
     * variable to hold boxArtExtraLargeFileType.
     */
    private String boxArtExtraLargeFileType;
    /**.
     * variable to hold mediaTrailerCount.
     */
	private static String mediaTrailerCount = "0";

	/**.
     * Checks TestMode
     * @return boolean
     */
    public boolean isTestMode() {
        return testMode;
    }

    /**.
     * Sets TestMode
     * @param testMode TestMode
     */
    public void setTestMode(boolean testMode){
        this.testMode = testMode;
    }

    /**.
     * An empty constructor, use this create a flight with no dependencies
     */
    public Flight() {
        refreshMap = new HashMap<Class, AtomicLong>();
        refreshMap.put(FlightInformation.class, new AtomicLong(0));
        refreshMap.put(FlightSystem.class, new AtomicLong(0));
    }

    /**.
     * Checks if service is available
     * @return boolean
     */
    public boolean isServiceAvailable() {
        boolean atg = false;
        boolean abs = false;

        final FlightSystem flightSystem = getFlightSystem();

        if (AtgLinkStatusCodes.ATG_LINK_UP
        .equals(flightSystem.getAtgLinkStatus())) {
            atg = true;
        }

        if (AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE
        .equals(flightSystem.getAbsServiceStatus())) {
            abs = true;
        }

        return atg && abs;
    }

    /**.
     * Sets AtgLinkUp
     * @param atgLinkUp AtgLinkUp
     */
    public void setAtgLinkUp(final boolean atgLinkUp){
        if (this.atgLinkUp.getAndSet(atgLinkUp) == !atgLinkUp) {
            refreshFlightSystemStatus();
        }
    }

    /**.
     * Checks ATG Link is UP or DOWN
     * @return boolean
     */
    public boolean isAtgLinkUp() {
        return atgLinkUp.get();
    }

    /**.
     * Sets if the flight is inside coverage area
     * @param inCoverage
     */
    public void setInCoverage(final boolean inCoverage){
        if (this.inCoverage.getAndSet(inCoverage) == !inCoverage) {
            refreshFlightSystemStatus();
        }
    }

    /**.
     * Checks service is available or not
     * @return boolean
     */
    public boolean isAbsServiceAvaiable() {
        if (AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE
        .equals(flightSystem.getAbsServiceStatus())) {
            return true;
        }

        return false;
    }

    /**.
     * Checks the flight is inside coverage area or not
     * @return
     */
    public boolean isInCoverage(){
        return inCoverage.get();
    }

    /**.
     * Gets  FlightInformation
     * @return FlightInformation
     */
    public FlightInformation getFlightInformation() {
        /* logger.debug("tesitng it reachs this");
            if(flightInfo == null || isTestMode()) {
                logger.debug("if exist===))");
                refreshFlightInfo();
            }else
             logger.debug("===))if exist");
        */  //todo magesh modified this
        if (flightInfo == null || isTestMode() || refresh(
        flightInfo, refresh
        )) {
            refreshFlightInfo();
        }
        return flightInfo;
    }

    /**.
     * Seta FlightInformation
     * @param flightInfo FlightInformation
     */
    public void setFlightInformation(final FlightInformation flightInfo) {
        this.flightInfo = flightInfo;
    }

    /**.
     * Gets  FlightStatus
     * @return FlightStatus
     */
    public FlightStatus getFlightStatus() {
        if (refresh(flightInfo, refresh)) {
            refreshFlightInfo();
        }
        return this.flightInfo.getFlightStatus();
    }

    /**.
     * Sets FlightSystem
     * @param flightSystem FlightSystem
     */
    public void setFlightSystem(final FlightSystem flightSystem) {
        this.flightSystem = flightSystem;
    }

    /**.
     * Gets FlightSystem
     * @return FlightSystem
     */
    public FlightSystem getFlightSystem() {
        if (refresh(flightSystem, refresh)) {
            refreshFlightSystemStatus();
        }
        return flightSystem;
    }

    /**.
     * Refreshes FlightInformation
     * @param o
     * @param refresh
     * @return boolean
     */
    private boolean refresh(Object o, long refresh){
        if (refresh == 0 || o == null) {
            return true;
        } else {
            if (refreshMap.containsKey(o.getClass())) {
                long currentTime = new Date().getTime();
                long lastRefresh = refreshMap.get(o.getClass()).get();
                if (lastRefresh + refresh <= currentTime) {
                    return true;
                }
            }
        }

        return false;
    }

    /**.
     * Resfreshes FlightInfo
     *
     */
    private void refreshFlightInfo() {
        if (this.flightInfoService instanceof FlightInfoService) {
            try {
                final Flight currentFlight =
                this.flightInfoService.getCurrentStatus();
                this.flightInfo = currentFlight.getFlightInformation();
                this.flightInfo
                .setFlightStatus(currentFlight.getFlightStatus());
                this.airlineCode =  this.flightInfo.getAirlineCode();
                refreshMap.get(FlightInformation.class)
                .set(new Date().getTime());
            } catch (final ServiceException ex) {
                // do nothing if an exception, leave the flight info as it is
                // the service logs the error and we ignore it
            }
        }
    }

    /**.
     * Refreshes FlightSystemStatus
     *
     */
    private void refreshFlightSystemStatus() {
        if (this.flightSystemService instanceof FlightSystemService) {
            try {
                flightSystem = flightSystemService.getCurrentStatus();
                refreshMap.get(FlightSystem.class).set(new Date().getTime());
            } catch (final ServiceException ex) {

            }
        }
    }

    /**.
     * Methode to override superclass method
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    /**.
     * Checks that the dependencies needed for
     * Spring creation of this object are met
     * @throws IllegalArgumentException If any service
     *  dependency isn't set properly
     */
    public void checkDependenciesSet() throws IllegalArgumentException{
        if (!(flightInfoService instanceof FlightInfoService)) {
            throw new IllegalArgumentException(
            "Flight must have flightInfoService dependency set"
            );
        }

        if (!(flightSystemService instanceof FlightSystemService)) {
            throw new IllegalArgumentException(
            "Flight must have flightSystemService dependency set"
            );
        }
    }

    /**.
     * Sets the flight info service, this method is primarily used for injection
     * @param flightInfoService Implementation of FlightInfoService
     */
    public void setFlightInfoService(
    final FlightInfoService flightInfoService
    ) {
        this.flightInfoService = flightInfoService;
    }

    /**.
     * Sets the flight system service, this
     * method is primarily used for injection
     * @param flightSystemService Implementation of FlightSystemService
     */
    public void setFlightSystemService(
    final FlightSystemService flightSystemService
    ) {
        this.flightSystemService = flightSystemService;
    }

    /**.
     * Sets Refresh time
     * @param refresh Refresh time
     */
    public void setRefresh(long refresh){
        this.refresh = refresh;
    }

    /**.
     * Gets Refresh time
     * @return long
     */
    public long getRefresh() {
        return refresh;
    }

    /**.
     *
     * @return prepareToLandAlert : prepare to land alert message
     */
    public String getPrepareToLandAlert() {
        return this.prepareToLandAlert;
    }

    /**.
     *
     * @param prepareToLandAlert : prepare to land alert message
     */
    public void setPrepareToLandAlert(String prepareToLandAlert){
        this.prepareToLandAlert = prepareToLandAlert;
    }

    /**.
     * Gets No Of ActiveSubscribers
     * @return int No Of ActiveSubscribers
     */
    public int getNoOfActiveSubscribers() {
        final FlightSystem flightSystem = getFlightSystem();
        return flightSystem.getNoOfActiveSubscribers();
    }

    /**
     * @return the videoConfigFilePath
     */
    public String getVideoConfigFilePath() {
        return videoConfigFilePath;
    }
    /**
     * @param videoConfigFilePath the videoConfigFilePath to set
     */
    public void setVideoConfigFilePath(String videoConfigFilePath) {
        this.videoConfigFilePath = videoConfigFilePath;
    }
    /**
     * @return the videoFrameFilePath
     */
    public String getVideoFrameFilePath() {
        return videoFrameFilePath;
    }
    /**
     * @param videoFrameFilePath the videoFrameFilePath to set
     */
    public void setVideoFrameFilePath(String videoFrameFilePath) {
        this.videoFrameFilePath = videoFrameFilePath;		
    }
    
    /**
     * checks video service availability in the flight
     */
    public void checkVideoServiceAvailability() {
    	
    	try
        {
            // This method check the availability of file in tomcat folder

            String tomcat_path = System.getProperty("catalina.base");
            File summaryFile = new File(tomcat_path +getVideoConfigFilePath());
            File videoFrameFile = new File(tomcat_path + getVideoFrameFilePath());
            logger.info(tomcat_path +getVideoConfigFilePath() + "  Exists : " + summaryFile.exists());
            logger.info(tomcat_path +getVideoFrameFilePath() + "  Exists : "  + videoFrameFile.exists() + " , size : " 
            				+ videoFrameFile.length());
            
            if (summaryFile.exists() && summaryFile.isFile()
            		&& videoFrameFile.exists() && videoFrameFile.isFile() && videoFrameFile.length() > 0) {
                videoServiceAvailable = true;
                getMediaCount(summaryFile);
                String mediaFileCount  = getCountByFileType(getMediaPath(), getMediaFileType());
                String trailerFileCount = getCountByFileType(getMediaPath(), getTrailerFileType());
                String boxSmallFileCount = getCountByFileType(getMediaPath(), getBoxArtSmallFileType() );
                String boxMediumFileCount = getCountByFileType(getMediaPath(), getBoxArtMediumFileType());
                String boxLargeFileCount = getCountByFileType(getMediaPath(), getBoxArtLargeFileType());
                String boxExtraLargeFileCount = getCountByFileType(getMediaPath(), getBoxArtExtraLargeFileType());
                String videoPluginCount = getCountByFileType(getVideoPluginPath(),".");
                mediaTrailerCount = mediaFileCount + "||" + trailerFileCount + "||" + boxSmallFileCount+ "||" + boxMediumFileCount + "||" + boxLargeFileCount + "||" + boxExtraLargeFileCount + "||" + videoPluginCount;
                
                
            } else {
                videoServiceAvailable = false;
            }
            logger.debug("videoServiceAvailability : " + videoServiceAvailable);
            
        }catch(Exception e)
        {
            logger.error("Exception occured while checking for"
                + " video service availability");
            videoServiceAvailable = false;
        }
        videoServiceInitialized = true;
    }

    /**
     * @return videoServiceAvailable
     */
    public boolean isVideoServiceAvailable() {
    	if(!videoServiceInitialized){
    		if(!videoServiceAvailable){
    			checkVideoServiceAvailability();
    		}   		
    	}
        return videoServiceAvailable;
    }
    
    public String getMediaCount(){
    	return mediaCount;
    }
    
    public String getMediaTrailerFileCount(){
    	
    	return mediaTrailerCount;
    }
    public void getMediaCount(File summaryFile) {
    	try {			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(summaryFile);
			mediaCount = Integer.toString(doc.getElementsByTagName("media").getLength());
			//Getting the count of contents from the list by list.getLength()
			logger.info("Media Count Value ::::" + doc.getElementsByTagName("media").getLength());
			
		} catch (ParserConfigurationException pce) {
			logger.error("CountXMLContentFromResponse::ParserConfigurationException getMediaCount " + pce.getMessage());
		} catch (IOException ioe) {
			logger.error("CountXMLContentFromResponse::IOException in getMediaCount" + ioe.getMessage());		
		} catch (SAXException sae) {
			logger.error("CountXMLContentFromResponse::SAXException in getMediaCount " + sae.getMessage());		
		} catch (Exception e) {
			logger.error("Exception::in getMediaCount " + e.getMessage());		
		}
    }
    
    public String getCountUsingCommand(String commandName) {
    	try{
    		ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", commandName);
    		Process process =  builder.start();
    		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return br.readLine();
            
    	} catch(Exception e){
    		logger.error("Exception in executing the command" + e.getMessage());
    	}
    	return "0";
    }
    
    public String getCountByFileType(String mediaPath, String fileType) {
    	
    	int count=0;
    	try{
    		String tomcat_path= System.getProperty("catalina.base");			
        	File mediaFolder = new File(tomcat_path + mediaPath);
        	logger.info("File Count::: in the path " + mediaPath + " For the file Type ::" + fileType);
          	count = checkFileUsingFileType(mediaFolder, fileType, count);
        	logger.info("File Count::: in the path " + mediaFolder + " For the file Type ::" + count);
    	} catch(Exception e) {
    		logger.error("Exception in reading the Media File Count "+e.getMessage());
    	}
    	
    	return Integer.toString(count);
    }
    
    
    public int checkFileUsingFileType(File mediaFile,String fileType,int count) {
    	
    	for (File file : mediaFile.listFiles()) {
			if(file.isDirectory()) {
				 count = + checkFileUsingFileType(file,fileType,count);
			}
			if (file.isFile()) {
				if(file.getName()!= null && file.getName().contains(fileType)){
					count++;	
				}				
			}
		}
    	
    	return count;
    }

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	/**
     * @return the mediaPath
     */
    public String getMediaPath() {
        return mediaPath;
    }

    /**
     * @param mediaPath the mediaPath to set
     */
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
   


    /**
     * @return the mediaFileType
     */
    public String getMediaFileType() {
        return mediaFileType;
    }

    /**
     * @param mediaFileType the mediaFileType to set
     */
    public void setMediaFileType(String mediaFileType) {
        this.mediaFileType = mediaFileType;
    }

    /**
     * @return the trailerFileType
     */
    public String getTrailerFileType() {
        return trailerFileType;
    }

    /**
     * @param trailerFileType the trailerFileType to set
     */
    public void setTrailerFileType(String trailerFileType) {
        this.trailerFileType = trailerFileType;
    }

    /**
     * @return the boxArtSmallFileType
     */
    public String getBoxArtSmallFileType() {
        return boxArtSmallFileType;
    }

    /**
     * @param boxArtSmallFileType the boxArtSmallFileType to set
     */
    public void setBoxArtSmallFileType(String boxArtSmallFileType) {
        this.boxArtSmallFileType = boxArtSmallFileType;
    }

    /**
     * @return the boxArtMediumFileType
     */
    public String getBoxArtMediumFileType() {
        return boxArtMediumFileType;
    }

    /**
     * @param boxArtMediumFileType the boxArtMediumFileType to set
     */
    public void setBoxArtMediumFileType(String boxArtMediumFileType) {
        this.boxArtMediumFileType = boxArtMediumFileType;
    }

    /**
     * @return the boxArtLargeFileType
     */
    public String getBoxArtLargeFileType() {
        return boxArtLargeFileType;
    }

    /**
     * @param boxArtLargeFileType the boxArtLargeFileType to set
     */
    public void setBoxArtLargeFileType(String boxArtLargeFileType) {
        this.boxArtLargeFileType = boxArtLargeFileType;
    }

    /**
     * @return the boxArtExtraLargeFileType
     */
    public String getBoxArtExtraLargeFileType() {
        return boxArtExtraLargeFileType;
    }

    /**
     * @param boxArtExtraLargeFileType the boxArtExtraLargeFileType to set
     */
    public void setBoxArtExtraLargeFileType(String boxArtExtraLargeFileType) {
        this.boxArtExtraLargeFileType = boxArtExtraLargeFileType;
    }

	public String getVideoPluginPath() {
		return videoPluginPath;
	}

	public void setVideoPluginPath(String videoPluginPath) {
		this.videoPluginPath = videoPluginPath;
	}



	
}
