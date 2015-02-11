/*
 * Passenger.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.model.SecurityChallengeDetails.SecurityChallengeType;
import com.aircell.abp.model.utils.AircellUtils;
import com.aircell.abp.model.utils.IctoToIataAirlineCodeMapping;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.bss.ws.TrackingType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**.
 * Class representing a Passenger on-board a flight with a wifi device capable
 * of using the ATG link.
 * @author miroslav.miladinovic
 */
@Configurable()
public class Passenger extends AircellUser implements Serializable {

    /**.
     * static final variable containg seralVersionUID
     */
    private static final long serialVersionUID = -5951135182198989342L;

    /**.
     * Logger object
     */
    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    /**.
     * FlightInformation object
     */
    private FlightInformation flight;

    /**.
     * Subscription object
     */
    private Subscription activeSubscription;

    /**.
     * String containing unavailable services
     */
    private String unavailableServices = "";

    /**.
     * String containing product code
     */
    private String productCode;

    /**.
     * IctoToIataAirlineCodeMapping object
     */
    private IctoToIataAirlineCodeMapping ictoToIataAirlineCodeMapping;

    /**.
     * Constructor
     *
     */
    private Passenger() {}

    /**.
     * Constructor - Initialize member variables
     * @param flight FlightInformation object
     */
    public Passenger(final FlightInformation flight) {
        super();
        if (flight == null) {
            throw new IllegalArgumentException("Flight object cannot be null.");
        }
        this.flight = flight;
    }

    /**.
     * Constructor - Initialize member variables
     * @param user AircellUser object
     * @param flight FlightInformation object
     */
    public Passenger(final AircellUser user, final FlightInformation flight) {
        this(flight);
        if (user == null) {
            throw new IllegalArgumentException(
            "aircell user object " + "cannot be null"
            );
        }
        // TODO Iter 2: make better -
        // TODO  this copying logic should be in the Aircell class!
        if (user.getLoginDetails() != null) {
            setLoginDetails(user.getLoginDetails());
        }
        if (user.getEmailAddress() != null) {
            setEmailAddress(user.getEmailAddress());
        }
        if (user.getIpAddress() != null) {
            setIpAddress(user.getIpAddress());
        }
        if (user.getAddressChallenge() != null) {
            setAddressDetails(user.getAddressDetails());
        }
        if (user.getOrder() != null) {
            setOrder(user.getOrder());
        }
        if (user.getUsernameChallenge() != null) {
            setUsernameChallenge(user.getUsernameChallenge());
        }
        if (user.getPasswordChallenge() != null) {
            setPasswordChallenge(user.getPasswordChallenge());
        }
        if (user.getCardNumberChallenge() != null) {
            setCardNumberChallenge(user.getCardNumberChallenge());
        }
        if (user.getAddressChallenge() != null) {
            setAddressChallenge(user.getAddressChallenge());
        }

        setState(user.getState());
    }

    // *********************************
    // *** business methods
    // *********************************

    /**.
     * Registers the new user with the system. Please note that user's address
     * details are <strong>NOT</strong> required.
     * This method has to be called only if this object is in the following
     * state: <code>State.NOT_LOGGED_IN</code>.
     * In case of successful outcome, the payload of the
     * <code>ServiceResponse</code> object will contain the internal user ID of
     * the newly registered user. Additionally this user will also be logged
     * into the system.
     * The <code>ServiceResponse</code> contains error codes of type {@link
     * com.aircell.abp.service.BSSErrorCode} or
     *  {@link com.aircell.abp.service.AAAErrorCode}.
     * @param loginDetails cannot be null
     * @param emailAddress cannot be null
     * @param personalDetails cannot be null
     * @param usernameChallenge cannot be null
     * @param passwordChallenge SecurityChallengeDetails object
     * @param ipAddress cannot be null
     * @param tsCsVersionAccepted cannot be null
     * @param marketingOptIn boolean value
     * @param tracking TrackingType object
     * @return the <code>ServiceResponse</code> object for this call.
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     */
    public ServiceResponse<Integer> register(
    final LoginDetails loginDetails, final String emailAddress,
    final PersonalDetails personalDetails,
    final SecurityChallengeDetails usernameChallenge,
    final SecurityChallengeDetails passwordChallenge, final String ipAddress,
    final String tsCsVersionAccepted, final boolean marketingOptIn,
    final TrackingType tracking
    ) throws IllegalArgumentException, IllegalStateException {
        logger.debug("Passenger.register: "
        + "registering user {} with ip {}"
        , loginDetails.getUsername(), ipAddress
        );
        assertRegisterPreconditions(
        loginDetails, emailAddress, personalDetails, usernameChallenge,
        passwordChallenge, ipAddress, tsCsVersionAccepted
        );
        assertState(State.NOT_LOGGED_IN);

        usernameChallenge.setType(SecurityChallengeType.USERNAME_CHALLENGE);
        passwordChallenge.setType(SecurityChallengeType.PASSWORD_CHALLENGE);

        final ServiceResponse<Integer> retval =
        getUserDetailsService().registerNewUser(
        loginDetails, emailAddress, personalDetails,
        new SecurityChallengeDetails[]{usernameChallenge, passwordChallenge},
        tsCsVersionAccepted, marketingOptIn, tracking
        );

        if (retval.isSuccess()) {
            this.getLoginDetails().setUsername(loginDetails.getUsername());
            this.getLoginDetails().setPassword(loginDetails.getPassword());
            this.setIpAddress(ipAddress);
            // set the user as logged in
            setState(State.LOGGED_IN);
        }
        return retval;
    }

