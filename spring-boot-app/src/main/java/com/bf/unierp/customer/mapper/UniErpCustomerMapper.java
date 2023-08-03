package com.bf.unierp.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UniErpCustomerMapper {

    // UNIERP 구매&렌탈 이력
    public List<HashMap<String, String>> selectMyRentalListUNIERP_info(Map map);

    // 간편배송 상세
    public Map<String, Object> selectDeliveryInfoUNIERP(Map<String, Object> map);

    // 배송 주소 이력 INSERT
    public int insertHistoryDataUNIERP(Map<String, Object> map);

    // 간편배송 인증
    public int selectDeliveryAuthUNIERP(Map<String, Object> map);

    //사은품 관련 정보
    public Map<String, Object> selectGiftInfoUNIERP(Map<String, Object> map);

    // 배송일 지정 및 주소변경 UNIERP
    public int updateCustInfoUNIERP(Map<String, Object> map);

    //배송일 지정,주소 및 사은품 주소 변경
    public int updateGiftAddrUNIERP(Map<String, Object> map);

    //배송 주소 물류대장 전송 api sp 호출용
    public void LMSendApi(Map<String, Object> map);

    // 사랑체 실고객 ERP DB 체크
    public List<HashMap<String, Object>> checkPurchases(Map params);

    // 간편 배송 조회 인증 리스트
    public List<Map<String, String>> selectOdrNumList(Map params);

    // 후기작성 인증 리스트
    public List<Map<String, String>> selectOdrNumReviewList(Map params);

    // 후기작성 의료기기 체크
    public Map<String, String> selectMedicalReviewChkList(Map params);

}
