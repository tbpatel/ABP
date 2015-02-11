package com.aircell.abp.model;

import com.aircell.abp.service.ATGSessionService;
import com.aircell.abp.service.LoginService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.SessionInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;

/**.
 * AirPassenger bean
 *
 */
@Configurable
public class AirPassenger implements Serializable {
    /**. private class variable serialVersionUID */
    private static final long serialVersionUID = 2735999621508491451L;
    /**. private class variable atgSessionService */
    private ATGSessionService atgSessionService;
    /**. private class variable captchaPassed */
    private boolean captchaPassed;
    /**. private class variable userMac */
    private String macAddress;    
    /**. private class variable loginDetails */
    private LoginDetails loginDetails;
    /**. private class variable loginService */
    private LoginService loginService;
    /**. private class variable sessionInfoService */
    private SessionInfoService sessionInfoService;
    /**. private class variable logger */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**. Default, empty constructor */
    public AirPassenger() {
    }

    /**.
     * Parameterized constructor.
     * @param details <code>Logindetails</code> bean
     * @param ip IP of the user
     */
    public AirPassenger(LoginDetails details, String ip) {
        this.loginDetails = details;
        this.loginDetails.setIpAddress(ip);
    }

    /**.
     * Ends the ATG session
     * @throws IllegalStateException thrown when no active session found
     */
    public void endATGSession() throws IllegalStateException {
        if (!(getSession().isActivated())) {
            throw new IllegalStateException(
            "Did not end session since no active session was found"
            );
        }
        getAtgSessionService()
        .sendEndATGSessionMsg(getLoginDetails().getIpAddress());
    }

    /**.
     * Ends the ATG session
     * @param ip IP of user
     * @throws IllegalStateException thrown when no active session found
     * for the specified IP
     */
    public void endATGSession(String ip) throws IllegalStateException {
        if (!(getSession(ip).isActivated())) {
            logger.error("No active session was found IP : {}", ip);
            return;
        }
        getAtgSessionService().sendEndATGSessionMsg(ip);
    }

    /**.
     * Gets AtgSessionService
     * @return ATGSessionService
     */
    public ATGSessionService getAtgSessionService() {
        return atgSessionService;
    }

    /**.
     * Gets <code>LoginDetails</code> bean.
     * @return LoginDetails <code>LoginDetails</code> bean
     */
    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    /**.
     * Gets LoginService
     * @return LoginService
     */
    public LoginService getLoginService() {
        return loginService;
    }

    /**.
     * retrieves info about the users connectivity
     *  session such as remaining time
     * of session. This is mainly used by status
     *  tray but also gives some states
     * (subscribed, authenticated etc) that are
     * useful elsewhere (for example the
     * startATGSession above use it to make sure it is in the right state)
     * @return The internet connectivity session for the user
     */
    public UserFlightSession getSession() {
        UserFlightSession retval = getSessionInfoService().getSession(
        getLoginDetails().getUsername(), getLoginDetails().getIpAddress()
        );
        if (null == retval) {
            throw new NullPointerException(
            "Could not retrive user flight session for user "
            + getLoginDetails().getUsername()
            );
        }
        return retval;
    }

    /**.
     * retrieves info about the users connectivity
     * session. This is mainly used to
     * know is there any active session
     * @param ipAddress user ip address
     * @return The internet connectivity session for the user
     */
    public UserFlightSession getSession(String ipAddress){
        UserFlightSession retval =
        getSessionInfoService().getSession(null, ipAddress);
        return retval;
    }

    /**.
     * Gets SessionInfoService
     * @return SessionInfoService
     */
    public SessionInfoService getSessionInfoService() {
        return sessionInfoService;
    }

    /**.
     * Checks if captcha is passed or not
     * @return boolean
     */
    public boolean isCaptchaPassed() {
        return captchaPassed;
    }

    /**
     * . Gets macAddress
     * @return String macAddress
     */
    public String getMacAddress() {
        return this.macAddress;
    }

    /**
     * . Sets macAddress
     * @param macAddress
     */
    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }     
    
    /**.
     * Sets <code>AtgSessionService</code>
     * @param atgSessionService atgSessionService object
     */
    public void setAtgSessionService(
    final ATGSessionService atgSessionService
    ) {
        this.atgSessionService = atgSessionService;
    }

    /**.
     * Sets true if captcha is passed.
     * Else sets false.
     * @param captchaPassed <code>true</code> if
     * captcha is passed, otherwise <code>false</code>
     */
    public void setCaptchaPassed(boolean captchaPassed) {
        this.captchaPassed = captchaPassed;
    }

    /**.
     * Sets <code>LoginDetails</code>
     * @param loginDetails <code>LoginDetails</code> bean.
     */
    public void setLoginDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    /**.
     * Sets <code>LoginService</code>
     * @param loginService <code>LoginService</code> bean
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**.
     * Sets <code>SessionInfoService</code>
     * @param service <code>SessionInfoService</code> bean
     */
    public void setSessionInfoService(final SessionInfoService service) {
        sessionInfoService = service;
    }

    /**.
     * Use to start the internet connectivity for a user. A subscription need to
     * have been provisioned previously using BSS
     * @param captchaPassed boolean value
     * @return just success or failure
     * @throws IllegalStateException
     */
    public ServiceResponse startATGSession(boolean captchaPassed)
    throws IllegalStateException {
        UserFlightSession session = getSession();

        if (!session.isAuthenticated()) {
            ServiceResponse loginResult = login();
            if (null == loginResult || !loginResult.isSuccess()) {
                return loginResult;
            }
        }

        final ServiceResponse response =
        getAtgSessionService().sendStartATGSessionMsg(
        getLoginDetails(), getLoginDetails().getIpAddress()
        );

        return response;
    }

    /**.
     * Method to login on the plane. This connects to the airborne systems to
     * establish a user session. Only used from startATGSession. Other classes
     * should use AircellUsers log in from the ground.
     * @return ServiceResponse
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    protected ServiceResponse login()
    throws IllegalArgumentException, IllegalStateException {

        if (loginDetails == null
        || StringUtils.isBlank(getLoginDetails().getUsername())
        || StringUtils.isBlank(getLoginDetails().getPassword()) || StringUtils
        .isBlank(getLoginDetails().getIpAddress())) {
            throw new IllegalArgumentException(
            "None of the args " + "for the login() can be null"
            );
        }

        if (getLoginService() == null) {
            throw new IllegalStateException(
            "loginService not configured. " + "Is AOP weaving configured ok?"
            );
        }

        final ServiceResponse retval = getLoginService().login(loginDetails);

        return retval;
    }
}
