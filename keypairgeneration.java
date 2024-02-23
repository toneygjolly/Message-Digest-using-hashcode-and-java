package com.keypair;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class keypairgeneration {
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPair keyPair = generKeyPair();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the data");
        String originalData =sc.nextLine();
        //String originalData = "Hello, this is a message to be encrypted!";
        String encryptedData = encrypt(originalData, keyPair.getPublic());
        System.out.println("Encrypted Data: " + encryptedData);
        System.out.println("----------------------------------------");

        String decryptedData = descrypt(encryptedData, keyPair.getPrivate());
        System.out.println("Decrypted Data: " + decryptedData);
    }
	private static  KeyPair generKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.
				getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.generateKeyPair();
	}
	private static String encrypt(String data, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes =cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
		
	}
	private static String descrypt(String encrypteddata, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder()
				.decode(encrypteddata));
		return new String(decryptedBytes);
	}

}
