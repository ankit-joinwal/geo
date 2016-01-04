package com.geogenie.geo.service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.Category;

@Repository("categoryDAO")
public class CategoryDAOImpl extends AbstractDAO implements CategoryDAO{

	@Override
	public Category create(Category category) {
		save(category);
		return category;
	}

	@Override
	public List<Category> getAll() {
		Criteria criteria = getSession().createCriteria(Category.class).add(Restrictions.eq("parentId", 0L));
		return (List<Category>) criteria.list();
		
	}
	
	@Override
	public List<Category> getSubCategories(Long categoryId) {
		Criteria criteria = getSession().createCriteria(Category.class).add(Restrictions.eq("parentId", categoryId));
		return (List<Category>) criteria.list();
	}
	
	@Override
	public Category getCategoryById(Long id) {
		Category category = (Category) getSession().get(Category.class, id);
		return category;
	}

}