package com.bf.web.customer.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerAnalysisHistVO {

	private String cahIdx;
	private String userIdx;
	private String userIp;
	private String menuName;
	private String menuType;
	private int bbsIdx;
	private String url;
	private String refererUrl;
	private String userAgent;
	private String regDate;
	
	private int startRowNum;
	private int totalCnt;

}
