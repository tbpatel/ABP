/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.utils.AircellServletUtils;

/**.
 * @author AKQA - bryan.swift
 * @version $Revision: 3136 $
 */
public class StatusTrayPageController
extends AircellParameterizableViewController {
    public Flight flight;

    /**.
     * @see com.aircell.abp.web.controller.
     * AircellParameterizableViewController#
     * handler(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response
    ) {
        ModelAndView mv = null;
        try {
            //mv = new ModelAndView();
            flight = getFlight();
            final FlightInformation flightInformation =
            flight.getFlightInformation();

            String destinationAirportCode =
            flightInformation.getDestinationAirportCode();
            String departureAirportCode =
            flightInformation.getDepartureAirportCode();
            String flightNumberInfo = flightInformation.getFlightNumber();
            String airlineCode = flightInformation.getAirlineCode();

            Map<String, Object> model = new HashMap<String, Object>();

            model.put(
            "route", destinationAirportCode + " to " + departureAirportCode
            );
            model.put("flightNumberInfo", flightNumberInfo);
            model.put("airlineCode", airlineCode);

            logger.info("StatusTrayPageController.handler: "
            + "route " + destinationAirportCode + " to " + departureAirportCode
            );
            logger.info("StatusTrayPageController.handler: "
            + "flightNumberInfo " + departureAirportCode);
            logger.info("StatusTrayPageController.handler: "
            + "airlineCode " + departureAirportCode);

            String viewName = getViewName(request);
            String username = request.getParameter("username");
            mv = new ModelAndView(viewName, model);
            AircellServletUtils.addDataToModel(request, mv);
            mv.setViewName(viewName);
            mv.addObject("username", username);
            return mv;
        } catch (Exception e) {
            logger.error("StatusTrayPageController.handler:"
            + " Exception caught", e);
            return null;
        }
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(final Flight flight) {
        this.flight = flight;
    }

}
