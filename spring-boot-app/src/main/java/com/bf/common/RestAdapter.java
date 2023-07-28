package com.bf.common;

import com.bf.common.util.UtilManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.net.ssl.SSLContext;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
public class RestAdapter {
	private static final int TIMEOUT	= 10 * 1000;

	/**
	 * RestTemplate 생성
	 * CONNECTION 타임아웃 시간 3초 설정
	 */
	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = null;
		
		try {
			TrustStrategy acceptingTrustStrategy = (new TrustStrategy() {
	            @Override
	            public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
	                return true;
	            }
	        });
			
			SSLContext sslContext;
			sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();
			
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

			CloseableHttpClient httpClient = HttpClients.custom()
			        .setSSLSocketFactory(csf)
			        .build();
			
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(TIMEOUT);
			factory.setReadTimeout(TIMEOUT);
			factory.setHttpClient(httpClient);
			
			restTemplate = new RestTemplate(factory);
			
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		return restTemplate;
	}
	
	/**
	 * RestTemplate 생성
	 * CONNECTION 타임아웃 시간 3초 설정
	 */
	private static RestTemplate getRestTemplate(int timeout) {
		RestTemplate restTemplate = null;
		
		try {
			TrustStrategy acceptingTrustStrategy = (new TrustStrategy() {
	            @Override
	            public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
	                return true;
	            }
	        });
			
			SSLContext sslContext;
			sslContext = SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();
			
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

			CloseableHttpClient httpClient = HttpClients.custom()
			        .setSSLSocketFactory(csf)
			        .build();
			
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(timeout);
			factory.setReadTimeout(timeout);
			factory.setHttpClient(httpClient);
			
			restTemplate = new RestTemplate(factory);
			
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		return restTemplate;
	}
	
	/**
	 * REST API 호출 - JSONArray 파라미터 용
	 * 
	 * @param HttpHeaders headers 		- HTTP 헤더 정보
	 * @param String callUrl 			- API 호출 URL
	 * @param HttpMethod method 		- HTTP Method (POST, PUT, GET, DELETE)
	 * @param JSONArray jsonArray 		- JSONArray 타입의 파라미터
	 * @return JSONObject jsonResult 	- JSONObject 타입 데이터 리턴
	 * @throws
	 */
	public static JSONObject callRestApi (HttpHeaders headers, String callUrl, HttpMethod method, JSONArray jsonArray) throws ParseException {
		log.info("[RestAdapter][callRestApi][JSONArray][START]");
		
		if (UtilManager.isEmptyOrNull(callUrl)) return null;
		
		JSONObject jsonResult 		= null;
		RestTemplate restTemplate 	= getRestTemplate();
		
		HttpHeaders httpHeaders		= headers;
		
		if (UtilManager.isEmptyOrNull(httpHeaders)) {
			httpHeaders	= new HttpHeaders();
			httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.setContentType(new MediaType(Constants.APPLICATION, Constants.JSON, Charset.forName(Constants.UTF_8)));
		}
		
		if (UtilManager.isEmptyOrNull(method)) {
			method = HttpMethod.POST;
		}
		
		if (UtilManager.isEmptyOrNull(jsonArray)) {
			jsonArray = new JSONArray();
		}
		
		log.info("[RestAdapter][callRestApi][url] : {}", callUrl);
		log.info("[RestAdapter][callRestApi][method] : {}", method);
		log.info("[RestAdapter][callRestApi][params] : {}", jsonArray.toJSONString());
		
        try {

            HttpEntity<String> payload 	= new HttpEntity<String>(jsonArray.toString(), httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(callUrl, method, payload, String.class);
            
            if (!UtilManager.isEmptyOrNull(response)) {
            	jsonResult = new JSONObject(response.getBody());
            }
            
            log.info("[RestAdapter][callRestApi][result][StatusCode] : {}", response.getStatusCode());
            log.info("[RestAdapter][callRestApi][result][Body] : {}", response.getBody());
        } catch (Exception ex) {
        	log.error("[RestAdapter][callRestApi][ERROR] : {}", ex.getMessage());
        }
        log.info("[RestAdapter][callRestApi][JSONArray][END]");
        return jsonResult;
	}
	
	/**
	 * REST API 호출 - JSONObject 파라미터 용
	 * 
	 * @param HttpHeaders headers 		- HTTP 헤더 정보
	 * @param String callUrl 			- API 호출 URL
	 * @param HttpMethod method 		- HTTP Method (POST, PUT, GET, DELETE)
	 * @param JSONObject jsonObject 	- JSONObject 타입의 파라미터
	 * @return JSONObject jsonResult 	- JSONObject 타입 데이터 리턴
	 * @throws
	 */
	public static JSONObject callRestApi (HttpHeaders headers, String callUrl, HttpMethod method, JSONObject jsonObject) throws ParseException {
		log.info("[RestAdapter][callRestApi][JSONObject][START]");
		
		if (UtilManager.isEmptyOrNull(callUrl)) return null;
		
		JSONObject jsonResult 		= null;
		RestTemplate restTemplate 	= getRestTemplate();
		
		HttpHeaders httpHeaders		= headers;
		
		if (UtilManager.isEmptyOrNull(httpHeaders)) {
			httpHeaders	= new HttpHeaders();
			httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.setContentType(new MediaType(Constants.APPLICATION, Constants.JSON, Charset.forName(Constants.UTF_8)));
		}
		
		if (UtilManager.isEmptyOrNull(method)) {
			method = HttpMethod.POST;
		}
		
		if (UtilManager.isEmptyOrNull(jsonObject)) {
			jsonObject = new JSONObject();
		}
		
		log.info("[RestAdapter][callRestApi][url] : {}", callUrl);
		log.info("[RestAdapter][callRestApi][method] : {}", method);
		log.info("[RestAdapter][callRestApi][params] : {}", jsonObject.toString());
		
        try {

            HttpEntity<String> payload 	= new HttpEntity<String>(jsonObject.toString(), httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(callUrl, method, payload, String.class);
            
            if (!UtilManager.isEmptyOrNull(response)) {
            	jsonResult = new JSONObject(response.getBody());
            }
            
            log.info("[RestAdapter][callRestApi][result][StatusCode] : {}", response.getStatusCode());
            log.info("[RestAdapter][callRestApi][result][Body] : {}", response.getBody());
        } catch (Exception ex) {
        	log.error("[RestAdapter][callRestApi][ERROR] : {}", ex.getMessage());
        }
        log.info("[RestAdapter][callRestApi][JSONObject][END]");
        return jsonResult;
	}
	
	/**
	 * REST API 호출 - JSONObject 파라미터 용
	 * 
	 * @param HttpHeaders headers 		- HTTP 헤더 정보
	 * @param String callUrl 			- API 호출 URL
	 * @param HttpMethod method 		- HTTP Method (POST, PUT, GET, DELETE)
	 * @param JSONObject jsonObject 	- JSONObject 타입의 파라미터
	 * @return JSONObject jsonResult 	- JSONObject 타입 데이터 리턴
	 * @throws
	 */
	public static JSONObject callRestApi (HttpHeaders headers, String callUrl, HttpMethod method, String paramStr) throws ParseException {
		log.info("[RestAdapter][callRestApi][JSONObject][START]");
		
		if (UtilManager.isEmptyOrNull(callUrl)) return null;
		
		JSONObject jsonResult 		= null;
		RestTemplate restTemplate 	= getRestTemplate();
		
		HttpHeaders httpHeaders		= headers;
		
		if (UtilManager.isEmptyOrNull(httpHeaders)) {
			httpHeaders	= new HttpHeaders();
			httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.setContentType(new MediaType(Constants.APPLICATION, Constants.JSON, Charset.forName(Constants.UTF_8)));
		}
		
		if (UtilManager.isEmptyOrNull(method)) {
			method = HttpMethod.POST;
		}
		
		if (UtilManager.isEmptyOrNull(paramStr)) {
			paramStr = "";
		}
		
		log.info("[RestAdapter][callRestApi][url] : {}", callUrl);
		log.info("[RestAdapter][callRestApi][method] : {}", method);
		log.info("[RestAdapter][callRestApi][params] : {}", paramStr);
		
        try {

            HttpEntity<String> payload 	= new HttpEntity<String>(paramStr, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(callUrl, method, payload, String.class);
            
            if (!UtilManager.isEmptyOrNull(response)) {
            	jsonResult = new JSONObject(response.getBody());
            }
            
            log.info("[RestAdapter][callRestApi][result][StatusCode] : {}", response.getStatusCode());
            log.info("[RestAdapter][callRestApi][result][Body] : {}", response.getBody());
        } catch (Exception ex) {
        	log.error("[RestAdapter][callRestApi][ERROR] : {}", ex.getMessage());
        }
        log.info("[RestAdapter][callRestApi][JSONObject][END]");
        return jsonResult;
	}
	
	/**
	 * REST API 호출 - JSONObject 파라미터 용
	 * 
	 * @param HttpHeaders headers 		- HTTP 헤더 정보
	 * @param String callUrl 			- API 호출 URL
	 * @param HttpMethod method 		- HTTP Method (POST, PUT, GET, DELETE)
	 * @param JSONObject jsonObject 	- JSONObject 타입의 파라미터
	 * @return JSONObject jsonResult 	- JSONObject 타입 데이터 리턴
	 * @throws
	 */
	public static JSONObject callRestApi (HttpHeaders headers, String callUrl, HttpMethod method, String paramStr, int timeout) throws ParseException {
		log.info("[RestAdapter][callRestApi][JSONObject][START]");
		
		if (UtilManager.isEmptyOrNull(callUrl)) return null;
		
		JSONObject jsonResult 		= null;
		RestTemplate restTemplate 	= getRestTemplate(timeout);
		
		HttpHeaders httpHeaders		= headers;
		
		if (UtilManager.isEmptyOrNull(httpHeaders)) {
			httpHeaders	= new HttpHeaders();
			httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.setContentType(new MediaType(Constants.APPLICATION, Constants.JSON, Charset.forName(Constants.UTF_8)));
		}
		
		if (UtilManager.isEmptyOrNull(method)) {
			method = HttpMethod.POST;
		}
		
		if (UtilManager.isEmptyOrNull(paramStr)) {
			paramStr = "";
		}
		
		log.info("[RestAdapter][callRestApi][url] : {}", callUrl);
		log.info("[RestAdapter][callRestApi][method] : {}", method);
		log.info("[RestAdapter][callRestApi][params] : {}", paramStr);
		
        try {

            HttpEntity<String> payload 	= new HttpEntity<String>(paramStr, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(callUrl, method, payload, String.class);
            
            if (!UtilManager.isEmptyOrNull(response)) {
            	jsonResult = new JSONObject(response.getBody());
            }
            
            log.info("[RestAdapter][callRestApi][result][StatusCode] : {}", response.getStatusCode());
            log.info("[RestAdapter][callRestApi][result][Body] : {}", response.getBody());
        } catch (Exception ex) {
        	log.error("[RestAdapter][callRestApi][ERROR] : {}", ex.getMessage());
        }
        log.info("[RestAdapter][callRestApi][JSONObject][END]");
        return jsonResult;
	}
	
	/**
	 * REST API 호출 - 파일 업로드 파라미터 용
	 * 
	 * @param String callUrl 						- API 호출 URL
	 * @param MultipartHttpServletRequest request 	- 파일 업로드 파라미터
	 * @return JSONObject jsonResult 				- JSONObject 타입 데이터 리턴
	 * @throws
	 */
	public static JSONObject callRestApi (String callUrl, MultipartHttpServletRequest request) throws ParseException {
		log.info("[RestAdapter][callRestApi][FILE UPLOAD][START]");
		
		if (UtilManager.isEmptyOrNull(callUrl)) return null;
		
		JSONObject jsonResult 		= null;
		RestTemplate restTemplate 	= getRestTemplate();
		
		HttpHeaders httpHeaders	= new HttpHeaders();
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		log.info("[RestAdapter][callRestApi][url] : {}", callUrl);
		
        try {
        	MultiValueMap<String, Object> body 	= new LinkedMultiValueMap<>();
        	Iterator<String> itr 				= request.getFileNames();
        	
        	while (itr.hasNext()) {
        		String fileName = URLDecoder.decode(itr.next(), "UTF-8");
        		MultipartFile file = request.getFile(fileName);
        		
        		String oriFileName = new String (file.getOriginalFilename().getBytes("8859_1"), "UTF-8");
        		oriFileName = URLEncoder.encode(oriFileName, "UTF-8");
        		
        		if (!file.isEmpty()) {
        			FileOutputStream fo = new FileOutputStream(oriFileName);
        			fo.write(request.getFile(fileName).getBytes());
        			fo.close();
        			
        			body.add(fileName, new FileSystemResource(oriFileName));
        		}
        		
        		log.info("[RestAdapter][callRestApi][FILE NAME] : {}", fileName);
        	}
        	
        	Enumeration enr = request.getParameterNames();
        	
        	while (enr.hasMoreElements()) {
        		String name = URLDecoder.decode(enr.nextElement().toString(), "UTF-8");
        		body.add(name, request.getParameter(name));
        		log.info("[RestAdapter][callRestApi][PARAM] : [{} : {}]", name, request.getParameter(name));
        	}
            HttpEntity<MultiValueMap<String, Object>> payload = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<String> response = restTemplate.postForEntity(callUrl, payload, String.class);
            if (!UtilManager.isEmptyOrNull(response)) {
            	jsonResult = new JSONObject(response.getBody());
            }
            
            log.info("[RestAdapter][callRestApi][result][StatusCode] : {}", response.getStatusCode());
            log.info("[RestAdapter][callRestApi][result][Body] : {}", response.getBody());
        } catch (Exception ex) {
        	log.error("[RestAdapter][callRestApi][ERROR] : {}", ex.getMessage());
        }
        log.info("[RestAdapter][callRestApi][FILE UPLOAD][END]");
        return jsonResult;
	}
	
}
