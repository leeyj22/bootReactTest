package com.bf.web.customer.service;

import com.bf.common.*;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.AES256Util;
import com.bf.common.util.UtilManager;
import com.bf.svc.customer.dao.SvcCustomerDao;
import com.bf.unierp.customer.dao.UniErpCustomerDao;
import com.bf.unierp.marketing.dao.UniErpMarketingDao;
import com.bf.unierp.marketing.mapper.UniErpMarketingMapper;
import com.bf.unierp.myinfo.dao.UnierpMyinfoDao;
import com.bf.web.api.service.ApiService;
import com.bf.web.api.vo.ApiResponseVO;
import com.bf.web.customer.dao.CustomerDao;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import com.bf.web.customer.vo.OnlineVO;
import com.bf.web.marketing.service.MarketingService;
import com.bf.web.marketing.vo.MarketingAgreeVO;
import com.bf.web.message.service.MessageService;
import com.bf.web.myinfo.dao.MyinfoDao;
import com.bf.web.myinfo.vo.Myinfo;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

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
    @Autowired
    private ApiService apiService;
    @Autowired
    private UnierpMyinfoDao unierpMyinfoDao;
    @Autowired
    private MyinfoDao myinfoDao;
    @Autowired
    private MarketingService marketingService;

    @Value(value = "system.unierp.serviceCode")
    private String uniErpServiceCode;
    @Value(value = "system.unierp.secretKey")
    private String uniErpSecretKey;
    @Value(value = "system.unierp.url")
    private String uniErpUrl;
    @Value(value = "system.host.name")
    private String hostName;

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

    public List<HashMap<String, String>> selectChairFaqListApi(Map params) {
        return customerDao.selectChairFaqListApi(params);
    }

    public List<NoticeVO> selectChairFaqDetailApi(NoticeVO paramsVO) {
        return customerDao.selectChairFaqDetailApi(paramsVO);
    }

    public int insertBbsGalleryInfo(Map param) {
        return customerDao.insertBbsGalleryInfo(param);
    }

    public String selectBbsIdx() {
        return customerDao.selectBbsIdx();
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
    public int insertHistoryData(Map<String, Object> map) throws Exception {
        return customerDao.insertHistoryData(map);
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

    // 구역 코드 조회 UNIERP
    public String selectAreaCodeUNIERP(Map<String, Object> params) throws Exception {
        return customerDao.selectAreaCodeUNIERP(params);
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

    /**
     * 서비스 접수 상세 조회
     * @param encSeq
     * @return
     * @throws BFException
     */
    public Response getMyAfterService(HttpServletRequest request, String encSeq) throws BFException {

        log.info("[CUSTOMER][SERVICE][customerService][getMyAfterService][START]");

        BFResponse res = null;
        try {
            String custName = request.getParameter("custName").trim();
            String phoneNum = request.getParameter("phoneNum").trim();
            String seq = UtilManager.decAES(encSeq);

            log.info("[CUSTOMER][SERVICE][customerService][getMyAfterService][seq] : {}", seq);

            if (!UtilManager.isEmptyOrNull(seq)) {

                // ERP API 설정
                String serviceCode = uniErpServiceCode;
                String secretKey = uniErpSecretKey;
                String apiUrl = uniErpUrl;
                String callUrl = apiUrl + "/api/v1/service/info";

                String callParams = "?";
                callParams += "serviceIdx=" + seq;
                callUrl += callParams;

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
                httpHeaders.add("serviceCode", serviceCode);
                httpHeaders.add("secretKey", secretKey);

                org.json.JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.GET, "");
                org.json.JSONObject dataJson = resultJson.getJSONObject("data");
                org.json.JSONObject statusJson = resultJson.getJSONObject("status");

                Map<String, Object> resMap = null;

                if (statusJson.get("code").equals("200")) {
                    org.json.JSONObject result = dataJson.getJSONObject("result");
                    resMap = UtilManager.toMap(result);

                    String serviceStatus = "";
                    if (resMap.containsKey("serviceState")) {
                        String serviceState = resMap.get("serviceState").toString();
                        if (!UtilManager.isEmptyOrNull(serviceState)) {
                            if (serviceState.equals("접수중")) {
                                serviceStatus = "RECEIVE";
                            } else if (serviceState.equals("접수요청중")) {
                                serviceStatus = "RECEIVE";
                            } else if (serviceState.equals("접수확인")) {
                                serviceStatus = "CONFIRM";
                            } else if (serviceState.equals("접수취소")) {
                                serviceStatus = "CANCEL";
                            } else if (serviceState.equals("접수")) {
                                serviceStatus = "CONFIRM";
                            } else if (serviceState.equals("방문예정")) {
                                serviceStatus = "ENGINEER";
                            } else if (serviceState.equals("미처리상태")) {
                                serviceStatus = "CONFIRM";
                            } else if (serviceState.equals("자재확인중")) {
                                serviceStatus = "CONFIRM";
                            } else if (serviceState.equals("기사배정")) {
                                serviceStatus = "ENGINEER";
                            } else if (serviceState.equals("방문중")) {
                                serviceStatus = "VISIT";
                            } else if (serviceState.equals("완결")) {
                                serviceStatus = "COMPLETE";
                            } else {
                                serviceStatus = "COMPLETE";
                            }
                        } else {
                            serviceStatus = "RECEIVE";
                        }

                    } else {
                        serviceStatus = "RECEIVE";
                    }
                    resMap.put("serviceStatus", serviceStatus);

                    String name = resMap.get("name").toString();
                    String contact = resMap.get("contact").toString();

                    if (name.equals(custName) && contact.equals(phoneNum) && !UtilManager.isEmptyOrNull(resMap)) {

                        // 이름 마스킹
                        name = UtilManager.setMaskProcess(name, "name");
                        resMap.put("name", name);

                        // 휴대폰 번호 마스킹
                        contact = UtilManager.setMaskProcess(contact, "tel");
                        resMap.put("contact", contact);

                        res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
                    } else {
                        res = new BFResponse(ResultCodes.ERR_DATA_NOT_FOUND);
                    }

                } else {
                    res = new BFResponse(ResultCodes.ERR_NOT_DEFINED, UtilManager.toMap(statusJson));
                }
            } else {
                res = new BFResponse(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

        } catch (BFException be) {
            log.error("[CUSTOMER][SERVICE][customerService][getMyAfterService][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("[CUSTOMER][SERVICE][customerService][getMyAfterService][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[CUSTOMER][SERVICE][customerService][getMyAfterService][END]");
        return res;
    }

    /**
     * 서비스 접수 취소
     * @param params
     * @return
     * @throws BFException
     */
    public Response cancelAfterService(Map<String, Object> params) throws BFException {
        log.info("[CUSTOMER][SERVICE][customerService][cancelAfterService][START]");

        BFResponse res = null;
        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"encSeq"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            // ERP API 설정
            String serviceCode = uniErpServiceCode;
            String secretKey = uniErpSecretKey;
            String apiUrl = uniErpUrl;
            String callUrl = apiUrl + "/api/v1/service/cancel";

            String seq = UtilManager.decAES(params.get("encSeq").toString());
            String param = "?serviceIdx="+seq;

            callUrl += param;

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
            httpHeaders.add("serviceCode", serviceCode);
            httpHeaders.add("secretKey", secretKey);

            org.json.JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, param);
            org.json.JSONObject statusJson = resultJson.getJSONObject("status");
            if (statusJson.get("code").equals("200")) {
                res = new BFResponse(ResultCodes.RET_SUCCESS);
            } else {
                res = new BFResponse(ResultCodes.ERR_DB_DELETE_FAILURE);
            }

        } catch (BFException e) {
            // TODO: handle exception
            log.error("[CUSTOMER][SERVICE][customerService][cancelAfterService][ERROR] : {} ", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[CUSTOMER][SERVICE][customerService][cancelAfterService][ERROR] : {}", e.getStackTrace());
        }
        log.info("[CUSTOMER][SERVICE][customerService][cancelAfterService][END]");
        return res;
    }

    public String insertOnlineService(OnlineVO vo) throws Exception {

        // 바디프랜드 웹 db에 저장
        int result = customerDao.insertOnlineService(vo);

        String result_api = null;

        if (result > 0) {
            // 서비스 db에 저장
            RestTemplate restTemplate = new RestTemplate();
            //
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            String apiResult = null;

            //JSON DATA
            jsonObject.put("service_idx", vo.getServiceIdx());	// 홈페이지 DB serviceIdx
            jsonObject.put("reserve_route", "M");				// erp_site_type 인입 사이트 타입 (W : PC 홈페이지(안라정), M : 모바일 홈페이지(안라정), K : 카카오챗봇, C : 멤버십어플(고객어플))
            jsonObject.put("symptom", vo.getContent());
            jsonObject.put("addr", vo.getZip() + " " +vo.getAddr1());
            jsonObject.put("addr_detail", vo.getAddr2());
            jsonObject.put("contract_nm", vo.getName());
            jsonObject.put("contract_hphone_no", vo.getContant());
            jsonObject.put("chk_cs", vo.getServiceGroup());
            jsonObject.put("etc_chk_cs", vo.getServiceGroupOther());
            jsonObject.put("himart_yn", vo.getPrdtShop());
            jsonObject.put("etc_purchase", vo.getPrdtShopOther());
            jsonObject.put("product_type", vo.getPrdtCate());
            jsonObject.put("model_nm", vo.getPrdtName());
            jsonObject.put("user_id", vo.getUserId());
            jsonObject.put("user_idx", vo.getUserIdx());

            log.info("====================================== 온라인 서비스 가접수 insertOnlineService BM =======================================================");
            log.info("[CUSTOMER][SERVICE][customerService][insertOnlineService] API onlineService jsonObject :: {} " + jsonObject.toString());
            log.info("===================================================================================================================================");

            // 테스트
//			String url = "http://localhost:8080/ServiceMgt/AsReserve/CreateReserve2.json";
//	        String url = "http://172.30.40.17:18080/ServiceMgt/AsReserve/CreateReserve2.json";

            // 운영
            String url = "https://svc.bfservice.co.kr/ServiceMgt/AsReserve/CreateReserve2.json";

            jsonArray.add(jsonObject);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            try {
                HttpEntity<String> payload = new HttpEntity<String>(jsonArray.toString(), headers);
                apiResult = restTemplate.postForObject(url, payload, String.class);
                log.info("====================================== 온라인 서비스 가접수 insertOnlineService BM =======================================================");
                log.info("[CUSTOMER][SERVICE][customerService][insertOnlineService] API onlineService apiResult : " + apiResult + " custName :" + vo.getName());
                log.info("===================================================================================================================================");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            // 결과값에 따른 처리
            JSONObject feedback = (JSONObject) new JSONParser().parse(apiResult);
            result_api = feedback.get("resultCode").toString();

            /****************************
             * AlimTalk Message Send API
             ****************************/
            String tmplCd = "body_service_02";	// 서비스 접수
            String phone = vo.getContant();
            String encSeq = UtilManager.encAES(vo.getServiceIdx());

            org.json.JSONObject jsonMsgObject = new org.json.JSONObject();
            jsonMsgObject.put("celNo", phone);
            jsonMsgObject.put("templateCode", tmplCd);
            jsonMsgObject.put("tranButton", true);
            jsonMsgObject.put("#{고객명}", vo.getName());
            jsonMsgObject.put("#{서비스명}", "서비스 접수");
            jsonMsgObject.put("#{url}", "m.bodyfriend.co.kr/customer/afterService/"+encSeq);

            // 고객 알림톡 API 호출
            if (!"".equals(phone)) {
                messageService.sendAlimTalk(jsonMsgObject);
            }
        }
        return result_api;
    }

    // 서비스 가접수 UNIERP
    public Response insertOnlineService(HttpServletRequest request, OnlineVO vo) throws BFException {

        log.info("[CUSTOMER][SERVICE][customerService][insertOnlineService][START]");

        boolean isSuccess = false;
        String serviceIdx = "";
        BFResponse res = null;
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            Enumeration enr             = request.getParameterNames();

            while (enr.hasMoreElements()) {
                String name = URLDecoder.decode(enr.nextElement().toString(), "UTF-8");
                params.put(name, URLDecoder.decode(request.getParameter(name), "UTF-8"));
            }

            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {
                    "name","prdtName","prdtCate","zip","addr1","addr2","contact","serviceGroup","content"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            // 마케팅 동의
            if (params.containsKey("marketingYn")) {
                MarketingAgreeVO mvo = new MarketingAgreeVO();
                mvo.setName(params.get("name").toString());
                mvo.setPhone(params.get("contact").toString().replace("-", ""));
                mvo.setAgreeStatus(params.get("marketingYn").equals("Y") ? 1 : 0);
                mvo.setRefPageIdx(Constants.MarketingPage.SERVICE);
                marketingService.insertMarketingAgree(request.getSession(), mvo);
            }

            // 파일업로드
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            org.json.JSONArray imgPaths = new org.json.JSONArray();
            // 파일 등록
            if (mRequest.getFiles("ex_filename").size() > 0) {
                for (MultipartFile mfile : mRequest.getFiles("ex_filename")) {
                    String filename = FileUpload.fileUpload(mfile, "/upload/customerClaim/");
                    if(!filename.equals("")) {
                        Map<String, Object> fileInfo = FileUpload.returnfileUploadMap(mfile, "/upload/customerClaim/", filename);
                        Map<String, Object> imgPath = new HashMap<String, Object>();
                        imgPath.put("name", fileInfo.get("originalFileName"));
//                        imgPath.put("path", "https://" + CommProperty.getProperty("system.domain") + fileInfo.get("path") + fileInfo.get("fileName"));
                        imgPath.put("path", "https://" + hostName + fileInfo.get("path") + fileInfo.get("fileName"));
                        imgPaths.put(imgPath);
                    }
                }
                log.info("imgPaths : {}", imgPaths.toString());
            }

            // ERP API 설정
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            if (!UtilManager.isEmptyOrNull(params.get("userIdx"))) {
                jsonObj.put("userIdx", params.get("userIdx"));
            }
            if (!UtilManager.isEmptyOrNull(params.get("userId"))) {
                jsonObj.put("userId", params.get("userId"));
            }
            jsonObj.put("name", params.get("name"));
            jsonObj.put("prdtName", params.get("prdtName"));
            jsonObj.put("prdtCate", params.get("prdtCate"));
            jsonObj.put("zip", params.get("zip"));
            jsonObj.put("addr1", params.get("addr1"));
            jsonObj.put("addr2", params.get("addr2"));
            jsonObj.put("contact", params.get("contact"));
            jsonObj.put("serviceGroup", params.get("serviceGroup"));
            jsonObj.put("title", params.get("title"));
            jsonObj.put("content", params.get("content"));
            jsonObj.put("custCode", params.get("custCode"));
            jsonObj.put("referer", "BP");
            jsonObj.put("imgPaths", imgPaths.toString());

            // API 설계 오류로 POST 이지만 GET처럼 파라미터 붙여서 보내야함
            Iterator<String> keys = jsonObj.keys();
            List<String> apiparams = new ArrayList<String>();;
            while (keys.hasNext()) {
                String key = keys.next().toString();
                apiparams.add(key + "=" + jsonObj.get(key));

            }
            String callUrl = "/api/v4/service/insert";

            ApiResponseVO apiVo = apiService.callErpApiServer(callUrl, HttpMethod.POST, jsonObj);
            if (apiVo.getStatusCode().equals("200")) {
                res = new BFResponse(ResultCodes.RET_SUCCESS);

                // 서비스 접수 idx
                serviceIdx = apiVo.getData().get("serviceIdx").toString();

                if (!UtilManager.isEmptyOrNull(serviceIdx)) {

                    // 알림톡 발송
                    String tmplCd = "body_service_02";  // 서비스 접수
                    String phone = params.get("contact").toString();
                    String name = params.get("name").toString();
                    String encSeq = UtilManager.encAES(serviceIdx);

                    org.json.JSONObject jsonMsgObject = new org.json.JSONObject();
                    jsonMsgObject.put("celNo", phone);
                    jsonMsgObject.put("templateCode", tmplCd);
                    jsonMsgObject.put("tranButton", true);
                    jsonMsgObject.put("#{고객명}", name);
                    jsonMsgObject.put("#{서비스명}", "서비스 접수");
                    jsonMsgObject.put("#{url}", "m.bodyfriend.co.kr/customer/afterService/"+encSeq);

                    // 고객 알림톡 API 호출
                    if (!messageService.sendAlimTalk(jsonMsgObject)) {
                        throw new Exception();
                    };
                }

            } else {
                res = new BFResponse(apiVo.getStatusCode(), apiVo.getStatusMessage());
            }

            isSuccess = true;

        } catch (BFException e) {
            // TODO: handle exception
            log.error("[CUSTOMER][SERVICE][customerService][insertOnlineService][ERROR] : {} ", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[CUSTOMER][SERVICE][customerService][insertOnlineService][ERROR] : {}", e.getStackTrace());
        } finally {

            // 로직 실패 시 접수 취소 요청
            if (!isSuccess && !UtilManager.isEmptyOrNull(serviceIdx)) {
                log.error("[CUSTOMER][SERVICE][customerService][insertOnlineService][서비스 접수 실패] : {}", serviceIdx);
                String callUrl = "/api/v1/service/cancel?serviceIdx=" + serviceIdx;
                try {
                    apiService.callErpApiServer(callUrl, HttpMethod.POST, null);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        log.info("[CUSTOMER][SERVICE][customerService][insertOnlineService][END]");
        return res;
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

    // 제품리스트 (erp)
    public List<HashMap<String, String>> getGoodsList(HttpServletRequest request) {

        List<HashMap<String, String>> list = unierpMyinfoDao.getGoodsList(request.getParameter("type"));
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                list.get(i).put("imgPath", myinfoDao.getGoodsImgPath(list.get(i).get("modelCode")));
            }
        }
        return list;
    }

}
