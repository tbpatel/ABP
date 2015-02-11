

package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**.
 * This controller is used to redirect  to the squid error page when there is an squid error.
 * @author Muthuselvi
 *
 */
public class SquidErrorController extends AircellParameterizableViewController {

    /**.
     * @see com.aircell.abp.web.controller.
     * AircellParameterizableViewController#handler
     * (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */

    /**.
     * handler Method
	 * @ return mv Model and View
     */
    protected ModelAndView handler(HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView mv = null;
        try {
            logger.debug("SquidErrorController.handler" + "...Enters");
            Map<String, Object> model = new HashMap<String, Object>();

            String viewName = getViewName(request);

            mv = new ModelAndView(viewName, model);
            logger.debug("SquidErrorController.handler mv ::: "
                + mv);

        } catch (Exception e) {
            logger.error("Exception caught", e);
        }
        logger.debug("SquidErrorController.handler" + "...Exits");
        return mv;
    }

}
