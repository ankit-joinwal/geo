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

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.MeetupAttendee;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
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
		return getUserByEmailId(user.getEmailId(), false);
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
}
