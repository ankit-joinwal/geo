package com.geogenie.geo.service.business;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geogenie.data.model.GAPIConfig;
import com.geogenie.data.model.ext.PlaceDetails;
import com.geogenie.data.model.requests.PlaceDetailsRequest;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.helper.PlaceDetailsHelper;

@Service
@Transactional
public class PlaceDetailsServiceImpl implements PlaceDetailService{
	private static final Logger logger = LoggerFactory.getLogger(PlaceDetailsServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GAPIConfig gapiConfig;

	@Override
	public PlaceDetails getPlaceDetails(PlaceDetailsRequest placeDetailsRequest)
			throws ServiceException {
		PlaceDetails placeDetails = null;
		try{
			placeDetails = PlaceDetailsHelper.executeSearch(restTemplate, placeDetailsRequest, gapiConfig);
			
		}catch(ServiceException exception){
			logger.error("Error occurred while retrieving place details ",exception);
			throw exception;
		}catch(Exception exception){
			logger.error("Error occurred while retrieving place details",exception);
			throw new ServiceException(ServiceErrorCodes.ERR_050, exception.getMessage());
		}
		return placeDetails;
	}
	
	
}
