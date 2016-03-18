package com.zeus.service.spring;

import com.zeus.service.annotation.API;
import com.zeus.service.annotation.Token;

@API
@Token("123423152151")
public interface AService{
	
	public void print();

}
