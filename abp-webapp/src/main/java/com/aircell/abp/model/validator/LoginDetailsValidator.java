package com.aircell.abp.model.validator;

import com.aircell.abp.model.LoginDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;


/**.
 * Responsible for processing the LoginDetails of an AircellUser and ensuring
 * the values meet the requirements
 * @author Geethu
 */
public class LoginDetailsValidator {
    /**. Logger */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**. Configurable username max length - spring */
    private int usernameMaxLength;

    /**. Configurabled regulare expression username must match - spring */
    private String usernameRegex;

    /**. Configurable password max length - spring */
    private int passwordMaxLength;

    /**. Configurable password min length - spring */
    private int passwordMinLength;

    /**. Configurable regular expression password must match - spring */
    private String passwordRegex;

    /**. @see org.springframework.validation.Validator#
     * supports(java.lang.Class)
     * @param clazz Class
     * @return boolean
     */
    public boolean supports(final Class clazz) {
        return LoginDetails.class.isAssignableFrom(clazz);
    }

    /**.
     * Spring Web Flow activated validation (validate + ${state}). Validates
     * 'userentry' view state after binding to loginDetails.
     * @param lDetails LoginDetails object
     * @param context MessageContext
     */
    public void validateLogin(
    final LoginDetails lDetails, MessageContext context
    ) {
        logger.debug("LoginDetailsValidator.validateLogin"
                   + " enter-------------->");

        //username validation follows
        if (StringUtils.hasText(lDetails.getUsername())) {
            if (lDetails.getUsername().length() > getUsernameMaxLength()) {
                context.addMessage(
                new MessageBuilder().error().source("username")
                .code("loginDetails.username.tooLong").build()
                );
            } else if (lDetails.getUsername().length() < 5) {
                context.addMessage(
                new MessageBuilder().error().source("username")
                .code("loginDetails.username.tooShort").build()
                );
            }

            if (!lDetails.getUsername().matches(usernameRegex)) {
                context.addMessage(
                new MessageBuilder().error().source("username")
                .code("L_LOG_UN_SNTX_ERR").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error().source("username")
            .code("L_LOG_UN_REQD").build()
            );
        }

        //password validation follows
        if (StringUtils.hasText(lDetails.getPassword())) {
            if (lDetails.getPassword().length() > getPasswordMaxLength()) {
                context.addMessage(
                new MessageBuilder().error().source("password")
                .code("loginDetails.password.tooLong").build()
                );
            } else if (lDetails.getPassword().length() < 5) {
                context.addMessage(
                new MessageBuilder().error().source("password")
                .code("L_LOG_PW_INC_LEN").build()
                );
            }

            if (!lDetails.getPassword().matches(passwordRegex)) {
                context.addMessage(
                new MessageBuilder().error().source("password")
                .code("L_LOG_PW_INVALID").build()
                );
            }
        } else {
            context.addMessage(
            new MessageBuilder().error().source("password")
            .code("L_LOG_PW_REQD").build()
            );
        }

        logger.debug("LoginDetailsValidator.validateLogin"
                   + " exit-------------->");
    }

    /**.
     * Gets Max length for Password
     * @return int passwordMaxLength
     */
    public int getPasswordMaxLength() {
        return passwordMaxLength;
    }

    /**.
     * Sets Max length for Password
     * @param passwordLength max password length
     */
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

    /**.
     * Gets Max length for UserName
     * @return int usernameMaxLength
     */
    public int getUsernameMaxLength() {
        return usernameMaxLength;
    }

    /**.
     * Sets Max length for UserName
     * @param usernameLength
     */
    public void setUsernameMaxLength(int usernameLength) {
        this.usernameMaxLength = usernameLength;
    }

    /**. @return the usernameRegex */
    public String getUsernameRegex() {
        return usernameRegex;
    }

    /**. @param usernameRegex the usernameRegex to set */
    public void setUsernameRegex(String usernameRegex) {
        this.usernameRegex = usernameRegex;
    }
}
