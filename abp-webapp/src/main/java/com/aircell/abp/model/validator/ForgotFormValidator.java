package com.aircell.abp.model.validator;

import com.aircell.abp.model.ForgotForm;
import com.aircell.abp.model.LoginDetails;
import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;


/**.
 * Responsible for processing the forgotform details of an AircellUser and
 * ensuring the values meet the requirements
 * @author EXCELACOM - geethu
 */
public class ForgotFormValidator {

    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**. Configurable max length for password field - spring */
    private int passwordMaxLength;
    /**. Configurable min length for password field - spring */
    private int passwordMinLength;
    /**. Configurable regular expression for paassword field - spring */
    private String passwordRegex;

    /**.
     * @param clazz Class
     * @return bolean
     */
    public boolean supports(final Class clazz) {
        return LoginDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'showForgotUserOne' view state after binding to ForgotForm.
     * @param form  ForgotForm
     * @param context MessageContext
     */
    public void validateForgotUsernameStep1(
    final ForgotForm form, MessageContext context
    ) {

        logger.debug("ForgotFormValidator.validateForgotUsernameStep1"
               +" enter-------------->");
        String email = form.getEmail();
        if (StringUtils.hasText(email)) {
            EmailValidator emailValidator = EmailValidator.getInstance();

            if (!emailValidator.isValid(email)) {
                context.addMessage(
                new MessageBuilder().error().source("email")
                .code("L_FUN1_EM_SNTX_ERR").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error().source("email")
            .code("L_FUN1_EM_REQD").build()
            );
        }
        logger.debug("ForgotFormValidator.validateForgotUsernameStep1"
               +" exit-------------->");
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'showForgotUserTwo' view state after binding to ForgotForm.
     * @param form ForgotForm
     * @param context MessageContext
     */
    public void validateForgotUsernameStep2(
    final ForgotForm form, MessageContext context
    ) {
        logger.debug("ForgotFormValidator.validateForgotUsernameStep2"
                   +" enter-------------->");
        String answer = form.getAnswer();

        if (!StringUtils.hasText(answer)) {
            context.addMessage(
            new MessageBuilder().error().source("answer")
            .code("L_FUN2_RMDR_ANS_INVALID").build()
            );
        }
        logger.debug("ForgotFormValidator.validateForgotUsernameStep2"
            + "validateShowForgotUserTwo exit-------------->");
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'showForgotPassOne' view state after binding to ForgotForm.
     * @param form ForgotForm
     * @param context MessageContext
     */
    public void validateForgotPasswordStep1(
    final ForgotForm form, MessageContext context
    ) {
        logger.debug("ForgotFormValidator.validateForgotPasswordStep1"
                    + " enter-------------->");
        String username = form.getUsername();

        if (!StringUtils.hasText(username)) {
            context.addMessage(
            new MessageBuilder().error().source("username")
            .code("L_FPW1_UN_SNTX_ERR").build()
            );
        }
        logger.debug("ForgotFormValidator.validateForgotPasswordStep1"
                  + " exit-------------->");
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'showForgotPassTwo' view state after binding to ForgotForm.
     * @param form ForgotForm
     * @param context MessageContext
     */
    public void validateForgotPasswordStep2(
    final ForgotForm form, MessageContext context
    ) {
        logger.debug("ForgotFormValidator.validateForgotPasswordStep2"
                 + " enter-------------->");
        String answer = form.getAnswer();
        String cardanswer = form.getCardAnswer();
        String addressanswer = form.getAddressAnswer();

        if (!StringUtils.hasText(answer)) {
            context.addMessage(
            new MessageBuilder().error().code("L_FPW2_RMDR_ANS_INVALID").build()
            );
        }

        if ((cardanswer != null) && (addressanswer != null)) {
            if (!StringUtils.hasText(cardanswer)) {
                context.addMessage(
                new MessageBuilder().error()
                .code("L_FPW2_RMDR_CC_L4DIG_INVALID").build()
                );
            }

            if (!StringUtils.hasText(addressanswer)) {
                context.addMessage(
                new MessageBuilder().error()
                .code("L_FPW2_RMDR_ZIP_INVALID").build()
                );
            }
        }
        logger.debug("ForgotFormValidator.validateForgotPasswordStep2"
                  +" exit-------------->");
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'showForgotPassThree' view state after binding to ForgotForm.
     * @param form ForgotForm
     * @param context MessageContext
     */
    public void validateForgotPasswordStep3(
    final ForgotForm form, MessageContext context
    ) {
        logger
        .debug("ForgotFormValidator.validateForgotPasswordStep3"
                 + " enter-------------->");
        String password = form.getPassword();
        String confirmpassword = form.getConfirmPassword();

        if (!form.isTermsofuse()) {
            context.addMessage(
            new MessageBuilder().error().source("termsofuse")
            .code("L_LOG_TC_NOT_ACCEPTED").build()
            );
        }

        if (StringUtils.hasText(password) && StringUtils
        .hasText(confirmpassword)) {
            if (password.equalsIgnoreCase(confirmpassword)) {
                if (password.length() > getPasswordMaxLength()) {
                    context.addMessage(
                    new MessageBuilder().error()
                    .code("loginDetails.password.tooLong").build()
                    );
                } else if (password.length() < getPasswordMinLength()) {
                    context.addMessage(
                    new MessageBuilder().error()
                    .code("L_LOG_PW_INC_LEN").build()
                    );
                }

                if (!password.matches(passwordRegex)) {
                    context.addMessage(
                    new MessageBuilder().error()
                    .code("L_FPW3_PWD_SNTX_ERR").build()
                    );
                }
            } else {
                context.addMessage(
                new MessageBuilder().error()
                .code("L_FPW3_CONFPWD_DNOT_MATCH").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error()
            .code("L_FPW3_PWD_CONFPWD_BLANK").build()
            );
        }
        logger
        .debug("ForgotFormValidator.validateForgotPasswordStep3"
                   + "  exit-------------->");
    }

    /**. @return passwordMaxLength */
    public int getPasswordMaxLength() {
        return passwordMaxLength;
    }

    /**. @param passwordLength */
    public void setPasswordMaxLength(int passwordLength) {
        this.passwordMaxLength = passwordLength;
    }

    /**. @return the passwordRegex */
    public String getPasswordRegex() {
        return passwordRegex;
    }

    /**. @return the passwordMinLength */
    public int getPasswordMinLength() {
        return passwordMinLength;
    }

    /**. @param passwordMinLength the passwordMinLength to set */
    public void setPasswordMinLength(int passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    /**. @param passwordRegex the passwordRegex to set */
    public void setPasswordRegex(String passwordRegex) {
        this.passwordRegex = passwordRegex;
    }
}