    /**.
     * Asserts conditions prior to registration
     * @param loginDetails LoginDetails object
     * @param emailAddress emailAddress
     * @param personalDetails PersonalDetails object
     * @param usernameChallenge user name security question
     * @param passwordChallenge password name security question
     * @param ipAddress user IP address
     * @param tsCsVersionAccepted  trye if tsCsVersion Accepted
     */
    protected void assertRegisterPreconditions(
    final LoginDetails loginDetails, final String emailAddress,
    final PersonalDetails personalDetails,
    final SecurityChallengeDetails usernameChallenge,
    final SecurityChallengeDetails passwordChallenge, final String ipAddress,
    final String tsCsVersionAccepted
    ) {
        if (loginDetails == null
        || StringUtils.isBlank(loginDetails.getUsername()) || StringUtils
        .isBlank(loginDetails.getPassword())) {
            throw new IllegalArgumentException(
            "Login details invalid:" + loginDetails
            );
        }
        if (StringUtils.isBlank(emailAddress)) {
            throw new IllegalArgumentException(
            "Email address invalid:" + emailAddress
            );
        }
        if (personalDetails == null) {
            throw new IllegalArgumentException(
            "Personal details invalid:" + personalDetails
            );
        }
        if (usernameChallenge == null) {
            throw new IllegalArgumentException(
            "Username security challenge invalid: " + usernameChallenge
            );
        }
        if (passwordChallenge == null) {
            throw new IllegalArgumentException(
            "Password security challenge invalid:" + passwordChallenge
            );
        }
        if (StringUtils.isBlank(ipAddress)) {
            throw new IllegalArgumentException(
            "IP address invalid:" + ipAddress
            );
        }
        if (StringUtils.isBlank(tsCsVersionAccepted)) {
            throw new IllegalArgumentException(
            "TsCs version invalid:" + tsCsVersionAccepted
            );
        }
    }

    /**.
     * Returns list of subscriptions this Passenger could use on this flight.
     * @param Locale
     * @param tracking
     * @return ServiceResponse ServiceResponse
     * @see com.aircell.abp.model.AircellUser#getAvailableSubscriptions()
     */
    @Override
    public ServiceResponse<Subscription[]> getAvailableSubscriptions(
    String Locale, TrackingType tracking, String deviceType
    ) {
        assertState(State.LOGGED_IN);
        final ServiceResponse<Subscription[]> retval =
        getSubscriptionService().getAvailableSubscriptions(
        getLoginDetails().getUsername(), getFlight(), Locale, tracking,
        deviceType
        );
        return retval;
    }

