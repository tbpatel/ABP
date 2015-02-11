/*
 * CreateAccountForm.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 * 
 * Redistribution and use in source and binary forms, without consent 
 * are strictly forbidden. 
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.web.form;

import com.aircell.abp.model.AddressDetails;
import com.aircell.abp.model.LoginDetails;
import com.aircell.abp.model.PersonalDetails;
import com.aircell.abp.model.SecurityChallengeDetails;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**.
 * . Command class for the Create Account Form. Please refer to the Aircell UI
 * design.
 * @author miroslav.miladinovic
 */
public class CreateAccountForm implements Serializable {

    /**. . */
    private static final long serialVersionUID = -476746461513168896L;

    private PersonalDetails personalDetails;
    private AddressDetails addressDetails;
    private String emailAddress;
    private String phoneNumber;
    private String confirmEmailAddress;

    private String usPhoneNumber1;
    private String usPhoneNumber2;
    private String usPhoneNumber3;
    private String intlPhoneNumber;

    private LoginDetails loginDetails;
    private String confirmPassword;
    private String flag;

    private SecurityChallengeDetails usernameReminder;
    private SecurityChallengeDetails passwordReminder;

    private boolean signupForMarketing;

    public CreateAccountForm() {
        this.personalDetails = new PersonalDetails();
        this.loginDetails = new LoginDetails();
        this.addressDetails = new AddressDetails();
        this.usernameReminder = new SecurityChallengeDetails();
        this.usernameReminder.setId(-1);
        this.passwordReminder = new SecurityChallengeDetails();
        this.passwordReminder.setId(-1);
    }

    public String getConfirmEmailAddress() {
        return confirmEmailAddress;
    }

    public void setConfirmEmailAddress(String confirmEmailAddress) {
        this.confirmEmailAddress = confirmEmailAddress;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    public SecurityChallengeDetails getPasswordReminder() {
        return passwordReminder;
    }

    public void setPasswordReminder(SecurityChallengeDetails passwordReminder) {
        this.passwordReminder = passwordReminder;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public SecurityChallengeDetails getUsernameReminder() {
        return usernameReminder;
    }

    public void setUsernameReminder(SecurityChallengeDetails usernameReminder) {
        this.usernameReminder = usernameReminder;
    }

    public boolean isSignupForMarketing() {
        return signupForMarketing;
    }

    public void setSignupForMarketing(boolean signupForMarketing) {
        this.signupForMarketing = signupForMarketing;
    }

    /**.
     * .
     * @return the addressDetails
     */
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    /**.
     * .
     * @param addressDetails the addressDetails to set
     */
    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    /**.
     * .
     * @return the intlPhoneNumber
     */
    public String getIntlPhoneNumber() {
        return intlPhoneNumber;
    }

    /**.
     * .
     * @param intlPhoneNumber the intlPhoneNumber to set
     */
    public void setIntlPhoneNumber(String intlPhoneNumber) {
        this.intlPhoneNumber = intlPhoneNumber;
    }

    /**.
     * .
     * @return the usPhoneNumber1
     */
    public String getUsPhoneNumber1() {
        return usPhoneNumber1;
    }

    /**.
     * .
     * @param usPhoneNumber1 the usPhoneNumber1 to set
     */
    public void setUsPhoneNumber1(String usPhoneNumber1) {
        this.usPhoneNumber1 = usPhoneNumber1;
    }

    /**.
     * .
     * @return the usPhoneNumber2
     */
    public String getUsPhoneNumber2() {
        return usPhoneNumber2;
    }

    /**.
     * .
     * @param usPhoneNumber2 the usPhoneNumber2 to set
     */
    public void setUsPhoneNumber2(String usPhoneNumber2) {
        this.usPhoneNumber2 = usPhoneNumber2;
    }

    /**.
     * .
     * @return the usPhoneNumber3
     */
    public String getUsPhoneNumber3() {
        return usPhoneNumber3;
    }

    /**.
     * .
     * @param usPhoneNumber3 the usPhoneNumber3 to set
     */
    public void setUsPhoneNumber3(String usPhoneNumber3) {
        this.usPhoneNumber3 = usPhoneNumber3;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(final String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
