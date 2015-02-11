/*
 * SecurityChallengeDetails.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**.
 *
 * Bean to hold security challenge details
 *
 */
public class SecurityChallengeDetails implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 4397571375894003830L;

    /**.
     *
     * Enum for security challenge types
     *
     */
    public enum SecurityChallengeType {
        /**. security challenge for username */
        USERNAME_CHALLENGE,
        /**. security challenge for password */
        PASSWORD_CHALLENGE,
        /**. security challenge for credit card number */
        CARD_NUMBER_CHALLENGE,
        /**. security challenge for address */
        ADDRESS_CHALLENGE;
    };

    /**. private class variable to hold id */
    private Integer id;
    /**. private class variable to hold serialVersquestionionUID */
    private String question;
    /**. private class variable to hold answer */
    private String answer;
    /**. private class variable to hold type of security challenge */
    private SecurityChallengeType type;

    /**.
     * Constructor
     *
     */
    public SecurityChallengeDetails() {

    }

    /**.
     * Constructor - Initialize member variables
     * @param id Integer containing id
     * @param question Security Question
     * @param answer Answer for the Security Question
     * @param type String contaning type
     */
    public SecurityChallengeDetails(
    final Integer id, final String question, final String answer,
    final SecurityChallengeType type
    ) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.type = type;
    }

    /**.
     * Gets answer for Security Question
     * @return answer for Security Question
     */
    public String getAnswer() {
        return answer;
    }

    /**.
     * Gets integer containing id
     * @return integer containg id
     */
    public Integer getId() {
        return id;
    }

    /**.
     * Gets security question
     * @return security question
     */
    public String getQuestion() {
        return question;
    }

    /**.
     * Sets answer for Security Question
     * @param answer Answer for security question to be set
     */
    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    /**.
     * Sets id
     * @param id id to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**.
     * Sets security question
     * @param question security question to be set
     */
    public void setQuestion(final String question) {
        this.question = question;
    }

    /**.
     * Gets SecurityChallengeType
     * @return the type */
    public SecurityChallengeType getType() {
        return type;
    }

    /**.
     * Sets SecurityChallengeType
     * @param type the type to set */
    public void setType(final SecurityChallengeType type) {
        this.type = type;
    }

    /**.
     * overrides superclass method
     * @return String String
     */
    @Override
    public final String toString() {
        final ToStringBuilder t = new ToStringBuilder(this);
        t.append("id", id);
        t.append("question", question);
        t.append("type", type);
        return t.toString();
    }
}
