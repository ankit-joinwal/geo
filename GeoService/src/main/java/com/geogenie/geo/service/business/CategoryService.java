package com.geogenie.geo.service.business;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.geogenie.Constants;
import com.geogenie.data.model.Category;

public interface CategoryService {

	@PreAuthorize("hasRole('"+Constants.ROLE_TYPE_ADMIN+"')")
	public Category create(Category category);
	
	public List<Category> getAll();
	
	
}
