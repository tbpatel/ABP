package com.aircell.abp.service;

import com.aircell.abp.model.UserFlightSession;

public interface SessionInfoService {

    public UserFlightSession getSession(String username, String ip);
}
