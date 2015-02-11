/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aircell.abp.backchannel.BackChannelUtils;
import com.aircell.abp.model.AircellUser;
import com.aircell.abp.model.ApplicationState;
import com.aircell.abp.model.CreditCardDetails;
import com.aircell.abp.model.Device;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.Order;
import com.aircell.abp.model.OrderLineItem;
import com.aircell.abp.model.SubscriptionProduct;
import com.aircell.abp.model.UserCredentials;
import com.aircell.abp.web.servlet.session.AircellSessionManager;

/**.
 * Utility methods abstracted out from the two Aircell specific Controller
 * classes
 * @author AKQA - bryan.swift
 * @version $Revision: 3552 $
 */
@Configurable
public class AircellServletUtils {
    /**. Constant which is used to get the alternate views fo
     * r success pages * */
    public static final String SUCCESS = "Success";
    /**. Constant which is used to get the alternate views for error pages * */
    public static final String ERROR = "Error";
    /**. Constant which is used to get the alternate views for error pages * */
    public static final String FAILURE = "Failure";
    /**. Constant name of the terms and conditions checkbox * */
    public static final String TERMS_PARAM = "terms";
    /**. Constant name of the submit parameter * */
    public static final String SUBMIT_PARAMETER = "submit";
    /**. Constant name of the value for the cancel button * */
    public static final String CANCEL = "cancel";
    /**. Name of the session id variable to put in the model */
    public static final String SESSION_ID_NAME = "jsessionid";
    /**.
     * Name of the property to add to the model for velocity to know it was a
     * failure
     */
    public static final String IS_FAILURE = "isFailure";
    /**. Request attribute set if it's a mobile device */
    public static final String IS_MOBILE = "isMobileDevice";
    /**. Request attribute set if it's a supported mobile device */
    public static final String IS_SUPPORTED_MOBILE = "isSupportedDevice";
    /**. Request attribute set if it's a supported browser */
    public static final String IS_SUPPORTED_BROWSER = "isSupportedBrowser";
    /**. Session attribute key for the logged in user */
    public static final String LOGGED_IN_USER = "loggedInUser";
    /**. Key for pojo which holds session attributes */
    public static final String SESSION_MANAGER = "sessionManager";
    /**. Request header to look for to test if the request is an ajax one */
    private static final String AJAX_HEADER = "ajax";
    
    private static final String XML_PARAM = "xml";
    
    private static final String JSONP_PARAM = "jsonp";
    
    private static final String AJAX_RESPONSE = "response";
    /**.
     * Request header to look for to test if the request is a validatation
     * request
     */
    private static final String VALIDATE_ONLY_HEADER = "validateOnly";
    /**. Key for supported devices list */
    public static final String SUPPORTED_DEVICES = "supportedDevices";
    /**. Key for mobile devices list */
    public static final String KNOWN_DEVICES = "knownDevices";
    /**. Key for attribute informing if reference data has run already */
    public static final String DO_REFERENCE_DATA_ONCE = "referenceDataDoOnce";

    public static final String SIMPLE_CAPCHA_SESSION_KEY =
    "simpleCaptchaSessionKey";


    /**. Flag used to determine Device Xml is updated from GBP */
    public static boolean abpDeviceXMLUpdated = false;


    /**. Beginning path to library files */
    private static String libraryPath;

    private static String gogolibraryPath;
    /**. Name of the request header to look fo the ip address in */
    private static String ipHeaderName;

    private static String queryString = null;

    private static final String LAPTOP = "laptop";

    private static final String MOBILE = "mobile";


    static {
        AircellServletUtils utils = new AircellServletUtils();
        libraryPath = utils.getLibraryPath();
        gogolibraryPath = utils.getGogolibraryPath();
        ipHeaderName = utils.getIpHeaderName();
    }

