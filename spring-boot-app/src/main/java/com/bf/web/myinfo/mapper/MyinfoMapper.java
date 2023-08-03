package com.bf.web.myinfo.mapper;

import com.bf.web.marketing.vo.MarketingAgreeVO;
import com.bf.web.myinfo.vo.Myinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MyinfoMapper {

    public int getCoupon(Myinfo myinfo);

    public int getCouponCount(Myinfo myinfo);

    public List<HashMap<String, String>> selectCouponList(Myinfo myinfo);

    public int getPoint(Myinfo myinfo);

    public int getPointCount(Myinfo myinfo);

    public List<HashMap<String, String>> selectPointList(Myinfo myinfo);

    /* 렌탈 개수 조회 */
    public int getRentalOrderCount(Map<String, Object> params);

    /* 주문/배송 조회 목록수 */
    public int getOrderCount(Map<String, Object> params);

    /* 주문/배송 조회 */
    public List<HashMap<String, Object>> selectOrderList(Map<String, Object> params);

    /** 주문 내역 상세 조회 */
    public List<HashMap<String, Object>> selectOrderDetail(Map<String, Object> params);

    /* 주문/배송 내역 옵션 조회 */
    public List<Map<String, Object>> selectOptionList(HashMap<String, Object> orderInfo);

    /* 취소/반품/교환 조회 목록수 */
    public int getCancelOrderCount(Map<String, Object> params);

    /* 취소/반품/교환 조회 */
    public List<HashMap<String, Object>> selectCancelOrderList(Map<String, Object> params);

    public int getQnaCount(Myinfo myinfo);

    public List<HashMap<String, String>> selectQnaList(Myinfo myinfo);

    public int getQnaGroups();

    public int insertQna(Myinfo myinfo);

    public HashMap<String, String> selectPersonalList(Myinfo myinfo);

    public Myinfo getMyinfo(Myinfo myinfo);

    public int updatePersonal(Myinfo myinfo);

    public List<HashMap<String, String>> selectQnaDetail(Myinfo myinfo);


    public List<HashMap<String, String>> getNomemberOrderList(Myinfo myinfo);

    // 주문상태 LOG 등록
    public int insertStatusLog(Map<String, Object> params);

    // 주문상태 업데이트
    public int updateTranState(Map<String, Object> params);

    // 쿠폰 확인
    public HashMap<String, String> selectCouponConfirm(String number);

    // 쿠폰 정보 수정
    public int updateCouponInfo(Map params);

    // 쿠폰 발급 27
    public void insertCouponAdd(Map params);

    // 쿠폰 발급 28
    public void insertCouponAdd2(Map params);

    // 쿠폰 발급 29
    public void insertCouponAdd3(Map params);

    // 쿠폰 중복 체크
    public HashMap<String, String> selectCouponYNChk(String number);

    public List<HashMap<String, String>> selectAfterService(String serviceIdx);

    //본인인증 업데이트
    public int updateMemberCertYn(Map<String, Object> params);

    //본인인증 여부 확인
    public HashMap<String, String> checkCertify(String userId);

//    //erp 구매&렌탈 이력
//    public List<HashMap<String, String>> selectMyRentalList(Map map){
//        return sqlSessionERP.selectList("MyinfoDao.selectMyRentalList", map);
//    }
//
//    //erp 구매&렌탈 이력(사용자만 조회)
//    public List<HashMap<String, String>> selectMyRentalList_Inst(Map map){
//        return sqlSessionERP.selectList("MyinfoDao.selectMyRentalList_Inst", map);
//    }

    //erp 구매&렌탈 이력(후기 제품조회)
    public List<HashMap<String, String>> getReviewWriteInfo(Map subParams);

    //제품이미지경로(바디프랜드)
    public String getGoodsImgPath(String modelCode);

//    //제품리스트 (erp)
//    public List<HashMap<String, String>> getGoodsList(String type){
//        return sqlSessionERP.selectList("MyinfoDao.getGoodsList", type);
//    }

    //SERVICE DB 처리 내역 조회 (가접수)
    public int selectTemporarySCVCount(Map params);


    //멤버십앱 회원정보 업데이트를 위한 정보 조회
    public Myinfo getUserInfoForMembership (String userIdx);

//    // 렌탈약정서 다운로드 정보 조회 (userkey, regdate)
//    public Map<String, Object> getRentalDownloadData (String orderIdx) {
//        return sqlSessionShowroom.selectOne("MyinfoDao.selectRentalDownLoadData", orderIdx);
//    }

    // 회원 마케팅 동의 전체 업데이트
    public int updateMemberMarketingAgreeAll (MarketingAgreeVO vo);


}
