package com.bf.web.customer.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqVO {
	
	// 페이징
	private int page;
	private int pageFrom;
	private int pageTo;
	private int firstLimitIndex;
	private int lastLimitIndex;
	
	private int bbsIdx;
	private int bbsCode;
	private String classification;
	private String classification_sub;
	private String subject;
	private String contents;
	private String writer;
	private String regDate;
	private String siteType;
	private int best;
	
}
