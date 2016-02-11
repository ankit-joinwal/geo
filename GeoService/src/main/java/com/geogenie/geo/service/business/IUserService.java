package com.geogenie.geo.service.business;

import java.util.List;

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserFriend;
import com.geogenie.data.model.UserTypeBasedOnDevice;
import com.geogenie.geo.service.exception.ServiceException;

public interface IUserService {

	public User getUser(long id);
	
	public List<User> getAllUsers();
	
	public User signupOrSignin(User user,UserTypeBasedOnDevice userTypeBasedOnDevice) throws ServiceException;
	
	public List<EventTag> getUserTagPreferences(Long id);
	
	public List<EventTag> saveUserTagPreferences(Long id,List<EventTag> tags);
	
	public SmartDevice getSmartDeviceDetails(String uniqueId) throws ServiceException;
	
	public List<Role> getUserRolesByDevice(String deviceId) throws ServiceException;
	
	public User loadUserByUsername(String username) throws ServiceException ;
	
	public List<UserFriend> setupUserFriendsForNewUser(Long userId, String[] friendSocialIds) throws ServiceException;

}
