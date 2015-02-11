/**.
 *
 */
package com.aircell.abp.model.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

/**.
 * @author AKQA - bryan.swift
 * @version $Revision: 3142 $
 */
public class AircellValidatorUtils {
    /**.
     * @see AircellValidatorUtils#rejectIfEmptyOrWhitespace(String, Errors,
     *      String, String, Object[], String)
     */
    public static void rejectIfEmptyOrWhitespace(
    Object value, Errors errors, String field, String errorCode
    ) {
        rejectIfEmptyOrWhitespace(value, errors, field, errorCode, null, null);
    }

    /**.
     * @see AircellValidatorUtils#rejectIfEmptyOrWhitespace(String, Errors,
     *      String, String, Object[], String)
     */
    public static void rejectIfEmptyOrWhitespace(
    Object value, Errors errors, String field, String errorCode,
    String defaultMessage
    ) {
        rejectIfEmptyOrWhitespace(
        value, errors, field, errorCode, null, defaultMessage
        );
    }

    /**.
     * @see AircellValidatorUtils#rejectIfEmptyOrWhitespace(String, Errors,
     *      String, String, Object[], String)
     */
    public static void rejectIfEmptyOrWhitespace(
    Object value, Errors errors, String field, String errorCode,
    Object[] errorArgs, String defaultMessage
    ) {
        String stringValue;
        if (value == null) {
            stringValue = null;
        } else {
            stringValue = value.toString();
        }
        rejectIfEmptyOrWhitespace(
        stringValue, errors, field, errorCode, errorArgs, defaultMessage
        );
    }

    /**.
     * Checks if a value is blank and if it is rejects the value
     * @param value to test
     * @param errors to use for rejecting
     * @param field name to reject
     * @param errorCode code to give as rejection reason
     * @param errorArgs to be provided with error message
     * @param defaultMessage if the errorCode is null or doesn't map
     */
    public static void rejectIfEmptyOrWhitespace(
    String value, Errors errors, String field, String errorCode,
    Object[] errorArgs, String defaultMessage
    ) {
        if (StringUtils.isBlank(value)) {
            errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
        }
    }
}
