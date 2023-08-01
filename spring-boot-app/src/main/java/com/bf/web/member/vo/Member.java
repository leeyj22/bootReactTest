package com.bf.web.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {

    private String orderNum;
    private String sessionId;
    private String findType;

    private String userIdx;
    private String levelCode;
    private String levelName;
    private String userId;
    private String pwd;
    private String name;
    private String nick;
    private String realNameCertType;
    private String realNameCert;
    private String sex;
    private String birthDay;
    private String birthOp;
    private String tel1;
    private String tel2;
    private String tel3;
    private String cell1;
    private String cell2;
    private String cell3;
    private String email;
    private String zip1;
    private String zip2;
    private String addr1;
    private String addr2;
    private String area;
    private String emailState;
    private String smsState;
    private String marriedDay;
    private String married;
    private String visitDate;
    private String visitCnt;
    private String ip;
    private String approval;
    private String purchasePrice;
    private String point;
    private String deposit;
    private String comName;
    private String comCeo;
    private String comNo1;
    private String comNo2;
    private String comNo3;
    private String comPost1;
    private String comPost2;
    private String comAddr1;
    private String comAddr2;
    private String comTel1;
    private String comTel2;
    private String comTel3;
    private String comFax1;
    private String comFax2;
    private String comFax3;
    private String comType;
    private String comKind;
    private String comHome;
    private String etc;
    private String accessDate;
    private String phone;
    private String company;
    private String type;
    private String marketingAgree;	//마케팅 수신 동의(1) 거부(0)

    // 휴대폰 인증
    private String certMet;
    private String tr_url;
    private String tr_add;
    private String returnUrl;

    private String joinType;
    private String businessNo1;
    private String businessNo2;
    private String businessNo3;
    private String department;
    private String companyName;
    private String phoneNo;
    private String authorizationIp;
    private String membershipYn;

}
