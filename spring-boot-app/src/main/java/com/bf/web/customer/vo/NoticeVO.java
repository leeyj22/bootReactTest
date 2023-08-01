package com.bf.web.customer.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
@ToString
public class NoticeVO {
	private int rowNbr;
	private String bbsIdx;
	private int bbsCode;
	private String classification;
	private String searchStr;
	private String subject;
	private String link;
	private String writer;
	private String regDate;
	private int hit;
	private String contents;

	private int curPage;
	private int pageSize;
	private int totalCnt;
	private String tab;
	
	private String startDate;
	private String year;
	private String month;
	private String date;
	
	private int firstLimitIndex;
	private int lastLimitIndex;
	
	private String thumbnail;
	private int upfileIdx;
	private String upFileName;
	private String oriFileName;
	private String fileSize;
	private String fileType;
	private String siteType;
	private String step2;
	private String brodTime;
	
	// 1:1문의
	private String userIdx;
	private String ip;
	private String depth;
	private String classificationName;
	private String ref;
	private int groups;
	private String publishStatus;		// 1:1문의(Q&A) 공개여부
	private String imgFile;				// 1:1문의(Q&A) 이미지
	private String qnaPwd;				// 1:1문의(Q&A) 패스워드
	private String replyComplete;
	private String replyCompleteName;
	private String searchTxtType;		// 검색 타입

}
