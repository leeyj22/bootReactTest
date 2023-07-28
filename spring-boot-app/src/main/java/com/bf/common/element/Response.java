package com.bf.common.element;

import com.bf.common.element.MapAdapter;
import com.bf.common.util.ContextUtil;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;


@XmlRootElement(name="Response")
public class Response {

	protected Map<String, Object> data;
	protected Map<String, String> status;
	
	public Response() {}
	public Response(Map<String, Object> data, Map<String, String> status) {
		this.data 	= data;
		this.status = status;
	}
	
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getStatus() {
		return status;
	}
	public void setStatus(Map<String, String> status) {
		ContextUtil.setAttribute("result", status);
		this.status = status;
	}
}