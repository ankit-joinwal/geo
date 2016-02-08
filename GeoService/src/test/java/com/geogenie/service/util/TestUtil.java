package com.geogenie.service.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geogenie.data.model.SocialDetailType;
import com.geogenie.data.model.SocialSystem;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;


public class TestUtil {



    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("Object :"+mapper.writeValueAsString(object));
        return mapper.writeValueAsBytes(object);
    }

    
    public static void main(String[] args) throws Exception{
    	User user = new User();
		user.setEmailId("test-user@testdomain.com");
		user.setIsEnabled("true");
		user.setName("Test User");
		user.setPassword("password");
		
		UserSocialDetail detail = new UserSocialDetail();
		detail.setSocialDetailType(SocialDetailType.USER_EXTERNAL_ID);
		detail.setSocialSystem(SocialSystem.FACEBOOK);
		detail.setUserSocialDetail("1234");
		Set<UserSocialDetail> socialDetails = new HashSet<>();
		socialDetails.add(detail);
		
		user.setSocialDetails(socialDetails);
		
		convertObjectToJsonBytes(user);
	}
}
