package com.geogenie.geo.service.transformers;

import com.geogenie.geo.service.exception.ServiceException;

public interface Transformer<T,V> {
	
	public T transform(V v) throws ServiceException;
}
