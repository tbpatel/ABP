package com.aircell.abp.web.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.FlightSystem;
import com.aircell.abp.model.Version;
import com.aircell.abp.model.rest.AvailableService;
import com.aircell.abp.model.rest.AvailableServicesWrapper;
import com.aircell.abp.model.rest.FlightInfo;
import com.aircell.abp.model.rest.FlightInfoWrapper;
import com.aircell.abp.model.rest.ServiceStatus;
import com.aircell.abp.model.rest.ServiceStatusWrapper;
import com.aircell.abp.model.rest.StatusTrayWrapper;
import com.aircell.abp.utils.AircellServletUtils;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;

/**.
 *
 * @author gvattem
 *
 * RESTful services implementation class
 */

@Path("/absServices/")
public class AbsServicesImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Logger partnerReqMonitor = LoggerFactory.getLogger("partnerReqMonitor");

    private Flight flight;
    private AbsServiceHelper absServiceHelper;
    private Version versionBean = null;
    private static String acpuVersion = null;

    private ArrayList<AvailableService> servicesList = null;

    private static final int RESP_CODE_SUCCESS = 200;
    private static final int RESP_CODE_FAILURE = 202;

    private int delayUsed = 0;
    private Calendar prevFlightInfoTime = null;
    private FlightInfo delayedFlightInfo = null;
    private FlightInfo currentFlightInfo = null;
    private static final int RAND_MAX = 8;
    private static final int RAND_MIN = 5;

    private static final String SERVICE_URI = "/abs/ws/absServices/";

    /**.
     * This is a /abp/ws/absServices/serviceStatus
     * RESTful service implementation method. This will return
     * ATG link status, Service Coverage Status and overall service
     * availability status in JSON format.
     *
     * @param request
     * @return ServiceStatusWrapper
     */
    @GET
    @Produces ("application/json")
    @Path("serviceStatus")
    public ServiceStatusWrapper serviceStatus(@Context HttpServletRequest request) {
		logger.debug("AbsServices.serviceStatus - Enter");
        ServiceStatusWrapper serviceStatusResp = new ServiceStatusWrapper();
		ServiceStatus serviceStatus = null;

		try{
			boolean linkStatus = false;
			boolean coverageStatus = false;

			final FlightSystem flightSystem = getFlight().getFlightSystem();

			if (AtgLinkStatusCodes.ATG_LINK_UP.equals(flightSystem
				.getAtgLinkStatus())) {
				linkStatus = true;
			}

			if (AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE.equals(flightSystem
				.getAbsServiceStatus())) {
				coverageStatus = true;
			}

			serviceStatus = new ServiceStatus();
			serviceStatus.setServiceStatus(linkStatus && coverageStatus);
			serviceStatus.setLinkStatus(linkStatus);
			serviceStatus.setServiceCoverage(coverageStatus);

			serviceStatusResp.setServiceStatus(serviceStatus);
			serviceStatusResp.setStatusCode(RESP_CODE_SUCCESS);

		}catch (Exception e){
			logger.error("AbsServices.serviceStatus - Exception occured : " + e);
			serviceStatusResp.setStatusCode(RESP_CODE_FAILURE);
		}

        logger.debug("AbsServices.serviceStatus - Exit : " + serviceStatusResp);

        return serviceStatusResp;

    }

    /**.
     * This is a /abp/ws/absServices/flightInfo/{partnerId}
     * RESTful service implementation method. This will return
     * the flight information in JSON format
     *
     * @param partnerId
     * @param request
     * @return FlightInfoWrapper
     */
    @GET
    @Produces ("application/json")
    @Path("flightInfo/{partnerId}")
    public FlightInfoWrapper flightInfo(@PathParam("partnerId") String partnerId,
            @Context HttpServletRequest request) {

        logger.debug("AbsServices.flightInfo - Enter");
		FlightInfoWrapper flightInfoResp = new FlightInfoWrapper();

		try{

			if(partnerId == null){
				flightInfoResp.setStatusCode(RESP_CODE_FAILURE);
				return flightInfoResp;
			}

		//	retrieveFlightInfo();    //retrieves the flight information if delay expired

	        StringBuffer sb = new StringBuffer();
	        sb.append("|");
            sb.append(request.getRequestURL().toString());
            sb.append("|");
            sb.append(AircellServletUtils.getIpAddress(request));
            sb.append("|");
            sb.append(partnerId);
            sb.append("|");

		    if(delayedFlightInfo != null){
				flightInfoResp.setFlightInfo(delayedFlightInfo);
				flightInfoResp.setStatusCode(RESP_CODE_SUCCESS);

	            sb.append(delayedFlightInfo.getAirlineCode());
	            sb.append(",");
	            sb.append(delayedFlightInfo.getTailNumber());
	            sb.append(",");
	            sb.append(delayedFlightInfo.getFlightNumber());
	            sb.append(",");
	            sb.append(delayedFlightInfo.getDepartureAirportCode());
	            sb.append(",");
	            sb.append(delayedFlightInfo.getDestinationAirportCode());
			}else{
				flightInfoResp.setStatusCode(RESP_CODE_FAILURE);
			}

			partnerReqMonitor.info(sb.toString());

		}catch(Exception e){
			logger.error("AbsSesrvices.flightInfo - Exception occured : " + e);
			e.printStackTrace();
			flightInfoResp.setStatusCode(RESP_CODE_FAILURE);
		}


		logger.debug("AbsSesrvices.flightInfo - Exit - " + flightInfoResp);
		return flightInfoResp;
    }

    /**.
     * This is a /abp/ws/absServices/statusTray
     * RESTful service implementation method. This will return the current
     * Status tray response that includes flight information, gogo fact and
     * service info in JSON format.
     *
     * @param request
     * @return StatusTrayWrapper
     */
    @GET
    @Produces ("application/json")
    @Path("statusTray")
    public StatusTrayWrapper statusTray(@Context HttpServletRequest request) {

        logger.debug(" AbsServices.statusTray Enter ");
        StatusTrayWrapper response = null;
        try{
            String ipAddress = AircellServletUtils.getIpAddress(request);
            retrieveFlightInfo();        //retrieves the flight information if delay expired
            response = getAbsServiceHelper().getStatusTrayResponse(currentFlightInfo, ipAddress);
            response.setStatus(RESP_CODE_SUCCESS);
        }catch(Exception e){
            logger.error("AbsServices.statusTray - Exception while processting "
                + "statusTray service : " + e);

            if(response == null){
                response = new StatusTrayWrapper();
            }
            response.setStatus(400);
        }
        logger.debug(" AbsServices.statusTray Exit ");
        return response;
    }

    /**.
     * This is a /abp/ws/absServices/listServices
     * RESTful service implementation method. This will return all the RESTful services
     * available in this class in ABS services
     *
     * @param request
     * @return AvailableServicesWrapper
     */

    @GET
    @Produces ("application/json")
    @Path("listServices")
    public AvailableServicesWrapper listServices(@Context HttpServletRequest request) {
		logger.debug("AbsSesrvices.listServices - Enter");

		AvailableServicesWrapper listServicesResp =
						new AvailableServicesWrapper();
        try {
            if (servicesList == null) {
                servicesList = new ArrayList<AvailableService>();

                Method[] methods =
                    this.getClass().getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    java.lang.annotation.Annotation[] annotations =
                        methods[i].getDeclaredAnnotations();

                    for (int j = 0; j < annotations.length; j++) {
                        if ("javax.ws.rs.Path".contains(
								annotations[j].annotationType().getName())) {
                            String annStr = annotations[j].toString();
                            try{
                                annStr = annStr.substring(
                                    annStr.indexOf("=") + 1,
                                    annStr.lastIndexOf(")"));
                            }catch (Exception e){
                                logger.debug("AbsSesrvices.listServices - "
                                    + "Exception occured while parsing annotation String : " + e);
                            }
							AvailableService availableService =
											new AvailableService();

							String url = "";
							try{
    							url = request.getRequestURL().toString();
    							url = url.substring(0, url.lastIndexOf("/"));
							}catch (Exception e){
							    url = "/abp/ws/absServices";
							}
							availableService.setName(methods[i].getName());
							availableService.setURL( url + "/" + annStr);
							servicesList.add(availableService);

                            break;
                        }
                    }
                }
            }

            listServicesResp.setStatusCode(RESP_CODE_SUCCESS);
            listServicesResp.setAvailableServices(servicesList);

        } catch (Exception e) {
            logger.error("AbsServices.listServices : "
                    + "Exception occured while processing listServices : " + e);
            listServicesResp.setStatusCode(RESP_CODE_FAILURE);
        }

        logger.debug("AbsServices.listServices...Exit");
        return listServicesResp;

    }

    /**.
     * This is a /abp/ws/absServices/json/proxyToThirdPartyURL
     * RESTful service implementation method. This will make http client call
     * to third party URL passed in tPartyURL parameter and return the response
     * in JSON format.
     *
     * @param request
     * @return String
     */

    @GET
    @Produces ("application/json")
    @Path("/json/proxyToThirdPartyURL")
    public String proxyToThirdPartyJson(@Context HttpServletRequest request) {
        logger.debug("AbsServices/proxyToThirdParty service");
        return this.getAbsServiceHelper().proxyToThirdPartyURL(request);
    }

    /**.
     * This is a /abp/ws/absServices/html/proxyToThirdPartyURL
     * RESTful service implementation method. This will make http client call
     * to third party URL passed in tPartyURL parameter and return the response
     * in html format.
     *
     * @param request
     * @return String
     */
    @GET
    @Produces ("text/html")
    @Path("/html/proxyToThirdPartyURL")
    public String proxyToThirdPartyHtml(@Context HttpServletRequest request) {
        logger.debug("AbsServices/proxyToThirdPartyHtml service");
        return this.getAbsServiceHelper().proxyToThirdPartyURL(request);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public AbsServiceHelper getAbsServiceHelper() {
        return absServiceHelper;
    }
    public void setAbsServiceHelper(AbsServiceHelper absServiceHelper) {
        this.absServiceHelper = absServiceHelper;
    }
    public Version getVersionBean(){
		return versionBean;
	}

	public void setVersionBean(Version versionBean){
		this.versionBean = versionBean;
	}

	/**.
	 * This method monitors delay time and retrieves the
	 * flight information on delay time expiration.
	 */
	private void retrieveFlightInfo(){
        boolean delayTimeExpired = false;
        boolean firstRequest = false;
        Calendar nowCalendar = Calendar.getInstance();

        if(delayedFlightInfo != null && prevFlightInfoTime != null) {
            Calendar tmpCalendar = (Calendar) prevFlightInfoTime.clone();
            tmpCalendar.add(Calendar.MINUTE, delayUsed);

            if(nowCalendar.compareTo(tmpCalendar) >= 0){
                delayTimeExpired = true;
            }
        }else{
            firstRequest = true;
        }

        if(firstRequest || delayTimeExpired ){
            FlightInformation flightInformation =
                                this.flight.getFlightInformation();

            FlightInfo tmpFlightInfo = getRestRespFlightInfo(flightInformation);

            if(tmpFlightInfo != null){
                delayedFlightInfo = tmpFlightInfo;
                prevFlightInfoTime = nowCalendar;
                delayUsed =
                    new Random().nextInt(RAND_MAX - RAND_MIN + 1) + RAND_MIN;
            }
        }
	}

	/**.
	 * This method takes FlightInformation as input parameter and
	 * populates all the flight information to FlightInfo (JAXB) object
	 * to return it in the REST service response.
	 *
	 * @param flightInformation
	 * @return FlightInfo
	 */
	private FlightInfo getRestRespFlightInfo(
           final FlightInformation flightInformation) {

       logger.debug("AbsServices.getRestRespFlightInfo - Enter");
       if(flightInformation == null){
           return null;
       }

       FlightInfo data = new FlightInfo();
       try{
           data.setAirlineName(flightInformation.getAirlineName());
           data.setAirlineCode(flightInformation.getAirlineCode());
           data.setAirlineCodeIata(flightInformation.getAirlineCodeIata());
           data.setTailNumber(flightInformation.getAircraftTailNumber());
           data.setFlightNumber(flightInformation.getFlightNumber());
           data.setFlightNumberAlpha(flightInformation.getFlightNumberAlpha());
           data.setFlightNumberNumeric(flightInformation.getFlightNumberNumeric());
           data.setDepartureAirportCode(flightInformation.getDepartureAirportCode());
           data.setDestinationAirportCode(flightInformation.getDestinationAirportCode());
           data.setDepartureAirportCodeIata(flightInformation.getDepartureAirportCodeIata());
           data.setDestinationAirportCodeIata(flightInformation.getDestinationAirportCodeIata());
           data.setDepartureAirportCodeFaa(flightInformation.getDepartureAirportCodeFaa());
           data.setDestinationAirportCodeFaa(flightInformation.getDestinationAirportCodeFaa());
           data.setDepartureCity(flightInformation.getDepartureCity());
           data.setDepartureCity(flightInformation.getDepartureCity());
           data.setExpectedArrival(flightInformation.getExpectedArrival());
           data.setFlightStatus(flightInformation.getFlightStatus());
           data.setAbpVersion(versionBean.getAbpVersionNo());

           if(acpuVersion == null){
               acpuVersion =
                   getFlight().getFlightSystem().getAcpuApplicationVersion();
           }
           data.setAcpuVersion(acpuVersion);

           if (getFlight().isVideoServiceAvailable()) {
               data.setVideoService(true);
               data.setMediaCount(getFlight().getMediaCount());
               data.setMediaTrailerCount(getFlight().getMediaTrailerFileCount());
           } else {
               data.setVideoService(false);
               data.setMediaCount("0");
               data.setMediaTrailerCount("0");
           }

       }catch(Exception e){
           logger.error("AbsServices.getRestRespFlightInfo - Exception occured "
               + " while populating flight information to FlightInfo object "
               + "to return it in flightInfo REST service response : " + e );
       }

       logger.debug("AbsServices.getRestRespFlightInfo - Exit");
       return data;
   }



	public void refreshFlightInformation(){
	    FlightInformation flightInformation = getFlight().getFlightInformation();
	    currentFlightInfo = this.getRestRespFlightInfo(flightInformation);
	}

	public void updateDelayedFlightInfo(){

	    if(delayedFlightInfo != null){
			synchronized(delayedFlightInfo){
				delayedFlightInfo = currentFlightInfo;
			}
		}else{
			delayedFlightInfo = currentFlightInfo;
		}
	}

}
