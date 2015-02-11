/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without
  *  consent are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model.business;

import com.aircell.abp.model.ApplicationState;

/**.
 * .
 * @author AKQA - bryan.swift
 * @version $Revision: 3285 $
 */
public class ApplicationStateManager {
    private ApplicationState state;

    public ApplicationStateManager() {
        this.state = new ApplicationState();
        showLogin(); // initial login state
        showRegister(); // initial register state
        showPurchaseUs(); // initial purchase state
        showConcierge(); // initial splash state
        state.setManager(this);
    }

    public ApplicationState getState() {
        return state;
    }

    /* !! Login Module States !! */
    public void showLogin() {
        state.setLoginModule(ApplicationState.LOGIN);
    }

    public void showRedirection() {
        state.setLoginModule(ApplicationState.LOGIN);
    }

    public void showPromotional() {
        state.setLoginModule(ApplicationState.PROMOTIONAL);
    }

    public void showForgotUsernameStepOne() {
        state.setLoginModule(ApplicationState.USERNAME_ONE);
    }

    public void showForgotUsernameStepTwo() {
        state.setLoginModule(ApplicationState.USERNAME_TWO);
    }

    public void showForgotPasswordStepOne() {
        state.setLoginModule(ApplicationState.PASSWORD_ONE);
    }

    public void showForgotPasswordStepTwo() {
        state.setLoginModule(ApplicationState.PASSWORD_TWO);
    }

    public void showForgotPasswordStepThree() {
        state.setLoginModule(ApplicationState.PASSWORD_THREE);
    }

    public void showForgotPasswordStepFour() {
        state.setLoginModule(ApplicationState.PASSWORD_FOUR);
    }

    /* !! Register Module States !! */
    public void showPurchase() {
        if (state.getRegisterModule() != ApplicationState.FREE_PURCHASE) {
            state.setRegisterModule(ApplicationState.PURCHASE);
        }
    }

    public void showRegister() {
        if (state.getRegisterModule() != ApplicationState.FREE_PURCHASE) {
            state.setRegisterModule(ApplicationState.REGISTER);
        }
    }

    public void showEditCard() {
        if (state.getRegisterModule() != ApplicationState.FREE_PURCHASE) {
            state.setRegisterModule(ApplicationState.EDIT_CARD);
        }
    }

    public void showSavedCard() {
        if (state.getRegisterModule() != ApplicationState.FREE_PURCHASE) {
            state.setRegisterModule(ApplicationState.SAVED_CARD);
        }
    }

    public void showDeleteCard() {
        if (state.getRegisterModule() != ApplicationState.FREE_PURCHASE) {
            state.setRegisterModule(ApplicationState.DELETE_CARD);
        }
    }

    public void showFreePurchase() {
        state.setRegisterModule(ApplicationState.FREE_PURCHASE);
    }

    /* !! Purchase Module States !! */
    public void showPurchaseUs() {
        state.setPurchaseModule(ApplicationState.PURCHASE_US);
    }

    public void showPurchaseIntl() {
        state.setPurchaseModule(ApplicationState.PURCHASE_INTL);
    }

    /* !! Order Module States !! */
    public void showOrderSummary() {
        state.setLoginModule(ApplicationState.ORDER_PROMO);
    }

    public void showOrderSummaryDiscount() {
        state.setLoginModule(ApplicationState.ORDER_DISCOUNT);
    }

    public void showOrderPromoError() {
        state.setLoginModule(ApplicationState.ORDER_PROMO_ERROR);
    }

    /* !! Splash Module States !! */
    public void showConcierge() {
        state.setSplashInfoModule(ApplicationState.CONCIERGE);
    }

    public void showWsj() {
        state.setSplashInfoModule(ApplicationState.WSJ);
    }

    public void showFDG() {
        state.setSplashInfoModule(ApplicationState.FDG);
    }

    public boolean isLogin() {
        int loginModule = state.getLoginModule();
        boolean login = ApplicationState.LOGIN == loginModule;
        login = login || ApplicationState.PASSWORD_ONE == loginModule
        || ApplicationState.PASSWORD_TWO == loginModule;
        login = login || ApplicationState.PASSWORD_THREE == loginModule
        || ApplicationState.PASSWORD_FOUR == loginModule;
        login = login || ApplicationState.USERNAME_ONE == loginModule
        || ApplicationState.USERNAME_TWO == loginModule;
        login = login || ApplicationState.PROMOTIONAL == loginModule;
        return login;
    }

    public boolean isRegister() {
        int registerModule = state.getRegisterModule();
        boolean register = ApplicationState.REGISTER == registerModule;
        return register;
    }

    public boolean isPurchase() {
        int registerModule = state.getRegisterModule();
        boolean register = ApplicationState.PURCHASE == registerModule;
        register = register || ApplicationState.EDIT_CARD == registerModule
        || ApplicationState.SAVED_CARD == registerModule;
        register = register || ApplicationState.DELETE_CARD == registerModule
        || ApplicationState.FREE_PURCHASE == registerModule;
        return register;
    }

    public boolean isOrderSummary() {
        int loginModule = state.getLoginModule();
        boolean orderSummary = ApplicationState.ORDER_DISCOUNT == loginModule;
        orderSummary =
        orderSummary || ApplicationState.ORDER_PROMO == loginModule;
        orderSummary =
        orderSummary || ApplicationState.ORDER_PROMO_ERROR == loginModule;
        return orderSummary;
    }
}
