package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.geogenie.Constants;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserFriend;
import com.geogenie.data.model.UserTypeBasedOnDevice;

public interface UserService {

	public User getUser(long id);
	
	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public List<User> getAllUsers();
	
	public User signupOrSignin(User user,UserTypeBasedOnDevice userTypeBasedOnDevice) ;
	
	public List<EventTag> getUserTagPreferences(Long id);
	
	public List<EventTag> saveUserTagPreferences(Long id,List<EventTag> tags);
	
	public SmartDevice getSmartDeviceDetails(String uniqueId) ;
	
	public List<Role> getUserRolesByDevice(String deviceId) ;
	
	public User loadUserByUsername(String username) ;
	
	public List<UserFriend> setupUserFriendsForNewUser(Long userId, String[] friendSocialIds);

}
