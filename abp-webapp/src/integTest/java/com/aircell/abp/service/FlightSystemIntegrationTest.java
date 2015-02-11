package com.aircell.abp.service;

import com.aircell.abp.model.FlightSystem;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FlightSystemIntegrationTest extends TestCase {

    static final String appCtxLocation =
    "com/aircell/abp/service/flightsystem-jms-int-test-spring-cfg.xml";

    private ApplicationContext ctx;

    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(appCtxLocation);
    }

    public void testHappyPath() {
        FlightSystemServiceImpl underTest =
        (FlightSystemServiceImpl) ctx.getBean("flightSystemService");
        FlightSystem response = underTest.getCurrentStatus();
        assertNotNull(response);
        assertEquals(
        AtgLinkStatusCodes.ATG_LINK_UP, response.getAtgLinkStatus()
        );
        assertEquals(
        AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE,
        response.getAbsServiceStatus()
        );
    }

}
