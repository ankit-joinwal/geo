package com.geogenie;

public interface Constants {

	String LOCATION_HEADER = "Location";
	String JDBC_DRIVER_PROPERTY = "jdbc.driverClassName";
	String JDBC_URL_PROPERTY = "jdbc.url";
	String JDBC_USERNAME_PROPERTY = "jdbc.username";
	String JDBC_PASSWORD_PROPERTY = "jdbc.password";
	String HIBERNATE_DIALECT_PROPERTY = "hibernate.dialect";
	String HIBERNATE_SHOW_SQL_PROPERTY = "hibernate.show_sql";
	String HIBERNATE_FORMAT_SQL_PROPERTY = "hibernate.format_sql";
	String HIBERNATE_HBM_DDL_PROPERTY = "hibernate.hbm2ddl.auto";
	String DEFAULT_USER_DAILY_QUOTA_PROPERTY = "ggenie.default.user.quota";
	Integer DEFAULT_USER_DAILY_QUOTA = 100;
	String G_NEARBY_PLACES_URL = "gplaces.nearby.url";
	String G_TSEARCH_URL = "gplaces.tsearch.url";
	String G_PLACE_DETAIL_URL = "gplaces.place.details.url";
	String DEFAULT_GAPI_DATA_EXCHANGE_FMT = "gapi.data.format";
	String GAPI_KEY = "gapi.key";
	String USER_SERVICE_URL = "user.svc.url";
	String USER_VALIDATE_URL = "user.validate.url";
	String AUTHORIZATION_HEADER = "Authorization";
	String USER_TYPE_HEADER = "type";
	String ACCEPT_HEADER = "Accept";
	String QUESTIONMARK = "?";
	String AMP = "&";
	String EQUAL = "=";
	String PLUS = "+";
	String UNAME_DELIM = "~";
	String DEVICE_PREFIX = "sd";
	//Default Radius for Nearby Search
	String DEFAULT_RADIUS = "1000";
	String RECORDS_PER_PAGE = "10";
	String MEETUP_DATE_FORMAT = "dd/MM/yyyy hh:mm aa";
}
