package com.zeus.service.spring;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.zeus.service.annotation.RPC;

@RPC
@Component
public class BService {

	AService aService;
	
	public void print(){
		System.out.println("print for B!");
		aService.print();
	}
	
	@PostConstruct
	public void doSomething(){
		
	}
}
