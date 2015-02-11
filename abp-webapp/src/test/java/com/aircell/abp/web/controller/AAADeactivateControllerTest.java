package com.aircell.abp.web.controller;

import com.aircell.abp.model.AirPassenger;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.validation.BindException;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**.
 * . Test class for AAADeactivateController
 * @author madhusudan
 */
public class AAADeactivateControllerTest extends TestCase {
    /**. . AAADeactivateController */
    private AAADeactivateController aaaDeactivateControllerUnderTest;
    /**. . MockHttpServletRequest */
    private MockHttpServletRequest req;
    /**. . MockHttpServletResponse */
    private MockHttpServletResponse res;
    /**. . AirPassenger */
    private AirPassenger passenger;
    /**. . BindException */
    private BindException errors;
    /**. . Object */
    private Object command;
    /**. . Map HashMap */
    private Map<Object, Object> model;
    /**. . */
    private ModelAndView mv;

    /**.
     * . Method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        aaaDeactivateControllerUnderTest = new AAADeactivateController();
        aaaDeactivateControllerUnderTest.setNotAuthForward("xxxx");
        aaaDeactivateControllerUnderTest.setSslCertStorePath("C:/program/java");
        req = new MockHttpServletRequest();
        res = new MockHttpServletResponse();
        passenger = new AirPassenger();
        errors = new BindException(
        aaaDeactivateControllerUnderTest, "exception"
        );
        command = new Object();
        model = new HashMap<Object, Object>();
        mv = new ModelAndView();
    }

    /**. . Test endATGSession */
    public void testendATGSession() {
        try {
            aaaDeactivateControllerUnderTest.endATGSession(
            passenger, req, res, errors
            );
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**. . Test createFailureModelAndView */
    public void testcreateFailureModelAndView() {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest httpRequest =
        new MockHttpServletRequest(servletContext);
        httpRequest.setAttribute("isMobileDevice", true);
        org.springframework.web.context.request.RequestContextHolder.
        setRequestAttributes(new ServletRequestAttributes(httpRequest));
        mv = aaaDeactivateControllerUnderTest.
        createFailureModelAndView(req, model);
        assertNotNull(mv);
    }
}
