package com.aircell.abp.model.validator;

import com.aircell.abp.model.LoginDetails;
import junit.framework.TestCase;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

public class LoginDetailsValidatorTest extends TestCase {

    /**. private class variable to hold LoginDetailsValidator object */
    private LoginDetailsValidator loginDetailsValidator;
    /**. private class variable to hold LoginDetails object */
    private LoginDetails form;
    /**. private class variable to hold MessageContext object */
    private MessageContext errors;

    /**. private class variable to hold ConfigService object */
    protected void setUp() throws Exception {
        super.setUp();
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage(
        "L_LOG_UN_SNTX_ERR", Locale.getDefault(),
        "loginDetails.username.invalid"
        );
        messageSource.addMessage(
        "L_LOG_PW_INVALID", Locale.getDefault(), "loginDetails.password.invalid"
        );

        messageSource.addMessage(
        "loginDetails.username.tooShort", Locale.getDefault(),
        "loginDetails.username.tooShort"
        );
        messageSource.addMessage(
        "L_LOG_PW_INC_LEN", Locale.getDefault(),
        "loginDetails.password.tooShort"
        );

        messageSource.addMessage(
        "loginDetails.username.tooLong", Locale.getDefault(),
        "loginDetails.username.tooLong"
        );
        messageSource.addMessage(
        "loginDetails.password.tooLong", Locale.getDefault(),
        "loginDetails.password.tooLong"
        );

        messageSource.addMessage(
        "L_LOG_UN_REQD", Locale.getDefault(), "loginDetails.username.mandatory"
        );
        messageSource.addMessage(
        "L_LOG_PW_REQD", Locale.getDefault(), "loginDetails.password.mandatory"
        );

        messageSource.addMessage(
        "loginDetails.username.tooLong", Locale.getDefault(),
        "loginDetails.username.tooLong"
        );
        messageSource.addMessage(
        "loginDetails.password.tooLong", Locale.getDefault(),
        "loginDetails.password.tooLong"
        );

        messageSource.addMessage(
        "L_LOG_TC_NOT_ACCEPTED", Locale.getDefault(),
        "error.login.notAcceptedTerms"
        );

        loginDetailsValidator = new LoginDetailsValidator();
        loginDetailsValidator.setPasswordMaxLength(26);
        loginDetailsValidator.setPasswordRegex("[a-zA-Z0-9]{6,}");
        loginDetailsValidator.setUsernameRegex("[a-zA-Z0-9]{6,}");
        loginDetailsValidator.setUsernameMaxLength(26);
        form = new LoginDetails();
        errors = new DefaultMessageContext(messageSource);
        form = new LoginDetails();
    }


    public void testValidateHappyPath() {
        form.setUsername("firstmlast");
        form.setTermsofuse(true);
        form.setPassword("firstmlast");
        loginDetailsValidator.validateLogin(form, errors);
        assertFalse(errors.hasErrorMessages());
    }


    public void testValidateInvalidUserName() {
        form.setUsername("first");
        form.setTermsofuse(true);
        form.setPassword("firstmlast");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(errors.toString().contains("loginDetails.username.invalid"));
    }

    public void testValidateInvalidPassword() {
        form.setUsername("firstmlast");
        form.setTermsofuse(true);
        form.setPassword("first");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(errors.toString().contains("loginDetails.password.invalid"));
    }

    public void testValidateTooShortUsername() {
        form.setUsername("fi");
        form.setTermsofuse(true);
        form.setPassword("first");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.username.tooShort")
        );
    }


    public void testValidateTooShortPassword() {
        form.setUsername("firstmlast");
        form.setTermsofuse(true);
        form.setPassword("fi");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.password.tooShort")
        );
    }


    public void testValidateTooLongUsername() {
        form.setUsername("firstmlastfirstmlastfirstmlastfirstmlastfirstmlast");
        form.setTermsofuse(true);
        form.setPassword("first");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(errors.toString().contains("loginDetails.username.tooLong"));
    }

    public void testValidateTooLongPassword() {
        form.setUsername("firstmlast");
        form.setTermsofuse(true);
        form.setPassword("firstmlastfirstmlastfirstmlastfirstmlast");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(errors.toString().contains("loginDetails.password.tooLong"));
    }

    public void testValidateUsernameMandatory() {
        form.setUsername("");
        form.setTermsofuse(true);
        form.setPassword("firstmlast");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.username.mandatory")
        );
    }

    public void testValidatePasswordMandatory() {
        form.setUsername("firstmlast");
        form.setTermsofuse(true);
        form.setPassword("");
        loginDetailsValidator.validateLogin(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.password.mandatory")
        );
    }

}
