package com.bf.web.pay.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CancelResultVO {
	private String tid;
	private String ResultCode;
	private String ResultMsg;
	private String CancelAmt;
	private String MID;
	private String Moid;
	private String CancelDate;
	private String CancelTime;
	private String CancelNum;
	private String cancelMsg;
	private boolean cancelSuccess;
}