    /**.
     * Key for Activate Error to retrieve the information if the service
     * Portal Activation error codes
     *
     */
    /**. String variable to hold the error code for unspecified error from ACPU*/
    public static String ACPU_UNSPECIFIED_ERROR = "AC-100";
    /**. String variable to hold the error code for unspecified error*/
    public static String ABP_UNSPECIFIED_ERROR = "PR-100";
    /**. String variable to hold the error code for user infor from gbp null*/
    public static String ABP_USER_INFO_NULL = "PR-101";
    /**. String variable to hold the error code for not a passenger*/
    public static String ABP_NOT_A_PASSENGER = "PR-102";
    /**. String variable to hold the error code for CAPTCHA not Passed*/
    public static String ABP_CAPTCHA_NOT_PASSED = "PR-103";
    /**. String variable to hold the error code for no error code received from ABP*/
    public static String ABP_NO_ERROR_RECEIVED = "PR-104";

    /**.
     * Method to retrieve the session manager from the session. If a session
     * does not exist null is returned. If a session exists but a session
     * manager doesn't exist a new one is created and placed in the session.
     * @param request - HttpServletRequest - the submitted request
     * @return null if an HttpSession doesn't exist, otherwise the
     *         AircellSessionManager in the HttpSession
     */
    public static AircellSessionManager getSession(HttpServletRequest request) {
        final Logger logger = LoggerFactory.getLogger(AircellServletUtils.class)
        ;
        AircellSessionManager sessionManager = null;
        HttpSession session = request.getSession(false);

        if (session != null) {
            sessionManager =
            (AircellSessionManager) session.getAttribute(SESSION_MANAGER);
            if (sessionManager == null) {
                session.setAttribute(SESSION_MANAGER, createSession(request));
                sessionManager =
                (AircellSessionManager) session.getAttribute(SESSION_MANAGER);
            }
        }
        return sessionManager;
    }

    /**.
     * Utility method to create a session and the AircellSesssionManager
     * @param request - HttpServletRequest - the submitted request
     */
    public static AircellSessionManager createSession(
    HttpServletRequest request
    ) {
        AircellSessionManager sessionManager = null;
        HttpSession session = request.getSession();
        if (session != null) {
            sessionManager =
            (AircellSessionManager) session.getAttribute(SESSION_MANAGER);
            if (sessionManager == null) {
                session
                .setAttribute(SESSION_MANAGER, new AircellSessionManager());
                sessionManager =
                (AircellSessionManager) session.getAttribute(SESSION_MANAGER);
            }
            sessionManager.setJsessionid(session.getId());
        }
        return sessionManager;
    }

    /**.
     * Utility method to create a session and the AircellSesssionManager with
     * Flight Info and user credentials
     * @param request
     * @return
     */
    public static AircellSessionManager createSessionWithFlightInfo(
    HttpServletRequest request
    ) throws IllegalArgumentException {
        AircellSessionManager sessionManager = createSession(request);
        HttpSession httpSession = request.getSession();
        FlightInformation flight = (FlightInformation) httpSession
        .getAttribute(BackChannelUtils.ABP_FLIGHT_INFO);
        UserCredentials credentials =
        (UserCredentials) httpSession.getAttribute(UserCredentials.KEY);
        if (flight == null || credentials == null) {
            throw new IllegalArgumentException(
            "ERROR: Flight Info is missing. "
            + "Can not create a new Session with Flight Info. ");
        }
        sessionManager.setFlightInfo(flight);
        sessionManager.setCredentials(credentials);
        return sessionManager;
    }

