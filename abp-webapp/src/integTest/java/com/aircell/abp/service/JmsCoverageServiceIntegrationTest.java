package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsCoverageServiceIntegrationTest extends TestCase {

    private static final String appCtxLocation =
    "com/aircell/abp/service/coveragestate-jms-int-test-spring-cfg.xml";

    private ApplicationContext ctx;
    private Flight flight;

    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(appCtxLocation);
        flight = (Flight) ctx.getBean("flight");
    }

    public void testLinkUpDownMessage() throws InterruptedException {
        final CoverageStateSender sender =
        (CoverageStateSender) ctx.getBean("coverageStateSender");
        assertFalse("Not in coverage", flight.isInCoverage());
        sender.sendEnteringCoverageMessage();
        Thread.sleep(2000);
        assertTrue("Entering coverage OK", flight.isInCoverage());
        sender.sendExitingCoverageMessage();
        Thread.sleep(2000);
        assertFalse("Existing coverage OK", flight.isInCoverage());
    }

}
