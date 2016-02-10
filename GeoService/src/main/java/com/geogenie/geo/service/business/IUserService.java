package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserTypeBasedOnDevice;
import com.geogenie.geo.service.exception.ServiceException;

public interface IUserService extends UserDetailsService{

	public User getUser(long id);
	
	public List<User> getAllUsers();
	
	public User signupOrSignin(User user,UserTypeBasedOnDevice userTypeBasedOnDevice) throws ServiceException;
	
	public List<EventTag> getUserTagPreferences(Long id);
	
	public List<EventTag> saveUserTagPreferences(Long id,List<EventTag> tags);
}
