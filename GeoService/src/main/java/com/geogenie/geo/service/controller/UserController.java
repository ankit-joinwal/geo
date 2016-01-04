package com.geogenie.geo.service.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.User;
import com.geogenie.geo.service.business.IUserService;

/**
 * @author Ankit.Joinwal
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	IUserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public User getUser(@PathVariable long id) {
		logger.debug("### Inside getUser method.Arguments {} ###",id);
		
		User user = userService.getUser(id);
		return user;
	}

	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public User registerUser(@Valid @RequestBody User user, HttpServletResponse  response) {
		
		logger.info("### Request recieved- RegisterUser. Arguments : {} ###"+user);
		User createdUser = userService.registerUser(user);
		
		logger.info("### Registration successfull for user : {} ",user.getEmailId());
		
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
	
	

}
