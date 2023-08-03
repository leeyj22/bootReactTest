package com.bf.svc.customer.dao;

import com.bf.svc.customer.mapper.SvcCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SvcCustomerDao {

    @Autowired
    private SvcCustomerMapper svcCustomerMapper;

    // 간편 서비스 인증
    public int selectServiceAuth(Map<String, Object> map) throws Exception {
        return svcCustomerMapper.selectServiceAuth(map);
    }

    // 간편 서비스 상세
    public Map<String, Object> selectServiceInfo(Map<String, Object> map) throws Exception {
        return svcCustomerMapper.selectServiceInfo(map);
    }

}
