package com.aircell.abp.service;

import com.aircell.abp.model.UserFlightSession;
import com.aircell.abs.acpu.common.SessionInfo;
import com.aircell.abs.acpu.common.SessionInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * Implementation of a service for getting air session information using a
 * synchronised JMS queue
 * @author jon.boydell
 */
public class SessionInfoJmsImpl implements SessionInfoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String sessionInfoJmsPropertyKey;
    private String sessionInfoJmsPropertyValue;
    private JmsSynchOperations jmsSyncService;

    /**.
     * The flight session associated with the specified user and ip address
     * @param username of the user's session being requested
     * @param ip client ip address (associated with the user's machine)
     */
    public UserFlightSession getSession(
    final String username, final String ip
    ) {
        logger.debug("SessionInfoJmsImpl.getSession: entered");
        final SessionInfoRequest request = new SessionInfoRequest(ip, username);
        final SessionInfo result = (SessionInfo) jmsSyncService
        .exchangeObjMsgOverTempQueue(
        request, getSessionInfoJmsPropertyKey(),
        getSessionInfoJmsPropertyValue()
        );


        if (null == result) {
            logger.error(
            "SessionInfoJmsImpl.getSession: FlightInfo request failed, "
            + "returned null"
            );
            throw new ServiceException(
            "FlightInfo request failed, returned null"
            );
        }

        logger.debug(
        "session information from jms  result.getUserName(): "
        + "result.getIpAddress() "
        + result.getUserName() + "IP:" + result.getIpAddress()
        + " MAC Addr : " + result.getUserMac()
        );

        final UserFlightSession response = new UserFlightSession();
        if (username == null && result.getUserName() != null
        && result.getUserName().length() > 0) {
            response.setUserName(result.getUserName());
        } else {
            response.setUserName(username);
        }
        response.setUserMac(result.getUserMac());
        response.setIp(ip);
        response.setAuthenticated(Boolean.FALSE);
        response.setActivated(Boolean.FALSE);

        switch (result.getState()) {
            case UNKNOWN_USER_STATE:
                logger.error(
                "SessionInfoJmsImpl.getSession: User Session "
                + "returned unknown user state",
                result
                );
                break;  //leave states null
            case ABS_SERVICE_UNAVAILABLE:
                logger.error(
                "SessionInfoJmsImpl.getSession: ABS unavailable "
                + "when calling get session info",
                result
                );
                break;  //leave states null
            case USER_AUTHENTICATED:
                logger
                .debug("SessionInfoJmsImpl.getSession: user is authenticated");
                response.setAuthenticated(Boolean.TRUE);
                response.setActivated(Boolean.FALSE);
                break;
            case USER_NOT_AUTHENTICATED:
                logger.debug(
                "SessionInfoJmsImpl.getSession: user is not authenticated"
                );
                response.setAuthenticated(Boolean.FALSE);
                response.setActivated(Boolean.FALSE);
                break;
            case USER_SUBSCRIBED:
                logger
                .debug("SessionInfoJmsImpl.getSession: user is subscribed");
                response.setAuthenticated(Boolean.TRUE);
                response.setActivated(Boolean.TRUE);
                break;
            case USER_UNSUBSCRIBED:
                logger
                .debug("SessionInfoJmsImpl.getSession: user is unsubscribed");
                response.setActivated(Boolean.FALSE);
                break;
            case USER_LOGGED_OFF:
                logger.debug(
                "SessionInfoJmsImpl.getSession: user was logged off"
                );
                response.setAuthenticated(Boolean.FALSE);
                response.setActivated(Boolean.FALSE);
                break;
            case USER_FORCIBLY_LOGGED_OFF:
                logger.debug(
                "SessionInfoJmsImpl.getSession: user was forcibly logged off"
                );
                response.setAuthenticated(Boolean.FALSE);
                response.setActivated(Boolean.FALSE);
                break;
        }
        response.setRemainingTime(result.getRemainingTimeSeconds());
        logger.debug("SessionInfoJmsImpl.getSession: exiting");
        return response;
    }

    /**.
     * Tests whether the properties this class depends on are set correctly
     * @throws IllegalArgumentException if the properties this class depends on
     * are null
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (jmsSyncService == null) {
            throw new IllegalArgumentException(
            "A non-null jmsSyncService must " + "be provided."
            );
        }
        if (sessionInfoJmsPropertyKey == null) {
            throw new IllegalArgumentException(
            "A non-null flightInfoJmsPropertyValue must " + "be provided."
            );
        }
        if (sessionInfoJmsPropertyValue == null) {
            throw new IllegalArgumentException(
            "A non-null flightInfoJmsPropertyValue must " + "be provided."
            );
        }
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    /**.
     * Returns the message property key used to retrieve user session info
     * @return
     */
    public String getSessionInfoJmsPropertyKey() {
        return sessionInfoJmsPropertyKey;
    }

    /**.
     * Sets the message property key used to retrieve user session info
     * @param sessionInfoJmsPropertyKey
     */
    public void setSessionInfoJmsPropertyKey(
    final String sessionInfoJmsPropertyKey
    ) {
        this.sessionInfoJmsPropertyKey = sessionInfoJmsPropertyKey;
    }

    /**.
     * Returns the message property value used to retrieve user session info
     * @return
     */
    public String getSessionInfoJmsPropertyValue() {
        return sessionInfoJmsPropertyValue;
    }

    /**.
     * Sets the message property value used to retrieve user session info
     * @param sessionInfoJmsPropertyValue
     */
    public void setSessionInfoJmsPropertyValue(
    final String sessionInfoJmsPropertyValue
    ) {
        this.sessionInfoJmsPropertyValue = sessionInfoJmsPropertyValue;
    }

    /**.
     * Returns the Synchronised JMS message exchange service
     * @return
     */
    public JmsSynchOperations getJmsSyncService() {
        return jmsSyncService;
    }

    /**.
     * Sets the Synchronised JMS message exchange service
     * @param jmsSyncService
     */
    public void setJmsSyncService(final JmsSynchOperations jmsSyncService) {
        this.jmsSyncService = jmsSyncService;
    }
}
