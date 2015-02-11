/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms,
 * without consent are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */
package com.aircell.abp.web.controller;

import com.aircell.abp.backchannel.AbpBackChannelClient;
import com.aircell.abp.backchannel.BackChannelUtils;
import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.utils.AircellServiceUtils;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.abp.utils.AircellServletUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**.
 * The <code>AAAActivateController</code> controller is called from GBP system
 * after user submits captcha and is ready to browse online. It communicates
 * with the GBP over a secure channel (back channel) to retrieve information
 * that is necessary for activation. Once the information is retrived, it
 * validates the session and verifes that the user has passed catpcha. If all
 * checks out, the controller then calls the AAA service to perform the actual
 * activation.
 * @author Oscar.Diaz
 */
public class AAAActivateController extends ABPAbstractCommandController {
    /**. String  object. */
    private String gbpSetActiveServiceUrl;
    /**. String  object. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**. String  object. */
    private String methodNameActivation;
    /**. String  object. */
    private String methodNameActivationFailed;
    /**. String  object. */
    private String notAuthenticatedForwardPage;
    /**. String  object. */
    private String sslCertStorePath;
    /**. String  object. */
    private String abpfailureView;
    /**. String  object. */
    private String abpVersion;

    /**.
     * This method getAbpVersion
     * @return String
     */
    public String getAbpVersion() {
        return abpVersion;
    }
    /**.
     * This method setAbpVersion
     * @param abpVersion String
     */
    public void setAbpVersion(String abpVersion) {
        this.abpVersion = abpVersion;
    }
    /**.
     * This method getFailureView
     * @return String
     */
    public String getFailureView() {
        return failureView;
    }
    /**.
     * This method setFailureView
     * @param failureView String
     */
    public void setFailureView(String failureView) {
        this.failureView = failureView;
    }
    /**.
     * This method getAbpfailureView
     * @return String
     */
    public String getAbpfailureView() {
        return abpfailureView;
    }
    /**.
     * This method setAbpfailureView
     * @param abpfailureView String
     */
    public void setAbpfailureView(String abpfailureView) {
        this.abpfailureView = abpfailureView;
    }
    /**. String  object. */
    private String failureView;

