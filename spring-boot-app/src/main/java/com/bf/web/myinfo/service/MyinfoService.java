package com.bf.web.myinfo.service;

import com.bf.common.*;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.Sha256;
import com.bf.common.util.UtilManager;
import com.bf.unierp.myinfo.dao.UnierpMyinfoDao;
import com.bf.web.api.service.ApiService;
import com.bf.web.api.vo.ApiResponseVO;
import com.bf.web.customer.dao.CustomerDao;
import com.bf.web.member.dao.MemberDao;
import com.bf.web.myinfo.dao.MyinfoDao;
import com.bf.web.myinfo.vo.Myinfo;
import com.bf.web.pay.vo.PayResultVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MyinfoService {

    @Autowired
    MyinfoDao myinfoDao;
    @Autowired
    UnierpMyinfoDao unierpMyinfoDao;
    @Autowired
    ApiService apiService;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CustomerDao customerDao;

    @Value(value = "${system.membership.salt}")
    String membershipSalt;
    @Value(value = "${system.membership.url}")
    String membershipUrl;
    @Value(value = "${system.unierp.serviceCode}")
    String uniErpServiceCode;
    @Value(value = "${system.unierp.url}")
    String uniErpUrl;
    @Value(value = "${system.unierp.secretKey}")
    String uniErpSecretKey;

    private final static String aes_key = "bfservicekey!@12";
    private final int LIST_SIZE = 15;

    /**
     * 쿠폰 목록 조회
     * @param session
     * @param myinfo
     * @return
     * @throws BFException
     */
    public Response selectCouponList(HttpSession session, Myinfo myinfo) throws BFException {

        Response res;
        try {
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            if (UtilManager.isEmptyOrNull(myinfo.getPage()) || myinfo.getPage() == 0) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));

            myinfo = setPageFromTo(myinfo);
            List<HashMap<String, String>> list = myinfoDao.selectCouponList(myinfo);

            int coupon = myinfoDao.getCoupon(myinfo);
            int count = myinfoDao.getCouponCount(myinfo); // 페이지 수 구하기 위해 총 몇 개인지

            Map resMap = new HashMap();
            resMap.put("availableCnt", coupon);
            resMap.put("totalCnt", count);
            resMap.put("totalPage", getTotalpage(count));
            resMap.put("list", list);

            res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);

        } catch (BFException e) {
            log.error("[MYINFO][SERVICE][MyinfoService][selectCouponList][ERROR] : {}" + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyinfoService][selectCouponList][ERROR] : {}" + e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }
        return res;
    }

    @Transactional
    public Map selectPointList(Myinfo myinfo) {
        Map resultMap = new HashMap();

        myinfo = setPageFromTo(myinfo);
        List<HashMap<String, String>> list = myinfoDao.selectPointList(myinfo);

        int point = myinfoDao.getPoint(myinfo);
        int count = myinfoDao.getPointCount(myinfo); // 페이지 수 구하기 위해 총 몇 개인지

        resultMap.put("point", point);
        resultMap.put("totalPage", getTotalpage(count));
        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    /**
     * 주문/배송 조회
     * @param params
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map selectOrderList(Map<String, Object> params) {

        // 페이징 설정
        int startPageNum = Integer.parseInt(params.get("curPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int curPage = (startPageNum-1)*pageSize;
        params.put("pageSize", startPageNum*pageSize);
        params.put("curPage" ,curPage);
        params.put("firstLimitIndex", curPage);
        params.put("lastLimitIndex", pageSize);

        Map resultMap = new HashMap();
        List<HashMap<String, Object>> orderList = myinfoDao.selectOrderList(params);

        int count = 0;

        if (params.get("paymentType").equals("9")) { // 렌탈
            count = myinfoDao.getRentalOrderCount(params);
            System.out.println("렌탈 >>>> ");
        } else {
            count = myinfoDao.getOrderCount(params);
        }

        resultMap.put("totalPage", getTotalpage(count));
        resultMap.put("pageSize", pageSize);
        resultMap.put("curPage", startPageNum);
        resultMap.put(Constants.RESULT_DATA, orderList);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    /**
     * 주문 내역 상세 조회
     * @param params
     * @return
     */
    public Map selectOrderDetail(Map<String, Object> params) {

        Map resultMap = new HashMap();
        List<HashMap<String, Object>> orderDetailList = myinfoDao.selectOrderDetail(params);

        // 상품 옵션 조회
        for (HashMap<String, Object> orderInfo : orderDetailList) {
            List<Map<String, Object>> optionList = myinfoDao.selectOptionList(orderInfo);
            orderInfo.put("optionList", optionList);
        }

        int count = myinfoDao.getOrderCount(params);

        resultMap.put(Constants.RESULT_DATA, orderDetailList);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    /**
     * 취소/반품/교환 목록 조회
     * @param params
     * @return
     */
    public Map selectCancelOrderList(Map<String, Object> params) {
        // 페이징 설정
        int startPageNum = Integer.parseInt(params.get("curPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int curPage = (startPageNum-1)*pageSize;
        params.put("pageSize", startPageNum*pageSize);
        params.put("curPage" ,curPage);
        params.put("firstLimitIndex", curPage);
        params.put("lastLimitIndex", pageSize);

        Map resultMap = new HashMap();
        List<HashMap<String, Object>> cancelOrderList = myinfoDao.selectCancelOrderList(params);

        int count = myinfoDao.getCancelOrderCount(params);

        resultMap.put("totalPage", getTotalpage(count));
        resultMap.put("pageSize", pageSize);
        resultMap.put("curPage", startPageNum);
        resultMap.put(Constants.RESULT_DATA, cancelOrderList);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    @Transactional
    public Map selectQnaList(Myinfo myinfo) {
        Map resultMap = new HashMap();

        myinfo = setPageFromTo(myinfo);
        List<HashMap<String, String>> list = myinfoDao.selectQnaList(myinfo);

        int count = myinfoDao.getQnaCount(myinfo);

        resultMap.put("totalPage", getTotalpage(count));
        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    @Transactional
    public Map insertQna(Myinfo myinfo) {
        Map resultMap = new HashMap();

        int groups = myinfoDao.getQnaGroups();
        myinfo.setGroups(groups);

        int result = myinfoDao.insertQna(myinfo);

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    @Transactional
    public Map selectQnaDetail(Myinfo myinfo) {

        Map resultMap = new HashMap();

        List<HashMap<String, String>> list = myinfoDao.selectQnaDetail(myinfo);

        // 개행
        for (int i=0; i<list.size(); i++) {
            list.get(i).put("contents", list.get(i).get("contents").toString().replace("\n", "<br>"));
        }

        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    @Transactional
    public Map selectPersonalList(Myinfo myinfo) {

        Map resultMap = new HashMap();

        myinfo = setPageFromTo(myinfo);
        HashMap<String, String> list = myinfoDao.selectPersonalList(myinfo);

        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    @Transactional
    public Map updatePersonal(Myinfo myinfo) {
        Map resultMap = new HashMap();

        //int result = myinfoDao.updatePersonal(myinfo);

        // 멤버십앱 회원정보 업데이트 api 호출
        int apiResult = callMembershipAppApiForUpdatePerson(myinfo);
        // api 업데이트 성공시
        int result = 0;
        if (apiResult > 0) {
            result = myinfoDao.updatePersonal(myinfo);
        }

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    /**
     * 회원정보 업데이트 - 멤버십앱
     * @return
     */
    private int callMembershipAppApiForUpdatePerson(Myinfo myinfo) {
        int result = 0;

        try {

            // 멤버십앱 정보 업데이트 위한 정보 조회
            Myinfo savedInfo = myinfoDao.getUserInfoForMembership(myinfo.getUserIdx());

            if (!UtilManager.isEmptyOrNull(savedInfo)) {

                myinfo.setUserId(savedInfo.getUserId());
                myinfo.setAccessDate(savedInfo.getAccessDate());
                myinfo.setName(savedInfo.getName());
                myinfo.setPoint(savedInfo.getPoint());

                // 비번 변경 안할경우 오리지널 비번
                if(myinfo.getPwd().equals("N")){
                    myinfo.setPwd(savedInfo.getPwd());
                }

                JSONArray jsonArray 			= new JSONArray();
                JSONObject jsonObject 			= new JSONObject();

                String userIdx = myinfo.getUserIdx();
                String salt = membershipSalt;
                Sha256 sha256 = new Sha256();
                String hash = sha256.enc(userIdx + salt);
                String sex = myinfo.getSex();
                String gender = sex.equals("1") ? "male" : (sex.equals("2") ? "female" : "");

                jsonObject.put("userIdx", userIdx);
                jsonObject.put("name", myinfo.getName());
                jsonObject.put("userId", myinfo.getUserId());
                jsonObject.put("password", myinfo.getPwd());
                //jsonObject.put("gender", gender);
                //jsonObject.put("birthday", myinfo.getBirthDay());
                jsonObject.put("tel1", myinfo.getTel1());
                jsonObject.put("tel2", myinfo.getTel2());
                jsonObject.put("tel3", myinfo.getTel3());
                jsonObject.put("mobile1", myinfo.getCell1());
                jsonObject.put("mobile2", myinfo.getCell2());
                jsonObject.put("mobile3", myinfo.getCell3());
                jsonObject.put("email", myinfo.getEmail());
                jsonObject.put("zipCode1", myinfo.getZip1());
                jsonObject.put("zipCode2", myinfo.getZip2());
                jsonObject.put("address1", myinfo.getAddr1());
                jsonObject.put("address2", myinfo.getAddr2());
                jsonObject.put("agreeEmail", false);
                jsonObject.put("agreeSms", false);
                jsonObject.put("agreeFriendTv", false);
                jsonObject.put("agreePush", false);
                jsonObject.put("agreeMarketing", false);
                jsonObject.put("createdAt", myinfo.getAccessDate()); // 가입일
                jsonObject.put("region", userIdx);
                jsonObject.put("point", myinfo.getPoint());
                jsonObject.put("deleted", false);
                //jsonObject.put("nation", userIdx);
                jsonObject.put("hash", hash);

                log.info("[MYINFO][SERVICE][MyInfoService][callMembershipAppApiForUpdatePerson][jsonObject] : {}", jsonObject.toString());

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Accept", "application/json;charset=UTF-8");
                httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

                // dev url
//				String callUrl = "https://api.bodyfriend.me/api/v2/bf/member/update";
                // production url
//				String callUrl = "http://dapi.bodyfriend.co.kr/api/v2/bf/member/update";
                String callUrl = membershipUrl;
                callUrl += "/api/v2/bf/member/update";

                JSONObject resultJson = null;
                resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonObject);

                log.info("[MYINFO][SERVICE][MyInfoService][callMembershipAppApiForUpdatePerson][resultJson] : {}", resultJson.toString());

                if (resultJson.get("status").equals("success")) {
                    return 1;
                } else {
                    return 0;
                }

            } else {
                // 멤버십앱 정보 업데이트 필요없음
                result = 1;
            }

        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyInfoService][callMembershipAppApiForUpdatePerson][ERROR] : {}", e.getStackTrace());
        }
        return result;
    }

    public Map getNomemberOrderList(Myinfo myinfo) {
        Map resultMap = new HashMap();

        myinfo = setPageFromTo(myinfo);
        List<HashMap<String, String>> list = myinfoDao.getNomemberOrderList(myinfo);

//			int count = myinfoDao.getOrderCount(myinfo);

//			resultMap.put("totalPage", getTotalpage(count));
        resultMap.put(Constants.RESULT_DATA, list);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    public int insertStatusLog(Map<String, Object> parmas) {

        return myinfoDao.insertStatusLog(parmas);
    }

    public Map updateTranState(Map<String, Object> params) {
        Map resultMap = new HashMap();

        int result = myinfoDao.updateTranState(params);

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    //쿠폰 확인
    public HashMap<String, String> selectCouponConfirm(String number)throws Exception {
        return myinfoDao.selectCouponConfirm(number);
    }

    // 쿠폰 정보 수정 및 쿠폰 발급
    public Map updateCouponInfo(Map params) {

        Map resultMap = new HashMap();

        int result = myinfoDao.updateCouponInfo(params);
        myinfoDao.insertCouponAdd(params);
        myinfoDao.insertCouponAdd2(params);
        myinfoDao.insertCouponAdd3(params);

        resultMap.put(Constants.RESULT_DATA, result);
        resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);

        return resultMap;
    }

    // 쿠폰 중복 체크
    public HashMap<String, String> selectCouponYNChk(String number) {

        return myinfoDao.selectCouponYNChk(number);
    }

    /*
     * 고장 접수내역 조회
     */
    public List<HashMap<String, String>> selectAfterService(String serviceIdx) {
        return myinfoDao.selectAfterService(serviceIdx);
    }

    //본인인증 업데이트
    public int updateMemberCertYn(Map<String, Object> params){
        return myinfoDao.updateMemberCertYn(params);
    }

    //본인인증 여부 확인
    public HashMap<String, String> checkCertify(HttpServletRequest request){
        return myinfoDao.checkCertify(request.getParameter("userId"));
    }

    //erp 구매&렌탈 이력 //안쓰는듯
    /*public List<HashMap<String, String>> selectMyRentalList(HttpServletRequest request) throws Exception {
        Map params = new HashMap();

        //String[] modelName = request.getParameter("modelName").split(",");

        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));
        params.put("type", request.getParameter("type"));
		*//*params.put("startDate", request.getParameter("startDate"));
		params.put("endDate", request.getParameter("endDate"));
		params.put("modelName", modelName);*//*

        List<HashMap<String, String>> list = myinfoDao.selectMyRentalList(params);
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                list.get(i).put("imgPath", myinfoDao.getGoodsImgPath(list.get(i).get("modelCode")));
            }
        }

        return list;
    }*/

    //erp 구매&렌탈 이력(사용자만 조회)
    public List<HashMap<String, String>> selectMyRentalList_Inst(HttpServletRequest request) throws Exception {
        Map params = new HashMap();

        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));
        params.put("type", request.getParameter("type"));

        List<HashMap<String, String>> list = unierpMyinfoDao.selectMyRentalListUNIERP_info(params);
        if(list.size() > 0){
            for(int i=0; i<list.size(); i++){
                list.get(i).put("imgPath", myinfoDao.getGoodsImgPath(list.get(i).get("modelCode")));
            }
        }
        return list;
    }

    // 상품평 유효제품 조회, erp 구매&렌탈 이력(후기 제품조회)
    public List<HashMap<String, String>> selectMyRentalList_Review(HttpServletRequest request) throws Exception {
        Map params = new HashMap();

        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));
        params.put("type", request.getParameter("type"));

        List<HashMap<String, String>> list = unierpMyinfoDao.selectMyRentalListUNIERP_info(params);

        Map subParams = new HashMap<String, Object>();
        List<HashMap<String, String>> writeInfo = new ArrayList<HashMap<String, String>>();

        subParams.put("list", list);
        subParams.put("custName", request.getParameter("name"));

        if(list.size() > 0){
            writeInfo = myinfoDao.getReviewWriteInfo(subParams);
            // 주문번호 추가
            for(int i=0; i<writeInfo.size(); i++){
                writeInfo.get(i).put("orderNo", list.get(i).get("orderNo"));
            }
        }

        return writeInfo;
    }

    // 가접수 내역 건수 조회 UNIERP
    public int selectTemporarySCVCountUNIERP(HttpServletRequest request) throws Exception {

        int result = 0;
        Map params = new HashMap();
        params.put("userName", request.getParameter("name"));
        params.put("phoneNo", request.getParameter("phone"));

        System.out.println("selectTemporarySCVCntUNIERP >>>> param >>> "+ params);
        result = unierpMyinfoDao.selectTemporarySCVCountUNIERP(params);
        System.out.println("== result : " + result);

        return result;
    }

    /**
     * UNIERP 제품 조회
     * @param request 이름 전화번호 (안마의자만 조회시 grpCode=M)
     * @return 제품 목록
     * @throws Exception
     */
    public Response getMyAllProductList(HttpServletRequest request, Map<String, Object> params) throws BFException {
        BFResponse res;
        try {

            log.info("[MYINFO][SERVICE][MyinfoService][getMyAllProductList][START]");

            HttpSession session = request.getSession();

            // 필수 파라미터 체크 (로그인 or 본인인증 필수)
            Map sessionCheck = UtilManager.checkCertOrLogin(session);
            if (!(boolean) sessionCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            // 파라미터 정보로 조회
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"name", "phone"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                params.put("name", sessionCheck.get(Consts.USER_NAME));
                params.put("phone", sessionCheck.get(Consts.USER_PHONE));
            }

            // 본인인증 정보로 조회
            String userType = "";
            if (!UtilManager.isEmptyOrNull(params.get("userType")) && params.get("userType").equals("cert")) {
                params.put("name", session.getAttribute(Constants.SESSION_CERT_NAME));
                params.put("phone", session.getAttribute(Constants.SESSION_CERT_PHONE));
            }

            // 이미지 사용 여부
            boolean isUseImg = true;
            if (!UtilManager.isEmptyOrNull(params.get("useImg"))) {
                isUseImg = !params.get("useImg").toString().equals("N");
            }

            String callUrl = "/api/v1/mybf/getAllproductList";
            String name = params.get("name").toString();
            String phone = params.get("phone").toString();

            JSONObject paramJson = new JSONObject();
            paramJson.put("userName", name);
            paramJson.put("phone", phone);

            ApiResponseVO vo = apiService.callErpApiServer(callUrl, HttpMethod.GET, paramJson);
            if (vo.getStatusCode().equals("200")) {

                ObjectMapper mapper = new ObjectMapper();
                org.json.JSONArray array = vo.getData().getJSONArray("productList");
                List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();

                for (int i=0; i<array.length(); i++ ) {

                    Map<String, Object> product = mapper.readValue(array.get(i).toString(), Map.class);

                    String grpCode = product.get("grpCode").toString();

                    if (UtilManager.isEmptyOrNull(params.get("grpCode"))) {

                        if (isUseImg) {
                            String imgPath = myinfoDao.getGoodsImgPath(product.get("modelCode").toString());
                            if (UtilManager.isEmptyOrNull(imgPath)) {
                                imgPath = NoImage.findImage(grpCode);
                            }
                            product.put("imgPath", imgPath);
                        }
                        productList.add(product);
                    } else {
                        // grpCode에 맞는 값만 조회 (grpCode = M/L/W)
                        if (grpCode.equals(params.get("grpCode").toString())) {
                            if (isUseImg) {
                                String imgPath = myinfoDao.getGoodsImgPath(product.get("modelCode").toString());
                                if (UtilManager.isEmptyOrNull(imgPath)) {
                                    imgPath = NoImage.findImage(grpCode);
                                }
                                product.put("imgPath", imgPath);
                            }
                            productList.add(product);
                        }

                    }

                }

                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("productList", productList);

                res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);

            } else {
                res = new BFResponse(vo.getStatusCode(), vo.getStatusMessage());
            }

        } catch (BFException e) {
            log.debug("[MYINFO][SERVICE][MyinfoService][getMyAllProductList][ERROR] " + e.getStackTrace());
            throw e;
        } catch (Exception e) {
            log.debug("[MYINFO][SERVICE][MyinfoService][getMyAllProductList][ERROR] " + e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getMyAllProductList][END]");

        return res;
    }

    /**
     * 분해조립 목록 조회
     * @param params
     * @return
     * @throws BFException
     */
    public Response getAssembleList(HttpSession session, Map<String, Object> params) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][getAssembleList][START]");

        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            Object stDate = params.get("fromDate");
            Object edDate = params.get("toDate");
            String userIdx = session.getAttribute(Constants.SESSION_USER_IDX).toString();
            params.put("userIdx", userIdx);

            Map<String, Object> userMap = memberDao.selectUserInfo(params);
            if (!UtilManager.isEmptyOrNull(userMap)) {
                // ERP API 설정
                String callUrl = uniErpUrl + "/api/v1/service/assemble/list";
                String serviceCode = uniErpServiceCode;
                String secretKey = uniErpSecretKey;

                String paramStr = "";
                paramStr += "name=" + userMap.get("name").toString();
                paramStr += "&contact=" + userMap.get("mobile1").toString() + userMap.get("mobile2").toString() + userMap.get("mobile3").toString();
                paramStr += "&siteType=B";

                if (stDate != null) {
                    paramStr += "&stDate="+stDate;
                }
                if (edDate != null) {
                    paramStr += "&edDate="+edDate;
                }

                callUrl += "?";
                callUrl += paramStr;

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
                httpHeaders.add("serviceCode", serviceCode);
                httpHeaders.add("secretKey", secretKey);

                org.json.JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.GET, paramStr);

                log.info("[MYINFO][SERVICE][MyinfoService][getAssembleList][jsonResult] : {}", resultJson);

                if (resultJson.getJSONObject("status").get("code").equals("200")) {

                    Map<String, Object> resMap = new HashMap<String, Object>();
                    resMap = UtilManager.toMap(resultJson.getJSONObject("data"));

                    res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
                } else {
                    res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
                }
            } else {
                res = new BFResponse(ResultCodes.ERR_DATA_NOT_FOUND);
            }
        } catch (BFException be) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAssembleList][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[MYINFO][SERVICE][MyinfoService][getAssembleList][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getAssembleList][END]");
        return res;
    }

    /**
     * 분해조립 취소
     * @param params
     * @return
     * @throws BFException
     */
    public Response cancelAssemble(Map<String, Object> params) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][START]");

        BFResponse res = null;

        /*try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"serviceIdx", "payOrdNo"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            String payOrdNo = (String) params.get("payOrdNo");
            String serviceIdx = (String) params.get("serviceIdx");

            // 접수 상태 조회 (접수중일 경우만 취소 가능)
            JSONObject assembleJson = this.sendAssembleInfoApi(serviceIdx);
            log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][assembleInfo] : {}", assembleJson);
            if (assembleJson.getJSONObject("status").get("code").equals("200")) {
                String serviceState = (String) assembleJson.getJSONObject("data").getJSONArray("result").getJSONObject(0).get("serviceState");
                if (!serviceState.equals("접수중")) {
                    throw new BFException(ResultCodes.ERR_NO_PERMISSION);
                }
            } else {
                throw new BFException(ResultCodes.ERR_PAY_CANCEL_FAIL);
            }

            // 주문 정보 조회 (orders 테이블)
            Map<String, Object> pgMap = productDao.getOrderPgData(payOrdNo);
            log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][pvo] : {}", pgMap);

            if (!UtilManager.isEmptyOrNull(pgMap)) {

                // 카드 취소
                String moid = payOrdNo;
                String tid = pgMap.get("tid").toString();
                String amt = pgMap.get("amount").toString();
                String cancelMsg = "고객 홈페이지 직접 취소";

                if (!UtilManager.isEmptyOrNull(tid) && !UtilManager.isEmptyOrNull(amt)) {
                    CancelRequestVO cancelVo = new CancelRequestVO();
                    cancelVo.setTid(tid);
                    cancelVo.setCancelAmt(amt);
                    cancelVo.setMoid(moid);
                    cancelVo.setCancelMsg(cancelMsg);
                    log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][cancelVo] : {}", cancelVo);

                    CancelResultVO crvo = payService.checkLogAndCancelRequest(cancelVo);
                    log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][crvo] : {}", crvo);

                    if (!crvo.isCancelSuccess()) {
                        throw new BFException(ResultCodes.ERR_PAY_CANCEL_FAIL, crvo.getResultMsg());
                    } else {
                        // 주문테이블 상태값 변경
                        Map<String, Object> tranParams = new HashMap<String, Object>();
                        tranParams.put("ordNbr", moid);
                        tranParams.put("tranState", "13");
                        productDao.updateOrdersTranState(tranParams);

                        // 로그 입력
                        Map<String, Object> logParams = new HashMap<String, Object>();
                        logParams.put("orderNumber", moid);
                        logParams.put("moid", moid);
                        logParams.put("log", "시스템");
                        logParams.put("tranState", "13");
                        productDao.insertOrderLog(logParams);
                    }
                }
            }

            // 분해조립 취소 API 요청
            JSONObject resultJson = this.sendCancelAssembleApi(serviceIdx);

            JSONObject statusJson = resultJson.getJSONObject("status");
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("code", statusJson.get("code"));
            resMap.put("message", statusJson.get("message"));

            if (statusJson.get("code").equals("200")) {

                // 주문테이블 상태값 변경
                Map<String, Object> tranParams = new HashMap<String, Object>();
                tranParams.put("ordNbr", payOrdNo);
                tranParams.put("tranState", "7");
                productDao.updateOrdersTranState(tranParams);

                // 로그 입력
                Map<String, Object> logParams = new HashMap<String, Object>();
                logParams.put("orderNumber", payOrdNo);
                logParams.put("moid", payOrdNo);
                logParams.put("log", "시스템");
                logParams.put("tranState", "7");
                productDao.insertOrderLog(logParams);

                res = new BFResponse(ResultCodes.RET_SUCCESS);
            } else {
                res = new BFResponse(ResultCodes.ERR_DB_DELETE_FAILURE, resMap);
            }
        } catch (BFException e) {
            log.error("[MYINFO][SERVICE][MyinfoService][cancelAssemble][ERROR] : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyinfoService][cancelAssemble][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }*/

        log.info("[MYINFO][SERVICE][MyinfoService][cancelAssemble][END]");
        return res;
    }

    /**
     * 분해조립 취소 API 요청
     * @param serviceIdx
     * @return
     * @throws Exception
     */
    public JSONObject sendCancelAssembleApi(String serviceIdx) throws Exception {
        // ERP API 설정
        String serviceCode = uniErpServiceCode;
        String secretKey = uniErpSecretKey;
        String apiUrl = uniErpUrl;
        String callUrl = apiUrl + "/api/v1/service/assemble/cancel";

        JSONObject jsonObj = new org.json.JSONObject();
        jsonObj.put("serviceIdx", serviceIdx);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
        httpHeaders.add("serviceCode", serviceCode);
        httpHeaders.add("secretKey", secretKey);

        JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonObj);

        log.info("[MYINFO][SERVICE][MyinfoService][sendCancelAssembleApi][jsonResult] : {}", resultJson);

        return resultJson;
    }

    /**
     * 분해조립 조회
     * @param payOrdNo
     * @return
     * @throws BFException
     */
    public Response getAssembleInfo(HttpSession session, String payOrdNo) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][getAssembleList][START]");

        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
