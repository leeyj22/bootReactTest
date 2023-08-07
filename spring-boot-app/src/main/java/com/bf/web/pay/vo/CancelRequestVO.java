package com.bf.web.pay.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CancelRequestVO {
	String tid;
	String cancelAmt;
	String partialCancelCode;	
	String moid;
	String cancelMsg;
	String mid;
	String merchantKey;
}
