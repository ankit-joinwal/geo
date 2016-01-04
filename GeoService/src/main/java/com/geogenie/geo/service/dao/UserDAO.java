package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.User;

public interface UserDAO {

	public List<User> getAllUsers();
	
	public User registerUser(User user);
	
	public User getUserById(Long id);
	
	public User getUserByEmailId(String emailId,boolean updateQuota);
	
	
}
