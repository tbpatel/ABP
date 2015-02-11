package com.aircell.abp.model.validator;

import com.aircell.abp.model.AddressDetails;
import junit.framework.TestCase;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.support.StaticMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**.
 * Tests for AddressDetailsValidatorTest
 * @author AKQA - Sagar
 */
public class AddressDetailsValidatorTest extends TestCase {

    /**. private class variable to hold AddressDetailsValidator object */
    private AddressDetailsValidator addressDetailsValidator;
    /**. private class variable to hold AddressDetails object */
    private AddressDetails addressDetails;
    /**. private class variable to hold MessageContext object */
    private MessageContext errors;
    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void setUp() {
        addressDetailsValidator = new AddressDetailsValidator();

        StaticMessageSource messageSource = new StaticMessageSource();

        messageSource.addMessage(
        "purchaseForm.address.address1.mandatory", Locale.getDefault(),
        "purchaseForm.address.address1.mandatory"
        );
        messageSource.addMessage(
        "purchaseForm.address.address1.city.mandatory", Locale.getDefault(),
        "purchaseForm.address.address1.city.mandatory"
        );
        messageSource.addMessage(
        "purchaseForm.address.address1.state.mandatory", Locale.getDefault(),
        "purchaseForm.address.address1.state.mandatory"
        );
        messageSource.addMessage(
        "addressDetails.stateCode.invalid", Locale.getDefault(),
        "addressDetails.stateCode.invalid"
        );
        messageSource.addMessage(
        "purchaseForm.address.address1.zip.mandatory", Locale.getDefault(),
        "purchaseForm.address.address1.zip.mandatory"
        );
        messageSource.addMessage(
        "addressDetails.countryCode.invalid", Locale.getDefault(),
        "addressDetails.countryCode.invalid"
        );
        messageSource.addMessage(
        "purchaseForm.address.address1.zip.syntax", Locale.getDefault(),
        "purchaseForm.address.address1.zip.syntax"
        );
        messageSource.addMessage(
        "addressDetails.address1.tooLong", Locale.getDefault(),
        "addressDetails.address1.tooLong"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.number.invalid", Locale.getDefault(),
        "purchaseForm.creditcard.number.invalid"
        );
        messageSource.addMessage(
        "addressDetails.address2.tooLong", Locale.getDefault(),
        "addressDetails.address2.tooLong"
        );
        messageSource.addMessage(
        "addressDetails.city.tooLong", Locale.getDefault(),
        "addressDetails.city.tooLong"
        );
        messageSource.addMessage(
        "purchaseForm.address.address1.city.syntax", Locale.getDefault(),
        "purchaseForm.address.address1.city.syntax"
        );

        errors = new DefaultMessageContext(messageSource);

        addressDetailsValidator.setAddressOneMaxLength(10);
        addressDetailsValidator.setAddressTwoMaxLength(10);
        addressDetailsValidator.setCityMaxLength(4);
        List<String> countriesWithPostcodes = new ArrayList<String>();
        countriesWithPostcodes.add("UK");
        countriesWithPostcodes.add("GB");
        addressDetailsValidator
        .setCountriesWithPostcodes(countriesWithPostcodes);
        addressDetailsValidator.setPostcodeMaxLength(10);
        addressDetails = new AddressDetails();

    }

    public void testInvalidAddress1() {

        addressDetails.setAddress1(null);
        addressDetails.setAddress2("address2");
        addressDetails.setCity("city");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testAddressTooLong() {

        addressDetails.setAddress1("vgghvhgvhgvhvhgvht");
        addressDetails.setAddress2("address2");
        addressDetails.setCity("city");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testAddressInvalid() {

        addressDetails.setAddress1("####???jhbvjv@!!!");
        addressDetails.setAddress2("address2");
        addressDetails.setCity("city");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testInvalidAddress2TooLong() {

        addressDetails.setAddress1("address1");
        addressDetails.setAddress2("dghrtfhfyjhfgjhgjhnjfgjbmkbjkbjkbh");
        addressDetails.setCity("city");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testCityBlank() {

        addressDetails.setAddress1("address1");
        addressDetails.setAddress2("address2");
        addressDetails.setCity(null);
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testCityTooLong() {

        addressDetails.setAddress1("address1");
        addressDetails.setAddress2("address2");
        addressDetails.setCity("kolkatakolkatakolkatakolkata");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

    public void testCitySyntaxError() {

        addressDetails.setAddress1("address1");
        addressDetails.setAddress2("address2");
        addressDetails.setCity("#$&");
        addressDetails.setUsStateCode("US");
        addressDetails.setCountryCode("UK");

        addressDetailsValidator.validate(addressDetails, errors);
        assertTrue(errors.hasErrorMessages());
        logger.debug("success1");
    }

}
