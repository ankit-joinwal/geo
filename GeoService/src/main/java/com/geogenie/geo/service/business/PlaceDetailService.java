package com.geogenie.geo.service.business;

import com.geogenie.data.model.ext.PlaceDetails;
import com.geogenie.data.model.requests.PlaceDetailsRequest;
import com.geogenie.geo.service.exception.ServiceException;

public interface PlaceDetailService {
	public PlaceDetails getPlaceDetails(PlaceDetailsRequest placeDetailsRequest);
}
