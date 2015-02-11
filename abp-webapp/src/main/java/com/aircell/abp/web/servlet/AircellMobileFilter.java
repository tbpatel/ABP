/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.servlet;

import com.aircell.abp.model.Device;
import com.aircell.abp.utils.AircellServletUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**.
 * . Filter to determine if the request is from a supported mobile device
 * @author AKQA - bryan.swift
 * @version $Revision: 3461 $
 */
public class AircellMobileFilter implements Filter {
    /**. . Logger */
    private static final Logger logger =
    LoggerFactory.getLogger(AircellMobileFilter.class);
    /**. . Name of the param to look for as the list location value */
    private static final String LIST_LOCATION_PARAM = "listLocation";
    /**. . Name of the param to look for as the exclude pattern value */
    private static final String EXCLUDE_PATTERN_PARAM = "excludePattern";
    /**. . Name of param to look for as the splash page */
    private static final String SPLASH_PAGE = "splashPage";
    /**. . Name of the param to look for as the secure port */
    private static final String SECURE_PORT = "securePort";
    /**.
     * . Request attribute to check for in order to determine whether or not to
     * actually run the filter
     */
    public static final String DO_ONCE_ATTRIBUTE = "MobileDoOnce";
    /**. . FilterConfig sent to init method */
    private FilterConfig filterConfig;
    /**. . List of supported mobile devices */
    private static List<Device> supportedMobileDevices;
    /**. . List of mobile device agent substrings */
    private static List<Device> knownDevices;
    /**. . List of devices this filter has seen */
    private static List<String> seenDevices;
    /**. . Location from which to retrieve mobile device info */
    private String listLocation;
    /**. . REGEX pattern for filter mappings to exclude */
    private String excludePattern;
    /**. . REGEX for splash page */
    private String splashPage;
    /**. . Value for secure port */
    private String securePort;

    /**. . @see javax.servlet.Filter#destroy() */
    public void destroy() {
        // when destroying this filter log each user agent the filter has seen
        for (String device : seenDevices) {
            logger.info("AircellMobileFilter.destroy: "+device);
        }
    }

    /**.
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @param req req
     * @param res res
     * @param chain chain
     * @throws IOException ex
     * @throws ServletException se
     */
    public void doFilter(
    ServletRequest req, ServletResponse res, FilterChain chain
    ) throws IOException, ServletException {
        if (req instanceof HttpServletRequest
        && req.getAttribute(AircellServletUtils.IS_MOBILE) == null
        && req.getAttribute(DO_ONCE_ATTRIBUTE) == null
        && req.getAttribute(AircellServletUtils.IS_SUPPORTED_MOBILE) == null) {
            // only do mobile detection if it hasn't already been
            // done for this request
            HttpServletRequest request = (HttpServletRequest) req;
            request.setAttribute(DO_ONCE_ATTRIBUTE, Boolean.TRUE);
            String path = request.getServletPath();
            if (!path.matches(excludePattern)) {
                // have to initialize mobile devices here otherwise a
                // tomcat restart hangs if the listLocation
                // is on the same server as the application
                initSupportedMobileDevices();
                String userAgent = request.getHeader("User-Agent");
                if (!seenDevices.contains(userAgent)) {
                    seenDevices.add(userAgent);
                }
                Boolean mobileDevice = isMobileDevice(request, userAgent);
                Boolean supportedDevice = isSupportedMobileDevice(userAgent);
                request
                .setAttribute(AircellServletUtils.IS_MOBILE, mobileDevice);
                request.setAttribute(
                AircellServletUtils.IS_SUPPORTED_MOBILE, supportedDevice
                );
                String ip = AircellServletUtils.getIpAddress(request);
                logger.info("AircellMobileFilter.doFilter: "
                + "url: {} ; mobile: {} ; user agent: {} ; ip: {}"
                , new Object[]{
                request.getServletPath(), mobileDevice, userAgent, ip
                }
                );
            }
            if (AircellServletUtils.isMobileDevice(request)
            && (res instanceof HttpServletResponse) && path != null
            && splashPage != null && path.matches(splashPage) && !request
            .isSecure()) {
                HttpServletResponse response = (HttpServletResponse) res;
                String url = buildSecureUrl(request);
                response.sendRedirect(url);
                return;
            }

        }

        chain.doFilter(req, res);
    }

