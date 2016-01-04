package com.geogenie.geo.service.business;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.geogenie.data.model.GeoServiceConfig;
import com.geogenie.data.model.User;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.utils.LoginUtil;

@Service
@Transactional
public class ValidationServiceImpl implements ValidationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValidationServiceImpl.class);

	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private GeoServiceConfig geoServiceConfig;

	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public GeoServiceConfig getGeoServiceConfig() {
		return geoServiceConfig;
	}

	public void setGeoServiceConfig(GeoServiceConfig geoServiceConfig) {
		this.geoServiceConfig = geoServiceConfig;
	}


	@Override
	public Object validateReuqest(String authorizationHeader) {
		LOGGER.info("### Inside ValidationServiceImpl.validateReuqest ###");

		// Extract details from header
		String encodedString = authorizationHeader.replaceFirst("Basic ", "");
		byte[] decodedBytes = Base64.decode(encodedString.getBytes());

		String usernameAndPassword = null;
		try {
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			String message = "Error while decoding authentication header";
			LOGGER.error(message, e);
			return new ServiceException(ServiceErrorCodes.ERR_001, message);
		}
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		String[] nameArr = LoginUtil.getUsernameParts(username);
		if (nameArr == null) {
			String message = "Username not in proper format : " + username;
			LOGGER.error(message);
			return new ServiceException(ServiceErrorCodes.ERR_001, message);
		}
		User user = null;
		try {
			user = LoginUtil.validateUserName(this.userDAO, nameArr, username,
					true);
		} catch (ServiceException e) {
			return e;
		}
		if (user.getDailyQuota() < 0) {
			String message = "User quota exceeded";
			return new ServiceException(ServiceErrorCodes.ERR_002, message);
		}

		return user;
	}
}
