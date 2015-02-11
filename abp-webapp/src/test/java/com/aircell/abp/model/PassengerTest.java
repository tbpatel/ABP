package com.aircell.abp.model;

import com.aircell.abp.model.AircellUser.State;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.SubscriptionService;
import com.aircell.bss.ws.TrackingType;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Calendar;
import java.util.Date;

public class PassengerTest extends MockObjectTestCase {

    /**. private class variable to hold mocked SubscriptionService object */
    private final SubscriptionService mockSubscriptionService =
    mock(SubscriptionService.class);

    /**. private class variable to hold AircellUser object */
    private final AircellUser user = new AircellUser();
    /**. private class variable to hold FlightInformation object */
    private final FlightInformation flight = new FlightInformation();
    /**. private class variable to hold Passenger object */
    private Passenger passenger;
    /**. private class variable to hold Locale */
    private String Locale;
    /**. private class variable to hold deviceType */
    private String deviceType;
    /**. private class variable to hold TrackingType object */
    private TrackingType tracking;

    public void setUp() {
        user.setUsername("TESTUSER");
        user.setState(State.LOGGED_IN);
        passenger = new Passenger(user, flight);
        passenger.setSubscriptionService(mockSubscriptionService);
        Locale = "en_US";
        deviceType = "laptop";
        tracking = new TrackingType();
        tracking
        .setTransactionId("1212SESSIONID8e383738::01/01/2009 10:10:22:234");
    }

    /**.
     * Test the status of hasActiveSubscription; If an empty response is
     * returned from the subscription service
     */
    public void testNoAvailableSubscriptions() {
        try {
            final ServiceResponse<Subscription[]> response =
            new ServiceResponse<Subscription[]>();

            checking(
            new Expectations() {
                {
                    one(mockSubscriptionService).getAvailableSubscriptions(
                    with(equal(user.getUsername())), with(equal(flight)),
                    with(equal(Locale)), with(equal(tracking)),
                    with(equal(deviceType))
                    );
                    will(returnValue(response));
                }}
            );

            assertFalse(
            passenger.hasActiveSubscription(Locale, deviceType, tracking)
            );
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**.
     * Test the status of hasActiveSubscription; If a response containing a
     * subscription with a valid active date (and remaining hours/minutes) is
     * returned from the subscription service
     */
    public void testAvailableActiveSubscription() {
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.HOUR, -1);

            final ServiceResponse<Subscription[]> response =
            new ServiceResponse<Subscription[]>();
            final Subscription subscription =
            createSubscription(cal.getTime(), 1, 1);
            response.setPayload(new Subscription[]{subscription });

            checking(
            new Expectations() {
                {
                    one(mockSubscriptionService).getAvailableSubscriptions(
                    with(equal(user.getUsername())), with(equal(flight)),
                    with(equal(Locale)), with(equal(tracking)),
                    with(equal(deviceType))
                    );
                    will(returnValue(response));
                }}
            );

            assertTrue(
            passenger.hasActiveSubscription(Locale, deviceType, tracking)
            );
            assertEquals(subscription, passenger.getActiveSubscription());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private Subscription createSubscription(
    final Date date, final int hours, final int minutes
    ) {
        final PaidForSubscription subscription = new PaidForSubscription();
        subscription.setDurationRemainingHours(hours);
        subscription.setDurationRemainingMinutes(minutes);
        subscription.setDateActivated(date);
        return subscription;
    }

}
