package com.bf.web.pay.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayResultVO {
	// 결제 API 응답 파라미터
	private String authResultCode;
	private String authResultMsg;	
	private String authToken;		
	private String payMethod; // CARD : 신용카드, BANK : 계좌이체, VBANK : 가상계좌, CELLPHONE : 휴대폰결제		
	private String mid;		
	private String moid;			
	private String signature;		
	private String amt;			
	private String reqReserved;	
	private String txTid;			
	private String nextAppURL;		
	private String netCancelURL;
	private boolean paySucces;	
	private String authCode;
	private String authDate;
	
	// 승인 API 응답 파라미터
	private String resultCode; // 3001 : 신용카드 성공코드, 4000 : 계좌이체 성공코드, 4100 : 가상계좌 발급 성공코드, A000 : 휴대폰 소액결제 성공코드, 7001 : 현금영수증
	private String resultMsg;
	private String tid;
	
	// 카드
	private String cardCode;
	private String cardName;
	private String cardNo;
	private String cardQuota;
	private String cardInterest;
	private String acquCardCode;
	private String acquCardName;
	private String cardCl;
	private String ccPartCl;
	private String clickpayCl;
	
	// 가상계좌
	private String vbankBankCode;
	private String vbankBankName;
	private String vbankNum;
	private String vbankExpDate;
	private String vbankExpTime;
	
	// 계좌이체	
	private String bankCode;
	private String bankName;
	private String rcptType;
	private String rcptTID;
	private String rcptAuthCode;
}
