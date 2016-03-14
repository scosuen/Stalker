package com.scott.stalker.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String encryption(String str) throws NoSuchAlgorithmException {

		MessageDigest md5 = MessageDigest.getInstance("md5");
		byte[] cipherData = md5.digest(str.getBytes());
		StringBuilder builder = new StringBuilder();
		for (byte cipher : cipherData) {
			String toHexStr = Integer.toHexString(cipher & 0xff);
			builder.append(toHexStr.length() == 1 ? "0" + toHexStr : toHexStr);
		}
		
		return builder.toString();
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(encryption("111"));
			System.out.println(new BigDecimal("1"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
