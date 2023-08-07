package com.bf.web.marketing.mapper;

import com.bf.web.marketing.vo.MarketingAgreeVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MarketingMapper {

    //마케팅 동의 확인
    public int insertMarketingAgree(MarketingAgreeVO vo);

    //마케팅 동의 여부 확인
    public int checkMarketingAgree(String userIdx);
}
