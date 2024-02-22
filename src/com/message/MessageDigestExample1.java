package com.message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MessageDigestExample1 {
	public static void main(String args[]) throws Exception {
		Scanner sc =new Scanner(System.in);
	    System.out.println("Enter the message");
	    String message =sc.nextLine();
	    MessageDigest md =MessageDigest.getInstance("SHA-256");
	    md.update(message.getBytes());
	    byte[] digest =md.digest();
	    System.out.println(digest);
	    System.out.println(message);
		
	}

}
