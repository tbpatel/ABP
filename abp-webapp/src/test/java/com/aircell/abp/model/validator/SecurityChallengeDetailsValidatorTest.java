/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model.validator;

import com.aircell.abp.model.SecurityChallengeDetails;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.validation.Errors;

/**.
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class SecurityChallengeDetailsValidatorTest extends MockObjectTestCase {

    /**. private class variable to hold
     * SecurityChallengeDetailsValidator object */
    private SecurityChallengeDetailsValidator securityChallengeValidator;
    /**. private class variable to hold SecurityChallengeDetails object */
    private SecurityChallengeDetails securityChallengeDetails;
    /**. private class variable to hold Errors object */
    private Errors errors;

    @Override
    protected void setUp() {
        securityChallengeValidator = new SecurityChallengeDetailsValidator();
        securityChallengeValidator.setAnswerMaxLength(26);
        securityChallengeValidator.setQuestionMaxLength(26);

        securityChallengeDetails = new SecurityChallengeDetails();
        errors = mock(Errors.class);
    }

    public void testSupports() {
        assertTrue(
        securityChallengeValidator.supports(SecurityChallengeDetails.class)
        );
    }

    /**. test a valid answer */
    public void testValidAnswer() {
        securityChallengeDetails.setAnswer("This is a valid answer");
        securityChallengeDetails.setQuestion("This is a valid question");
        securityChallengeDetails.setId(1);

        checking(
        new Expectations() {
            {
                one(errors).getFieldValue(with(same("id")));
                one(errors).getFieldValue(with(same("answer")));
                exactly(1).of(errors).getNestedPath();
                never(errors).rejectValue(
                with(same("question")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("answer")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("question")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("answer")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
            }}
        );

        securityChallengeValidator.validate(securityChallengeDetails, errors);
    }

    /**. test invalid answer */
    public void testInvalidAnswer() {
        securityChallengeDetails
        .setAnswer("This is an invalid answer. This is an invalid answer.");
        securityChallengeDetails.setQuestion("This is a valid question");
        securityChallengeDetails.setId(1);

        checking(
        new Expectations() {
            {
                one(errors).getFieldValue(with(same("id")));
                one(errors).getFieldValue(with(same("answer")));
                exactly(1).of(errors).getNestedPath();
                never(errors).rejectValue(
                with(same("question")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("answer")), with(any(String.class)),
                with(any(String.class))
                );
                never(errors).rejectValue(
                with(same("question")), with(any(String.class)),
                with(any(Object[].class)), with(any(String.class))
                );
                one(errors).rejectValue(
                with(same("answer")),
                with(equal("securityDetails.answer.tooLong")),
                with(any(Object[].class)), with(any(String.class))
                );
            }}
        );

        securityChallengeValidator.validate(securityChallengeDetails, errors);
    }
}
