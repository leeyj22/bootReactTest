package com.bf.web.customer.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnlineVO {

	private String serviceIdx;
	private String name;
	private String contant;
	private String zip;
	private String addr1;
	private String addr2;
	private String prdtCate;
	private String prdtName;
	private String serviceGroup;
	private String serviceGroupOther;
	private String prdtShop;
	private String prdtShopOther;
	private String content;
	private String userId;
	private String userIdx;
	private String custCode;
	private String mainType;		// 홈페이지 기준 사이트 구분
	private String erpSiteType; 	// UNIERP 기준 사이트 구분
	
	private String certUserName;
	private String certUserContact;
	
}
