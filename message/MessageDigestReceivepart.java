package com.message;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;

public class MessageDigestReceivepart {
public static void main(String args[]) throws Exception {
	FileInputStream fis =new FileInputStream("test");
	ObjectInputStream ois =new ObjectInputStream(fis);
	Object o =ois.readObject();
	String data =(String)o;
	System.out.println("got message"+ data);
	o =ois.readObject();
	byte origDigest[] =(byte[])o;
	MessageDigest md =MessageDigest.getInstance("SHA");
	md.update(data.getBytes());
	if(MessageDigest.isEqual(md.digest(), origDigest))
		System.out.println("Messsgae is valid");
	else {
		System.out.println("not valid");
	}
}
}
