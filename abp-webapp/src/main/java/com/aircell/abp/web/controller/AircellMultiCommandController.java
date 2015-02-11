/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.utils.AircellServletUtils;


/**.
 * . Handles multiple simple command actions based on a given name
 * @author AKQA - bryan.swift
 * @version $Revision: 3641 $
 */
public class AircellMultiCommandController extends AircellCommandController {
    /**. . Name of the method to be used as the a
     * ction handler if not the default */
    private String handlerName;

    /**.
     * .
     * @see com.aircell.abp.web.controller.AircellCommandController#
     * handler(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    public ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response, Object command,
    BindException errors
    ) {

        Class controller = this.getClass();
        ModelAndView mv;
        try {
            Method handlerMethod = controller.getMethod(
            handlerName, new Class[]{
            HttpServletRequest.class, HttpServletResponse.class, Object.class,
            BindException.class
            }
            );
            Class returnType = handlerMethod.getReturnType();
            if (returnType.equals(ModelAndView.class)) {
                mv = (ModelAndView) handlerMethod.invoke(
                this, new Object[]{ request, response, command, errors }
                );
                return mv;
            }
        } catch (NoSuchMethodException nsme) {
            logger.error("AircellMultiCommandController.handler: "
            + AircellServletUtils.getIpAddress(request)
            + ": unable to find method: " + handlerName, nsme
            );
        } catch (InvocationTargetException ite) {
            logger.error("AircellMultiCommandController.handler: "
            + AircellServletUtils.getIpAddress(request)
            + ": unable to invoke method: " + handlerName, ite
            );
        } catch (IllegalAccessException iae) {
            logger.error("AircellMultiCommandController.handler: "
            + AircellServletUtils.getIpAddress(request)
            + ": unable to access method: " + handlerName, iae
            );
        }
        // if not able to use the handlerName then this is busted
        return createFailureModelAndView(request);
    }

    /**. . @return the handlerName */
    public String getHandlerName() {
        return handlerName;
    }

    /**. . @param handlerName the handlerName to set */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

}
