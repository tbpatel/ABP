package com.aircell.abp.web.controller;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.backchannel.BackChannelUtils;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**.
 * . Test class for AAAActivateController
 * @author madhusudan
 */
public class AAAActivateControllerTest extends TestCase {
    /**. . AAAActivateController */
    private AAAActivateController aaaActivateControllerUnderTest;
    /**. . MockHttpServletRequest */
    private MockHttpServletRequest request;
    /**. . MockHttpServletResponse */
    private MockHttpServletResponse response;
    /**. . BindException */
    private BindException errors;
    /**. . Object */
    private Object object;
    /**. . String variable */
    private final String abpversion = "4.5.0";
    /**. . Map */
    private Map<String, Object> model;
    /**. . AirPassenger */
    private AirPassenger airPassenger;
    /**. . ModelAndView */
    private ModelAndView mv;

    /**.
     * . Method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        aaaActivateControllerUnderTest = new AAAActivateController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        errors = new BindException(aaaActivateControllerUnderTest, "exception");
        object = new Object();
        model = new HashMap<String, Object>();
        airPassenger = new AirPassenger();
        mv = new ModelAndView();
        request.setParameter(BackChannelUtils.GBP_SESSION_ID, "1234");
    }

    /**. . Test method for handler */
//    public void testhandler() {
//        aaaActivateControllerUnderTest
//        .setHttpClientGbpPage("gbp/page/gbpBackChannel.do");
//        aaaActivateControllerUnderTest.setHttpPostCookieName("JSESSIONID");
//        aaaActivateControllerUnderTest.setCookieMaxAge(-1);
//        aaaActivateControllerUnderTest.setCookieSecure(false);
//        aaaActivateControllerUnderTest
//        .setHttpPostDomain("/airborne.gogo.com/abp");
//        aaaActivateControllerUnderTest.setHttpPath("/");
//        aaaActivateControllerUnderTest.setParamName("method");
//        aaaActivateControllerUnderTest.setSslCertStorePath("java/jdk/jer");
//        mv = aaaActivateControllerUnderTest.handler(
//        request, response, object, errors
//        );
//        assertNotNull(mv);
//    }

    /**. . Test method for getFailureView */
    public void testgetFailureView() {
        aaaActivateControllerUnderTest
        .setFailureView("airborne.gogo.com/gbp/techError.do");
        String failure = aaaActivateControllerUnderTest.getFailureView(request);
        assertNotNull(failure);
    }

    /**. . Test method for getFailureViews */
    public void testgetFailureViewsAbpVersionNotNull() {
        aaaActivateControllerUnderTest
        .setAbpfailureView("airborne.gogo.com/gbp/techError.do");
        mv = aaaActivateControllerUnderTest.getFailureViews(
        request, abpversion, model
        );
        assertNotNull(mv);
    }

    /**. . Test method for getFailureViews */
    public void testAbpVersionNull() {
        aaaActivateControllerUnderTest
        .setFailureView("airborne.gogo.com/gbp/techError.do");
        String abpVersionNull = "";
        mv = aaaActivateControllerUnderTest.getFailureViews(
        request, abpVersionNull, model
        );
        assertNotNull(mv);

    }

    /**. . Test method for getSuccessView */
    public void testgetSuccessView() {
        aaaActivateControllerUnderTest
        .setSuccessView("airborne.gogo.com/gbp/gobrowse.do");
        String success = aaaActivateControllerUnderTest.getSuccessView(request);
        assertNotNull(success);
    }

    /**. . Test method for startATGSession */
    public void teststartATGSession() {
        try {
            try {
                ServiceResponse<?> acpuResponse =
                aaaActivateControllerUnderTest.startATGSession(
                airPassenger, request, response, errors
                );
                assertNotNull(acpuResponse);
            } catch (IllegalStateException e) {
                e.getMessage();
            }
        } catch (RuntimeException e) {
            e.getMessage();
        }
    }

}
