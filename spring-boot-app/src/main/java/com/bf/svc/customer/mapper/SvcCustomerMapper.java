package com.bf.svc.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface SvcCustomerMapper {

    // 간편 서비스 인증
    public int selectServiceAuth(Map<String, Object> map);

    // 간편 서비스 상세
    public Map<String, Object> selectServiceInfo(Map<String, Object> map);

}
