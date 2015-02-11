/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms,
 * without consent are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import com.aircell.abp.model.business.ApplicationStateManager;

/**...
 * Bean to hold the current state for the views
 * @author AKQA - bryan.swift
 * @version $Revision: 3476 $
 */
@Configurable
public class ApplicationState {
    /**.. Logger .*/
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // -- next in sequence 22
    /* !! Login Module States !! */
    /**.. . Application State static variable for login */
    public static final Integer LOGIN = 1;
    /**.. . Application State static variable for login */
    public static final Integer PROMOTIONAL = 11;
    /**.. . Application State static variable for login */
    public static final Integer PASSWORD_ONE = 2;
    /**.. . Application State static variable for login */
    public static final Integer PASSWORD_TWO = 3;
    /**.. . Application State static variable for login */
    public static final Integer PASSWORD_THREE = 4;
    /**.. . Application State static variable for login */
    public static final Integer PASSWORD_FOUR = 16;
    /**.. . Application State static variable for login */
    public static final Integer USERNAME_ONE = 5;
    /**.. . Application State static variable for login */
    public static final Integer USERNAME_TWO = 6;
    /* !! Order Module States !! */
    /**.. . Application State static variable */
    public static final Integer ORDER_PROMO = 9;
    /**.. . Application State static variable */
    public static final Integer ORDER_DISCOUNT = 10;
    /**.. . Application State static variable */
    public static final Integer ORDER_PROMO_ERROR = 21;

    /* !! Register Module States !! */
    /**.. . Application State static variable */
    public static final Integer REGISTER = 7;
    /**.. . Application State static variable */
    public static final Integer PURCHASE = 8;
    /**.. . Application State static variable */
    public static final Integer EDIT_CARD = 12;
    /**.. . Application State static variable */
    public static final Integer SAVED_CARD = 13;
    /**.. . Application State static variable */
    public static final Integer DELETE_CARD = 15;
    /**.. . Application State static variable */
    public static final Integer FREE_PURCHASE = 14;

    /* !! Purchase Module States !! */
    /**.. . Application State static variable */
    public static final Integer PURCHASE_INTL = 19;
    /**.. . Application State static variable */
    public static final Integer PURCHASE_US = 20;

    /* !! Splash Information States !! */
    /**.. . Application State static variable */
    public static final Integer CONCIERGE = 17;
    /**.. . Application State static variable */
    public static final Integer WSJ = 18;
    /**.. . Application State static variable */
    public static final Integer FDG = 19;
    /**.. . Application State static variable */
    private ApplicationStateManager manager;


    /**.. . Private Variable for setter and getter*/
    private Integer loginModule;
    /**.. . Private Variable for setter and getter*/
    private Integer registerModule;
    /**.. . Private Variable for setter and getter*/
    private Integer purchaseModule;
    /**.. . Private Variable for setter and getter*/
    private Integer splashInfoModule;

    /**.. . Private Variable for setter and getter*/
    private boolean javascriptEnabled;

    /**.. . Private Variable for setter and getter*/
    private Map<String, Object> constants;

    /**.. . Private Variable for setter and getter*/
    private String stateName;
    /**.. . Private Variable for setter and getter*/
    private String domainName;
    /**.. . Private Variable for setter and getter*/
    private String domainSecure;
    /**.. . Private Variable for setter and getter*/
    private String staticWebPath;
    /**.. . Private Variable for setter and getter*/
    private String staticGogoWebPath;
    /**.. . Private Variable for setter and getter*/
    private String springWebPath;
    /**.. . Private Variable for setter and getter*/
    private String jahiaWebPath;
    /**.. . Private Variable for setter and getter*/
    private String jahiaVirtualSite;
    /**.. . Private Variable for setter and getter*/
    private String home;
    /**.. . Private Variable for setter and getter*/
    private String gbpTrackingPath;
    /**.. . Private Variable for setter and getter*/
    private String gbpTrackingUrl;
    /**.. . Private Variable for setter and getter*/
    private String ampTrackingPath;
    /**.. . Private Variable for setter and getter*/
    private String ampTrackingUrl;
    /**.. . Private Variable for setter and getter*/
    private String dotcomTrackingPath;
    /**.. . Private Variable for setter and getter*/
    private String dotcomTrackingUrl;

