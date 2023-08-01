package com.bf.web.marketing.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketingAgreeVO {

    private String userIdx;
    private String name;
    private String phone;
    private int agreeStatus;
    private int refPageIdx;
    
}
