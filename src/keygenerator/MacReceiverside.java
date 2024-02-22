package keygenerator;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MacReceiverside {
    

    public static String decodeMessage() throws Exception
    {
        try {
            
        FileInputStream fis=new FileInputStream("mactest");
        ObjectInputStream ois=new ObjectInputStream(fis);
        System.out.println("test"+ois);
        Object o=ois.readObject();
        String data =(String)o;
        System.out.println("Got Message :"+data);
        /*System.out.println("enter the secret key");
        Scanner sc= new Scanner(System.in);
    
        String secretKey=sc.nextLine();*/
        
        String path = "Mykeystoring";
        
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] password = "changeit".toCharArray();
        FileInputStream fisK = new FileInputStream(path);
        keyStore.load(fisK, password);
       

        PasswordProtection protectionParam = new KeyStore.PasswordProtection(password);
        SecretKeyEntry secretKeyEntry = (SecretKeyEntry) keyStore.getEntry("secretKeyAlias", protectionParam);
        SecretKey mySecretKey = secretKeyEntry.getSecretKey();
        String secretKeyString = new String(mySecretKey.getEncoded(), StandardCharsets.UTF_8);
        SecretKeySpec SKS=new SecretKeySpec(secretKeyString.getBytes(),"HmacSHA256");
        Mac hmacSha256=Mac.getInstance("HmacSHA256");
        hmacSha256.init(SKS);
        byte[] combined =Base64.getDecoder().decode(data);
        byte[] messageBytes=new byte[combined.length-hmacSha256.getMacLength()];
        byte[] receivedHmac=new byte[hmacSha256.getMacLength()];
        System.arraycopy(combined, 0, messageBytes, 0, messageBytes.length);
        
        System.arraycopy(combined, messageBytes.length, receivedHmac, 0, receivedHmac.length);
        byte[] calculatedHmac=hmacSha256.doFinal(messageBytes);
        if(Arrays.equals(calculatedHmac,receivedHmac))
        {
            return new String(messageBytes);
            
        }
        
        else
        {
            return "INVALID MAC:message integrity compromised";
        }
        
        
        
        
        }
        catch(NoSuchAlgorithmException|InvalidKeyException e)
        {
            e.printStackTrace();
            return "error during decoding:"+e.getMessage();
            
        }
        
    }
    public static void main(String[] args) throws Exception {
        
        String decodedMeassage=decodeMessage();
        System.out.println(decodedMeassage);
        
    }
}