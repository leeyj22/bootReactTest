package com.bf.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RequestContext {
	private static final ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> RESPONSE = new ThreadLocal<HttpServletResponse>();
	private static final ThreadLocal<Map<String, String>> CONTEXT = new ThreadLocal<Map<String, String>>(); 
	
	public static HttpServletRequest getRequest() {
		return REQUEST.get();
	}
	
	public static HttpServletResponse getResponse() {
		return RESPONSE.get();
	}
	
	public static Map<String, String> getContext() {
		return CONTEXT.get();
	}
	
	public static void setRequest(HttpServletRequest request) {
		REQUEST.set(request);
	}
	
	public static void setResponse(HttpServletResponse response) {
		RESPONSE.set(response);
	}
	
	public static void setContext(Map<String, String> context) {
		CONTEXT.set(context);
	}
	
	public static String getProtocol() {
		if (CONTEXT.get() == null) {
			return "";
		}
		return CONTEXT.get().get("protocol");
	}
}
