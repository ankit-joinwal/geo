package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.geogenie.data.model.User;

public interface IUserService extends UserDetailsService{

	public User getUser(long id);
	
	public List<User> getAllUsers();
	
	public User registerUser(User user);
	
	
}
