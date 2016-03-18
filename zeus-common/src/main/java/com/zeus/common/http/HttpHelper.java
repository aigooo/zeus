package com.zeus.common.http;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
	
	public static Object post(String url,Object data) throws Exception{
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost method = new HttpPost(url); 
		
		StringEntity entity = new StringEntity((String) data,"utf-8");   
		entity.setContentEncoding("UTF-8");    
		method.setEntity(entity);
		
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Object get(String url) throws Exception{
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet method = new HttpGet(url); 
		
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
