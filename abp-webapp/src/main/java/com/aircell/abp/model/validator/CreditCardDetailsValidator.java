/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model.validator;


import com.aircell.abp.model.CreditCardDetails;
import com.aircell.abp.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.CreditCardValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**.
 * Validator implementation for <code>CreditCardDetails</code> objects.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public class CreditCardDetailsValidator {
    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**. Configurable max length for name on card field - spring */
    private int nameOnCardMaxLength;
    /**. Configurable name on card Regex for name on card field - spring */
    private String nameOnCardRegex;
    /**. Configurable min length for security code - spring */
    private int securityCodeMinLength;
    /**. Configurable max length for security code - spring */
    private int securityCodeMaxLength;
    // TODO optimize - as it stands, this model
    //TODO validator depends on a serivce. Move card type validation elsewhere?
    private ConfigService configService;

    /**. @see org.springframework.validation.Validator#
     * supports(java.lang.Class) */
    public boolean supports(final Class clazz) {
        return CreditCardDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     */
    public void validate(
    CreditCardDetails cardDetails, MessageContext context
    ) {

        logger.debug("CreditCardDetailsValidator.validate" + " Enter..");

        // -- nameOnCard
        String name = cardDetails.getNameOnCard();
        if (StringUtils.isNotBlank(name)
        && name.length() > getNameOnCardMaxLength()) {
            context.addMessage(
            new MessageBuilder().error().source("nameOnCard")
            .code("creditCardDetails.nameOnCard.tooLong").build()
            );
        }
        if (StringUtils.isNotBlank(name)) {
            Pattern p = Pattern.compile("^\\w[^/%|\\^\\\\&<]*$");
            Matcher m = p.matcher(name);
            if (!m.find() || !m.matches()) {
                context.addMessage(
                new MessageBuilder().error().source("nameOnCard")
                .code("purchaseForm.creditcard.name.syntax").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error().source("nameOnCard")
            .code("purchaseForm.creditcard.name.mandatory").build()
            );
        }

        // -- cardType
        if (!getConfigService().getSupportedCardTypes().isEmpty()) {
            final String cardType = cardDetails.getCardType();
            if (StringUtils.isNotBlank(cardType) && !getConfigService()
            .getSupportedCardTypes().containsKey(cardType)) {
                context.addMessage(
                new MessageBuilder().error().source("cardType")
                .code("creditCardDetails.cardType.invalid").build()
                );
            }
            if (!getConfigService().getSupportedCardTypes()
            .containsKey(cardType)) {

                context.addMessage(
                new MessageBuilder().error().source("cardType")
                .code("purchaseForm.creditcard.type.madatory").build()
                );
            }
        } else {
            logger.warn(
            "Credit card options list on the credit card details object "
            + "is empty! Please check your Spring configuration - these objects "
            + "are @Configurable(). Validation of credit card type ignored."
            );
        }



        // -- cardNumber
        validateCardNumber(cardDetails, context);

        // -- securityCode
        validateSecurityCode(cardDetails, context);

        // -- expiryMonth and expiryYear
        Calendar now = Calendar.getInstance();
        final int currentMonth = now.get(Calendar.MONTH);
        final int currentYear = now.get(Calendar.YEAR);

        validateCardExpiry(cardDetails, context, currentMonth, currentYear);
        logger.debug("CreditCardDetailsValidator.validate" + " Exit..");
    }


    /**.
     * Method to validate CardNumber
     * @param card CreditCardDetails
     * @param context MessageContext
     */
    public void validateCardNumber(
    CreditCardDetails card, MessageContext context
    ) {
        String cardNumber = card.getCardNumber();
        if (StringUtils.isBlank(cardNumber)) {
            context.addMessage(
            new MessageBuilder().error().source("cardNumber")
            .code("purchaseForm.creditcard.number.mandatory").build()
            );
        }

        CreditCardValidator creditCardValidator = card.getCardTypeValidator();
        // VD-450 Fix - will throw only "CreditCard Number is Mandatory"
        // and no other errors - when CardNumber is left empty
        if (cardNumber != null && !cardNumber.trim().equals("")) {
            if (!cardNumber.matches("[0-9]{1,}")) {
                context.addMessage(
                new MessageBuilder().error().source("cardNumber")
                .code("purchaseForm.creditcard.number.syntax").build()
                );
            } else if (!creditCardValidator.isValid(cardNumber)) {
                context.addMessage(
                new MessageBuilder().error().source("cardNumber")
                .code("purchaseForm.creditcard.number.invalid").build()
                );
            }
        }
    }

    /**.
     * Method to Validate SecurityCode
     * @param card
     * @param context
     */
    public void validateSecurityCode(
    CreditCardDetails card, MessageContext context
    ) {
        String securityCode = card.getSecurityCode();
        if (StringUtils.isNotBlank(card.getSecurityCode())) {
            //Added by Rajasekar
            //To fix, error message display in purchase screen and to
            // disable purchase button,If Card type is AMEX and
            //  security code is less than 4,
            if (card.getCardType().equals(card.getAmexKey())
            && securityCode.length() < getSecurityCodeMaxLength()) {
                context.addMessage(
                new MessageBuilder().error().source("securityCode")
                .code("creditCardDetails.securityCode.tooShort").build()
                );
            } else if (securityCode.length() < getSecurityCodeMinLength()) {
                context.addMessage(
                new MessageBuilder().error().source("securityCode")
                .code("creditCardDetails.securityCode.tooShort").build()
                );
            } else if (securityCode.length() > getSecurityCodeMaxLength(card)) {
                context.addMessage(
                new MessageBuilder().error().source("securityCode")
                .code("creditCardDetails.securityCode.tooLong").build()
                );
            }
            if (!securityCode.matches("[0-9]{1,}")) {
                context.addMessage(
                new MessageBuilder().error().source("securityCode")
                .code("purchaseForm.creditcard.securityCode.syntax").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error().source("securityCode")
            .code("purchaseForm.creditcard.securityCode.mandatory").build()
            );
        }
    }

    /**.
     * Method to validate CardExpiry
     * @param ccd CreditCardDetails
     * @param context MessageContext
     */
    public void validateCardExpiry(
    final CreditCardDetails ccd, MessageContext context
    ) {

        Calendar now = Calendar.getInstance();
        final int currentMonth = now.get(Calendar.MONTH);
        final int currentYear = now.get(Calendar.YEAR);
        validateCardExpiry(ccd, context, currentMonth, currentYear);
    }

    /**.
     * Method to validate CardExpiry
     * @param ccd
     * @param context
     * @param currentMonthZeroBased
     * @param currentYear
     */
    public void validateCardExpiry(
    final CreditCardDetails ccd, MessageContext context,
    final int currentMonthZeroBased, final int currentYear
    ) {
        if (ccd == null || context == null) {
            throw new IllegalArgumentException(
            "CreditCardDetails and Errors args must be non-null"
            );
        }
        if (currentMonthZeroBased < Calendar.JANUARY
        || currentMonthZeroBased > Calendar.DECEMBER) {
            throw new IllegalArgumentException(
            "currentMonth arg has to be valid month integer"
            );
        }

        if (StringUtils.isBlank(ccd.getExpiryMonth())) {
            context.addMessage(
            new MessageBuilder().error().source("expiryMonth")
            .code("purchaseForm.creditcard.expmonth.mandatory").build()
            );
        }
        if (ccd.getExpiryYear() == null) {
            context.addMessage(
            new MessageBuilder().error().source("expiryYear")
            .code("purchaseForm.creditcard.expyear.mandatory").build()
            );
        }

        boolean isvalidMonth = true;
        try {

            Integer.parseInt(ccd.getExpiryMonth());
        } catch (Exception e) {
            isvalidMonth = false;
        }
        // -- expiryYear
        if (ccd.getExpiryYear() != null && (ccd.getExpiryYear()
        < currentYear)) {
            context.addMessage(
            new MessageBuilder().error().source("expiryYear")
            .code("creditCardDetails.expiryYear.expired").build()
            );
        }
    }


    /**.
     * Gets SecurityCodeMaxLength for specified card type
     * @param card
     * @return int maxLength
     */
    public int getSecurityCodeMaxLength(CreditCardDetails card) {
        String type = card.getCardType();
        int maxLength = 4;
        if (type != null) {
            if (card.getMastercardKey().equals(type)
            || card.getVisaKey().equals(type) || card.getDiscoverKey()
            .equals(type)) {
                maxLength = 3;
            } else if (card.getAmexKey().equals(type)) {
                // default is correct
                maxLength = 4;
            }
        }
        return maxLength;
    }

    /**.
     * Gets SecurityCodeMaxLength
     * @return int securityCodeMaxLength
     */
    public int getSecurityCodeMaxLength() {
        return this.securityCodeMaxLength;
    }

    /**.
     * Sets SecurityCodeMaxLength
     * @param secruityCodeMaxLength
     */
    public void setSecurityCodeMaxLength(int secruityCodeMaxLength) {
        this.securityCodeMaxLength = secruityCodeMaxLength;
    }

    /**.
     * Gets Regular Expression for NameOnCard
     * @return String nameOnCardRegex
     */
    public String getNameOnCardRegex() {
        return this.nameOnCardRegex;
    }

    /**.
     * Sets Regular Expression for NameOnCard
     * @param nameOnCardRegex
     */
    public void setNameOnCardRegex(String nameOnCardRegex) {
        this.nameOnCardRegex = nameOnCardRegex;
    }

    /**.
     * Gets Minimum Length for SecurityCode
     * @return int securityCodeMinLength
     */
    public int getSecurityCodeMinLength() {
        return securityCodeMinLength;
    }

    /**.
     * Sets Minimum Length for SecurityCode
     * @param securityCodeMinLength
     */
    public void setSecurityCodeMinLength(int securityCodeMinLength) {
        this.securityCodeMinLength = securityCodeMinLength;
    }

    /**.
     * Gets ConfigService
     * @return ConfigService
     */
    public ConfigService getConfigService() {
        return configService;
    }

    /**.
     * Sets ConfigService
     * @param configService
     */
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /**.
     * Gets Max length for NameOnCard
     * @return int nameOnCardMaxLength
     */
    public int getNameOnCardMaxLength() {
        return nameOnCardMaxLength;
    }

    /**.
     * Sets Max length for NameOnCard
     * @param nameOnCardMaxLength
     */
    public void setNameOnCardMaxLength(final int nameOnCardMaxLength) {
        this.nameOnCardMaxLength = nameOnCardMaxLength;
    }
}
