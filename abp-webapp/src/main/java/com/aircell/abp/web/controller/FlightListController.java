package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.utils.AircellServletUtils;

public class FlightListController extends AircellCommandController {

	private Flight flight;

	@Override
	public ModelAndView handler(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		// TODO Auto-generated method stub
		final String ipAddress = AircellServletUtils.getIpAddress(request);
		AirPassenger passenger = new AirPassenger();
		String macAddress = "";
		final Flight flight = getFlight();

		FlightInformation flightInformation = null;
		UserFlightSession session = passenger.getSession(ipAddress);

		if (flight != null) {
			
			flightInformation = flight.getFlightInformation();
		}

		if (flight == null || flightInformation == null || session == null) {
			logger.warn("StatusTrayController.statusTray - "
					+ "Flight information or user flight session is null ");
			return createFailureModelAndView(request);
		} else {
			macAddress = session.getUserMac() != null ? session.getUserMac()
					: "";
			logger.debug("MAC Address:::" + macAddress);

			Map<String, Object> flightInfo = new HashMap<String, Object>();

			flightInfo.put("origin",
					flightInformation.getDepartureAirportCodeFaa());
			flightInfo.put("departureCity",
					flightInformation.getDepartureCity());
			flightInfo.put("destination",
					flightInformation.getDestinationAirportCodeFaa());
			flightInfo.put("destinationCity",
					flightInformation.getDestinationCity());
			flightInfo.put("flightNumber",
					flightInformation.getFlightNumberNumeric());
			flightInfo.put("airline", flightInformation.getAirlineCodeIata());
			flightInfo.put("airlineName", flightInformation.getAirlineName());
			flightInfo.put("loggedIn", "1");
			flightInfo.put("destinationAirportCode",
					flightInformation.getDestinationAirportCode());
			flightInfo.put("departureAirportCode",
					flightInformation.getDepartureAirportCode());
			flightInfo.put("flightNumberInfo",
					flightInformation.getFlightNumber());
			flightInfo.put("airlineCode", flightInformation.getAirlineCode());
			flightInfo.put("tailNumber",
					flightInformation.getAircraftTailNumber());
			flightInfo.put("videoService",
					flightInformation.getVideoServiceAvailability());
			flightInfo.put("mediaCount", flightInformation.getMediaCount());
			flightInfo.put("mediaTrailerCount",
					flightInformation.getMediaTrailerCount());

			if (flightInformation.getFlightStatus() != null) {

				flightInfo.put("hSpeed", flightInformation.getFlightStatus()
						.getHSpeed());
				flightInfo.put("vSpeed", flightInformation.getFlightStatus()
						.getVSpeed());
				flightInfo.put("latitude", flightInformation.getFlightStatus()
						.getLatitude());
				flightInfo.put("longitude", flightInformation.getFlightStatus()
						.getLongitude());
				flightInfo.put("altitude", flightInformation.getFlightStatus()
						.getAltitude());
				flightInfo.put("localTime", flightInformation.getFlightStatus()
						.getLocalTime());

				if (flightInformation.getFlightStatus().getUtcTime() != null) {
					flightInfo.put("utcTime", flightInformation
							.getFlightStatus().getUtcTime().toString());
				}

				if (flightInformation.getExpectedArrival() != null) {
					flightInfo.put("expectedArrival", flightInformation
							.getExpectedArrival().toString());
				}
			}
			flightInfo.put("ipAddress", ipAddress);
			flightInfo.put("macAddress", macAddress);

			return createSuccessModelAndView(request, flightInfo);
		}
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

}
