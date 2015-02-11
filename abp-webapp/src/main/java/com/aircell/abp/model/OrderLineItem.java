/*
 * OrderLineItem.java 1 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */
package com.aircell.abp.model;

/**. @author miroslav.miladinovic at AKQA Inc. */
public interface OrderLineItem {

    /**. @return  */
    double getPriceExTax();

    /**. @return  */
    String getProductCode();

    /**. @return  */
    int getQuantity();

    /**. @return  */
    double getTaxAmount();

    /**. @return  */
    double getTotalAmount();


    /**. @return  */
    double getDiscountAmount();

    /**.
     * Sets the promotional code for this item. An item can only have up to 1
     * promo code. Setting empty or null promo code is allowed and will erase
     * any promo codes set previously.
     * @param promoCode String containing promo code
     */
    void setPromoCode(final String promoCode);

    /**.
     * Returns the current promo code of this line item. Null or blank string
     * values indicate that no promo code is applied to this item.
     * @return current promo code
     */
    String getPromoCode();


    /**. @param taxAmount */
    void setTaxAmount(final double taxAmount);


    /**. @param discountAmount */
    void setDiscountAmount(final double discountAmount);

    /**. @param totalAmount */
    void setTotalAmount(final double totalAmount);

}
