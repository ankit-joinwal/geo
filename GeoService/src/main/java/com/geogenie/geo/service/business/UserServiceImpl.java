package com.geogenie.geo.service.business;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geogenie.data.model.User;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.utils.LoginUtil;

@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User registerUser(User user) {
		logger.debug("### Inside registerUser of UserServiceImpl ###");

		return this.userDAO.registerUser(user);
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

	

}
