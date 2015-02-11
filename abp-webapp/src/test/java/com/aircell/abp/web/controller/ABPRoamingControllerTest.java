

package com.aircell.abp.web.controller;

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
 * Test class for ABPRoamingController
 * @author madhusudan
 *
 */
public class ABPRoamingControllerTest extends TestCase {
    /**.
     * ABPRoamingController
     */
    private ABPRoamingController underTest;
    /**.
     * MockHttpServletRequest
     */
    private MockHttpServletRequest req;
    /**.
     * MockHttpServletResponse
     */
    private MockHttpServletResponse res;
    /**.
     * Flight
     */
    private Flight flight;
    /**.
     * FlightInformation
     */
    private FlightInformation flightInformation;
    /**.
     * ModelAndView
     */
    private ModelAndView mv;
    /**.
     * Method to override
     * @throws Exception General Exception
     */
    protected void setUp() throws Exception {

        underTest = new ABPRoamingController();
        Version version = new Version();
        version.setAbpVersionNo("2.3.4");
        version.setAcpuVersion("2.3.4");
        underTest.setVersion(version);

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

    }
    /**.
     * Test handleRequestInternal method
     * @throws Exception General Exception
     */
    public void testhandleRequestInternal() throws Exception {
        mv = underTest.handleRequestInternal(req, res);
        assertNotNull(mv);
    }
}
