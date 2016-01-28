package com.geogenie.geo.service.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;

public interface UserDAO {

	public List<User> getAllUsers();
	
	public User saveUser(User user);
	
	public User getUserById(Long id);
	
	public User getUserByEmailId(String emailId,boolean updateQuota);
	
	public void saveUserSocialData(UserSocialDetail userSocialDetails);
	
	public Map<String,UserSocialDetail> getSocialDetails(Set<String> socialIds);
	
	public UserSocialDetail getSocialDetail(String socialId);
	
	public MeetupAttendee getAttendeeByMeetupIdAndSocialId(String meetupId, Long socialId);
	
	
}
