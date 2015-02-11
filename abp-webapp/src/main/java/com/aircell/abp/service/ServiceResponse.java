/*
 * ServiceResponse.java 23 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import org.apache.commons.lang.builder.ToStringBuilder;

/**.
 * Generic class which can encapsulate response information to a call made to a
 * <code>domain</code> or a <code>service</code> object. Calls to domain or
 * service objects usually involve sending some data and can result in success
 * or failure.  Success or failure should be taken in the business sense.
 * Failures can be result of either genuine business failure (such as invalid
 * login details) or real exception (database went down). Either way objects of
 * this class can hold the information about failure conditions. This class
 * shoud be used such that if the <code>isSuccess</code> returns false, then
 * either <code>getErrorCode</code> or <code>getErrorCause</code> should return
 * an object (a non-null values). However, whilst this usage is stipulated, bear
 * in mind that it is not required. Additionally, this class makes use of Java
 * 1.5 Generics feature. This is so that the type of the <code>payload</code>
 * object can be specified at compile time.  For more info on the Generics, have
 * a look at: http://java.sun.com/j2se/1.5/pdf/generics-tutorial.pdf
 * @author miroslav.miladinovic at AKQA Inc.
 */
public class ServiceResponse<T> {

    /**. private class variable to hold  boolean value*/
    private boolean success;
    /**. private enum to hold  errorCode */
    private Enum errorCode;
    /**. private class variable to hold  gbperrorCode */
	private String gbperrorCode;
    /**. private class variable to hold  errorText */
    private String errorText;
    /**. private class variable to hold  errorCause */
    private Throwable errorCause;
    /**. private class variable to hold  payload */
    private T payload;
    /**. private class variable to hold  purchaseTime */
    private String purchaseTime;

    /**.
     * Sets the success flag to false.
     */
    public ServiceResponse() {
        this.success = false;
    }

    /**.
     *
     * @param success the value of the success flag
     */
    public ServiceResponse(final boolean success) {
        this.success = success;
    }

    /**.
     *
     * @param success the value of the success flag
     * @param errorCode error code enum - can be null
     */
    public ServiceResponse(final boolean success, final Enum errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    /**.
     *
     * @param success boolean value
     * @param gbperrorCode GBP error code
     */
    public ServiceResponse(final boolean success, final String gbperrorCode) {
        this.success = success;
        this.gbperrorCode = gbperrorCode;
    }

    /**.
     * @return the purchase time
     */
    public String getPurchaseTime() {
        return purchaseTime;
    }

    /**.
     * @param purchaseTime the purchase time to set
     */
    public void setPurchaseTime(final String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    /**.
     * Gets GbperrorCode
     * @return String gbperrorCode
     */
    public String getGbperrorCode() {
        return gbperrorCode;
    }

    /**.
     * Sets GbperrorCode
     * @param gbperrorCode GbperrorCode
     */
    public void setGbperrorCode(final String gbperrorCode) {
        this.gbperrorCode = gbperrorCode;
    }
    /**.
     * @return the errorText
     */
    public String getErrorText() {
        return errorText;
    }

    /**.
     * @param errorText the errorText to set
     */
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }

    /**.
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**.
     * @param success the success to set
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**.
     * @return the errorCode
     */
    public Enum getErrorCode() {
        return errorCode;
    }

    /**.
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(final Enum errorCode) {
        this.errorCode = errorCode;
    }

    /**.
     * @return the payload
     */
    public T getPayload() {
        return payload;
    }

    /**.
     * @param payload the payload to set
     */
    public void setPayload(final T payload) {
        this.payload = payload;
    }

    /**.
     * @return the errorCause
     */
    public Throwable getErrorCause() {
        return errorCause;
    }

    /**.
     * @param errorCause the errorCause to set
     */
    public void setErrorCause(final Throwable errorCause) {
        this.errorCause = errorCause;
    }

    /**.
     * Uses the Apache Commons <code>ToStringBuilder</code>.
     * @see java.lang.Object#toString()
     * @return String String value
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
