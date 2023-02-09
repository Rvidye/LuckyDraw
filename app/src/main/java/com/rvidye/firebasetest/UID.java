package com.rvidye.firebasetest;

import java.util.Random;

public class UID {
	public static char[] UID(int length)
	{
		String numbers = "0123456789";
		Random rndm_method = new Random();

		char[] otp = new char[length];

		for(int i = 0; i < length; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}
}
