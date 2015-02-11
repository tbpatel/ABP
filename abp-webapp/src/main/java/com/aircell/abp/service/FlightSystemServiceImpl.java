package com.aircell.abp.service;

import com.aircell.abp.model.FlightSystem;
import com.aircell.abs.acpu.common.SystemStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * Implementation of the ABSSystemService interface that gets the status of the
 * flight systems at the current moment in time from a JMS Queue
 * @author jon.boydell at AKQA Ltd.
 * @see com.aircell.abp.service.FlightSystemService#getCurrentStatus
 */
public class FlightSystemServiceImpl implements FlightSystemService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String flightSystemJmsPropertyKey;
    private String flightSystemJmsPropertyValue;
    private JmsSynchOperations jmsSyncService;
    /** variable to instantiate absWhitelistVersion .**/
    private String absWhitelistVersion;

    /**.
     * Determines whether this class's dependencies have been set correctly
     * @throws IllegalArgumentException If they haven't
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (jmsSyncService == null) {
            throw new IllegalArgumentException(
            "A non-null jmsSyncService must " + "be provided."
            );
        }
        if (flightSystemJmsPropertyKey == null) {
            throw new IllegalArgumentException(
            "A non-null flightSystemPropertyKey must " + "be provided."
            );
        }
        if (flightSystemJmsPropertyValue == null) {
            throw new IllegalArgumentException(
            "A non-null flightSystemPropertyValue must " + "be provided."
            );
        }
    }

    /**.
     * Returns the current status of the critical flight systems
     * @throws ServiceException if null is returned by the internal JMS message
     * exchanger
     */
    public FlightSystem getCurrentStatus() {
        logger.debug("FlightSystemServiceImple.getCurrentStatus: entered");
        final SystemStatus result = (SystemStatus) jmsSyncService
        .exchangeObjMsgOverTempQueue(
        null, getFlightSystemJmsPropertyKey(), getFlightSystemJmsPropertyValue()
        );

        if (!(result instanceof SystemStatus)) {
            throw new ServiceException(
            "FlightSystemStatus request failed, returned null"
            );
        }
        if(result != null){
            result.getABSWhitelistVersion();
        }
        final FlightSystem response = new FlightSystem();

        response.setAtgLinkStatus(result.getAtgLinkStatus());
        response.setAbsServiceStatus(result.getAbsServiceStatus());
        response.setNoOfActiveSubscribers(result.getNoOfSubscribedUsers());
        response.setAcpuApplicationVersion(result.getAcpuApplicationVersion());
        // Added fro whitelist version        
        response.setAbsWhitelistVersion(result.getABSWhitelistVersion());
        
        logger.debug(
        "FlightSystemServiceImple.getCurrentStatus   ATG Link status : "
        + result.getAtgLinkStatus() + " ,   ABS Status : "
        + result.getAbsServiceStatus() + " ,   Active Subscribers : "
        + result.getNoOfSubscribedUsers() + " - exiting" + result.getABSWhitelistVersion() + "whitelistverion no"  
        );
        logger.debug("AcpuApplicationVersion:- "+ response.getAcpuApplicationVersion());
        return response;
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    public String getFlightSystemJmsPropertyKey() {
        return flightSystemJmsPropertyKey;
    }

    /**.
     * Overrides the key used to get the flight system state from the received
     * JMS Message
     * @param flightSystemJmsPropertyKey JMS message property key
     */
    public void setFlightSystemJmsPropertyKey(
    final String flightSystemJmsPropertyKey
    ) {
        this.flightSystemJmsPropertyKey = flightSystemJmsPropertyKey;
    }

    public String getFlightSystemJmsPropertyValue() {
        return flightSystemJmsPropertyValue;
    }

    /**.
     * Overrides the value sent to the flight system state queue to elicit a
     * response containing the flight system state
     * @param flightSystemJmsPropertyValue JMS message value for the
     * flightSystemJmsPropertyKey
     */
    public void setFlightSystemJmsPropertyValue(
    final String flightSystemJmsPropertyValue
    ) {
        this.flightSystemJmsPropertyValue = flightSystemJmsPropertyValue;
    }

    /**.
     * Returns the JMS service responsible for exchanging messages
     * @return JMS service
     */
    public JmsSynchOperations getJmsSyncService() {
        return jmsSyncService;
    }

    /**.
     * Sets the JMS service responsible for exchanging messages
     * @param jmsSyncService JMS service
     */
    public void setJmsSyncService(final JmsSynchOperations jmsSyncService) {
        this.jmsSyncService = jmsSyncService;
    }
}