    /**.
     * This method handles the request,response objects
     * and populates the error objects (if reqd)
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param command Object
     * @param errors BindException
     * @return ModelAndView
     */
    @Override
    public ModelAndView handler(
    HttpServletRequest req, HttpServletResponse res, Object command,
    BindException errors
    ) {

        ModelAndView mv;
        String sessionId = req.getParameter(BackChannelUtils.GBP_SESSION_ID);
        String abpUserIP = AircellServletUtils.getIpAddress(req);
        logger.debug(
        "Entering AAAActivateController.handler with session id:" + sessionId
        + " for IP: " + abpUserIP
        );
        String errorCode = null;
        String errorText = null;
        String errorCause = null;
        Map<String, Object> model = new HashMap<String, Object>();

        try {
            AbpBackChannelClient client = new AbpBackChannelClient(
            this.getHttpClientGbpPage()
            );

            // Create back channel cookie
            client.setCookieName(this.getHttpPostCookieName());
            client.setCookieValue(sessionId);
            client.setCookieMaxAge(this.getCookieMaxAge());
            client.setCookieSecure(this.isCookieSecure());
            client.setCookieHttpDomain(this.getHttpPostDomain());
            client.setCookieHttpPath(this.getHttpPath());

            // Retrive user via secure communication link
            logger.debug("AAAActivateController.handler: "
            + abpUserIP
            + " Proceed to call '" + this.getHttpClientGbpPage()
            + "' thru back channel "
            );
            String userSt = client.getAircellUserStringPostMethod(
            abpUserIP, this.getParamName(), this.getMethodName(),
            sslCertStorePath
            );
            String tmpStr = userSt;
            try {
            	if(tmpStr != null) {
                String one =
                tmpStr.substring(0, tmpStr.indexOf("password") + 25);
                String two = tmpStr
                .substring(tmpStr.indexOf("username") - 44, tmpStr.length());
                logger.info("AAAActivateController.handler: "
                + abpUserIP
                + " Response back from the bac channel USER==>"
                + one + "xxxxxxxx"
                + two
                );
            	} else {
            		 logger.error ("AAAActivateController.handler: "
            	                + abpUserIP
            	                + " Response back from the bac channel is null"
            	                );
            	}
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("AAAActivateController.handler: "
                + abpUserIP
                + " Response back from the bac channel USER tmpStr=>'" + tmpStr
                );
            }

            logger.debug("AAAActivateController.handler: "
                + abpUserIP
                + " Response back from the bac channel USER='"
            	+ userSt + "'");


            // If no user exists, forward to the ABP default page to setup
            // session and data
            if (userSt == null || userSt.length() == 0) {

                mv = createFailureModelAndView(req, model);

                errorCode = AircellServletUtils.ABP_USER_INFO_NULL;
				errorText = "User Info received from GBP is either null or blank";
            } else {
                AirPassenger passenger =
                BackChannelUtils.decodeAircellUserFromXML(userSt);
                logger.debug("AAAActivateController.handler: "
                		+ abpUserIP
                       	+ " passenger " + passenger);
                if (passenger == null) {

                    mv = createFailureModelAndView(req, model);

                    errorCode = AircellServletUtils.ABP_NOT_A_PASSENGER;
					errorText = "Could not create Passenter object from UserInfo received from GBP";
                } else if (!passenger.isCaptchaPassed()) {
                    logger.error("AAAActivateController.handler: "
                    	+ abpUserIP
                        + " Error: captcha not passed.");

                    mv = createFailureModelAndView(req, model);

                    errorCode = AircellServletUtils.ABP_CAPTCHA_NOT_PASSED;
					errorText = "CAPTCHA is not passed in the User Info received from GBP";
                } else {
                    logger.debug("AAAActivateController.handler: "
                    + abpUserIP
                    + " Proceed to call the startATGSession() "
                    + "method and generate JMS msg to ACPU");
                    ServiceResponse acpuResponse = startATGSession(
                    passenger, req, res, errors
                    );
                    
                    String tmpUnameStr = ""; 
                    if(passenger.getLoginDetails()!= null){
                        tmpUnameStr = passenger.getLoginDetails().getUsername();
                    }
                    logger.info("AAAActivateController.handler: "
                    	+ "ACPU response from startATGSession()= "
                    	+ acpuResponse.isSuccess()
                    	+ "   for IP Address = "  + abpUserIP
                    	+ " and passenger = " + tmpUnameStr);

                    if (acpuResponse.isSuccess()) {
                        // call a back channel server and set the activeService
                        // flag to true

                        client = new AbpBackChannelClient(
                        getGbpSetActiveServiceUrl()
                        );
                        client.setCookieName(this.getHttpPostCookieName());
                        client.setCookieValue(sessionId);
                        client.setCookieMaxAge(this.getCookieMaxAge());
                        client.setCookieSecure(this.isCookieSecure());
                        client.setCookieHttpDomain(this.getHttpPostDomain());
                        client.setCookieHttpPath(this.getHttpPath());
                        String result = client.setGbpActiveServicePostMethod(
                        abpUserIP, this.getParamName(),
                        this.getMethodNameActivation()
                        );
                        logger.info("AAAActivateController.handler: "
                        	+ abpUserIP
                            + " setGbpActiveServicePostMethod Response ="
                            + result);
                        mv = createSuccessModelAndView(req);
                        errorCode = null;
                        errorText = null;
                        errorCause = null;
                    } else {

                        errorCode = AircellServletUtils.ACPU_UNSPECIFIED_ERROR;

						if(acpuResponse.getErrorCode() != null) {
                            errorCode = acpuResponse.getErrorCode().toString();
						}
                        if(acpuResponse.getErrorText()!= null) {
                            errorText = acpuResponse.getErrorText().toString();
                        }
                        if(acpuResponse.getErrorCause()!= null) {
                            errorCause =
                                acpuResponse.getErrorCause().getMessage();
                        }
                        StringBuffer strBuff = new StringBuffer();
                        strBuff.append("Activation Failed for IP  : ");
                        strBuff.append(abpUserIP);
                        strBuff.append(", MAC Address - ");
                        strBuff.append(passenger.getMacAddress());
                        strBuff.append(", ErrorCode= ");
                        strBuff.append(errorCode);
                        strBuff.append(", ErrorText= ");
                        strBuff.append(errorText);
                        strBuff.append(", ErrorCause= ");
                        strBuff.append(errorCause);

                        logger.error("AAAActivateController.handler: "
                            + strBuff.toString());

                        AircellServiceUtils.errorsFromErrorCodes(
                        acpuResponse, errors, ""
                        );
                        // check to see if there is a password reset

                        model.put(
                        "passwordRejected", isPasswordReset(acpuResponse)
                        );
                       mv = createFailureModelAndView(req, model);

                    }
                }
            }
        } catch (Exception e) {
            logger.error("AAAActivateController.handler: "
            + abpUserIP
            + " Error: Connection to the ground was unsuccessful. "
            + "\nDetails: "
            + e.getMessage(), e
            );
            errors.reject(
            "purchaseAndActivateform.startATGSession.failed.generic",
            "Session cannot be started at the moment. Please try again later"
            );
            // TODO: New York : Send the user to a Connection error page
            mv = createFailureModelAndView(req);

            errorCode = AircellServletUtils.ABP_UNSPECIFIED_ERROR;
            errorText = e.getMessage();
        }

        Flight flight = getFlight();
        if (flight != null) {
            mv.addObject("flight", flight.getFlightInformation());
        }
        // Check for activateError and call
        // back channel to update the activate Error.
        if (errorCode != null) {
            sendActivateError(errorCode, errorText, errorCause, sessionId, abpUserIP);

        }

        mv.addObject(AircellServletUtils.SESSION_ID_NAME, sessionId);
        return mv;
    }

