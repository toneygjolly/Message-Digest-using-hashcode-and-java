package keygenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class keygenerationexample {
    public static void main(String[] args) throws KeyStoreException, CertificateException 
    {
        
        try {
            encodeMessage();
        }
        catch(InvalidKeyException| NoSuchAlgorithmException|IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    public static void encodeMessage() throws NoSuchAlgorithmException,InvalidKeyException,IOException, KeyStoreException, CertificateException
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the message");
        String message=sc.nextLine();
        
        KeyGenerator kg = KeyGenerator.getInstance("DES");
         SecureRandom random = new SecureRandom(); 
         System.out.println(random.nextDouble());
         kg.init(random);
         Key key = kg.generateKey();
         System.out.println(key);
            
//        System.out.println("enter the secret key");
//        String secretKey=sc.nextLine();
//        
        
        KeyStore keyStore = KeyStore.getInstance("JCEKS");

        // Loading the KeyStore object
        char[] password = "changeit".toCharArray();
        String path = "Mykeystoring";
        FileOutputStream fisK = new FileOutputStream(path);
        
        keyStore.load(null, password);
        KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(password);
        SecretKey mySecretKey = new SecretKeySpec((key.toString().getBytes()), "DSA");
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(mySecretKey);
        keyStore.setEntry("secretKeyAlias", secretKeyEntry, protectionParam);
        java.io.FileOutputStream fosK = null;
        fosK = new java.io.FileOutputStream(path);
        keyStore.store(fosK, password);
       //keystoring  
        SecretKeySpec SKS=new  SecretKeySpec(key.toString().getBytes(), "HmacSHA256");
        Mac hmacSha256=Mac.getInstance("HmacSHA256");
        hmacSha256.init(SKS);
        byte[] hmac=hmacSha256.doFinal(message.getBytes());
        
        byte[] combined=new byte[message.getBytes().length+hmac.length];
        System.arraycopy(message.getBytes(),0, combined, 0, message.getBytes().length);
        System.arraycopy(hmac, 0, combined, message.getBytes().length, hmac.length);
        String macResult= Base64.getEncoder().encodeToString(combined);
        
        
        FileOutputStream fos=new FileOutputStream("mactest");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(macResult);
        System.out.println("Mac ="+macResult);
    }

}

