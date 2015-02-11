/*
 * SubscriptionOrderLineItem.java 2 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class SubscriptionOrderLineItem implements OrderLineItem, Serializable {

    /**. variable for logging */
    protected final transient Logger logger =
    LoggerFactory.getLogger(getClass());

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = -4268243291607322355L;

    /**. private class variable to hold price Excluding Tax */
    private double priceExTax;
    /**. private class variable to hold productCode */
    private String productCode;
    /**. private class variable to hold quantity */
    private int quantity;
    /**. private class variable to hold taxAmount */
    private double taxAmount;
    /**. private class variable to hold promoCode */
    private String promoCode;
    /**. private class variable to hold discountAmount */
    private double discountAmount;
    /**. private class variable to hold totalAmount */
    private double totalAmount;

    /**.
     * Sets the quantity of order
     * @param quantity the quantity to set */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**.
     * Gets the quantity of order
     * @return quantity of order
     */
    public int getQuantity() {
        return quantity;
    }

    /**.
     * Gets taxAmount for the order
     * @return taxAmount for the order
     */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**.
     * Sets taxAmount for the order
     * @param taxAmount tax amount for the order to be set
     */
    public void setTaxAmount(final double taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**.
     * Gets price exclusive of tax
     * @return price exclusive of tax
     */
    public double getPriceExTax() {
        return priceExTax;
    }

    /**.
     * Sets price exclusive of tax
     * @param priceExTax price exclusive of tax to set
     */
    public void setPriceExTax(double priceExTax) {
        this.priceExTax = priceExTax;
    }

    /**.
     * Gets productCode
     * @return productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**.
     * Sets productCode
     * @param productCode the productCode to set */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**.
     * @see com.aircell.abp.model.OrderLineItem#getTotalAmount()
     * @return double totalAmount
     *
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**.
     * Sets Total ammount
     * @param totalAmount Total ammount
     */
    public void setTotalAmount(final double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**.
     * @see com.aircell.abp.model.OrderLineItem#getPromoCode()
     * @return String promoCode
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**.
     * @see com.aircell.abp.model.OrderLineItem#setPromoCode(String)
     * @param  promoCode promoCode
     */
    public void setPromoCode(final String promoCode) {
        if (StringUtils.isBlank(promoCode)) {
            this.promoCode = null;
        } else {
            this.promoCode = promoCode;
        }
    }

    /**.
     * Gets discountAmount
     * @return discountAmount
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**.
     * Sets discountAmount
     * @param discountAmount discountAmount to be set
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
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
