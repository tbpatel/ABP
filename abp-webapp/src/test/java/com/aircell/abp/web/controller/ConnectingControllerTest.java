package com.aircell.abp.web.controller;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;


public class ConnectingControllerTest extends MockObjectTestCase {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelAndView modelAndView;
    private ConnectingController underTest;

    private MockHttpServletRequest request;
    private HttpServletResponse response;
    private String abpflg = null;

    protected void setUp() throws Exception {
        super.setUp();

        modelAndView = new ModelAndView();
        underTest = new ConnectingController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        underTest.setAbpDefaultPg("abpDefaultPg");

    }

    public void testHandleRequestInternal_NotNull() {
        try {
            request.setParameter("abpflg", "abpflg");
            modelAndView = underTest.handleRequestInternal(request, response);
            assertNull(modelAndView);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("exception" + e);
            e.printStackTrace();
        }
    }

    public void testHandleRequestInternal_Null() {
        try {
            request.setParameter("abpflg", abpflg);
            modelAndView = underTest.handleRequestInternal(request, response);
            assertNull(modelAndView);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("exception" + e);
            e.printStackTrace();
        }
    }

}
