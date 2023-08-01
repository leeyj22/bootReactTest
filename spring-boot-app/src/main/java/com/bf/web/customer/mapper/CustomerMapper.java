package com.bf.web.customer.mapper;

import com.bf.web.customer.event.vo.BbsGalleryVO;
import com.bf.web.customer.vo.*;
import com.bf.web.myinfo.vo.Myinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerMapper {

    // 공지사항 리스트
    public List<NoticeVO> selectNoticeList();

    // 공지 리스트
    List<NoticeVO> selectNoticeNormalList(NoticeVO noticeVO);

    public NoticeVO selectNoticeDetail(String idx);

    public void updateHitCountForNotice(String idx);

    // QnA 등록
    public int insertQna(Myinfo myinfo);

    // QnA 수정
    public int updateQna(Myinfo myinfo);

    public int getQnaCount();

    public int getQnaGroups();

    public List<FaqVO> selectFaqList(FaqVO paramVO);

    // FAQ List 안마의자 전용 API
    public List<HashMap<String, String>> selectChairFaqListApi(Map params);

    // FAQ Detail 안마의자 전용 API
    public List<NoticeVO> selectChairFaqDetailApi(NoticeVO paramsVO);

    /**B2B**/
    // DB 등록
    public int insertBbsGalleryInfo(Map param);

    // bbsIdx 조회
    public String selectBbsIdx();

    // 파일 업로드 List 조회
    public List<HashMap<String, Object>> selectB2bFileList(Map params);

    /**자료실**/
    // 자료실 리스트
    public List<BbsGalleryVO> selectReferenceList(Map param);

    // 자료실 건수 조회
    public int selectReferenceListTotalCount(Map param);

    // 자료실 내용 조회
    public NoticeVO selectReferenceDetail(String bbsIdx);

    // 자료실 카테고리 조회
    public List<Map<String, Object>> getReferenceStep2List(String classification);


    /**홈쇼핑**/
    public List<HomeshoppingVO> get(HomeshoppingVO homeshoppingVO);

    // 방송 일정안내
    public List<NoticeVO> selectBroadcastInfo(Map pMap);


    /**QnA**/
    // 1:1 문의
    public List<NoticeVO> selectQnaList(NoticeVO noticeVO);

    // Q&A 게시글  비밀번호 체크
    public Map qnaPwdCheck(Myinfo myinfo);

    // Q&A 게시글 작성자 체크
    public String selectUserIdx(String bbsIdx);

    // Q&A 게시글 삭제
    public int qnaDelete(String groups);

    // Q&A 상세 페이지
    public List<Myinfo> serviceQnaDetail(int groups);

    // Q&A 상세 페이지
    public List<HashMap<String, String>> selectQnaDetail(Map<String, String> params);

    // Q&A 등록
    public Map<String, String> serviceQnaWriteData(Map<String, String> params);

    // 서비스 접수
    public int insertOnlineService(OnlineVO vo);

    // 제품이미지경로(바디프랜드)
    public String getGoodsImgPath(String modelCode);

    // 배송 주소 이력 INSERT
    public int insertHistoryData(Map<String, Object> map);

    // 지역 구분 코드 조회
    public String selectAreaCode(Map<String, Object> params);

    // 지역 구분 코드 조회 UNIERP 형식
    public String selectAreaCodeUNIERP(Map<String, Object> params);

    /**에브리알**/
    // 에브리알 상담접수 (등록)
    public int everyRConsultInsert(Map params);


    /**공유 안마의자**/
    // 공용 안마의자 문의 상담접수 (등록)
    public int shareConsultInsert(Map params);


    // 상담접수 (이미지 맵핑 데이터 등록)
    public int consultMappingInsert(Map params);

    // 상담접수 consultIdx 조회
    public String selectConsultIdx();

    // 이미지 등록
    public int insertUpfile(Map<String, Object> params);

    // 파일 업로드 List 조회
    public List<HashMap<String, Object>> selectFileList(Map params);

    // 방문 페이지 등록
    public int insertVisitPage(Map params);

    //건강수명Plus 관련 로직 삭제

    // 방문 페이지 등록
    public void insertCustomerAnalysisHist(CustomerAnalysisHistVO paramVO);

    // 알림톡 템플릿 코드 조회
    public HashMap<String, String> selectTemplate(String tmplCd);

    // 분해조립 홈페이지 주문번호 채번
    public String getPayOrdNo();

    // 분해조립 홈페이지 주문번호 확인
    public int checkPayOrdNo(String payOrdNo);

    // 분해조립 홈페이지 주문번호 등록
    public int insertPayOrdNo(Map<String, Object> params);

    // 분해조립 홈페이지 주문번호 등록
    public String getPayOrdInfo(String payOrdNo);

    // 분해조립 serviceIdx 조회
    public String getServiceIdxFromPayOrdNo(String payOrdNo);

    // 검색
    public List<Map<String, Object>> search(Map params);

    // 검색 키워드 저장
    public void insertSearchHistory(Map<String, Object> params);

    //검색 카운트
    public  List<Map<String, String>> searchCount(Map<String, Object> map);

    // 검색 리스트
    public List<Map<String, Object>> selectSearchList(Map params);

    // 검색 리스트
    public  Map<String, Object>  searchDetail(Map params);

    // 후기작성 체크
    public Map<String, String> selectOdrNumReviewChkList(Map params);

}
