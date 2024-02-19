package com.mac;

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
	FileInputStream fis =new FileInputStream("mactest");
	ObjectInputStream ois =new ObjectInputStream(fis);
	Object o = ois.readObject();
	String data =(String) o;
	System.out.println("got message" +data);
	System.out.println("Enter the secret key");
	Scanner sc=new Scanner(System.in);
	String secretkey =sc.nextLine();
	SecretKeySpec secretkeySpec =new SecretKeySpec(secretkey.getBytes(), "HmacSHA256");
	Mac hmacsha256 = Mac.getInstance("HmacSHA256");
	hmacsha256.init(secretkeySpec);
	byte[] combined = Base64.getDecoder().decode(data);
	byte[] messageBytes = new byte[combined.length - hmacsha256.getMacLength()];
	byte[] receivedHmac =new byte[hmacsha256.getMacLength()];
	System.arraycopy(combined, 0,messageBytes, 0, messageBytes.length);
	System.arraycopy(combined, messageBytes.length, receivedHmac, 0, receivedHmac.length);
	byte[] calculatedHmac =hmacsha256.doFinal(messageBytes);
	if(Arrays.equals(calculatedHmac, receivedHmac)) {
		return new String(messageBytes);
		
	}
	else {
		return "Invalid MAC:Messaage integrity compromised.";
		
	}
	}
	catch (InvalidKeyException e) {
		// TODO: handle exception
	e.printStackTrace();
	return "Error DURING DECODING: "+e.getMessage();
}
}
public static void main(String[]args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
	String decodedMessage;
	decodedMessage =decodeMessage();
	System.out.println("Decoded message:"+decodedMessage);
}

}
