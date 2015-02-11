/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.model;

/**.
 * Command class for the ForgotController
 * @author AKQA - bryan.swift
 * @version $Revision: 3654 $
 */
public class ForgotForm extends LoginDetails {
    /**. class variable to hold serialVersionUID */
    private static final long serialVersionUID = -4999380899842806036L;
    /**. Answer to the given security question */
    private String answer;
    /**. Answer to the card security question */
    private String cardAnswer;
    /**. Answer to the address security question */
    private String addressAnswer;
    /**. User's email address */
    private String email;
    /**. Last four digits of a credit card if the user has one saved */
    private String lastFourDigits;
    /**. The user's new password repeated */
    private String confirmPassword;
    /**. User's billing zip code if the user has
     * saved credit card information */
    private String zipcode;
    /**. True if terms of use  is accepted */
    private boolean termsofuse;

    /**.
     * Has user accepted terms
     * @return String answer
     */
    public String getAnswer() {
        return answer;
    }

    /**. @param answer the answer to set */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**. @return the addressAnswer */
    public String getAddressAnswer() {
        return addressAnswer;
    }

    /**. @param addressAnswer the addressAnswer to set */
    public void setAddressAnswer(String addressAnswer) {
        this.addressAnswer = addressAnswer;
    }

    /**. @return the cardAnswer */
    public String getCardAnswer() {
        return cardAnswer;
    }

    /**. @param cardAnswer the cardAnswer to set */
    public void setCardAnswer(String cardAnswer) {
        this.cardAnswer = cardAnswer;
    }

    /**. @return the email */
    public String getEmail() {
        return email;
    }

    /**. @param email the email to set */
    public void setEmail(String email) {
        this.email = email;
    }

    /**. @return the lastFourDigits */
    public String getLastFourDigits() {
        return lastFourDigits;
    }

    /**. @param lastFourDigits the lastFourDigits to set */
    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    /**. @return the confirmPassword */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**. @param confirmPassword the confirmPassword to set */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**. @return the zipcode */
    public String getZipcode() {
        return zipcode;
    }

    /**. @param zipcode the zipcode to set */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**.
     * Checks Termsofuse checked or not
     * @return boolean
     */
    public boolean isTermsofuse() {
        return termsofuse;
    }

    /**.
     * Sets Termsofuse
     * @param termsofuse true if termsofuse is checked, or else false
     */
    public void setTermsofuse(boolean termsofuse) {
        this.termsofuse = termsofuse;
    }
}
