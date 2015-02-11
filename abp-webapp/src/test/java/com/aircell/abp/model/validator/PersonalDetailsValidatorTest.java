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
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.validation.Errors;

/**.
 * Tests for PersonalDetailsValidator
 * @author AKQA - bryan.swift
 * @version $Revision: 3606 $
 */
public class PersonalDetailsValidatorTest extends MockObjectTestCase {

    /**. private class variable to hold PersonalDetailsValidator object */
    private PersonalDetailsValidator personalDetailsValidator;
    /**. private class variable to hold PersonalDetails object */
    private PersonalDetails personalDetails;
    /**. private class variable to hold Errors object */
    private Errors errors;

    @Override
    protected void setUp() {
        personalDetailsValidator = new PersonalDetailsValidator();
        personalDetailsValidator.setFirstnameMaxLength(6);
        personalDetailsValidator.setLastnameMaxLength(6);
        personalDetailsValidator.setTitleMaxLength(4);

        personalDetails = new PersonalDetails();
        errors = mock(Errors.class);
    }

    public void testSupports() {
        assertTrue(personalDetailsValidator.supports(PersonalDetails.class));
    }

    /**. test valid details */
    public void testValid() {
        personalDetails.setFirstname("first");
        personalDetails.setLastname("last");
        personalDetails.setTitle("Mr.");

        checking(
        new Expectations() {
            {
                one(errors).getFieldValue(with(same("firstname")));
                one(errors).getFieldValue(with(same("lastname")));
                //one(errors).getFieldValue(with(same("title")));
                never(errors).rejectValue(
                with(same("first")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("last")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("title")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("first")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("last")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("title")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
            }}
        );

        personalDetailsValidator.validate(personalDetails, errors);
    }

    /**. test too long details */
    public void testInvalid() {
        personalDetails.setFirstname("firstname");
        personalDetails.setLastname("lastname");
        personalDetails.setTitle("Missus");

        checking(
        new Expectations() {
            {
                one(errors).getFieldValue(with(same("firstname")));
                one(errors).getFieldValue(with(same("lastname")));
                //one(errors).getFieldValue(with(same("title")));
                never(errors).rejectValue(
                with(same("firstname")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("lastname")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("title")), with(any(String.class)),
                with(any(String.class))
                );
                one(errors).rejectValue(
                with(same("firstname")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                one(errors).rejectValue(
                with(same("lastname")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                one(errors).rejectValue(
                with(same("title")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
            }}
        );

        personalDetailsValidator.validate(personalDetails, errors);
    }
}
