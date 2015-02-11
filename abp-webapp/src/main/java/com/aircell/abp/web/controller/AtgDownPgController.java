package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.utils.AircellUtils;


public class AtgDownPgController extends AircellParameterizableViewController {

	
	 /** . Flight object */
    private Flight flight = null;
    
    /**.
     * String
     */
    private String kuDownPg;
    
    /**. Getter flight
     * @return flight
     */
    public Flight getFlight() {
		return flight;
	}
    /**. Setter flight
     * @param flight
     */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	/**. Getter kuDownPg
     * @return kuDownPg
     */
	public String getKuDownPg() {
		return kuDownPg;
	}
	 /**. Setter kuDownPg
     * @param kuDownPg
     */
	public void setKuDownPg(String kuDownPg) {
		this.kuDownPg = kuDownPg;
	}
	/**.
     * @see com.aircell.abp.web.controller.
     * AircellParameterizableViewController#handler
     * (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override

    protected ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response
    ) {
        ModelAndView mv = null;
        try {
            logger.debug("AtgDownPgController.handler" + "...Enters");
            Map<String, Object> model = new HashMap<String, Object>();
			String flightType = AircellUtils.FLIGHT_TYPE_ATG;
            if(flight != null) {
            	FlightInformation flightInfo = flight.getFlightInformation();
            	if(flightInfo != null && flightInfo.getAirlineCode() != null) {
            		 request.setAttribute(AircellUtils.AIRLINE_CODE, flightInfo.getAirlineCode());
            	}
            	   // Setting flight type in request
                AircellUtils aUtils = new AircellUtils();
                if(getFlight() != null && getFlight().getFlightSystem() != null && getFlight().getFlightSystem().getAcpuApplicationVersion() != null) {
                	flightType = aUtils.getFlightType(getFlight().getFlightSystem().getAcpuApplicationVersion());                	
                }else{
                    logger.warn("ACPU Application Version is null");
                }
            }
            request.setAttribute(AircellUtils.FLIGHT_TYPE_STR, flightType);
            
            String serviceDownPg = null;
    		if (flightType.equalsIgnoreCase(AircellUtils.FLIGHT_TYPE_KU)) {
    			serviceDownPg = this.getKuDownPg();
    		} else {
    			serviceDownPg = getViewName(request);
    		}
            mv = new ModelAndView(serviceDownPg, model);
            
            logger.info("AtgDownPgController.handler mv ::: " + mv);

        } catch (Exception e) {
            logger.error("AtgDownPgController.handler Exception caught", e);
        }
        logger.debug("AtgDownPgController.handler" + "...Exits");
        return mv;
    }


}