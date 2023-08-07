package com.bf.web.api.service;

import com.bf.common.Constants;
import com.bf.common.Consts;
import com.bf.common.RestAdapter;
import com.bf.common.util.UtilManager;
import com.bf.web.api.vo.ApiResponseVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.google.gson.Gson;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiService {

	@Value(value = "${system.unierp.serviceCode}")
	private String uniErpServiceCode;
	@Value(value = "${system.unierp.secretKey}")
	private String uniErpSecretKey;
	@Value(value = "${system.unierp.url}")
	private String uniErpUrl;
	@Value(value="${system.webroot.filePath}")
	public static String systemFilePath;

	
	/**
	 * 
	 * @param url
	 * @param method
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	public ApiResponseVO callErpApiServer(String url, HttpMethod method, JSONObject jsonData) throws Exception {
		
		log.info("[API][SERVICE][ApiService][callErpApiServer][START]");
		
		// ERP API 설정
		String serviceCode = uniErpServiceCode;
		String secretKey = uniErpSecretKey;
		String apiUrl = uniErpUrl;
		String callUrl = apiUrl + url;
		
		switch (method) {
		case  GET:
			log.info("GET");
			Iterator<String> keys = jsonData.keys();
			List<String> params = new ArrayList<String>();;
			while (keys.hasNext()) {
				String key = keys.next().toString();				
				params.add(key + "=" + jsonData.get(key));
						
			}
			callUrl += "?";
			callUrl += params.stream().collect(Collectors.joining("&"));
			break;
		case  POST:
			log.info("POST");
			break;
		}
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
		httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
		httpHeaders.add("serviceCode", serviceCode);
		httpHeaders.add("secretKey", secretKey);
		
		JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, method, jsonData);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(resultJson.toString());
		
        String statusCode = "";
        String statusMessage = "";
		
		if (node.has("status")) {
		    statusCode = node.get("status").get("code").asText();
		    statusMessage = node.get("status").get("message").asText();
		}
		
		ApiResponseVO vo = new ApiResponseVO();		
		vo.setResultJson(resultJson);
        vo.setStatusCode(statusCode);
        vo.setStatusMessage(statusMessage);
		    
		if (node.has("data")) {
		    
		    if (node.hasNonNull("data")) {
		        vo.setData(resultJson.getJSONObject("data"));
		    }
		}
		
		log.info("[API][SERVICE][ApiService][callErpApiServer][END]");
		
		return vo;
	}
	
	/**
	 * 파일 업로드
	 * @param request
	 * @return
	 */
	public String saveFiles(MultipartHttpServletRequest request) {
        Map returnMap = new HashMap<>();
        Gson gson = new Gson();
        
        try {
            Iterator<String> iterator       = request.getFileNames();
            
            MultipartFile multipartFile     = null;
            String originalFileName         = null;
            String originalFileExtension    = null;
            String storedFileName           = null;
             
            String filePath                 = "/upload/file/";
            
            if (!UtilManager.isEmptyOrNull(request.getParameter("filePath"))) {
                filePath = URLDecoder.decode(request.getParameter("filePath").toString(), "UTF-8");
            }
            
            String fileFullPath             = systemFilePath + filePath;
            
            File file = new File(fileFullPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            
            List<Map<String,Object>> list   = new ArrayList<Map<String,Object>>();
             
            while (iterator.hasNext()) {
                multipartFile = request.getFile(iterator.next());
                
                if (multipartFile.isEmpty() == false){
                    originalFileName        = URLDecoder.decode(multipartFile.getOriginalFilename(), "UTF-8");
                    originalFileExtension   = originalFileName.substring(originalFileName.lastIndexOf("."));
                    storedFileName          = getRandomString() + originalFileExtension;
                     
                    file                    = new File(fileFullPath + storedFileName);
                    
                    multipartFile.transferTo(file);
                    String makeFilePath         = filePath + storedFileName;
                    Map<String, Object> params  = new HashMap<String,Object>();
                    params.put("oriFileName"    , originalFileName);
                    params.put("filePath"       , makeFilePath);
                    params.put("upFileName"     , storedFileName);
                    params.put("fileSize"       , multipartFile.getSize());
                    
                    list.add(params);
                }
            }
            
            returnMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
            returnMap.put(Constants.RESULT_DATA, list);
        } catch (Exception e) {
            returnMap.put(Constants.RESULT_CODE, Constants.FAIL);
            returnMap.put(Constants.RESULT_MSG, "파일 저장에 실패하였습니다.");
        }
        
        return gson.toJson(returnMap);
    }
	
	public static String getRandomString(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
