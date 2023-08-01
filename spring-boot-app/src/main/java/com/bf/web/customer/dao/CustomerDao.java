package com.bf.web.customer.dao;

import com.bf.web.customer.event.vo.BbsGalleryVO;
import com.bf.web.customer.mapper.CustomerMapper;
import com.bf.web.customer.vo.*;
import com.bf.web.myinfo.vo.Myinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao {

    @Autowired
    CustomerMapper customerMapper;

    public List<NoticeVO> selectNoticeList() {
        return customerMapper.selectNoticeList();
    }

    public List<NoticeVO> selectNoticeNormalList(NoticeVO noticeVO) {
        return customerMapper.selectNoticeNormalList(noticeVO);
    }

    public NoticeVO selectNoticeDetail(String idx) {
        return customerMapper.selectNoticeDetail(idx);
    }

    public void updateHitCountForNotice(String idx) {
        customerMapper.updateHitCountForNotice(idx);
    }

    public int insertQna(Myinfo myinfo) {
        return customerMapper.insertQna(myinfo);
    }

    public int updateQna(Myinfo myinfo) {
        return customerMapper.updateQna(myinfo);
    }

    public int getQnaCount() {
        return customerMapper.getQnaCount();
    }

    public int getQnaGroups() {
        return customerMapper.getQnaGroups();
    }

    public List<FaqVO> selectFaqList(FaqVO paramVO) {
        return customerMapper.selectFaqList(paramVO);
    }

    public List<HashMap<String, String>> selectChairFaqListApi(Map params) {
        return customerMapper.selectChairFaqListApi(params);
    }

    public List<NoticeVO> selectChairFaqDetailApi(NoticeVO paramsVO) {
        return customerMapper.selectChairFaqDetailApi(paramsVO);
    }

    public int insertBbsGalleryInfo(Map param) {
        return customerMapper.insertBbsGalleryInfo(param);
    }

    public String selectBbsIdx() {
        return customerMapper.selectBbsIdx();
    }

    public List<HashMap<String, Object>> selectB2bFileList(Map params) {
        return customerMapper.selectB2bFileList(params);
    }

    public List<BbsGalleryVO> selectReferenceList(Map param) {
        return customerMapper.selectReferenceList(param);
    }

    public int selectReferenceListTotalCount(Map param) {
        return customerMapper.selectReferenceListTotalCount(param);
    }

    public NoticeVO selectReferenceDetail(String bbsIdx) {
        return customerMapper.selectReferenceDetail(bbsIdx);
    }

    public List<Map<String, Object>> getReferenceStep2List(String classification) {
        return customerMapper.getReferenceStep2List(classification);
    }

    public List<HomeshoppingVO> get(HomeshoppingVO homeshoppingVO) {
        return customerMapper.get(homeshoppingVO);
    }

    public List<NoticeVO> selectBroadcastInfo(Map pMap) {
        return customerMapper.selectBroadcastInfo(pMap);
    }

    public List<NoticeVO> selectQnaList(NoticeVO noticeVO) {
        return customerMapper.selectQnaList(noticeVO);
    }

    public Map qnaPwdCheck(Myinfo myinfo) {
        return customerMapper.qnaPwdCheck(myinfo);
    }

    public String selectUserIdx(String bbsIdx) {
        return customerMapper.selectUserIdx(bbsIdx);
    }

    public int qnaDelete(String groups) {
        return customerMapper.qnaDelete(groups);
    }

    public List<Myinfo> serviceQnaDetail(int groups) {
        return customerMapper.serviceQnaDetail(groups);
    }

    public List<HashMap<String, String>> selectQnaDetail(Map<String, String> params) {
        return customerMapper.selectQnaDetail(params);
    }

    public Map<String, String> serviceQnaWriteData(Map<String, String> params) {
        return customerMapper.serviceQnaWriteData(params);
    }

    public int insertOnlineService(OnlineVO vo) {
        return customerMapper.insertOnlineService(vo);
    }

    public String getGoodsImgPath(String modelCode) {
        return customerMapper.getGoodsImgPath(modelCode);
    }

    public int insertHistoryData(Map<String, Object> map) {
        return customerMapper.insertHistoryData(map);
    }

    public String selectAreaCode(Map<String, Object> params) {
        return customerMapper.selectAreaCode(params);
    }

    public String selectAreaCodeUNIERP(Map<String, Object> params) {
        return customerMapper.selectAreaCodeUNIERP(params);
    }

    public int everyRConsultInsert(Map params) {
        return customerMapper.everyRConsultInsert(params);
    }

    public int shareConsultInsert(Map params) {
        return customerMapper.shareConsultInsert(params);
    }

    public int consultMappingInsert(Map params) {
        return customerMapper.consultMappingInsert(params);
    }

    public String selectConsultIdx() {
        return customerMapper.selectConsultIdx();
    }

    public int insertUpfile(Map<String, Object> params) {
        return customerMapper.insertUpfile(params);
    }

    public List<HashMap<String, Object>> selectFileList(Map params) {
        return customerMapper.selectFileList(params);
    }

    public int insertVisitPage(Map params) {
        return customerMapper.insertVisitPage(params);
    }

    public void insertCustomerAnalysisHist(CustomerAnalysisHistVO paramVO) {
        customerMapper.insertCustomerAnalysisHist(paramVO);
    }

    public HashMap<String, String> selectTemplate(String tmplCd) {
        return customerMapper.selectTemplate(tmplCd);
    }

    public String getPayOrdNo() {
        return customerMapper.getPayOrdNo();
    }

    public int checkPayOrdNo(String payOrdNo) {
        return customerMapper.checkPayOrdNo(payOrdNo);
    }

    public int insertPayOrdNo(Map<String, Object> params) {
        return customerMapper.insertPayOrdNo(params);
    }

    public String getPayOrdInfo(String payOrdNo) {
        return customerMapper.getPayOrdInfo(payOrdNo);
    }

    public String getServiceIdxFromPayOrdNo(String payOrdNo) {
        return customerMapper.getServiceIdxFromPayOrdNo(payOrdNo);
    }

    public List<Map<String, Object>> search(Map params) {
        return customerMapper.search(params);
    }

    public void insertSearchHistory(Map<String, Object> params) {
        customerMapper.insertSearchHistory(params);
    }

    public List<Map<String, String>> searchCount(Map<String, Object> map) {
        return customerMapper.searchCount(map);
    }

    public List<Map<String, Object>> selectSearchList(Map params) {
        return customerMapper.selectSearchList(params);
    }

    public Map<String, Object> searchDetail(Map params) {
        return customerMapper.searchDetail(params);
    }

    public Map<String, String> selectOdrNumReviewChkList(Map params) {
        return customerMapper.selectOdrNumReviewChkList(params);
    }

}
