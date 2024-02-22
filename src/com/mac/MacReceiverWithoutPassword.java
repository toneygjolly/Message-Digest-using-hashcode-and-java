package com.mac;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacReceiverWithoutPassword {

    public static String decodeMessage() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        try {
            FileInputStream fis = new FileInputStream("mactest");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            String data = (String) o;
            System.out.println("Received message: " + data);

            // Retrieve the secret key from a secure source (e.g., configuration file)
            String secretKey = getSecretKey();

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            hmacSHA256.init(secretKeySpec);

            byte[] combined = Base64.getDecoder().decode(data);
            byte[] receivedHmac = Arrays.copyOfRange(combined, 0, hmacSHA256.getMacLength());
            byte[] messageBytes = Arrays.copyOfRange(combined, hmacSHA256.getMacLength(), combined.length);

            byte[] calculatedHmac = hmacSHA256.doFinal(messageBytes);

            if (Arrays.equals(calculatedHmac, receivedHmac)) {
                return new String(messageBytes);
            } else {
                return "Invalid MAC: Message integrity compromised.";
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "Error DURING DECODING: " + e.getMessage();
        }
    }

    private static String getSecretKey() throws IOException {
        // Retrieve the secret key from a secure source, such as a configuration file
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream("/home/administrator/eclipse-installer/plugins/org.eclipse.justj.openjdk.hotspot.jre.minimal.stripped.linux.x86_64_17.0.10.v20240120-1143/jre/lib/security/cacerts");
        properties.load(input);
        return properties.getProperty("secretKey");
    }
    public static String decodeMessage1() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        try {
            FileInputStream fis = new FileInputStream("mactest");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            String data = (String) o;
            System.out.println("Received message: " + data);

            // Retrieve the secret key from a secure source (e.g., configuration file)
            String secretKey = getSecretKey();

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            hmacSHA256.init(secretKeySpec);

            byte[] combined = Base64.getDecoder().decode(data);
            byte[] receivedHmac = Arrays.copyOfRange(combined, 0, hmacSHA256.getMacLength());
            byte[] messageBytes = Arrays.copyOfRange(combined, hmacSHA256.getMacLength(), combined.length);

            byte[] calculatedHmac = hmacSHA256.doFinal(messageBytes);

            if (Arrays.equals(calculatedHmac, receivedHmac)) {
                String decodedMessage = new String(messageBytes, "UTF-8");
                return decodedMessage;
            } else {
                return "Invalid MAC: Message integrity compromised.";
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "Error DURING DECODING: " + e.getMessage();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        String decodedMessage = decodeMessage();
        System.out.println("Decoded message: " + decodedMessage);
    }
}
