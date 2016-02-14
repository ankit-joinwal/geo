package com.geogenie.geo.service.business;

import com.geogenie.data.model.ext.Places;
import com.geogenie.data.model.requests.TextSearchRequest;
import com.geogenie.geo.service.exception.ServiceException;

public interface TextSearchService {

	public Places search(TextSearchRequest textSearchRequest) ;
}
