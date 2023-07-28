package com.bf.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class Util {
	/**
     * 파라미터 정보 및 세션 정보 설정
     * 
     * @param request 파라미터
     * @return Map 객체
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getParameterMap(HttpServletRequest request) {

        Map pMap = new HashMap();

        // 파라미터 정보 설정
        Map parameterMap = request.getParameterMap();
        Iterator itr = parameterMap.keySet().iterator();

        Enumeration er = request.getParameterNames();		
		while (er.hasMoreElements()) {	
			String key = (String) er.nextElement();
			String value = request.getParameter(key);
			
			pMap.put(key.toString(), unescape(value.toString()));
			
		}
        return pMap;
    }
    
    /**
	 * Html 태그 적용및 개행 처리
	 * 
	 * @param src
	 *            원본 문자열
	 * @return 처리 후 문자열
	 */
	public static String unescape(String src) {
		if (src == null) {
			return null;
		}

		src = src.replaceAll("&quot;", "\"");
		src = src.replaceAll("&lt;", "<");
		src = src.replaceAll("&gt;", ">");
		src = src.replaceAll("&#40;", "(").replaceAll("&#41;", ")");
		src = src.replaceAll("&#38;", "&");
		src = src.replaceAll("&amp;", "&");

		return src;
	}
	
	/**
	 * IP
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String ipaddr(HttpServletRequest request) throws Exception {
		// IP 가져오기
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		    ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		    ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		    ip = request.getHeader("HTTP_CLIENT_IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		    ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		    ip = request.getRemoteAddr(); 
		}
		
		return ip;
	}
	
	public static String getRandomString(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
    /**
     * 해당 String이 null 이거나 length가 0인지 검사
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 텍스트를 HTML 태그로 변환 
     * 
     * @param text
     * @return 
     */
    public static String textToHTML(String text) {
        if(text == null) {
          return null;
        }
        int length = text.length();
        boolean prevSlashR = false;
        StringBuffer out = new StringBuffer();
        for(int i = 0; i < length; i++) {
          char ch = text.charAt(i);
          switch(ch) {
          case '\r':
            if(prevSlashR) {
              out.append("<br>");         
            }
            prevSlashR = true;
            break;
          case '\n':
            prevSlashR = false;
            out.append("<br>");
            break;
          case '"':
            if(prevSlashR) {
              out.append("<br>");
              prevSlashR = false;         
            }
            out.append("&quot;");
            break;
          case '<':
            if(prevSlashR) {
              out.append("<br>");
              prevSlashR = false;         
            }
            out.append("&lt;");
            break;
          case '>':
            if(prevSlashR) {
              out.append("<br>");
              prevSlashR = false;         
            }
            out.append("&gt;");
            break;
          case '&':
            if(prevSlashR) {
              out.append("<br>");
              prevSlashR = false;         
            }
            out.append("&amp;");
            break;
          default:
            if(prevSlashR) {
              out.append("<br>");
              prevSlashR = false;         
            }
            out.append(ch);
            break;
          }
        }
        return out.toString();
      }

    /**
	 * null or "" check
	 * @param value
	 * @return
	 */
	public static boolean isEmptyOrNull(Object value) {
		if (value == null) {
			return true;
		}
		return value.toString().trim().isEmpty();
	}
}
