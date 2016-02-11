package com.geogenie.geo.service.dao;

import java.util.List;

import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;

public interface SmartDeviceDAO {

	public SmartDevice getSmartDeviceByDeviceId(String deviceId);
	
	public List<Role> getUserRolesByDevice(String deviceId);
}
