package com.aircell.abp.model;

import java.io.Serializable;
import java.util.Locale;

/**.
 *
 * Class to hold local language information
 *
 */
public class LanguageInfo implements Serializable {

    /**. private class variable serialVersionUID */
    private static final long serialVersionUID = -9125641925956396564L;
    /**. private class variable to hold local language */
    private Locale language;
    /**.
     * Gets Language
     * @return Locale
     */
    public Locale getLanguage() {
        return language;
    }

    /**.
     * Sets Language
     * @param language local language
     */
    public void setLanguage(Locale language) {
        this.language = language;
    }

}