    /**.
     * Creates an Order for the AircellUser in the AircellSessionManager which
     * is retrieved from the request. Adds all the OrderLineItem objects in the
     * AircellSessionManager to the new order. Sets an empty list of
     * OrderLineItem objects back onto the AircellSessionManager.
     * @param request from which to retrieve the AircellSessionManager
     * @return the Order created for the AircellUser in the
     *         AircellSessionManager
     */
    public static Order createOrder(HttpServletRequest request)
    throws IllegalArgumentException {
        AircellSessionManager session = getSession(request);
        List<OrderLineItem> orders = session.getOrders();
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException(
            "can not create order for no line items"
            );
        }
        AircellUser user = session.getUser();
        Order order = user.createNewOrder();
        for (OrderLineItem orderItem : orders) {
            order.addOrderLineItem(orderItem);
        }
        orders.clear();
        session.setOrders(orders);
        return order;
    }

    /**.
     * Helper method to examine a request to determine if it was submitted via
     * AJAX
     * @param request - HttpServletRequest - the submitted request
     * @return true if the request is found to have been made via AJAX
     */
    public static boolean isAjax(HttpServletRequest request) {
        String ajax = request.getHeader(AJAX_HEADER);
        boolean isAjax = false;
        if (ajax != null && ajax.equals("true")) {
            isAjax = true;
        }
        return isAjax;
    }

    /**.
     * Helper method to examine a request to determine if a cancel button was
     * pressed
     * @param request - HttpServletRequest - the submitted request
     * @return true if the cancel button has been pressed
     */
    public static boolean isCancel(HttpServletRequest request) {
        String cancel = request.getParameter(SUBMIT_PARAMETER);
        boolean isCancel = false;
        if (cancel != null && cancel.equals(CANCEL)) {
            isCancel = true;
        }
        return isCancel;
    }

    /**.
     * Helper method to examine a request to determine if if only validation
     * should be done
     * @param request - HttpServletRequest - the submitted request
     * @return true if the request says only validation should be performed
     */
    public static boolean isValidateOnly(HttpServletRequest request) {
        String validateOnly = request.getHeader(VALIDATE_ONLY_HEADER);
        boolean isValidateOnly = false;
        if (validateOnly != null && validateOnly.equals("true")) {
            isValidateOnly = true;
        }
        return isValidateOnly;
    }

    /**.
     * Helper method to examine a request to determine if it came from a mobile
     * device
     * @param request - HttpServletRequest - the submitted request
     * @return true if the request has an attribute by the designated name in
     *         it
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        Boolean mobileDevice = (Boolean) request.getAttribute(IS_MOBILE);
        boolean isMobileDevice = false;
        if (mobileDevice != null && mobileDevice) {
            isMobileDevice = true;
        } else if (mobileDevice == null) {
            String mobileDeviceString = request.getParameter(IS_MOBILE);
            if (mobileDeviceString != null && mobileDeviceString
            .equalsIgnoreCase(Boolean.TRUE.toString())) {
                isMobileDevice = true;
            }
        }
        return isMobileDevice;
    }
    
    public static boolean isXMLAjax(HttpServletRequest request) {
        String xmlParam = (String) request.getParameter(AJAX_RESPONSE);
        boolean isXmlResponse = false;
        if (xmlParam != null && xmlParam.equalsIgnoreCase(XML_PARAM)){
        	isXmlResponse=true;
        }
        return isXmlResponse;
    }
    
    public static boolean isJsonAjax(HttpServletRequest request) {
        String jsonpParam = (String) request.getParameter(AJAX_RESPONSE);
        boolean isjsonpResponse = false;
        if (jsonpParam != null && jsonpParam.equalsIgnoreCase(JSONP_PARAM)){
        	isjsonpResponse=true;
        }
        return isjsonpResponse;
    }


    public static String getDeviceCategory(HttpServletRequest request) {
        if (isMobileDevice(request)) {
            return MOBILE;
        } else {
            return LAPTOP;
        }
    }


    /**.
     * Helper method to examine a request to determine if it came from a
     * supported mobile device
     * @param request - HttpServletRequest - the submitted request
     * @return true if the request has an attribute by the designated name in
     *         it
     */
    public static boolean isSupportedMobileDevice(HttpServletRequest request) {
        Boolean supportedDevice =
        (Boolean) request.getAttribute(IS_SUPPORTED_MOBILE);
        boolean isSupportedDevice = false;
        if (supportedDevice != null && supportedDevice) {
            isSupportedDevice = true;
        }
        return isSupportedDevice;
    }


    /**.
     * Examines an HttpServletRequest in order to determine the ip address of
     * the client
     * @param request - HttpServletRequest - the submitted request
     * @return the ip address found in the request
     */
    public static String getIpAddress(HttpServletRequest request) {
        final Logger logger = LoggerFactory.getLogger(AircellServletUtils.class)
        ;
        //    logger.debug("{}",enumerationToString(request.getHeaderNames()));
        String ip = request.getRemoteAddr();
        //    logger.debug("getRemoteAddr returned: {}",ip);
        boolean ipUseHeader = StringUtils.isNotBlank(ipHeaderName);
        if (ipUseHeader) {
            String headerIp = request.getHeader(ipHeaderName);
            if (StringUtils.isNotBlank(headerIp) && !headerIp.equals("unknown")) {
                ip = headerIp;
            }
        }
        logger.debug("AircellServletUtils.getIpAddress:"
                + " getIpAddress returning : {}", ip);
        return ip;
    }
    
    

    public static String enumerationToString(Enumeration enumeration) {
        boolean first = true;
        StringBuilder enumBuilder = new StringBuilder();
        while (enumeration.hasMoreElements()) {
            Object element = enumeration.nextElement();
            if (!first) {
                enumBuilder.append(", ");
            } else {
                first = false;
            }
            enumBuilder.append(element.toString());
        }
        String enumString = enumBuilder.toString();
        return enumString;
    }

    /**.
     * Initialize the list of the supported devices
     * @param listLocation - String - fully qualified url to the location of the
     * device list
     * @return List<String> of browser user agents or substrings to match
     */
    public static Map<String, List<Device>> getSupportedMobileDevices(
    String listLocation
    ) {
        final Logger logger = LoggerFactory.getLogger(AircellServletUtils.class)
        ;
        Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
        List<Device> knownDevices = new ArrayList<Device>();
        List<Device> supportedMobileDevices = new ArrayList<Device>();
        Document document = null;
        ByteArrayInputStream byteStreamData;
        logger
        .debug("AircellServletUtils :: getSupportedMobileDevices .... Enters ");
        logger
        .debug("AircellServletUtils :: getSupportedMobileDevices, listLocation : "
            + listLocation);
        try {
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String[] listurl = listLocation.split("http");

            /* This Block executes only when the URL pattern contains http
               listLocation from Ground contains
               http:/localhost/static/devices.xml  */
            if (listurl.length > 1) {
                URL url = new URL(listLocation);
                logger.debug("AircellServletUtils.getSupportedMobileDevices: "
                       + "url: {}", url.toString());
                //logger.debug("openning stream for {}", listLocation);
                InputStream stream = url.openStream();
                document = builder.parse(stream);

            } else {
                /*  This Block executes only when the
                    listlocation String is equal devices.xml
                    listLocation from abp(AIR) contains devices.xml only */
                if (listLocation.equals("devices.xml")) {
                    ClassPathResource classPathResource =
                    new ClassPathResource(listLocation);
                    logger.debug(
                    "AircellServletUtils.getSupportedMobileDevices:"
                    + classPathResource.getPath() + " d " + classPathResource
                    .getFile()
                    );
                    document =
                    builder.parse(classPathResource.getInputStream());
                } else {

                    /*  This Block executes only when the listlocation
                        String is notequal to devices.xml or
                        http:/localhost/static/devices.xml. This block of code
                        handles a String that contains devices.xml
                        data from the gbp(Ground) */

                    byteStreamData =
                    new ByteArrayInputStream(listLocation.getBytes());
                    
                    document = builder.parse(byteStreamData);
                    abpDeviceXMLUpdated = true;
                    if (knownDevices.size() < 1 && devices.size() < 1
                    && supportedMobileDevices.size() < 1) {
                        knownDevices.clear();
                        devices.clear();
                        supportedMobileDevices.clear();
                        logger.debug("AircellServletUtils"
                        + ".getSupportedMobileDevices:"
                        + "Clearing the Object of knownDevices :: devices"
                        + " :: supportedMobileDevices  "
                        + knownDevices.size() + "::" + devices.size() + "::"
                        + supportedMobileDevices.size()
                        );
                    }

                }


            }


            NodeList deviceNodes = document.getElementsByTagName("device");
            for (int i = 0; i < deviceNodes.getLength(); i++) {
                Device device = new Device();
                Node deviceNode = deviceNodes.item(i);
                NamedNodeMap attributes = deviceNode.getAttributes();
                String deviceName = deviceNode.getTextContent();
                device.setName(deviceName);
                if (attributes != null) {
                    Node mobile = attributes.getNamedItem("mobile");
                    if (mobile != null) {
                        String sMobile = mobile.getTextContent();
                        if (StringUtils.isNotBlank(sMobile)) {
                            Boolean bMobile = new Boolean(sMobile);
                            device.setMobile(bMobile);
                        }
                    } else {
                        device.setMobile(true);
                    }
                    Node minVersion = attributes.getNamedItem("minVersion");
                    if (minVersion != null) {
                        String sMinVersion = minVersion.getTextContent();
                        if (StringUtils.isNotBlank(sMinVersion) && StringUtils
                        .isNumeric(sMinVersion)) {
                            Integer iMinVersion = new Integer(sMinVersion);
                            device.setMinVersion(iMinVersion);
                        }
                    }
                    Node supported = attributes.getNamedItem("supported");
                    if (supported != null) {
                        String sIsSupported = supported.getTextContent();
                        if (StringUtils.isNotBlank(sIsSupported)) {
                            device.setSupported(true);
                        }
                    }
                }
                knownDevices.add(device);
                if (device.isSupported()) {
                    supportedMobileDevices.add(device);
                }
            }
        } catch (MalformedURLException mue) {
            logger.error("AircellServletUtils"
            + ".getSupportedMobileDevices:"+ listLocation
            + " is not a properly formed url", mue);
        } catch (IOException ioe) {
            logger.error("AircellServletUtils.getSupportedMobileDevices: "
                    + "problem reading data from " + listLocation, ioe);
        } catch (ParserConfigurationException pce) {
            logger.error("AircellServletUtils.getSupportedMobileDevices: "
            + "unable to get a properly configured parser", pce);
        } catch (SAXException se) {
            logger.error("AircellServletUtils.getSupportedMobileDevices: "
            + "unable to parse document", se);
        }

        devices.put(SUPPORTED_DEVICES, supportedMobileDevices);
        devices.put(KNOWN_DEVICES, knownDevices);
        logger
        .debug("AircellServletUtils.getSupportedMobileDevices .... Exits ");
        return devices;
    }


    /**.
    /**.
     * Adds data which should be available to most/all pages to the model and
     * view
     * @param request submitted
     * @param mv ModelAndView to add to
     */
    public static void addDataToModel(
    HttpServletRequest request, ModelAndView mv
    ) {
        final Logger logger = LoggerFactory.getLogger(AircellServletUtils.class)
        ;
        View view = mv.getView();
        String viewName = mv.getViewName();
        if (view == null && viewName != null && !viewName
        .startsWith("redirect")) {
            AircellSessionManager session = getSession(request);
            if (session != null) {
                String redirection = session.getRedirectionError();
                mv.addObject("redirectionError", redirection);
                int count = session.getCount();
                mv.addObject("count", count);
                // -- flight
                FlightInformation flightInformation = session.getFlightInfo();
                if (flightInformation != null) {
                    mv.addObject("flight", flightInformation);
                } else {
                }
                // -- user
                AircellUser user = session.getUser();
                if (user != null) {
                    mv.addObject("user", user);
                } else {
                    //logger.warn("!! USER WAS NOT FOUND IN THE SESSION !!");
                }
                // -- card
                CreditCardDetails card = session.getCard();
                if (card != null) {
                    CreditCardDetails cardCopy =
                    AircellServiceUtils.copyCreditCard(card);
                    mv.addObject("card", cardCopy);
                }
                // -- jsessionid
                String jsessionid = session.getJsessionid();
                if (jsessionid != null) {
                    mv
                    .addObject(AircellServletUtils.SESSION_ID_NAME, jsessionid);
                }
                // -- product
                SubscriptionProduct product = session.getProduct();
                if (product != null) {
                    mv.addObject("product", product);
                }
                // -- state
                ApplicationState state = session.getState();
                if (state != null) {
                    mv.addObject(state.getStateName(), state);
                }
                String captchaError = session.getCaptchaError();
                if (captchaError != null) {
                    mv.addObject("captchaError", captchaError);
                } else {
                    mv.addObject("captchaError", "none");
                }

                String webtrendsId = session.getWebtrendsId();
                if (webtrendsId != null) {
                    mv.addObject("webtrendsId", webtrendsId);
                }
                String invoiceDate = session.getInvoiceDate();
                if (invoiceDate != null) {
                    mv.addObject("invoiceDate", invoiceDate);
                }

                String invoiceTime = session.getInvoiceTime();
                if (invoiceTime != null) {
                    mv.addObject("invoiceTime", invoiceTime);
                }

                int quantity = session.getQuantity();
                if (quantity != 0) {
                    mv.addObject("quantity", quantity);
                }


            }
            mv.addObject("libraryStart", libraryPath);

            mv.addObject("gogolibraryStart", gogolibraryPath);
        }
    }

    /**.
     * Uses the resource bundles to resolve error codes. Code is resolved to the
     * first message found.
     * @param error to resolve
     * @param messages resource bundle source to search
     * @param locale to look for message for
     * @return resolved error if a value is found; default message if no value
     *         for code is found; null otherwise
     */
    public static String resolveErrorAsMessage(
    ObjectError error, ResourceBundleMessageSource messages, Locale locale
    ) {
        String message = null;
        String[] codes = error.getCodes();
        Object[] args = error.getArguments();
        for (String code : codes) {
            try {
                message = messages.getMessage(code, args, locale);
            } catch (NoSuchMessageException nsme) {
                // do nothing
            }
            if (!StringUtils.isBlank(message)) {
                break;
            }
        }
        if (StringUtils.isBlank(message)) {
            message = error.getDefaultMessage();
        }
        return message;
    }

    /**.
     * @param var
     * @param delimiters
     * @return
     */
    public static String delimeterSeparatedToCamelCase(
    String var, String delimiters
    ) {
        StringBuilder camelCase = new StringBuilder();

        boolean first = true;

        StringTokenizer tokenizer = new StringTokenizer(var, delimiters);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (first) {
                camelCase.append(token.charAt(0));
                first = false;
            } else {
                camelCase.append(Character.toUpperCase(token.charAt(0)));
            }

            camelCase.append(token.substring(1, token.length()));
        }

        return camelCase.toString();
    }

    /**.
     * @param priceToFormat
     * @return
     */
    public static String formatPrice(Double priceToFormat) {
        StringBuilder priceBuilder = new StringBuilder();
        String price = priceToFormat.toString();
        int periodIndex = price.indexOf(".");
        String beforePeriod = price.substring(0, periodIndex + 1);
        String afterPeriod = price.substring(periodIndex + 1);
        int afterPeriodLength = afterPeriod.length();
        if (afterPeriodLength > 2) {
            afterPeriod = afterPeriod.substring(0, 2);
        } else {
            int numToAdd = 2 - afterPeriodLength;
            for (int i = 0; i < numToAdd; i++) {
                afterPeriod = afterPeriod + "0";
            }
        }
        priceBuilder.append("$");
        priceBuilder.append(beforePeriod);
        priceBuilder.append(afterPeriod);
        return priceBuilder.toString();
    }

    /* ********* SPRING PROPERTIES ********* */

    /**. @return the libraryPath */
    public String getLibraryPath() {
        return libraryPath;
    }

    /**. @param libraryPath the libraryPath to set */
    public void setLibraryPath(String pLibraryPath) {
        libraryPath = pLibraryPath;
    }

    /**. @return the gogolibraryPath */
    public String getGogolibraryPath() {
        return gogolibraryPath;
    }

    /**. @param gogolibraryPath the gogolibraryPath to set */
    public void setGogolibraryPath(String pGogoLibraryPath) {
        gogolibraryPath = pGogoLibraryPath;
    }


    /**. @return the headerName */
    public String getIpHeaderName() {
        return ipHeaderName;
    }

    /**. @param headerName the headerName to set */
    public void setIpHeaderName(String headerName) {
        ipHeaderName = headerName;
    }

}
