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
