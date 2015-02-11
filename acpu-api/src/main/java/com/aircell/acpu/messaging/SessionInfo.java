package com.aircell.acpu.messaging;

import java.io.Serializable;

public class SessionInfo implements Serializable {
	private static final long serialVersionUID = -3820577012146139975L;

	private String ipAddress, username;
	private long inPackets, outPackets;
	private int remainingTimeInSeconds;

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

	public long getInPackets() {
		return inPackets;
	}

	public void setInPackets(long inPackets) {
		this.inPackets = inPackets;
	}

	public long getOutPackets() {
		return outPackets;
	}

	public void setOutPackets(long outPackets) {
		this.outPackets = outPackets;
	}

	public int getRemainingTimeInSeconds() {
		return remainingTimeInSeconds;
	}

	public void setRemainingTimeInSeconds(int remainingTimeInSeconds) {
		this.remainingTimeInSeconds = remainingTimeInSeconds;
	}
}
