/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

//import com.aircell.shared.web.servlet.session.AircellSessionManager;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.validation.Errors;

import com.aircell.abp.model.AircellUser;
import com.aircell.abp.model.CreditCardDetails;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.InFlightProductCatalog;
import com.aircell.abp.model.SecurityChallengeDetails;
import com.aircell.abp.model.SubscriptionProduct;
import com.aircell.abp.service.AAAErrorCode;
import com.aircell.abp.service.BSSErrorCode;
import com.aircell.abp.service.ConfigService;
import com.aircell.abp.service.ServiceResponse;
import com.aircell.abp.web.servlet.session.AircellSessionManager;
import com.aircell.abs.acpu.common.AcpuErrorCode;
import com.aircell.bss.ws.TrackingType;

/**.
 * Shared utils which call services
 * @author AKQA - bryan.swift
 * @version $Revision: 3613 $
 */
public class AircellServiceUtils {
    /**. Number of minutes cached products are good for in a session */
    public static final Integer PRODUCTS_EXPIRE_AFTER_MINUTES = 15;

    /**.
     * Method to get a SubscriptionProduct by a provided productCode
     * @param productCode - code for product to retrieve
     * @param flight - flight to find product for
     * @return SubscriptionProduct if one found corresponding to productCode,
     *         null otherwise
     */
    public static SubscriptionProduct getProductByCode(
    AircellSessionManager session, String productCode, FlightInformation flight,
    String Locale, String DeviceType, TrackingType tracking
    ) {
        List<SubscriptionProduct> products =
        new ArrayList<SubscriptionProduct>();
        if (session.getProducts() != null) {
            products = session.getProducts();
        } else {
            final InFlightProductCatalog catalog = new InFlightProductCatalog();
            ServiceResponse<SubscriptionProduct[]> productsResponse = catalog
            .getSubscriptionsForSale(flight, Locale, DeviceType, tracking);
            if (productsResponse.isSuccess()) {
                SubscriptionProduct[] productArray =
                productsResponse.getPayload();
                products = Arrays.asList(productArray);
                session.setProducts(products);
            }
        }
        SubscriptionProduct product = null;
        for (SubscriptionProduct cProduct : products) {
            String cProductCode = cProduct.getProductCode();
            if (cProductCode.equals(productCode)) {
                product = cProduct;
                break;
            }
        }
        return product;
    }

    /**. @return  */
    public static List<String> getYearList() {
        List<String> years = new ArrayList<String>();
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        for (int i = currentYear; i < currentYear + 11;
             i++) { //Incremented the year count @ Satish
            Integer year = new Integer(i);
            years.add(year.toString());
        }
        return years;
    }

    /**.
     * Method to get the list of allowed password challenge questions
     * @return List<String> of password challenge questions
     */
    public static List<Map<String, String>> getForgotPasswordQuestionOptions(
    String locale, TrackingType tracking
    ) {
        List<Map<String, String>> questions =
        new ArrayList<Map<String, String>>();
        AircellUser service = new AircellUser();
        SecurityChallengeDetails[] challengeDetails =
        service.getSecurityChallengeOptionsPassword(locale, tracking);
        for (SecurityChallengeDetails challenge : challengeDetails) {
            Map<String, String> questionMap = new HashMap<String, String>();
            String question = challenge.getQuestion();
            String id = new Integer(challenge.getId()).toString();
            questionMap.put("id", id);
            questionMap.put("question", question);
            questions.add(questionMap);
        }
        return questions;
    }

    /**.
     * Method to get the list of allowed username challenge questions
     * @return List<String> of username challenge questions
     */
    public static List<Map<String, String>> getForgotUsernameQuestionOptions(
    String locale, TrackingType tracking
    ) {
        List<Map<String, String>> questions =
        new ArrayList<Map<String, String>>();
        AircellUser service = new AircellUser();
        SecurityChallengeDetails[] challengeDetails =
        service.getSecurityChallengeOptionsUsername(locale, tracking);
        for (SecurityChallengeDetails challenge : challengeDetails) {
            Map<String, String> questionMap = new HashMap<String, String>();
            String question = challenge.getQuestion();
            String id = challenge.getId().toString();
            questionMap.put("id", id);
            questionMap.put("question", question);
            questions.add(questionMap);
        }
        return questions;
    }

    /**.
     * Method to get the list of allowed card types
     * @param configService ABPConfigService reference in
     * order to retrieve cards
     * @return List of card type mappings value -> label
     */
    public static List<Map<String, String>> getCardTypes(
    ConfigService configService
    ) {
        List<Map<String, String>> cardTypes =
        new ArrayList<Map<String, String>>();
        Map<String, String> supportedTypes =
        configService.getSupportedCardTypes();
        Set<String> values = supportedTypes.keySet();
        for (String value : values) {
            String label = supportedTypes.get(value);
            Map<String, String> cardType = new HashMap<String, String>();
            cardType.put("value", value);
            cardType.put("label", label);
            cardTypes.add(cardType);
        }
        return cardTypes;
    }

    /**.
     * Method to get the list of supported countries
     * @param configService ABPConfigService reference in order to retrieve
     * countries
     * @param locale Locale for which to retrieve country names (labels)
     * @return List of country mappings value -> label
     */
    public static List<Map<String, String>> getCountriesByLocale(
    ConfigService configService, Locale locale
    ) {
        List<Map<String, String>> countries =
        new ArrayList<Map<String, String>>();
        Map<String, String> supportedCountries =
        configService.getSupportedUserCountries(locale);
        Set<String> values = supportedCountries.keySet();
        for (String value : values) {
            String label = supportedCountries.get(value);
            Map<String, String> country = new HashMap<String, String>();
            country.put("value", value);
            country.put("label", label);
            countries.add(country);
        }
        return countries;
    }

