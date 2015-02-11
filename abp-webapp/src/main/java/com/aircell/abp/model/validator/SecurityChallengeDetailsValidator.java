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
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**.
 * Validator for SecurityChallengeDetails
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class SecurityChallengeDetailsValidator implements Validator {
    /**. Configurable max length for security questions - spring */
    private int questionMaxLength;
    /**. Configurable max length for security answers - spring */
    private int answerMaxLength;

    /**.
     * @see org.springframework.validation.Validator#
     * supports(java.lang.Class)
     * @param clazz Class
     * @return boolean
     */
    public boolean supports(final Class clazz) {
        return SecurityChallengeDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * Enforces the security challenge rules
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     * @param  target Object
     * @param  errors Errors
     */
    public void validate(final Object target, final Errors errors) {
        String nestedPath = errors.getNestedPath();
        ValidationUtils.rejectIfEmpty(
        errors, "id",
        nestedPath + "securityChallengeDetails.question.mandatory",
        "Security challenge question mandatory"
        );
        ValidationUtils.rejectIfEmpty(
        errors, "answer",
        nestedPath + "securityChallengeDetails.answer.mandatory",
        "Security challenge answer mandatory"
        );

        final SecurityChallengeDetails scd = (SecurityChallengeDetails) target;


        if (StringUtils.isNotBlank(scd.getAnswer())
        && scd.getAnswer().length() > getAnswerMaxLength()) {
            errors.rejectValue(
            "answer", nestedPath + "securityDetails.answer.tooLong",
            new Object[]{getAnswerMaxLength() },
            "Security challenge anwer cannot be longer than {0} characters"
            );
        }
    }

    // *** Spring injected properties

    /**.
     * Gets Max length for Answer
     * @return int answerMaxLength
     */
    public int getAnswerMaxLength() {
        return answerMaxLength;
    }

    /**.
     * Sets Max length for Answer
     * @param answerLength Max length for Answer
     */
    public void setAnswerMaxLength(int answerLength) {
        this.answerMaxLength = answerLength;
    }

    /**.
     * Gets Max length for Question
     * @return int questionMaxLength
     */
    public int getQuestionMaxLength() {
        return questionMaxLength;
    }

    /**.
     * Sets Max length for Question
     * @param questionLength Max length for Question
     */
    public void setQuestionMaxLength(int questionLength) {
        this.questionMaxLength = questionLength;
    }
}
