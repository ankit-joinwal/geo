package com.geogenie.geo.service.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.UserSocialDetail;

public interface UserDAO {

	public User createNewMobileUser(User userToCreate);
	
	public User setupFirstDeviceForUser(User user, SmartDevice smartDevice);
	
	public User addDeviceToExistingUserDevices(User user, SmartDevice smartDevice);
	
	public User createNewWebUser(User userToCreate);
	
	public List<User> getAllUsers();
	
	public User getUserById(Long id);
	
	public User getUserByEmailId(String emailId,boolean updateQuota);
	
	public User getUserByEmailIdWithRoles(String emailId,boolean updateQuota);
	
	public void saveUserSocialData(UserSocialDetail userSocialDetails);
	
	public Map<String,UserSocialDetail> getSocialDetails(Set<String> socialIds);
	
	public UserSocialDetail getSocialDetail(String socialId);
	
	public MeetupAttendee getAttendeeByMeetupIdAndSocialId(String meetupId, Long socialId);
	
	public Role getRoleType(String roleName);
	
	public List<User> setupFriendsUsingExternalIds(User user,String[] externalIds);
}
