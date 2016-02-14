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
import com.geogenie.geo.service.exception.RestErrorCodes;
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


}
