package com.aircell.acpu.messaging;

public class SystemStatus {
	private AtgLinkStatusCode atgLinkStatus;
	private AbsServiceStatusCode absServiceStatus;
	private long absServiceAvailabilitySeconds;
	private int noOfSubscribedUsers;

	public AtgLinkStatusCode getAtgLinkStatus() {
		return atgLinkStatus;
	}

	public void setAtgLinkStatus(AtgLinkStatusCode atgLinkStatus) {
		this.atgLinkStatus = atgLinkStatus;
	}

	public AbsServiceStatusCode getAbsServiceStatus() {
		return absServiceStatus;
	}

	public void setAbsServiceStatus(AbsServiceStatusCode absServiceStatus) {
		this.absServiceStatus = absServiceStatus;
	}

	public long getAbsServiceAvailabilitySeconds() {
		return absServiceAvailabilitySeconds;
	}

	public void setAbsServiceAvailabilitySeconds(long absServiceAvailabilitySeconds) {
		this.absServiceAvailabilitySeconds = absServiceAvailabilitySeconds;
	}

	public int getNoOfSubscribedUsers() {
		return noOfSubscribedUsers;
	}

	public void setNoOfSubscribedUsers(int noOfSubscribedUsers) {
		this.noOfSubscribedUsers = noOfSubscribedUsers;
	}
}
