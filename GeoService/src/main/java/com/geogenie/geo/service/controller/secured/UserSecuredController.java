package com.geogenie.geo.service.controller.secured;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.Constants;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserFriend;
import com.geogenie.data.model.UserFriendsResponse;
import com.geogenie.data.model.UserSetting;
import com.geogenie.data.model.UserTypeBasedOnDevice;
import com.geogenie.geo.service.business.UserService;
import com.geogenie.geo.service.exception.ClientException;
import com.geogenie.geo.service.exception.RestErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

/**
 * TODO: For All methods, check additionally that one user does not end up updating other user
 * @author Ankit.Joinwal
 */
@RestController
@RequestMapping("/api/secured/users")

public class UserSecuredController implements Constants{

	private static final Logger logger = LoggerFactory.getLogger(UserSecuredController.class);
	
	@Autowired
	UserService userService;

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public User getUser(@PathVariable long id) {
		logger.debug("### Inside getUser method.Arguments {} ###",id);
		//TODO:Check if user can access other user's profile?
		User user = userService.getUser(id);
		return user;
	}

	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public User signinOrSignupUser(@RequestHeader(required = true, value = Constants.USER_TYPE_HEADER) String userType,
			@Valid @RequestBody User user, HttpServletRequest  request) throws ServiceException{
		
		logger.info("### Request recieved- signinOrSignupUser. Arguments : {} ###"+user);
		logger.info("   Social Details : {} ",user.getSocialDetails());
		
		logger.info("	User Type : "+userType);
		UserTypeBasedOnDevice userTypeBasedOnDevice = null;
		
		if(userType.equals(UserTypeBasedOnDevice.MOBILE.toString())){
			userTypeBasedOnDevice = UserTypeBasedOnDevice.MOBILE;
		}else if (userType.equals(UserTypeBasedOnDevice.WEB.toString())){
			userTypeBasedOnDevice = UserTypeBasedOnDevice.WEB;
		}else{
			throw new ClientException(RestErrorCodes.ERR_001,ERROR_USER_TYPE_INVALID);
		}
		User createdUser = userService.signupOrSignin(user,userTypeBasedOnDevice);
		
		logger.info("### Signup/Signin successfull for user : {} ",user.getEmailId());
		
		return createdUser;
		
	}

	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public List<User> getAllUsers() {
		logger.info("### Request recieved- Get All Users ###");
		List<User> users = userService.getAllUsers();
		return users;
	}
	
	@RequestMapping(value = "/{id}/preferences/tags", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public List<EventTag> getUserTagPreferences(@PathVariable Long id){
		logger.info("### Request recieved- Get All Users ###");
		return this.userService.getUserTagPreferences(id);
	}
	
	@RequestMapping(value = "/{id}/preferences/tags", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public List<EventTag> saveUserTagPreferences(@PathVariable Long id,@RequestBody List<EventTag> tags){
		logger.info("### Request recieved- Get All Users ###");
		return this.userService.saveUserTagPreferences(id, tags);
	}

	@RequestMapping(value = "/{id}/friends", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public UserFriendsResponse setupFriendsForNewUser(@PathVariable Long id,@RequestBody String[] friendsSocialIds) throws ServiceException{
		logger.info("### Request recieved- setupFriendsForNewUser for user {} ###",id);
		UserFriendsResponse friendsResponse = new UserFriendsResponse();
		if(friendsSocialIds!=null && friendsSocialIds.length>0){
			List<UserFriend> userFriends = this.userService.setupUserFriendsForNewUser(id, friendsSocialIds);
			friendsResponse.setFriends(userFriends);
			friendsResponse.setCount(userFriends.size());
		}
		return  friendsResponse;
	}
	
	@RequestMapping(value = "/{id}/settings", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public List<UserSetting> getUserSetings(@PathVariable Long id){
		logger.info("### Request recieved- getUserSetings for user {} ###",id);
		List<UserSetting> userSettings = this.userService.getUserSettings(id);
		return  userSettings;
	}
	
	@RequestMapping(value = "/{id}/settings", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.OK)
	public void saveUserSetings(@PathVariable Long id,@RequestBody List<UserSetting> settings){
		logger.info("### Request recieved- saveUserSetings for user {} ###",id);
		this.userService.setUserSettings(id, settings);
	}
	
}
