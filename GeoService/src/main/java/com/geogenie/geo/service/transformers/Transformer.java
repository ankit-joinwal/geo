package com.geogenie.geo.service.transformers;

public interface Transformer<T,V> {
	
	public T transform(V v);
}
