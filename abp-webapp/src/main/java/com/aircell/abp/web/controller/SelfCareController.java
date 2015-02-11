package com.aircell.abp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aircell.abp.model.AircellUser;
import com.aircell.abp.service.EncryptionService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abp.web.servlet.session.AircellSessionManager;

/**.
 * Controller for brokering requests to the MDS self-care system user must be
 * logged in for any of this to work
 * @author jon.boydell
 */
public class SelfCareController extends AbstractController {

    private static final Logger logger =
    LoggerFactory.getLogger(SelfCareController.class);

    private String echat = null;
    private String requestType;
    private String gbpMyAccount;
    private String mdsView;
    private String returnUrl;

    private EncryptionService service;


    public String getEchat() {
        return echat;
    }

    public void setEchat(String echat) {
        this.echat = echat;
    }

    @Override
    public ModelAndView handleRequestInternal(
    HttpServletRequest req, HttpServletResponse res
    ) throws Exception {

        final AircellSessionManager sessionMgr =
        AircellServletUtils.getSession(req);
        final StringBuilder clearRequest = new StringBuilder();
        AircellUser user = null;
        if (sessionMgr != null) {
            user = sessionMgr.getUser();
        }
        /**. if gbp-myaccount access, check session and user info,
         * if session && user info are null
         * forward to selfCare.do / gbpMyAccountController
         * for forced authentication.
         */
        if ("gbp-myaccount".equals(getRequestType())) {

            if (sessionMgr == null || user == null
            || (user != null && !user.isLoggedIn())
            || (user != null && StringUtils.isEmpty(user.getUsername())) || (
            sessionMgr != null && !sessionMgr.isForcedAuthenticated())) {
                // forced authentication required
                logger.info(
                AircellServletUtils.getIpAddress(req)
                + " SelfCareController.handleRequestInternal -"
                + " forced authentication required");
                return new ModelAndView(getGbpMyAccount());
            }
        }

        if (sessionMgr != null) {
            if (sessionMgr.getUser() != null) {
                clearRequest.append(user.getUsername());
                try {
                    if (!StringUtils.isEmpty(returnUrl)) {
                        clearRequest.append(",");
                        clearRequest.append(returnUrl);
                    }
                } catch (Exception e) {
                    logger.error("SelfCareController.handleRequestInternal:"
                           + " Exception.........", e);
                }
            } else {
                logger
                .warn("SelfCareController.handleRequestInternal: "
                        + "User is either null or not logged in,"
                        + " user={}", user);
            }
        }

        final String sClearRequest = clearRequest.toString();
        String encrypted = "";
        if (StringUtils.isNotEmpty(sClearRequest)) {
            final ServiceResponse<String> encryptedRequest =
            service.encryptString(clearRequest.toString());
            if (encryptedRequest.isSuccess()) {
                encrypted = encryptedRequest.getPayload();
            }
        }

        final StringBuilder urlString = new StringBuilder();
        urlString.append(mdsView);

        if (user != null) {
            urlString.append("?id=");
        }

        urlString.append(encrypted);
        logger.info(
        AircellServletUtils.getIpAddress(req)
        + " SelfCareController.handleRequestInternal - redirecting to MDS : "
        + urlString.toString()
        );
        res.sendRedirect(urlString.toString());

        return null;
    }

    public void setEncryptionService(final EncryptionService service) {
        this.service = service;
    }

    public void setMdsView(final String mdsView) {
        this.mdsView = mdsView;
    }

    public void setReturnUrl(final String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getGbpMyAccount() {
        return this.gbpMyAccount;
    }

    public void setGbpMyAccount(String gbpMyAccount) {
        this.gbpMyAccount = gbpMyAccount;
    }
}
