package com.mac;

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
	Scanner sc=new Scanner(System.in);
	System.out.println("Enter the message");
	String message= sc.nextLine();
	System.out.println("Enter the seurity key");
	String secretkey =sc.nextLine();
	SecretKeySpec secretkeySpec =new SecretKeySpec(secretkey.getBytes(), "HmacSHA26");
	Mac hmacsha256 =Mac.getInstance("HmacSHA256");
	hmacsha256.init(secretkeySpec);
	byte[] hmac = hmacsha256.doFinal(message.getBytes());
	byte[] combined =new byte[message.getBytes().length + hmac.length];
	System.arraycopy(message.getBytes(), 0, combined, 0, message.getBytes().length);
	System.arraycopy(hmac, 0, combined, message.getBytes().length, hmac.length);
	String macResult =Base64.getEncoder().encodeToString(combined);
	 FileOutputStream fos =new FileOutputStream("mactest");
	 ObjectOutputStream oos=new ObjectOutputStream(fos);
	 oos.writeObject(macResult);
	 System.out.println("mac result");
	 System.out.println(new String(macResult));
}
}
