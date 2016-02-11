package com.geogenie.geo.service.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geogenie.Constants;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.User;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.UserFriend;
import com.geogenie.data.model.UserFriendsResponse;
import com.geogenie.data.model.UserRoleType;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.data.model.UserTypeBasedOnDevice;
import com.geogenie.geo.service.dao.EventTagDAO;
import com.geogenie.geo.service.dao.SmartDeviceDAO;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;
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
	
	@Autowired
	private SmartDeviceDAO smartDeviceDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User signupOrSignin(User user,UserTypeBasedOnDevice userTypeBasedOnDevice) throws ServiceException{
		logger.debug("### Inside signupOrSignin of UserServiceImpl ###");
		
		if(userTypeBasedOnDevice == UserTypeBasedOnDevice.MOBILE){
			return	handleMobileUser(user);
		}else{
			return 	handleWebUser(user);
		}
		/*Set<UserSocialDetail> socialDetails = user.getSocialDetails();
		User registeredUser = this.userDAO.saveUser(user);
		for(UserSocialDetail socialDetail : socialDetails){
			socialDetail.setUser(registeredUser);
			this.userDAO.saveUserSocialData(socialDetail);
		}
		registeredUser = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		return registeredUser;*/
	}
	
	private User handleMobileUser(User user) throws ServiceException{
		logger.info("Validating mobile user");
		LoginUtil.validateMobileUser(user);
		logger.info("Mobile user validation success.\n Checking if user exists or not?");
		
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		if(userInDB == null){
			logger.info("Mobile user does not exist.Registering user :"+user.getEmailId());
			
			Date now = new Date();
			user.setCreateDt(now);
			Set<SmartDevice> userDevices = new HashSet<>(user.getSmartDevices());
			user.setSmartDevices(null);
			
			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.APP_USER.getRoleType());
			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			User createdUser =  this.userDAO.createNewMobileUser(user);
			
			String privateKeyForDevice = UUID.randomUUID().toString();
			
			SmartDevice newDevice = null;
			for(SmartDevice smartDevice : userDevices){
				newDevice = smartDevice;
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setCreateDt(now);
				newDevice.setUser(createdUser);
				
				break;
			}
			createdUser =  this.userDAO.setupFirstDeviceForUser(createdUser, newDevice);
			Long id = createdUser.getId();
			for(UserSocialDetail socialDetail : user.getSocialDetails()){
				socialDetail.setUser(createdUser);
				this.userDAO.saveUserSocialData(socialDetail);
			}
			List<EventTag> allTags = this.eventTagDAO.getAll();
			this.eventTagDAO.saveUserTagPreferences(allTags, id);
			
			return createdUser;
		}else{
			logger.info("Mobile user exists. Checking if case of new device or login case.");
			Set<SmartDevice> existingDevices = userInDB.getSmartDevices();
			String deviceIdInRequest = null;
			for(SmartDevice smartDevice : user.getSmartDevices()){
				deviceIdInRequest = smartDevice.getUniqueId();
				break;
			}
			boolean newDeviceCase = true;
			boolean devicesExistForUser = false;
			//Case when web user is trying to setup phone. There will be no devices existing for him.
			if(existingDevices!=null && !existingDevices.isEmpty()){
				devicesExistForUser = true;
				for(SmartDevice smartDevice : existingDevices){
					if(deviceIdInRequest.equals(smartDevice.getUniqueId())){
						newDeviceCase = false;
						break;
					}
				}
			}
			
			if(newDeviceCase && !devicesExistForUser){
				logger.info("First time mobile setup for user : {}",user.getEmailId());
				Date now = new Date();
				String privateKeyForDevice = UUID.randomUUID().toString();
				SmartDevice newDevice = null;
				for(SmartDevice smartDevice : user.getSmartDevices()){
					newDevice = smartDevice;
					break;
				}
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setUser(userInDB);
				newDevice.setCreateDt(now);
				user.getSmartDevices().add(newDevice);
				return this.userDAO.setupFirstDeviceForUser(userInDB, newDevice);
				
				
			}else if(newDeviceCase && devicesExistForUser){
				logger.info("New Device setup for user having existing devices {} ",user.getEmailId());
				Date now = new Date();
				String privateKeyForDevice = UUID.randomUUID().toString();
				
				SmartDevice newDevice = null;
				for(SmartDevice smartDevice : user.getSmartDevices()){
					newDevice = smartDevice;
					break;
				}
				newDevice.setPrivateKey(privateKeyForDevice);
				newDevice.setCreateDt(now);
				newDevice.setUser(userInDB);
				User userWithAllDevices = this.userDAO.addDeviceToExistingUserDevices(userInDB, newDevice);
				User userObjectToReturn = null;
				try{
					logger.info("Cloning user object to return only newly added device");
					userObjectToReturn = (User) userWithAllDevices.clone();
				}catch(CloneNotSupportedException cloneNotSupportedException){
					logger.error("Error while cloning user object",cloneNotSupportedException);
					//TODO:Add custom clone code here
					userObjectToReturn = userWithAllDevices;
				}
				Set<SmartDevice> newDevices = new HashSet<>(1);
				newDevices.add(newDevice);
				userObjectToReturn.setSmartDevices(newDevices);
				return userObjectToReturn;
			}else{
				logger.info("No new device added. Simple User login case");
				User userObjectToReturn = null;
				try{
					logger.info("Cloning user object to return only newly added device");
					userObjectToReturn = (User) userInDB.clone();
				}catch(CloneNotSupportedException cloneNotSupportedException){
					logger.error("Error while cloning user object",cloneNotSupportedException);
					//TODO:Add custom clone code here
					userObjectToReturn = userInDB;
				}
				Set<SmartDevice> newDevices = new HashSet<>(1);
				userObjectToReturn.setSmartDevices(newDevices);
				return userObjectToReturn;
			}
			
		}
		
	}

	private User handleWebUser(User user) throws ServiceException{
		logger.info("Validating Web User");
		LoginUtil.validateWebUser(user);
		logger.info("User validation successful. \n Checking if user existing or not.");
		User userInDB = this.userDAO.getUserByEmailId(user.getEmailId(), false);
		if(userInDB==null){
			logger.info("User does not exist. Signup Case");
			Date now = new Date();
			user.setCreateDt(now);
			Set<Role> userRoles = new HashSet<>();
			Role appUserRole = this.userDAO.getRoleType(UserRoleType.APP_USER.getRoleType());
			userRoles.add(appUserRole);
			user.setUserroles(userRoles);
			User createdUser = this.userDAO.createNewWebUser(user);
			for(UserSocialDetail socialDetail : user.getSocialDetails()){
				socialDetail.setUser(createdUser);
				this.userDAO.saveUserSocialData(socialDetail);
			}
			List<EventTag> allTags = this.eventTagDAO.getAll();
			this.eventTagDAO.saveUserTagPreferences(allTags, createdUser.getId());
			return createdUser;
		}else{
			logger.info("User exists.Returning user details");
			User userToReturn= null;
			try {
				userToReturn = (User) userInDB.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("Error while cloning user object",e);
				//TODO:Add custom clone code here
				userToReturn = userInDB;
			}
			userToReturn.setSmartDevices(new HashSet<SmartDevice>());
			return userToReturn;
		}
		
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
	public User loadUserByUsername(String username)
			throws ServiceException {

		logger.info("### Inside loadUserByUsername. Username :{}  ###",
				username);
		
		User user = this.userDAO.getUserByEmailIdWithRoles(username, false);
		return user;
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
	
	
	@Override
	public SmartDevice getSmartDeviceDetails(String uniqueId) throws ServiceException{
		logger.info("### Get SmartDevice Details ###");
		SmartDevice smartDevice = this.smartDeviceDAO.getSmartDeviceByDeviceId(uniqueId);
		if(smartDevice==null){
			throw new ServiceException(ServiceErrorCodes.ERR_001,"Device does not exist or is not enabled");
		}
		return smartDevice;
	}
	
	@Override
	public List<Role> getUserRolesByDevice(String deviceId)
			throws ServiceException {
		logger.info("### Get getUserRolesByDevice  ###");
		List<Role> userRoles = this.smartDeviceDAO.getUserRolesByDevice(deviceId);
		if(userRoles==null){
			throw new ServiceException(ServiceErrorCodes.ERR_001,"User unauthorized");
		}
		
		return userRoles;
	}
	
	@Override
	public List<UserFriend> setupUserFriendsForNewUser(Long userId,
			String[] friendSocialIds) throws ServiceException{
		logger.info("### Inside setupUserFriendsForNewUser  ###");
		User user = this.userDAO.getUserById(userId);
		
		if(user==null){
			logger.error("User does not exist for id "+userId);
			throw new ServiceException(ServiceErrorCodes.ERR_001,"User does not exist for id "+userId);
		}
		
		List<User> friendsInSystem = this.userDAO.setupFriendsUsingExternalIds(user,friendSocialIds);
		if(friendsInSystem==null){
			friendsInSystem = new ArrayList<User>();
		}
		
		Transformer<List<UserFriend>, List<User>> transformer = (Transformer<List<UserFriend>, List<User>>)TransformerFactory.getTransformer(Transformer_Types.USER_TO_FRIEND_TRANSFORMER);
		List<UserFriend> userFriends = transformer.transform(friendsInSystem);
		return userFriends;
	}
}
