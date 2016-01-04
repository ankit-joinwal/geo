package com.geogenie.geo.service.business;

import com.geogenie.data.model.ext.Places;
import com.geogenie.data.model.requests.NearbySearchRequest;
import com.geogenie.geo.service.exception.ServiceException;

public interface NearbySearchService {

	public Places search(NearbySearchRequest nearbySearchRequest) throws ServiceException;
}
