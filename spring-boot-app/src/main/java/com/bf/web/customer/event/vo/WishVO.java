package com.bf.web.customer.event.vo;

public class WishVO {
	
	private String seq;
	private String userIdx;
	private String userId;
	private String name;
	private String title;
	private String contents;
	private String tel1;
	private String tel2;
	private String tel3;
	private String wishAmount;
	private String policesAgree;
	private String ip;

	private String phone;					//휴대폰
	private String callRequestDate;			//상담 요청일
	private String callRequestTime;			//상담 요청시간
	
	public String getCallRequestDate() {
		return callRequestDate;
	}
	public void setCallRequestDate(String callRequestDate) {
		this.callRequestDate = callRequestDate;
	}
	public String getCallRequestTime() {
		return callRequestTime;
	}
	public void setCallRequestTime(String callRequestTime) {
		this.callRequestTime = callRequestTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getUserIdx() {
		return userIdx;
	}
	public void setUserIdx(String userIdx) {
		this.userIdx = userIdx;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getTel3() {
		return tel3;
	}
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}
	public String getWishAmount() {
		return wishAmount;
	}
	public void setWishAmount(String wishAmount) {
		this.wishAmount = wishAmount;
	}
	public String getPolicesAgree() {
		return policesAgree;
	}
	public void setPolicesAgree(String policesAgree) {
		this.policesAgree = policesAgree;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WishVO [seq=").append(seq).append(", userIdx=").append(userIdx).append(", userId=")
				.append(userId).append(", name=").append(name).append(", title=").append(title).append(", contents=")
				.append(contents).append(", tel1=").append(tel1).append(", tel2=").append(tel2).append(", tel3=")
				.append(tel3).append(", wishAmount=").append(wishAmount).append(", policesAgree=").append(policesAgree)
				.append(", ip=").append(ip).append("]");
		return builder.toString();
	}
	
}
