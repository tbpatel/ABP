package com.aircell.acpu.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * . To prevent the stub from producing ATG or COVERAGE messages (necessary to
 * run the integration test for these messages) add either the argument
 * NO_ATG_LINK or NO_COVERAGE (adding both will prevent either message being
 * sent).
 * @author jon.boydell at AKQA Inc.
 */
public class ACPUStubRunner {

    private static final Logger logger =
    LoggerFactory.getLogger(ACPUStubRunner.class);

    /**.
     * @param args
     */
    public static void main(String[] args) {
        final String appCtxLocation = "springcfg/acpu-stub-context.xml";
        final ApplicationContext ctx =
        new ClassPathXmlApplicationContext(appCtxLocation);
        logger.info("ACPUStubRunner.main: Application ctx {} "
                + "started from {}", ctx, appCtxLocation);

        boolean stubAtgLink = true;
        boolean stubCoverage = true;

        if (args.length > 0) {
            final Collection<String> argumentList = new ArrayList<String>();
            Collections.addAll(argumentList, args);

            if (argumentList.contains("NO_ATG_LINK")) {
                stubAtgLink = false;
            }

            if (argumentList.contains("NO_COVERAGE")) {
                stubCoverage = false;
            }
        }

        final JmsMessageSender jmsMessageSender =
        (JmsMessageSender) ctx.getBean("jmsMessageSender");

        // add ATGLinkUp and Down messages to testTopic every 15 seconds
        if (stubAtgLink) {
            final AlternateJmsRunner atgLinkStateRunner =
            new AlternateJmsRunner(
            "ATGLinkUp", "ATGLinkDown", "topic/testTopic"
            );
            atgLinkStateRunner.setJmsMessageSender(jmsMessageSender);
            final Thread atgLinkStateThread = new Thread(atgLinkStateRunner);
            atgLinkStateThread.start();
        }

        if (stubCoverage) {
            // add Entering and Exiting Coverage Area messages
            // to testDurableTopic every 15 seconds
            final AlternateJmsRunner coverageStateRunner =
            new AlternateJmsRunner(
            "EnterCoverageArea", "ExitingCoverageArea", "topic/testDurableTopic"
            );
            coverageStateRunner.setJmsMessageSender(jmsMessageSender);
            final Thread coverageStateThread = new Thread(coverageStateRunner);
            coverageStateThread.start();
        }
    }

}
