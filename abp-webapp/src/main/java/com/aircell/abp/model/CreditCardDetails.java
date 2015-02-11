/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.CreditCardValidator;

import java.io.Serializable;
import java.util.Calendar;

/**.
 *
 * This class hold the credit card details
 *
 */
public class CreditCardDetails implements Serializable {
    /**. private class variable serialVersionUID */
    private static final long serialVersionUID = 5628288046837769829L;
    /**. private class variable MONTH_JAVA_OFFSET */
    public static final int MONTH_JAVA_OFFSET = 1;
    /**. private class variable MONTH_MIN_VALUE */
    public static final int MONTH_MIN_VALUE =
    Calendar.JANUARY + MONTH_JAVA_OFFSET;
    /**. private class variable MONTH_MAX_VALUE */
    public static final int MONTH_MAX_VALUE =
    Calendar.DECEMBER + MONTH_JAVA_OFFSET;

    /**. private class variable nameOnCard */
    private String nameOnCard;
    /**. private class variable cardType */
    private String cardType;
    /**. private class variable cardNumber */
    private String cardNumber;
    /**. private class variable securityCode */
    private String securityCode;
    /**. private class variable expiryMonth */
    private String expiryMonth;
    /**. private class variable expiryYear */
    private Integer expiryYear;
    /**. private class variable cardId */
    private String cardId;
    /**. private class variable billingAddress */
    private AddressDetails billingAddress;
    /**. private class variable bSavedCard */
    private boolean bSavedCard = false;

    /* ** Spring configured properties ** */
    /**. private class variable mastercardKey */
    private String mastercardKey;
    /**. private class variable visaKey */
    private String visaKey;
    /**. private class variable amexKey */
    private String amexKey;
    /**. private class variable discoverKey */
    private String discoverKey;


