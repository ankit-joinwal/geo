package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.PushNotificationSettingMaster;
import com.geogenie.data.model.User;

public interface PushNotificationDAO {

	public List<PushNotificationSettingMaster> getPushNotTypes();
	
}
