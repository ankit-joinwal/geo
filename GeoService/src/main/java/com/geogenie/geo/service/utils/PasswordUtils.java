package com.geogenie.geo.service.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	public static String encryptPass(String password){
		return bCryptPasswordEncoder.encode(password);
	}
	
	
	
}
