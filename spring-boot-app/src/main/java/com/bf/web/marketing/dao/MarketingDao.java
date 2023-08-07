package com.bf.web.marketing.dao;

import com.bf.web.marketing.mapper.MarketingMapper;
import com.bf.web.marketing.vo.MarketingAgreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MarketingDao {

    @Autowired
    MarketingMapper marketingMapper;

    public int insertMarketingAgree(MarketingAgreeVO vo){
        return marketingMapper.insertMarketingAgree(vo);
    }

    public int checkMarketingAgree(String userIdx){
        return marketingMapper.checkMarketingAgree(userIdx);
    }

}
