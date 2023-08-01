package com.bf.web.myinfo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Myinfo {

	private String userIdx;
	private int page;
	private int pageFrom;
	private int pageTo;

	// 포인트
	private String ordNbr;
	private String log;
	private int point;
	private String regDate;

	// 수리내역
	private String phone;

	// 주문내역
	private int paymentType;
	private String fromDate;
	private String toDate;
	private String search;

	// 1:1문의
	private String bbsIdx;
	private String subject;
	private String productName;
	private String productType;
	private int classification;
	private String writer;
	private String email;
	private int emailRequest;
	private String cell1;
	private String cell2;
	private String cell3;
	private int smsRequest;
	private String contents;
	private String ip;
	private int groups;
	private String publishStatus;		// 1:1문의(Q&A) 공개여부
	private String imgFile;				// 1:1문의(Q&A) 이미지
	private String qnaPwd;				// 1:1문의(Q&A) 패스워드
	private int reply_complete;
	
	// 개인정보변경
	private String name;
	private String userId;
	private String curpwd;
	private String pwd;
	private String sex;
	private String birthDay;
	private String birthOp;
	private String zip1;
	private String zip2;
	private String addr1;
	private String addr2;
	private String area;
	private String emailState;
	private String tel1;
	private String tel2;
	private String tel3;
	private String smsState;
	private String orderType;
	private String marketingAgree;
	private String accessDate; // 가입일
	
	private String tmState;
	private String snsState;
	private String dmState;
	private String marketingAgreeDate;
	
	//법인회원
	private String companyName;
	private String comTel1;
	private String comTel2;
	private String comTel3;
	private String businessNo1;
	private String businessNo2;
	private String businessNo3;
	private String memberType;

	// 페이징
	private int firstLimitIndex;
	private int lastLimitIndex;
}
