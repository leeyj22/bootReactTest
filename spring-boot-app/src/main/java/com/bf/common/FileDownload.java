package com.bf.common;

import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 파일 다운로드 (GET방식 API 호출하여 서버에서 다운)
 * 
 * [고객지원 > 자료실] 다운로드
 * => location.href="/customer/filedownload.bf?document_nm=" + fileName;
 *
 */
public class FileDownload {
    public int filDown(HttpServletRequest request,
            HttpServletResponse response, String filePath, String realFilNm,
            String viewFileNm) throws IOException {
        
        int result = 0; 
        
        File file = new File( filePath + realFilNm);
        if (file.exists() && file.isFile()) {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setContentLength((int) file.length());
            String browser = getBrowser(request);
            String disposition = getDisposition(viewFileNm, browser);
            response.setHeader("Content-Disposition", disposition);
            response.setHeader("Content-Transfer-Encoding", "binary");
            OutputStream out = response.getOutputStream();
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
            result = 1;
            
            if (fis != null)
                fis.close();
            out.flush();
            out.close();
        } else {
            result = 0;
        }
        return result; 
    }

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
            return "MSIE";
        else if (header.indexOf("Chrome") > -1)
            return "Chrome";
        else if (header.indexOf("Opera") > -1)
            return "Opera";
        return "Firefox";
    }

    private String getDisposition(String filename, String browser)
            throws UnsupportedEncodingException {
        String dispositionPrefix = "attachment;filename=";
        String encodedFilename = null;
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll(
                    "\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\""
                    + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\""
                    + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        }
        return dispositionPrefix + encodedFilename;
    }
}
