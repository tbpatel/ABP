/*
 * AAAErrorCode.java 20 Sep 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import java.io.Serializable;

/**.
 * Error codes coming from the AAA system.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public enum AAAErrorCode implements Serializable {
    INVALID_USERID("1"),
    INVALID_DEVICE_TYPE("2"),
    UNABLE_TO_FIND_DEVICE("3"),
    INVALID_PASSWORD("4"),
    UNABLE_TO_AUTHENTICATE_SUBSCRIBER_CREDENTIALS("5"),
    INACTIVE("6"),
    ILLEGAL_CONFIGURATION("7"),
    ILLEGAL_XML("9"),
    USERID_ALREADY_IN_USE("10"),
    INVALID_REQUEST("11"),
    REQUIRED_DATA("12"),
    UNABLE_TO_START_SUBSCRIBER_SESSION("13"),
    UNABLE_TO_FIND_LOCATION("14"),
    UNABLE_TO_START_SERVICE("15"),
    UNABLE_TO_STOP_SERVICE("16"),
    UNABLE_TO_GET_ACTIVE_STATUS("17"),
    INVALID_URL_ENCODING("18"),
    NO_SOAP_BODY("19"),
    UNABLE_TO_CREATE_SUBSCRIBER("20"),
    UNABLE_TO_FIND_SUBSCRIBER_INFORMATION("21"),
    UNABLE_TO_PROCESS_PAYMENT("22"),
    UNABLE_TO_DELETE_SUBSCRIBER("23"),
    UNABLE_TO_DELETE_SUBSCRIBER_PAYMENT_ACCOUNT("24"),
    UNABLE_TO_QUERY_SUBSCIRBER_USAGE("25"),
    UNABLE_TO_FIND_MEMBER("26"),
    PAYMENT_METHOD_CREDIT_CARD_ONLY("27"),
    NO_CREDIT_CARD_INFORMATION("28"),
    NO_ORDER("29"),
    UNABLE_TO_REMOVE_SUBSCRIBED_PRODUCTS("30"),
    NO_SUBSCRIBED_PRODUCTS("31"),
    UNABLE_TO_FIND_ACCOUNT_BUNDLE("32"),
    UNABLE_TO_REMOVE_ACCOUNT_BUNDLE("33"),
    UNABLE_TO_REMOVE_SUBSCRIBER_PLAN("34"),
    UNABLE_TO_END_SUBSCRIBER_SESSION("35"),
    UNABLE_TO_EXECUTE_POLICY("36"),
    UNABLE_TO_UPDATE_SUBSCRIBER("37"),
    UNABLE_TO_DELETE_MEMBER_IDENTITY("38"),
    UNABLE_TO_UPDATE_MEMBER_IDENTITY("39"),
    UNABLE_TO_ADD_MEMBER_IDENTITY("40"),
    NO_MEMBER_IDENTITY("41"),
    UNABLE_TO_ADD_CREDIT_CARD_ACCOUNT("42"),
    UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER("43"),
    UNABLE_TO_ASSIGN_SERVICE_TO_SUBSCRIBER_WITH_PAYMENT("44"),
    NO_PAYMENT("45"),
    UNABLE_TO_CHANGE_PASSWORD("46"),
    MAX_SUCCESS_URL_LENGTH("47"),
    MAX_ERROR_URL_LENGTH("48"),
    FIND_ALL_PRODUCT_BUNDLES("49"),
    FIND_ALL_PRODUCT_BUNDLES_FOR_LOCATION("50");

    private final String errorCode;

    /**.
     * Constructor - Initialize member varialbles
     * @param errorCode errorCode
     */
    AAAErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**.
     * Gets error code
     * @return errorcode
     */
    public String getErrorCode() {
        return this.errorCode;
    }
}
