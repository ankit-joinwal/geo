package com.geogenie.geo.service.business;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geogenie.data.model.Category;
import com.geogenie.geo.service.dao.CategoryDAO;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDAO categoryDAO;
	
	
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	@Override
	public Category create(Category category) {
		Date now = new Date();
		category.setCreateDt(now);
		return categoryDAO.create(category);
	}

	@Override
	public List<Category> getAll() {
		return categoryDAO.getAllParentCategories();
	}
	
	@Override
	public List<Category> getSubCategories(Long catgoryId) {
		return categoryDAO.getSubCategories(catgoryId);
	}

	@Override
	public List<Category> getAllSubCategories() {
		return categoryDAO.getAllSubCategories();
	}
}
