package com.geogenie.geo.service.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.geo.service.dao.EventTagDAO;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.utils.LoginUtil;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventTagDAO eventTagDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User registerUser(User user) {
		logger.debug("### Inside registerUser of UserServiceImpl ###");
		Set<UserSocialDetail> socialDetails = user.getSocialDetails();
		User registeredUser = this.userDAO.saveUser(user);
		for(UserSocialDetail socialDetail : socialDetails){
			socialDetail.setUser(registeredUser);
			this.userDAO.saveUserSocialData(socialDetail);
		}
		registeredUser = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		return registeredUser;
	}

	@Override
	public User getUser(long id) {
		return this.userDAO.getUserById(id);
	}

	@Override
	public List<User> getAllUsers() {
		return this.userDAO.getAllUsers();
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		logger.info("### Inside loadUserByUsername. Username :{}  ###",
				username);
		String[] nameArr = LoginUtil.getUsernameParts(username);
		if (nameArr == null) {
			String message = "Username not in proper format : " + username;
			logger.error(message);

			throw new UsernameNotFoundException(message);
		}

		User user = null;
		try {
			user = LoginUtil.validateUserName(this.userDAO, nameArr, username,false);
		} catch (ServiceException se) {
			throw new UsernameNotFoundException(se.getMessage());
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		logger.info("Found user in database: " + user);

		return new org.springframework.security.core.userdetails.User(username,
				user.getPassword(), authorities);
	}

	@Override
	public List<EventTag> getUserTagPreferences(Long id) {
		logger.info("### Getting user tag preferences ###");
		List<EventTag> userTags = this.eventTagDAO.getUserTags(id);
		//If no preferences exist for user, populate entire tags for user
		if(userTags!=null && !userTags.isEmpty()){
			return userTags;
		}else{
			logger.info("No Tag Preferences Exist for user. Saving all tags as preferences");
			List<EventTag> allTags = this.eventTagDAO.getAll();
			this.eventTagDAO.saveUserTagPreferences(allTags, id);
			logger.info("Tag preferences saved");
			return allTags;
		}
	}

	
	@Override
	public List<EventTag> saveUserTagPreferences(Long id, List<EventTag> tags) {
		logger.info("### Save user tag preferences ###");
		List<String> tagNames = new ArrayList<>();
		for(EventTag tag: tags){
			tagNames.add(tag.getName());
		}
		List<EventTag> tagsInDB = this.eventTagDAO.getTagsByNames(tagNames);
		return this.eventTagDAO.saveUserTagPreferences(tagsInDB, id);
	}
}
