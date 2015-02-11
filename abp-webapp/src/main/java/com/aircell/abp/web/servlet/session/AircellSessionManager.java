/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */
package com.aircell.abp.web.servlet.session;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aircell.abp.model.AircellUser;
import com.aircell.abp.model.ApplicationState;
import com.aircell.abp.model.CreditCardDetails;
import com.aircell.abp.model.FlightInformation;
import com.aircell.abp.model.OrderLineItem;
import com.aircell.abp.model.SubscriptionProduct;
import com.aircell.abp.model.UserCredentials;
import com.aircell.abp.model.business.ApplicationStateManager;
import com.aircell.abp.utils.AircellServiceUtils;


/**.
 * . Object used to keep session namespace clean
 * @author AKQA - bryan.swift
 * @version $Revision: 3613 $
 */
public class AircellSessionManager {
    /**. . Application view state */
    private ApplicationStateManager stateManager;

    /**. . loggedInUser */
    private int count;
    private AircellUser user;

    /**. . flag for is this is a new user in the session */
    private boolean newUser;

    /**. . forgot path user */
    private AircellUser forgotUser;

    /**. . shopping cart / basket */
    private List<OrderLineItem> orders;

    /**. . selected product */
    private SubscriptionProduct product;

    /**. . credit card information */
    private CreditCardDetails card;

    /**. . Whether or not the card should be saved on purchase */
    private boolean saveCard;

    /**. . Whether or not the user has active service */
    private boolean activeService;

    /**. . credentials object */
    private UserCredentials credentials;

    /**. . Flight info for this session */
    private FlightInformation flightInfo;

    /**. . Cached products */
    private List<SubscriptionProduct> products;

    /**.
     * . Calendar representing the time when the product list was set into the
     * session
     */
    private Calendar productSetTime;

    /**. . jsessionid for this user's session */
    private String jsessionid;

    /**. . contactType for the contact us page email/Phone chaked */
    private String contactType;

    /**. . transactionType for the webtrends */
    private String transactionType;


    private String redirectionError = null;

    /**. . to display captchaError */
    private String captchaError;

    /**. . auto_login flag to indicate active session in ACPU. */
    private boolean autoLogin;

    /**. . auto_login username, username in ACPU active session. */
    private String autoLoginUsername;

    /**. . to set Webtrends Id */
    private String webtrendsId;

    /**. . to set Invoice Date */
    private String invoiceDate;

    /**. . to set Invoice Time */
    private String invoiceTime;

    /**. . to set Quantity */
    private int quantity;

    private String airlineCode;
    private String sessionType;
    private String unavailableService;
    
    private boolean redirectFlag=false;

    /**. Flag to indicate user has seen splash page */
    private boolean userSeenSplashPage;
    /**. Flag to inidicate the forcedAuthRequired */
    private boolean forcedAuthRequired;
    /**. Flag to inidicate the forcedAuthenticated */
    private boolean forcedAuthenticated;
    /**. String variable to  hold the authreqform*/
    private String authRequestFrom;
    /**. String variable to  hold the abpVersion*/
    private String abpVersion;
    /**. String variable to  hold the tcVerstion*/
    private String tcVersion;
    /**. String variable to  hold the acpuVersion*/
    private String acpuVersion;
    /**. String variable to  hold the language*/
    private Locale language;
    /**. String variable to  hold the loginSuccess value*/
    private String loginSuccess;
    /**. String variable to  hold the fromURL value*/
    private String fromUrl;
    /**. String variable to  hold the toURL value*/
    private String toUrl;
    /**String variable to  hold the product type value*/
    private String paymentType;
    /**Flag to check the user is a Roaming user or not*/
    private boolean isRoamingUser;
    /**Flag to check the user is a Smart Client user or not*/
    private boolean isSmartClientUser;
    
    private String whitelistVersion;

    /**. . Default Constructor * */
    public AircellSessionManager() {
        this.stateManager = new ApplicationStateManager();
    }

    /**. . @return tcVersion - TOS version retrieved from MDS */
    public String getTcVersion() {
        return tcVersion;
    }

    /**. . @param tcVersion TOS version retrieved from MDS */
    public void setTcVersion(String tcVersion) {
        this.tcVersion = tcVersion;
    }

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    /**. . Method to clean up session variables once purchase is completed */
    public String getAbpVersion() {
        return abpVersion;
    }

