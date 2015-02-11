/*
 * SubscriptionProduct.java 30 Aug 2007
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
import java.math.BigInteger;
import java.util.Date;

/**.
 * Represents a subscription product which can be bought on the portal: either
 * in flight or on the ground.
 * @author miroslav.miladinovic at AKQA Inc.
 */
public class SubscriptionProduct implements Serializable {

    /**. private class variable to hold serialVersionUID */
    private static final long serialVersionUID = -3819530060840016958L;

    /**. private class variable to hold productCode */
    private String productCode;
    /**. private class variable to hold priceExTax */
    private double priceExTax;
    /**. private class variable to hold name */
    private String name;
    /**. private class variable to hold shortDescription */
    private String shortDescription;
    /**. private class variable to hold longDescription */
    private String longDescription;
    /**. private class variable to hold expiryDate */
    private Date expiryDate;
    /**. private class variable to hold pcode */
    private com.aircell.bss.ws.ImageUrls pcode;
    /**. private class variable to hold prdImage */
    private String prdImage;
    /**. private class variable to hold images */
    private String images;
    /**. private class variable to hold presentationPriority */
    private java.math.BigInteger presentationPriority =
    BigInteger.valueOf(Integer.MAX_VALUE);

    //Added @ Satish for Loading Product Image
    /**.
     * Gets Product Image
     * @return Product Image
     */
    public final String getPrdImage() {
        return prdImage;
    }

    /**.
     * Sets Product Image
     * @param prdImage Product Image to be set
     */
    public final void setPrdImage(String prdImage) {
        this.prdImage = prdImage;
    }

    /**.
     * Gets the name
     * @return the name */
    public String getName() {
        return name;
    }

    /**.
     * Sets the name
     * @param name the name to set */
    public void setName(final String name) {
        this.name = name;
    }

    /**.
     * Gets Price exclusive of tax
     * @return the priceExTax */
    public double getPriceExTax() {
        return priceExTax;
    }

    /**.
     * Sets Price exclusive of tax
     * @param priceExTax the priceExTax to set */
    public void setPriceExTax(final double priceExTax) {
        this.priceExTax = priceExTax;
    }

    /**.
     * Gets the productCode
     * @return the productCode */
    public String getProductCode() {
        return productCode;
    }

    /**.
     * Sets the productCode
     * @param productCode the productCode to set */
    public void setProductCode(final String productCode) {
        this.productCode = productCode;
    }

    /**.
     * Gets the productDescription
     * @return the productDescription */
    public String getProductDescription() {
        return getShortDescription();
    }

    /**.
     * @param productDescription the productDescription to set
     * @deprecated in favour of getShortDescription and getLongDescription
     */
    public void setProductDescription(String productDescription) {
        setShortDescription(productDescription);
    }

    /**.
     * Gets the expiry date
     * @return the expiryDate */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**.
     * Sets the expiry date
     * @param expiryDate the expiryDate to set */
    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**.
     * Gets longDescription
     * @return longDescription
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**.
     * Sets longDescription
     * @param longDescription longDescription to be set
     */
    public void setLongDescription(final String longDescription) {
        this.longDescription = longDescription;
    }

    /**.
     * Gets shortDescription
     * @return shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**.
     * Sets shortDescription
     * @param shortDescription shortDescription to be set
     */
    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**.
     * Uses the Apache Commmons <code>ToStringBuilder</code>
     * @see java.lang.Object#toString()
     * @return String String value
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**.
     * Gets Pcode
     * @return Pcode
     */
    public com.aircell.bss.ws.ImageUrls getPcode() {
        return pcode;
    }

    /**.
     * Sets Pcode
     * @param pcode Pcode to be set
     */
    public void setPcode(com.aircell.bss.ws.ImageUrls pcode) {
        this.pcode = pcode;
    }

    /**.
     * Sets images
     * @param images the images to set */
    public void setImages(String images) {
        this.images = images;
    }

    /**.
     * Gets images
     * @return the images */
    public String getImages() {
        return images;
    }


    /**.
     * Sets presentationPriority
     * @param presentationPriority the presentationPriority to set */
    public void setPresentationPriority(
    java.math.BigInteger presentationPriority
    ) {
        this.presentationPriority = presentationPriority;
    }

    /**.
     * Gets presentationPriority
     * @return the presentationPriority */
    public java.math.BigInteger getPresentationPriority() {
        return presentationPriority;
    }
}
