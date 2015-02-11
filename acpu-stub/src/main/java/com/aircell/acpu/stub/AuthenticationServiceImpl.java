package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * .
 * @author miroslav.miladinovic
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, AuthResponseData> userData;

    public AuthResponseData authenticateUser(final AuthRequestData data) {
        if (getUserData() == null) {
            throw new IllegalStateException(
            "This service cannot work without user data set."
            + " Check your spring config.");
        }
        AuthResponseData responseData = null;
        if (data != null && StringUtils.isNotBlank(data.getUsername())) {
            final String username = data.getUsername();
            responseData = getUserData().get(data.getUsername());
            logger.debug("AuthenticationServiceImpl.authenticateUser: "
                + "Found response data for username '{}': {}"
                , username, responseData
            );
        }
        if (responseData == null) {
            responseData = new AuthResponseData("", "", false);
            responseData.setStatus(true);
        }
        logger.info("AuthenticationServiceImpl.authenticateUser:"
        + "Returning data for request '{}': data returned '{}'", data,
        responseData
        );
        return responseData;
    }

    // *** Spring injected properties;
    public Map<String, AuthResponseData> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, AuthResponseData> userData) {
        this.userData = userData;
    }
}
