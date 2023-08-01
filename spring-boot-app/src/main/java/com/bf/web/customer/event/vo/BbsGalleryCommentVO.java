package com.bf.web.customer.event.vo;

public class BbsGalleryCommentVO {

	private int commentIdx;
	private int bbsCode;
	private int bbsIdx;
	private int userIdx;
	private String writer;
	private String pwd;
	private String comment;
	private String ip;
	private String regDate;
	private int rowNbr;
	private String tab;
	private String commentUseType;
	

	public String getCommentUseType() {
		return commentUseType;
	}

	public void setCommentUseType(String commentUseType) {
		this.commentUseType = commentUseType;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public int getRowNbr() {
		return rowNbr;
	}

	public void setRowNbr(int rowNbr) {
		this.rowNbr = rowNbr;
	}

	public int getCommentIdx() {
		return commentIdx;
	}

	public void setCommentIdx(int commentIdx) {
		this.commentIdx = commentIdx;
	}

	public int getBbsCode() {
		return bbsCode;
	}

	public void setBbsCode(int bbsCode) {
		this.bbsCode = bbsCode;
	}

	public int getBbsIdx() {
		return bbsIdx;
	}

	public void setBbsIdx(int bbsIdx) {
		this.bbsIdx = bbsIdx;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
}