//				throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            if (UtilManager.isEmptyOrNull(payOrdNo)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            String serviceIdx = customerDao.getServiceIdxFromPayOrdNo(payOrdNo);

            JSONObject assembleJson = this.sendAssembleInfoApi(serviceIdx);
            if (assembleJson.getJSONObject("status").get("code").equals("200")) {
                Map resMap = UtilManager.toMap(assembleJson.getJSONObject("data").getJSONArray("result").getJSONObject(0));
                res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
            } else {
                throw new BFException(ResultCodes.ERR_NOT_DEFINED);
            }

        } catch (BFException be) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAssembleList][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[MYINFO][SERVICE][MyinfoService][getAssembleList][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getAssembleList][END]");
        return res;
    }

    /**
     * 분해조립 정보 API 요청
     * @param serviceIdx
     * @return
     * @throws Exception
     */
    public JSONObject sendAssembleInfoApi(String serviceIdx) throws Exception {
        // ERP API 설정
        String serviceCode = uniErpServiceCode;
        String secretKey = uniErpSecretKey;
        String apiUrl = uniErpUrl;
        String callUrl = apiUrl + "/api/v1/service/assemble/adminList";

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("serviceIdx", serviceIdx);

        JSONArray jsonArr = new JSONArray();
        jsonArr.add(jsonObj);

        JSONObject jsonWrapObj = new JSONObject();
        jsonWrapObj.put("assembleList", jsonArr);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
        httpHeaders.add("serviceCode", serviceCode);
        httpHeaders.add("secretKey", secretKey);

        JSONObject resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonWrapObj);

        log.info("[MYINFO][SERVICE][MyinfoService][sendCheckAssembleInfo][jsonResult] : {}", resultJson);

        return resultJson;
    }

    /**
     * 마이페이지 > 서비스 내역 목록
     * @param session
     * @return
     * @throws BFException
     */
    public Response getAfterServiceList(HttpSession session, Map<String, Object> params) throws BFException {

        log.info("[MYINFO][SERVICE][MyinfoService][getAfterServiceList][START]");
        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_NAME, Constants.SESSION_PHONE, Constants.SESSION_USER_IDX});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            String userIdx = session.getAttribute(Constants.SESSION_USER_IDX).toString();
            String name = session.getAttribute(Constants.SESSION_NAME).toString();
            String contact = session.getAttribute(Constants.SESSION_PHONE).toString();

            JSONObject paramJson = new JSONObject();
            paramJson.put("name", session.getAttribute(Constants.SESSION_NAME));
            paramJson.put("contact", session.getAttribute(Constants.SESSION_PHONE));
            paramJson.put("startDt", params.get("fromDate"));
            paramJson.put("endDt", params.get("toDate"));

            ApiResponseVO vo = apiService.callErpApiServer("/api/v2/service/list", HttpMethod.GET, paramJson);
            if (vo.getStatusCode().equals("200")) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(vo.getData().toString());

                Map<String, Object> resMap = new HashMap<String, Object>();
                List<Map<String, Object>> serviceList = new ArrayList<Map<String,Object>>();

                Iterator<JsonNode> result = node.get("result").iterator();
                while(result.hasNext()) {
                    JsonNode serviceNode = result.next();

                    String certUserIdx = serviceNode.has("userIdx") ? serviceNode.get("userIdx").asText() : "";
                    String certName = serviceNode.has("name") ? serviceNode.get("name").asText() : "";
                    String certPhone = serviceNode.has("contact") ? serviceNode.get("contact").asText() : "";

                    if (userIdx.equals(certUserIdx) || (name.equals(certName) && contact.equals(certPhone))) {

                        String serviceStatus = "";
                        String serviceState = serviceNode.get("serviceState").asText();
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

                        Map<String, Object> serviceMap = new HashMap<String, Object>();
                        serviceMap.put("serviceGroup", serviceNode.get("serviceGroup").asText());
                        serviceMap.put("serviceIdx", serviceNode.get("serviceIdx").asText());
                        serviceMap.put("regDate", serviceNode.get("regDate").asText());
                        serviceMap.put("prdtName", serviceNode.get("prdtName").asText());
                        serviceMap.put("serviceState", serviceStatus);
                        serviceList.add(serviceMap);
                    }

                }
                resMap.put("list", serviceList);

                res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
            } else {
                res = new BFResponse(vo.getStatusCode(), vo.getStatusMessage());
            }

        } catch (BFException e) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAfterServiceList][ERROR] : {}" + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAfterServiceList][ERROR] : {}" + e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getAfterServiceList][END]");
        return res;

    }

    /**
     * 마이페이지 > 서비스 내역 목록 > 상세
     * @param params, name, phone
     * @return
     * @throws BFException
     */
    public Response getAfterServiceInfo(HttpServletRequest request, HttpSession session, Map<String, Object> params) throws BFException {

        log.info("[MYINFO][SERVICE][MyinfoService][getAfterServiceInfo][START]");
        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_NAME, Constants.SESSION_PHONE});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            // 필수 파라미터 체크
            paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"serviceIdx"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            JSONObject paramJson = new JSONObject();
            paramJson.put("serviceIdx", params.get("serviceIdx"));

            ApiResponseVO vo = apiService.callErpApiServer("/api/v1/service/info", HttpMethod.GET, paramJson);

            if (vo.getStatusCode().equals("200")) {

                String userIdx = "";
                if (!UtilManager.isEmptyOrNull(session.getAttribute(Constants.SESSION_USER_IDX))) {
                    userIdx = session.getAttribute(Constants.SESSION_USER_IDX).toString();
                }

                String name = session.getAttribute(Constants.SESSION_NAME).toString();
                String phone = session.getAttribute(Constants.SESSION_PHONE).toString();

                Map<String, Object> resMap = UtilManager.toMap(vo.getData().getJSONObject("result"));
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

                if (resMap.containsKey("userIdx") && (resMap.get("userIdx").equals(userIdx))) {
                    res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
                } else if (resMap.containsKey("certUserName") && resMap.containsKey("certUserPhone") && resMap.get("certUserName").equals(name) && resMap.get("certUserPhone").equals(phone)) {
                    res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
                } else if (resMap.containsKey("name") && resMap.containsKey("contact") && resMap.get("name").equals(name) && resMap.get("contact").equals(phone)) {
                    res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
                } else {
                    res = new BFResponse(ResultCodes.ERR_API_PERMISSION);
                }

            } else if (vo.getStatusCode().equals("ERR_MS_6001")) {
                res = new BFResponse(ResultCodes.ERR_DATA_NOT_FOUND);
            } else {
                res = new BFResponse(ResultCodes.ERR_API_CONNECT);
            }

        } catch (BFException e) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAfterServiceInfo][ERROR] : {}", e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyinfoService][getAfterServiceInfo][ERROR] : {}", e.getMessage());
            e.printStackTrace();
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getAfterServiceInfo][END]");
        return res;

    }


    /**
     * 결제 정보 조회
     * @param custCode, authType
     * @return
     * @throws BFException
     */
    public Response getPaymentDetails(HttpSession session, String custCode, AuthType authType) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][getPaymentDetails][START]");

        BFResponse res = null;

        log.info("authType = {} {}", authType, authType.getDesc());

        try {
            String[] checkSession = null;
            switch (authType) {
                case LOGIN:
                    checkSession = new String[] {Constants.SESSION_USER_IDX};
                    break;
                case CERT:
                    checkSession = new String[] {Constants.SESSION_CERT_NAME, Constants.SESSION_CERT_PHONE};
                    break;
            }

            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, checkSession);
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            if (UtilManager.isEmptyOrNull(custCode)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            JSONObject paramJson = new JSONObject();
            paramJson.put("custCode", custCode);

            ApiResponseVO vo = apiService.callErpApiServer("/api/v1/mybf/getPaymentDetails", HttpMethod.GET, paramJson);
            if (vo.getStatusCode().equals("200")) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(vo.getData().toString());

                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put("custName", node.get("custName")); // 고객명
                resMap.put("modelName", node.get("modelName")); //제품명
                resMap.put("totMinapMonth", node.get("totMinapMonth")); // 미납개월
                resMap.put("totNabbooMonth", node.get("totNabbooMonth")); // 납부개월
                resMap.put("payInfoList", node.get("payInfoList")); // 결제정보목록

                res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);

            } else {
                throw new BFException(ResultCodes.ERR_NOT_DEFINED);
            }

        } catch (BFException be) {
            log.error("[MYINFO][SERVICE][MyinfoService][getPaymentDetails][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[MYINFO][SERVICE][MyinfoService][getPaymentDetails][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getPaymentDetails][END]");
        return res;
    }

    /**
     * 결제 정보 조회 (미납/잔여요금 납부)
     * @param custCode
     * @return
     * @throws BFException
     */
    public Response getPaymentInfo(HttpSession session, String custCode) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][getPaymentInfo][START]");

        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            if (UtilManager.isEmptyOrNull(custCode)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            Map<String, Object> resMap = new HashMap<String, Object>();

            JSONObject paramJson = new JSONObject();
            paramJson.put("custCode", custCode);

            ApiResponseVO vo = apiService.callErpApiServer("/api/v1/mybf/getProductPayInfo", HttpMethod.GET, paramJson);
            if (vo.getStatusCode().equals("200")) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(vo.getData().toString());
                resMap = mapper.readValue(node.toString(), new TypeReference<Map<String, Object>>() {});

                res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
            } else {
                res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            }

        } catch (BFException be) {
            log.error("[MYINFO][SERVICE][MyinfoService][getPaymentInfo][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            log.error("[MYINFO][SERVICE][MyinfoService][getPaymentInfo][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][getPaymentInfo][END]");

        return res;
    }

    /**
     * 본인 인증 후 고객 연락처 변경
     * @param params
     * @param params
     * @return
     * @throws BFException
     */
    public Response updatePhone(HttpSession session, Map<String, Object> params) throws BFException {
        log.info("[MYINFO][SERVICE][MyinfoService][updatePhone][START]");

        BFResponse res = null;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_NOT_LOGIN);
            }

            // 로그인 정보
            String userIdx = session.getAttribute(Constants.SESSION_USER_IDX).toString();
            String sessName = session.getAttribute(Constants.SESSION_NAME).toString();

            // 본인인증
            String certName = params.get("certName").toString();
            String certPhoneNo = params.get("certPhoneNo").toString();

            if (sessName.equals(certName)) {

                String[] phoneNo = UtilManager.parsePhone(certPhoneNo);
                String cell1 = phoneNo[0];
                String cell2 = phoneNo[1];
                String cell3 = phoneNo[2];

                // 내 정보 조회
                Myinfo myinfo = new Myinfo();
                myinfo.setUserIdx(userIdx);
                myinfo = myinfoDao.getMyinfo(myinfo);
                myinfo.setCell1(cell1);
                myinfo.setCell2(cell2);
                myinfo.setCell3(cell3);

                log.info("myinfo : {}", myinfo.toString());

                // 정보 수정
                Map resMap = this.updatePersonal(myinfo);
                if (resMap.get(Constants.RESULT_CODE).equals(Constants.SUCCESS) && Integer.parseInt(resMap.get(Constants.RESULT_DATA).toString()) > 0) {
                    res = new BFResponse(ResultCodes.RET_SUCCESS);
                } else {
                    throw new BFException(ResultCodes.ERR_PHONE_CHG_FAIL);
                }

            } else {
                res = new BFResponse(ResultCodes.ERR_USER_NAME);
            }

        } catch (BFException be) {
            log.error("[MYINFO][SERVICE][MyinfoService][updatePhone][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[MYINFO][SERVICE][MyinfoService][updatePhone][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MYINFO][SERVICE][MyinfoService][updatePhone][END]");
        return res;
    }

    public Map<String, Object> paymentComplete (HttpServletRequest request, HttpSession session) throws Exception {

        boolean isFinish = false;

        String userIdx = (String) session.getAttribute(Constants.SESSION_USER_IDX);
        String serviceIdx = null;
        String siteType = UtilManager.getSiteType(request);

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 나이스페이 결제
        PayResultVO vo = null;
        /*try {

            vo = payService.payResult(request);

            log.info("[MYINFO][SERVICE][MyinfoService][paymentComplete][vo] : {}", vo.toString());

            // 결제 성공
            if (vo.isPaySucces()) {

                String[] reqReserved = vo.getReqReserved().split("\\|");
                String custCode = reqReserved[0];
                String payCnt = reqReserved[1];

                Map<String, Object> cardInfo = myinfoDao.getCardInfoUnierp(vo.getCardName());
                String erpCardCo = (String) cardInfo.get("cardCoCd");

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("custCode", custCode);            // 고객번호
                jsonObj.put("payCnt", payCnt);                // 납입회차
                jsonObj.put("Amt", vo.getAmt());              // 납부금액
                jsonObj.put("authCode", vo.getAuthCode());      // 카드승인번호
                jsonObj.put("acctNo", vo.getCardNo());          // 카드번호
                jsonObj.put("coCd", erpCardCo);                 // 카드사코드

                String callUrl = "/api/v1/mybf/updatePaymentInfo";

                Iterator<?> keys = jsonObj.keys();
                List<String> params = new ArrayList<String>();;
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    params.add(key + "=" + jsonObj.get(key));

                }
                callUrl += "?";
                callUrl += params.stream().collect(Collectors.joining("&"));

                ApiResponseVO apiVo = apiService.callErpApiServer(callUrl, HttpMethod.POST, jsonObj);

                if (apiVo.getStatusCode().equals("200")) {

                    // ERP 취소 방법이 없어서 우선 완료 처리
                    isFinish = true;

                    // 관리자 메모 등록
                    Map<String, Object> detailVo = new HashMap<String, Object>();
                    detailVo.put("orderNumber", vo.getMoid());
                    detailVo.put("contents", "ORDER NO : "+ custCode);
                    orderService.saveAdminMemo(detailVo);

                    // 결제 정보 업데이트
                    Map<String, Object> payResultParams = new HashMap<String, Object>();
                    payResultParams.put("tranState", "2");
                    payResultParams.put("resultCode", vo.getResultCode());
                    payResultParams.put("resultMsg", vo.getResultMsg());
                    payResultParams.put("pgAccountNbr", "");
                    payResultParams.put("pgTradeNbr", vo.getTid()+"|"+vo.getAuthCode()+"|"+vo.getAuthDate());
                    payResultParams.put("moid", vo.getMoid());
                    payResultParams.put("paymentDate", "Y"); // 공백이 아니면 now
                    productDao.updatePayResult(payResultParams);

                    // 주문처리 시스템 로그 등록
                    Map<String, Object> logParams = new HashMap<String, Object>();
                    logParams.put("orderNumber", vo.getMoid());
                    logParams.put("moid", vo.getMoid());
                    logParams.put("log", "시스템");
                    logParams.put("tranState", "2");
                    productDao.insertOrderLog(logParams);

                } else {
                    isFinish = false;
                }

            } else {
                // 결제 실패
                isFinish = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("[MYINFO][SERVICE][MyinfoService][paymentComplete][ERROR] : {}", e.getMessage());
        } finally {

            if (!isFinish) {
                // 실패시 결제 취소
                CancelRequestVO cancelVo = new CancelRequestVO();
                cancelVo.setTid(vo.getTid());
                cancelVo.setCancelAmt(vo.getAmt());
                cancelVo.setPartialCancelCode("0");
                cancelVo.setMoid(vo.getMoid());
                cancelVo.setCancelMsg("서버 오류로 인한 자동 취소");

                payService.cancelRequest(cancelVo);

                vo.setPaySucces(false);
                vo.setResultCode("SERVER_ERROR");
                vo.setResultMsg("서버 오류로 인한 결제 취소");

                // 주문 상태 업데이트
                Map<String, Object> updateParams = new HashMap<String, Object>();
                updateParams.put("tranState", "13");
                updateParams.put("ordNbr", vo.getMoid());
                productDao.updateOrdersTranState(updateParams);

                // 주문처리 시스템 로그 등록
                Map<String, Object> logParams = new HashMap<String, Object>();
                logParams.put("orderNumber", vo.getMoid());
                logParams.put("moid", vo.getMoid());
                logParams.put("log", "시스템");
                logParams.put("tranState", "13");
                productDao.insertOrderLog(logParams);

                log.error("[MYINFO][SERVICE][MyinfoService][paymentComplete][ERROR][CANCEL PAYMENT][END][Moid] : {}", vo.getMoid());
            }

            // 결제 결과 담아서 리턴
            resultMap.put("payResult", vo);
        }*/

        return resultMap;
    }

    // 총 페이지 수 구하기
    private int getTotalpage(int count) {
        int totalPage;

        if (count == 0) {
            totalPage = 0;
        } else if (count % LIST_SIZE == 0) {
            totalPage = count / LIST_SIZE;
        } else {
            totalPage = (int)(count / LIST_SIZE) + 1;
        }

        return totalPage;
    }

    // 페이지 from, to 셋팅하기
    private Myinfo setPageFromTo(Myinfo myinfo) {
        int page = myinfo.getPage();
        int pageFrom = (page - 1) * LIST_SIZE;
        int pageTo = page * LIST_SIZE;

        myinfo.setPageFrom(pageFrom);
        myinfo.setPageTo(pageTo);

        myinfo.setFirstLimitIndex(pageFrom);
        myinfo.setLastLimitIndex(LIST_SIZE);

        return myinfo;
    }

    // 두 날짜와의 개월수 차이 구하기 (yyyy-MM-DD -> yyyyMMdd)
    public String _diffDate(String paramMonth, String paramMonth2) throws ParseException {
        // 무상as종료 날짜 - 현재날짜
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        Date date1 = sdf.parse(paramMonth);
        Date date2 = sdf.parse(paramMonth2);
        String szSDate = sdf2.format(date1);
        String szEDate = sdf2.format(date2);
        int sYear= Integer.parseInt(szSDate.substring(0,4));
        int sMonth = Integer.parseInt(szSDate.substring(4,6));
        int eYear = Integer.parseInt(szEDate.substring(0,4));
        int eMonth = Integer.parseInt(szEDate.substring(4,6));
        int month_diff = (eYear - sYear)* 12 + (eMonth - sMonth);

        String returnMonth = Integer.toString(Math.abs(month_diff));
        System.out.println(returnMonth);

        return returnMonth;
    }

}
