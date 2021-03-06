package com.geogenie.geo.service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.geogenie.data.model.PushNotificationSettingMaster;

@Repository("pushNotificationDAO")
public class PushNotificationDAOImpl extends AbstractDAO implements PushNotificationDAO{

	@Override
	public List<PushNotificationSettingMaster> getPushNotTypes() {
		Criteria criteria = getSession().createCriteria(PushNotificationSettingMaster.class)
				.addOrder(Order.asc("displayOrder"));
		return criteria.list();
	}
	
	
}
