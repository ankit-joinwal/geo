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
import com.geogenie.data.model.requests.TextSearchRequest;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

public class TextSearchHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(TextSearchHelper.class);

	public static Places executeSearch(RestTemplate restTemplate,
			TextSearchRequest textSearchRequest, GAPIConfig gapiConfig)
			throws ServiceException {

		StringBuilder url = new StringBuilder(gapiConfig.getTextSearchURL());
		url.append(gapiConfig.getDataExchangeFormat() + Constants.QUESTIONMARK);
		// "query" param
		url.append(TextSearchRequest.TextSearchRequestParamNames.QUERY
				.getName()
				+ Constants.EQUAL
				+ textSearchRequest.getQuery().replaceAll(" ", Constants.PLUS));

		// "location" param
		if (textSearchRequest.getLocation() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.LOCATION
							.getName() + Constants.EQUAL
					+ textSearchRequest.getLocation());
		}

		// "radius" param
		if (textSearchRequest.getRadius() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.RADIUS
							.getName() + Constants.EQUAL
					+ textSearchRequest.getRadius());
		}

		// "types" param
		if (textSearchRequest.getTypes() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.TYPES
							.getName() + Constants.EQUAL
					+ textSearchRequest.getTypes());
		}

		// "name" param
		if (textSearchRequest.getName() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.NAME
							.getName()
					+ Constants.EQUAL
					+ textSearchRequest.getName().replaceAll(" ",
							Constants.PLUS));
		}

		// "rankby" param
		if (textSearchRequest.getRankBy() != null) {
			url.append(Constants.AMP
					+ TextSearchRequest.TextSearchRequestParamNames.RANKBY
							.getName() + Constants.EQUAL
					+ textSearchRequest.getRankBy());
		}

		// "key" param
		url.append(Constants.AMP
				+ TextSearchRequest.TextSearchRequestParamNames.KEY.getName()
				+ Constants.EQUAL + gapiConfig.getGapiKey());

		logger.info("### Inside TextSearchHelper.executeSearch | URL : {} "
				+ url.toString());

		logger.info("### Executing Search ###");
		ResponseEntity<Places> placesResponse = restTemplate.exchange(
				url.toString(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Places>() {
				});

		HttpStatus returnStatus = placesResponse.getStatusCode();
		boolean isSuccess = returnStatus.is2xxSuccessful();
		if (isSuccess) {
			logger.info("### Search successful for url : {}" + url.toString());

		} else {
			if (returnStatus.is4xxClientError()) {
				throw new ServiceException(ServiceErrorCodes.ERR_010,
						"Invalid Client Request");
			} else if (returnStatus.is5xxServerError()) {
				throw new ServiceException(ServiceErrorCodes.ERR_010,
						"Error with backend services");
			}
		}

		return placesResponse.getBody();
	}
}
