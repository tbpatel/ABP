/*
 * SecurityChallengeDetailsTest.java 13 Sep 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aircell LLC. All rights reserved.
 */

package com.aircell.abp.model;

import com.aircell.abp.model.SecurityChallengeDetails.SecurityChallengeType;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**. @author miroslav.miladinovic at AKQA Inc. */
public class SecurityChallengeDetailsTest extends TestCase {

    /**. private class variable to hold SecurityChallengeDetails object */
    SecurityChallengeDetails underTest;
    /**. private class variable to hold file object */
    File tempFile;
    /**. private class variable to hold FileOutputStream object */
    FileOutputStream fos;
    /**. private class variable to hold FileInputStream object */
    FileInputStream fis;

    protected void setUp() throws Exception {
        super.setUp();
        underTest = new SecurityChallengeDetails();
        tempFile = File.createTempFile("SecurityChallengeDetailsTest", "ser");
        fos = new FileOutputStream(tempFile);
        fis = new FileInputStream(tempFile);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            if (tempFile != null) {
                tempFile.delete();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public void testSerializeSecurityChallengeObjects() throws Exception {
        final Integer ID = 1;
        final String ANSWER = "answer";
        final String QUESTION = "question";
        final ObjectOutputStream oos = new ObjectOutputStream(fos);
        underTest.setId(ID);
        underTest.setAnswer(ANSWER);
        underTest.setQuestion(QUESTION);
        underTest.setType(SecurityChallengeType.PASSWORD_CHALLENGE);
        oos.writeObject(underTest);
        oos.flush();
        oos.close();

        final ObjectInputStream ois = new ObjectInputStream(fis);
        final SecurityChallengeDetails read =
        (SecurityChallengeDetails) ois.readObject();
        ois.close();
        assertNotSame(underTest, read);
        assertNotNull(read);
        assertEquals(ID, read.getId());
        assertEquals(ANSWER, read.getAnswer());
        assertEquals(QUESTION, read.getQuestion());
        assertEquals(SecurityChallengeType.PASSWORD_CHALLENGE, read.getType());
    }

}