    /**.
     * Method to get the list of supported states
     * @param configService ABPConfigService reference
     *  in order to retrieve states
     * @param locale Locale for which to retrieve state names (labels)
     * @return List of state mappings value -> label
     */
    public static List<Map<String, String>> getStatesByLocale(
    ConfigService configService, Locale locale
    ) {
        List<Map<String, String>> states = new ArrayList<Map<String, String>>();
        Map<String, String> supportedStates =
        configService.getSupportedUserUSStates(locale);
        Set<String> values = supportedStates.keySet();
        for (String value : values) {
            String label = supportedStates.get(value);
            Map<String, String> state = new HashMap<String, String>();
            state.put("value", value);
            state.put("label", label);
            states.add(state);
        }
        return states;
    }

    /**.
     * Retrieve the valid United States country codes
     * @param configService ABPConfigService reference in order
     * to retrieve country codes
     * @return Set of us country codes
     */
    public static Set<String> getUsCountryCodes(ConfigService configService) {
        return configService.getCountryCodesForUS();
    }

    /**.
     * Determine whether or not a given country code is a US country
     * @param configService ConfigService reference in order
     * to retrieve country codes
     * @param code to test
     * @return true if code is a us country code
     */
    public static boolean isUsCountryCode(
    ConfigService configService, String code
    ) {
        Set<String> usCodes = getUsCountryCodes(configService);
        return usCodes.contains(code);
    }

    /**.
     * Helper method to copy needed card information into a new object
     * @param creditCard - CreditCardDetails to copy
     * @return CreditCardDetails with only displayed credit card information
     *         copied
     */
    public static CreditCardDetails copyCreditCard(
    CreditCardDetails creditCard
    ) {
        CreditCardDetails card = new CreditCardDetails();
        card.setNameOnCard(creditCard.getNameOnCard());
        String number = creditCard.getCardNumber();
        if (number.length() >= 4) {
            int numXes = number.length() - 4;
            StringBuffer bNumber = new StringBuffer();
            for (int i = 0; i < numXes; i++) {
                bNumber.append("x");
            }
            bNumber.append(number.substring(numXes));
            card.setCardNumber(bNumber.toString());
        } else {
            card.setCardNumber(number);
        }
        card.setCardType(creditCard.getCardType());
        card.setSecurityCode(creditCard.getSecurityCode());
        card.setExpiryMonth(creditCard.getExpiryMonth());
        card.setExpiryYear(creditCard.getExpiryYear());
        card.setBillingAddress(creditCard.getBillingAddress());
        card.setSavedCardFlag(creditCard.getSavedCardFlag());
        return card;
    }

