package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.Category;

public interface CategoryDAO{

	public Category create(Category category);
	
	public List<Category> getAllParentCategories();
	
	public List<Category> getSubCategories(Long categoryId);
	
	public Category getCategoryById(Long id);
	
	public List<Category> getAllSubCategories();
	
	public List<Category> getCategoriesByName(List<String> categoryNames);
}
