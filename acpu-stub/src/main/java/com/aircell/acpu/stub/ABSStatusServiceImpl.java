package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;
import com.aircell.abs.acpu.common.SystemStatus;

public class ABSStatusServiceImpl implements ABSStatusService {

    public SystemStatus getSystemStatus() {

        SystemStatus response = new SystemStatus(
        AtgLinkStatusCodes.ATG_LINK_UP,
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE, 1000, 10); //, "test");

        return response;
    }

}
