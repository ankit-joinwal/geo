package com.geogenie.geo.service.business;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geogenie.Constants;
import com.geogenie.data.model.Category;
import com.geogenie.data.model.GAPIConfig;
import com.geogenie.data.model.ext.Places;
import com.geogenie.data.model.requests.NearbySearchRequest;
import com.geogenie.geo.service.dao.CategoryDAO;
import com.geogenie.geo.service.exception.ClientException;
import com.geogenie.geo.service.exception.RestErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.helper.NearbySearchHelper;

/**
 * Service for Performing "Near by" search. <a href=
 * "https://developers.google.com/places/web-service/search#PlaceSearchRequests"
 * >
 * 
 * @author ajoinwal
 * 
 */
@Service
@Transactional
public class NearbySearchServiceImpl implements NearbySearchService ,Constants{

	private static final Logger logger = LoggerFactory
			.getLogger(NearbySearchServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GAPIConfig gapiConfig;

	@Autowired
	private CategoryDAO categoryDAO;

	public CategoryDAO getCategoryDAO() {
		return categoryDAO;
	}

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public GAPIConfig getGapiConfig() {
		return gapiConfig;
	}

	public void setGapiConfig(GAPIConfig gapiConfig) {
		this.gapiConfig = gapiConfig;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Places search(NearbySearchRequest nearbySearchRequest)
			 {
		Places places = null;
		try {
			Category category = categoryDAO.getCategoryById(nearbySearchRequest
					.getCategoryId());
			if (category == null) {
				throw new ClientException(RestErrorCodes.ERR_001,ERROR_INVALID_CATEGORY);
			}
			nearbySearchRequest.setTypes(category.getName());

			places = NearbySearchHelper.executeSearch(restTemplate,
					nearbySearchRequest, gapiConfig);
			if(places!=null && places.getResults()!=null){
				places.setTotalRecords(places.getResults().size());
			}

		}catch (ClientException exception) {
			logger.error("Error occurred while performing near by search",
					exception);
			throw exception;
		} 
		catch (ServiceException exception) {
			logger.error("Error occurred while performing near by search",
					exception);
			throw exception;
		} catch (Exception exception) {
			logger.error("Error occurred while performing near by search",
					exception);
			throw new ServiceException(GEO_SERVICE_NAME,RestErrorCodes.ERR_050,
					exception.getMessage());
		}
		return places;
	}

}
