package com.bf.unierp.customer.dao;

import com.bf.unierp.customer.mapper.UniErpCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UniErpCustomerDao {

    @Autowired
    UniErpCustomerMapper uniErpCustomerMapper;

    public List<Map<String, Object>> selectMyRentalListUNIERP_info(Map map){
        return uniErpCustomerMapper.selectMyRentalListUNIERP_info(map);
    }

    public Map<String, Object> selectDeliveryInfoUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.selectDeliveryInfoUNIERP(map);
    }

    public int insertHistoryDataUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.insertHistoryDataUNIERP(map);
    }

    public int selectDeliveryAuthUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.selectDeliveryAuthUNIERP(map);
    }

    public Map<String, Object> selectGiftInfoUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.selectGiftInfoUNIERP(map);
    }

    public int updateCustInfoUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.updateCustInfoUNIERP(map);
    }

    public int updateGiftAddrUNIERP(Map<String, Object> map){
        return uniErpCustomerMapper.updateGiftAddrUNIERP(map);
    }

    public void LMSendApi(Map<String, Object> map){
        uniErpCustomerMapper.LMSendApi(map);
    }

    public List<HashMap<String, Object>> checkPurchases(Map params){
        return uniErpCustomerMapper.checkPurchases(params);
    }

    public List<Map<String, String>> selectOdrNumList(Map params){
        return uniErpCustomerMapper.selectOdrNumList(params);
    }

    public List<Map<String, String>> selectOdrNumReviewList(Map params){
        return uniErpCustomerMapper.selectOdrNumReviewList(params);
    }

    public Map<String, String> selectMedicalReviewChkList(Map params){
        return uniErpCustomerMapper.selectMedicalReviewChkList(params);
    }

}
