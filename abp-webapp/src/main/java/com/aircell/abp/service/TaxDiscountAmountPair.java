/*
 * TaxDiscountPair.java 28 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import org.apache.commons.lang.builder.ToStringBuilder;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class TaxDiscountAmountPair {
    /**. private class variable to hold  taxAmount*/
    private double taxAmount;
    /**. private class variable to hold  discountAmount*/
    private double discountAmount;
    /**. private class variable to hold  totalAmount*/
    private double totalAmount;

    /**. @return the discountAmount */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**. @param discountAmount the discountAmount to set */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**. @return the taxAmount */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**. @param taxAmount the taxAmount to set */
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**.
     * Gets TotalAmount
     * @return double totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**.
     * Sets TotalAmount
     * @param totalAmount TotalAmount
     */
    public void setTotalAmount(final double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**. @see java.lang.Object#toString()
     * @return String String value
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
