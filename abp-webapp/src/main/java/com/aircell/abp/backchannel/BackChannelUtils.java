/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.backchannel;

import com.aircell.abp.model.AirPassenger;
import com.aircell.abp.model.AircellUser;
import com.aircell.abp.model.FlightInformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Properties;

/**. . @author Oscar.Diaz */
public class BackChannelUtils {

    //It signals if the ABP Back channel flag was set in the request
    //for the splash page on the GBP
    /**. Static String for holding the flag*/
    public static final String ABP_FLAG = "abpflg";

    /**. Static variable for holding the flag*/
    public static final String ABP_FLAG_TO_PURCHASE = "abppurchaseflg";

    /**. Static variable for holding the flag true value*/
    public static final String ABP_FLAG_TRUE = "1";

    /**. Query string name in the portal url that is forwarded by acpu. */
    public static final String ACPU_QUERY_STRING = "acpu_redirect";
    /**.
     * value of query string acpu_redirect in the portal url that
     * is forwarded by acpu.
     */
    public static final String ACPU_QUERY_STRING_VALUE = "true";
    /**.
     * Flag to pass the acpu_redirect info to GBP. Will be used to identify acpu
     * redirect and redirect user to plnas page instead of Splash page if the
     * user has already seen the splash page
     */
    public static final String ACPU_REDIRECT_FLAG_TRUE = "2";
    public static final String ABP_REDIRECT_TRUE = "true";
    /**. Static variable for holding user Ip*/
    public static final String ABP_USER_IP = "abpuserip";
    /**. Static variable for holding user MAC address*/
    public static final String ABP_USER_MAC_ADDR = "abp_user_mac_address";
    /**. Static variable for holding the flight info string*/
    public static final String ABP_FLIGHT_INFO = "flghtinf";

    /**. Static variable for holding the session id string */
    public static final String GBP_SESSION_ID = "gbpsessionid";

    /**. added this maintain the sessionid in cookie. -magesh */
    public static final String COOKIE_SESSION_ID = "cookiesessionid";

    /**. User Name attribute key for the logged in GBP user */
    public static final String GBP_USER_NAME = "gbpuname";
    /**. User Password attribute key for the logged in GBP user */
    public static final String GBP_USER_PASSWORD = "gbppwd";

    /**. Static variable for holding the auth seperator*/
    public static final String RESULT_AUTH_SEPARATOR = ":|:";

    /**. Static variable for holding the string gbpcaptchapassed*/
    public static final String GBP_CAPTCH_PASSED = "gbpCapthcaPassed";

    /**. Static variable for holding the string activate error code key*/
    public static final String ACTIVATE_ERROR_CODE = "activate_error_code";
    /**. Static variable for holding the string activate error text key*/
    public static final String ACTIVATE_ERROR_TEXT = "activate_error_text";
    /**. Static variable for holding the string activate error cause key*/
    public static final String ACTIVATE_ERROR_CAUSE = "activate_error_cause";
    /**. Static variable for holding the string abp gbp time out*/
    public static final String ABP_GBP_TIMEOUT = "ABP_GBP_TIMEOUT";
    /**. Static variable for holding the string MAX_ABP_GBP_ATTEMPTS*/
    public static final String MAX_ABP_GBP_ATTEMPTS = "MAX_ABP_GBP_ATTEMPTS";
    /**. Static variable for holding properties*/
    private static Properties abpProperties = null;


    /**.
     * Method to set load the properties for ABP
     * and set in the abpProperties object
     * @param obj obj
     * @return prop
     */
    public static Properties getAbpProperties(Object obj) {
        try {
            if (abpProperties == null) {
                abpProperties = new Properties();
                InputStream inStream = obj.getClass().getClassLoader()
                .getResourceAsStream("abp.properties");
                abpProperties.load(inStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return abpProperties;
    }

    /**.
     * Decodes the Flight information from XML.
     * @param xmlFlight xmlFlight
     * @return fl fl
     */
    public static FlightInformation decodeFlightFromXML(
    final String xmlFlight
    ) {

        java.beans.XMLDecoder decoder = new java.beans.XMLDecoder(
        new ByteArrayInputStream(xmlFlight.getBytes())
        );
        FlightInformation fl = (FlightInformation) decoder.readObject();
        decoder.close();
        return fl;
    }

    /**.
     * Encodes the Flight information into XML. Returns the XML as String
     * @param flight flight
     * @return string  
     */
    public static String encodeFlightToXML(final FlightInformation flight) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        java.beans.XMLEncoder e = new java.beans.XMLEncoder(out);
        e.writeObject(flight);
        e.close();
        return out.toString();

    }

    /**.
     * Encodes the AircellUser information into XML. Returns the XML as String
     * @param user user
     * @return String
     */
    public static String encodeUserToXML(AirPassenger user) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        java.beans.XMLEncoder e = new java.beans.XMLEncoder(out);
        e.writeObject(user);
        e.close();
        return out.toString();

    }

    /**.
     * Decodes the AircellUser information from XML.
     * @param xmlUsr xmlUsr
     * @return ap
     */
    public static AirPassenger decodeAircellUserFromXML(String xmlUsr) {

        java.beans.XMLDecoder decoder = new java.beans.XMLDecoder(
        new ByteArrayInputStream(xmlUsr.getBytes())
        );
        AirPassenger ap = (AirPassenger) decoder.readObject();
        decoder.close();
        return ap;
    }

    /**.
     * Encodes the AircellUser information into serialized string. Returns the
     * Object as String
     * @param user user
     * @return String
     * @throws IOException ex
     */
    public static String serializeUserToArray(AircellUser user)
    throws IOException {

        //         Serialize to a byte array
        ObjectOutput out =
        new ObjectOutputStream(new FileOutputStream("filename.ser"));
        out.writeObject(user);
        out.close();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        out = new ObjectOutputStream(bos);
        out.writeObject(user);
        out.close();

        //      Get the bytes of the serialized object
        byte[] buf = bos.toByteArray();
        return buf.toString();

    }

    /**
     *
     * @param userSt st
     * @return user user
     * @throws IOException ex
     * @throws ClassNotFoundException exception
     */
    public static AircellUser deserializeUserFromArray(String userSt)
    throws IOException, ClassNotFoundException {

        //         Serialize to a byte array
        if (userSt != null && userSt.length() > 0) {
            byte[] bytes = userSt.getBytes();
            ObjectInputStream in =
            new ObjectInputStream(new ByteArrayInputStream(bytes));
            AircellUser user = (AircellUser) in.readObject();
            in.close();


            return user;
        } else {
            return null;
        }

    }


}
