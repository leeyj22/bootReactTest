package com.bf.web.pay.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayRequestVO {
    String PayMethod;
    String GoodsName;
    String Amt;
    String MID;
    String Moid;
    String BuyerName;
    String BuyerEmail;
    String BuyerTel;
    String ReturnURL;
    String GoodsCl;
    String TransType;
    String CharSet;
    String ReqReserved;
    String EdiDate;
    String SignData;
    
    public PayRequestVO() {
        super();
    }

    public PayRequestVO(String mID, String moid, String buyerName, String buyerTel) {
        super();
        MID = mID;
        Moid = moid;
        BuyerName = buyerName;
        BuyerTel = buyerTel;
    }
    
}