    public void setAbpVersion(String abpVersion) {
        this.abpVersion = abpVersion;
    }

    public String getAcpuVersion() {
        return acpuVersion;
    }

    public void setAcpuVersion(String acpuVersion) {
        this.acpuVersion = acpuVersion;
    }

    public void postPurchaseCleanUp() {
        setOrders(null);
        setSaveCard(false);
        setNewUser(false);
    }

    /**. . @return the state */
    public ApplicationStateManager getStateManager() {
        return stateManager;
    }

    /**. . @param state the state to set */
    public void setStateManager(ApplicationStateManager stateManager) {
        this.stateManager = stateManager;
    }

    /**.
     * . Get the ApplicationState associated with this session's
     * ApplicationStateManager
     * @return ApplicationState for ApplicationStateManager
     */
    public ApplicationState getState() {
        ApplicationStateManager stateManager = getStateManager();
        ApplicationState state = stateManager.getState();

        return state;
    }

    /**. . @return the orders */
    public List<OrderLineItem> getOrders() {
        return orders;
    }

    /**. . @param orders the orders to set */
    public void setOrders(List<OrderLineItem> orders) {
        this.orders = orders;
    }

    /**. . @return the product */
    public SubscriptionProduct getProduct() {
        return product;
    }

    /**. . @param product the product to set */
    public void setProduct(SubscriptionProduct product) {
        this.product = product;
    }

    /**. . @return the user */
    public AircellUser getUser() {
        return user;
    }

    /**. . @param user the user to set */
    public void setUser(AircellUser user) {
        this.user = user;
    }

    /**. . @return the forgotUser */
    public AircellUser getForgotUser() {
        return forgotUser;
    }

    /**. . @param forgotUser the forgotUser to set */
    public void setForgotUser(AircellUser forgotUser) {
        this.forgotUser = forgotUser;
    }

    /**. . @return the activeService */
    public boolean isActiveService() {
        return activeService;
    }

    /**. . @return the card */
    public CreditCardDetails getCard() {
        return card;
    }

    /**. . @param card the card to set */
    public void setCard(CreditCardDetails card) {
        this.card = card;
    }

    /**. . @return the activeService */
    public boolean hasActiveService() {
        return activeService;
    }

    /**. . @param activeService the activeService to set */
    public void setActiveService(boolean activeService) {
        this.activeService = activeService;
    }

    /**. . @return the saveCard */
    public boolean isSaveCard() {
        return saveCard;
    }

    /**. . @param saveCard the saveCard to set */
    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    /**. . @return the credentials */
    public UserCredentials getCredentials() {
        return credentials;
    }

