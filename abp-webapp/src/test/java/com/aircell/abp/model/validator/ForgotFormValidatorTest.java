package com.aircell.abp.model.validator;


import com.aircell.abp.model.ForgotForm;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

public class ForgotFormValidatorTest extends TestCase {

    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**. private class variable to hold ForgotFormValidator object */
    private ForgotFormValidator forgotFormValidator;
    /**. private class variable to hold ForgotForm object */
    private ForgotForm form;
    /**. private class variable to hold MessageContext object */
    private MessageContext errors;

    /**. private class variable to hold username Max Length */
    private int usernameMaxLength;
    /**. Configurabled regulare expression username must match - spring */
    private String usernameRegex;
    /**. Configurable password max length - spring */
    private int passwordMaxLength;
    /**. Configurable password min length - spring */
    private int passwordMinLength;
    /**. Configurable regular expression password must match - spring */
    private String passwordRegex;

    protected void setUp() throws Exception {
        super.setUp();
        StaticMessageSource messageSource = new StaticMessageSource();

        forgotFormValidator = new ForgotFormValidator();
        forgotFormValidator.setPasswordMaxLength(26);
        forgotFormValidator.setPasswordRegex("[a-zA-Z0-9]{6,}");
        form = new ForgotForm();

        errors = new DefaultMessageContext(messageSource);
        messageSource.addMessage(
        "L_FUN2_RMDR_ANS_INVALID", Locale.getDefault(),
        "securityChallengeDetails.answer.mandatory"
        );
        messageSource.addMessage(
        "L_FPW1_UN_SNTX_ERR", Locale.getDefault(),
        "loginDetails.username.mandatory"
        );
        messageSource.addMessage(
        "L_FPW2_RMDR_ANS_INVALID", Locale.getDefault(), "error.challenge.empty"
        );
        messageSource.addMessage(
        "L_FPW2_RMDR_CC_L4DIG_INVALID", Locale.getDefault(),
        "error.challenge.empty"
        );
        messageSource.addMessage(
        "L_FPW2_RMDR_ZIP_INVALID", Locale.getDefault(), "error.challenge.empty"
        );
        messageSource.addMessage(
        "L_FPW3_PWD_SNTX_ERR", Locale.getDefault(),
        "loginDetails.password.invalid"
        );
        messageSource.addMessage(
        "L_FPW3_CONFPWD_DNOT_MATCH", Locale.getDefault(),
        "login.confirmPassword.notMatch"
        );
        messageSource.addMessage(
        "L_FPW3_PWD_CONFPWD_BLANK", Locale.getDefault(),
        "loginDetails.password.mandatory"
        );
        messageSource.addMessage(
        "loginDetails.password.tooLong", Locale.getDefault(),
        "loginDetails.password.tooLong"
        );
        messageSource.addMessage(
        "L_FUN1_EM_REQD", Locale.getDefault(),
        "errors.forgotUsername.email.empty"
        );
        messageSource.addMessage(
        "L_FUN1_EM_SNTX_ERR", Locale.getDefault(),
        "eerrors.forgotUsername.email.noUser"
        );
        messageSource.addMessage(
        "L_LOG_TC_NOT_ACCEPTED", Locale.getDefault(),
        "error.login.notAcceptedTerms"
        );

    }

    public void testValidateForgotUserNameHappyPath() {
        form.setEmail("firstmlast@yahoo.com");
        forgotFormValidator.validateForgotUsernameStep1(form, errors);
        assertFalse(errors.hasErrorMessages());
    }

    public void testValidateForgotUsernameInvalidEmail() {
        form.setEmail("first");
        forgotFormValidator.validateForgotUsernameStep1(form, errors);
        assertTrue(
        errors.toString().contains("errors.forgotUsername.email.noUser")
        );
    }

    public void testValidateForgotUsernameEmpty() {
        form.setEmail("");
        forgotFormValidator.validateForgotUsernameStep1(form, errors);
        assertTrue(
        errors.toString().contains("errors.forgotUsername.email.empty")
        );
    }

    public void testValidateShowForgotUserTwoHappyPath() {
        form.setAnswer("sdfsd");
        forgotFormValidator.validateForgotUsernameStep2(form, errors);
        assertFalse(errors.hasErrorMessages());
    }

    public void testValidateShowForgotUserTwoNoAnswer() {
        form.setAnswer("");
        forgotFormValidator.validateForgotUsernameStep2(form, errors);
        assertTrue(
        errors.toString().contains("securityChallengeDetails.answer.mandatory")
        );
    }

    public void testValidateShowForgotPassOneNoUsername() {
        form.setAnswer("");
        forgotFormValidator.validateForgotPasswordStep1(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.username.mandatory")
        );
    }

    public void testValidateShowForgotPassOneHappyPath() {
        System.out
        .println("testValidateShowForgotPassOneHappyPath" + errors.toString());
        form.setUsername("sdfsdf");
        forgotFormValidator.validateForgotPasswordStep1(form, errors);
        logger.debug(errors.toString());
        assertFalse(errors.hasErrorMessages());
    }

    public void testValidateShowForgotPassTwoSavedCard() {
        form.setAnswer("");
        form.setCardAnswer("");
        form.setAddressAnswer("");
        forgotFormValidator.validateForgotPasswordStep2(form, errors);
        assertTrue(errors.toString().contains("error.challenge.empty"));
    }

    public void testValidateShowForgotPassThreeNoPassword() {
        form.setPassword("");
        form.setConfirmPassword("");
        forgotFormValidator.validateForgotPasswordStep3(form, errors);
        assertTrue(
        errors.toString().contains("loginDetails.password.mandatory")
        );
    }

    public void testValidateShowForgotPassThreeHappyPath() {
        form.setPassword("password");
        form.setConfirmPassword("password");
        form.setTermsofuse(true);
        forgotFormValidator.validateForgotPasswordStep3(form, errors);
        assertFalse(errors.hasErrorMessages());
    }

    public void testValidateShowForgotPassThreeInvalidPassword() {
        form.setPassword("sdf");
        form.setConfirmPassword("sdf");
        forgotFormValidator.validateForgotPasswordStep3(form, errors);
        assertTrue(errors.toString().contains("loginDetails.password.invalid"));
    }

    public void testValidateShowForgotPassNoMatch() {
        form.setPassword("password1");
        form.setConfirmPassword("password2");
        forgotFormValidator.validateForgotPasswordStep3(form, errors);
        assertTrue(
        errors.toString().contains("login.confirmPassword.notMatch")
        );
    }

    public void testValidateShowForgotPassTooLong() {
        form
        .setPassword("password1password2password2password2password2password2");
        form.setConfirmPassword(
        "password1password2password2password2password2password2"
        );
        forgotFormValidator.validateForgotPasswordStep3(form, errors);
        assertTrue(errors.toString().contains("loginDetails.password.tooLong"));
    }


}
