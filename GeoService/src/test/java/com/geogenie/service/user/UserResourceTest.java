package com.geogenie.service.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.geogenie.data.model.SocialDetailType;
import com.geogenie.data.model.SocialSystem;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.service.config.MockAppContext;
import com.geogenie.service.config.MockServletContextConfig;
import com.geogenie.service.util.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MockServletContextConfig.class,
		MockAppContext.class})
@WebAppConfiguration
public class UserResourceTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	@Test
	public void testAddNewUserSuccess() throws Exception{
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
		
		  mockMvc.perform(post("/api/public/users")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(TestUtil.convertObjectToJsonBytes(user))
	        ).andExpect(status().isCreated());
		
	}
}
