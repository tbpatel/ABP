/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.springframework.web.servlet.support.RequestContext;

import com.aircell.abp.service.ConfigService;
import com.aircell.abp.service.SessionInfoService;
import com.aircell.abp.service.UserDetailsService;
import com.aircell.abp.utils.AircellServletUtils;

/**.
 * . Abstract controller class which adds some functionality for requests
 * identified as ajax requests
 * @author AKQA - bryan.swift
 * @version $Revision: 3641 $
 */
public abstract class AircellCommandController
extends AbstractCommandController {
    /* ---- SERVICES --- */
    /**. . Configuration service */
    private ConfigService configService;
    /**. . ATG Session information service */
    private SessionInfoService sessionInfoService;
    /**. . User details service */
    private UserDetailsService userDetailsService;
    /**. . Default success view */
    private String successView;
    /**. . Default failure view */
    private String failureView;
    /**. . Holds the path to the success json view */
    private String jsonSuccess;
    /**. . Holds the path to the error/failure json view */
    private String jsonError;
    /**. . Holds the path to the success XML view */
    private String xmlSuccess;
    /**. . Holds the path to the error/failure json view */
    private String xmlError;
    /**. . Holds the path to the success json view */
    private String jsonpSuccess;
    /**. . Holds the path to the error/failure json view */
    private String jsonpError;
    /**. . Holds the path to the success mobile view */
    private String mobileSuccess;
    /**. . Holds the path to the error/failure mobile view */
    private String mobileError;
    /**. . Map to hold alternate views */
    private Map<String, String> alternateViews;

    /**.
     * . Simple method to create an appropriate ModelAndView based on whether or
     * not the request was an ajax one - Delegates to {@link
     * #createSuccessModelAndView(HttpServletRequest, Map)} passing null
     * @param request the submitted request
     * @return ModelAndView with an appropriate view
     */
    protected ModelAndView createSuccessModelAndView(
    HttpServletRequest request
    ) {
        return createSuccessModelAndView(request, null);
    }
    protected ModelAndView createSuccessMediaModelAndView(
    	    HttpServletRequest request
    	    ) {
    	        return createSuccessMediaModelAndView(request, null);
    	    }
    protected ModelAndView createFailureMediaModelAndView(
    	    HttpServletRequest request
    	    ) {
    	        return createFailureMediaModelAndView(request, null);
    	    }

    /**.
     * . Simple method to create an appropriate ModelAndView based on whether or
     * not the request was an ajax one
     * @param request the submitted request
     * @param model - a map of modelName -> modelObject pairs
     * @return ModelAndView with an appropriate view
     */
    protected ModelAndView createSuccessModelAndView(
    HttpServletRequest request, Map model
    ) {
        String success = getSuccessView(request);
        ModelAndView mv = new ModelAndView(success);
        if (model != null) {
            mv.addAllObjects(model);
        }

        return mv;
    }
    
    protected ModelAndView createSuccessMediaModelAndView(
    HttpServletRequest request, Map model
    ) {
    	 logger.info("Inside createSuccessMediaModelAndView");
        String success = getMediaSuccessView(request);
        logger.info("Success value:::" + success);
        ModelAndView mv = new ModelAndView(success);
        if (model != null) {
            mv.addAllObjects(model);
        }

        return mv;
    }
    
    protected ModelAndView createFailureMediaModelAndView(
    	    HttpServletRequest request, Map model
    	    ) {
    	        String failure = getMediaFailureView(request);
    	        ModelAndView mv = new ModelAndView(failure);
    	        if (model != null) {
    	            mv.addAllObjects(model);
    	        }

    	        return mv;
    	    }


    /**.
     * . Simple method to create an appropriate ModelAndView for failure -
     * Delegates to {@link #createFailureModelAndView(HttpServletRequest, Map)}
     * passing null
     * @param request the submitted request
     * @return ModelAndView with an appropriate view
     */
    protected ModelAndView createFailureModelAndView(
    HttpServletRequest request
    ) {
        return createFailureModelAndView(request, null);
    }

    /**.
     * . Simple method to create an appropriate ModelAndView based on whether or
     * not the request was an ajax one
     * @param request the submitted request
     * @param model - a map of modelName -> modelObject pairs
     * @return ModelAndView with an appropriate view
     */
    @SuppressWarnings("unchecked")
    protected ModelAndView createFailureModelAndView(
    HttpServletRequest request, Map model
    ) {
        if (model == null) {
            model = new HashMap();
        }
        String failure = getFailureView(request);
        model.put(AircellServletUtils.IS_FAILURE, Boolean.TRUE);
        ModelAndView mv = new ModelAndView(failure);
        if (model != null) {
            mv.addAllObjects(model);
        }

        return mv;
    }

    /**.
     * .
     * @param request
     * @param mv
     * @return
     */
    protected boolean isSuccess(HttpServletRequest request, ModelAndView mv) {
        String successView = getSuccessView(request);
        String viewName = mv.getViewName();
        return StringUtils.equals(viewName, successView);
    }

    /**.
     * .
     * @param request
     * @param mv
     * @return
     */
    protected boolean isFailure(HttpServletRequest request, ModelAndView mv) {
        String failureView = getFailureView(request);
        String viewName = mv.getViewName();
        return StringUtils.equals(viewName, failureView);
    }

    /**.
     * . Runs prior to returning the model and view
     * @param request
     * @param response
     * @param mv
     */
    protected final void onComplete(
    HttpServletRequest request, HttpServletResponse response, ModelAndView mv
    ) {
        if (isSuccess(request, mv)) {
            onSuccess(request, response);
        } else if (isFailure(request, mv)) {
            onFailure(request, response);
        }
        View view = mv.getView();
        String viewName = mv.getViewName();
        if (view != null || viewName.startsWith("redirect")) {
            mv.getModel().clear();
        }
    }

    /**.
     * . Method to be run when the controller has executed successfully prior to
     * returning the model and view
     * @param request
     * @param response
     */
    protected void onSuccess(
    HttpServletRequest request, HttpServletResponse response
    ) {
        logger.debug(
        AircellServletUtils.getIpAddress(request)
        + ": exiting onSuccess with view - " + getSuccessView(request)
        );
    }

    /**.
     * . Method to be run when the controller has executed with a failure state
     * prior to returning the model and view
     * @param request
     * @param response
     */
    protected void onFailure(
    HttpServletRequest request, HttpServletResponse response
    ) {
        logger.debug(
        AircellServletUtils.getIpAddress(request)
        + ": exiting on failure with view - " + getFailureView(request)
        );
    }

    /**.
     * .
     * @see org.springframework.web.servlet.mvc.AbstractCommandController#
     * handle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @SuppressWarnings("unchecked") @Override
    protected final ModelAndView handle(
    HttpServletRequest request, HttpServletResponse response, Object command,
    BindException errors
    ) throws Exception {
        logger.debug(
        AircellServletUtils.getIpAddress(request) + ": handling request"
        );
        ModelAndView mv = handler(request, response, command, errors);
        Map<String, Object> model = referenceData(request, command, errors);
        AircellServletUtils.addDataToModel(request, mv);
        mv.addAllObjects(model);
        onComplete(request, response, mv);
        logger.warn("AircellCommandController.handle:"
                  + " !Errors! - " + errors.toString());
        return mv;
    }

    /**.
     * . Overridden in order to retrieve global form errors for the ajax failure
     * requests and add them to the model.
     * @see org.springframework.web.servlet.mvc.SimpleFormController#
     * referenceData(javax.servlet.http.HttpServletRequest,
     *      java.lang.Object, org.springframework.validation.Errors)
     */
    @SuppressWarnings("unchecked")
    protected Map referenceData(
    HttpServletRequest request, Object command, Errors errors
    ) throws Exception {
        Boolean doOnce = (Boolean) request
        .getAttribute(AircellServletUtils.DO_REFERENCE_DATA_ONCE);
        if (doOnce == null || !doOnce) {
            request.setAttribute(
            AircellServletUtils.DO_REFERENCE_DATA_ONCE, Boolean.TRUE
            );
            Map<String, Object> referenceData = new HashMap<String, Object>();
            if (referenceData == null) {
                referenceData = new HashMap<String, Object>();
            }
            List<String> errorList = new ArrayList<String>();
            List globalErrors = errors.getGlobalErrors();
            for (Object errorObj : globalErrors) {
                if (errorObj instanceof ObjectError) {
                    ObjectError error = (ObjectError) errorObj;
                    String message = resolveErrorAsMessage(request, error);
                    errorList.add(message);
                }
            }
            List<String> allErrorList = new ArrayList<String>();
            for (ObjectError error : (List<ObjectError>) errors
            .getAllErrors()) {
                String code = error.getCode();
                code =
                AircellServletUtils.delimeterSeparatedToCamelCase(code, ".");
                allErrorList.add(code);
            }
            referenceData.put("globalErrors", errorList);
            referenceData.put("errorCodes", allErrorList);
            return referenceData;
        } else {
            return new HashMap();
        }
    }

    /**.
     * . Method to do the heavy lifting of deciding which ModelAndView to
     * return
     * @param request current HTTP request
     * @param response current HTTP response
     * @param command the populated command object
     * @param errors validation errors holder
     * @return a ModelAndView to render, or <code>null</code> if handled
     *         directly
     */
    public abstract ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response, Object command,
    BindException errors
    );

    /**.
     * . Examines the request to determine the attributes which determining the
     * appropriate success view depends on then finds the appropriate success
     * view
     * @param request the submitted request
     * @return String name of the success view
     */
    protected String getSuccessView(HttpServletRequest request) {
        boolean isAjax = isAjax(request);
        boolean isValidateOnly = isValidateOnly(request);
        boolean isMobile = isMobile(request);
        String success = getSuccessView();
        if (isAjax && isValidateOnly) {
            success = getJsonError();
        } else if (isAjax) {
            success = getJsonSuccess();
        } else if (isMobile) {
            success = getMobileSuccess();
        }
        if (StringUtils.isBlank(success)) {
            success = getSuccessView();
        }
        return success;
    }

    //Added by Rajasekar - To fix AMP free session promocode
    // issue(Defect id - 1018)
    protected String getFreeSuccessView(HttpServletRequest request) {
        boolean isAjax = isAjax(request);
        boolean isValidateOnly = isValidateOnly(request);

        String success = getSuccessView();
        if (isAjax && isValidateOnly) {
            success = getJsonError();
        } else if (isAjax) {
            success = getJsonSuccess();
        }
        return success;
    }
    
    protected String getMediaSuccessView(HttpServletRequest request){
    	
    	boolean isAjax = isAjax(request);
        boolean isXMLAjax = isXmlAjax(request);
        boolean isJsonpAjax = isJsonpAjax(request);
        String success = getSuccessView();
        if (isXMLAjax) {
            success = getXmlSuccess();
        } else if (isJsonpAjax) {
            success = getJsonpSuccess();
        } else if(isAjax){
        	success=getJsonSuccess();
        }
        return success;
    }
    
  protected String getMediaFailureView(HttpServletRequest request){
    	
    	boolean isAjax = isAjax(request);
        boolean isXMLAjax = isXmlAjax(request);
        boolean isJsonpAjax = isJsonpAjax(request);
        String failure = getFailureView();
        if (isXMLAjax) {
        	failure = getXmlError();
        } else if (isJsonpAjax) {
        	failure = getJsonpError();
        } else if(isAjax){
        	failure = getJsonError();
        }
        return failure;
    }

    /**.
     * . Examines the request to determine the attributes which determining the
     * appropriate failure view depends on then finds the appropriate failure
     * view
     * @param request the submitted request
     * @return String name of the failure view
     */
    protected String getFailureView(HttpServletRequest request) {
        boolean isAjax = isAjax(request);
        boolean isValidateOnly = isValidateOnly(request);
        boolean isMobile = isMobile(request);
        String failure = getFailureView();
        if (isAjax || isValidateOnly) {
            failure = getJsonError();
        } else if (isMobile) {
            failure = getMobileError();
        }
        if (StringUtils.isBlank(failure)) {
            failure = getFailureView();
        }
        return failure;
    }

    /**.
     * . Uses the resource bundles to resolve error codes. Code is resolved to
     * the first message found.
     * @param request submitted
     * @param error to resolve
     * @return resolved error if a value is found; default message if no value
     *         for code is found; null otherwise
     */
    protected String resolveErrorAsMessage(
    HttpServletRequest request, ObjectError error
    ) {
        final RequestContext rc = new RequestContext(request);
        final Locale locale = rc.getLocale();
        ApplicationContext ctx = getApplicationContext();
        ResourceBundleMessageSource messages =
        (ResourceBundleMessageSource) ctx.getBean("messageSource");
        String message =
        AircellServletUtils.resolveErrorAsMessage(error, messages, locale);
        return message;
    }

    /**. . @see AircellServletUtils#isAjax(HttpServletRequest) */
    public boolean isAjax(HttpServletRequest request) {
        return AircellServletUtils.isAjax(request);
    }

    /**. . @see AircellServletUtils#isValidateOnly(HttpServletRequest) */
    public boolean isValidateOnly(HttpServletRequest request) {
        return AircellServletUtils.isValidateOnly(request);
    }

    /**. . @see AircellServletUtils#isMobileDevice(HttpServletRequest) */
    public boolean isMobile(HttpServletRequest request) {
        return AircellServletUtils.isMobileDevice(request);
    }

    /**. .
     * @see AircellServletUtils#isSupportedMobileDevice(HttpServletRequest) */
    public boolean isSupportedMobile(HttpServletRequest request) {
        return AircellServletUtils.isSupportedMobileDevice(request);
    }
    
    public boolean isXmlAjax(HttpServletRequest request) {
    	return AircellServletUtils.isXMLAjax(request);
    }
    
    public boolean isJsonpAjax(HttpServletRequest request) {
    	return AircellServletUtils.isJsonAjax(request);
    }

    // ****************************************************************
    // *** Spring injected properties *********************************
    // ****************************************************************
    /**. . @return the configService */
    public ConfigService getConfigService() {
        return configService;
    }

    /**. . @param configService the configService to set */
    public void setConfigService(final ConfigService configService) {
        this.configService = configService;
    }

    /**. . @return the sessionInfoService */
    public SessionInfoService getSessionInfoService() {
        return sessionInfoService;
    }

    /**. . @param sessionInfoService the sessionInfoService to set */
    public void setSessionInfoService(SessionInfoService sessionInfoService) {
        this.sessionInfoService = sessionInfoService;
    }

    /**. . @return the userDetailsService */
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    /**. . @param userDetailsService the userDetailsService to set */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**. . @return the failureView */
    public String getFailureView() {
        return failureView;
    }

    public void setFailureView(String failureView) {
        this.failureView = failureView;
    }

    public String getJsonError() {
        return jsonError;
    }

    /**. . @param jsonError the jsonError to set */
    public void setJsonError(String jsonError) {
        this.jsonError = jsonError;
    }

    public String getJsonSuccess() {
        return jsonSuccess;
    }

    /**. . @param jsonSuccess the jsonSuccess to set */
    public void setJsonSuccess(String jsonSuccess) {
        this.jsonSuccess = jsonSuccess;
    }

    /**. . @return the mobileError */
    public String getMobileError() {
        return mobileError;
    }

    /**. . @param mobileError the mobileError to set */
    public void setMobileError(String mobileError) {
        this.mobileError = mobileError;
    }

    /**. . @return the mobileSuccess */
    public String getMobileSuccess() {
        return mobileSuccess;
    }

    /**. . @param mobileSuccess the mobileSuccess to set */
    public void setMobileSuccess(String mobileSuccess) {
        this.mobileSuccess = mobileSuccess;
    }

    /**. . @return the successView */
    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    /**. . @return the alternateViews */
    public Map<String, String> getAlternateViews() {
        return alternateViews;
    }

    /**. . @param alternateViews the alternateViews to set */
    public void setAlternateViews(Map<String, String> alternateViews) {
        this.alternateViews = alternateViews;
    }

	public String getXmlSuccess() {
		return xmlSuccess;
	}

	public void setXmlSuccess(String xmlSuccess) {
		this.xmlSuccess = xmlSuccess;
	}

	public String getXmlError() {
		return xmlError;
	}

	public void setXmlError(String xmlError) {
		this.xmlError = xmlError;
	}

	public String getJsonpSuccess() {
		return jsonpSuccess;
	}

	public void setJsonpSuccess(String jsonpSuccess) {
		this.jsonpSuccess = jsonpSuccess;
	}

	public String getJsonpError() {
		return jsonpError;
	}

	public void setJsonpError(String jsonpError) {
		this.jsonpError = jsonpError;
	}
}
