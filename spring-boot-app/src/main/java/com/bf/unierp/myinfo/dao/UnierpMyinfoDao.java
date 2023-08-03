package com.bf.unierp.myinfo.dao;

import com.bf.unierp.myinfo.mapper.UnierpMyinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UnierpMyinfoDao {

    @Autowired private UnierpMyinfoMapper unierpMyinfoMapper;

    public List<HashMap<String, String>> selectMyRentalListUNIERP_info(Map map){
        return unierpMyinfoMapper.selectMyRentalListUNIERP_info(map);
    }

    public int selectTemporarySCVCountUNIERP(Map params){
        return unierpMyinfoMapper.selectTemporarySCVCountUNIERP(params);
    }

    public Map<String, Object> getCardInfoUnierp (String cardNm){
        return unierpMyinfoMapper.getCardInfoUnierp(cardNm);
    }

}
