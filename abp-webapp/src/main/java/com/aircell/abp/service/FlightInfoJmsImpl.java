package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.FlightStatus;
import com.aircell.abs.acpu.common.FlightInfo;
import com.aircell.abs.acpu.common.GpsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.             BaseJmsServiceTest
 * Implementation of FlightInfoService that gets the current flight information
 * from a JMS Queue. This class is intended for Spring injection.  In the Spring
 * configuration call this method's checkDependenciesSet method in the
 * "init-method" bean argument.
 */
public class FlightInfoJmsImpl implements FlightInfoService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String flightInfoJmsPropertyKey;
    private String flightInfoJmsPropertyValue;
    private JmsSynchOperations jmsSyncService;

    /**.
     * Checks whether this class has all its dependencies set
     * If this class is being Spring injected call this
     * method in the init-method bean argument
     * @throws IllegalArgumentException If any one of them isn't set
     */
    public void checkDependenciesSet() throws IllegalArgumentException {
        if (jmsSyncService == null) {
            throw new IllegalArgumentException(
            "A non-null jmsSyncService must " + "be provided."
            );
        }
        if (flightInfoJmsPropertyKey == null) {
            throw new IllegalArgumentException(
            "A non-null flightInfoJmsPropertyKey must " + "be provided."
            );
        }
        if (flightInfoJmsPropertyValue == null) {
            throw new IllegalArgumentException(
            "A non-null flightInfoJmsPropertyValue must " + "be provided."
            );
        }
    }

    /**.
     * Returns the status of the Flight at the current moment in
     * time from the ACPU, will not return GPS data unless
     * the GPS is ENABLED and WORKING
     * @see com.aircell.abp.service.FlightInfoService#getCurrentStatus
     * @throws ServiceException If null is returned from the ACPU
     */
    public Flight getCurrentStatus() {
        logger.debug("FlightInfoJmsImpl.getCurrentStatus: entered");
        final FlightInfo result = (FlightInfo) jmsSyncService
        .exchangeObjMsgOverTempQueue(
        null, getFlightInfoJmsPropertyKey(), getFlightInfoJmsPropertyValue()
        );

        if (null == result) {
            throw new ServiceException(
            "FlightInfo request failed, returned null"
            );
        }

        logger.info(
        "FlightInfoJmsImpl.getCurrentStatus: Recieved "
        + "flight data from ACPU, response={0} : Flight #: "
        + result.getFlightNo() + " Tail #: " + result.getTailNumber()
        + "  Aricraft type: " + result.getAircraftType()
        );

        final FlightStatus flightStatus = new FlightStatus();
        if (null != result.getGpsFeed()
        && result.getGpsFeed().getState().equals(GpsData.GpsState.ENABLED)
        && result.getGpsFeed().getHealth().equals(GpsData.GpsHealth.WORKING)) {

            flightStatus.setAltitude(result.getGpsFeed().getAltitude());
            flightStatus.setHSpeed(result.getGpsFeed().getHorizontalVelocity());
            flightStatus.setVSpeed(result.getGpsFeed().getVerticalVelocity());
            flightStatus.setLatitude(result.getGpsFeed().getLatitude());
            flightStatus.setLongitude(result.getGpsFeed().getLongitude());
            flightStatus.setUtcTime(result.getGpsFeed().getUtcTime());
            flightStatus.setLocalTime(result.getGpsFeed().getLocalTime());

        } else if (null == result.getGpsFeed()) {
            logger.info(
            "FlightInfoJmsImpl.getCurrentStatus: There is missing info "
            + "coming from ACPU: FlightInfo.getGpsFeed() is NULL"
            );
        } else if (null == result.getGpsFeed().getState() || !result
        .getGpsFeed().getState().equals(GpsData.GpsState.ENABLED)) {
            logger.info(
            "FlightInfoJmsImpl.getCurrentStatus: There is missing info "
            + "coming from ACPU: FlightInfo.getGpsFeed().getState() = "
            + result.getGpsFeed().getState()
            );
        } else if (null == result.getGpsFeed().getHealth() || !result
        .getGpsFeed().getHealth().equals(GpsData.GpsHealth.WORKING)) {
            logger.info(
            "FlightInfoJmsImpl.getCurrentStatus: There is missing info "
            + "coming from ACPU: FlightInfo.getGpsFeed().getHealth() = "
            + result.getGpsFeed().getHealth()
            );
        }

        final Flight response = new Flight();
        final FlightInformation flightInfo = new FlightInformation();
        flightInfo.setAirlineName(result.getAirlineName());
        flightInfo.setDepartureCity(result.getDepartureCity());
        flightInfo.setDepartureAirportCode(result.getDepartureAirport());
        flightInfo
        .setDepartureAirportCodeIata(result.getDepartureAirportIata());
        flightInfo.setDepartureAirportCodeFaa(result.getDepartureAirportFaa());
        flightInfo.setDestinationCity(result.getArrivalCity());
        flightInfo.setDestinationAirportCode(result.getArrivalAirport());
        flightInfo
        .setDestinationAirportCodeIata(result.getArrivalAirportIata());
        flightInfo.setDestinationAirportCodeFaa(result.getArrivalAirportFaa());
        flightInfo.setExpectedArrival(result.getExpectedArrivalTime());
        flightInfo.setFlightNumber(result.getFlightNo());
        flightInfo.setFlightNumberAlpha(result.getFlightNoAlpha());
        flightInfo.setFlightNumberNumeric(result.getFlightNoNumeric());
        flightInfo.setFlightStatus(flightStatus);
        flightInfo.setAircraftTailNumber(result.getTailNumber());
        flightInfo.setAirlineCode(result.getAirlineCode());
        flightInfo.setAirlineCodeIata(result.getAirlineCodeIata());
        flightInfo.setFlightStatus(flightStatus);
        flightInfo.setNoOfActiveSubscribers(Integer.toString(0));
        response.setFlightInformation(flightInfo);
        logger.debug("FlightInfoJmsImpl.getCurrentStatus: exiting");
        return response;
    }

    // *******************************************
    // ******** Spring injected properties *******
    // *******************************************

    public JmsSynchOperations getJmsSyncService() {
        return jmsSyncService;
    }

    public void setJmsSyncService(final JmsSynchOperations jmsSyncService) {
        this.jmsSyncService = jmsSyncService;
    }

    public String getFlightInfoJmsPropertyKey() {
        return flightInfoJmsPropertyKey;
    }

    public void setFlightInfoJmsPropertyKey(
    final String flightInfoJmsPropertyKey
    ) {
        this.flightInfoJmsPropertyKey = flightInfoJmsPropertyKey;
    }

    public String getFlightInfoJmsPropertyValue() {
        return flightInfoJmsPropertyValue;
    }

    public void setFlightInfoJmsPropertyValue(
    final String flightInfoJmsPropertyValue
    ) {
        this.flightInfoJmsPropertyValue = flightInfoJmsPropertyValue;
    }

}
