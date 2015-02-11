/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent are
 * strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */


package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aircell.abp.backchannel.AbpBackChannelClient;
import com.aircell.abp.backchannel.BackChannelUtils;
import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.model.Version;
import com.aircell.abp.utils.AircellServletUtils;

/**.
 * ABPRoamingController class
 * @author Muthuselvi
 *
 */
public class ABPRoamingController extends AbstractController {
    /**.
     * Logger object
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**.
     * Flight object
     */
    private Flight flight = null;
    /**.
     * String variable
     */
    private String httpClientGbpPage = null;
    /**.
     * String variable
     */
    private String httpPostCookieName = null;
    /**.
     * String variable
     */
    private String methodName = null;
    /**.
     * String variable
     */
    private String paramName = null;
    /**.
     * String variable
     */
    private String roamingView = null;
    /**.
     * String variable
     */
    private String atgDownPg;
	/**.
	 * Version
	 */
  	private Version version = null;

    /**.
     * handleRequestInternal method
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @throws Exception General Exception
     * @return mv ModelAndView
     */
    public ModelAndView handleRequestInternal(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
		logger.debug("ABPRoamingController.handleRequestInternal.. Enter");

        // User IP address on the plane
        ModelAndView mv = null;
        final String ipAddress = AircellServletUtils.getIpAddress(req);
        flight = getFlight();
		if (!flight.isServiceAvailable()) {
			return getServiceDownView(req);
		}

        FlightInformation flightInfo = flight.getFlightInformation();

		if (flightInfo == null || flightInfo.getAirlineCode() == null
				|| "".equals(flightInfo.getAirlineCode())) {

			logger.error(ipAddress + " - Either flight info is null or airline code is null, Flight Info : " + flightInfo);
			return getServiceDownView(req);
        }



        flightInfo.setNoOfActiveSubscribers(Integer.toString(flight
            .getNoOfActiveSubscribers()));
        String macAddress = "";

        if (getFlight().isVideoServiceAvailable()) {
            flightInfo.setVideoServiceAvailability("true");
            flightInfo.setMediaCount(getFlight().getMediaCount());
            flightInfo.setMediaTrailerCount(getFlight().getMediaTrailerFileCount());
        } else {
            flightInfo.setVideoServiceAvailability("false");
            flightInfo.setMediaCount("0");
            flightInfo.setMediaTrailerCount("0");
        }

		Version version = getVersion();
		flightInfo.setAbpVersionNo(version.getAbpVersionNo());
		flightInfo.setAcpuVersionNo(getFlight().getFlightSystem()
			.getAcpuApplicationVersion());
		//Added for Whitelist version
        flightInfo.setAbsWhitelistVersionNo(getFlight()
						.getFlightSystem().getAbsWhitelistVersion());

        try {
            AirPassenger passenger = new AirPassenger();
            UserFlightSession session = passenger.getSession(ipAddress);
            macAddress = session.getUserMac();
        } catch (Exception e) {
            logger.error("ABPRoamingController.handleREquestInternal:"
                + " Exception occured while "
                + "calling passenger.getSession "
                + "to retrieve Mac Address");
        }

        // Session id assinged to GBP session (will use it to identify all
        // sessions...ABP, GBP)
        String assignedSession = null;

        String flStr = BackChannelUtils.encodeFlightToXML(flightInfo);
        logger.info(ipAddress
        		+ " - ABPRoamingController.handleREquestInternal "
        		+ "Flight Information received from ACPU : " + flStr);


        // Open a back channel to send flight and IP info to GBP system

        try {
            AbpBackChannelClient client =
                new AbpBackChannelClient(getHttpClientGbpPage());
            String cookieValue = null;
            assignedSession =
                client.getNewGBPSessionId(macAddress,ipAddress, flStr,
                    this.getParamName(), cookieValue, this.getMethodName());

			if(hasXmlTags(assignedSession)) {
				assignedSession = null;
			}
        } catch (Exception e) {
            logger.error(
                    "ABPRoamingController.handleREquestInternal - IP {}"
                    +" Exception occured while retrieving GBP Session id {}:  ",
                    ipAddress, e);
			return getServiceDownView(req);
        }

        if (assignedSession != null && assignedSession.length() > 0) {
            logger
                .info(
                    "ABPRoamingController.handleREquestInternal - IP {"
                    +"}, GBP Session Id {}  ",
                    ipAddress, assignedSession);
        } else {
            logger.info("ABPRoamingController.handleREquestInternal - IP {} session id returned from GBP is null",
                ipAddress);
        }

        mv = new ModelAndView(this.getRoamingView());
        if (flight != null) {
            mv.addObject("flight", flight.getFlightInformation());
        }
        if (assignedSession != null) {
            mv.addObject(AircellServletUtils.SESSION_ID_NAME, assignedSession);
        }

        return mv;

    }


	 /**
     * . is a method that return service down page ModelAndView.
     * @param req HttpServletRequest
     * @return ModelAndView
     */
    private ModelAndView getServiceDownView(HttpServletRequest req) {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("atgLinkStatus", this.getFlight().getFlightSystem()
			.getAtgLinkStatus().toString());
		model.put("absServiceStatus", this.getFlight().getFlightSystem()
			.getAbsServiceStatus().toString());

        ModelAndView mv = new ModelAndView(this.getAtgDownPg(), model);

        return mv;
    }

    private boolean hasXmlTags (String sessionId) {
		boolean hasXmlTags = false;

		if(sessionId != null && (sessionId.contains("<") || sessionId.contains(">"))) {
			hasXmlTags = true;
		}

		return hasXmlTags;
	}

    /**.
     * Getter method
     * @return flight
     */
    public Flight getFlight() {
        return this.flight;
    }
    /**.
     * Getter method
     * @return httpClientGbpPage
     */
    public String getHttpClientGbpPage() {
        return httpClientGbpPage;
    }
    /**.
     * Getter method
     * @return httpPostCookieName
     */
    public String getHttpPostCookieName() {
        return httpPostCookieName;
    }
    /**.
     * Getter method
     * @return methodName
     */
    public String getMethodName() {
        return methodName;
    }
    /**.
     * Getter method
     * @return paramName
     */
    public String getParamName() {
        return paramName;
    }
    /**.
     * Getter method
     * @return roamingView
     */
    public String getRoamingView() {
        return roamingView;
    }
    /**.
     * Getter method
     * @return atgDownPg
     */
    public String getAtgDownPg() {
        return this.atgDownPg;
    }
    /**.
     * Setter method
     * @param flight Flight bean
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    /**.
     * Setter method
     * @param httpClientGbpPage String
     */
    public void setHttpClientGbpPage(String httpClientGbpPage) {
        this.httpClientGbpPage = httpClientGbpPage;
    }
    /**.
     * Setter method
     * @param httpPostCookieName String
     */
    public void setHttpPostCookieName(String httpPostCookieName) {
        this.httpPostCookieName = httpPostCookieName;
    }
    /**.
     * Setter method
     * @param methodName String
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    /**.
     * Setter methood
     * @param paramName String
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    /**.
     * Setter method
     * @param roamingView String
     */
    public void setRoamingView(String roamingView) {
        this.roamingView = roamingView;
    }
    /**.
     * Setter method
     * @param atgDownPg atgDownPg
     */
    public void setAtgDownPg(final String atgDownPg) {
        this.atgDownPg = atgDownPg;
    }
    /**.
     * Getter method
     * @return version
     */
    public Version getVersion() {
        return this.version;
    }
    /**.
     * Getter method
     * @param version
     */
    public void setVersion(Version version) {
        this.version = version;
    }
}