    /**. @see com.aircell.abp.web.controller.
     * AircellCommandController#
     * getFailureView(javax.servlet.http.HttpServletRequest) */
    @Override
    protected String getFailureView(HttpServletRequest request) {
        String gbpSessionID =
        request.getParameter(BackChannelUtils.GBP_SESSION_ID);
        String failure = super.getFailureView(request);
        if (StringUtils.contains(failure, "redirect:") || StringUtils
        .contains(failure, "forward:")) {
            StringBuilder builder = new StringBuilder(failure);
            builder.append(";").append(AircellServletUtils.SESSION_ID_NAME)
            .append("=").append(gbpSessionID);
            failure = builder.toString();
        }
        return failure;
    }
    /**.This method returns the filure view.
     * @param request HttpServletRequest
     * @param abpversion String
     * @param model Map
     * @return ModelAndView
     * */

    protected ModelAndView getFailureViews(
    HttpServletRequest request, String abpversion, Map model
    ) {
        ModelAndView mv = null;

        /* commenting out the logic that will determine activation error
         * view from plan or ground and in all the scenarios displaying
         * the activation error page from ground.
         *
         * */
        mv = createFailureModelAndView(request, model);

        return mv;
    }

    /**. @see com.aircell.abp.web.controller.AircellCommandController#
     * getSuccessView(javax.servlet.http.HttpServletRequest)
     * @param request HttpServletRequest
     * @return String
     * */
    @Override
    protected String getSuccessView(HttpServletRequest request) {
        String gbpSessionID =
        request.getParameter(BackChannelUtils.GBP_SESSION_ID);
        String success = super.getSuccessView(request);
        if (StringUtils.contains(success, "redirect:") || StringUtils
        .contains(success, "forward:")) {
            StringBuilder builder = new StringBuilder(success);
            builder.append(";").append(AircellServletUtils.SESSION_ID_NAME)
            .append("=").append(gbpSessionID);
            success = builder.toString();
        }
        return success;
    }

    /**.
     * Method to start the timer on the ATG session
     * @param p - Passenger for whome the session is being started
     * @param request - the submitted request
     * @param response - the response to send
     * @param errors - any errors generated by the submission or link starting
     * process
     * @return the ServiceResponse returned by the start session call on the
     * @throws IllegalStateException when the user's session is already started
     */
    public ServiceResponse startATGSession(
    final AirPassenger passenger, final HttpServletRequest request,
    final HttpServletResponse response, final BindException errors
    ) throws IllegalStateException {
        ServiceResponse acpuResponse;
        if (!passenger.getSession().isActivated()) {
            // if the session is not already active activate it
            // if the activate controller is being called then captcha has to
            // have been passed
            acpuResponse = passenger.startATGSession(true);
            if (!acpuResponse.isSuccess()) {
                if (AcpuErrorCode.ATG_LINK_DOWN.equals(
                acpuResponse.getErrorCode()
                )) {
                    errors.reject(
                    "purchaseAndActivateForm.startATGSession."
                    + "failed.atgLinkDown",
                    "Session cannot be started at "
                    + "the moment as the ATG link id down");
                } else if (AcpuErrorCode.AUTHENTICATION_REJECTED
                .equals(acpuResponse.getErrorCode())) {
                    AircellServiceUtils.errorsFromErrorCodes(
                    acpuResponse, errors, ""
                    );
                    // the caller must check for password reset
                    errors.reject(
                    "purchaseAndActivateForm.startATGSession.failed.pwdReset",
                    "Session cannot be started. New password must be entered"
                    );
                    // errors.rejectValue("pwdReset",
                    // "creditCardDetails.securityCode.invalid",
                    // "Security code can only be digits");
                } else {
                    errors.reject(
                    "purchaseAndActivateform.startATGSession.failed.generic",
                    "Session cannot be started at "
                    + "the moment. Please try again later");
                }
            }
        } else {
            // otherwise return success, which here means the user has an active
            // session
            acpuResponse = new ServiceResponse();
            acpuResponse.setSuccess(true);
        }
        return acpuResponse;
    }
    /** this method sends the actiavte errors.
     * @param errorCode String
     * @param errorText String
     * @param errorCause String
     * @param sessionId String
     * @param abpUserIP String
     * */

