package com.aircell.abp.service;

import com.aircell.abp.model.Flight;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsATGLinkStateServiceIntegrationTest extends TestCase {

    static final String appCtxLocation =
    "com/aircell/abp/service/atglinkstate-jms-int-test-spring-cfg.xml";

    private ApplicationContext ctx;
    private Flight flight;

    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext(appCtxLocation);
        flight = (Flight) ctx.getBean("flight");
    }

    public void testLinkUpDownMessage() throws InterruptedException {
        final ATGLinkStateSender sender =
        (ATGLinkStateSender) ctx.getBean("atgLinkStateSender");
        assertFalse("Link down OK", flight.isAtgLinkUp());
        sender.sendATGLinkUpMessage();
        Thread.sleep(2000);
        assertTrue("Link up OK", flight.isAtgLinkUp());
        sender.sendATGLinkDownMessage();
        Thread.sleep(2000);
        assertFalse("Link down OK", flight.isAtgLinkUp());
    }

}
