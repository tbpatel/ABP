/*
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.form;

import java.io.Serializable;

/**.
 * @author AKQA - bryan.swift
 * @version $Revision: 3134 $
 */
public class StatusTrayCommand implements Serializable {
    /**. Serial id */
    private static final long serialVersionUID = -4865153786364377897L;
    /**. Username */
    private String username;

    /**. @return the username */
    public String getUsername() {
        return username;
    }

    /**. @param username the username to set */
    public void setUsername(String username) {
        this.username = username;
    }

}
