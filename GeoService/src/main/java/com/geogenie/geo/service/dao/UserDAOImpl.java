package com.geogenie.geo.service.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.PushNotificationSettingMaster;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.SocialDetailType;
import com.geogenie.data.model.SocialSystem;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSettingType;
import com.geogenie.data.model.UserSetting;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.geo.service.utils.PasswordUtils;
import com.geogenie.geo.service.utils.UserSVCConstants;

@Repository("userDAO")
public class UserDAOImpl extends AbstractDAO implements UserDAO {
	private static final Logger logger = LoggerFactory
			.getLogger(UserDAOImpl.class);
	@Autowired
	private Environment environment;
	
	@Autowired
	private EventTagDAO eventTagDAO;
	
	@Autowired
	private PushNotificationDAO pushNotificationDAO;

	@Override
	public User createNewMobileUser(User userToCreate) {
		try {
			userToCreate.setDailyQuota(Integer.parseInt(environment
					.getRequiredProperty(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA_PROPERTY)));
		} catch (NumberFormatException exception) {
			logger.error("Error occured while setting default daily quota",
					exception);

			userToCreate.setDailyQuota(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA);
		}
		userToCreate.setPassword(PasswordUtils.encryptPass(userToCreate.getPassword()));
		Long id = (Long) save(userToCreate);
		return getUserById(id);
	}
	
	@Override
	public User createNewWebUser(User userToCreate) {
		try {
			userToCreate.setDailyQuota(Integer.parseInt(environment
					.getRequiredProperty(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA_PROPERTY)));
		} catch (NumberFormatException exception) {
			logger.error("Error occured while setting default daily quota",
					exception);

			userToCreate.setDailyQuota(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA);
		}
		userToCreate.setPassword(PasswordUtils.encryptPass(userToCreate.getPassword()));
		Long id = (Long) save(userToCreate);
		return getUserById(id);
	}
	
	@Override
	public User setupFirstDeviceForUser(User user, SmartDevice smartDevice) {
		Set<SmartDevice> smartDevices = new HashSet<>(1);
		smartDevices.add(smartDevice);
		user.setSmartDevices(smartDevices);
		saveOrUpdate(user);
		
		
		List<PushNotificationSettingMaster> notificationSettingMasters = this.pushNotificationDAO.getPushNotTypes();
		Date now = new Date();
		for(PushNotificationSettingMaster notificationSettingMaster : notificationSettingMasters){
			UserSetting settings = new UserSetting();
			settings.setSettingType(UserSettingType.PUSH_NOTIFICATION.getSettingType());
			settings.setName(notificationSettingMaster.getName());
			settings.setUser(user);
			settings.setDisplayName(notificationSettingMaster.getDisplayName());
			settings.setValue("ON");
			settings.setCreateDt(now);
			save(settings);
		}
		
		return getUserById(user.getId());
	}
	
