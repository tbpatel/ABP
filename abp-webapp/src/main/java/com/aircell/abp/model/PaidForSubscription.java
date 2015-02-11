/*
 * PaidForSubscription.java 3 Aug 2007
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
import java.util.Date;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class PaidForSubscription implements Subscription, Serializable {

    /**.
     * Integer containg id
     */
    private int id;

    /**.
     * Cost when purchased
     */
    private double costWhenPurchased;

    /**.
     * Activated date
     */
    private Date dateActivated;

    /**.
     * String containing decsription
     */
    private String description;

    /**.
     * String containing name
     */
    private String name;

    /**.
     * boolean checking if valid
     */
    private boolean valid;

    /**.
     * remaining hours
     */
    private int durationRemainingHours;

    /**.
     * remaining minutes
     */
    private int durationRemainingMinutes;

    /**.
     * String containing product code
     */
    private String productCode;

    /**.
     * static final variable conatining serialVersionUID
     */
    private static final long serialVersionUID = -4814873334896064381L;

    /**. Gets id of the subscription
     * @return the id of this subscription */
    public int getId() {
        return this.id;
    }

    /**. Sets the internal id of the subscription
     * @param id the internal id of this subscription */
    public void setId(final int id) {
        this.id = id;
    }


    /**. Gets the cost when purchased
     * @return the costWhenPurchased */
    public double getCostWhenPurchased() {
        return costWhenPurchased;
    }

    /**. Sets the cost when purchased
     * @param costWhenPurchased the costWhenPurchased to set */
    public void setCostWhenPurchased(final double costWhenPurchased) {
        this.costWhenPurchased = costWhenPurchased;
    }

    /**. Gets the activation date
     * @return the dateActivated */
    public Date getDateActivated() {
        return dateActivated;
    }

    /**. Sets the activation date
     * @param dateActivated the dateActivated to set */
    public void setDateActivated(final Date dateActivated) {
        this.dateActivated = dateActivated;
    }

    /**. Gets the description of subscription
     * @return the description */
    public String getDescription() {
        return description;
    }

    /**. Sets the description of subscription
     * @param description the description to set */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**. Gets the remaining hours of duration
     * @return the durationRemainingHours */
    public int getDurationRemainingHours() {
        return durationRemainingHours;
    }

    /**. Sets the remaining hours of duration
     * @param durationRemainingHours the durationRemainingHours to set */
    public void setDurationRemainingHours(final int durationRemainingHours) {
        this.durationRemainingHours = durationRemainingHours;
    }

    /**. Gets the remaining minutes of duration
     * @return the durationRemainingMinutes */
    public int getDurationRemainingMinutes() {
        return durationRemainingMinutes;
    }

    /**. Sets the remaining minutes of duration
     * @param durationRemainingMinutes the durationRemainingMinutes to set */
    public void setDurationRemainingMinutes(
    final int durationRemainingMinutes
    ) {
        this.durationRemainingMinutes = durationRemainingMinutes;
    }

    /**. Gets the name of subscription
     * @return the name */
    public String getName() {
        return name;
    }

    /**. Sets the name of subscription
     * @param name the name to set */
    public void setName(final String name) {
        this.name = name;
    }

    /**. @return the valid */
    public boolean isValid() {
        return valid;
    }

    /**. @param valid the valid to set */
    public void setValid(final boolean valid) {
        this.valid = valid;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    /**.
     * Overrides superclass method
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**. Sets the product code
     * @param productCode the productCode to set */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**. Gets the product code
     * @return the productCode */
    public String getProductCode() {
        return productCode;
    }

}
