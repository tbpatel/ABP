/**..
 *
 */
package com.aircell.abp.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.utils.AircellUtils;
import com.aircell.abp.utils.AircellServletUtils;

/**.
 * Aircell implementation of ParameterizableViewController
 * @author AKQA - bryan.swift
 * @version $Revision: 3641 $
 */
public class AircellParameterizableViewController
extends ParameterizableViewController {
    /**. Additional data to add to model */
    private Map<String, Object> model;
    /**. Configurable view for mobile devices */
    private String mobileView;
    
    /**.
     * Flight
     */
    private  Flight flight = null;
  
    /**.
     * Over-ridden to add flight to model
     * @see org.springframework.web.servlet.mvc.ParameterizableViewController#
     * handleRequestInternal(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final ModelAndView handleRequestInternal(
    HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        logger.debug("AircellParameterizableViewController"
            + ".handleRequestInternal: "
        + AircellServletUtils.getIpAddress(request) + ": handling request"
        );

        Map<String, Object> model = getModel();


        ModelAndView mv = handler(request, response);
        if (mv == null) {
            mv = super.handleRequestInternal(request, response);
            String viewName = getViewName(request);
            mv.setViewName(viewName);
            AircellServletUtils.addDataToModel(request, mv);
        }
        setAirlineCodeReqAttribute(request);
        if (model != null) {
            mv.addAllObjects(model);
        }
        return mv;
    }

    /**.
     * Available handler method which can be over-ridden in subclasses. If
     * over-ridden will return the result of handler rather than the result of
     * parent's handleRequestInternal - if over-ridden it is up to handler to
     * call AircellServletUtils.addDataToModel if needed
     * @param request - the submitted request
     * @param response - the provided response
     * @return ModelAndView if defined, null otherwise
     */
    protected ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response
    ) {
        return null;
    }

    /**.
     * Retrieves the viewName based on properties in the request
     * @param request submitted
     * @return viewName corresponding to the request
     */
    protected String getViewName(HttpServletRequest request) {
        String viewName;
        if (AircellServletUtils.isMobileDevice(request) && StringUtils.isNotBlank(getMobileView())) {
            viewName = getMobileView();
        } else {
            viewName = getViewName();
        }
        return viewName;
    }

    public String getMobileView() {
        return mobileView;
    }
    /**
     * @param mobileView the mobileView to set
     */
    public void setMobileView(String mobileView) {
        this.mobileView = mobileView;
    }
    
    /**. @return the model */
    public Map<String, Object> getModel() {
        return model;
    }

    /**. @param model the model to set */
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
    /** . Getter for Flight
     * @return flight Flight
     */
    public Flight getFlight() {
		return flight;
	}

	/** . Setter for Flight
	 * @param flight Flight
	 */
	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	/**.
     * sets the airlineCode as request attribute
     * @param HttpServletRequest request
     */
	protected void setAirlineCodeReqAttribute(HttpServletRequest request) {
		if(getFlight() != null && getFlight().getAirlineCode() != null) {
        	request.setAttribute(AircellUtils.AIRLINE_CODE, getFlight().getAirlineCode());
        }
		logger.info("AircellParameterizableViewController"
	            + ".setAirlineCodeReqAttribute: AIRLINE_CODE : "
	        + request.getAttribute(AircellUtils.AIRLINE_CODE));
	}

}
