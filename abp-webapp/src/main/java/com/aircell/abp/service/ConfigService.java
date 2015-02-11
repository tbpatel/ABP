/*
 * ABPConfigService.java 23 Jul 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**.
 * Service for getting all things about configuration of the airborne portal.
 * Implementations such that beans of this class can be made
 * <code>singleton</code>s in the Spring context are encouraged.
 * @author miroslav.miladinovic
 */
public interface ConfigService {

    /**.
     * Returns the version tag of currently loaded terms and conditions.
     * @return tersms and conditions version tag
     */
    String getTermsConditionsVersionTag();

    /**.
     * Returns the version tag of for Different Airline based on airline code
     * @return tersms and conditions version tag
     */
    String getAirlineTermsConditionsVersionTag(String airlinecode);

    /**.
     * Returns the the version tag of the current build.
     * @return
     */
    String getAppVersionTag();

    /**.
     * Returns the list of supported countries. Countries are keyed by their ISO
     * standard code. The value in the map is the localised name of that
     * country.
     * @param localizeCntryNamesTo which locale to localize country names to.
     * @return map of country code as the key and localized country name as the
     *         value.
     */
    Map<String, String> getSupportedUserCountries(
    final Locale localizeCntryNamesTo
    );

    /**.
     * @param localizeCntryNamesTo Locale object
     * @return Map object containing supported US States
     */
    Map<String, String> getSupportedUserUSStates(
    final Locale localizeCntryNamesTo
    );

    /**.
     * Return the list of ISO codes which denote US teritories.
     * @return the list of ISO codes which denote US teritories.
     */
    Set<String> getCountryCodesForUS();

    /**. @return  */
    Map<String, String> getSupportedCardTypes();

    /**.
     * Return the list of languages supported
     * @return the list of languages supported
     *  */
    Set<String> getLanguageSupported();

}
