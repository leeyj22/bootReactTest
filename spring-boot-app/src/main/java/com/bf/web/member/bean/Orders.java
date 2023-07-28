package com.bf.web.member.bean;

public class Orders {
	private String ordNbr;
	private String ordGoods;
	private int amount;
	private int tranState;
	private String regDate;
	private int paymentType;
	private int escrowState;
	private int orderType;
	
	public int getOrderType() {
        return orderType;
    }
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    public String getOrdNbr() {
		return ordNbr;
	}
	public void setOrdNbr(String ordNbr) {
		this.ordNbr = ordNbr;
	}
	public String getOrdGoods() {
		return ordGoods;
	}
	public void setOrdGoods(String ordGoods) {
		this.ordGoods = ordGoods;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getTranState() {
		return tranState;
	}
	public void setTranState(int tranState) {
		this.tranState = tranState;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public int getEscrowState() {
		return escrowState;
	}
	public void setEscrowState(int escrowState) {
		this.escrowState = escrowState;
	}
	
	
}
