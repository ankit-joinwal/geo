package com.geogenie.geo.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.Category;
import com.geogenie.geo.service.business.CategoryService;

@RestController
@RequestMapping("/api/public/categories")
public class CategoryPublicController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryPublicController.class);
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Category> get(){
		logger.info("### Request recieved- Get All Categories. ###");
		return categoryService.getAll();
	}
	
	
	@RequestMapping(value="/{id}/subcategories",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Category> getSubCategories(@PathVariable Long id){
		logger.info("### Request recieved- Get Sub Categories for {} ###",id);
		return categoryService.getSubCategories(id);
	}
	

	
	@RequestMapping(value="/subcategories",method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Category> getAllSubCategories(){
		logger.info("### Request recieved- Get All Sub Categories ###");
		return categoryService.getAllSubCategories();
	}
	
}
