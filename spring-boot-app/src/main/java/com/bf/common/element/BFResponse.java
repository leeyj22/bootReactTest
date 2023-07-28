package com.bf.common.element;

import com.bf.common.Consts;
import com.bf.common.ResultCodes;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name="Response")
public class BFResponse extends Response {
	
	public BFResponse() {}
	
	public BFResponse(String code) {
		this.setStatusCode(code);
	}
	
	public BFResponse(ResultCodes ecode) {
		this.setStatusCode(ecode.getCode(), ecode.getMessage());
	}
	
	public BFResponse(ResultCodes ecode, Map data) {
		this.setStatusCode(ecode.getCode(), ecode.getMessage());
		this.setData(data);
	}

	public BFResponse(String code, String msg) {
		this.setStatusCode(code, msg);
	}
	
	public BFResponse(String code, Map data) {
		this.setStatusCode(code);
		this.setData(data);
	}

	public BFResponse(String code, String msg, Map data) {
		this.setStatusCode(code, msg);
		this.setData(data);
	}
	
	public String findStatusCode() {
		if (null == this.status) {
			return null;
		}
		return this.status.get("code");
	}

	public String findStatusMsg() {
		if (null == this.status) {
			return null;
		}
		return this.status.get("message");
	}
	
	public void setStatusCode(String code) {
		Map<String, String> status = new HashMap<String, String>();
		status.put("code", code);
		status.put("message", Consts.ReturnStatus.valueOf("C" + code).getMsg());
		this.setStatus(status);
	}
	
	private void setStatusCode(String code, String msg) {
		Map<String, String> status = new HashMap<String, String>();
		status.put("code", code);
		status.put("message", msg);
		this.setStatus(status);
	}
	
	public void removeData() {
		this.data.clear();
	}
}
