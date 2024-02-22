package com.withoutkey;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacReceiverside {
    public static String decodeMessage() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        try {
            // Read the encoded message from the file
            FileInputStream fis = new FileInputStream("mactest");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            String data = (String) o;
            System.out.println("Received message: " + data);

            // Retrieve the secret key from the file
            String secretkey = getSecretKeyFromFile();

            SecretKeySpec secretkeySpec = new SecretKeySpec(secretkey.getBytes(), "HmacSHA256");
            Mac hmacsha256 = Mac.getInstance("HmacSHA256");
            hmacsha256.init(secretkeySpec);

            // Decode the Base64 encoded string
            byte[] combined = Base64.getDecoder().decode(data);
            byte[] messageBytes = Arrays.copyOfRange(combined, 0, combined.length - hmacsha256.getMacLength());
            byte[] receivedHmac = Arrays.copyOfRange(combined, messageBytes.length, combined.length);

            // Verify the HMAC
            byte[] calculatedHmac = hmacsha256.doFinal(messageBytes);

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

    private static String getSecretKeyFromFile() throws IOException {
        // Read the secret key from the file
        try (Scanner scanner = new Scanner(new FileInputStream("secretkey.txt"))) {
            return scanner.nextLine();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        String decodedMessage = decodeMessage();
        System.out.println("Decoded message: " + decodedMessage);
    }
}
