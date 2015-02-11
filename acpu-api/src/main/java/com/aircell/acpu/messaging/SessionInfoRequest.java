package com.aircell.acpu.messaging;

import java.io.Serializable;

public class SessionInfoRequest implements Serializable {
	private static final long serialVersionUID = 186137706975745642L;
	
	private String ipAddress, userName;



	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