    /**.
     * Gets the Credit Card Number
     * @return cardNumber Credit Card Number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**.
     * Sets Credit Card Number
     * @param cardNumber Credit Card Number
     */
    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }

    /**.
     * Gets Expiry Month
     * @return String Expiry Month
     */
    public String getExpiryMonth() {
        return expiryMonth;
    }

    /**.
     * Sets Expiry Month Zero Based
     * @return Integer
     */
    public Integer getExpireMonthZeroBased() {
        return Integer.parseInt(getExpiryMonth()) - MONTH_JAVA_OFFSET;
    }
    /*Selvi-  For fix AMP  Card Expiration Month field does not accept
     2 characters in Edit Card page, Integer
        type is replced with string type as
         spring doesnt support for 08 and 09.*/

    /**.
     * Sets ExpiryMonth
     * @param expireMonth ExpiryMonth
     */
    public void setExpiryMonth(String expireMonth){
        //logger.debug("CreditCardDetails: setExpiryMonth "+expireMonth);
        this.expiryMonth = (expireMonth);
        //logger.debug("CreditCardDetails: this.expiryMonth"+this.expiryMonth );
    }

    /**.
     * Gets ExpiryYear
     * @return Integer ExpiryYear
     */
    public Integer getExpiryYear() {
        return expiryYear;
    }

    /**.
     * Sets ExpiryYear
     * @param expireYear ExpiryYear
     */
    public void setExpiryYear(Integer expireYear){
        this.expiryYear = expireYear;
    }

    /**.
     * Gets name On Card
     * @return String name On Card
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /**.
     * Sets name On Card
     * @param nameOnCard name On Card
     */
    public void setNameOnCard(String nameOnCard){
        this.nameOnCard = nameOnCard;
    }

    /**.
     * Gets securityCode
     * @return String securityCode
     */
    public String getSecurityCode() {
        return securityCode;
    }

    /**.
     * Sets securityCode
     * @param securityCode securityCode
     */
    public void setSecurityCode(String securityCode){
        this.securityCode = securityCode;
    }

    /**.
     * Gets cardType
     * @return String cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**.
     * Sets cardType
     * @param cardType cardType
     */
    public void setCardType(String cardType){
        this.cardType = cardType;
    }

    /**.
     * Method to retrieve the appropriate type of validator (for card number)
     * based on the card type of this object
     * @return CreditCardValidator appropriate to this card
     */
    public CreditCardValidator getCardTypeValidator() {
        CreditCardValidator validator =
        new CreditCardValidator(CreditCardValidator.NONE);
        String type = getCardType();

        // Card types are hard coded to avoid purchase
        //  failure. this is a temporary fix
        setMastercardKey("MASTER");
        setVisaKey("VISA");
        setAmexKey("AMEX");
        setDiscoverKey("DISCOV");

        if (type != null) {
            if (getMastercardKey().equals(type)) {
                validator =
                new CreditCardValidator(CreditCardValidator.MASTERCARD);
            } else if (getVisaKey().equals(type)) {
                validator = new CreditCardValidator(CreditCardValidator.VISA);
            } else if (getAmexKey().equals(type)) {
                validator = new CreditCardValidator(CreditCardValidator.AMEX);
            } else if (getDiscoverKey().equals(type)) {
                validator =
                new CreditCardValidator(CreditCardValidator.DISCOVER);
            }
        }
        return validator;
    }

    /**.
     * Helper method to determine if the card has expired
     * @return true if it is past the card's expiry date, false otherwise
     */
    public boolean isExpired() {
        Calendar now = Calendar.getInstance();
        Integer nowMonth = now.get(Calendar.MONTH) + MONTH_JAVA_OFFSET;
        Integer nowYear = now.get(Calendar.YEAR);
        nowYear = nowYear % 100;
        // For fix AMP  Card Expiration Month field
        // does not accept 2 characters in Edit Card page
        this.expiryYear = expiryYear % 100;
        boolean expired = (nowYear.intValue() == this.expiryYear.intValue()
        && nowMonth.intValue() > Integer.parseInt((this.expiryMonth))) || (
        nowYear.intValue() > this.expiryYear.intValue());
        return expired;
    }


    /**.
     * Overrides superclass method
     * @return String String
     */
    public String toString(){
        final ToStringBuilder sb = new ToStringBuilder(this);
        sb.append("nameOnCard", getNameOnCard());
        sb.append("cardType", getCardType());
        return sb.toString();
    }

    /**.
     * Parses the expiry date
     * @param expiry date to be parsed
     * @param expectedFormat format
     */
    public void parseExpiryDate(String expiry, String expectedFormat){
        if (StringUtils.isEmpty(expiry) || expiry.length() != 4) {
            throw new IllegalArgumentException("String must be exactly 4 long");
        }

        if ("MMYY".equalsIgnoreCase(expectedFormat)) {
            final int month = Integer.parseInt(expiry.substring(0, 2));
            final int year = Integer.parseInt(expiry.substring(2));
            if (month >= MONTH_MIN_VALUE && month <= MONTH_MAX_VALUE) {
                //  For fix AMP  Card Expiration Month field does
                //  not accept 2 characters in Edit Card page
                setExpiryMonth(Integer.toString(month));
            } else {
                throw new IllegalArgumentException(
                "Month not in acceptable range"
                );
            }
            setExpiryYear(year);
        } else if ("YYMM".equalsIgnoreCase(expectedFormat)) {
            final int year = Integer.parseInt(expiry.substring(0, 2));
            final int month = Integer.parseInt(expiry.substring(2));

            if (month >= MONTH_MIN_VALUE && month <= MONTH_MAX_VALUE) {
                //For fix AMP  Card Expiration Month field does
                //  not accept 2 characters in Edit Card page
                setExpiryMonth(Integer.toString(month));
            } else {
                throw new IllegalArgumentException(
                "Month not in acceptable range"
                );
            }
            setExpiryYear(year);
        } else {
            throw new IllegalArgumentException(
            "Expected format must be either MMYY or YYMM"
            );
        }
    }

    /**.
     * Parses the expiry date
     * @param expiry  dated to be parsed
     */
    public void parseExpiryDate(String expiry){
        parseExpiryDate(expiry, "MMYY");
    }

    /**.
     * Gets Card Id
     * @return String Card Id
     */
    public String getCardId() {
        return cardId;
    }

    /**.
     * Sets Card Id
     * @param cardId Card Id
     */
    public void setCardId(String cardId){
        this.cardId = cardId;
    }

    /**.
     * Gets  <code>BillingAddress</code> from <code>AddressDetails</code>
     * @return AddressDetails
     */
    public AddressDetails getBillingAddress() {
        return billingAddress;
    }

    /**.
     * Sets BillingAddress
     * @param billingAddress AddressDetails object
     */
    public void setBillingAddress(AddressDetails billingAddress){
        this.billingAddress = billingAddress;
    }

    /* ** Spring configured properties ** */
    /**.
     * Gets AmexKey
     * @return the amexKey
     */
    public String getAmexKey() {
        return amexKey;
    }

    /**.
     * Sets AmexKey
     * @param amexKey the amexKey to set
     */
    public void setAmexKey(String amexKey){
        this.amexKey = amexKey;
    }

    /**.
     * Gets DiscoverKey
     * @return the discoverKey
     */
    public String getDiscoverKey() {
        return discoverKey;
    }

    /**.
     * Sets DiscoverKey
     * @param discoverKey the discoverKey to set
     */
    public void setDiscoverKey(String discoverKey) {
        this.discoverKey = discoverKey;
    }

    /**.
     * Gets MastercardKey
     * @return the mastercardKey
     */
    public String getMastercardKey() {
        return mastercardKey;
    }

    /**.
     * Sets MastercardKey
     * @param mastercardKey the mastercardKey to set
     */
    public void setMastercardKey(String mastercardKey){
        this.mastercardKey = mastercardKey;
    }

    /**.
     * gets VisaKey
     * @return the visaKey
     */
    public String getVisaKey() {
        return visaKey;
    }

    /**.
     * Sets VisaKey
     * @param visaKey the visaKey to set
     */
    public void setVisaKey(String visaKey){
        this.visaKey = visaKey;
    }

    /**.
     * Gets SavedCardFlag
     * @return the bSavedCard
     */
    public boolean getSavedCardFlag() {
        return this.bSavedCard;
    }

    /**.
     * Sets SavedCardFlag
     * @param bSavedCard the bSavedCard to set
     */
    public void setSavedCardFlag(boolean bSavedCard){
        this.bSavedCard = bSavedCard;
    }
}