	@Override
	public User addDeviceToExistingUserDevices(User user,
			SmartDevice smartDevice) {
		Set<SmartDevice> smartDevices = user.getSmartDevices();
		smartDevices.add(smartDevice);
		merge(user);
		
		return getUserById(user.getId());
	}
	
	
	@Override
	public List<User> getAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class)
				.setFetchMode("smartDevices", FetchMode.JOIN);

		return (List<User>) criteria.list();
	}

	@Override
	public void saveUserSocialData(UserSocialDetail socialDetails) {
		Criteria criteria = getSession().createCriteria(UserSocialDetail.class).add(Restrictions.eq("socialSystem", socialDetails.getSocialSystem()))
				.add(Restrictions.eq("userSocialDetail", socialDetails.getUserSocialDetail()))
				.add(Restrictions.eq("socialDetailType", socialDetails.getSocialDetailType()));
		
		UserSocialDetail existingDetail = (UserSocialDetail) criteria.uniqueResult();
		if(existingDetail!= null){
			socialDetails.setId(existingDetail.getId());
			merge(socialDetails);
		}else{
			saveOrUpdate(socialDetails);
		}
	}

	@Override
	public User getUserById(Long id) {
		Criteria idCrt = getSession().createCriteria(User.class)
				.setFetchMode("smartDevices", FetchMode.JOIN)
				.add(Restrictions.eq("id", id))
				.add(Restrictions.eq("isEnabled", "true"));

		User user = (User) idCrt.uniqueResult();
		// logger.info("User Devices :: ["+user.getSmartDevices()+" ] ");
		return user;
	}
	
	

	@Override
	public User getUserByEmailId(String emailId, boolean updateQuota) {
		Criteria emailCrt = getSession().createCriteria(User.class)
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("isEnabled", "true"))
				.setFetchMode("smartDevices", FetchMode.JOIN);
		;
		User user = (User) emailCrt.uniqueResult();
		if (updateQuota) {
			Integer quota = user.getDailyQuota();
			user.setDailyQuota(quota - 1);
			// check if specific API to be called to saving
		}
		return user;
	}

	
	@Override
	public User getUserByEmailIdWithRoles(String emailId, boolean updateQuota) {
		Criteria emailCrt = getSession().createCriteria(User.class)
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("isEnabled", "true"))
				.setFetchMode("smartDevices", FetchMode.JOIN)
				.setFetchMode("userroles", FetchMode.JOIN);
		;
		User user = (User) emailCrt.uniqueResult();
		
		if(user!=null && user.getUserroles()!=null){
			user.getUserroles().size();
		}
		if (updateQuota) {
			Integer quota = user.getDailyQuota();
			user.setDailyQuota(quota - 1);
			// check if specific API to be called to saving
		}
		return user;
	}
	
	@Override
	public Map<String, UserSocialDetail> getSocialDetails(Set<String> socialIds) {
		Map<String, UserSocialDetail> socialIdVsDetailsMap = new HashMap<>();
		Criteria criteria = getSession().createCriteria(UserSocialDetail.class)
				.add(Restrictions.in("userSocialDetail", socialIds));
		List<UserSocialDetail> userSocialDetails = (List<UserSocialDetail>) criteria
				.list();
		if (userSocialDetails != null) {
			for (UserSocialDetail detail : userSocialDetails) {
				socialIdVsDetailsMap.put(detail.getUserSocialDetail(), detail);
			}
		}
		return socialIdVsDetailsMap;
	}
	
	@Override
	public UserSocialDetail getSocialDetail(String socialId) {
		Criteria criteria = getSession().createCriteria(UserSocialDetail.class)
				.add(Restrictions.eq("userSocialDetail", socialId));
		
		UserSocialDetail userSocialDetail = (UserSocialDetail) criteria.uniqueResult();
		return userSocialDetail;
	}
	
	@Override
	public MeetupAttendee getAttendeeByMeetupIdAndSocialId(String meetupId,
			Long socialId) {
		Criteria criteria = getSession().createCriteria(MeetupAttendee.class).createAlias("socialDetail", "sdtl").createAlias("meetup", "meet")
				.setFetchMode("sdtl", FetchMode.JOIN).setFetchMode("meet", FetchMode.JOIN).add(Restrictions.eq("sdtl.id", socialId))
				.add(Restrictions.eq("meet.uuid", meetupId));
		MeetupAttendee meetupAttendee = (MeetupAttendee) criteria.uniqueResult();
		
		return meetupAttendee;
	}
	
	@Override
	public Role getRoleType(String roleName) {
		Criteria criteria = getSession().createCriteria(Role.class).add(Restrictions.eq("userRoleType", roleName));
		
		return (Role)criteria.uniqueResult();
	}
	
	@Override
	public List<User> setupFriendsUsingExternalIds(User user ,String[] externalIds) {
		
		Criteria criteria = getSession().createCriteria(User.class).setFetchMode("friends", FetchMode.JOIN)
				.createAlias("socialDetails", "socialDetail")
				.add(Restrictions.in("socialDetail.userSocialDetail", externalIds))
				.add(Restrictions.eq("socialDetail.socialSystem", SocialSystem.FACEBOOK))
				.add(Restrictions.eq("socialDetail.socialDetailType", SocialDetailType.USER_EXTERNAL_ID));
		List<User> friendsInSystem = criteria.list();
		if(friendsInSystem!=null){
			//Save friends with user
			user.setFriends(new HashSet<>(friendsInSystem));
			//Now save this user into friends
			for(User friend : friendsInSystem){
				friend.getFriends().add(user);
			}
		}else{
			friendsInSystem = new ArrayList<User>(1);
		}
		
		return friendsInSystem;
	}
	
	@Override
	public List<UserSetting> getUserSettings(User user) {
		Criteria criteria = getSession().createCriteria(UserSetting.class,"setting")
				.createAlias("setting.user", "user")
				.setFetchMode("user", FetchMode.JOIN)
				.add(Restrictions.eq("user.id", user.getId()));
		return criteria.list();
	}
	
	
	@Override
	public void saveUserSettings(List<UserSetting> oldSettings,List<UserSetting> newSettings) {
		for(UserSetting userSetting :oldSettings){
			getSession().delete(userSetting);
		}
		for(UserSetting userSetting :newSettings){
			getSession().save(userSetting);
		}
		
	}
}
