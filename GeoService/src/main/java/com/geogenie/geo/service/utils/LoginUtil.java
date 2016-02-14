package com.geogenie.geo.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geogenie.Constants;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.geo.service.exception.ClientException;
import com.geogenie.geo.service.exception.RestErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

public class LoginUtil implements Constants{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginUtil.class);
	
	public static String[] getUsernameParts(String username){
		if(username==null)
			return null;
		
		String[] nameArr = username.split(UNAME_DELIM);
		return nameArr;
	}
	
	
	public static void validateMobileUser(User user) throws ClientException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = ERROR_LOGIN_SOCIAL_DETAILS_MISSING;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices()==null || user.getSmartDevices().isEmpty()){
			msg = ERROR_LOGIN_SOCIAL_DETAILS_MISSING;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
		
		if(user.getSmartDevices().size() > 1){
			msg =  ERROR_LOGIN_INVALID_DEVICES_IN_REQ;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}else{
			SmartDevice smartDevice = null;
			for(SmartDevice device : user.getSmartDevices()){
				smartDevice = device;
				break;
			}
			if(smartDevice==null){
				msg = ERROR_LOGIN_DEVICE_MISSING;
				throw new ClientException(RestErrorCodes.ERR_001,msg);
			}
			if(smartDevice.getUniqueId()==null || smartDevice.getUniqueId().isEmpty()){
				msg = ERROR_LOGIN_SD_ID_MISSING;
				throw new ClientException(RestErrorCodes.ERR_001,msg);
			}
		}
		
	}
	
	public static void validateWebUser(User user) throws ClientException{
		LOGGER.info("Validating user:"+user);
		String msg =  null;
		//check for mandatory values
		if(user.getSocialDetails()==null || user.getSocialDetails().isEmpty()){
			msg = ERROR_LOGIN_SOCIAL_DETAILS_MISSING;
			LOGGER.error(msg);
			throw new ClientException(RestErrorCodes.ERR_001,msg);
		}
	}
	
}
