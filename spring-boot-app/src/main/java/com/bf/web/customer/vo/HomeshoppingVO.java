package com.bf.web.customer.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HomeshoppingVO {

	private int bbsCode;
	private String subject;
	private String writer;
	private String contents;
	private String uptDate;
	private String regDate;
	private String selectedMonth;
	
	private int year;
	private int month;
	private int date;
	
	private int top;
	
}
