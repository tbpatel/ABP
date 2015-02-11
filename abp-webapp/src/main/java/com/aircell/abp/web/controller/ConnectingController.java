package com.aircell.abp.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aircell.abp.backchannel.BackChannelUtils;

/**.
 * Created by IntelliJ IDEA. User: excelacom Date: Jul 8, 2008 Time: 3:56:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectingController extends AbstractController {

    private String abpDefaultPg;

    public static final String COOKIE_TEST_NAME = "testCookieName";
    public static final String COOKIE_TEST_VALUE = "testCookieName";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String getAbpDefaultPg() {
        return abpDefaultPg;
    }

    public void setAbpDefaultPg(String abpDefaultPg) {
        this.abpDefaultPg = abpDefaultPg;
    }

    public ModelAndView handleRequestInternal(
    HttpServletRequest req, HttpServletResponse res
    ) throws Exception {

        /*String redirectUrl = this.getAbpDefaultPg();
          logger.debug("Redirecting to =======>" + redirectUrl);
          res.sendRedirect(redirectUrl);
          return null;*/

        logger.debug(
        "ConnectingController.handleRequestInternal::Enter..."
        );
        Cookie ck = new Cookie(COOKIE_TEST_NAME, COOKIE_TEST_VALUE);
        ck.setMaxAge(-1);
        ck.setPath("/");
        res.addCookie(ck);

        String redirectUrl = this.getAbpDefaultPg();
        try {
            String flagValue = "";
            flagValue = req.getParameter("abpflg");
            if (null != flagValue && flagValue.trim() != "") {
                redirectUrl =
                redirectUrl + "?" + BackChannelUtils.ABP_FLAG + "=" + flagValue;
                redirectUrl = redirectUrl + "&splash=true";
                req.setAttribute("abpflg", flagValue);
                req.setAttribute("abpflg", flagValue);
            }
        } catch (Exception e) {
            logger.error("ConnectingController.handleRequestInternal: "
            + "Problem in the Redirecting URL", e);
        }

        logger
        .debug("ConnectingController.handleRequestInternal: "
            + "Redirecting from Connecting Controller to ....." + redirectUrl);
        res.sendRedirect(redirectUrl);
        logger.debug(
        "ConnectingController.handleRequestInternal:::Exit..............."
        );
        return null;
    }
}
