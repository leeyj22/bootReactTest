package com.bf.unierp.myinfo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UnierpMyinfoMapper {

    //SERVICE DB 처리 내역 조회 (가접수)
    public int selectTemporarySCVCountUNIERP(Map params);

    //ERP 카드사 코드 확인
    public Map<String, Object> getCardInfoUnierp (String cardNm);

    //제품리스트 (erp)
    public List<HashMap<String, String>> getGoodsList(String type);

}
