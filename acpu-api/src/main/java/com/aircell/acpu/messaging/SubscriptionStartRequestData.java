package com.aircell.acpu.messaging;

import java.io.Serializable;

public class SubscriptionStartRequestData implements Serializable {
	private static final long serialVersionUID = 2761742224097243331L;
	
	private String ipAddress, domain, userName, password;
	private boolean captchaPassed;


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCaptchaPassed() {
		return captchaPassed;
	}

	public void setCaptchaPassed(boolean captchaPassed) {
		this.captchaPassed = captchaPassed;
	}
}
