package com.zeus.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class HttpsHelper {
	
	public static Map<String,SSLContext> sslContexts = new HashMap<>();
	
	public static void register(String keyStoreKey,String keyStorePath,String keyStoreType,String keyStorePass) throws Exception{
	    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
	    FileInputStream instream = new FileInputStream(new File(keyStorePath));
	    keyStore.load(instream, keyStorePass.toCharArray());
        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, keyStorePass.toCharArray()).build();
        sslContexts.put(keyStoreKey, sslContext);
	}
	
	@SuppressWarnings("deprecation")
	public static Object post(String url,Object data,String keyStoreKey) throws Exception{
		SSLContext sslcontext = sslContexts.get(keyStoreKey);
		if(sslcontext==null){
			throw new Exception("证书信息不存在！");
		}
		
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslcontext,new String[]{"TLSv1"},null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        
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
}
