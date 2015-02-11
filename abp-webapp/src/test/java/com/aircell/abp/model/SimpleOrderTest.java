/*
 * OrderTest.java 3 Aug 2007
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
import com.aircell.abp.service.BSSErrorCode;
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

public class SimpleOrderTest extends MockObjectTestCase {

    private static class OrderLineItemA implements OrderLineItem {
        public double getPriceExTax() {
            return 0;
        }

        public String getProductCode() {
            return null;
        }

        public int getQuantity() {
            return 0;
        }

        public double getTaxAmount() {
            return 0;
        }

        public void setTaxAmount(double taxAmount) {
        }

        public double getTotalAmount() {
            return 0;
        }

        public String getPromoCode() {
            return null;
        }

        public void setPromoCode(String promoCode) {
        }

        public double getDiscountAmount() {
            return 0;
        }

        public void setDiscountAmount(final double discountAmount) {
        }

        public void setTotalAmount(double totalAmount) {
        }
    }

    private static class OrderLineItemB extends OrderLineItemA {

    }

    /**. private class variable to hold AircellUser object */
    private AircellUser user;
    /**. private class variable to hold SimpleOrder object */
    private SimpleOrder underTest;
    /**. private class variable to hold LoginDetails object */
    private LoginDetails loginDetails;
    /**. private class variable to hold CreditCardDetails object */
    private CreditCardDetails cardDetails;
    /**. private class variable to hold AddressDetails object */
    private AddressDetails usAddress;
    /**. private class variable to hold ConfigService object */
    private ConfigService configService;

    /**. private class variable to hold US country codes */
    public Set<String> usCountryCodes;

    /**. private class variable to hold mocked PaymentService object */
    private PaymentService paymentServiceMock;
    /**. private class variable to hold calculated ServiceResponse object */
    private ServiceResponse<TaxDiscountAmountPair> calculateOrderResponse;
    /**. private class variable to hold ServiceResponse object */
    private ServiceResponse<String> processOrderResponse;
    /**. private class variable to hold TaxDiscountAmountPair object */
    private TaxDiscountAmountPair taxDiscount;
    /**. private class variable to hold SubscriptionOrderLineItem object */
    private SubscriptionOrderLineItem lineItem;
    /**. private class variable to hold price Excluding Tax */
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
    /**. private class variable to hold totalDue */
    private double totalDue = 11.00;

    /**. private class variable to hold address1 */
    private final String address1 = "175 Varick Street";
    /**. private class variable to hold address2 */
    private final String address2 = "10th Floor";
    /**. private class variable to hold city */
    private final String city = "New York";
    /**. private class variable to hold Country */
    private final String intlCountry = "US";
    /**. private class variable to hold US State */
    private final String usState = "NY";
    /**. private class variable to hold US ZipCode */
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

    /**. private class variable to hold order Reference */
    private final String orderRef = "123";
    /**. private class variable to hold DeviceType */
    private final String DeviceType = "en_US";

    /**. private class variable to hold purchase DateTime */
    private Date purchaseTime;
    /**. private class variable to hold TrackingType object */
    private TrackingType tracking;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        paymentServiceMock = mock(PaymentService.class);
        calculateOrderResponse = new ServiceResponse<TaxDiscountAmountPair>();
        taxDiscount = new TaxDiscountAmountPair();

        loginDetails = new LoginDetails();
        loginDetails.setUsername(username);
        loginDetails.setPassword(password);
        user = new AircellUser();
        user.setLoginDetails(loginDetails);
        user.setState(State.LOGGED_IN);
        underTest = new SimpleOrder(user);
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

        tracking = new TrackingType();
        tracking
        .setTransactionId("232323Session23626::12/01/2009 12:13:22:234");

        purchaseTime = new Date();
    }

    private Expectations configServiceExpectations() {
        return new Expectations() {
            {
                allowing(configService).getCountryCodesForUS();
                will(returnValue(usCountryCodes));
            }
        };
    }

    public void testHasLineItemOfTypeWithSingleItem() {
        underTest.removeOrderLineItem(lineItem);
        final OrderLineItemA item = new OrderLineItemA();


        assertFalse(
        "order is empty and " + "am not expecting any order line items.",
        underTest.hasLineItemOfType(item.getClass())
        );
        assertFalse(
        "order is emtpy and am not expecting " + "any order line items",
        underTest.hasLineItemOfType(OrderLineItem.class)
        );

        underTest.addOrderLineItem(item);

        assertTrue(
        "order has a single order line item and "
        + "of the type we are trying to match",
        underTest.hasLineItemOfType(item.getClass())
        );

        assertFalse(
        "order has a single order line item, "
        + "but defo not of the type we are trying to " + "test for",
        underTest.hasLineItemOfType(OrderLineItemB.class)
        );

        assertTrue(
        "order has a single order line item, "
        + "but should say false for the order line item interface type",
        underTest.hasLineItemOfType(OrderLineItem.class)
        );

        underTest.removeOrderLineItem(item);

        assertFalse(
        "order is empty and " + "am not expecting any order line items.",
        underTest.hasLineItemOfType(item.getClass())
        );
        assertFalse(
        "order is emtpy and am not expecting " + "any order line items",
        underTest.hasLineItemOfType(OrderLineItem.class)
        );

    }

    public void testHasLineItemOfTypeWhichIsSubtype() {
        final AircellUser user = new AircellUser();
        final SimpleOrder underTest = new SimpleOrder(user);

        final OrderLineItemA item = new OrderLineItemA();
        final OrderLineItemB itemB = new OrderLineItemB();

        assertFalse(
        "order is empty and " + "am not expecting any order line items.",
        underTest.hasLineItemOfType(item.getClass())
        );
        assertFalse(
        "order is emtpy and am not expecting " + "any order line items",
        underTest.hasLineItemOfType(OrderLineItem.class)
        );

        underTest.addOrderLineItem(item);

        assertTrue(
        "order has a single order line item and "
        + "of the type we are trying to match",
        underTest.hasLineItemOfType(item.getClass())
        );

        assertFalse(
        "order has a single order line item, "
        + "but defo not of the type we are trying to " + "test for",
        underTest.hasLineItemOfType(itemB.getClass())
        );

        assertTrue(
        "order has a single order line item, "
        + "but should say false for the order line item interface type",
        underTest.hasLineItemOfType(OrderLineItem.class)
        );

        underTest.addOrderLineItem(itemB);

        assertTrue(
        "order has a single order line item, "
        + "but defo not of the type we are trying to " + "test for",
        underTest.hasLineItemOfType(itemB.getClass())
        );

        assertTrue(
        "order has a single order line item and "
        + "of the type we are trying to match",
        underTest.hasLineItemOfType(item.getClass())
        );

        underTest.removeOrderLineItem(item);

        assertTrue(
        "order has an order line item of the type " + "we are testing for",
        underTest.hasLineItemOfType(itemB.getClass())
        );

        assertTrue(
        "order has an order line item and "
        + "and of subtype we are trying to match for",
        underTest.hasLineItemOfType(item.getClass())
        );
    }

    public void testToOrderLineItemArray() {
        final OrderLineItemA item1 = new OrderLineItemA();
        final OrderLineItemA item2 = new OrderLineItemA();
        final AircellUser user = new AircellUser();
        final SimpleOrder underTest = new SimpleOrder(user);

        underTest.addOrderLineItem(item1);
        underTest.addOrderLineItem(item2);

        final Set<OrderLineItem> retval = underTest.getOrderLineItemsReadOnly();
        assertNotNull(retval);
        assertEquals(2, retval.size());
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
                with(aNonNull(Date.class)), with(equal(tracking))
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

    public void testIsEmpty() {
        assertFalse(
        "order expected to have one or more items on setup.",
        underTest.isEmpty()
        );
        underTest.removeOrderLineItem(lineItem);
        assertTrue(
        "order expected to have only one"
        + " item on setup, so this should make the order empty.",
        underTest.isEmpty()
        );
    }

    public void testApplyPromoCode_EmptyOrder() {
        lineItem.setPromoCode(null);
        underTest.removeOrderLineItem(lineItem);
        underTest.applyPromoCode();
        assertNull(lineItem.getPromoCode());
    }

    public void testApplyPromoCode_NonEmptyBasket_ButNoPromoCode() {
        underTest.setPromoCode(null);
        lineItem.setPromoCode(null);
        underTest.applyPromoCode();
        assertNull(lineItem.getPromoCode());
    }

    public void testApplyPromoCode_NonEmptyBasket_WithPromoCode() {
        underTest.setPromoCode(promoCode);
        lineItem.setPromoCode(null);
        underTest.applyPromoCode();
        assertEquals(promoCode, lineItem.getPromoCode());
    }

    public void testExecutePaymentCall_happyPath() {
        processOrderResponse.setSuccess(true);
        processOrderResponse.setPayload(orderRef);
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        checking(
        new Expectations() {
            {
                one(paymentServiceMock).purchaseItems(
                with(equal(loginDetails)), with(equal(cardDetails)),
                with(equal(items)), with(equal(purchaseTime)),
                with(equal(totalDue)), with(equal(true)),
                with(equal(DeviceType)), with(equal(tracking))
                );
                will(returnValue(processOrderResponse));
            }
        }
        );
        checking(configServiceExpectations());

        final ServiceResponse<String> response = underTest.executePaymentCall(
        cardDetails, purchaseTime, totalDue, true, DeviceType, tracking
        );
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(
        "order ref expected to be passed through from the paymetn service",
        orderRef, response.getPayload()
        );
    }

    public void testMakePayment_CalledWhenEmpty() {
        underTest.removeOrderLineItem(lineItem);
        assertTrue(
        "order expected to be emtpy for this test. check your test data setup",
        underTest.isEmpty()
        );
        try {
            underTest.makePayment(cardDetails, false, DeviceType, tracking);
            fail("IllegalStateException expected as the order is empty");
        } catch (IllegalStateException expected) {
        }
    }

    public void testMakePayment_CalledInIncorrectState() {
        underTest.setState(SimpleOrder.State.PAYMENT_CLEARED);
        try {
            underTest.makePayment(cardDetails, false, DeviceType, tracking);
            fail(
            "IllegalStateException should have been "
            + "raised because the order is not in the UNPAID_FOR state"
            );
        } catch (IllegalStateException expected) {
        }
    }

    public void testMakePayment_WhenUserNotInLoggedInState() {
        user.setState(AircellUser.State.NOT_LOGGED_IN);
        checking(
        new Expectations() {
            {
                never(paymentServiceMock);
            }
        }
        );
        try {
            underTest.makePayment(cardDetails, false, DeviceType, tracking);
            fail(
            "IllegalStateException expected as the"
            + " user is not in the logged in state");
        } catch (IllegalStateException expected) {
        }
    }

    public void testMakePayment_UnableToDeterminTotalDue() {
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        calculateOrderResponse.setSuccess(false);
        calculateOrderResponse.setErrorCode(
        BSSErrorCode.
        CANNOT_PERFORM_TAX_CALCULATIONS_BECAUSE_OF_INSUFFICIENT_DATA
        );

        checking(
        new Expectations() {
            {
                one(paymentServiceMock).calculateTaxAndPromotionForItems(
                with(equal(loginDetails)), with(equal(items)),
                with(aNonNull(Date.class)), with(equal(tracking))
                );
                will(returnValue(calculateOrderResponse));
            }
        }
        );
        final ServiceResponse<String> retval =
        underTest.makePayment(cardDetails, true, DeviceType, tracking);
        assertNotNull(retval);
        assertFalse(retval.isSuccess());
        assertEquals(
        BSSErrorCode.
        CANNOT_PERFORM_TAX_CALCULATIONS_BECAUSE_OF_INSUFFICIENT_DATA,
        retval.getErrorCode()
        );
        assertEquals(
        "the order should stay in the unpaid state as"
        + " the operation to determin order total failed",
        SimpleOrder.State.UNPAID_FOR, underTest.getState()
        );
    }

    public void testMakePayment_PaymentClears_SaveCardDetails() {
        final double orderTotal = 11.00;
        final boolean saveCardDetails = true;
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        calculateOrderResponse.setSuccess(true);
        calculateOrderResponse.setPayload(taxDiscount);
        taxDiscount.setTotalAmount(orderTotal);

        processOrderResponse.setSuccess(true);
        processOrderResponse.setPayload(orderRef);

        checking(
        new Expectations() {
            {
                one(paymentServiceMock).calculateTaxAndPromotionForItems(
                with(equal(loginDetails)), with(equal(items)),
                with(aNonNull(Date.class)), with(equal(tracking))
                );
                will(returnValue(calculateOrderResponse));
            }
            {
                one(paymentServiceMock).purchaseItems(
                with(equal(loginDetails)), with(equal(cardDetails)),
                with(equal(items)), with(aNonNull(Date.class)),
                with(equal(orderTotal)), with(equal(saveCardDetails)),
                with(equal(DeviceType)), with(equal(tracking))
                );
                will(returnValue(processOrderResponse));
            }
        }
        );
        final ServiceResponse<String> retval = underTest
        .makePayment(cardDetails, saveCardDetails, DeviceType, tracking);
        assertNotNull(retval);
        assertTrue(retval.isSuccess());
        assertEquals("order ref shuold match", orderRef, retval.getPayload());
        assertEquals(
        "The order shuold have changed state to the payment cleared",
        SimpleOrder.State.PAYMENT_CLEARED, underTest.getState()
        );
        assertNotNull(
        "order shuodl ahve purchase time following successfull clearance",
        underTest.getPurchaseTime()
        );
    }

    public void testMakePayment_PaymentClears_DontCardDetails() {
        final double orderTotal = 11.00;
        final boolean saveCardDetails = false;
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        calculateOrderResponse.setSuccess(true);
        calculateOrderResponse.setPayload(taxDiscount);
        taxDiscount.setTotalAmount(orderTotal);

        processOrderResponse.setSuccess(true);
        processOrderResponse.setPayload(orderRef);

        checking(
        new Expectations() {
            {
                one(paymentServiceMock).calculateTaxAndPromotionForItems(
                with(equal(loginDetails)), with(equal(items)),
                with(aNonNull(Date.class)), with(equal(tracking))
                );
                will(returnValue(calculateOrderResponse));
            }
            {
                one(paymentServiceMock).purchaseItems(
                with(equal(loginDetails)), with(equal(cardDetails)),
                with(equal(items)), with(aNonNull(Date.class)),
                with(equal(orderTotal)), with(equal(saveCardDetails)),
                with(equal(DeviceType)), with(equal(tracking))
                );
                will(returnValue(processOrderResponse));
            }
        }
        );
        final ServiceResponse<String> retval = underTest
        .makePayment(cardDetails, saveCardDetails, DeviceType, tracking);
        assertNotNull(retval);
        assertTrue(retval.isSuccess());
        assertEquals("order ref shuold match", orderRef, retval.getPayload());
        assertEquals(
        "The order shuold have changed state to the payment cleared",
        SimpleOrder.State.PAYMENT_CLEARED, underTest.getState()
        );
        assertNotNull(
        "order shuodl ahve purchase time following successfull clearance",
        underTest.getPurchaseTime()
        );
    }


    public void testMakePayment_PaymentDoesntClear() {
        final double orderTotal = 11.00;
        final boolean saveCardDetails = false;
        final Set<OrderLineItem> items = underTest.getOrderLineItemsReadOnly();
        calculateOrderResponse.setSuccess(true);
        calculateOrderResponse.setPayload(taxDiscount);
        taxDiscount.setTotalAmount(orderTotal);

        processOrderResponse.setSuccess(false);
        processOrderResponse.setErrorCode(
        BSSErrorCode.PAYMENT_BUREAU_UNAVAILABLE_CANNOT_TAKE_PAYMENT
        );

        checking(
        new Expectations() {
            {
                one(paymentServiceMock).calculateTaxAndPromotionForItems(
                with(equal(loginDetails)), with(equal(items)),
                with(aNonNull(Date.class)), with(equal(tracking))
                );
                will(returnValue(calculateOrderResponse));
            }
            {
                one(paymentServiceMock).purchaseItems(
                with(equal(loginDetails)), with(equal(cardDetails)),
                with(equal(items)), with(aNonNull(Date.class)),
                with(equal(orderTotal)), with(equal(saveCardDetails)),
                with(equal(DeviceType)), with(equal(tracking))
                );
                will(returnValue(processOrderResponse));
            }
        }
        );
        final ServiceResponse<String> retval = underTest
        .makePayment(cardDetails, saveCardDetails, DeviceType, tracking);
        assertNotNull(retval);
        assertFalse(retval.isSuccess());
        assertEquals(
        "The order shuold NOT have changed state as the payment didnt clear",
        SimpleOrder.State.UNPAID_FOR, underTest.getState()
        );
        assertNull(
        "order shuodl not ahve purchase time following unsuccessfull clearance",
        underTest.getPurchaseTime()
        );
    }
}
