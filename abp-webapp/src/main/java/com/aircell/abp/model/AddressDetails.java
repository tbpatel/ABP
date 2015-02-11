/*
 * AddressDetails.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.service.ConfigService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.Serializable;

/**.
 *
 * Address Detail Form bean
 *
 */
@Configurable
public class AddressDetails implements Serializable {
    /**. private class variable */
    private static final long serialVersionUID = 411473781969686250L;
    /**. private class variable to hold  ConfigService*/
    private transient ConfigService configService;
    /**. private class variable to hold address1 */
    private String address1;
    /**. private class variable to hold address2 */
    private String address2;
    /**. private class variable to hold city */
    private String city;
    /**. private class variable  to hold usStateCode */
    private String usStateCode;

    /**.
     * Default constructor
     *
     */
    public AddressDetails() {
    }

    /**.
     * Parameterized Constructor
     * @param address1 address1
     * @param address2 address2
     * @param city City
     * @param usStateCode US state code
     * @param usZipCode US Zip code
     * @param intlPostcode International Post code
     * @param countryCode Country code
     */
    public AddressDetails(
    String address1, String address2, String city, String usStateCode,
    String usZipCode, String intlPostcode, String countryCode
    ) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.usStateCode = usStateCode;
        this.usZipCode = usZipCode;
        this.intlPostcode = intlPostcode;
        this.countryCode = countryCode;
    }
    /**. private class variable  to hold usZipCode */
    private String usZipCode;
    /**. private class variable  to hold international Postcode */
    private String intlPostcode;
    /**. private class variable  to hold countryCode */
    private String countryCode;

   /**.
    * Method to get address1
    * @return String address1
    */
    public String getAddress1() {
        return address1;
    }

    /**.
     * Method to set address1
     * @param address1 address1
     */
    public void setAddress1(final String address1) {
        this.address1 = address1;
    }
    /**.
     * Method to get address2
     * @return String address2
     */
    public String getAddress2() {
        return address2;
    }
    /**.
     * Method to set address2
     * @param address2 address2
     */
    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    /**.
     * Method to get City
     * @return String City
     */
    public String getCity() {
        return city;
    }

    /**.
     * Method to set City
     * @param city city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**.
     * Method to get Country Code
     * @return String country Code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**.
     * Method to set International Country Code
     * @param intlCountry  International country Code
     */
    public void setCountryCode(final String intlCountry) {
        this.countryCode = intlCountry;
    }

    /**.
     * Method to get International Post Code
     * @return String International Post Code
     */
    public String getIntlPostcode() {
        return intlPostcode;
    }

    /**.
     * Method to set International Post Code
     * @param intlPostcode International Post Code
     */
    public void setIntlPostcode(final String intlPostcode) {
        this.intlPostcode = intlPostcode;
    }

    /**.
     * Method to get US State Code
     * @return String US State Code
     */
    public String getUsStateCode() {
        return usStateCode;
    }

    /**.
     *  Method to set US State Code
     * @param usState US State Code
     */
    public void setUsStateCode(final String usState) {
        this.usStateCode = usState;
    }

    /**.
     * Method to get US ZIP Code
     * @return String US ZIP Code
     */
    public String getUsZipCode() {
        return usZipCode;
    }

    /**.
     * Method to set US ZIP Code
     * @param usZipCode US ZIP Code
     */
    public void setUsZipCode(final String usZipCode) {
        this.usZipCode = usZipCode;
    }

    /**.
     * Method to check if the country code is for US
     * or International
     * @return boolean true if country code is not for US
     */
    public boolean isInternational() {
        return !getConfigService().getCountryCodesForUS()
        .contains(getCountryCode());
    }


    /**.
     * Method to get configService(Spring injected services)
     * @return configService Spring injected services
     */
    ConfigService getConfigService() {
        return configService;
    }

    /**.
     * Method to set configService(Spring injected services)
     * @param configService Spring injected services
     */
    public void setConfigService(ConfigService configService){
        this.configService = configService;
    }

    /**.
     * Methode to override superclass method
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
