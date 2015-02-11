package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.utils.AircellServletUtils;

/**.
 * This controller class is used to forward to technical error page on
 * activation failure and to forward to amp connecting page to retry activating
 * the service when the retry button on technical error page is clicked.
 * @author gajender.vattem
 */

public class TechnicalErrorController extends AircellParameterizableViewController {

    private String abpWebappHome;
    private String gbpWebappHome;
    private Logger logger = LoggerFactory.getLogger(getClass());

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
        logger.debug("TechnicalErrorController.handler ... Start");

        ModelAndView mv = null;

        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("abpWebappHome", getAbpWebappHome());
            model.put("gbpWebappHome", getGbpWebappHome());
            mv = new ModelAndView(this.getViewName(request), model);
            AircellServletUtils.addDataToModel(request, mv);

        } catch (Exception e) {
            logger.error(
            "TechnicalErrorController.handler :"
            + " Exception occured while handling technical error ",
            e
            );
        }
        logger.debug("TechnicalErrorController.handler  ....End");

        return mv;
    }

    public String getAbpWebappHome() {
        return this.abpWebappHome;
    }

    public void setAbpWebappHome(String abpWebappHome) {
        this.abpWebappHome = abpWebappHome;
    }

    public String getGbpWebappHome() {
        return this.gbpWebappHome;
    }

    public void setGbpWebappHome(String gbpWebappHome) {
        this.gbpWebappHome = gbpWebappHome;
    }
}
