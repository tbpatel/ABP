package com.aircell.abp.model.validator;

import com.aircell.abp.model.CreditCardDetails;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.validation.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class CreditCardDetailsValidatorTest extends MockObjectTestCase {

    /**. private class variable to hold NAME_ON_CARD MAX LENGTH */
    private static final int NAME_ON_CARD_MAX_LENGTH = 20;
    /**. private class variable to hold SECURITY_CODE_MIN_LENGTH */
    private static final int SECURITY_CODE_MIN_LENGTH = 3;
    /**. private class variable to hold SECURITY_CODE_MAX_LENGTH */
    private static final int SECURITY_CODE_MAX_LENGTH = 3;
    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**. private class variable to hold CreditCardDetailsValidator object */
    private CreditCardDetailsValidator underTest;
    /**. private class variable to hold Errors object */
    private Errors errorsMock;
    /**. private class variable to hold CreditCardDetails object */
    private CreditCardDetails ccd;
    /**. private class variable to hold MessageContext object */
    private MessageContext errors;

    /**. class variable to hold card types */
    Map<String, String> cardTypes = new HashMap();
    /**. private class variable to hold CreditCardDetails object */
    private CreditCardDetails cardDetails;
    /**. class variable to hold US Countries */
    Map<String, String> userCountries = new HashMap();
    /** expiry year set for unit testing. */
    Calendar now = Calendar.getInstance();	
    private final int expiryYear = now.get(Calendar.YEAR);

    protected void setUp() throws Exception {


        underTest = new CreditCardDetailsValidator();

        underTest = new CreditCardDetailsValidator();
        underTest.setNameOnCardMaxLength(20);
        underTest.setNameOnCardMaxLength(NAME_ON_CARD_MAX_LENGTH);
        underTest.setSecurityCodeMinLength(SECURITY_CODE_MIN_LENGTH);
        underTest.setSecurityCodeMaxLength(SECURITY_CODE_MAX_LENGTH);
        StaticMessageSource messageSource = new StaticMessageSource();

        //errorsMock = mock(Errors.class);

        messageSource.addMessage(
        "purchaseForm.creditcard.type.madatory", Locale.getDefault(),
        "purchaseForm.creditcard.type.madatory"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.number.mandatory", Locale.getDefault(),
        "purchaseForm.creditcard.number.mandatory"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.securityCode.mandatory", Locale.getDefault(),
        "purchaseForm.creditcard.securityCode.mandatory"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.expmonth.mandatory", Locale.getDefault(),
        "purchaseForm.creditcard.expmonth.mandatory"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.expyear.mandatory", Locale.getDefault(),
        "purchaseForm.creditcard.expyear.mandatory"
        );
        messageSource.addMessage(
        "creditCardDetails.expiryMonth.invalid", Locale.getDefault(),
        "creditCardDetails.expiryMonth.invalid"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.number.invalid", Locale.getDefault(),
        "purchaseForm.creditcard.number.invalid"
        );
        messageSource.addMessage(
        "creditCardDetails.cardType.invalid", Locale.getDefault(),
        "creditCardDetails.cardType.invalid"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.name.mandatory", Locale.getDefault(),
        "purchaseForm.creditcard.name.mandatory"
        );
        messageSource.addMessage(
        "creditCardDetails.nameOnCard.tooLong", Locale.getDefault(),
        "creditCardDetails.nameOnCard.tooLong"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.name.syntax", Locale.getDefault(),
        "purchaseForm.creditcard.name.syntax"
        );
        messageSource.addMessage(
        "creditCardDetails.expiryMonth.expired", Locale.getDefault(),
        "creditCardDetails.expiryMonth.expired"
        );
        messageSource.addMessage(
        "creditCardDetails.expiryYear.expired", Locale.getDefault(),
        "creditCardDetails.expiryYear.expired"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.number.syntax", Locale.getDefault(),
        "purchaseForm.creditcard.number.syntax"
        );
        messageSource.addMessage(
        "creditCardDetails.securityCode.tooShort", Locale.getDefault(),
        "creditCardDetails.securityCode.tooShort"
        );
        messageSource.addMessage(
        "creditCardDetails.securityCode.tooLong", Locale.getDefault(),
        "creditCardDetails.securityCode.tooLong"
        );
        messageSource.addMessage(
        "purchaseForm.creditcard.securityCode.syntax", Locale.getDefault(),
        "purchaseForm.creditcard.securityCode.syntax"
        );

        errors = new DefaultMessageContext(messageSource);
        ccd = new CreditCardDetails();


        cardTypes.put("VISA", "VISA");

        userCountries.put("US", "US1");

        ccd.setCardNumber("4444444444444448");

        underTest.setNameOnCardMaxLength(10);
        underTest.setSecurityCodeMaxLength(4);


    }


    public void testvalidateCardNumber() {
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("4444444444444448");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setSecurityCode("123");
        underTest.validateCardNumber(ccd, errors);
        assertFalse(errors.hasErrorMessages());
        logger.debug("success1");
    }


    public void testvalidateCardNumberBlankCardNo() {
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        //ccd.setCardType("VISA");
        ccd.setSecurityCode("123");
        underTest.validateCardNumber(ccd, errors);
        logger.debug("error:" + errors.toString());
        assertTrue(
        errors.toString().contains("purchaseForm.creditcard.number.mandatory")
        );
        logger.debug("success1");
    }


    public void testvalidateCardNumberIrregularCardNo() {
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("test");
        ccd.setExpiryYear(expiryYear);
        ccd.setExpiryMonth("11");
        //ccd.setCardType("VISA");
        ccd.setSecurityCode("123");
        underTest.validateCardNumber(ccd, errors);
        logger.debug("error:" + errors.toString());
        assertTrue(
        errors.toString().contains("purchaseForm.creditcard.number.syntax")
        );
        logger.debug("success1");
    }


    public void testvalidateCardNumberInvalidCardNo() {
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setSecurityCode("123");
        underTest.validateCardNumber(ccd, errors);
        logger.debug("error:" + errors.toString());
        assertTrue(
        errors.toString().contains("purchaseForm.creditcard.number.invalid")
        );
        logger.debug("success1");
    }


    public void testvalidateSecurityCode() {
        logger.debug("testvalidateSecurityCode");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISAk");
        ccd.setSecurityCode("123");
        ccd.setMastercardKey("abcd");
        ccd.setVisaKey("abcd");
        ccd.setDiscoverKey("abc");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        assertFalse(errors.hasErrorMessages());
        logger.debug("success1");
    }


    public void testvalidateSecurityCodeBlankSecurityCode() {
        logger.debug("testvalidateSecurityCodeBlankSecurityCode");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISA");
        ccd.setSecurityCode("");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        //assertFalse(errors.hasErrorMessages());
        assertTrue(
        errors.toString().contains(
        "purchaseForm.creditcard.securityCode.mandatory"
        )
        );
        logger.debug("success1");
    }


    public void testvalidateSecurityCodeSecCodeTooShort() {
        logger.debug("testvalidateSecurityCodeSecCodeTooShort");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISA");
        ccd.setSecurityCode("123");
        ccd.setMastercardKey("abcd");
        ccd.setVisaKey("abcd");
        ccd.setDiscoverKey("abc");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        //assertFalse(errors.hasErrorMessages());
        assertTrue(
        errors.toString().contains("creditCardDetails.securityCode.tooShort")
        );
        logger.debug("success1");
    }


    public void testvalidateSecurityCodeSecCodeTooShortI() {
        logger.debug("testvalidateSecurityCodeSecCodeTooShort");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISAK");
        ccd.setSecurityCode("12");
        ccd.setMastercardKey("abcd");
        ccd.setVisaKey("abcd");
        ccd.setDiscoverKey("abc");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        //assertFalse(errors.hasErrorMessages());
        assertTrue(
        errors.toString().contains("creditCardDetails.securityCode.tooShort")
        );
        logger.debug("success1");
    }


    public void testvalidateSecurityCodeSecCodeTooLong() {
        logger.debug("testvalidateSecurityCodeSecCodeTooShort");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISAK");
        ccd.setSecurityCode("12345345345345");
        ccd.setMastercardKey("abcd");
        ccd.setVisaKey("abcd");
        ccd.setDiscoverKey("abc");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        //assertFalse(errors.hasErrorMessages());
        assertTrue(
        errors.toString().contains("creditCardDetails.securityCode.tooLong")
        );
        logger.debug("success1");
    }


    public void testvalidateSecurityCodeSyntaxError() {
        logger.debug("testvalidateSecurityCodeSecCodeTooShort");
        ccd.setNameOnCard("ddddd");
        ccd.setCardNumber("39485793475");
        ccd.setExpiryYear(2010);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setAmexKey("VISAK");
        ccd.setSecurityCode("test");
        ccd.setMastercardKey("abcd");
        ccd.setVisaKey("abcd");
        ccd.setDiscoverKey("abc");
        underTest.validateSecurityCode(ccd, errors);
        logger.debug("error:" + errors.toString());
        //assertFalse(errors.hasErrorMessages());
        assertTrue(
        errors.toString().contains(
        "purchaseForm.creditcard.securityCode.syntax"
        )
        );
        logger.debug("success1");
    }


    public void testValidateCardExpiry() {
        ccd.setNameOnCard("ddddd");
        ccd.setExpiryYear(expiryYear);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setSecurityCode("123");

        underTest.validateCardExpiry(ccd, errors);
        logger.debug("errors " + errors.toString());
        assertFalse(errors.hasErrorMessages());
        logger.debug("success7");

    }

    public void testCardExpiryYearReqd() {
        ccd.setNameOnCard("ddddd");
        ccd.setExpiryYear(null);
        ccd.setExpiryMonth("11");
        ccd.setCardType("VISA");
        ccd.setSecurityCode("123");

        Calendar now = Calendar.getInstance();
        final int currentMonth = now.get(Calendar.MONTH);
        final int currentYear = now.get(Calendar.YEAR);

        underTest.validateCardExpiry(ccd, errors, currentMonth, currentYear);

        //logger.debug("errors "+ errors.toString());
        assertTrue(
        errors.toString().contains("purchaseForm.creditcard.expyear.mandatory")
        );
        logger.debug("success9");

    }




}


