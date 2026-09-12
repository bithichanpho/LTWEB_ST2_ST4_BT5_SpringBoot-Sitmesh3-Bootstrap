package com.webprogramming.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {
	public static String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}
 
	public static boolean checkPassword(String rawPassword, String hashedPassword) {
		if (rawPassword == null || hashedPassword == null) {
			return false;
		}
		return hashPassword(rawPassword).equals(hashedPassword);
	}
}
