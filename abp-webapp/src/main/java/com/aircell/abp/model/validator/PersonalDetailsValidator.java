/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model.validator;

import com.aircell.abp.model.PersonalDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**.
 * Responsible for processing the PersonalDetails of an AircellUser and ensuring
 * the values meet the requirements
 * @author AKQA - bryan.swift
 * @version $Revision: 3606 $
 */
public class PersonalDetailsValidator implements Validator {
    /**. Configurable title max length - spring */
    private int titleMaxLength;
    /**. Configurable firstname max length - spring */
    private int firstnameMaxLength;
    /**. Configurable lastname max length - spring */
    private int lastnameMaxLength;

    /**. @see org.springframework.validation.Validator#
     * supports(java.lang.Class)
     * @param clazz Class
     * @return boolean
     */
    public boolean supports(final Class clazz) {
        return PersonalDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * Enforce validation rules specific to personal details
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     * @param target Object
     * @param errors Errors
     */
    public void validate(final Object target, final Errors errors) {

        final PersonalDetails pd = (PersonalDetails) target;

        // -- title
        String title = pd.getTitle();
        if (StringUtils.isNotBlank(title)
        && title.length() > getTitleMaxLength()) {
            errors.rejectValue(
            "title", "personalDetails.title.tooLong",
            new Object[]{getTitleMaxLength() },
            "Title cannot be longer than {0} characters"
            );
        }

        // -- firstname
        String firstname = pd.getFirstname();
        ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, "firstname", "personalDetails.firstname.mandatory",
        "First Name is mandatory"
        );
        if (StringUtils.isNotBlank(firstname)
        && firstname.length() > getFirstnameMaxLength()) {
            errors.rejectValue(
            "firstname", "personalDetails.firstname.tooLong",
            new Object[]{getFirstnameMaxLength() },
            "Firstname cannot be longer than {0} characters"
            );
        }

        // -- lastname
        String lastname = pd.getLastname();
        ValidationUtils.rejectIfEmptyOrWhitespace(
        errors, "lastname", "personalDetails.lastname.mandatory",
        "Lastname is mandatory"
        );
        if (StringUtils.isNotBlank(lastname)
        && lastname.length() > getLastnameMaxLength()) {
            errors.rejectValue(
            "lastname", "personalDetails.lastname.tooLong",
            new Object[]{getLastnameMaxLength() },
            "Lastname cannot be longer than {0} characters"
            );
        }
    }

    // *** Spring injected properties ***

    /**.
     * Gets Max length for First name
     * @return int firstnameMaxLength
     */
    public int getFirstnameMaxLength() {
        return firstnameMaxLength;
    }

    /**.
     * Sets  Max length for First name
     * @param firstnameLength max length for First name
     */
    public void setFirstnameMaxLength(int firstnameLength) {
        this.firstnameMaxLength = firstnameLength;
    }

    /**.
     * Gets  Max length for Last name
     * @return int lastnameMaxLength
     */
    public int getLastnameMaxLength() {
        return lastnameMaxLength;
    }

    /**.
     * Sets Max length for Last name
     * @param lastnameLength Max length for Last name
     */
    public void setLastnameMaxLength(int lastnameLength) {
        this.lastnameMaxLength = lastnameLength;
    }

    /**.
     * Gets Max length for title
     * @return int titleMaxLength
     */
    public int getTitleMaxLength() {
        return titleMaxLength;
    }

    /**.
     * Sets Max length for title
     * @param titleLength Max length for title
     */
    public void setTitleMaxLength(int titleLength) {
        this.titleMaxLength = titleLength;
    }

}