    /**. @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     *
     * @param filterConfig filter
     * @throws ServletException se
     * */
    public void init(FilterConfig filterConfig) throws ServletException {
        logger
        .debug("Initializing AircellMobileFilter.filter '"
              + filterConfig.getFilterName() + "'");
        this.filterConfig = filterConfig;
        seenDevices = new ArrayList<String>();
        String ipListLocation =
        filterConfig.getInitParameter(LIST_LOCATION_PARAM);
        String excludePattern =
        filterConfig.getInitParameter(EXCLUDE_PATTERN_PARAM);
        String splashPage = filterConfig.getInitParameter(SPLASH_PAGE);
        String securePort = filterConfig.getInitParameter(SECURE_PORT);
        if (listLocation == null && ipListLocation == null) {
            String error =
            "listLocation init param is required for AircellMobileFilter";
            throw new ServletException(error);
        } else if (ipListLocation != null) {
            setListLocation(ipListLocation);
        }
        StringBuilder excludePatternBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(excludePattern)) {
            excludePatternBuilder.append(excludePattern);
      // TODO New York - should probably include listLocation in exclude pattern
        }
        setExcludePattern(excludePatternBuilder.toString());
        if (StringUtils.isNotBlank(splashPage)) {
            setSplashPage(splashPage);
        }
        if (StringUtils.isNotBlank(securePort)) {
            setSecurePort(securePort);
        }
        logger.debug("AircellMobileFilter.init: "
        + "Filter '" + filterConfig.getFilterName()
        + "' configured successfully"
        );
    }

    /**.
     * initializes the list of supported mobile devices.
     */
    public void initSupportedMobileDevices() {
        //logger.debug("called initSupportedMobileDevices");
        initSupportedMobileDevices(getListLocation());
    }

    /**. Delegates to {@link AircellServletUtils#
     * getSupportedMobileDevices(String)}
     *
     * @param listLocation listlocation
     * */
    private static synchronized void initSupportedMobileDevices(
    String listLocation
    ) {
        if (knownDevices == null) {
            logger
            .debug("AircellMobileFilter.initSupportedMobileDevices: "
              + "initializing mobile device lists from: {}", listLocation);
            Map<String, List<Device>> devicesMap =
            AircellServletUtils.getSupportedMobileDevices(listLocation);
            knownDevices = devicesMap.get(AircellServletUtils.KNOWN_DEVICES);
            supportedMobileDevices =
            devicesMap.get(AircellServletUtils.SUPPORTED_DEVICES);
        } else {
            logger.debug("AircellMobileFilter.initSupportedMobileDevices: "
                   + "mobile devices initialized; bailing out");
        }
    }

    /**.
     * refreshes the supported mobile devices.
     */
    public void refreshSupportedMobileDevices() {
        refreshSupportedMobileDevices(getListLocation());
    }

    /**.
     * Refresh the list of mobile devices
     * @param listLocation location
     */
    private static synchronized void refreshSupportedMobileDevices(
    String listLocation
    ) {
        Map<String, List<Device>> devicesMap =
        AircellServletUtils.getSupportedMobileDevices(listLocation);
        knownDevices = devicesMap.get(AircellServletUtils.KNOWN_DEVICES);
        supportedMobileDevices =
        devicesMap.get(AircellServletUtils.SUPPORTED_DEVICES);
    }

    /**.
     * Determine if a given user agent is a supported mobile device
     * @param request req
     * @param userAgent  userAgent
     * @return true if userAgent is in the supported user agent list false
     *         otherwise
     */

    public Boolean isMobileDevice(
    HttpServletRequest request, String userAgent
    ) {
        boolean isMobileDevice = true;
        for (Device device : knownDevices) {
            String deviceName = device.getName();
            if (StringUtils.containsIgnoreCase(userAgent, deviceName)) {
                isMobileDevice = device.isMobile();
                break;
            }
        }
        if (!isMobileDevice) {
            String xWapProfile = request.getHeader("x-wap-profile");
            String profile = request.getHeader("Profile");
            String opt = request.getHeader("Opt");
            String optProfile;
            if (StringUtils.isNotBlank(xWapProfile)) {
                isMobileDevice = true;
            } else if (StringUtils.isNotBlank(profile)) {
                isMobileDevice = true;
            } else if (StringUtils.isNotBlank(opt)) {
                String namespace = StringUtils.substringAfter(opt, "ns=");
                StringBuilder optProfileHeader =
                new StringBuilder(namespace).append("-Profile");
                optProfile = request.getHeader(optProfileHeader.toString());
                if (StringUtils.isNotBlank(optProfile)) {
                    isMobileDevice = true;
                }
            }
        }
        return isMobileDevice;
    }

    /**.
     * Determine if a given user agent is a supported mobile device
     * @param userAgent userAgent
     * @return true if userAgent is in the supported user agent list false
     *         otherwise
     */
    public Boolean isSupportedMobileDevice(String userAgent) {
        boolean isSupportedMobileDevice = false;
        for (Device device : supportedMobileDevices) {
            String deviceName = device.getName();
            if (StringUtils.containsIgnoreCase(userAgent, deviceName)
            && !StringUtils.containsIgnoreCase(userAgent, "HTC")) {
                isSupportedMobileDevice = device.isSupported();
                break;
            }
        }
        return isSupportedMobileDevice;
    }

    /**.
     * @param userAgent agent
     * @return deviceObject
     */
    public Device getDevice(String userAgent) {
        Device rDevice = null;
        for (Device device : knownDevices) {
            String deviceName = device.getName();
            if (StringUtils.containsIgnoreCase(userAgent, deviceName)) {
                rDevice = device;
            }
        }
        return rDevice;
    }


    /**.
     * .
     * @param request
     * @return
     */
    private String buildSecureUrl(HttpServletRequest request) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://");
        urlBuilder.append(request.getServerName());
        urlBuilder.append(":");
        urlBuilder.append(getSecurePort());
        if (StringUtils.isNotBlank(request.getContextPath())) {
            urlBuilder.append(request.getContextPath());
        }
        String path = request.getServletPath();
        if (StringUtils.isNotBlank(path)) {
            urlBuilder.append(path);
        }
        HttpSession session = request.getSession(false);

        String sessionId = null;
        if (session != null) {
            sessionId = session.getId();
        } else {
            sessionId = request.getSession().getId();
        }

        if (StringUtils.isNotBlank(sessionId)) {
            urlBuilder.append(";jsessionid=");
            urlBuilder.append(sessionId);
        }
        if (StringUtils.isNotBlank(request.getPathInfo())) {
            urlBuilder.append(request.getPathInfo());
        }
        if (StringUtils.isNotBlank(request.getQueryString())) {
            urlBuilder.append("?");
            urlBuilder.append(request.getQueryString());
        }
        String url = urlBuilder.toString();
        return url;
    }

    /**. . @return the filterConfig */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**. . @param filterConfig the filterConfig to set */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**. . @return the listLocation */
    public String getListLocation() {
        return listLocation;
    }

    /**. . @param listLocation the listLocation to set */
    public void setListLocation(String listLocation) {
        this.listLocation = listLocation;
    }

    /**. . @return the excludePattern */
    public String getExcludePattern() {
        return excludePattern;
    }

    /**. . @param excludePattern the excludePattern to set */
    public void setExcludePattern(String excludePattern) {
        this.excludePattern = excludePattern;
    }

    /**. . @return the securePort */
    public String getSecurePort() {
        return securePort;
    }

    /**. . @param securePort the securePort to set */
    public void setSecurePort(String securePort) {
        this.securePort = securePort;
    }

    /**. . @return the splashPage */
    public String getSplashPage() {
        return splashPage;
    }

    /**. . @param splashPage the splashPage to set */
    public void setSplashPage(String splashPage) {
        this.splashPage = splashPage;
    }
}
