/*
 * PersonalDetails.java 23 Jul 2007
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**.
 *
 * Bean class to hold personal details
 *
 */
public class PersonalDetails implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = 3654176032339112858L;

    /**. private class variable to hold title */
    private String title = "";
    /**. private class variable to hold firstname */
    private String firstname = "";

    /*private String middlename;*/
    /**. private class variable to hold lastname */
    private String lastname = "";
    /**. variable for logging */
    protected final transient Logger logger =
    LoggerFactory.getLogger(getClass());

/**.
 * Constructor
 *
 */
    public PersonalDetails() {
    }

    /**.
     * Constructor - Initialize member variables
     * @param title value of title
     * @param first value of first name
     * @param middle value of middle name
     * @param last value of last name
     */
    public PersonalDetails(
    String title, String first, String middle, String last
    ) {
        this.title = (title == null) ? "-" : title;
        this.firstname = first;
        /*this.middlename = middle;*/
        this.lastname = last;
    }

    /**.
     * Gets title for a name
     * @return title for a name
     */
    public String getTitle() {
        return title;
    }

    /**.
     * Sets tilte for a name
     * @param title title for a name to set
     */
    public void setTitle(final String title) {

        this.title = (title == null) ? "-" : title;
    }

    /**.
     * Gets firstName
     * @return firstName
     */
    public String getFirstname() {
        return firstname;
    }

    /**.
     * Sets firstName
     * @param first firstName to set
     */
    public void setFirstname(final String first) {
        this.firstname = first;
    }

    /* public String getMiddlename() {
        return middlename;
    }
    public void setMiddlename(final String middle) {
        this.middlename = middle;
    }*/

    /**.
     * Gets lastName
     * @return lastName
     */
    public String getLastname() {
        return lastname;
    }
    /**.
     * Sets lastName
     * @param last lastName ro set
     */
    public void setLastname(final String last) {
        this.lastname = last;
    }

    /**.
     * overrides superclass method
     * @return String String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
