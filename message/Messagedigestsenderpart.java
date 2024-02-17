package com.message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Messagedigestsenderpart {
	public static void main(String args[]) throws NoSuchAlgorithmException, IOException {
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the message");
		String data = sc.nextLine();
		
		FileOutputStream fos =new FileOutputStream("test");
		MessageDigest md =MessageDigest.getInstance("SHA");
		ObjectOutputStream oos =new ObjectOutputStream(fos);
		byte buf[] =data.getBytes();
		md.update(buf);
		oos.writeObject(data);
		oos.writeObject(md.digest());
		
	}

}
