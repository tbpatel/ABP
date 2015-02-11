package com.aircell.abp.web.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aircell.abp.backchannel.AbpBackChannelClient;
import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightGeistLibrary;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.model.rest.FlightInfo;
import com.aircell.abp.model.rest.ServiceInfo;
import com.aircell.abp.model.rest.StatusTrayWrapper;
import com.aircell.abp.model.rest.StatusTrayFlightInfo;
import com.aircell.abp.backchannel.BackChannelUtils;
import com.aircell.abp.utils.AircellServletUtils;

/**.
 * 
 * @author gvattem
 *
 *  This is a helper class to process AbsServices REST services.
 */

public class AbsServiceHelper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static List<String> flightGeistFacts;
    private static String prevDestination = "";
    private static boolean prepareToLandAlertOn = false;
    public FlightGeistLibrary flightGeistLibrary;
    private Flight flight;

    private String httpClientGbpPage = null;
    private String methodName = null;
    private String paramName = null;

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getHttpClientGbpPage() {
        return httpClientGbpPage;
    }

    public void setHttpClientGbpPage(String httpClientGbpPage) {
        this.httpClientGbpPage = httpClientGbpPage;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public FlightGeistLibrary getFlightGeistLibrary() {
        return flightGeistLibrary;
    }

    public void setFlightGeistLibrary(FlightGeistLibrary flightGeistLibrary) {
        this.flightGeistLibrary = flightGeistLibrary;
    }

    /**.
     * This method will generate statusTray response
     * 
     * @param flightInfo
     * @param ipAddress
     * @return StatusTrayWrapper
     * @throws Exception
     */
    public StatusTrayWrapper getStatusTrayResponse(final FlightInfo flightInfo,
            final String ipAddress) throws Exception{

        logger.debug("AbsServiceHelper.getStatusTrayResponse Enter ");
        StatusTrayWrapper statusTrayWrapper = new StatusTrayWrapper();
        
        ServiceInfo serviceInfo = new ServiceInfo();
        UserFlightSession flightSession =
            new AirPassenger().getSession(ipAddress);

        String prepareToLandAlert = null;
        String serviceAlert = null;
        String destination = null;
        String airline = null;

        if (flightInfo != null) {
            destination = flightInfo.getDepartureAirportCodeFaa();
            airline = flightInfo.getAirlineCodeIata();
        }
        if (destination != null
                && !prevDestination.equalsIgnoreCase(destination)) {

            logger.debug("AbsServiceHelper.getStatusTrayResponse : " + "ip : "
                    + ipAddress
                    + " prevDestination and currentDestination are different "
                    + "reinitialize prev destination, flightGeist, "
                    + "prepare to land alert : ");

            prevDestination = destination;
            flightGeistFacts = null;
            getFlight().setPrepareToLandAlert(null);
            prepareToLandAlertOn = false;

            logger.debug("AbsServiceHelper.getStatusTrayResponse " + "ip : "
                    + ipAddress
                    + " preparation for landing alert after reinitializing : "
                    + getFlight().getPrepareToLandAlert());
        }

        if (!getFlight().isServiceAvailable()) {
            logger.error("ip : " + ipAddress
                    + "AbsServiceHelper.getStatusTrayResponse : "
                    + "service is not available");
            if (!getFlight().isAbsServiceAvaiable()) {
                getFlight().setPrepareToLandAlert(null);
                prepareToLandAlertOn = false;
            }
            serviceAlert =
                "Gogo is currently unavailable. It could be for any one of "
                        + "the following reasons: your plane has dipped below 10,000 feet,"
                        + " left U.S. airspace, or a network outage has occurred. "
                        + "Please try reconnecting soon.";

            serviceInfo.setService("Inactive");

        } else {
            if (flightSession.isActivated()) {
                // user has active session.
                serviceInfo.setService("Active");
                serviceInfo.setRemaining(flightSession.getRemainingTime());
                serviceInfo.setQuality(flightSession.isActivated() ? "Good"
                        : "Bad");
                logger
                    .debug("ip : "
                            + ipAddress
                            + "  AbsServiceHelper.getStatusTrayResponse : no service alerts, "
                            + "checking for prepare to land alert");
                prepareToLandAlert = getFlight().getPrepareToLandAlert();
                if (prepareToLandAlert != null) {
                    logger
                        .info("ip : "
                                + ipAddress
                                + " AbsServiceHelper.getStatusTrayResponse  : received "
                                + "prepare to land alert - "
                                + prepareToLandAlert);

                    serviceAlert =
                        "This plane is beginning its descent for arrival at "
                                + "your destination.  When it reaches 10,000 feet "
                                + "Gogo will be unavailable.  Be sure and save your work.";

                    if (!prepareToLandAlertOn) {
                        prepareToLandAlertOn = true;

                        try {
                            setGbpPrepareToLandAlertFlag();
                        } catch (Exception e) {
                            logger
                                .error("AbsServiceHelper.getStatusTrayResponse "
                                        + "Exception occured while making call to GBP "
                                        + "Backchannel Server setGbpPrepareToLandAlertFlag : "
                                        + e);
                        }
                    }
                }
            } else {
                // user has signed out and ACPU has no session for user.
                serviceInfo.setService("Inactive");
            }
        }

        String[] alerts = null;
        if (serviceAlert != null) {
            logger
                .info("ip : "
                        + ipAddress
                        + "  AbsServiceHelper.getStatusTrayResponse : Alert exist, ");
            alerts = new String[1];
            alerts[0] = serviceAlert;
        } else {
            logger.debug("ip : " + ipAddress
                    + "  AbsServiceHelper.getStatusTrayResponse : no alerts, ");
            alerts = new String[] {};
        }
        serviceInfo.setAlerts(alerts);

        if (flightGeistFacts == null || flightGeistFacts.isEmpty()) {
            if (destination != null) {
                prevDestination = destination;
            }

            flightGeistFacts =
                this.getFlightGeistLibrary().getFlightGeistFacts(airline,
                    destination);
        }
        int factIndex =
            new Double(Math.random() * flightGeistFacts.size()).intValue();
        String gogoFact = (String) flightGeistFacts.get(factIndex);

        statusTrayWrapper.setGogoFacts(gogoFact);
        statusTrayWrapper.setServiceInfo(serviceInfo);
        statusTrayWrapper.setFlightInfo(getStatusTrayFlightInfo(flightInfo));

        logger.debug("ip : " + ipAddress
                + "  AbsServiceHelper.getStatusTrayResponse: remaining time:{}"
                + flightSession.getRemainingTime());

        logger.debug(" AbsServiceHelper.getStatusTrayResponse Exit ");

        return statusTrayWrapper;
    }

    /**.
     * This method makes http client call to third party URL and 
     * return the response as String.
     * 
     * @param request
     * @return String
     */
    public String proxyToThirdPartyURL(
            final HttpServletRequest request) {
        String responseBody = "";
        String tPartyURL = request.getParameter("tPartyURL");
        String ipAddress = AircellServletUtils.getIpAddress(request);
        if(tPartyURL == null || "".equals(tPartyURL)){
            logger.debug(ipAddress + " - AbsServiceHelper.proxyToThirdPartyURL "
                + "blank url is not acceptable ");
            return Integer.toString(HttpStatus.SC_NOT_ACCEPTABLE);
        }
        
        HttpClient client = new HttpClient();        
        GetMethod method = new GetMethod(tPartyURL);       
        
        try {
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
                      new DefaultHttpMethodRetryHandler(0, false));
              
            int statusCode = client.executeMethod(method);
            logger.info(ipAddress + "  - AbsServiceHelper.proxyToThirdPartyURL "
                  + " - Third Party HttpClient URL: " + tPartyURL 
                  + ",   Status Code = " + statusCode);
              
            if (statusCode != HttpStatus.SC_OK) {
                logger.warn(ipAddress + "  - AbsServiceHelper.proxyToThirdPartyURL "
                    + "HttpClient Method failed: " + method.getStatusText());
            } else{          
                  responseBody = method.getResponseBodyAsString();
            }
          
        } catch (HttpException he) {
            logger.error(ipAddress + "  - AbsServiceHelper.proxyToThirdPartyURL "
                + "Http error connecting to '" 
                + tPartyURL + "' : " + he);
            responseBody = Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException ioe) {
            logger.error(ipAddress + "  - AbsServiceHelper.proxyToThirdPartyURL "
                + "IOException, Unable to connect to '" 
                + tPartyURL + "'" + ioe);
            responseBody = Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error(ipAddress + "  - AbsServiceHelper.proxyToThirdPartyURL "
                + "Exception, Unable to connect to '" + tPartyURL + "'" + e);
            responseBody = Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } 
        finally {          
          method.releaseConnection();
          client = null;
        }
        return responseBody;        
    }
    
    /**.
     * This method is pass prepare to land alert info
     * to Ground.
     * 
     */
    private void setGbpPrepareToLandAlertFlag() {
        logger
            .debug("StatusTrayController.setGbpPrepareToLandAlertFlag - Start");
        try {
            AbpBackChannelClient client =
                new AbpBackChannelClient(getHttpClientGbpPage());
            String flStr =
                BackChannelUtils.encodeFlightToXML(getFlight()
                    .getFlightInformation());
            client.setGbpPrepareToLandAlertFlag(this.getMethodName(), this
                .getParamName(), flStr);
        } catch (Exception e) {
            logger.error("Exception occured while setting "
                    + "GbpPrepareToLandAlertFlag in GBP from ABP : " + e);
        }
        logger.debug("StatusTrayController.setGbpPrepareToLandAlertFlag - End");
    }

    /**.
     * This method takes FlightInfo object as input parameter and 
     * populates all the flight information to StatusTrayFlightInfo 
     * object to return it in the REST service /statusTray response.
     * StatusTrayFlightInfo is same as FlightInfo but with different
     * field names. 
     * 
     * @param fl
     * @return StatusTrayFlightInfo
     */
    private StatusTrayFlightInfo getStatusTrayFlightInfo(FlightInfo fl) {
        if (fl == null) {
            return null;
        }
        StatusTrayFlightInfo stFlightInfo = new StatusTrayFlightInfo();

        stFlightInfo.setAirlineName(fl.getAirlineName());
        stFlightInfo.setAirlineCode(fl.getAirlineCode());
        stFlightInfo.setAirlineCodeIata(fl.getAirlineCodeIata());
        stFlightInfo.setTailNumber(fl.getTailNumber());
        stFlightInfo.setFlightNumberInfo(fl.getFlightNumber());
        stFlightInfo.setFlightNumberAlpha(fl.getFlightNumberAlpha());
        stFlightInfo.setFlightNumberNumeric(fl.getFlightNumberNumeric());
        stFlightInfo.setDepartureAirportCode(fl.getDepartureAirportCode());
        stFlightInfo.setDepartureAirportCodeIata(fl
            .getDepartureAirportCodeIata());
        stFlightInfo.setOrigin(fl.getDepartureAirportCodeFaa());
        stFlightInfo.setDestinationAirportCode(fl.getDestinationAirportCode());
        stFlightInfo.setDestinationAirportCodeIata(fl
            .getDestinationAirportCodeIata());
        stFlightInfo.setDestination(fl.getDestinationAirportCodeFaa());
        stFlightInfo.setDepartureCity(fl.getDepartureCity());
        stFlightInfo.setDestinationCity(fl.getDestinationCity());
        stFlightInfo.setExpectedArrival(fl.getExpectedArrival());
        stFlightInfo.setAbpVersion(fl.getAbpVersion());
        stFlightInfo.setAcpuVersion(fl.getAcpuVersion());
        stFlightInfo.setVideoService(fl.getVideoService());

        if (fl.getFlightStatus() != null) {
            stFlightInfo.setLatitude(fl.getFlightStatus().getLatitude());
            stFlightInfo.setLongitude(fl.getFlightStatus().getLongitude());
            stFlightInfo.setAltitude(fl.getFlightStatus().getAltitude());
            stFlightInfo.setHSpeed(fl.getFlightStatus().getHSpeed());
            stFlightInfo.setVSpeed(fl.getFlightStatus().getVSpeed());
            stFlightInfo.setLocalTime(fl.getFlightStatus().getLocalTime());
            stFlightInfo.setUtcTime(fl.getFlightStatus().getUtcTime());
        }

        return stFlightInfo;
    }

}
