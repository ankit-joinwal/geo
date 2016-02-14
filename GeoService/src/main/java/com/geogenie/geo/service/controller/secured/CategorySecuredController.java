package com.geogenie.geo.service.controller.secured;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geogenie.data.model.Category;
import com.geogenie.geo.service.business.CategoryService;

@RestController
@RequestMapping("/api/secured/categories")
public class CategorySecuredController {

	private static final Logger logger = LoggerFactory.getLogger(CategorySecuredController.class);
	@Autowired
	private CategoryService categoryService;
	
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@RequestMapping(method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public Category create(@Valid @RequestBody Category category){
		logger.info("### Request recieved- CreateCategory. Arguments : {} ###"+category);
		Category category2 = categoryService.create(category);
		return category2;
	}
	
}
