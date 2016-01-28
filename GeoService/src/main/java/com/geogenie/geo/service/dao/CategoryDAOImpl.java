package com.geogenie.geo.service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
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
	public List<Category> getAllParentCategories() {
		Criteria criteria = getSession().createCriteria(Category.class).add(Restrictions.eq("parentId", 0L)).addOrder(Order.asc("displayOrder"));
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
		//category.getRelatedEventTypes();
		return category;
	}
	@Override
	public List<Category> getAllSubCategories() {
		Criteria criteria = getSession().createCriteria(Category.class).add(Restrictions.ne("parentId", 0L));
		return (List<Category>) criteria.list();
	}

	@Override
	public List<Category> getCategoriesByName(List<String> categoryNames) {
		Criteria criteria = getSession().createCriteria(Category.class).add(Restrictions.in("name", categoryNames)).setFetchMode("relatedEventTypes", FetchMode.JOIN);
		List<Category> categList = (List<Category>) criteria.list();
		return categList;
	}
}