    /**.
     * @param response
     * @param errors
     * @param defaultMessage
     */
    public static void errorsFromErrorCodes(
    ServiceResponse response, Errors errors, String defaultMessage
    ) {
        Enum code = response.getErrorCode();
        // map AAAErrorCode and AcpuErrorCode values
        if (BSSErrorCode
        .CANNOT_PERFORM_TAX_CALCULATIONS_BECAUSE_OF_INSUFFICIENT_DATA
        .equals(code)) {
            errors.reject(
            "error.cannotPerformTaxCalculationsBecauseOfInsufficientData",
            "error.cannotPerformTaxCalculationsBecauseOfInsufficientData"
            );
        } else if (BSSErrorCode.ESB_INPUT_DATA_INVALID.equals(code)) {
            errors.reject("error.esbInputInvalid", "error.esbInputInvalid");
        } else if (BSSErrorCode.ESB_OUTPUT_DATA_INVALID.equals(code)) {
            errors.reject("error.esbOutputInvalid", "error.esbOutputInvalid");
        } else if (BSSErrorCode.MANDATORY_DATA_NOT_SUPPLIED.equals(code)) {
            errors.reject("error.missingData", "error.missingData");
        } else if (BSSErrorCode.PASSWORD_INVALID.equals(code)) {
            errors.reject("error.passwordInvalid", "error.passwordInvalid");
        } else if (BSSErrorCode.PAYMENT_BUREAU_UNAVAILABLE_CANNOT_TAKE_PAYMENT
        .equals(code)) {
            errors.reject(
            "error.paymentBureauUnavailable", "error.paymentBureauUnavailable"
            );
        } else if (BSSErrorCode.PAYMENT_CARD_IS_NOT_VALID.equals(code)) {
            errors
            .reject("error.paymentCardNotValid", "error.paymentCardNotValid");
        } else if (BSSErrorCode.PAYMENT_CARD_REJECTED_BY_PAYMENT_BUREAU
        .equals(code)) {
            errors
            .reject("error.paymentCardRejected", "error.paymentCardRejected");
        } else if (BSSErrorCode.PRODUCT_CODE_INVALID.equals(code)) {
            errors
            .reject("error.productCodeInvalid", "error.productCodeInvalid");
        } else if (BSSErrorCode.PROMO_CODE_ALREADY_USED_MAX_NUMBER_OF_TIMES
        .equals(code)) {
            errors.reject("error.promoCodeMaxUsed", "error.promoCodeMaxUsed");
        } else if (BSSErrorCode.PROMO_CODE_DOES_NOT_EXIST.equals(code)
        || BSSErrorCode.PROMO_CODE_INVALID.equals(code)) {
            errors.reject(
            "error.promoCodeDoesNotExist", "error.promoCodeDoesNotExist"
            );
        } else if (BSSErrorCode.PROMO_CODE_EXPIRED.equals(code)) {
            errors.reject("error.promoCodeExpired", "error.promoCodeExpired");
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_DATE.equals(code)) {
            errors.reject(
            "error.promoCodeInvalidForDate", "error.promoCodeInvalidForDate"
            );
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_FLIGHT.equals(code)) {
            errors.reject(
            "error.promoCodeInvalidForFlight", "error.promoCodeInvalidForFlight"
            );
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_PRODUCT.equals(code)) {
            errors.reject(
            "error.promoCodeInvalidForProduct",
            "error.promoCodeInvalidForProduct"
            );
        } else if (AcpuErrorCode.ACPU_CLIENT_ERORR.equals(code)) {
            errors.reject("error.acpuClientError", "error.acpuClientError");
        } else if (AcpuErrorCode.ACPU_COMPONENT_DOWN.equals(code)) {
            errors.reject("error.acpuComponentDown", "error.acpuComponentDown");
        } else if (AcpuErrorCode.ACPU_EXTERNAL_STATE_ERROR.equals(code)) {
            errors.reject(
            "error.acpuExternalStateError", "error.acpuExternalStateError"
            );
        } else if (AcpuErrorCode.ACPU_FIREWALL_NOT_RESPONDING.equals(code)) {
            errors.reject(
            "error.acpuFirewallNotResponding", "error.acpuFirewallNotResponding"
            );
        } else if (AcpuErrorCode.ACPU_FIREWALL_UPDATE_ERROR.equals(code)) {
            errors.reject(
            "error.acpuFirewallUpdateError", "error.acpuFirewallUpdateError"
            );
        } else if (AcpuErrorCode.ACPU_SERVER_ERROR.equals(code)) {
            errors.reject("error.acputServerError", "error.acputServerError");
        } else if (AcpuErrorCode.ACPU_SERVER_NOT_OPERATIONAL.equals(code)) {
            errors.reject(
            "error.acpuServerNotOperational", "error.acpuServerNotOperational"
            );
        } else if (AcpuErrorCode.ACPU_SERVER_SHUTDOWN_IN_PROGRESS
        .equals(code)) {
            errors.reject(
            "error.acpuServerShutdownInProgress",
            "error.acpuServerShutdownInProgress"
            );
        } else if (AcpuErrorCode.ATG_LINK_DOWN.equals(code)) {
            errors.reject("error.atgLinkDown", "error.atgLinkDown");
        } else if (AcpuErrorCode.AUTHENTICATION_REJECTED.equals(code)) {
            errors.reject(
            "error.acpuAuthenticationRejected",
            "error.acpuAuthenticationRejected"
            );
        } else if (AcpuErrorCode.INCORRECT_RADIUS_SHARED_SECRET.equals(code)) {
            errors.reject(
            "error.acpuIncorrectRadiusSharedSecret",
            "error.acpuIncorrectRadiusSharedSecret"
            );
        } else if (AcpuErrorCode.NO_ERROR.equals(code)) {
            errors.reject("error.noError", "error.noError");
        } else if (AcpuErrorCode.OUT_OF_COVERAGE_AREA.equals(code)) {
            errors.reject("error.outOfCoverageArea", "error.outOfCoverageArea");
        } else if (AcpuErrorCode.RADIUS_SERVER_ERROR.equals(code)) {
            errors.reject("error.radiusServerError", "error.radiusServerError");
        } else if (AcpuErrorCode.RADIUS_SERVER_NOT_REACHABLE.equals(code)) {
            errors.reject(
            "error.radiusServerNotReachable", "error.radiusServerNotReachable"
            );
        } else if (AcpuErrorCode.SUBSCRIPTION_REJECTED.equals(code)) {
            errors
            .reject("error.subscriptionRejected", "error.subscriptionRejected");
        } else if (AcpuErrorCode.UNASSIGNED_CLIENT_IP_ADDRESS.equals(code)) {
            errors.reject(
            "error.unassignedClientIpAddress", "error.unassignedClientIpAddress"
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_CLIENT_ERORR.equals(code)) {
            errors.reject(
            "error.apcuUnknownClientError", "error.apcuUnknownClientError"
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_EXTERNAL_STATE_ERROR
        .equals(code)) {
            errors.reject(
            "error.acpuUnknownExternalStateError",
            "error.acpuUnknownExternalStateError"
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_SERVER_ERROR.equals(code)) {
            errors.reject(
            "error.acpuUnknownServerError", "error.acpuUnknownServerError"
            );
        } else if (AcpuErrorCode.UNKNOWN_RADIUS_SERVER_ERROR.equals(code)) {
            errors.reject(
            "error.acpuUnknownRadiusServerError",
            "error.acpuUnknownRadiusServerError"
            );
        } else if (AAAErrorCode.FIND_ALL_PRODUCT_BUNDLES.equals(code)) {
            errors.reject(
            "error.aaaFindAllProductBundles", "error.aaaFindAllProductBundles"
            );
        } else if (AAAErrorCode.FIND_ALL_PRODUCT_BUNDLES_FOR_LOCATION
        .equals(code)) {
            errors.reject(
            "error.aaaFindAllProductBundlesLocation",
            "error.aaaFindAllProductBundlesLocation"
            );
        }
        else if (AAAErrorCode.INVALID_REQUEST.equals(code)) {
            errors.reject("error.aaaInvalidRequest", "error.aaaInvalidRequest");
        } else if (AAAErrorCode.INVALID_URL_ENCODING.equals(code)) {
            errors.reject(
            "error.aaaInvalidUrlEncoding", "error.aaaInvalidUrlEncoding"
            );
        } else if (AAAErrorCode.MAX_ERROR_URL_LENGTH.equals(code)) {
            errors
            .reject("error.aaaMaxErrorUrlLength", "error.aaaMaxErrorUrlLength");
        } else if (AAAErrorCode.MAX_SUCCESS_URL_LENGTH.equals(code)) {
            errors.reject(
            "error.aaaMaxSuccessUrlLength", "error.aaaMaxSuccessUrlLength"
            );
        } else if (AAAErrorCode.NO_CREDIT_CARD_INFORMATION.equals(code)) {
            errors.reject(
            "error.aaaNoCreditCardInformation",
            "error.aaaNoCreditCardInformation"
            );
        } else if (AAAErrorCode.NO_MEMBER_IDENTITY.equals(code)) {
            errors
            .reject("error.aaaNoMemberIdentity", "error.aaaNoMemberIdentity");
        } else if (AAAErrorCode.NO_ORDER.equals(code)) {
            errors.reject("error.aaaNoOrder", "error.aaaNoOrder");
        } else if (AAAErrorCode.NO_PAYMENT.equals(code)) {
            errors.reject("error.aaaNoPayment", "error.aaaNoPayment");
        } else if (AAAErrorCode.NO_SOAP_BODY.equals(code)) {
            errors.reject("error.aaaNoSoapBody", "error.aaaNoSoapBody");
        } else if (AAAErrorCode.NO_SUBSCRIBED_PRODUCTS.equals(code)) {
            errors.reject(
            "error.aaaNoSubscribedProducts", "error.aaaNoSubscribedProducts"
            );
        } else if (AAAErrorCode.PAYMENT_METHOD_CREDIT_CARD_ONLY.equals(code)) {
            errors.reject(
            "error.aaaPaymentMethodCardOnly", "error.aaaPaymentMethodCardOnly"
            );
        } else if (AAAErrorCode.REQUIRED_DATA.equals(code)) {
            errors.reject("error.aaaRequiredData", "error.aaaRequiredData");
        } else if (AAAErrorCode.UNABLE_TO_ADD_CREDIT_CARD_ACCOUNT
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToAddCreditCard", "error.aaaUnableToAddCreditCard"
            );
        } else if (AAAErrorCode.UNABLE_TO_ADD_MEMBER_IDENTITY.equals(code)) {
            errors.reject(
            "error.aaaUnableToAddMemberIdentity",
            "error.aaaUnableToAddMemberIdentity"
            );
        } else if (AAAErrorCode.UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToAssignServiceToSubscriber",
            "error.aaaUnableToAssignServiceToSubscriber"
            );
        } else if (AAAErrorCode
        .UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER_WITH_PAYMENT.equals(code)) {
            errors.reject(
            "error.aaaUnableToAssignServiceToSubscriberWithPayment",
            "error.aaaUnableToAssignServiceToSubscriberWithPayment"
            );
        } else if (AAAErrorCode.UNABLE_TO_AUTHENTICATE_SUBSCRIBER_CREDENTIALS
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToAuthenticateSubscriber",
            "error.aaaUnableToAuthenticateSubscriber"
            );
        } else if (AAAErrorCode.UNABLE_TO_CHANGE_PASSWORD.equals(code)) {
            errors.reject(
            "error.aaaUnableToChangePassword", "error.aaaUnableToChangePassword"
            );
        } else if (AAAErrorCode.UNABLE_TO_CREATE_SUBSCRIBER.equals(code)) {
            errors.reject(
            "error.aaaUnableToCreateSubscriber",
            "error.aaaUnableToCreateSubscriber"
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_MEMBER_IDENTITY.equals(code)) {
            errors.reject(
            "error.aaaUnableToDeleteMemberIdentity",
            "error.aaaUnableToDeleteMemberIdentity"
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_SUBSCRIBER.equals(code)) {
            errors.reject(
            "error.aaaUnableToDeleteSubscriber",
            "error.aaaUnableToDeleteSubscriber"
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_SUBSCRIBER_PAYMENT_ACCOUNT
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToDeleteSubscriberPaymentAccount",
            "error.aaaUnableToDeleteSubscriberPaymentAccount"
            );
        } else if (AAAErrorCode.UNABLE_TO_END_SUBSCRIBER_SESSION.equals(code)) {
            errors.reject(
            "error.aaaUnableToEndSubscriberSession",
            "error.aaaUnableToEndSubscriberSession"
            );
        } else if (AAAErrorCode.UNABLE_TO_EXECUTE_POLICY.equals(code)) {
            errors.reject(
            "error.aaaUnableToExecutePolicy", "error.aaaUnableToExecutePolicy"
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_ACCOUNT_BUNDLE.equals(code)) {
            errors.reject(
            "error.aaaUnableToFindAccountBundle",
            "error.aaaUnableToFindAccountBundle"
            );
        }
        else if (AAAErrorCode.UNABLE_TO_FIND_LOCATION.equals(code)) {
            errors.reject(
            "error.aaaUnableToFindLocation", "error.aaaUnableToFindLocation"
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_MEMBER.equals(code)) {
            errors.reject(
            "error.aaaUnableToFindMember", "error.aaaUnableToFindMember"
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_SUBSCRIBER_INFORMATION
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToFindSubscriberInformation",
            "error.aaaUnableToFindSubscriberInformation"
            );
        } else if (AAAErrorCode.UNABLE_TO_GET_ACTIVE_STATUS.equals(code)) {
            errors.reject(
            "error.aaaAunableToGetActiveStatus",
            "error.aaaAunableToGetActiveStatus"
            );
        } else if (AAAErrorCode.UNABLE_TO_PROCESS_PAYMENT.equals(code)) {
            errors.reject(
            "error.aaaUnableToProcessPayment", "error.aaaUnableToProcessPayment"
            );
        } else if (AAAErrorCode.UNABLE_TO_QUERY_SUBSCIRBER_USAGE.equals(code)) {
            errors.reject(
            "error.aaaUnableToQuerySubscriberUsage",
            "error.aaaUnableToQuerySubscriberUsage"
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_ACCOUNT_BUNDLE.equals(code)) {
            errors.reject(
            "error.aaaUnableToRemoveAccountBundle",
            "error.aaaUnableToRemoveAccountBundle"
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_SUBSCRIBED_PRODUCTS
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToRemoveSubscribedProducts",
            "error.aaaUnableToRemoveSubscribedProducts"
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_SUBSCRIBER_PLAN.equals(code)) {
            errors.reject(
            "error.aaaUnableToRemoveSubscriberPlan",
            "error.aaaUnableToRemoveSubscriberPlan"
            );
        } else if (AAAErrorCode.UNABLE_TO_START_SERVICE.equals(code)) {
            errors.reject(
            "error.aaaUnableToStartService", "error.aaaUnableToStartService"
            );
        } else if (AAAErrorCode.UNABLE_TO_START_SUBSCRIBER_SESSION
        .equals(code)) {
            errors.reject(
            "error.aaaUnableToStartSubscriberSession",
            "error.aaaUnableToStartSubscriberSession"
            );
        } else if (AAAErrorCode.UNABLE_TO_STOP_SERVICE.equals(code)) {
            errors.reject(
            "error.aaaUnableToStopService", "error.aaaUnableToStopService"
            );
        } else if (AAAErrorCode.UNABLE_TO_UPDATE_MEMBER_IDENTITY.equals(code)) {
            errors.reject(
            "error.aaaUnableToUpdateMemberIdentity",
            "error.aaaUnableToUpdateMemberIdentity"
            );
        } else if (AAAErrorCode.UNABLE_TO_UPDATE_SUBSCRIBER.equals(code)) {
            errors.reject(
            "error.aaaUnableToUpdateSubscriber",
            "error.aaaUnableToUpdateSubscriber"
            );
        }
        // Updatded Error Code list for MAPA Release - Mahi
        else if (BSSErrorCode.UNSPECIFIED.equals(code)) {
            errors.reject("error.unspecified", "error.unspecified");
        } else if (BSSErrorCode.EXCEPTION.equals(code)) {
            errors.reject("error.exception", "error.exception");
        } else if (BSSErrorCode.TERMS_CHANGED.equals(code)) {
            errors.reject("error.termsChanged", "error.termsChanged");
        } else if (BSSErrorCode.SERVICE_UNAVAILABLE.equals(code)) {
            errors
            .reject("error.serviceUnavailable", "error.serviceUnavailable");
        } else if (BSSErrorCode.WEBSERVICE_CLIENT_EXCEPTION.equals(code)) {
            errors.reject(
            "error.webserviceClientException", "error.webserviceClientException"
            );
        } else if (BSSErrorCode.WEBSERVICE_CLIENT_EXCEPTION_SEE_DETAILS
        .equals(code)) {
            errors.reject(
            "error.webserviceClientExceptionSeeDetails",
            "error.webserviceClientExceptionDetails"
            );
        } else if (BSSErrorCode.WEBSERVICE_SERVER_FAULT.equals(code)) {
            errors.reject(
            "error.webserviceServerFault", "error.webserviceServerFault"
            );
        } else if (BSSErrorCode.USERNAME_HAS_ALREADY_BEEN_USED.equals(code)) {
            errors.reject(
            "error.usernameHasAlreadyBeenUsed",
            "error.usernameHasAlreadyBeenUsed"
            );
        } else if (BSSErrorCode.EMAIL_HAS_ALREADY_BEEN_USED.equals(code)) {
            errors.reject(
            "error.emailHasAlreadyBeenUsed", "error.emailHasAlreadyBeenUsed"
            );
        } else if (BSSErrorCode.EMAIL_ADDRESS_DOES_NOT_EXIST.equals(code)) {
            errors.reject(
            "error.emailAddressDoesNotExist", "error.emailAddressDoesNotExist"
            );
        } else if (BSSErrorCode.USERNAME_DOES_NOT_EXIST.equals(code)) {
            errors
            .reject("error.usernameDoesNotExist", "error.usernameDoesNotExist");
        } else if (BSSErrorCode.SERVICE_ID_DOES_NOT_EXIST.equals(code)) {
            errors.reject(
            "error.serviceIdDoesNotExist", "error.serviceIdDoesNotExist"
            );
        } else if (BSSErrorCode.PAYMENT_RECEIVED_SERVICE_ACTIVATION_FAILURE
        .equals(code)) {
            errors.reject(
            "error.paymentReceivedServiceActivationFailure",
            "error.paymentReceivedServiceActivationFailure"
            );
        } else if (BSSErrorCode.PROMO_CODE_FUTURE_START_DATE.equals(code)) {
            errors.reject(
            "error.promoCodeFutureStartDate", "error.promoCodeFutureStartDate"
            );
        } else if (BSSErrorCode.PAYMENT_DETAILS_MUST_BE_SUPPLIED.equals(code)) {
            errors.reject(
            "error.paymentDetailsMustBeSupplied",
            "error.paymentDetailsMustBeSupplied"
            );
        } else if (BSSErrorCode.PAYMENT_PROFILE_NOT_FOUND.equals(code)) {
            errors.reject(
            "error.paymentProfileNotFound", "error.paymentProfileNotFound"
            );
        } else if (BSSErrorCode.SERVICE_ALREADY_ACTIVE.equals(code)) {
            errors
            .reject("error.serviceAlreadyActive", "error.serviceAlreadyActive");
        } else if (BSSErrorCode.AAA_USER_CREATION_FAILURE.equals(code)) {
            errors.reject(
            "error.aaaUserCreationFailure", "error.aaaUserCreationFailure"
            );
        } else if (BSSErrorCode.AAA_SERVICE_ACTIVATION_FAILURE.equals(code)) {
            errors.reject(
            "error.aaaServiceActivationFailure",
            "error.aaaServiceActivationFailure"
            );
        } else if (BSSErrorCode.AAA_PASSWORD_RESET_FAILURE.equals(code)) {
            errors
            .reject("error.passwordResetFailure", "error.passwordResetFailure");
        } else if (BSSErrorCode.MEMBER_NOT_FOUND.equals(code)) {
            errors.reject("error.memberNotFound", "error.memberNotFound");
        } else if (BSSErrorCode.INVALID_CHARACTERS_NEW_PASSWORD.equals(code)) {
            errors.reject(
            "error.invalidCharactersNewPassword",
            "error.invalidCharactersNewPassword"
            );
        } else if (BSSErrorCode.INVALID_TRANSACTION_IDENTIFIER.equals(code)) {
            errors.reject(
            "error.invalidTransactionIdentifier",
            "error.invalidTransactionIdentifier"
            );
        } else if (BSSErrorCode.INVALID_PAYMENT_CARD_IDENTIFIER.equals(code)) {
            errors.reject(
            "error.invalidPaymentCardIdentifier",
            "error.invalidPaymentCardIdentifier"
            );
        } else if (BSSErrorCode.USERNAME_INVALID.equals(code)) {
            errors.reject("error.usernameInvalid", "error.usernameInvalid");
        }
    }

    /**.
     * @param response
     * @param errors
     * @param defaultMessage
     */
    public static void contextsFromErrorCodes(
    ServiceResponse response, MessageContext errors, String defaultMessage
    ) {
        Enum code = response.getErrorCode();
        

        if (BSSErrorCode
        .CANNOT_PERFORM_TAX_CALCULATIONS_BECAUSE_OF_INSUFFICIENT_DATA
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source(
            "error.cannotPerformTaxCalculationsBecauseOfInsufficientData"
            ).code(
            "error.cannotPerformTaxCalculationsBecauseOfInsufficientData"
            ).build()
            );
        } else if (BSSErrorCode.ESB_INPUT_DATA_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.esbInputInvalid")
            .code("error.esbInputInvalid").build()
            );
        } else if (BSSErrorCode.ESB_OUTPUT_DATA_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.esbOutputInvalid")
            .code("error.esbOutputInvalid").build()
            );
        } else if (BSSErrorCode.MANDATORY_DATA_NOT_SUPPLIED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.missingData")
            .code("error.missingData").build()
            );
        } else if (BSSErrorCode.PASSWORD_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.passwordInvalid")
            .code("error.passwordInvalid").build()
            );
        } else if (BSSErrorCode.PAYMENT_BUREAU_UNAVAILABLE_CANNOT_TAKE_PAYMENT
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.paymentBureauUnavailable")
            .code("error.paymentBureauUnavailable").build()
            );
        } else if (BSSErrorCode.PAYMENT_CARD_IS_NOT_VALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.paymentCardNotValid")
            .code("error.paymentCardNotValid").build()
            );
        } else if (BSSErrorCode.PAYMENT_CARD_REJECTED_BY_PAYMENT_BUREAU
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.paymentCardRejected")
            .code("error.paymentCardRejected").build()
            );
        } else if (BSSErrorCode.PRODUCT_CODE_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.productCodeInvalid")
            .code("error.productCodeInvalid").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_ALREADY_USED_MAX_NUMBER_OF_TIMES
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.promoCodeMaxUsed")
            .code("error.promoCodeMaxUsed").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_DOES_NOT_EXIST.equals(code)
        || BSSErrorCode.PROMO_CODE_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.promoCodeDoesNotExist")
            .code("error.promoCodeDoesNotExist").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_EXPIRED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.promoCodeExpired")
            .code("error.promoCodeExpired").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_DATE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.promoCodeInvalidForDate")
            .code("error.promoCodeInvalidForDate").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_FLIGHT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.promoCodeInvalidForFlight")
            .code("error.promoCodeInvalidForFlight").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_INVALID_FOR_PRODUCT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.promoCodeInvalidForProduct")
            .code("error.promoCodeInvalidForProduct").build()
            );
        } else if (AcpuErrorCode.ACPU_CLIENT_ERORR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acpuClientError")
            .code("error.acpuClientError").build()
            );
        } else if (AcpuErrorCode.ACPU_COMPONENT_DOWN.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acpuComponentDown")
            .code("error.acpuComponentDown").build()
            );
        } else if (AcpuErrorCode.ACPU_EXTERNAL_STATE_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acpuExternalStateError")
            .code("error.acpuExternalStateError").build()
            );
        } else if (AcpuErrorCode.ACPU_FIREWALL_NOT_RESPONDING.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuFirewallNotResponding")
            .code("error.acpuFirewallNotResponding").build()
            );
        } else if (AcpuErrorCode.ACPU_FIREWALL_UPDATE_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acpuFirewallUpdateError")
            .code("error.acpuFirewallUpdateError").build()
            );
        } else if (AcpuErrorCode.ACPU_SERVER_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acputServerError")
            .code("error.acputServerError").build()
            );
        } else if (AcpuErrorCode.ACPU_SERVER_NOT_OPERATIONAL.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuServerNotOperational")
            .code("error.acpuServerNotOperational").build()
            );
        } else if (AcpuErrorCode.ACPU_SERVER_SHUTDOWN_IN_PROGRESS
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuServerShutdownInProgress")
            .code("error.acpuServerShutdownInProgress").build()
            );
        } else if (AcpuErrorCode.ATG_LINK_DOWN.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.atgLinkDown")
            .code("error.atgLinkDown").build()
            );
        } else if (AcpuErrorCode.AUTHENTICATION_REJECTED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuAuthenticationRejected")
            .code("error.acpuAuthenticationRejected").build()
            );
        } else if (AcpuErrorCode.INCORRECT_RADIUS_SHARED_SECRET.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuIncorrectRadiusSharedSecret")
            .code("error.acpuIncorrectRadiusSharedSecret").build()
            );
        } else if (AcpuErrorCode.NO_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.noError")
            .code("error.noError").build()
            );
        } else if (AcpuErrorCode.OUT_OF_COVERAGE_AREA.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.outOfCoverageArea")
            .code("error.outOfCoverageArea").build()
            );
        } else if (AcpuErrorCode.RADIUS_SERVER_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.radiusServerError")
            .code("error.radiusServerError").build()
            );
        } else if (AcpuErrorCode.RADIUS_SERVER_NOT_REACHABLE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.radiusServerNotReachable")
            .code("error.radiusServerNotReachable").build()
            );
        } else if (AcpuErrorCode.SUBSCRIPTION_REJECTED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.subscriptionRejected")
            .code("error.subscriptionRejected").build()
            );
        } else if (AcpuErrorCode.UNASSIGNED_CLIENT_IP_ADDRESS.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.unassignedClientIpAddress")
            .code("error.unassignedClientIpAddress").build()
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_CLIENT_ERORR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.apcuUnknownClientError")
            .code("error.apcuUnknownClientError").build()
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_EXTERNAL_STATE_ERROR
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuUnknownExternalStateError")
            .code("error.acpuUnknownExternalStateError").build()
            );
        } else if (AcpuErrorCode.UNKNOWN_ACPU_SERVER_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.acpuUnknownServerError")
            .code("error.acpuUnknownServerError").build()
            );
        } else if (AcpuErrorCode.UNKNOWN_RADIUS_SERVER_ERROR.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.acpuUnknownRadiusServerError")
            .code("error.acpuUnknownRadiusServerError").build()
            );
        } else if (AAAErrorCode.FIND_ALL_PRODUCT_BUNDLES.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaFindAllProductBundles")
            .code("error.aaaFindAllProductBundles").build()
            );
        } else if (AAAErrorCode.FIND_ALL_PRODUCT_BUNDLES_FOR_LOCATION
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaFindAllProductBundlesLocation")
            .code("error.aaaFindAllProductBundlesLocation").build()
            );
        }
        else if (AAAErrorCode.INVALID_REQUEST.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaInvalidRequest")
            .code("error.aaaInvalidRequest").build()
            );
        } else if (AAAErrorCode.INVALID_URL_ENCODING.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaInvalidUrlEncoding")
            .code("error.aaaInvalidUrlEncoding").build()
            );
        } else if (AAAErrorCode.MAX_ERROR_URL_LENGTH.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaMaxErrorUrlLength")
            .code("error.aaaMaxErrorUrlLength").build()
            );
        } else if (AAAErrorCode.MAX_SUCCESS_URL_LENGTH.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaMaxSuccessUrlLength")
            .code("error.aaaMaxSuccessUrlLength").build()
            );
        } else if (AAAErrorCode.NO_CREDIT_CARD_INFORMATION.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaNoCreditCardInformation")
            .code("error.aaaNoCreditCardInformation").build()
            );
        } else if (AAAErrorCode.NO_MEMBER_IDENTITY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaNoMemberIdentity")
            .code("error.aaaNoMemberIdentity").build()
            );
        } else if (AAAErrorCode.NO_ORDER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaNoOrder")
            .code("error.aaaNoOrder").build()
            );
        } else if (AAAErrorCode.NO_PAYMENT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaNoPayment")
            .code("error.aaaNoPayment").build()
            );
        } else if (AAAErrorCode.NO_SOAP_BODY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaNoSoapBody")
            .code("error.aaaNoSoapBody").build()
            );
        } else if (AAAErrorCode.NO_SUBSCRIBED_PRODUCTS.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaNoSubscribedProducts")
            .code("error.aaaNoSubscribedProducts").build()
            );
        } else if (AAAErrorCode.PAYMENT_METHOD_CREDIT_CARD_ONLY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaPaymentMethodCardOnly")
            .code("error.aaaPaymentMethodCardOnly").build()
            );
        } else if (AAAErrorCode.REQUIRED_DATA.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaRequiredData")
            .code("error.aaaRequiredData").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_ADD_CREDIT_CARD_ACCOUNT
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToAddCreditCard")
            .code("error.aaaUnableToAddCreditCard").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_ADD_MEMBER_IDENTITY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToAddMemberIdentity")
            .code("error.aaaUnableToAddMemberIdentity").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToAssignServiceToSubscriber")
            .code("error.aaaUnableToAssignServiceToSubscriber").build()
            );
        } else if (AAAErrorCode
        .UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER_WITH_PAYMENT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToAssignServiceToSubscriberWithPayment")
            .code(
            "error.aaaUnableToAssignServiceToSubscriberWithPayment"
            ).build()
            );
        } else if (AAAErrorCode.UNABLE_TO_AUTHENTICATE_SUBSCRIBER_CREDENTIALS
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToAuthenticateSubscriber")
            .code("error.aaaUnableToAuthenticateSubscriber").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_CHANGE_PASSWORD.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToChangePassword")
            .code("error.aaaUnableToChangePassword").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_CREATE_SUBSCRIBER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToCreateSubscriber")
            .code("error.aaaUnableToCreateSubscriber").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_MEMBER_IDENTITY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToDeleteMemberIdentity")
            .code("error.aaaUnableToDeleteMemberIdentity").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_SUBSCRIBER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToDeleteSubscriber")
            .code("error.aaaUnableToDeleteSubscriber").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_DELETE_SUBSCRIBER_PAYMENT_ACCOUNT
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToDeleteSubscriberPaymentAccount")
            .code("error.aaaUnableToDeleteSubscriberPaymentAccount").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_END_SUBSCRIBER_SESSION.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToEndSubscriberSession")
            .code("error.aaaUnableToEndSubscriberSession").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_EXECUTE_POLICY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToExecutePolicy")
            .code("error.aaaUnableToExecutePolicy").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_ACCOUNT_BUNDLE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToFindAccountBundle")
            .code("error.aaaUnableToFindAccountBundle").build()
            );
        }
        else if (AAAErrorCode.UNABLE_TO_FIND_LOCATION.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaUnableToFindLocation")
            .code("error.aaaUnableToFindLocation").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_MEMBER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaUnableToFindMember")
            .code("error.aaaUnableToFindMember").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_FIND_SUBSCRIBER_INFORMATION
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToFindSubscriberInformation")
            .code("error.aaaUnableToFindSubscriberInformation").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_GET_ACTIVE_STATUS.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaAunableToGetActiveStatus")
            .code("error.aaaAunableToGetActiveStatus").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_PROCESS_PAYMENT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToProcessPayment")
            .code("error.aaaUnableToProcessPayment").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_QUERY_SUBSCIRBER_USAGE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToQuerySubscriberUsage")
            .code("error.aaaUnableToQuerySubscriberUsage").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_ACCOUNT_BUNDLE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToRemoveAccountBundle")
            .code("error.aaaUnableToRemoveAccountBundle").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_SUBSCRIBED_PRODUCTS
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToRemoveSubscribedProducts")
            .code("error.aaaUnableToRemoveSubscribedProducts").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_REMOVE_SUBSCRIBER_PLAN.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToRemoveSubscriberPlan")
            .code("error.aaaUnableToRemoveSubscriberPlan").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_START_SERVICE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaUnableToStartService")
            .code("error.aaaUnableToStartService").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_START_SUBSCRIBER_SESSION
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToStartSubscriberSession")
            .code("error.aaaUnableToStartSubscriberSession").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_STOP_SERVICE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaUnableToStopService")
            .code("error.aaaUnableToStopService").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_UPDATE_MEMBER_IDENTITY.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToUpdateMemberIdentity")
            .code("error.aaaUnableToUpdateMemberIdentity").build()
            );
        } else if (AAAErrorCode.UNABLE_TO_UPDATE_SUBSCRIBER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaUnableToUpdateSubscriber")
            .code("error.aaaUnableToUpdateSubscriber").build()
            );
        }
        // Updatded Error Code list for MAPA Release - Mahi
        else if (BSSErrorCode.UNSPECIFIED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.unspecified")
            .code("error.unspecified").build()
            );
        } else if (BSSErrorCode.EXCEPTION.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.exception")
            .code("error.exception").build()
            );
        } else if (BSSErrorCode.TERMS_CHANGED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.termsChanged")
            .code("error.termsChanged").build()
            );
        } else if (BSSErrorCode.SERVICE_UNAVAILABLE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.serviceUnavailable")
            .code("error.serviceUnavailable").build()
            );
        } else if (BSSErrorCode.WEBSERVICE_CLIENT_EXCEPTION.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.webserviceClientException")
            .code("error.webserviceClientException").build()
            );
        } else if (BSSErrorCode.WEBSERVICE_CLIENT_EXCEPTION_SEE_DETAILS
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.webserviceClientExceptionSeeDetails")
            .code("error.webserviceClientExceptionDetails").build()
            );
        } else if (BSSErrorCode.WEBSERVICE_SERVER_FAULT.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.webserviceServerFault")
            .code("error.webserviceServerFault").build()
            );
        } else if (BSSErrorCode.USERNAME_HAS_ALREADY_BEEN_USED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.usernameHasAlreadyBeenUsed")
            .code("error.usernameHasAlreadyBeenUsed").build()
            );
        } else if (BSSErrorCode.EMAIL_HAS_ALREADY_BEEN_USED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.emailHasAlreadyBeenUsed")
            .code("error.emailHasAlreadyBeenUsed").build()
            );
        } else if (BSSErrorCode.EMAIL_ADDRESS_DOES_NOT_EXIST.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.emailAddressDoesNotExist")
            .code("error.emailAddressDoesNotExist").build()
            );
        } else if (BSSErrorCode.USERNAME_DOES_NOT_EXIST.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.usernameDoesNotExist")
            .code("error.usernameDoesNotExist").build()
            );
        } else if (BSSErrorCode.SERVICE_ID_DOES_NOT_EXIST.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.serviceIdDoesNotExist")
            .code("error.serviceIdDoesNotExist").build()
            );
        } else if (BSSErrorCode.PAYMENT_RECEIVED_SERVICE_ACTIVATION_FAILURE
        .equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.paymentReceivedServiceActivationFailure")
            .code("error.paymentReceivedServiceActivationFailure").build()
            );
        } else if (BSSErrorCode.PROMO_CODE_FUTURE_START_DATE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.promoCodeFutureStartDate")
            .code("error.promoCodeFutureStartDate").build()
            );
        } else if (BSSErrorCode.PAYMENT_DETAILS_MUST_BE_SUPPLIED.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.paymentDetailsMustBeSupplied")
            .code("error.paymentDetailsMustBeSupplied").build()
            );
        } else if (BSSErrorCode.PAYMENT_PROFILE_NOT_FOUND.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.paymentProfileNotFound")
            .code("error.paymentProfileNotFound").build()
            );
        } else if (BSSErrorCode.SERVICE_ALREADY_ACTIVE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.serviceAlreadyActive")
            .code("error.serviceAlreadyActive").build()
            );
        } else if (BSSErrorCode.AAA_USER_CREATION_FAILURE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.aaaUserCreationFailure")
            .code("error.aaaUserCreationFailure").build()
            );
        } else if (BSSErrorCode.AAA_SERVICE_ACTIVATION_FAILURE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.aaaServiceActivationFailure")
            .code("error.aaaServiceActivationFailure").build()
            );
        } else if (BSSErrorCode.AAA_PASSWORD_RESET_FAILURE.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.passwordResetFailure")
            .code("error.passwordResetFailure").build()
            );
        } else if (BSSErrorCode.MEMBER_NOT_FOUND.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.memberNotFound")
            .code("error.memberNotFound").build()
            );
        } else if (BSSErrorCode.INVALID_CHARACTERS_NEW_PASSWORD.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.invalidCharactersNewPassword")
            .code("error.invalidCharactersNewPassword").build()
            );
        } else if (BSSErrorCode.INVALID_TRANSACTION_IDENTIFIER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.invalidTransactionIdentifier")
            .code("error.invalidTransactionIdentifier").build()
            );
        } else if (BSSErrorCode.INVALID_PAYMENT_CARD_IDENTIFIER.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error()
            .source("error.invalidPaymentCardIdentifier")
            .code("error.invalidPaymentCardIdentifier").build()
            );
        } else if (BSSErrorCode.USERNAME_INVALID.equals(code)) {
            errors.addMessage(
            new MessageBuilder().error().source("error.usernameInvalid").
            code("error.usernameInvalid").build());
        }

    }

}
