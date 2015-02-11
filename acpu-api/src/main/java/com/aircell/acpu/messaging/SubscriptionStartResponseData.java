package com.aircell.acpu.messaging;

import java.io.Serializable;

public class SubscriptionStartResponseData implements Serializable {
	private static final long serialVersionUID = 309317751044531098L;

	private String ipAddress, userName, password;
	private boolean status;
	private AcpuErrorCode errorCode;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public AcpuErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(AcpuErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
