

package com.aircell.abp.web.controller;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**.
 * Test Class for SquidErrorController
 * @author madhusudan
 *
 */
public class SquidErrorControllerTest extends TestCase {
    /**.
     * SquidErrorController
     */
    private SquidErrorController underTest;
    /**.
     * MockHttpServletRequest
     */
    private MockHttpServletRequest request;
    /**.
     * MockHttpServletResponse
     */
    private MockHttpServletResponse response;
    /**.
     * ModelAndView
     */
    private ModelAndView mv;
    /**.
     * method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        underTest = new SquidErrorController();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        mv = new ModelAndView();

    }
    /**.
     * Test handler method
     * @throws Exception General Exception
     */
    public void test() throws Exception {
        mv = underTest.handler(request, response);
        assertNotNull(mv);
    }
}
