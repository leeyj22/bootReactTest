package com.bf.web.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

@Getter
@Setter
@ToString
public class ApiResponseVO {
	JSONObject resultJson;
	String statusCode;
	String statusMessage;
	JSONObject data;
	String message;
}
