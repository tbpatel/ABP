package com.aircell.acpu.stub;

import com.aircell.abs.acpu.common.AuthRequestData;
import com.aircell.abs.acpu.common.AuthResponseData;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * @author miroslav.miladinovic
 */
public class AuthenticationServiceImplTest extends TestCase {

    private AuthenticationServiceImpl underTest;

    @Override
    protected void setUp() throws Exception {
        underTest = new AuthenticationServiceImpl();
        final Map<String, AuthResponseData> data =
        new HashMap<String, AuthResponseData>();
        underTest.setUserData(data);

        final AuthResponseData p1 = new AuthResponseData("", "", false);
        p1.setUsername("user1");
        p1.setIpAddress("10.2.100.25");
        p1.setStatus(true);

        final AuthResponseData p2 = new AuthResponseData("", "", false);
        p2.setUsername("user2");
        p2.setIpAddress("10.2.100.26");
        p2.setStatus(false);
        p2.setFailureReason("INVALID_DETAILS");

        final AuthResponseData p3 = new AuthResponseData("", "", false);
        p3.setUsername("user3");
        p3.setIpAddress("10.2.100.27");
        p3.setStatus(false);
        p3.setFailureReason("ALREADY_LOGGED_IN");

        data.put("user1", p1);
        data.put("user2", p2);
        data.put("user3", p3);
    }

    public void testProfileOK() {
        final AuthRequestData request = new AuthRequestData("", "", "");
        request.setUsername("user1");
        request.setPassword("blah");
        request.setIpAddress("10.2.30.100");
        final AuthResponseData retval = underTest.authenticateUser(request);
        assertNotNull(retval);
        assertEquals("user1", retval.getUsername());
        assertTrue(retval.getStatus());
    }

    public void testProfileDoesntExist() {
        final AuthRequestData request = new AuthRequestData("", "", "");
        request.setUsername("NOWAYYOUFINDTHIS");
        request.setPassword("blah");
        request.setIpAddress("10.2.30.100");
        final AuthResponseData retval = underTest.authenticateUser(request);
        assertNotNull(retval);
        // expecting success if usename cannot be found.
        assertTrue(retval.getStatus());

    }

    public void testWithNull() {
        final AuthResponseData retval = underTest.authenticateUser(null);
        assertNotNull(retval);
        assertTrue(retval.getStatus());

    }

    public void testWithNullUsername() {
        final AuthRequestData request = new AuthRequestData("", "", "");
        request.setUsername(null);
        request.setPassword("blah");
        request.setIpAddress("10.2.30.100");
        final AuthResponseData retval = underTest.authenticateUser(request);
        assertNotNull(retval);
        assertTrue(retval.getStatus());

    }

    public void testInvalidState() {
        underTest.setUserData(null);
        try {
            underTest.authenticateUser(new AuthRequestData("", "", ""));
            fail("IlleglaStateException should have been raised");
        } catch (IllegalStateException expected) {
            assertTrue(true);
        }
    }

}
