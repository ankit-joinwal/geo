package com.geogenie.geo.service.business;

import java.util.List;

import com.geogenie.data.model.Category;

public interface CategoryService {

	public Category create(Category category);
	
	public List<Category> getAll();
	
	public List<Category> getSubCategories(Long catgoryId);
	
	
}
