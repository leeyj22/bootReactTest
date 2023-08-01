package com.bf.web.customer.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BbsGalleryVO {
	
	private String bbsCode;
	private int bbsIdx;
	private String userIdx;
	private String subject;
	private String classification;
	private String listFileName;
	private String writer;
	private String pwd;
	private String hit;
	private String ref;
	private String contents;
	private String commentCnt;
	private String upfileCnt;
	private String userIp;
	private String secret;
	private String uptDate;
	private String regDate;
	private String startDate;
	private String endDate;
	private String banner;
	private String group;
	private String onOffType;
	
	private String rowNbr;
	private String option;
	private String selectCnt;
	private String depth;
	private String originUserIdx;
	private String oriFileName;
	private String upFileName;
	
	private String tab;
	private String mainBanner;
	private String classDate;
	private String classDay;
	
	private int curPage;
	private int pageSize;
	private int totalCnt;
	private int firstLimitIndex;
	private int lastLimitIndex;
	
	private int prevIdx;
	private int nextIdx;
	private String prevSubject;
	private String nextSubject;
	private String prevDate;
	private String nextDate;
	private String commentUseType;
	private String serverTime;
	
	//이벤트 리뷰
	private String eventIdx;			// 이벤트 리뷰 Idx
	private String delState;			// 게시글 상태
	private String giftImgPc;			// 사은품 이미지(PC)
	private String giftImgMobile;		// 사은품 이미지(MOBILE)
	private String winningImgPc;		// 당첨자 후기 이미지(PC)
	private String winningImgMobile;	// 당첨자 후기 이미지(MOBILE)
	private String month;				// 년월(%y년m월)
	private String monthVal;			// 년일(%y-%m)
	private String siteType;			// 사이트 타입
	private String step2;
	private String winnerNoticeInfo;	// 당첨자 발표 정보
	private String eventJoinYm;			// 참여 이벤트 타입(예, 2020-10)
	
	private String link;
	private String thumbnail;
	private String progressType;
	
}
