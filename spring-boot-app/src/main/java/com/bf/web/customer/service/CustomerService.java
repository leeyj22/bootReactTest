package com.bf.web.customer.service;

import com.bf.common.Constants;
import com.bf.common.Consts;
import com.bf.common.RestAdapter;
import com.bf.common.util.AES256Util;
import com.bf.common.util.UtilManager;
import com.bf.svc.customer.dao.SvcCustomerDao;
import com.bf.unierp.customer.dao.UniErpCustomerDao;
import com.bf.web.customer.dao.CustomerDao;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import com.bf.web.message.service.MessageService;
import com.bf.web.myinfo.vo.Myinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomerService{

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private UniErpCustomerDao uniErpCustomerDao;
    @Autowired
    private SvcCustomerDao svcCustomerDao;

    @Autowired
    private MessageService messageService;

    @Value(value = "system.unierp.serviceCode")
    private String uniErpServiceCode;
    @Value(value = "system.unierp.secretKey")
    private String uniErpSecretKey;
    @Value(value = "system.unierp.url")
    private String uniErpUrl;

    private final int LIST_SIZE = 15;
    private final static String aes_key = "bfservicekey!@12";

//    private static final String baseUrl = "https://erp.bodyfriend.co.kr";
//    private static final String secretKey = "34587180942444ee9e21180e6a12e941";

    public List<NoticeVO> selectNoticeList() {
        return customerDao.selectNoticeList();
    }

    public List<NoticeVO> selectNoticeNormalList(NoticeVO noticeVO) {
        return customerDao.selectNoticeNormalList(noticeVO);
    }

    public NoticeVO selectNoticeDetail(String idx) {
        return customerDao.selectNoticeDetail(idx);
    }

    public void updateHitCountForNotice(String idx) {
        customerDao.updateHitCountForNotice(idx);
    }

    public List<FaqVO> selectFaqList(FaqVO paramVO) {
        return customerDao.selectFaqList(paramVO);
    }


    // Q&A 게시글 비밀번호 체크
    public Map qnaPwdCheck(Myinfo myinfo) {
        return customerDao.qnaPwdCheck(myinfo);
    }

    // Q&A 게시글 작성자 체크
    public String selectUserIdx(String bbsIdx) {
        return customerDao.selectUserIdx(bbsIdx);
    }

    // Q&A 게시글 삭제 체크
    public Map qnaDelete(String groups) {

        Map resultMap = new HashMap();
        int result = customerDao.qnaDelete(groups);

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;

    }

    // Q&A 리스트 조회
    public List<NoticeVO> selectQnaList(NoticeVO noticeVO) {
        return customerDao.selectQnaList(noticeVO);
    }

    // Q&A 상세 데이터 조회
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map selectQnaDetail(Map<String, String> params) {

        Map resultMap = new HashMap();

        List<HashMap<String, String>> list = customerDao.selectQnaDetail(params);

        for(HashMap<String, String> map : list){
            map.put("contents",map.get("contents").replace("\n", "<br>"));
        }

        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    // Q&A 수정 페이지 기존 데이터 조회
    public Map<String, String> serviceQnaWriteData(Map<String, String> params) {
        return customerDao.serviceQnaWriteData(params);
    }

    // 1:1문의 등록
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map insertQna(Myinfo myinfo) throws Exception {
        Map resultMap = new HashMap();

        int groups = customerDao.getQnaGroups();
        myinfo.setGroups(groups);

        int result = customerDao.insertQna(myinfo);

        if (result == 1) {

            /****************************
             * AlimTalk Message Send API
             ****************************/
            // 알림톡 파라미터
            org.json.JSONObject params = new org.json.JSONObject();
            String phone = myinfo.getCell1() + myinfo.getCell2() + myinfo.getCell3();
            params.put("#{고객명}"	, myinfo.getWriter());	// 알림톡 발송 고객명
            params.put("celNo"		, phone);				// 알림톡 발송 연락처
            params.put("templateCode"		, "body_receipt_01");	// 알림톡 발송 템플릿 코드
            params.put("#{서비스명}", "1:1문의하기");			// 알림톡 발송 템플릿 코드

            // 고객 알림톡 API 호출
            if (!"".equals(phone)) {
                messageService.sendAlimTalk(params);
            }
        }

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    // 1:1문의 수정
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map updateQna(Myinfo myinfo) {
        Map resultMap = new HashMap();

        int result = customerDao.updateQna(myinfo);

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    /**
     * 제품 및 이전/설치 접수 제품 리스트 조회
     *
     * @param request
     * @return
     * @throws Exception
     */
    // erp 구매&렌탈 이력
    public List<Map<String, Object>> selectMyRentalList_Inst(HttpServletRequest request) throws Exception {
        Map params = new HashMap();

        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));
//        params.put("startDate", request.getParameter("startDate"));
//        params.put("endDate", request.getParameter("endDate"));
//        params.put("type", request.getParameter("type"));

        List<Map<String, Object>> list = uniErpCustomerDao.selectMyRentalListUNIERP_info(params);
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                list.get(i).put("imgPath", customerDao.getGoodsImgPath(String.valueOf(list.get(i).get("modelCode"))));
            }
        }

        return list;
    }

    // 간편배송 인증 - UNIERP
    public int selectDeliveryAuthUNIERP(Map<String, Object> params) throws Exception {

        AES256Util aes256 = new AES256Util(aes_key);

        params.put("telNumber", aes256.enc(params.get("telNumber").toString()));

        System.out.println("======================== selectDeliveryAuthUNIERP");
        System.out.println(params.toString());
        System.out.println("========================");

        return uniErpCustomerDao.selectDeliveryAuthUNIERP(params);
    }

    // 간편배송 상세
    public Map<String, Object> selectDeliveryInfoUNIERP(Map<String, Object> map) throws Exception {

        AES256Util aes256 = new AES256Util(aes_key);

        String telNum    = (String)map.get("telNum");
        String telNo     = "";
        String hphoneNo  = "";
        String gisaName  = "";
        String gisaTelNo = "";
        String dueDate   = "";
        String custName   = "";
        String maskTel   = "";
        String maskHTel   = "";
        String maskName   = "";

        map.put("telNum", aes256.enc(telNum));

        Map<String, Object> resultMap = uniErpCustomerDao.selectDeliveryInfoUNIERP(map);

        if(resultMap != null && (String)resultMap.get("TEL_NO") == null){
            telNo = "";
            resultMap.put("TEL_NO", telNo);
        }else if(resultMap != null && (String)resultMap.get("TEL_NO") != null){
            telNo    = aes256.dec((String)resultMap.get("TEL_NO"));
            maskTel = UtilManager.setMaskProcess(telNo , "tel");
            resultMap.put("TEL_NO", maskTel);
        }

        if(resultMap != null && (String)resultMap.get("HPHONE_NO") == null){
            hphoneNo = "";
            resultMap.put("HPHONE_NO", hphoneNo);
        }else if(resultMap != null && (String)resultMap.get("HPHONE_NO") != null){
            hphoneNo = aes256.dec((String)resultMap.get("HPHONE_NO"));
            maskHTel = UtilManager.setMaskProcess(hphoneNo , "tel");
            resultMap.put("HPHONE_NO", maskHTel);
        }

        if(resultMap != null && (String)resultMap.get("DUE_DATE") == null){
            dueDate = "";
        }else if(resultMap != null && (String)resultMap.get("DUE_DATE") != null){
            dueDate = (String) resultMap.get("DUE_DATE").toString().replaceAll("-", ".");
        }

        if(resultMap != null && (String)resultMap.get("GISA_TELNO") == null){
            gisaTelNo = "";
            resultMap.put("GISA_TELNO", gisaTelNo);
        }else if(resultMap != null && (String)resultMap.get("GISA_TELNO") != null){
            gisaTelNo = aes256.dec((String)resultMap.get("GISA_TELNO"));
            resultMap.put("GISA_TELNO", gisaTelNo);
        }

        if(resultMap != null && (String)resultMap.get("GISA_NAME") == null){
            gisaName = "";
        }else if(resultMap != null && (String)resultMap.get("GISA_NAME") != null){
            gisaName = (String) resultMap.get("GISA_NAME");
            resultMap.put("GISA_NAME", gisaName);
        }
        if(resultMap != null && (String)resultMap.get("CUST_NAME") != null){
            custName = resultMap.get("CUST_NAME").toString();
            maskName =  UtilManager.setMaskProcess(custName , "name");
            resultMap.put("CUST_NAME", maskName);
        }

        map.put("EXPECT_DATE", dueDate);

        return resultMap;
    }

    // 배송 주소 이력 INSERT
    public int insertHistoryDataUNIERP(Map<String, Object> map) throws Exception {
        return uniErpCustomerDao.insertHistoryDataUNIERP(map);
    }

    // 배송일 지정 및 주소변경 UNIERP
    public int updateCustInfoUNIERP(Map<String, Object> map) throws Exception {
        return uniErpCustomerDao.updateCustInfoUNIERP(map);
    }

    // 배송일 지정,주소 및 사은품 주소 변경
    public int updateGiftAddrUNIERP(Map<String, Object> map) throws Exception {
        return uniErpCustomerDao.updateGiftAddrUNIERP(map);
    }

    //사은품 관련 정보
    public Map<String, Object> selectGiftInfoUNIERP(Map<String, Object> map) {
        return uniErpCustomerDao.selectGiftInfoUNIERP(map);
    }

    // 배송 주소 물류대장 전송 api sp 호출용
    public void LMSendApi(Map<String, Object> map) throws Exception {
        uniErpCustomerDao.LMSendApi(map);
    }

    // 배송일 제외 조회
    public org.json.JSONArray selectExceptDay(HttpServletRequest request) throws Exception {
        org.json.JSONArray resList = null;

        // T=이전설치, A=분해조립
        String holidayType = request.getParameter("holidayType");
        if (UtilManager.isEmptyOrNull(holidayType)) {
            holidayType = "T";
        }

        String serviceCode = uniErpServiceCode;
        String secretKey = uniErpSecretKey;
        String apiUrl = uniErpUrl;
        String callUrl = apiUrl + "/api/v2/common/holiday";
        String param = "?holidayType="+holidayType;
        callUrl += param;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
        httpHeaders.add("serviceCode", serviceCode);
        httpHeaders.add("secretKey", secretKey);

        org.json.JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.GET, "");

        log.info("[CUSTOMER][SERVICE][customerService][selectExceptDay][jsonResult] : {}", resultJson);

        if (resultJson.getJSONObject("status").get("code").equals("200")) {
            resList = resultJson.getJSONObject("data").getJSONArray("holidayList");
        }
        return resList;
    }

    // 간편 배송 조회 인증 리스트
    public List<Map<String, String>> selectOdrNumList(Map params) {
        return uniErpCustomerDao.selectOdrNumList(params);
    }

    // 후기작성 인증 리스트
    public List<Map<String, String>> selectOdrNumReviewList(Map params) {

        Map<String, String> Chk2 = new HashMap<String, String>();
        List<Map<String, String>> list = uniErpCustomerDao.selectOdrNumReviewList(params);
        int i = 0;

        // 후기 작성 갯수 체크
        for (Map<String, String> Chk : list) {

            Chk.put("ORDER_NO", list.get(i).get("ORDER_NO"));
            Chk2 = customerDao.selectOdrNumReviewChkList(Chk);
            Chk.put("cnt", String.valueOf(Chk2.get("cnt")));
            i++;

        }

        int j = 0;
        // 후기 작성 의료기기 체크
        for (Map<String, String> Chk : list) {

            Chk.put("GOODS_CD", list.get(j).get("GOODS_CD"));
            Chk2 = uniErpCustomerDao.selectMedicalReviewChkList(Chk);
            if(String.valueOf(Chk2.get("mCnt")).equals("0")){
                Chk.put("medical","N");
            }else {
                Chk.put("medical","Y");
            }
            j++;
        }
        return list;
    }


    // 사랑체 실고객 ERP DB 체크
    public Map<String, Object> checkPurchases(HttpServletRequest request) throws Exception {
        Map<String, Object> resMap = new HashMap<String, Object>();

        int result = 0;
        Map params = new HashMap();
        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));

        String custInfo = params.get("name")+ ", " + params.get("phone");
        List<HashMap<String, Object>> list = uniErpCustomerDao.checkPurchases(params);
        result = list.size();

        if(null!=request.getParameter("funnel") && "membership".equals(request.getParameter("funnel"))) {
            result = 1;
            custInfo = "C";
        }
        log.info("[CUSTOMER][SERVICE][CustomerService][checkPurchases][result] {}"	, custInfo + ", 구매건 수 : " + result);

        if (result > 0) {
            resMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
            resMap.put(Constants.RESULT_MSG, "체크 완료");
        } else {
            resMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resMap.put(Constants.RESULT_MSG, "확인 불가");
        }
        return resMap;
    }

    // 간편 서비스 인증
    public int selectServiceAuth(Map<String, Object> params) throws Exception {

        AES256Util aes256 = new AES256Util(aes_key);
        params.put("telNumber", aes256.enc(params.get("telNumber").toString()));
        log.info("[CUSTOMER][SERVICE][CustomerService][selectServiceAuth][params] {}"	, params.toString());

        return svcCustomerDao.selectServiceAuth(params);
    }

    // 간편 서비스 조회 상세화면
    public Map<String, Object> selectServiceInfo(Map<String, Object> map) throws Exception {

        AES256Util aes256 = new AES256Util(aes_key);

        String telNum    = (String)map.get("telNum");
        String telNo     = "";
        String hphoneNo  = "";
        String gisaName  = "";
        String gisaTelNo = "";
        String dueDate   = "";

        Map<String, Object> resultMap = svcCustomerDao.selectServiceInfo(map);
        System.out.println("[CustomerService][selectServiceInfo] Select Data... ");

        if(resultMap != null && (String)resultMap.get("TEL_NO") == null){
            telNo = "";
            resultMap.put("TEL_NO", telNo);
        }else if(resultMap != null && (String)resultMap.get("TEL_NO") != null){
            telNo    = aes256.dec((String)resultMap.get("TEL_NO"));
            resultMap.put("TEL_NO", telNo);
        }

        if(resultMap != null && (String)resultMap.get("HPHONE_NO") == null){
            hphoneNo = "";
            resultMap.put("HPHONE_NO", hphoneNo);
        }else if(resultMap != null && (String)resultMap.get("HPHONE_NO") != null){
            hphoneNo = aes256.dec((String)resultMap.get("HPHONE_NO"));
            resultMap.put("HPHONE_NO", hphoneNo);
        }

        if(resultMap != null && (String)resultMap.get(", info.ENGINEER_CHARGE") == null){
            gisaTelNo = "";
            resultMap.put("GISA_TELNO", gisaTelNo);
        }else if(resultMap != null && (String)resultMap.get("GISA_TELNO") != null){
            gisaTelNo = aes256.dec((String)resultMap.get("GISA_TELNO"));
            resultMap.put("GISA_TELNO", gisaTelNo);
        }

        if(resultMap != null && (String)resultMap.get("GISA_NAME") == null){
            gisaName = "";
        }else if(resultMap != null && (String)resultMap.get("GISA_NAME") != null){
            gisaName = (String) resultMap.get("GISA_NAME");
            resultMap.put("GISA_NAME", gisaName);
        }
        map.put("EXPECT_DATE", dueDate);

        return resultMap;
    }

}
