package com.zeus.persist.model;

public interface IdModel<T> {

	public T getId();
	
	public void setId(T id);
	
	public String getOrder();
	
	public void setOrder(String order);
}