    /**.
     * Determines whether this user already has an active subscription
     * If the user does have a subscription that is not active yet, then the
     * subscription is activated This method assumes that a subscription with a
     * null dateActivated property or a dateActivated property that is not
     * before today is a valid subscription that has not yet been activated
     * @param Locale Locale
     * @param tracking TrackingType
     * @param deviceType mobile or laptop
     * @return true if the passenger has: a) an active subscription with either
     *         hours or minutes still available on it or b) if the user has a
     *         subscription that is not active yet, false otherwise that the BSS
     *         will change the start time of the subscription to the current
     *         time
     */
    public boolean hasActiveSubscription(
    String Locale, String deviceType, TrackingType tracking
    ) throws Exception {
        final ServiceResponse<Subscription[]> subscriptions =
        getAvailableSubscriptions(Locale, tracking, deviceType);

        logger.debug("Passenger.hasActiveSubscription: "
             + "Enter handling the unavailable service...");
        String iataAirlineCode="";
        if(getIctoToIataAirlineCodeMapping() != null
                &&getIctoToIataAirlineCodeMapping().getAirlineCodeTwoChar()
                != null){
        iataAirlineCode=(String) getIctoToIataAirlineCodeMapping().
        getAirlineCodeTwoChar().get(flight.getAirlineCode());
        }
        if (subscriptions.getPayload() != null) {
            for (int i = 0; i < subscriptions.getPayload().length; i++) {
                productCode = subscriptions.getPayload()[ i ].getProductCode();
             // The condition to check whether the user is ENTERPRISE user or
             //   not is added temporarily. Since ESB is returning the default
             // product code as "20" this validation fails. This will be removed
                //once the product code is updated for ENTERPRISE user
             String productName=subscriptions.getPayload()[ i ].getName();
             if(productName !=null && !productName.
                     equals(AircellUtils.ENTERPRISE)){
                if (productCode != null && productCode
                .startsWith(iataAirlineCode)) {

                    if (!deviceType.equalsIgnoreCase(AircellUtils.MOBILE)
                    && productCode.length() > 5 && productCode.substring(2, 6)
                    .contains(AircellUtils.MB)) {
                        setUnavailableServices("unavailable.service.forDevice");
                        return false;

                    }
                } else if (productCode != null) {
                    setUnavailableServices("unavailable.service.forFlight");
                    return false;
                }
              }
                logger.debug("Passenger.hasActiveSubscription: "
                + "Error Occured  for returning user ----   "
                + getUnavailableServices()
                );
            }
        }

        logger.debug("Passenger.hasActiveSubscription: "
               + "Exit handling the unavailable service");
        if (null != subscriptions && subscriptions.getPayload() != null
        && subscriptions.getPayload().length > 0) {
            final Subscription currentSubscription =
            subscriptions.getPayload()[ 0 ];

            if (!(currentSubscription.getDateActivated() instanceof Date)
            || !currentSubscription.getDateActivated()
            .before(Calendar.getInstance().getTime())) {

                final ServiceResponse activeSubscriptionsResponse =
                getSubscriptionService().activateSubscription(
                getLoginDetails().getUsername(), currentSubscription,
                deviceType, tracking
                );
                if (activeSubscriptionsResponse.isSuccess()) {
                    setActiveSubscription(currentSubscription);
                    return true;
                } else {
                    throw new Exception(
                    "purchaseAndActivateForm.requestFailed"
                    );
                }
            }

            if (currentSubscription != null) {
                setActiveSubscription(currentSubscription);
                return true;
            } else {
                setActiveSubscription(null);
                return false;
            }

/*
@ magesh commented to check the data is not validated with
time by portal to allow
unlimited access which would return zero as time
if(currentSubscription.getDurationRemainingHours() > 0
                    || currentSubscription.getDurationRemainingMinutes() > 0) {

                setActiveSubscription(currentSubscription);
                return true;
            }*/
        }
        return false;
    }

    /**.
     * @param cardDetails CreditCardDetails
     * object containing credit card details
     * @param saveCardDetails boolean specifying if card detilas are saved
     * @param Locale String
     * @param tracking TrackingType
     * @param deviceType mobile or laptop
     * @return ServiceResponse object
     * @throws IllegalStateException
     */
    public ServiceResponse purchaseAndActivateSubscription(
    final CreditCardDetails cardDetails, final boolean saveCardDetails,
    final String Locale, final TrackingType tracking, final String deviceType
    ) throws IllegalStateException {
        assertState(State.LOGGED_IN);
        if (getOrder() == null) {
            throw new IllegalStateException(
            "Passenger has to have an " + "order existing"
            );
        }
        if (cardDetails == null) {
            throw new IllegalArgumentException(
            "Credit card details must " + "not be null."
            );
        }

        if (!getOrder().hasLineItemOfType(SubscriptionOrderLineItem.class)) {
            throw new IllegalStateException(
            "Current order doesn't have any " + "subscription line items."
            );
        }

        final ServiceResponse paymentStatus = getOrder()
        .makePayment(cardDetails, saveCardDetails, deviceType, tracking);

        if (paymentStatus.isSuccess()) {
            final ServiceResponse<Subscription[]> available =
            getAvailableSubscriptions(Locale, tracking, deviceType);
            if (!available.isSuccess()) {
                throw new RuntimeException(
                "Error while getting subscriptions for a passenger",
                available.getErrorCause()
                );
            }
            // TODO security: check that
            // TODO username/password combo hasn't changed since login.
            final ServiceResponse activeSubscriptionsResponse =
            getSubscriptionService().activateSubscription(
            getLoginDetails().getUsername(), available.getPayload()[ 0 ],
            deviceType, tracking
            );

            logger.info(
            "Passenger '{}' activated subscription '{}' on flight '{}', "
            + "serviceResponse '{}'", new Object[]{
            this, available.getPayload()[ 0 ], getFlight(),
            activeSubscriptionsResponse
            }
            );
            Passenger.this.setTime(paymentStatus.getPurchaseTime());
            if (activeSubscriptionsResponse.isSuccess()) {
                setActiveSubscription(available.getPayload()[ 0 ]);
            }
            return activeSubscriptionsResponse;
        } else {
            logger.info(
            "Passenger '{}' could not successfully pay for order "
            + "in order to activate subscription. Flight '{}'", this,
            getFlight()
            );
            return paymentStatus;
        }
    }

