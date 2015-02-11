/*
 * StatusTrayController.java 22 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.backchannel.AbpBackChannelClient;
import com.aircell.abp.backchannel.BackChannelUtils;
import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightGeistLibrary;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abp.web.form.StatusTrayCommand;

/**.
 * Controller to return the status tray data
 * @author AKQA - bryan.swift
 * @version $Revision: 3362 $
 */
public class StatusTrayController extends AircellCommandController {

    private Flight flight;
    /**. Flight Geist Library * */
    private FlightGeistLibrary flightGeistLibrary;
    private String httpClientGbpPage = null;
    private String methodName = null;
    private String paramName = null;

    private static List<String> flightGeistFacts;
    private static String prevDestination = "";
    private static boolean prepareToLandAlertOn = false;

    /**.
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
        logger.debug("StatusTrayController.handler" + "...Enters");
        StatusTrayCommand form = (StatusTrayCommand) command;
        // set up flight info
        final Flight flight = getFlight();
        final FlightInformation flightInformation =
        flight.getFlightInformation();
        String ip = AircellServletUtils.getIpAddress(request);
        String username = form.getUsername();


        UserFlightSession flightSession = getUserFlightSession(username, ip);
        if (flight == null || flightInformation == null
        || flightSession == null) {
			logger.warn ("StatusTrayController.statusTray - "
			+ "Flight information or user flight session is null ");
            return createFailureModelAndView(request);
        }

        String origin = flightInformation.getDepartureAirportCodeFaa();
        String departureCity = flightInformation.getDepartureCity();
        String destination = flightInformation.getDestinationAirportCodeFaa();
        String destinationCity = flightInformation.getDestinationCity();
        String flightNumber = flightInformation.getFlightNumberNumeric();
        String airline = flightInformation.getAirlineCodeIata();
        String airlineName = flightInformation.getAirlineName();

        String destinationAirportCode =
        flightInformation.getDestinationAirportCode();
        String departureAirportCode =
        flightInformation.getDepartureAirportCode();
        String flightNumberInfo = flightInformation.getFlightNumber();
        String airlineCode = flightInformation.getAirlineCode();

        Map<String, Object> flightInfo = new HashMap<String, Object>();

        flightInfo.put("origin", origin);
        flightInfo.put("departureCity", departureCity);
        flightInfo.put("destination", destination);
        flightInfo.put("destinationCity", destinationCity);
        flightInfo.put("flightNumber", flightNumber);
        flightInfo.put("airline", airline);
        flightInfo.put("airlineName", airlineName);
        flightInfo.put("loggedIn", "1");
        flightInfo.put("destinationAirportCode", destinationAirportCode);
        flightInfo.put("departureAirportCode", departureAirportCode);
        flightInfo.put("flightNumberInfo", flightNumberInfo);
        flightInfo.put("airlineCode", airlineCode);
        flightInfo.put("tailNumber", flightInformation.getAircraftTailNumber());
        flightInfo.put("videoServiceAvailable",
        	Boolean.toString(flight.isVideoServiceAvailable()));
        flightInfo.put("mediaCount",flight.getMediaCount());
        flightInfo.put("mediaTrailerCount", flight.getMediaTrailerFileCount());

        if(flightInformation.getFlightStatus() != null) {

			flightInfo.put("hSpeed", flightInformation.getFlightStatus().getHSpeed());
			flightInfo.put("vSpeed", flightInformation.getFlightStatus().getVSpeed());
			flightInfo.put("latitude", flightInformation.getFlightStatus().getLatitude());
			flightInfo.put("longitude", flightInformation.getFlightStatus().getLongitude());
			flightInfo.put("altitude", flightInformation.getFlightStatus().getAltitude());
			flightInfo.put("localTime", flightInformation.getFlightStatus().getLocalTime());

			if(flightInformation.getFlightStatus().getUtcTime() != null) {
				flightInfo.put("utcTime",flightInformation.getFlightStatus().getUtcTime().toString());
			}

			if(flightInformation.getExpectedArrival() != null) {
				flightInfo.put("expectedArrival", flightInformation.getExpectedArrival().toString());
			}
		}

        Map<String, Object> serviceInfo = new HashMap<String, Object>();
        String prepareToLandAlert = null;
        String serviceAlert = null;

        logger.debug("StatusTrayController.handler"
        + " ip : " + ip + "  ---     prevDestination : " + prevDestination
        + " ,    currentDestination : " + destination
        );
        // initialize the prepare to land alert,
        // flightGeistFacts and set the prev destination if the prev
        // destination and current destination or not same
        if (destination != null && !prevDestination
        .equalsIgnoreCase(destination)) {
            logger.info(
            "ip : " + ip
            + "  ---   prevDestination and currentDestination are different "
            + "reinitialize prev destination, flightGeist, "
            + "prepare to land alert : "
            );
            prevDestination = destination;
            flightGeistFacts = null;
            flight.setPrepareToLandAlert(null);
            prepareToLandAlertOn = false;
            logger.debug(
            "ip : " + ip
            + "  ---   preparation for landing alert after reinitializing : "
            + flight.getPrepareToLandAlert()
            );
        }

        if (!flight.isServiceAvailable()) {
            logger.error(
            "ip : " + ip
            + "  ---   StatusTrayController.handler : "
            + "service is not available");
            if (!flight.isAbsServiceAvaiable()) {
                flight.setPrepareToLandAlert(null);
                prepareToLandAlertOn = false;
            }
            serviceAlert =
            "Gogo is currently unavailable. It could be for any one of "
            + "the following reasons: your plane has dipped below 10,000 feet,"
            + " left U.S. airspace, or a network outage has occurred. "
            + "Please try reconnecting soon.";
            serviceInfo.put("service", "Inactive");
        } else {
            if (flightSession.isActivated()) {
                //user has active session.
                serviceInfo.put("service", "Active");
                serviceInfo.put("remaining", flightSession.getRemainingTime());
                serviceInfo
                .put("quality", flightSession.isActivated() ? "Good" : "Bad");

                logger.debug(
                "ip : " + ip
                + "  ---   StatusTrayController.handler : no service alerts, "
                + "checking for prepare to land alert"
                );
                prepareToLandAlert = flight.getPrepareToLandAlert();
                if (prepareToLandAlert != null) {
                    logger.info(
                    "ip : " + ip
                    + "  ---   StatusTrayController.handler : received "
                    + "prepare to land alert - "
                    + prepareToLandAlert
                    );
                    serviceAlert =
                    "This plane is beginning its descent for arrival at "
                    + "your destination.  When it reaches 10,000 feet "
                    + "Gogo will be unavailable.  Be sure and save your work.";
                    if (!prepareToLandAlertOn) {
                        prepareToLandAlertOn = true;

                        try {

                            setGbpPrepareToLandAlertFlag();

                        } catch (Exception e) {
                            logger.error("StatusTrayController.handler: "
                            + "Exception occured while making call to GBP "
                            + "Backchannel Server "
                            + ".setGbpPrepareToLandAlertFlag : "
                            + e
                            );
                        }
                    }
                }
            } else {
                //user has signed out and ACPU has no session for user.
                logger.info("ip : " + ip
				+ "  - user has no active session, service is not available");
                serviceInfo.put("service", "Inactive");
            }
        }

        // alerts

        String[] alerts = null;
        if (serviceAlert != null) {
            logger.debug(
            "ip : " + ip
            + "  ---   StatusTrayController.handler : Alert exist, "
            + "create alerts array and set the value"
            );
            alerts = new String[1];
            alerts[ 0 ] = serviceAlert;
        } else {
            logger.debug(
            "ip : " + ip
            + "  ---   StatusTrayController.handler : no alerts, "
            + "creating empty array of alerts"
            );
            alerts = new String[]{};
        }
        serviceInfo.put("alerts", alerts);

        if (flightGeistFacts == null || flightGeistFacts.isEmpty()) {
            if (destination != null) {
                prevDestination = destination;
            }

            flightGeistFacts = this.getFlightGeistLibrary()
            .getFlightGeistFacts(airline, destination);
        }
        int factIndex =
        new Double(Math.random() * flightGeistFacts.size()).intValue();
        String gogoFact = (String) flightGeistFacts.get(factIndex);


        // add to context values
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("flightInfo", flightInfo);
        values.put("serviceInfo", serviceInfo);
        values.put("gogoFacts", gogoFact);

        logger.debug(
        "ip : " + ip + "  ---   StatusTrayController.handler" + "...Exits"
        );
        return createSuccessModelAndView(request, values);
    }

    /**.
     * Helper method to retrieve the UserFlightSession for the current user
     * @param username of the logged in user
     * @param ip of the logged in user's computer
     * @return UserFlightSession for the current user
     */
    private UserFlightSession getUserFlightSession(String username, String ip) {
        AirPassenger passenger =
        new AirPassenger(new LoginDetails(username, ""), ip);
        return passenger.getSession();
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(final Flight flight) {
        this.flight = flight;
    }

    public FlightGeistLibrary getFlightGeistLibrary() {
        return this.flightGeistLibrary;
    }

    public void setFlightGeistLibrary(FlightGeistLibrary flightGeistLibrary) {
        this.flightGeistLibrary = flightGeistLibrary;
    }

    public String getHttpClientGbpPage() {
        return this.httpClientGbpPage;
    }

    public void setHttpClientGbpPage(String st) {
        this.httpClientGbpPage = st;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String st) {
        this.methodName = st;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String st) {
        this.paramName = st;
    }

    private void setGbpPrepareToLandAlertFlag() {
        logger
        .debug("StatusTrayController.setGbpPrepareToLandAlertFlag - Start");
        try {
            AbpBackChannelClient client =
            new AbpBackChannelClient(getHttpClientGbpPage());
            String flStr =
            BackChannelUtils.encodeFlightToXML(flight.getFlightInformation());
            client.setGbpPrepareToLandAlertFlag(
            this.getMethodName(), this.getParamName(), flStr
            );
        } catch (Exception e) {
            logger.error("StatusTrayController.setGbpPrepareToLandAlertFlag"
            + "Exception occured while setting "
            + "GbpPrepareToLandAlertFlag in GBP from ABP : "
            + e
            );
        }
        logger.debug("StatusTrayController.setGbpPrepareToLandAlertFlag - End");
    }
}
