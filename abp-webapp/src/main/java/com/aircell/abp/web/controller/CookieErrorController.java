package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abp.web.servlet.session.AircellSessionManager;



public class CookieErrorController
extends AircellParameterizableViewController {
    Flight flight = null;

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
            logger.debug("CookieErrorController.handler" + "...Enters");
            AircellSessionManager session =
            AircellServletUtils.getSession(request);
            FlightInformation flightInfo = getFlight().getFlightInformation();
            Map<String, Object> model = new HashMap<String, Object>();
            if (session != null) {
                model.put("loggedin", "1");
            } else {
                model.put("loggedin", "0");
            }
            if (flightInfo != null) {
                logger.info(
                "\n\n CookieErrorController.handler AIRLINE CODE ::: "
                    + flightInfo.getAirlineCode()
                );
                logger.info(
                "\n\n CookieErrorController.handler FLIGHT NUMBER ::: "
                    + flightInfo.getFlightNumber()
                );
                logger.info(
                "\n\n CookieErrorController.handler ROUTE ::: "
                + flightInfo.getDepartureAirportCode() + " to " + flightInfo
                .getDestinationAirportCode()
                );

                model.put("airlinecode", flightInfo.getAirlineCode());
                model.put("flightno", flightInfo.getFlightNumber());
                model.put(
                "route",
                flightInfo.getDepartureAirportCode() + " to " + flightInfo
                .getDestinationAirportCode()
                );
                String viewName = getViewName(request);
                mv = new ModelAndView(viewName, model);
                logger.info("CookieErrorController.handler mv ::: " + mv);
            } else {
                logger.info("CookieErrorController.handler: "
                          + "There is no flight info");
            }
        } catch (Exception e) {
            logger.error("CookieErrorController.handler: Exception caught", e);
        }
        logger.debug("CookieErrorController.handler" + "...Exits");
        return mv;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
  }

}