package com.aircell.abp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aircell.abp.model.AirLineInformation;




/**
 * The Class MultiAirlineController.
 */
public class MultiAirlineController extends AbstractController {

 /** Private String variable to hold abp default pg. */
 private String abpDefaultPg;

 /** Private AirLineInformation object to hold airline info. */
 private AirLineInformation airlineInfo;


    /**
     * Gets the abp default pg.
     *
     * @return the abp default pg
     */
    public String getAbpDefaultPg() {
        return abpDefaultPg;
    }

    /**
     * Sets the abp default pg.
     *
     * @param abpDefaultPg the new abp default pg
     */
    public void setAbpDefaultPg(String abpDefaultPg) {
        this.abpDefaultPg = abpDefaultPg;
    }


    /**.
     * handleRequestInternal method
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return ModelAndView object
     * @throws Exception the exception
     */
    public ModelAndView handleRequestInternal(
        HttpServletRequest req, HttpServletResponse res) throws Exception {

        logger.info("MultiAirlineController.handleRequestInternal:: Enters..");
        String redirectUrl = this.getAbpDefaultPg();
        AirLineInformation airlineInfo = getAirlineInfo();
        logger.debug(" request get URl " + req.getRequestURL());
        airlineInfo.setAirlineCode(req.getParameter("airlineCode"));
        airlineInfo.setSessionId(req.getSession().getId());
        logger.debug(" airlineInfo.setAirlineCode"
            + airlineInfo.getAirlineCode());
        logger.debug(" airlineInfo.setAirlineCode"
            + airlineInfo.getSessionId());
        logger.debug("Redirecting from MultiAirlineController to =======>"
            + redirectUrl);
        res.sendRedirect(redirectUrl);
        logger.info("MultiAirlineController.handleRequestInternal::Exits....");
        return null;
        }

/**
 * Gets the airline info.
 *
 * @return the airline info
 */
public AirLineInformation getAirlineInfo() {
return airlineInfo;
}

/**
 * Sets the airline info.
 *
     * @param airlineInfo the new airline info
     */
    public void setAirlineInfo(AirLineInformation airlineInfo) {
        this.airlineInfo = airlineInfo;
    }



}
