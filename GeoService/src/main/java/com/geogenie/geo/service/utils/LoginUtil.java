package com.geogenie.geo.service.utils;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geogenie.Constants;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

public class LoginUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUtil.class);
	
	public static String[] getUsernameParts(String username){
		if(username==null)
			return null;
		
		String[] nameArr = username.split(Constants.UNAME_DELIM);
		return nameArr;
	}
	
	public static User validateUserName(UserDAO userDAO,String[] nameArr,String username,boolean updateUserQuota) throws ServiceException{
		User user = null;
		String loginId = null;
		
		if (username.contains(Constants.DEVICE_PREFIX)) {
			LOGGER.info("Request is from mobile device");
			String prefix = nameArr[0];
			String uniqueId = nameArr[1];
			loginId = nameArr[2];
			LOGGER.info("Parts [ 0 : {} , 1 : {} , 2 : {} ]",prefix,uniqueId,loginId);
			
			user = userDAO.getUserByEmailId(loginId,updateUserQuota);
			if (user == null) {
				String message = "User not found" + username;
				LOGGER.error(message);
				throw new ServiceException(ServiceErrorCodes.ERR_001,message);
			}
			Set<SmartDevice> devices = user.getSmartDevices();
			boolean deviceValid = false;
			for (SmartDevice device : devices) {
				if (uniqueId.equals(device.getUniqueId())) {
					deviceValid = true;
				}
			}

			if (!deviceValid) {
				String message = "Device is not registered " + username;
				LOGGER.error(message);

				throw new ServiceException(ServiceErrorCodes.ERR_001,message);
			}
		}else{
			loginId = nameArr[0];
			LOGGER.info("Request is from web browser");
			user = userDAO.getUserByEmailId(loginId,updateUserQuota);
			if (user == null) {
				String message = "User not found" + username;
				LOGGER.error(message);
				throw new ServiceException(ServiceErrorCodes.ERR_001,message);
			}
		}
		
		return user;
	}
	
	public static void validateMobileUser(User user) throws ServiceException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = "Social Details are null";
			LOGGER.error(msg);
			throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices()==null || user.getSmartDevices().isEmpty()){
			msg = "Device details not found for user";
			LOGGER.error(msg);
			throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices().size() > 1){
			msg =  "Invalid request. Cannot have more than one device details in one request";
			LOGGER.error(msg);
			throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
		}else{
			SmartDevice smartDevice = null;
			for(SmartDevice device : user.getSmartDevices()){
				smartDevice = device;
				break;
			}
			if(smartDevice==null){
				msg = "Smart Device is null in request";
				throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
			}
			if(smartDevice.getUniqueId()==null || smartDevice.getUniqueId().isEmpty()){
				msg = "Smart Device Unique Id is not present in request";
				throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
			}
		}
		
	}
	
	public static void validateWebUser(User user) throws ServiceException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = "Social Details are null";
			LOGGER.error(msg);
			throw new ServiceException(ServiceErrorCodes.ERR_001,msg);
		}
	}
	
}
