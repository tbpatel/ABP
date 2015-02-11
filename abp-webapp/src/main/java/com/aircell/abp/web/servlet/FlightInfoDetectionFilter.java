package com.aircell.abp.web.servlet;

import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.backchannel.BackChannelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**.
 * . Filter class that guarantees to be just executed once per request, on any
 * servlet container.
 * @author Oscar.Diaz
 */
public class FlightInfoDetectionFilter extends OncePerRequestFilter {
    /**. . Logger */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**. . REGEX pattern for filter mappings to exclude */
    private String excludePattern;
    /**. . Regex url for filter mappings to exclude */
    private String excludeUrl;

    /**.
     * . Default URL to be forwarded to if the Flight info is not in the
     * session
     */

    private String defaultUrl;

    // This method will be executed once per request
    // and only if the current request hasnt been filtered before
    @Override
    /**This method performs the filtering task
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     *  */
    protected void doFilterInternal(
    HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain
    ) throws ServletException, IOException {


// proceed to check for Flight info in the session
        HttpSession session = request.getSession(false);
        //logger.debug("Session ID on FlightInfoDetectionFilter: "+session);
        if (session != null) {
            // It means that there is a session already created
            //
            Object flight =
            session.getAttribute(BackChannelUtils.ABP_FLIGHT_INFO);
            if (flight != null && flight instanceof FlightInformation) {
            //Roaming Partner Changes starts
            if (request.isRequestedSessionIdFromURL()) {
                     Cookie sessionCookie = new Cookie("JSESSIONID",
                     session.getId());
                     sessionCookie.setPath("/");
                     response.addCookie(sessionCookie);
                 }
            //Roaming Partner Changes ends
            // there is already an instance of flight info
                filterChain.doFilter(request, response);
                return;
            } else {
                // session exists but with not flight info
                // redirect the request to the default ABP url
                response.sendRedirect(this.getDefaultUrl());
                return;
            }
        } else {
            // this is a completely new session and must be redirected
            // to ABP default URL
            response.sendRedirect(this.getDefaultUrl());
            logger.debug("FlightInfoDetectionFilter.doFilterInternal:"
                    + " redirecting to " + getDefaultUrl());
            return;
        }
    }

    /**.
     * . Can return true to avoid filtering of the given request. The default
     * implementation always returns false. Can be overridden in subclasses for
     * custom filtering control.
     * @param request current HTTP request
     * @return whether the given request should &lti&gtnot</i> be filtered
     * @throws ServletException in case of errors
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    throws ServletException {

        String path = request.getServletPath();
        if (path.matches(excludePattern)) {
            return true;
        }
        return false;
    }

    /**. . @return the excludePattern */
    public String getExcludePattern() {
        return excludePattern;
    }

    /**. . @param excludePattern the excludePattern to set */
    public void setExcludePattern(String excludePattern) {
        this.excludePattern = excludePattern;
    }

    /**. . @return the defaultUrl */
    public String getDefaultUrl() {
        return defaultUrl;
    }

    /**. . @param defaultUrl the defaultUrl to set */
    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    /**. . @return the excludeUrl */
    public String getExcludeUrl() {
        return excludeUrl;
    }

    /**. . @param excludeUrl the excludeUrl to set */
    public void setExcludeUrl(String excludeUrl) {
        this.excludeUrl = excludeUrl;
    }

}
