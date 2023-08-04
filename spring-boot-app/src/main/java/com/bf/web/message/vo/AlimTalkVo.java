package com.bf.web.message.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlimTalkVo {
	private static final String SENDERKEY	= "c36afc5382ed8dd31e07c65e370b210424cb78b4";
	private String tmplCd;
	private String callback;
	private String phone;
	private String subject;
	private String msg;
	private String date;
	private String tranButton;
	private String replaceType;
	private String replaceMsg;
	
	public AlimTalkVo() {
		init();
	}
	
	public AlimTalkVo(String tmplCd, String phone, String msg) {
		init();
		
		this.tmplCd = tmplCd;
		this.phone = phone;
		this.msg = msg;
		this.replaceMsg = msg;
	}
	
	public AlimTalkVo(String tmplCd, String phone, String msg, String tranButton) {
		init();
		
		this.tmplCd = tmplCd;
		this.phone = phone;
		this.msg = msg;
		this.replaceMsg = msg;
		this.tranButton = tranButton;
	}
	
	private void init() {
		this.callback = "0234488980";
		this.subject = "바디프랜드알리미입니다.";
		this.replaceType = "L";
	}

}
