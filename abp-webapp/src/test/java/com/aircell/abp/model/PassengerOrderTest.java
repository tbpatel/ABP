/*
 * PassengerOrder.java 28 Sep 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.model.AircellUser.State;
import com.aircell.abp.service.ConfigService;
import com.aircell.abp.service.PaymentService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.service.TaxDiscountAmountPair;
import com.aircell.bss.ws.TrackingType;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**. @author miroslav.miladinovic at AKQA Inc. */

public class PassengerOrderTest extends MockObjectTestCase {

    /**. private class variable to hold Passenger object */
   private Passenger user;
    /**. private class variable to hold PassengerOrder object */
   private PassengerOrder underTest;
    /**. private class variable to hold LoginDetails object */
   private LoginDetails loginDetails;
    /**. private class variable to hold CreditCardDetails object */
    private CreditCardDetails cardDetails;
    /**. private class variable to hold AddressDetails object */
    private AddressDetails usAddress;
    /**. private class variable to hold ConfigService object */
    private ConfigService configService;
    /**. private class variable to hold US county codes */
    public Set<String> usCountryCodes;

    /**. private class variable to hold PaymentService object */
    private PaymentService paymentServiceMock;
    /**. private class variable to hold ServiceResponse object */
    private ServiceResponse<TaxDiscountAmountPair> calculateOrderResponse;
    /**. private class variable to hold TaxDiscountAmountPair */
    private TaxDiscountAmountPair taxDiscount;
    /**. private class variable to hold ServiceResponse object
     *  for process order */
    private ServiceResponse<String> processOrderResponse;
    /**. private class variable to hold SubscriptionOrderLineItem object */
    private SubscriptionOrderLineItem lineItem;
    /**. private class variable to hold priceExTax */
    private double priceExTax = 9.99;
    /**. private class variable to hold productCode */
    private String productCode = "S001";
    /**. private class variable to hold promoCode */
    private String promoCode = "P001";
    /**. private class variable to hold quantity */
    private int quantity = 1;
    /**. private class variable to hold username */
    private String username = "joe.bloggs";
    /**. private class variable to hold password */
    private String password = "toro";

    /**. private class variable to hold aircraftTailNumber */
    private String aircraftTailNumber = "711";
    /**. private class variable to hold airlineCode */
    private String airlineCode = "AA";
    /**. private class variable to hold departureAirportCode */
    private String departureAirportCode = "JFK";
    /**. private class variable to hold destinationAirportCode */
    private String destinationAirportCode = "LAX";
    /**. private class variable to hold airlineName */
    private String airlineName = "American Airlines";
    /**. private class variable to hold expectedArrival dateTime */
    private Date expectedArrival = new Date();
    /**. private class variable to hold flightNumber */
    private String flightNumber = "AA8888";
    /**. private class variable to hold flightStatus object */
    private FlightStatus flightStatus = new FlightStatus();
    /**. private class variable to hold FlightInformation object */
    private FlightInformation flightInfo;

    /**. private class variable to hold address1 */
    private final String address1 = "175 Varick Street";
    /**. private class variable to hold address2 */
    private final String address2 = "10th Floor";
    /**. private class variable to hold city */
    private final String city = "New York";
    /**. private class variable to hold intlCountry */
    private final String intlCountry = "US";
    /**. private class variable to hold usState */
    private final String usState = "NY";
    /**. private class variable to hold usZipCode */
    private final String usZipCode = "10014";

    /**. private class variable to hold cardNumber */
    private final String cardNumber = "28738748738";
    /**. private class variable to hold cardType */
    private final String cardType = "VISA";
    /**. private class variable to hold expiryYear */
    private final int expiryYear = 9;
    /**. private class variable to hold expiryMonth */
    private final int expiryMonth = 12;
    /**. private class variable to hold nameOnCard */
    private final String nameOnCard = "M Miladinovic";
    /**. private class variable to hold securityCode */
    private final String securityCode = "123";

    /**. private class variable to hold purchase DateTime */
    private final Date purchaseTime = new Date();
    /**. private class variable to hold order Reference */
    private final String orderRef = "789";
    /**. private class variable to hold DeviceType */
    private final String DeviceType = "laptop";
    /**. private class variable to hold TrackingType object */
    private TrackingType tracking;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        paymentServiceMock = mock(PaymentService.class);
        calculateOrderResponse = new ServiceResponse<TaxDiscountAmountPair>();
        taxDiscount = new TaxDiscountAmountPair();
        tracking = new TrackingType();

        flightInfo = new FlightInformation();
        flightInfo.setAircraftTailNumber(aircraftTailNumber);
        flightInfo.setAirlineCode(airlineCode);
        flightInfo.setAirlineName(airlineName);
        flightInfo.setDepartureAirportCode(departureAirportCode);
        flightInfo.setDestinationAirportCode(destinationAirportCode);
        flightInfo.setExpectedArrival(expectedArrival);
        flightInfo.setFlightNumber(flightNumber);
        flightInfo.setFlightStatus(flightStatus);

