/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.ApplicationState;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abp.web.servlet.session.AircellSessionManager;


/**.
 * .
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class JavascriptDetectionController
extends AircellParameterizableViewController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**.
     * .
     * @see
     * com.aircell.shared.web.servlet.mvc.AircellParameterizableViewController#
     * handler(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response
    ) {
        logger.debug("JavascriptDetectionController.handler" + "...Enters");
        if (AircellServletUtils.isAjax(request)) {
            AircellSessionManager session =
            AircellServletUtils.getSession(request);
            if (session != null) {
                ApplicationState state = session.getState();
                state.setJavascriptEnabled(true);
                logger.info("JavascriptDetectionController.handler: "
                + "State is Java Script Enabled ::"
                + state.isJavascriptEnabled()
                );
            }
        }
        logger.debug(
        "JavascriptDetectionController.handler javascript enabled:{}"
        + "...Exits"
        );
        return super.handler(request, response);
    }

}
