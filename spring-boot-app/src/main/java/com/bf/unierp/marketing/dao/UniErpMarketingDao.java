package com.bf.unierp.marketing.dao;

import com.bf.unierp.marketing.mapper.UniErpMarketingMapper;
import com.bf.web.marketing.vo.MarketingAgreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UniErpMarketingDao {

    @Autowired
    UniErpMarketingMapper uniErpMarketingMapper;

}
