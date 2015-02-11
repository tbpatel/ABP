package com.aircell.abp.model;

import com.aircell.abp.service.ServiceResponse;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.bss.ws.TrackingType;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**.
 * Integration test for the Passenger class.
 * @author miroslav.miladinovic
 */
public class AircellUserIntegrationTest
extends AbstractDependencyInjectionSpringContextTests {
    /**. class variable */
    private TrackingType tracking;

    /**.
     * Tests login with valid details
     *
     */
    public void testPassengerLogin() {
        final AircellUser p = new AircellUser();
        tracking =
        new TrackingType("sessionID2382632323::01/01/2009 12:30:12:234");
        final LoginDetails details = new LoginDetails("username", "username");
        final String ipAddress = "10.2.100.35";
        final ServiceResponse r =
        p.loginToGround(details, ipAddress, "1", tracking);
        assertTrue(r.isSuccess());
    }

    /**.
     * Tests login with invalid details
     *
     */
    public void testPassengerLoginWithInvalidDetails() {
        final AircellUser p = new AircellUser();
        tracking =
        new TrackingType("sessionID2382632323::01/01/2009 12:30:12:234");
        final LoginDetails details =
        new LoginDetails("username", "willfailwithinvalidetails");
        final String ipAddress = "10.2.100.35";
        final ServiceResponse r =
        p.loginToGround(details, ipAddress, "1", tracking);
        assertFalse(r.isSuccess());
        assertEquals(AcpuErrorCode.AUTHENTICATION_REJECTED, r.getErrorCode());
    }

    /**.
     * Overrides superclass
     * @return String array of String
     */
    @Override
    protected String[] getConfigLocations() {
        return new String[]{
        "com/aircell/abp/model/passenger-integration-test-context.xml"
        };
    }

}
