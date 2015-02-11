/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms,
 * without consent are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */


package com.aircell.abp.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
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
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.model.Version;
import com.aircell.abp.model.utils.AircellUtils;
import com.aircell.abp.utils.AircellServletUtils;

/**.
   The <code>ABPDefaultController</code> is the default controller for the ABP.
 * It is called by ACPU system if the the ACPU determines that a unauthenticated
 * user is trying access internet. Usually this will happen the first time a
 * user opens a browser on the plane. When invoked, this controller will retrive
 * the user's IP address and flight information. It will then call the GBP,
 * passing it this information. The GBP will create a new session and store the
 * information within it. When done, the GBP will return the session id of the
 * newly created session. This session id will be used to identify all sessions
 * (ABP, GBP and Jahia).
 *  @author Muthuselvi
 *
 */
public class ABPDefaultController extends AbstractController {
    /**
     * String
     */
    private String abpDefaultPg;
    /**.
     * String
     */
    private String ampDefaultPg;
    /**.
     * String
     */
    public static final String CTX_KEY_SPLASH_IP_COLLECTION =
        "splashIPCollection";
    /**.
     * String
     */
    public String paramName = null;
    /**.
     * String
     */
    public String xWapProfile = null;
    /**.
     * String
     */
    public String profile = null;
    /**.
     * String
     */
    public String opt = null;
    /**.
     * String
     */
    private String atgDownPg;
    /**.
     * String
     */
    private String kuDownPg;
    /**.
     * String
     */
    private String noAirlineCodePg;
    /**.
     * Logger object
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Should be set via Spring configuration files
    /**.
     * String
     */
    private String splashPage;
    //private String goBrowsePage;
    /**.
     * String
     */
    private String purchasePage;
    // Added to indicate the updation of device XML from GBP.
    //      private static boolean abpDeviceXMLUpdated = false;
    /**.
     * String
     */
    private String deviceMethodName;
    /**.
     * Integer
     */
   private  int cookieMaxAge = -1;
    /**.
     * Boolean
     */
  private boolean cookieSecure = false;
    /**.
     * Flight
     */
  private  Flight flight = null;
    //Added for getting and setting the
    // abpversion and acpuversion from the flight
    /**.
     * Version
     */
  private Version version = null;
    /**.
     * String
     */
  private String httpClientGbpPage = null;
    /**.
     * String
     */
  private String httpPath = null;
    /**.
     * String
     */
  private String httpPostCookieName = null;
    /**.
     * String
     */
  private  String httpPostDomain = null;
    /**.
     * String
     */
  private String methodName = null;
    /**.
     * String
     */
    private String staticWebPath;

    /**.
     * String
     */
    private  String cookieErrorPage = null;
    /**.
     * String
     */
    
    private String videoServiceMethodName;
    
    public static final String COOKIE_TEST_NAME = "testCookieName";
    /**.
     * String
     */
    public static final String COOKIE_TEST_VALUE = "testCookieName";
    
    public static boolean updateVideoServiceAvailability = false; 