        loginDetails = new LoginDetails();
        loginDetails.setUsername(username);
        loginDetails.setPassword(password);

        user = new Passenger(flightInfo);
        user.setLoginDetails(loginDetails);
        user.setState(State.LOGGED_IN);
        underTest = new PassengerOrder(user, flightInfo);
        underTest.setPaymentService(paymentServiceMock);

        lineItem = new SubscriptionOrderLineItem();
        lineItem.setPriceExTax(priceExTax);
        lineItem.setProductCode(productCode);
        lineItem.setPromoCode(promoCode);
        lineItem.setQuantity(quantity);
        underTest.addOrderLineItem(lineItem);

        processOrderResponse = new ServiceResponse<String>();

        configService = mock(ConfigService.class);
        usCountryCodes = new HashSet<String>();
        usCountryCodes.add("US");

        usAddress = new AddressDetails();
        usAddress.setAddress1(address1);
        usAddress.setAddress2(address2);
        usAddress.setCity(city);
        usAddress.setCountryCode(intlCountry);
        usAddress.setUsStateCode(usState);
        usAddress.setUsZipCode(usZipCode);
        usAddress.setConfigService(configService);

        cardDetails = new CreditCardDetails();
        cardDetails.setBillingAddress(usAddress);
        cardDetails.setCardNumber(cardNumber);
        cardDetails.setCardType(cardType);
        cardDetails.setExpiryYear(expiryYear);
        cardDetails.setExpiryMonth(Integer.toString(expiryMonth));
        cardDetails.setNameOnCard(nameOnCard);
        cardDetails.setSecurityCode(securityCode);

        tracking
        .setTransactionId("1212SESSIONID8e383738::01/01/2009 10:10:22:234");
    }

    private Expectations configServiceExpectations() {
        return new Expectations() {
            {
                allowing(configService).getCountryCodesForUS();
                will(returnValue(usCountryCodes));
            }
        };
    }

    public void testGetOrderTotalInfo() {
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        calculateOrderResponse.setSuccess(true);
        calculateOrderResponse.setPayload(taxDiscount);
        taxDiscount.setDiscountAmount(10.11);
        taxDiscount.setTaxAmount(10.00);
        taxDiscount.setTotalAmount(100.11);
        checking(
        new Expectations() {
            {
                one(paymentServiceMock).calculateTaxAndPromotionForItems(
                with(equal(loginDetails)), with(equal(items)),
                with(equal(flightInfo)), with(aNonNull(Date.class)),
                with(equal(tracking))
                );
                will(returnValue(calculateOrderResponse));
            }
        }
        );
        final ServiceResponse<TaxDiscountAmountPair> retval =
        underTest.getOrderTotalInfo(tracking);
        assertNotNull(retval);
        assertTrue(retval.isSuccess());
        assertEquals(
        "total returned from this method should be"
        + " what was returned from the paymetn service",
        100.11, retval.getPayload().getTotalAmount()
        );
        assertEquals(
        "discount returned from this method should be"
        + " what was returned from the paymetn service",
        10.11, retval.getPayload().getDiscountAmount()
        );
        assertEquals(
        "tax returned from this method should be"
        + " what was returned from the paymetn service",
        10.00, retval.getPayload().getTaxAmount()
        );
    }

    public void testGetOrderTotalInfo_EmptyOrder() {
        underTest.removeOrderLineItem(lineItem);
        checking(
        new Expectations() {
            {
                never(paymentServiceMock);
            }
        }
        );
        final ServiceResponse<TaxDiscountAmountPair> retval =
        underTest.getOrderTotalInfo(tracking);
        assertNotNull(retval);
        assertTrue(retval.isSuccess());
        assertEquals(
        "empty order - should have been zero", 0.00,
        retval.getPayload().getDiscountAmount()
        );
        assertEquals(
        "empty order - should have been zero", 0.00,
        retval.getPayload().getTaxAmount()
        );
        assertEquals(
        "empty order - should have been zero", 0.00,
        retval.getPayload().getTotalAmount()
        );
    }

    public void testExecutePaymentCall_HappyPath() {
        final double totalDue = 11.00;
        final boolean saveCardDetails = false;
        processOrderResponse.setSuccess(true);
        processOrderResponse.setPayload(orderRef);
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        checking(
        new Expectations() {
            {
                one(paymentServiceMock).purchaseItems(
                with(equal(loginDetails)), with(equal(cardDetails)),
                with(equal(items)), with(equal(flightInfo)),
                with(equal(purchaseTime)), with(equal(totalDue)),
                with(equal(saveCardDetails)), with(equal(DeviceType)),
                with(equal(tracking))
                );
                will(returnValue(processOrderResponse));
            }
        }
        );
        checking(configServiceExpectations());

        final ServiceResponse<String> response = underTest.executePaymentCall(
        cardDetails, purchaseTime, totalDue, saveCardDetails, DeviceType,
        tracking
        );
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(
        "order ref expected to be passed through from the paymetn service",
        orderRef, response.getPayload()
        );
    }


}
