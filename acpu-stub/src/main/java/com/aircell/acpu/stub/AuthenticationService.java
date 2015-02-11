package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;


/**
 * .
 * @author miroslav.miladinovic
 */
public interface AuthenticationService {

    /**
     * . Return response data object for the given request data The
     * implementation is expected to read configurable response data. Response
     * object with 'invalid details' status should be returned in case no
     * response data was found for the given request data.
     * @param data authentication response
     * @return response
     */
    AuthResponseData authenticateUser(final AuthRequestData data);
}
