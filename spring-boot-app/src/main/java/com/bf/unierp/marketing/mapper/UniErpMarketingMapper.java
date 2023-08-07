package com.bf.unierp.marketing.mapper;

import com.bf.web.marketing.vo.MarketingAgreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UniErpMarketingMapper {

    //이벤트 참여 중복 확인
    public int checkDuplicate(Map<String, Object> params);

    //쿠폰 번호 생성
    public String createSerialNo(String onairMedia);

    //쿠폰 입력
    public Map<String, Object> insertSerialNo(Map<String, Object> params);

    //시리얼 정보 확인
    public Map<String, Object> getSerialInfo(String serialNo);

    //시리얼 사용
    public int useSerial(Map<String, Object> params);

    //시리얼 삭제
    public int deleteSerialNo(Map<String, Object> params);

    //전시장 조회
    public List<Map<String, Object>> getSaleGrpList ();

    //방송 정보 조회
    public Map<String, Object> getOnairInfo(String onairNo);

    //쿠폰별 페이지뷰 조회
    public String getCouponView(String serialNo);

    //사용가능한 전시장 쿠폰 조회
    public List<Map<String, Object>> getShowroomCoupon();

    //쿠폰정보에 홈페이지 전시장체험 접수 아이디 추가
    public int updateSeqOnShowroomCoupon(Map<String, Object> params);

}
