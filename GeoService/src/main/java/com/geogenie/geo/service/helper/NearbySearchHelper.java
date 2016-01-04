package com.geogenie.geo.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.geogenie.Constants;
import com.geogenie.data.model.GAPIConfig;
import com.geogenie.data.model.ext.Places;
import com.geogenie.data.model.requests.NearbySearchRequest;
import com.geogenie.geo.service.dao.CategoryDAO;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

public class NearbySearchHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(NearbySearchHelper.class);

	public static Places executeSearch(RestTemplate restTemplate,
			NearbySearchRequest nearbySearchRequest, GAPIConfig gapiConfig)
			throws ServiceException {

		StringBuilder url = new StringBuilder(gapiConfig.getNearBySearchURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		url.append(NearbySearchRequest.NearbySearchRequestParamNames.LOCATION
				.getName()
				+ Constants.EQUAL
				+ nearbySearchRequest.getLocation());
		if (nearbySearchRequest.getRadius() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.RADIUS
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getRadius());
		}
		if (nearbySearchRequest.getTypes() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.TYPES
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getTypes());
		}
		if (nearbySearchRequest.getName() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.NAME
							.getName()
					+ Constants.EQUAL
					+ nearbySearchRequest.getName().replaceAll(" ",
							Constants.PLUS));
		}
		if (nearbySearchRequest.getRankBy() != null) {
			url.append(Constants.AMP
					+ NearbySearchRequest.NearbySearchRequestParamNames.RANKBY
							.getName() + Constants.EQUAL
					+ nearbySearchRequest.getRankBy());
		}
		url.append(Constants.AMP
				+ NearbySearchRequest.NearbySearchRequestParamNames.KEY
						.getName() + Constants.EQUAL + gapiConfig.getGapiKey());

		logger.info("### Inside NearbySearchHelper.executeSearch | URL : {} "
				+ url.toString());

		logger.info("### Executing Search ###");
		ResponseEntity<Places> placesResponse = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Places>() {
				});

		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if(isSuccess){
			logger.info("### Search successful for url : {}"+url.toString());
			
		}else{
			if(returnStatus.is4xxClientError()){
				throw new ServiceException(ServiceErrorCodes.ERR_010,"Invalid Client Request");
			}else if (returnStatus.is5xxServerError()){
				throw new ServiceException(ServiceErrorCodes.ERR_010,"Error with backend services");
			}
		}
		
		return placesResponse.getBody();
	}
}
