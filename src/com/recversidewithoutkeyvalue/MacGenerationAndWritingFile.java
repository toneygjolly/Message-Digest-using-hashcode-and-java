package com.recversidewithoutkeyvalue;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MacGenerationAndWritingFile {
    public static void main(String[] args)
    {
    	try {

     encodeMessage();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public static void encodeMessage() throws Exception
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the message");
        String message=sc.nextLine();
        System.out.println("enter the secret key");
        String secretKey=sc.nextLine();


     KeyStore keystore =KeyStore.getInstance("JCEKS");

        // Loading the KeyStore object
        char[] password = "changeit".toCharArray();
        String path = "Mykeystoring";
        FileOutputStream fisK = new FileOutputStream(path);

        keystore.load(null, password);
        KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(password);
        SecretKey mySecretKey = new SecretKeySpec(secretKey.getBytes(), "DSA");
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(mySecretKey);
        keystore.setEntry("secretKeyAlias", secretKeyEntry, protectionParam);
        java.io.FileOutputStream fosK = null;
        fosK = new java.io.FileOutputStream(path);
        keystore.store(fosK, password);










        SecretKeySpec SKS=new  SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac hmacSha256=Mac.getInstance("HmacSHA256");
        hmacSha256.init(SKS);
        byte[] hmac=hmacSha256.doFinal(message.getBytes());

        byte[] combined=new byte[message.getBytes().length+hmac.length];
        System.arraycopy(message.getBytes(),0, combined, 0, message.getBytes().length);
        System.arraycopy(hmac, 0, combined, message.getBytes().length, hmac.length);
        String macResult= Base64.getEncoder().encodeToString(combined);


        FileOutputStream fos=new FileOutputStream("MACTEST");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(macResult);
        System.out.println("Mac ="+macResult);
    }

}