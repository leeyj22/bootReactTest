package com.bf.common.util;

import com.bf.common.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ContextUtil {
	public static HttpServletRequest getRequest() {
		return RequestContext.getRequest();
	}
	
	public static HttpSession getSession() {
		HttpServletRequest request = ContextUtil.getRequest();
		if (request == null) return null;
		return request.getSession();
	}
	
	public static Object getAttribute(String name) {
		HttpServletRequest request = ContextUtil.getRequest();
		if (request == null) return null;
		return request.getAttribute(name);
	}
	
	public static void setAttribute(String name, Object obj) {
		HttpServletRequest request = ContextUtil.getRequest();
		if (request == null) return;
		request.setAttribute(name, obj);
	}
	
	public static void setAttribute(String name, String key, String value) {
		HttpServletRequest request = ContextUtil.getRequest();
		if (request == null) return;
		Object obj = request.getAttribute(name);
		if (obj == null) {
			obj = new HashMap();
		}
		((Map)obj).put(key, value);
		request.setAttribute(name, obj);
	}
	
	public static Object getParameter(String name) {
		HttpServletRequest request = ContextUtil.getRequest();
		if (request == null) return null;
		return request.getParameter(name);
	}

	private static final PathMatchingResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

	public static Resource getResource(String location ) {
		return RESOURCE_RESOLVER.getResource( location );
	}

	public static Resource[] getResources( String locationPattern ) throws IOException {
		return RESOURCE_RESOLVER.getResources( locationPattern );
	}

}
