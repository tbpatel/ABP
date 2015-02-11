package com.aircell.abp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class AircellEncryption{

    /**
    * The constructor.
    */
    public AircellEncryption() {
    }



    public void saveToFile(String fileName,
    BigInteger mod, BigInteger exp) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);


        ObjectOutputStream oout = new ObjectOutputStream(
        new BufferedOutputStream(fos));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
            } catch (Exception e) {
            throw new IOException("Unexpected error");
            } finally {
            oout.close();
        }
    }



    public void keygen() throws Exception
    {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
        RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
        RSAPrivateKeySpec.class);

        saveToFile("c:\\ssl\\code\\publicPOC.key", pub.getModulus(),
        pub.getPublicExponent());
        saveToFile("c:\\ssl\\code\\privatePOC.key", priv.getModulus(),
        priv.getPrivateExponent());

    }

    PublicKey readKeyFromFile(String keyFileName) throws IOException {
        InputStream in =
        AircellEncryption.class.getClassLoader().getResourceAsStream(keyFileName);
        ObjectInputStream oin =
        new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
            } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
            } finally {
            oin.close();
        }
    }


    PrivateKey readPKeyFromFile(String keyFileName) throws IOException {
        InputStream in =
        AircellEncryption.class.getClassLoader().getResourceAsStream(keyFileName);
        ObjectInputStream oin =
        new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey pubKey = fact.generatePrivate(keySpec);
            return pubKey;
            } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
            } finally {
            oin.close();
        }
    }





    public byte[] rsaEncrypt(byte[] data) {

        try{
            PublicKey pubKey = readKeyFromFile("publicPOC.key");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        }
        catch (Exception e)
        {
            System.out.println("Excep " + e.toString());
            return "error".getBytes();
        }
    }


    public byte[] rsaDecrypt(byte[] data) {
        try
        {
            PrivateKey pubKey = readPKeyFromFile("privatePOC.key");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        }
        catch (Exception e)
        {
            System.out.println("Excep " + e.toString());
            return "error".getBytes();
        }
    }








}