    /* (non-Javadoc)
      * @see com.aircell.abp.model.AircellUser#createNewOrder()
      */
    /**.
     * Overrides superclass method
     * @return Order Order
     */
    @Override
    public Order createNewOrder() {
        assertState(State.LOGGED_IN);
        final PassengerOrder order = new PassengerOrder(this, getFlight());
        setOrder(order);
        return getOrder();
    }

    // **********************************
    // *** getters/setters **************
    // **********************************

    /**.
     * Gets FlightInformation
     * @return FlightInformation
     */
    public FlightInformation getFlight() {
        return flight;
    }

    /**.
     * Gets activeSubscription
     * @return the activeSubscription */
    public Subscription getActiveSubscription() {
        return activeSubscription;
    }

    /**.
     * Sets activeSubscription
     * @param activeSubscription the activeSubscription to set */
    public void setActiveSubscription(final Subscription activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    /**.
     * Overrides superclass method
     * @return String String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**. class bariable time */
    public String time;

    /**.
     * Gets time
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**.
     * Sets time
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**.
     * Change the format of date and time
     * @param purchaseTime date and time as a String
     * @return date and time as a String
     */
    public String getDateAndTime(String purchaseTime) {

        Format formatter;
        formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        String dateAndTime = "";
        try {
            Date d1 = (Date) formatter.parseObject(purchaseTime);

            // to set Date
            String month = Integer.toString(d1.getMonth() + 1);
            String year = Integer.toString(d1.getYear() + 1900);
            String date = Integer.toString(d1.getDate());

            if (month.length() == 1) {
                month = "0" + month;
            }
            if (year.length() == 1) {
                year = "0" + year;
            }
            if (date.length() == 1) {
                date = "0" + date;
            }


            //to set Time

            String hour = Integer.toString(d1.getHours());
            String mins = Integer.toString(d1.getMinutes());
            String sec = Integer.toString(d1.getSeconds());


            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            if (mins.length() == 1) {
                mins = "0" + mins;
            }
            if (sec.length() == 1) {
                sec = "0" + sec;
            }

            dateAndTime =
            month + "/" + date + "/" + year + "&" + hour + ":" + mins + ":"
            + sec;
        } catch (Exception e) {
            logger.debug("parse exception  " + e);
        }

        return dateAndTime;
    }

    /**.
     * Sets unavailableServices
     * @param unavailableServices the unavailableServices to set */
    public void setUnavailableServices(String unavailableServices) {
        this.unavailableServices = unavailableServices;
    }

    /**.
     * Gets unavailableServices
     * @return the unavailableServices */
    public String getUnavailableServices() {
        return unavailableServices;
    }

    /**.
     * Gets ictoToIataAirlineCodeMapping
     * @param ictoToIataAirlineCodeMapping the
     * ictoToIataAirlineCodeMapping to set
     */
    public void setIctoToIataAirlineCodeMapping(
            IctoToIataAirlineCodeMapping ictoToIataAirlineCodeMapping) {
        this.ictoToIataAirlineCodeMapping = ictoToIataAirlineCodeMapping;
    }

    /**.
     * Sets ictoToIataAirlineCodeMapping
     * @return the ictoToIataAirlineCodeMapping
     */
    public IctoToIataAirlineCodeMapping getIctoToIataAirlineCodeMapping() {
        return ictoToIataAirlineCodeMapping;
    }



}
