/**.
 *
 */
package com.aircell.abp.model.validator;

import com.aircell.abp.model.AddressDetails;
import com.aircell.abp.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**.
 * Validator for the AddressDetails object
 * @author AKQA - bryan.swift
 * @version $Revision: 3142 $
 */
public class AddressDetailsValidator {


    private Logger logger = LoggerFactory.getLogger(getClass());
    /**. Configurable max length for address one field - spring */
    private int addressOneMaxLength;
    /**. Configurable max length for address two field - spring */
    private int addressTwoMaxLength;
    /**. Configurable max length for city field - spring */
    private int cityMaxLength;
    /**. Configurable max length for postcode field - spring */
    private int postcodeMaxLength;
    /**. Configurable address regex for address field validation - spring */
    private String addressRegex;
    /**. Configurable list of country codes which require postcodes - spring */
    private List<String> countriesWithPostcodes;
    /**. Regex for valid us zip code */
    private static final String US_ZIPCODE_REGEX = "^[\\d]{5}([-| ][\\d]{4})?$";
    // TODO optimize - as it stands, this model validator depends on a serivce.
    // TODO Move card type validation elsewhere?
    private ConfigService configService;

    /**. @see org.springframework.validation.Validator#supports(java.lang.Class) */
    public boolean supports(final Class clazz) {
        return AddressDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     */
    public void validate(
    AddressDetails addressDetails, MessageContext context
    ) {

        Pattern p = Pattern.compile("^\\w[^/%|\\^\\\\&<]*$");

        // -- address1
        String addressOne = addressDetails.getAddress1();

        if (StringUtils.isBlank(addressOne)) {
            context.addMessage(
            new MessageBuilder().error().source("address1")
            .code("purchaseForm.address.address1.mandatory").build()
            );
        }
        if (StringUtils.isNotBlank(addressOne)) {
            if (addressOne.length() > getAddressOneMaxLength()) {

                context.addMessage(
                new MessageBuilder().error().source("address1")
                .code("addressDetails.address1.tooLong").build()
                );
            }

            if (!hasCreditCardDetailsInvalidError(context)) {
                Matcher m = p.matcher(addressOne);
                if (!m.find() || !m.matches()) {

                    context.addMessage(
                    new MessageBuilder().error().source("common")
                    .code("purchaseForm.creditcard.number.invalid").build()
                    );
                }
            }
        }

        // -- address2
        String addressTwo = addressDetails.getAddress2();
        if (StringUtils.isNotBlank(addressTwo)) {
            if (addressTwo.length() > getAddressTwoMaxLength()) {
                context.addMessage(
                new MessageBuilder().error().source("address2")
                .code("addressDetails.address2.tooLong").build()
                );
            }

            if (!hasCreditCardDetailsInvalidError(context)) {
                Matcher m = p.matcher(addressTwo);
                if (!m.find() || !m.matches()) {
                    context.addMessage(
                    new MessageBuilder().error().source("common")
                    .code("creditCardDetails.invalid").build()
                    );
                }
            }
        }

        // -- city
        String city = addressDetails.getCity();

        if (StringUtils.isBlank(city)) {
            context.addMessage(
            new MessageBuilder().error().source("city")
            .code("purchaseForm.address.address1.city.mandatory").build()
            );
        }
        if (StringUtils.isNotBlank(city)) {
            if (city.length() > getCityMaxLength()) {
                context.addMessage(
                new MessageBuilder().error().source("city")
                .code("addressDetails.city.tooLong").build()
                );
            }

            if (!hasCreditCardDetailsInvalidError(context)) {

                Matcher m = p.matcher(city);
                if (!m.find() || !m.matches()) {

                    context.addMessage(
                    new MessageBuilder().error().source("common")
                    .code("purchaseForm.address.address1.city.syntax").build()
                    );
                }
            }
        }

        //if (addressDetails.isInternational()) {
        //validateInternational(addressDetails, errors);
        //} else {
        validateUs(addressDetails, context);
        //}
    }


    /**.
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     */
    private void validateUs(
    AddressDetails addressDetails, MessageContext context
    ) {
        Locale locale = Locale.US;
        // -- country
        // -- state

        String stateCode = addressDetails.getUsStateCode();
        String countryCode = addressDetails.getCountryCode();
        if (countryCode.equalsIgnoreCase("US")) {


            Map<String, String> stateMap =
            getConfigService().getSupportedUserUSStates(locale);
            Set<String> stateCodes = stateMap.keySet();
            if (StringUtils.isBlank(stateCode)) {
                context.addMessage(
                new MessageBuilder().error().source("usStateCode")
                .code("purchaseForm.address.address1.state.mandatory").build()
                );
            } else if (!stateCodes.contains(stateCode)) {
                context.addMessage(
                new MessageBuilder().error().source("usStateCode")
                .code("purchaseForm.address.address1.state.mandatory").build()
                );
            }
            Map<String, String> countryMap =
            getConfigService().getSupportedUserCountries(locale);
            Set<String> countryCodes = countryMap.keySet();
            if (!countryCodes.contains(countryCode)) {
                context.addMessage(
                new MessageBuilder().error().source("countryCode")
                .code("addressDetails.countryCode.invalid").build()
                );
            }
        }

        // -- zip code
        if (countryCode.equalsIgnoreCase("US")) {
            String zipcode = addressDetails.getUsZipCode();
            if (StringUtils.isBlank(stateCode)) {
                context.addMessage(
                new MessageBuilder().error().source("usZipCode")
                .code("purchaseForm.address.address1.zip.mandatory").build()
                );
            }
            if (StringUtils.isNotBlank(zipcode) && !zipcode
            .matches(US_ZIPCODE_REGEX)) {


                context.addMessage(
                new MessageBuilder().error().source("usZipCode")
                .code("purchaseForm.address.address1.zip.syntax").build()
                );
            }


        }

    }


    /* ** Spring configured values ** */
    /**. @return the addressOneMaxLength */
    public int getAddressOneMaxLength() {
        return addressOneMaxLength;
    }

    /**. @param addressOneMaxLength the addressOneMaxLength to set */
    public void setAddressOneMaxLength(int addressOneMaxLength) {
        this.addressOneMaxLength = addressOneMaxLength;
    }

    /**. @return the addressTwoMaxLength */
    public int getAddressTwoMaxLength() {
        return addressTwoMaxLength;
    }

    /**. @param addressTwoMaxLength the addressTwoMaxLength to set */
    public void setAddressTwoMaxLength(int addressTwoMaxLength) {
        this.addressTwoMaxLength = addressTwoMaxLength;
    }

    /**. @return the cityMaxLength */
    public int getCityMaxLength() {
        return cityMaxLength;
    }

    /**. @param cityMaxLength the cityMaxLength to set */
    public void setCityMaxLength(int cityMaxLength) {
        this.cityMaxLength = cityMaxLength;
    }

    /**. @return the countriesWithPostcodes */
    public List<String> getCountriesWithPostcodes() {
        return countriesWithPostcodes;
    }

    /**. @param countriesWithPostcodes the countriesWithPostcodes to set */
    public void setCountriesWithPostcodes(List<String> countriesWithPostcodes) {
        this.countriesWithPostcodes = countriesWithPostcodes;
    }

    /**. @return the postcodeMaxLength */
    public int getPostcodeMaxLength() {
        return postcodeMaxLength;
    }

    /**. @param postcodeMaxLength the postcodeMaxLength to set */
    public void setPostcodeMaxLength(int postcodeMaxLength) {
        this.postcodeMaxLength = postcodeMaxLength;
    }

    /**. @return the addressRegex */
    public String getAddressRegex() {
        return this.addressRegex;
    }

    /**. @param addressRegex the addressRegex to set */
    public void setAddressRegex(String addressRegex) {
        this.addressRegex = addressRegex;
    }

    /**. @return the configService */
    public ConfigService getConfigService() {
        return configService;
    }

    /**. @param configService the configService to set */
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /**.
     * Checks if there is CreditCardDetails Invalid Error
     * @param context
     * @return boolean
     */
    private boolean hasCreditCardDetailsInvalidError(MessageContext context) {

        if (context == null) {
            return false;
        }

        if (context.getMessagesBySource("common").length != 0) {
            return true;
        }

        return false;
    }

}
