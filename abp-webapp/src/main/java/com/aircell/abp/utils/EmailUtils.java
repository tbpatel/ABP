/*
 * This source file is Proprietary and Confidential.
 * 
 * Redistribution and use in source and binary forms, without consent 
 * are strictly forbidden. 
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.utils;

import org.apache.commons.lang.StringUtils;

/**.
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class EmailUtils {
    /**.
     * Converts a comma separated list into an array of strings,
     *  if there is only * one entry then an array
     *  with a single string is returned
     * @param recipientString - comma separated list
     * @return String[] of recipients included in recipientString
     */
    public static String[] commaSeparatedToArray(String recipientString) {
        String[] recipients;
        if (recipientString.indexOf(",") != -1) {
            recipients = StringUtils.split(recipientString, ",");
        } else {
            recipients = new String[]{ recipientString };
        }
        return recipients;
    }
}
