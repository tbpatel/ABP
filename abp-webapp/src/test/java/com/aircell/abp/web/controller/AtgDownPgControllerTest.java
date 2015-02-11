package com.aircell.abp.web.controller;

import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**.
 * . Test class for AtgDownPgController
 * @author madhusudan
 */


public class AtgDownPgControllerTest extends MockObjectTestCase {
    /**. . ModelAndView */
    private ModelAndView modelAndView;
    /**. . AtgDownPgController */
    private AtgDownPgController underTest;
    /**. . MockHttpServletRequest */
    private MockHttpServletRequest request;
    /**. . HttpServletResponse */
    private HttpServletResponse response;

    /**.
     * . Method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        modelAndView = new ModelAndView();
        underTest = new AtgDownPgController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setParameter("abpflg", "abpflg");
    }

    /**. . Test handler */
    public void testHandleNotNull() {
        try {
            modelAndView = underTest.handler(request, response);
            assertNotNull(modelAndView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