    /**. . @param credentials the credentials to set */
    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }

    /**. . @return the flight */
    public FlightInformation getFlightInfo() {
        return flightInfo;
    }

    /**. . @param flight the flight to set */
    public void setFlightInfo(FlightInformation flightInfo) {
        this.flightInfo = flightInfo;
    }

    /**. . @return the jsessionid */
    public String getJsessionid() {
        return jsessionid;
    }

    /**. . @param jsessionid the jsessionid to set */
    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    /**. . @return the newUser */
    public boolean isNewUser() {
        return newUser;
    }

    /**. . @param newUser the newUser to set */
    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    /**. . @return the captchaError */
    public String getCaptchaError() {
        return captchaError;
    }

    /**. . @param to set captchaError */
    public void setCaptchaError(String captchaError) {
        this.captchaError = captchaError;
    }

    /**. . @return the Webtrends Id */
    public String getWebtrendsId() {
        return webtrendsId;
    }

    /**. . @param to set captchaError */
    public void setWebtrendsId(String webtrendsId) {
        this.webtrendsId = webtrendsId;
    }

    /**. . @return the Invoice date */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**. . @param to set Invoice Date */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**. . @return the Invoice Time */
    public String getInvoiceTime() {
        return invoiceTime;
    }

    /**. . @param to set time */
    public void setInvoiceTime(String invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    /**. . @param to set Quantity */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**. . @return the Quantity */
    public int getQuantity() {
        return quantity;
    }

    /**. . @return the products */
    public List<SubscriptionProduct> getProducts() {
        List<SubscriptionProduct> products = null;
        Calendar now = Calendar.getInstance();
        Calendar setTime = this.productSetTime;

        if (setTime != null) {
            long setMillis = setTime.getTimeInMillis();
            long nowMillis = now.getTimeInMillis();
            long diff = nowMillis - setMillis;
            long expiresAfter =
            AircellServiceUtils.PRODUCTS_EXPIRE_AFTER_MINUTES * 60 * 1000;

            if (diff <= expiresAfter) {
                products = this.products;
            }
        }

        this.products = products;

        return products;
    }

    /**. . @param products the products to set */
    public void setProducts(List<SubscriptionProduct> products) {
        this.products = products;

        Calendar now = Calendar.getInstance();
        this.productSetTime = now;
    }

    public String getRedirectionError() {
        return redirectionError;
    }

    public void setRedirectionError(String redirectionError) {
        final Logger logger =
        LoggerFactory.getLogger(AircellSessionManager.class);
        logger.info("AircellSessionManager.setRedirectionError:"
                + " redirectionError_bean:" + redirectionError);
        this.redirectionError = redirectionError;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**.
     * . sets the userSeenSplashPage
     * @param userSeenSplashPage
     */
    public void setUserSeenSplashPage(boolean userSeenSplashPage) {
        this.userSeenSplashPage = userSeenSplashPage;
    }

    /**.
     * . returns userSeenSplashPage that indicates whether the user has seen the
     * splash page or not
     * @return userSeenSplashPage
     */
    public boolean hasUserSeenSplashPage() {
        return this.userSeenSplashPage;
    }

    public boolean isAutoLogin() {
        return this.autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getAutoLoginUsername() {
        return this.autoLoginUsername;
    }

    public void setAutoLoginUsername(String autoLoginUsername) {
        this.autoLoginUsername = autoLoginUsername;
    }

    public void setForcedAuthRequired(boolean forcedAuthRequired) {
        this.forcedAuthRequired = forcedAuthRequired;
    }

    public boolean isForcedAuthRequired() {
        return this.forcedAuthRequired;
    }

    public void setAuthRequestFrom(String authRequestFrom) {
        this.authRequestFrom = authRequestFrom;
    }

    public String getAuthRequestFrom() {
        return this.authRequestFrom;
    }

    public boolean isForcedAuthenticated() {
        return this.forcedAuthenticated;
    }

    public void setForcedAuthenticated(boolean forcedAuthenticated) {
        this.forcedAuthenticated = forcedAuthenticated;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    /**. . @param sessionType the sessionType to set */
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    /**. . @return the sessionType */
    public String getSessionType() {
        return sessionType;
    }

    /**. . @return the fromUrl */
    public String getFromUrl() {
        return fromUrl;
    }

    /**. . @param fromUrl the fromUrl to set */
    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    /**. . @return the toUrl */
    public String getToUrl() {
        return toUrl;
    }


    /**. . @param toUrl the toUrl to set */
    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }


    /**. . @return the contactType */
    public String getContactType() {
        return contactType;
    }

    /**. . @param contactType the contactType to set */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    /**. . @return the transactionType */
    public String getTransactionType() {
        return transactionType;
    }

    /**. . @param transactionType the transactionType to set */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**. . @param unavailableService the unavailableService to set */
    public void setUnavailableService(String unavailableService) {
        this.unavailableService = unavailableService;
    }

    /**. . @return the unavailableService */
    public String getUnavailableService() {
        return unavailableService;
    }

	public boolean isRedirectFlag() {
		return redirectFlag;
	}

	public void setRedirectFlag(boolean redirectFlag) {
		this.redirectFlag = redirectFlag;
	}

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

	/**To check the user is a Roaming user or not.
	 * @return boolean isRoamingUser
	 */
	public boolean isRoamingUser() {
		return isRoamingUser;
	}

	/**Sets Flag for a Roaming user
	 * @param isRoamingUser isRoamingUser
	 */
	public void setRoamingUser(boolean isRoamingUser) {
		this.isRoamingUser = isRoamingUser;
	}

	/**To check the user is a SmartClient user or not.
	 * @return boolean isSmartClientUser
	 */
	public boolean isSmartClientUser() {
		return isSmartClientUser;
	}

	/**Sets Flag for a SmartClient user
	 * @param isSmartClientUser isSmartClientUser
	 */
	public void setSmartClientUser(boolean isSmartClientUser) {
		this.isSmartClientUser = isSmartClientUser;
	}
 
    
	public String getWhitelistVersion() {
		return whitelistVersion;
	}
	public void setWhitelistVersion(String whitelistVersion) {
		this.whitelistVersion = whitelistVersion;
	}
}
