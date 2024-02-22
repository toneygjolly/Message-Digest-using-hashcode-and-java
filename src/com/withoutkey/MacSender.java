package com.withoutkey;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacSender {
    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        encodeMessage();
    }

    public static void encodeMessage() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the message");
        String message = sc.nextLine();

        // Ask for the secret key and save it to a file
        System.out.println("Enter the security key");
        String secretKey = sc.nextLine();
        saveSecretKeyToFile(secretKey);

        // Use the secret key to create the HMAC
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac hmacsha256 = Mac.getInstance("HmacSHA256");
        hmacsha256.init(secretKeySpec);
        byte[] hmac = hmacsha256.doFinal(message.getBytes());

        // Combine the message and HMAC and encode to Base64
        byte[] combined = new byte[message.getBytes().length + hmac.length];
        System.arraycopy(message.getBytes(), 0, combined, 0, message.getBytes().length);
        System.arraycopy(hmac, 0, combined, message.getBytes().length, hmac.length);
        String macResult = Base64.getEncoder().encodeToString(combined);

        // Save the result to a file
        FileOutputStream fos = new FileOutputStream("mactest");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(macResult);

        System.out.println("MAC result: " + macResult);
    }

    private static void saveSecretKeyToFile(String secretKey) throws IOException {
        // Save the secret key to a file
        try (FileOutputStream keyFos = new FileOutputStream("secretkey.txt")) {
            keyFos.write(secretKey.getBytes());
        }
    }
}
