

package com.aircell.abp.web.controller;

import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.aircell.abp.model.Flight;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.FlightSystem;
import com.aircell.abp.model.Version;
import com.aircell.abs.acpu.common.AbsServiceStatusCodes;
import com.aircell.abs.acpu.common.AtgLinkStatusCodes;

/**.
 *
 * @author madhusudan
 *
 */
public class ABPDefaultControllerTest extends TestCase {
    /**.
     *
     */
    private ABPDefaultController underTest;
    /**.
     *
     */
    private MockHttpServletRequest req;
    /**.
     *
     */
    private MockHttpServletResponse res;
    /**. . String variable */
    private String name = "testCookieName";
    /**.
     *
     */
    private ModelAndView mv;
    /**.
     * Method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        underTest = new ABPDefaultController();
        Version version = new Version();
        version.setAbpVersionNo("2.3.4");
        version.setAcpuVersion("2.3.4");
        underTest.setVersion(version);
        underTest.setAbpDefaultPg("airborne.gogo.com/abp/ampConnecting.jsp");
        FlightInformation flightInfo = new FlightInformation();
        flightInfo.setAircraftTailNumber("123");
        flightInfo.setAirlineCode("Aircell");
        flightInfo.setDepartureAirportCode("JKX");
        FlightSystem flightSystem = new FlightSystem();
        flightSystem.setAtgLinkStatus(AtgLinkStatusCodes.ATG_LINK_UP);
        flightSystem
            .setAbsServiceStatus(AbsServiceStatusCodes.ABS_SERVICE_AVAILABLE);
        Flight flight = new Flight();
        flight.setAtgLinkUp(true);
        flight.setFlightInformation(flightInfo);
        flight.setFlightSystem(flightSystem);
        underTest.setFlight(flight);
        res = new MockHttpServletResponse();
        req = new MockHttpServletRequest();
        mv = new ModelAndView();
        Cookie[] c = new Cookie[1];
        Cookie cook1 = new Cookie("testCookieName", "testCookieName");
        c[0] = cook1;
        req.setCookies(c);

    }
    /**.
     * Test handleRequestInternal method
     * @throws Exception General Exception
     */
    public void testhandleRequestInternal() throws Exception {

        mv = underTest.handleRequestInternal(req, res);
        //assertNotNull(mv);

    }

    /**. . Test getCookie */
    public void testgetCookie() {
        String value = underTest.getCookie(name, req);
        assertNotNull(value);
    }
}
