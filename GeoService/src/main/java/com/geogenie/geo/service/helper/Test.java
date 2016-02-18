package com.geogenie.geo.service.helper;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geogenie.data.model.DeviceType;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.SocialDetailType;
import com.geogenie.data.model.SocialSystem;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.data.model.UserTypeBasedOnDevice;

public class Test {
	public static void main(String[] args)throws Exception {
		Long timeStamp = System.currentTimeMillis();
		System.out.println(timeStamp);
		String sign = calculateSignature("9129a43b-a9de-416d-b3b8-b0f5d6b82792", timeStamp);
		System.out.println(sign);
		
	}
	
	private static String calculateSignature(String secret, Long timeStamp) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),
					"HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			String timeStampStr = timeStamp + "";
			byte[] rawHmac = mac.doFinal(timeStampStr.getBytes());
			String result = new String(Base64.encode(rawHmac));
			return result;
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException();
		}
	}
}
