package com.zeus.service.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.zeus.service.annotation.RPC;

@RPC
@Component
public class CService {

	private AService aService;
	
	public void print(){
		System.out.println("print for C!");
		aService.print();
	}
	
	@PostConstruct
	public void doSomething(){
		
	}

	public AService getaService() {
		return aService;
	}

	public void setaService(AService aService) {
		this.aService = aService;
	}
}
