package com.geogenie.geo.service.business;

import com.geogenie.data.model.ext.Places;
import com.geogenie.data.model.requests.NearbySearchRequest;

public interface NearbySearchService {

	public Places search(NearbySearchRequest nearbySearchRequest) ;
}
