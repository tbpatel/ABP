package com.aircell.acpu.messaging;

import java.io.Serializable;

public class AuthResponseData implements Serializable {
	private static final long serialVersionUID = 6706411869712832544L;
	
	private String ipAddress, username;
	private boolean status;
	private AcpuErrorCode errorCode;
	private String failureReason;


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
}
