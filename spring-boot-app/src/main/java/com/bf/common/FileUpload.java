package com.bf.common;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUpload {
    
    private static Logger LOGGER = LoggerFactory.getLogger(FileUpload.class);
    
	public static int fileUpload(MultipartFile fileData, String path, String fileName) throws IOException {
		
		int result = 0;
		
		String originalFileName = fileData.getOriginalFilename();
		String contentType = fileData.getContentType();
		long fileSize = fileData.getSize();
		
		LOGGER.info("fileName : {}", fileName);
		LOGGER.info("originalFileName : {}", originalFileName);
		LOGGER.info("path : {}", path);
		LOGGER.info("contentType : {}", contentType);
		LOGGER.info("fileSize : {}", fileSize);
		
		InputStream is = null;
		OutputStream out = null;
		try {
			if (fileSize > 0) {
				is = fileData.getInputStream();
				File realUploadDir = new File(path);
				if (!realUploadDir.exists()) {             //경로에 폴더가 존재하지 않으면 생성합니다.
					realUploadDir.mkdirs();
				}
				out = new FileOutputStream(path +"/"+ fileName);
				FileCopyUtils.copy(is, out);            //InputStream에서 온 파일을 outputStream으로 복사
				
				result = 1;
			}else{
				result = 0;
				new IOException("잘못된 파일을 업로드 하셨습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = 2;
			new IOException("파일 업로드에 실패하였습니다.");
		}finally{
			if(out != null){out.close();}
			if(is != null){is.close();}
		}
		
		return result;
	}
	
	public static String fileUpload(MultipartFile fileData, String path) throws IOException {
		String type = fileData.getOriginalFilename().split("\\.")[1];
		String randomName = RandomStringUtils.random(31, true, true);
		int result = FileUpload.fileUpload(fileData
				, Constants.File.UPLOAD_PATH + path, randomName+"."+type);
		if(result==1) {
			return randomName+"."+type;
		} else {
			return "";
		}
	}
	
	public static void deleteFile(String path){
		File file = new File(Constants.File.UPLOAD_PATH+path);
		if(file.exists()) {
			file.delete();
		}
	}
	
	public static Map<String,Object> returnfileUploadMap(MultipartFile fileData, String path, String fileName) throws IOException {
		String originalFileName = fileData.getOriginalFilename();
		String contentType = fileData.getContentType();
		long fileSize = fileData.getSize();
		
		LOGGER.info("fileName : {}", fileName);
        LOGGER.info("originalFileName : {}", originalFileName);
        LOGGER.info("path : {}", path);
        LOGGER.info("contentType : {}", contentType);
        LOGGER.info("fileSize : {}", fileSize);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fileName", fileName);
		resultMap.put("originalFileName", originalFileName);
		resultMap.put("path",path);
		resultMap.put("fileSize", fileSize);
		
		return resultMap;
	}

}