    /**.
     * Method handleRequestInternal
     * @return modelandview or null
     */
    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest req,
            HttpServletResponse res) throws Exception {

        logger.debug(AircellServletUtils.getIpAddress(req) + " - ABPDefaultController.handleRequestInternal:: Enters..");
        boolean flag=false;
		String ipAddress = AircellServletUtils.getIpAddress(req);
        String flagValue=req.getParameter("splash");

        String airlinecode = null;
    	if(flight != null && flight.getAirlineCode() != null){
    		airlinecode = flight.getAirlineCode();
    		req.setAttribute(AircellUtils.AIRLINE_CODE, airlinecode);
    	}
        if (null != flagValue && flagValue.trim() != ""){
            flag=true;
        }
        if (flag) {
            ModelAndView mv = handleAbpDefault(req, res);
            return mv;


        } else {
            // String redirectUrl = this.getAbpDefaultPg();
            String redirectUrl = null;
            if (AircellServletUtils.isMobileDevice(req)) {
                redirectUrl = this.getAmpDefaultPg();
            } else {
                redirectUrl = this.getAbpDefaultPg();
            }

            if (BackChannelUtils.ACPU_QUERY_STRING_VALUE.equalsIgnoreCase(req
                .getParameter(BackChannelUtils.ACPU_QUERY_STRING))) {
                redirectUrl =
                    redirectUrl + "?" + BackChannelUtils.ABP_FLAG + "="
                            + BackChannelUtils.ACPU_REDIRECT_FLAG_TRUE;
                if(airlinecode != null){
                	redirectUrl =
                            redirectUrl+ "&" + AircellUtils.AIRLINE_CODE + "=" 
                            		+airlinecode;
                }
                req.setAttribute(BackChannelUtils.ABP_FLAG,
                    BackChannelUtils.ACPU_REDIRECT_FLAG_TRUE);
            } else{
            	redirectUrl =
                        redirectUrl + "?" + AircellUtils.AIRLINE_CODE + "="
                                + airlinecode;
            }

            logger.debug(AircellServletUtils.getIpAddress(req)
                + " - Redirecting from ABPDefaultController to "
                    + redirectUrl);

            res.sendRedirect(redirectUrl);
            logger
                .debug(AircellServletUtils.getIpAddress(req) + "ABPDefaultController.handleRequestInternal::Exits....");
            return null;

        }
    }
    /**.
     * Method handleabpDefault1
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return modelview null
     * @throws Exception general Exception
     */
    public ModelAndView handleAbpDefault(HttpServletRequest req,
            HttpServletResponse res) throws Exception {
        logger.debug(AircellServletUtils.getIpAddress(req) + "inside handleAbpDefault enters------->");
        String updatedGbpDeviceXml;

        // User IP address on the plane
        final String ipAddress = AircellServletUtils.getIpAddress(req);
        String macAddress = "";

        // Retrieve flight information
        Flight flight = getFlight();
        logger.debug("flight.isServiceAvailable() :::" + flight.isServiceAvailable());
        if (!flight.isServiceAvailable()) {

            ModelAndView mv = getServiceDownView(req);

            return mv;
        }

        FlightInformation flightInfo = getFlight().getFlightInformation();

        // GJ - check for airline code, display service not available page
        // if flight object has no airline code.
        if (flightInfo == null || flightInfo.getAirlineCode() == null
                || "".equals(flightInfo.getAirlineCode())) {

            logger.error(ipAddress + " - Either flight info is null or airline code is null, Flight Info : " + flightInfo);
            return getServiceDownView(req);
        }

        /*
         * set the no of active subscribers to flight information,
         *  so that it can be passed to gbp along
         * with the flight information and can be used later  in
         * payment info page to check active subscribers
         * against the new subscripts cap.
         */

        flightInfo.setNoOfActiveSubscribers(Integer.toString(flight
            .getNoOfActiveSubscribers()));
        if (getFlight().isVideoServiceAvailable()) {
            flightInfo.setVideoServiceAvailability("true");
            flightInfo.setMediaCount(getFlight().getMediaCount());
            flightInfo.setMediaTrailerCount(getFlight().getMediaTrailerFileCount());
        } else {
            flightInfo.setVideoServiceAvailability("false");
            flightInfo.setMediaCount("0");
            flightInfo.setMediaTrailerCount("0");
        }
        
        // Session id assinged to GBP session (will use it to
        // identify all sessions...ABP, GBP, Jahia)
        String assignedSession = null;
        AirPassenger passenger = null;

        try {
            logger.debug("Before checking for the cookie");
            // added by magesh for the checking if the cookie already exist
            // Get cookie array from HttpServletRequest
            String cookieValue =
                this.getCookie(this.getHttpPostCookieName(), req);
            logger.debug(ipAddress + " - Cookie value " + cookieValue);

            //**.*************** end of checking

            // Check is there any active ATG session with user machine
            // IP, forward to gobrowse page with
            // no captcha.

            passenger = new AirPassenger();
            //added code
            Version version = getVersion();
            flightInfo.setAbpVersionNo(version.getAbpVersionNo());
            flightInfo.setAcpuVersionNo(getFlight().getFlightSystem()
                .getAcpuApplicationVersion());
            //Added for Whitelist version
            flightInfo.setAbsWhitelistVersionNo(
					getFlight().getFlightSystem().getAbsWhitelistVersion());
            logger.debug(" ABP version : " + flightInfo.getAbpVersionNo()
                + ", and ACPU version : " + flightInfo.getAcpuVersionNo()
				+ ", and Whitelist version : "
				+ flightInfo.getAbsWhitelistVersionNo());

            try {
                UserFlightSession session = passenger.getSession(ipAddress);
                macAddress = session.getUserMac();
                logger.info("UserName:::::" + session.getUserName() +"  ::: MAC Address:::"
                		+ macAddress);
                if (session.isActivated()) {
                    logger.debug("ABPDefaultController.handleAbpDefault: "
                            + "Found an existing active session for "
                            + "IP : {}  : username {}", ipAddress, session
                        .getUserName());
                    if (session.getUserName() != null) {
                        LoginDetails loginDetails =
                            new LoginDetails(session.getUserName(), null);
                        loginDetails.setIpAddress(ipAddress);
                        passenger.setLoginDetails(loginDetails);
                        passenger.setCaptchaPassed(true);
                        passenger.setMacAddress(macAddress);
                    } else {
                        passenger = null;
                    }
                } else {
                    passenger = null;
                }

            } catch (Exception e) {
                logger.error("ABPDefaultController.handleAbpDefault: "
                        + "Exception occured while checking for "
                        + "active session for IP {} : " + ipAddress, e);
            }

            String flStr = BackChannelUtils.encodeFlightToXML(flightInfo);
            logger.info(ipAddress
            		+ " - ABPDefaultController.handleAbpDefault: "
            		+ "Flight Information received from ACPU : "
            		+ flStr);

            // modified the below method to send the sessionid
            // available in cookie to gbp -magesh
            // this method would return the same sessionid
            // if that already exist in gbp. -magesh

            // Open a back channel to send flight and IP info to GBP system
            AbpBackChannelClient client =
                new AbpBackChannelClient(getHttpClientGbpPage());
            
            if (passenger != null && passenger.isCaptchaPassed()) {                
                
                String tmpUnameStr = ""; 
                if(passenger.getLoginDetails()!= null){
                    tmpUnameStr = passenger.getLoginDetails().getUsername();
                }
                
                logger.info(ipAddress
                		+ " - "
                		+ passenger.getMacAddress()
                		+ " - "
                		+ " passenger object is not null,"
                        +" get passenger's session for passenger name : "
                        + tmpUnameStr);
                assignedSession =
                    client.getNewGBPSessionId(macAddress, passenger, flStr, this
                        .getParamName(), cookieValue, this.getMethodName());
            } else {
                logger.debug(ipAddress + " - passenger object is null, create new session.");
                assignedSession =
                    client.getNewGBPSessionId(macAddress, ipAddress, flStr, this
                        .getParamName(), cookieValue, this.getMethodName());
            }

            if(hasXmlTags(assignedSession)) {
				assignedSession = null;
			}

            
            if(!updateVideoServiceAvailability) {	
            	client.updateGBPVideoServiceFlag(this.getVideoServiceMethodName(), this.getParamName(), flStr,assignedSession);
            	updateVideoServiceAvailability = true;
            
            }
            if (!AircellServletUtils.abpDeviceXMLUpdated) {
                // this method would return the device.xml
                // as String from gbp. -Arvind
                updatedGbpDeviceXml =
                    client.getUpdatedDeviceData(this.getParamName(),
                        cookieValue, this.getDeviceMethodName());
                if (updatedGbpDeviceXml!=""
                        && updatedGbpDeviceXml != null) {
                    try {
                        // this method would update abp(Air) Devices
                        // map with the Device.xml Data from the
                        // gbp(Ground) -Arvind
                        AircellServletUtils
                            .getSupportedMobileDevices(updatedGbpDeviceXml);
                    } catch (Exception e) {
                        logger.error(ipAddress
                            + " - Error while updating Device XML from GBP : "
                            + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            logger.error(ipAddress
                + " - Exception occured while processing handleAbpDefault : "
                + e.getMessage());

             return getServiceDownView(req);
        }

        if (!isCookiesEnabled(req)) {
            logger.error("Cookie NOT enabled. redirect to CookieErrorPage");
            res.sendRedirect(this.getCookieErrorPage());
            return null;
        }

        if (assignedSession != null && assignedSession.length() > 0) {

            logger.info(ipAddress
                + " - Session id returned from GBP is {} ", assignedSession);
            StringBuffer splashPageUrl = new StringBuffer(splashPage);
            logger.debug("ABPDefaultController.handleAbpDefault: "
                   + "Initial page :" + splashPageUrl);
            if (BackChannelUtils.ACPU_REDIRECT_FLAG_TRUE.equalsIgnoreCase(req
                .getParameter(BackChannelUtils.ABP_FLAG))) {
                splashPageUrl.append("?").append(BackChannelUtils.ABP_FLAG)
                    .append("=").append(
                        BackChannelUtils.ACPU_REDIRECT_FLAG_TRUE);
            } else {
                splashPageUrl.append("?").append(BackChannelUtils.ABP_FLAG)
                    .append("=").append(BackChannelUtils.ABP_FLAG_TRUE);
            }

            try {
                // Create non--persistent cookie with assigned session id
                // and then redirect user to GBP splash page
                Cookie ck =
                    new Cookie(this.getHttpPostCookieName(), assignedSession);
                ck.setMaxAge(this.getCookieMaxAge());
                //ck.setDomain(this.getHttpPostDomain());
                ck.setPath(this.getHttpPath());
                res.addCookie(ck);

                if (passenger != null && passenger.isCaptchaPassed()) {
                    logger.debug("ABPDefaultController.handleAbpDefault: "
                            + "Found a active session for IP {}, "
                            + "forwarding to Go Browse Page", ipAddress);
                    //splashPageUrl=new StringBuffer(splashPage);
                    splashPageUrl.append("?").append(
                        BackChannelUtils.GBP_CAPTCH_PASSED).append("=").append(
                        BackChannelUtils.ABP_REDIRECT_TRUE);

                    logger.info(ipAddress + " - Found an active session, "
                        + "forwarding to URL : " + splashPageUrl.toString());
                    res.sendRedirect(splashPageUrl.toString());
                } else {
                    res.sendRedirect(splashPageUrl.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return getServiceDownView(req);
            }
        } else {
            logger.error(ipAddress + " - retrieved session id"
            + " from GBP is null or empty");

            return getServiceDownView(req);
        }

        return null;
    }

    /**.
     * Setter method
     * @return cookieSecure
     */
    public boolean isCookieSecure() {
        return this.cookieSecure;
    }
    /**.
     * Setter method
     * @param atgDownPg atgDownPg
     */
    public void setAtgDownPg(final String atgDownPg) {
        this.atgDownPg = atgDownPg;
    }

    /*
    public void setAmpAtgDownPg(final String ampAtgDownPg) {
          this.ampAtgDownPg = ampAtgDownPg;
    }
     */
    /**.
     * Setter method
     * @param age Integer
     */
    public void setCookieMaxAge(int age) {
        this.cookieMaxAge = age;
    }
    /**.
     * Setter method
     * @param cookieSecurity boolean
     */
    public void setCookieSecure(boolean cookieSecurity) {
        this.cookieSecure = cookieSecurity;
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
     * @param st String
     */
    public void setHttpClientGbpPage(String st) {
        this.httpClientGbpPage = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setHttpPath(String st) {
        this.httpPath = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setHttpPostCookieName(String st) {
        this.httpPostCookieName = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setHttpPostDomain(String st) {
        this.httpPostDomain = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setMethodName(String st) {
        this.methodName = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setParamName(String st) {
        this.paramName = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setSplashPg(final String st) {
        this.splashPage = st;
    }
    /**.
     * Setter method
     * @param st String
     */
    public void setPurchasePg(final String st) {
        this.purchasePage = st;
    }

    /* public void setGoBrowsePg(final String st) {
          this.goBrowsePage = st;
      }*/
    /**.
     * Getter Method
     * @return staticWebPath
     */
    public String getStaticWebPath() {
        return staticWebPath;
    }
    /**.
     * Setter method
     * @param staticWebPath String
     */
    public void setStaticWebPath(String staticWebPath) {
        this.staticWebPath = staticWebPath;
    }
    /**.
     * Getter method
     * @return noAirlineCodePg String
     */
    public String getNoAirlineCodePg() {
        return noAirlineCodePg;
    }
    /**.
     * Setter method
     * @param noAirlineCodePg String
     */
    public void setNoAirlineCodePg(final String noAirlineCodePg) {
        this.noAirlineCodePg = noAirlineCodePg;
    }
    /**.
     * Getter method
     * @return deviceMethodName
     */
    public String getDeviceMethodName() {
        return deviceMethodName;
    }
    /**.
     * Setter method
     * @param deviceMethodName String
     */
    public void setDeviceMethodName(String deviceMethodName) {
        this.deviceMethodName = deviceMethodName;
    }

    /**.
     * method to check and return the value of the specified cookie.
     *
     * @param name String
     * @param request HttpServletRequest
     * @return null if not found or else the value
     * @author mageshkarnam
     */
    public String getCookie(String name, HttpServletRequest request) {
        String value = null;
        Cookie[] c = request.getCookies();
        if (c != null && name != null) {
            for (int i = 0; i < c.length; i++) {
                if (c[i].getName().equals(name)) {
                    value = c[i].getValue();
                    break;
                }
            }
        }
        return value;
    }
    /**.
     * Getter method
     * @return kuDownPg
     */
    public String getKuDownPg() {
        return kuDownPg;
    }
    /**.
     * Getter method
     * @param kuDownPg String
     */
    public void setKuDownPg(String kuDownPg) {
        this.kuDownPg = kuDownPg;
    }
    /**.
     * Getter method
     * @return cookieErrorPage
     */
    public String getCookieErrorPage() {
        return cookieErrorPage;
    }
    /**.
     * Setter method
     * @param cookieErrorPage
     */
    public void setCookieErrorPage(String cookieErrorPage) {
        this.cookieErrorPage = cookieErrorPage;
    }
    /**.
     * Getter method
     * @return atgDownPg
     */
    public String getAtgDownPg() {
        return this.atgDownPg;
    }

    /*
     public String getAmpAtgDownPg() {
     return this.ampAtgDownPg;
     }
     */
    /**.
     * Getter method
     * @return cookieMaxAge
     */
    public int getCookieMaxAge() {
        return this.cookieMaxAge;
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
    /**.
     * Getter method
     * @return httpClientGbpPage
     */
    public String getHttpClientGbpPage() {
        return httpClientGbpPage;
    }
    /**.
     * Getter method
     * @return httpPath
     */
    public String getHttpPath() {
        return httpPath;
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
     * @return httpPostDomain
     */
    public String getHttpPostDomain() {
        return httpPostDomain;
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
     * @return splashPage
     */
    public String getSplashPg() {
        return splashPage;
    }

    /* public String getGoBrowsePg() {
         return goBrowsePage;
     }*/
    /**.
     * Getter method
     * @return purchasePage
     */
    public String getPurchasePg() {
        return purchasePage;
    }
    /**.
     * Getter method
     * @return ampDefaultPg
     */
    public String getAmpDefaultPg() {
        return ampDefaultPg;
    }
    /**.
     * Setter method
     * @param ampDefaultPg
     */
    public void setAmpDefaultPg(String ampDefaultPg) {
        this.ampDefaultPg = ampDefaultPg;
    }
     /**.
      * Getter method
      * @return abpDefaultPg
      */
    public String getAbpDefaultPg() {
        return abpDefaultPg;
    }
    /**.
     * Setter method
     * @param abpDefaultPg
     */
    public void setAbpDefaultPg(String abpDefaultPg) {
        this.abpDefaultPg = abpDefaultPg;
    }

    private boolean isCookiesEnabled(HttpServletRequest req) {
		boolean cookieEnabled = false;

		//    Iterate through cookies to find if test cookie exists.
		if (null != req.getCookies()) {

			for (int i = 0; i < req.getCookies().length; i++) {
				Cookie cookie = (Cookie) req.getCookies()[i];
				if (cookie.getName().equals(
					ABPDefaultController.COOKIE_TEST_NAME)
						&& cookie.getValue().equals(
							ABPDefaultController.COOKIE_TEST_VALUE)) {
					cookieEnabled = true;
					break;
				}
			}
        }

        return cookieEnabled;
	}

    private boolean hasXmlTags (String sessionId) {
		boolean hasXmlTags = false;

		if(sessionId != null && (sessionId.contains("<") || sessionId.contains(">"))) {
			hasXmlTags = true;
		}

		return hasXmlTags;
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
		model.put("staticWebPath", this.getStaticWebPath());
		
		String flightType = AircellUtils.FLIGHT_TYPE_ATG;
		if(flight != null){
        	if(flight.getAirlineCode() != null){
                req.setAttribute(AircellUtils.AIRLINE_CODE, flight.getAirlineCode());
        	}
        	   // Setting flight type in request
            AircellUtils aUtils = new AircellUtils();
            if(getFlight() != null && getFlight().getFlightSystem() != null && getFlight().getFlightSystem().getAcpuApplicationVersion() != null) {
                flightType = aUtils.getFlightType(getFlight().getFlightSystem().getAcpuApplicationVersion());                   
            }else{
                logger.warn("ACPU Application Version is null");
            }
        }
		req.setAttribute(AircellUtils.FLIGHT_TYPE_STR, flightType);
		
		String serviceDownPg = null;
		if (flightType.equalsIgnoreCase(AircellUtils.FLIGHT_TYPE_KU)) {
			serviceDownPg = this.getKuDownPg();
		} else {
			serviceDownPg = this.getAtgDownPg();
		}
        ModelAndView mv = new ModelAndView(serviceDownPg, model);
               
        return mv;
    }
    
	public String getVideoServiceMethodName() {
		return videoServiceMethodName;
	}
	public void setVideoServiceMethodName(String videoServiceMethodName) {
		this.videoServiceMethodName = videoServiceMethodName;
	}

}