    /**
     *
     */
    public ApplicationState() {
        // done so constants can be accessible in jsp pages
        constants = new HashMap<String, Object>();
        Class applicationState = getClass();
        Field[] fields = applicationState.getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)
            && Modifier.isPublic(modifiers)) {
                String name = field.getName();
                Object value = null;
                try {
                    value = field.get(null);
                } catch (IllegalAccessException iae) {
                    logger.error("unable to add {} "
                    + "to constants map", name, iae);
                }
                if (name != null && value != null) {
                    constants.put(name, value);
                }
            }
        }
    }


    /**..
     * Getter Method
     * @return loginModule
     */
    public Integer getLoginModule() {
        return loginModule;
    }

    /**..
     * Setter Method
     * @param loginModule loginModule
     */
    public void setLoginModule(Integer loginModule) {
        this.loginModule = loginModule;
    }

    /**..
     * Getter Method
     * @return registerModule
     */
    public Integer getRegisterModule() {
        return registerModule;
    }

    /**..
     * Setter Method
     * @param purchaseModule purchaseModule
     */
    public void setRegisterModule(Integer purchaseModule) {
        this.registerModule = purchaseModule;
    }

    /**..
     * Getter Method
     * @return purchaseModule
     */
    public Integer getPurchaseModule() {
        return purchaseModule;
    }


    /**..
     * Setter Method
     * @param purchaseModule  purchaseModule
     */
    public void setPurchaseModule(Integer purchaseModule) {
        this.purchaseModule = purchaseModule;
    }

    /**..
     * getter Method
     * @return splashInfoModule
     */
    public Integer getSplashInfoModule() {
        return splashInfoModule;
    }

    /**..
     * Setter Method
     * @param splashInfoModule splashInfoModule
     */
    public void setSplashInfoModule(Integer splashInfoModule) {
        this.splashInfoModule = splashInfoModule;
    }

    /**..
     * getter Method
     * @return javascriptEnabled
     */
    public boolean isJavascriptEnabled() {
        return javascriptEnabled;
    }

    /**..
     * Setter Method
     * @param javascriptEnabled javascriptEnabled
     */
    public void setJavascriptEnabled(boolean javascriptEnabled) {
        this.javascriptEnabled = javascriptEnabled;
    }

    /**..
     * Getter Method
     * @return constants
     */
    public Map<String, Object> getConstants() {
        return constants;
    }

    /**..
     * Getter Method
     * @return manager
     */
    public ApplicationStateManager getManager() {
        return manager;
    }

    /**..
     * Setter Method
     * @param manager manager
     */
    public void setManager(ApplicationStateManager manager) {
        this.manager = manager;
    }

    // *** SPRING ***


    /**..
     * Setter Method
     * @return home
     */
    public String getHome() {
        return home;
    }

    /**..
     * Setter Method
     * @param home  home
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**..
     * Getter Method
     * @return jahiaWebPath
     */
    public String getJahiaWebPath() {
        return jahiaWebPath;
    }

    /**..
     * Getter Method
     * @param jahiaWebPath jahiaWebPath
     */
    public void setJahiaWebPath(String jahiaWebPath) {
        this.jahiaWebPath = jahiaWebPath;
    }

    /**..
     * Getter Method
     * @return springWebPath
     */
    public String getSpringWebPath() {
        return springWebPath;
    }

    /**..
     * Setter Method
     * @param springWebPath springWebPath
     */
    public void setSpringWebPath(String springWebPath) {
        this.springWebPath = springWebPath;
    }

    /**..
     * Getter Method
     * @return staticWebPath
     */
    public String getStaticWebPath() {
        return staticWebPath;
    }

    /**..
     * Setter Method
     * @param staticWebPath staticWebPath
     */
    public void setStaticWebPath(String staticWebPath) {
        this.staticWebPath = staticWebPath;
    }

    /**..
     * Getter Method
     * @return staticGogoWebPath
     */
    public String getStaticGogoWebPath() {
        return staticGogoWebPath;
    }

    /**..
     * Setter Method
     * @param staticGogoWebPath staticGogoWebPath
     */
    public void setStaticGogoWebPath(String staticGogoWebPath) {
        this.staticGogoWebPath = staticGogoWebPath;
    }

    /**..
     * Getter Method
     * @return ampTrackingPath
     */
    public String getAmpTrackingPath() {
        return ampTrackingPath;
    }

    /**..
     * Setter Method
     * @param ampTrackingPath ampTrackingPath
     */
    public void setAmpTrackingPath(String ampTrackingPath) {
        this.ampTrackingPath = ampTrackingPath;
    }

    /**..
     * Getter Method
     * @return ampTrackingUrl
     */
    public String getAmpTrackingUrl() {
        return ampTrackingUrl;
    }

    /**..
     * Setter Method
     * @param ampTrackingUrl ampTrackingUrl
     */
    public void setAmpTrackingUrl(String ampTrackingUrl) {
        this.ampTrackingUrl = ampTrackingUrl;
    }

    /**..
     * Getter Method
     * @return gbpTrackingPath
     */
    public String getGbpTrackingPath() {
        return gbpTrackingPath;
    }

    /**..
     * Setter Method
     * @param gbpTrackingPath gbpTrackingPath
     */
    public void setGbpTrackingPath(String gbpTrackingPath) {
        this.gbpTrackingPath = gbpTrackingPath;
    }

    /**..
     * Getter Method
     * @return gbpTrackingUrl
     */
    public String getGbpTrackingUrl() {
        return gbpTrackingUrl;
    }

    /**..
     * Setter Method
     * @param gbpTrackingUrl  gbpTrackingUrl
     */
    public void setGbpTrackingUrl(String gbpTrackingUrl) {
        this.gbpTrackingUrl = gbpTrackingUrl;
    }

    /**..
     * Getter Method
     * @return domainName
     */
    public String getDomainName() {
        return domainName;
    }

    /**..
     * Setter Method
     * @param domainName   domainName
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**..
     * Getter Method
     * @return   domainSecure
     */
    public String getDomainSecure() {
        return domainSecure;
    }

    /**..
     * Setter Method
     * @param domainSecure  domainSecure
     */
    public void setDomainSecure(String domainSecure) {
        this.domainSecure = domainSecure;
    }

    /**..
     * Getter Method
     * @return dotcomTrackingPath
     */
    public String getDotcomTrackingPath() {
        return dotcomTrackingPath;
    }

    /**..
     * Setter Method
     * @param dotcomTrackingPath dotcomTrackingPath
     */
    public void setDotcomTrackingPath(String dotcomTrackingPath) {
        this.dotcomTrackingPath = dotcomTrackingPath;
    }

    /**..
     * @return dotcomTrackingUrl
     * */
    public String getDotcomTrackingUrl() {
        return dotcomTrackingUrl;
    }

    /**..
     * Setter Method
     * @param dotcomTrackingUrl dotcomTrackingUrl
     */
    public void setDotcomTrackingUrl(String dotcomTrackingUrl) {
        this.dotcomTrackingUrl = dotcomTrackingUrl;
    }


    /**..
     * Getter Method
     * @return jahiaVirtualSite
     */
    public String getJahiaVirtualSite() {
        return jahiaVirtualSite;
    }

    /**..
     * Setter Method
     * @param jahiaVirtualSite jahiaVirtualSite
     */
    public void setJahiaVirtualSite(String jahiaVirtualSite) {
        this.jahiaVirtualSite = jahiaVirtualSite;
    }

    /**..
     * Getter Method
     * @return stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**..
     * Setter Method
     * @param stateName stateName
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
