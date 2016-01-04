package com.geogenie.geo.service.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.geo.service.utils.PasswordUtils;
import com.geogenie.geo.service.utils.UserSVCConstants;

@Repository("userDAO")
public class UserDAOImpl extends AbstractDAO implements UserDAO {
	private static final Logger logger = LoggerFactory
			.getLogger(UserDAOImpl.class);
	@Autowired
	private Environment environment;

	@Override
	public List<User> getAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class)
				.setFetchMode("smartDevices", FetchMode.JOIN);

		return (List<User>) criteria.list();
	}

	@Override
	public User registerUser(User user) {
		logger.info("### Checking if user exists already ###");
		Query query = getSession().getNamedQuery("getUserByEmail");
		query.setString("emailId", user.getEmailId());
		User userInDB = (User) query.uniqueResult();
		Date now = new Date();
		Set<SmartDevice> smartDevicesInRequest = user.getSmartDevices();
		SmartDevice smartDeviceInRequest = null;
		for (SmartDevice smartDevice : smartDevicesInRequest) {
			smartDeviceInRequest = smartDevice;
			smartDeviceInRequest.setCreateDt(now);
			break;
		}
		user.setCreateDt(now);
		if (userInDB != null) {
			logger.info("### User found.Registering only device. ###");

			userInDB.getSmartDevices().add(smartDeviceInRequest);
			merge(userInDB);
		} else {
			logger.info("### User not found.Do fresh registration. ###");
			try {
				user.setDailyQuota(Integer.parseInt(environment
						.getRequiredProperty(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA_PROPERTY)));
			} catch (NumberFormatException exception) {
				logger.error("Error occured while setting default daily quota",
						exception);

				user.setDailyQuota(UserSVCConstants.DEFAULT_USER_DAILY_QUOTA);
			}

			user.setPassword(PasswordUtils.encryptPass(user.getPassword()));
			saveOrUpdate(user);
		}

		return user;
	}

	@Override
	public User getUserById(Long id) {
		Criteria idCrt = getSession().createCriteria(User.class)
				.setFetchMode("smartDevices", FetchMode.JOIN)
				.add(Restrictions.eq("id", id))
				.add(Restrictions.eq("isEnabled", "true"));
				
		
		User user = (User) idCrt.uniqueResult();
		//logger.info("User Devices :: ["+user.getSmartDevices()+" ] ");
		return user;
	}

	@Override
	public User getUserByEmailId(String emailId,boolean updateQuota) {
		Criteria emailCrt = getSession().createCriteria(User.class)
				.add(Restrictions.eq("emailId", emailId))
				.add(Restrictions.eq("isEnabled", "true"))
				.setFetchMode("sd", FetchMode.JOIN);
		;
		User user = (User) emailCrt.uniqueResult();
		if(updateQuota){
			Integer quota = user.getDailyQuota();
			user.setDailyQuota(quota-1);
			//check if specific API to be called to saving
		}
		return user;
	}
	

}