    private void sendActivateError(
    String errorCode, String errorText, String errorCause, String sessionId, String abpUserIP
    ) {
        try {
            if (errorCode != null) {
                AbpBackChannelClient client =
                new AbpBackChannelClient(getGbpSetActiveServiceUrl());

                client = new AbpBackChannelClient(getGbpSetActiveServiceUrl());
                client.setCookieName(this.getHttpPostCookieName());
                client.setCookieValue(sessionId);
                client.setCookieMaxAge(this.getCookieMaxAge());
                client.setCookieSecure(this.isCookieSecure());
                client.setCookieHttpDomain(this.getHttpPostDomain());
                client.setCookieHttpPath(this.getHttpPath());
                logger
                .debug("AAAActivateController.sendActivateError"
                	  + abpUserIP
                      + " Proceed to send activate "
                      + "Error through back channel");
                String response = client.setGbpActivateErrorPostMethod(
                    abpUserIP, errorCode, errorText, errorCause,
                    this.getParamName(),this.getMethodNameActivationFailed());
                logger.error("AAAActivateController.sendActivateError"
                + " response of setting activateError "
                + "through backchannel : ipaddress : "
                + abpUserIP + ". Error Code :" 
                + errorCode + ". Error Text " 
                + errorText + ". Response : " + response 
                );
            }
        } catch (Exception e) {
            logger.error("AAAActivateController.sendActivateError"
            + "Error: Connection to the ground was unsuccessful. ipAddress : "
            + abpUserIP + " :\nDetails: " + e.getMessage(), e
            );
        }
    }
    /** this method isPasswordReset.
     * @param acpuResponse ServiceResponse
     * @return boolean
     * */
    private boolean isPasswordReset(ServiceResponse acpuResponse) {
        if (AcpuErrorCode.AUTHENTICATION_REJECTED.equals(
        acpuResponse.getErrorCode()
        )) {
            // there was a password reset
            return true;
        }
        return false;
    }
    /**.
     * This method setGbpSetActiveServiceUrl
     * @param st String
     */

    public void setGbpSetActiveServiceUrl(final String st) {
        this.gbpSetActiveServiceUrl = st;
    }
    /**.
     * This method setMethodName
     * @param st String
     */
    public void setMethodName(final String st) {
        this.methodName = st;
    }
    /**.
     * This method setMethodNameActivation
     * @param st String
     */
    public void setMethodNameActivation(final String st) {
        this.methodNameActivation = st;
    }
    /**.
     * This method setMethodNameActivationFailed
     * @param st String
     */
    public void setMethodNameActivationFailed(final String st) {
        this.methodNameActivationFailed = st;
    }
    /**.
     * This method setNotAuthForward
     * @param st String
     */
    public void setNotAuthForward(final String st) {
        this.notAuthenticatedForwardPage = st;
    }
    /**.
     * This method setParamName
     * @param st String
     */
    public void setParamName(final String st) {
        this.paramName = st;
    }
    /**.
     * This method setSslCertStorePath
     * @param st String
     */
    public void setSslCertStorePath(final String st) {
        this.sslCertStorePath = st;
    }
    /**.
     * This method getGbpSetActiveServiceUrl
     * @return String
     */
    public String getGbpSetActiveServiceUrl() {
        return gbpSetActiveServiceUrl;
    }
    /**.
     * This method getMethodName
     * @return String
     */
    public String getMethodName() {
        return methodName;
    }
    /**.
     * This method getMethodNameActivation
     * @return String
     */
    public String getMethodNameActivation() {
        return methodNameActivation;
    }
    /**.
     * This method getMethodNameActivationFailed
     * @return String
     */
    public String getMethodNameActivationFailed() {
        return methodNameActivationFailed;
    }
    /**.
     * This method getNotAuthForward
     * @return String
     */
    public String getNotAuthForward() {
        return notAuthenticatedForwardPage;
    }
    /**.
     * This method getParamName
     * @return String
     */
    public String getParamName() {
        return paramName;
    }
    /**.
     * This method getSslCertStorePath
     * @return String
     */
    public String getSslCertStorePath() {
        return sslCertStorePath;
    }

}
