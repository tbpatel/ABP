/*
 * BSSErrorCode.java 23 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

/**.
 * Represents an error the ABP application services can return. This enum
 * defines all possible error codes.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public enum BSSErrorCode {
    UNSPECIFIED("-1"),
    EXCEPTION("-2"),
    TERMS_CHANGED("-3"),
    SERVICE_UNAVAILABLE("8"),
    WEBSERVICE_CLIENT_EXCEPTION("20"),
    WEBSERVICE_CLIENT_EXCEPTION_SEE_DETAILS("21"),
    MANDATORY_DATA_NOT_SUPPLIED("22"),
    WEBSERVICE_SERVER_FAULT("30"),
    USERNAME_HAS_ALREADY_BEEN_USED("50"),
    EMAIL_HAS_ALREADY_BEEN_USED("51"),
    EMAIL_ADDRESS_DOES_NOT_EXIST("52"),
    USERNAME_DOES_NOT_EXIST("53"),
    PRODUCT_CODE_INVALID("54"),
    PROMO_CODE_INVALID("55"),
    PROMO_CODE_ALREADY_USED_MAX_NUMBER_OF_TIMES("56"),
    PAYMENT_CARD_IS_NOT_VALID("57"),
    PAYMENT_CARD_REJECTED_BY_PAYMENT_BUREAU("58"),
    PAYMENT_BUREAU_UNAVAILABLE_CANNOT_TAKE_PAYMENT("59"),
    SERVICE_ID_DOES_NOT_EXIST("60"),
    CANNOT_PERFORM_TAX_CALCULATIONS_BECAUSE_OF_INSUFFICIENT_DATA("80"),
    PROMO_CODE_EXPIRED("81"),
    PROMO_CODE_INVALID_FOR_FLIGHT("82"),
    PROMO_CODE_INVALID_FOR_PRODUCT("83"),
    PROMO_CODE_INVALID_FOR_DATE("84"),
    PROMO_CODE_DOES_NOT_EXIST("85"),
    PAYMENT_RECEIVED_SERVICE_ACTIVATION_FAILURE("86"),
    PROMO_CODE_FUTURE_START_DATE("87"),
    PAYMENT_PROFILE_NOT_FOUND("88"),
    PAYMENT_DETAILS_MUST_BE_SUPPLIED("89"),
    USERNAME_INVALID("90"),
    PASSWORD_INVALID("91"),
    SERVICE_ALREADY_ACTIVE("92"),
    AAA_USER_CREATION_FAILURE("101"),
    AAA_SERVICE_ACTIVATION_FAILURE("102"),
    AAA_PASSWORD_RESET_FAILURE("103"),
    MEMBER_NOT_FOUND("104"),
    INVALID_CHARACTERS_NEW_PASSWORD("105"),
    INVALID_TRANSACTION_IDENTIFIER("110"),
    INVALID_PAYMENT_CARD_IDENTIFIER("111"),
    ESB_OUTPUT_DATA_INVALID("BWENGINE-100031"),
    ESB_INPUT_DATA_INVALID("BWENGINE-100030");

    private final String errorCode;

    /**.
     * Constructor - Initialize member variables
     * @param errorCode errorCode
     */
    BSSErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**.
     * Gets errorCode
     * @return errorCode
     */
    public String getErrorCode() {
        return this.errorCode;
    }
}
