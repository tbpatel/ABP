package com.aircell.abp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.UserFlightSession;
import com.aircell.abp.utils.AircellServletUtils;

public class UserListController extends AircellCommandController{

	@Override
	public ModelAndView handler(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		// TODO Auto-generated method stub
		final String ipAddress = AircellServletUtils.getIpAddress(request);
		AirPassenger passenger = new AirPassenger();
		String macAddress = "";
		String username= "";
		Map<String, Object> userInfo = new HashMap<String, Object>();
		UserFlightSession session = passenger.getSession(ipAddress);
        if(session != null) {
        	macAddress = session.getUserMac()!= null ? session.getUserMac():"";
        	username= session.getUserName()!= null? session.getUserName():"";
        logger.info("UserName:::::" + username +"  ::: MAC Address:::"
        		+ macAddress);
       
        }
        userInfo.put("ipAddress", ipAddress);
        userInfo.put("macAddress", macAddress);
        userInfo.put("userName", username);
    
		return createSuccessModelAndView(request, userInfo);
	}

}
