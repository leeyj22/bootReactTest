package com.bf.web.customer.controller;

import com.bf.common.BFException;
import com.bf.common.Constants;
import com.bf.common.Consts;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.*;
import com.bf.web.customer.service.CustomerService;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import com.bf.web.customer.vo.OnlineVO;
import com.bf.web.member.vo.Orders;
import com.bf.web.myinfo.vo.Myinfo;
import com.bf.web.terms.service.TermsService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.XML;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class CustomerController {

    @Resource private CustomerService customerService;

    @Autowired private TermsService termsService;

    private final static String aes_key = "bfservicekey!@12";

    @ResponseBody
    @RequestMapping(value = "/customer/noticeList")
    public List<NoticeVO> getNoticeList (){

        List<NoticeVO> getNoticeList = customerService.selectNoticeList();
        return getNoticeList;
    }

    @ResponseBody
    @RequestMapping(value="/customer/selectNoticeList.json", method = RequestMethod.POST)
    public Map selectNoticeList(HttpServletRequest request, NoticeVO noticeVO) throws Exception {

        Map params = RequestUtil.getParameterMap(request);

        int startPageNum = Integer.parseInt(request.getParameter("curPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int curPage = (startPageNum-1)*pageSize;
        noticeVO.setPageSize(startPageNum*pageSize);
        noticeVO.setCurPage(curPage);

        noticeVO.setFirstLimitIndex(curPage);
        noticeVO.setLastLimitIndex(pageSize);
        noticeVO.setSearchStr(params.get("searchStr").toString());

        List<NoticeVO> noticeMap = customerService.selectNoticeList();
        List<NoticeVO> noticeNormalMap = customerService.selectNoticeNormalList(noticeVO);

        Map rMap = new HashMap<String, Object>();

        rMap.put("noticeList", noticeMap);
        rMap.put("noticeNormalList", noticeNormalMap);

        return rMap;
    }

    @RequestMapping(value="/customer/noticeDetail")
    public ModelAndView noticeDetail(HttpServletRequest request, @RequestParam(value = "idx", required = true) String idx,
                                     HttpSession session){
        ModelAndView mav = new ModelAndView();
        // 조회수 증가
        customerService.updateHitCountForNotice(idx);
        mav.addObject("idx", idx);
        mav.setViewName("/web/customer/noticeDetail");

        return mav;
    }

    @ResponseBody
    @RequestMapping(value="/customer/noticeDetailInfo.json", method = {RequestMethod.GET, RequestMethod.POST})
    public NoticeVO noticeDetailInfo(HttpServletRequest request, @RequestParam(value = "idx", required = true) String idx){
        NoticeVO info = customerService.selectNoticeDetail(idx);
        Util util = new Util();

        if (info.getContents() != null && info.getContents() != "") {
            info.setContents(util.unescape(info.getContents()));
        }

        return info;
    }

    /**
     * Q&A 리스트 조회
     *
     * @param request
     * @param noticeVO
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/customer/selectQnaList.json", method = {RequestMethod.GET, RequestMethod.POST})
    public List<NoticeVO> selectQnaList( HttpServletRequest request, NoticeVO noticeVO) throws Exception {
        //페이징 세팅
        int startPageNum = Integer.parseInt(request.getParameter("curPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int curPage = (startPageNum-1)*pageSize;
        noticeVO.setPageSize(startPageNum*pageSize);
        noticeVO.setCurPage(curPage);

        noticeVO.setFirstLimitIndex(curPage);
        noticeVO.setLastLimitIndex(pageSize);

        //검색 파라미터 세팅
        noticeVO.setTab(request.getParameter("tab"));
        noticeVO.setSearchStr(request.getParameter("searchStr"));
        noticeVO.setSearchTxtType(request.getParameter("searchTxtType"));

        //조회
        List<NoticeVO> result = customerService.selectQnaList(noticeVO);
        Util util = new Util();
        for(NoticeVO temp : result) {
            temp.setContents(util.unescape(temp.getContents()));
        }

        return result;
    }

    /**
     * Q&A 상세 페이지
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/customer/serviceQnaDetail")
    public ModelAndView serviceQnaDetail(Model model, HttpServletRequest request, HttpSession session){

        NoticeVO noticeVO = new NoticeVO();
        String groups = request.getParameter("groups");

        // 수정 시 게시글 작성자와 Session의 UserIdx 체크용 조회문
        String bbsIdx = request.getParameter("bbsIdx");
        String userIdx = customerService.selectUserIdx(bbsIdx);
        ModelAndView mav = new ModelAndView();

        mav.addObject("groups", groups);
        mav.addObject("bbsIdx", bbsIdx);
        mav.addObject("userIdx", userIdx);
        mav.setViewName("/web/customer/serviceQnaDetail");

        return mav;
    }

    /**
     * Q&A 상세 데이터 조회
     *
     * @param session
     * @param request
     * @param bbsIdx
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="/customer/selectQnaDetail.json")
    public Map selectQnaDetail(HttpSession session, HttpServletRequest request, @RequestParam(value = "bbsIdx") String bbsIdx){
//		if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
        Map<String, String> params = new HashMap<String,String>();
        String groups = request.getParameter("groups");
        //NoticeVO noticeVO = new NoticeVO();
        params.put("groups", groups);
        params.put("bbsIdx", bbsIdx);
        params.put("userIdx", (String)session.getAttribute(Constants.SESSION_USER_IDX));

        return customerService.selectQnaDetail(params);
    }

    /**
     * Q&A 등록 페이지
     *
     * @param model
     * @param bbsIdx
     * @return
     */
    @RequestMapping(value= {"/customer/serviceQnaWrite", "/customer/service_qna"})
    public ModelAndView serviceQnaWrite(Model model, @RequestParam(value = "bbsIdx", required = false) String bbsIdx,
                                        HttpSession session){

        ModelAndView mav = new ModelAndView();

        // 로그인
        Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX, Constants.SESSION_NAME, Constants.SESSION_PHONE});
        if ((boolean) paramsCheck.get(Consts.CHECK)) {
            Map<String, String> params = new HashMap<String, String>();

            if (bbsIdx != null ) {
                params.put("bbsIdx", bbsIdx);
                mav.addObject("bbsIdx", bbsIdx);
                mav.addObject("detail", customerService.serviceQnaWriteData(params));
            }
//            mav.addObject("serviceTerms", termsService.getContent(22));
            mav.setViewName("/web/customer/serviceQnaWrite");
        } else {
            mav.setViewName("/web/customer/serviceQna");
        }

        return mav;
    }

    /**
     * 비공개글 비밀번호 확인
     *
     * @param request
     * @param session
     * @param myinfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customer/qnaPwdCheck.json")
    public Map qnaPwdCheck(HttpServletRequest request, HttpSession session, Myinfo myinfo){

        Sha256 sha256 = new Sha256();
        Map resultMap = new HashMap();
        Map dataMap = new HashMap();

        myinfo.setBbsIdx(request.getParameter("bbsIdx"));

        String qnaPwd = myinfo.getQnaPwd();
        if(!qnaPwd.equals("N")){
            myinfo.setQnaPwd(sha256.enc(qnaPwd));
        }

        dataMap = customerService.qnaPwdCheck(myinfo);

        if (dataMap != null) {
            resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
            resultMap.put(Constants.RESULT_DATA, dataMap);
        }else{
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
        }

        return resultMap;
    }

    /**
     * Q&A 게시글 삭제
     *
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customer/qnaDelete.json")
    public Map qnaDelete(HttpServletRequest request, HttpSession session){

        Map resultMap = new HashMap();

        String userIdx = (String)session.getAttribute(Constants.SESSION_USER_IDX).toString();
        String writeUser = (String)request.getParameter("writeUser").toString();
        String groups = request.getParameter("groups");

        //관리자 답변과 함께 처리하기 위해 그룹번호를 파라미터로 담음
        if (userIdx.equals(writeUser)) {
            customerService.qnaDelete(groups);
            resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
            resultMap.put(Constants.RESULT_MSG, "삭제가 완료되었습니다.");
        } else{
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "타인의 게시글을 삭제할 수 없습니다.");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/customer/certCheck")
    public Response certCheck(HttpServletRequest request) throws UnsupportedEncodingException {

        HttpSession session = request.getSession(false);

        Response response = new BFResponse();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> certMap = UtilManager.checkCertOrLogin(session);
        log.info("[CUSTOMER][CONTROLLER][CustomerController][assembleAndDisassembleStep1][certMap] : {}", certMap.toString());

        if ((boolean) certMap.get(Consts.CHECK)) {
            result.put("userName", URLDecoder.decode(certMap.get(Consts.USER_NAME).toString(), "UTF-8"));
            result.put("phoneNo", certMap.get(Consts.USER_PHONE));
            result.put("certYn", "Y");
            result.put("isCertify", true);
        } else {
            result.put("certYn", "N");
            result.put("isCertify", false);
            result.put("accessMsg", "잘못된 접근입니다. 정상적인 경로를 통해 본인인증 후 이용해 주세요.");
        }
        response.setData(result);
        return response;
    }

    @ResponseBody
    @GetMapping(value = "/customer/getTerms")
    public BFResponse getTerms(@RequestParam(value = "term_index") int termsIndex){
        BFResponse response = new BFResponse();
        Map<String, Object> map = new HashMap<>();
        map.put("serviceTerms", termsService.getContent(termsIndex));
        response.setData(map);
        return response;
    }

    // 서비스 접수
    /*@RequestMapping(value="/customer/after-service")
    public ModelAndView afterServiceCert(HttpServletRequest request, HttpSession session){
        ModelAndView mav = new ModelAndView();

        int cert = 0;
        String userName = "";
        String phoneNo = "";

        // 본인인증 확인
        Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_CERT, Constants.SESSION_CERT_NAME, Constants.SESSION_CERT_PHONE});
        if ((boolean) paramsCheck.get(Consts.CHECK) && session.getAttribute(Constants.SESSION_CERT).equals("Y")) {
            cert += 1;
            userName = session.getAttribute(Constants.SESSION_CERT_NAME).toString();
            phoneNo = session.getAttribute(Constants.SESSION_CERT_PHONE).toString();
        }

        // 로그인
        paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX, Constants.SESSION_NAME, Constants.SESSION_PHONE});
        if ((boolean) paramsCheck.get(Consts.CHECK)) {
            cert += 1;
            userName = session.getAttribute(Constants.SESSION_NAME).toString();
            phoneNo = session.getAttribute(Constants.SESSION_PHONE).toString();
        }

        if (cert > 0) {
            mav.addObject("userName", userName);
            mav.addObject("phoneNo", phoneNo);

            // 개인정보수집 · 이용 동의 약관
            mav.addObject("serviceTerms34", termsService.getContent(34));

            // 마케팅 활용 동의 약관
            mav.addObject("serviceTerms33", termsService.getContent(33));
            mav.setViewName("/web/customer/afterService");
        } else {
            mav.setViewName("redirect:/customer/after-service/intro");
        }

        return mav;
    }*/

    // 서비스 접수 확인 - 내역
    @RequestMapping(value="/customer/afterService/{encSeq}", method = RequestMethod.POST)
    public ModelAndView getMyAfterService(HttpServletRequest request, @PathVariable Map<String, String> pathVariables, RedirectAttributes attr) throws BFException {

        Response res = customerService.getMyAfterService(request, pathVariables.get("encSeq"));

        ModelAndView mav = new ModelAndView("web/customer/afterServiceInfo");
        if (res.getStatus().get("code").equals("200")) {
            mav.addObject("result", res.getData());
            mav.addObject("status", res.getStatus());
            mav.addObject("encSeq", pathVariables.get("encSeq"));
        } else {
            attr.addFlashAttribute("message", "접수된 서비스 내역이 없습니다.");
            mav.setViewName("redirect:/customer/afterService/"+pathVariables.get("encSeq"));
        }

        return mav;
    }

    // 서비스 접수 등록 API
    @ResponseBody
    @RequestMapping(value="/customer/saveAfterService.json", method = {RequestMethod.GET, RequestMethod.POST})
    public String onlineService (HttpServletRequest request, OnlineVO vo, HttpSession session) throws Exception {

        vo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));
        vo.setCertUserContact((String)session.getAttribute(Constants.SESSION_CERT_PHONE));
        vo.setCertUserName((String)session.getAttribute(Constants.SESSION_CERT_NAME));

        String result = customerService.insertOnlineService(vo);
        return result;
    }

    // FAQ List 안마의자 전용 API
    @ResponseBody
    @RequestMapping(value="/customerHelp/selectChiarFaqListApi.json", method = {RequestMethod.GET, RequestMethod.POST})
    public List<HashMap<String, String>> selectChiarFaqListApi(HttpServletRequest request) throws Exception {

        Map params = new HashMap<>();
        params.put("classification", request.getParameter("classification"));
        params.put("keyword", request.getParameter("keyword"));

        return customerService.selectChairFaqListApi(params);
    }

    // FAQ 상세 안마의자 전용 API
    @ResponseBody
    @RequestMapping(value="/customerHelp/selectChiarFaqDetailApi.json", method = {RequestMethod.GET, RequestMethod.POST})
    public List<NoticeVO> selectChiarFaqDetailApi(HttpServletRequest request) throws Exception {
        Util utils = new Util();
        Map pMap = utils.getParameterMap(request);

        NoticeVO paramsVO = new NoticeVO();

        paramsVO.setBbsIdx(request.getParameter("bbsIdx"));
        List<NoticeVO> faqList = customerService.selectChairFaqDetailApi(paramsVO);

        for(NoticeVO _temp : faqList) {
            _temp.setContents(utils.unescape(_temp.getContents()));
        }
        return faqList;
    }

    @ResponseBody
    @RequestMapping(value="/customer/selectFaqList", method = {RequestMethod.GET, RequestMethod.POST})
    public List<FaqVO> selectFaqList(HttpServletRequest request,
                                     @RequestParam(value = "classification", required = true) String classification
                                    ,@RequestParam(value = "search", required = true) String search) throws Exception {
        Util utils = new Util();
        Map pMap = utils.getParameterMap(request);

        switch (classification) {
            case "trouble" :
                classification = "동작안됨";
                break;
            case "inquiry" :
                classification = "상품문의";
                break;
            case "rental" :
                classification = "렌탈/구매";
                break;
            case "payment" :
                classification = "결제/배송";
                break;
            case "service" :
                classification = "서비스";
                break;
            case "benefits" :
                classification = "혜택";
                break;
            case "return" :
                classification = "취소/환불/반품";
                break;
            case "member" :
                classification = "회원";
                break;
            case "others" :
                classification = "기타";
                break;
            case "remote" :
                classification = "리모컨";
                break;
            case "supplies" :
                classification = "소모품";
                break;
        }

        FaqVO paramVO = new FaqVO();
        paramVO.setContents(search);
        paramVO.setClassification(classification);
        List<FaqVO> faqList = customerService.selectFaqList(paramVO);

        for(FaqVO _temp : faqList) {
            _temp.setContents(utils.unescape(_temp.getContents()));
        }
        return faqList;
    }

    //자료실 상세
    //TODO : 조회수 증가 때문에 가져옴
    @RequestMapping(value="/customer/companyReferenceDetail")
    public ModelAndView companyReferenceDetail(@RequestParam(value = "bbsIdx", required = false) String bbsIdx,
                                               HttpSession session){
        ModelAndView mav = new ModelAndView();

        // 조회수 증가
        customerService.updateHitCountForNotice(bbsIdx);
        mav.addObject("bbsIdx", bbsIdx);
        mav.setViewName("/web/customer/companyReferenceDetail");
        return mav;
    }
/*

    //자료실 리스트 조회
    @ResponseBody
    @RequestMapping(value="/customer/selectReferenceList.json")
    public BbsGalleryTotalVO selectReferenceList(
            @RequestParam Map<String, Object> params
    ){
        BbsGalleryTotalVO result = new BbsGalleryTotalVO();

        int startPageNum = Integer.parseInt(params.get("curPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int curPage = (startPageNum-1)*pageSize;

        params.put("searchStr", params.get("searchStr").toString());
        params.put("firstLimitIndex", curPage);
        params.put("lastLimitIndex", pageSize);

        List<BbsGalleryVO> list = customerService.selectReferenceList(params);
        result.setList(list);

        return result;
    }

    // 자료실 카테고리 조회
    @ResponseBody
    @RequestMapping(value="/customer/getReferenceStep2List.json")
    public Map<String, Object> getReferenceStep2List(){
        Map<String, Object> resMap = customerService.getReferenceStep2List();
        return resMap;
    }


    //자료실 내용 조회
    @ResponseBody
    @RequestMapping(value="/customer/selectReferenceDetail.json")
    public NoticeVO selectReferenceDetail(HttpServletRequest request){
        String bbsIdx = request.getParameter("bbsIdx");

        NoticeVO result = customerService.selectReferenceDetail(bbsIdx);
        Util util = new Util();

        result.setContents(util.unescape(result.getContents()));

        return result;
    }
*/

    /**
     * 간편 서비스 조회 인증
     *
     * @param params 전화번호, 고객번호
     * @return json data 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/customer/serviceAuth.json")
    public void serviceAuth(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject obj = new JSONObject();

        log.info("[MO][CUSTOMER][CONTROLLER][serviceAuth] Start ~");
        log.info("[MO][CUSTOMER][CONTROLLER][serviceAuth] params {} : " + params);

        int result = customerService.selectServiceAuth(params);

        log.info("[MO][CUSTOMER][CONTROLLER][serviceAuth] result {} : " + result);
        log.info("[MO][CUSTOMER][CONTROLLER][serviceAuth] END");
        try{
            if(result >= 1){
                obj.put("msg", "success");
            }else{
                obj.put("msg", "failed");
            }
        }catch(Exception e){
            obj.put("msg", "error");
        }finally{
            response.getWriter().write(obj.toJSONString());
        }

    }

    /**
     * 간편 서비스 조회 상세화면
     *
     * @param telNum 전화번호
     * @param custNum 고객번호
     * @return resultMap 수취자명, 수취인연락처, 설치주소, 설치제품, 설치예정일
     * @throws Exception
     */
    @RequestMapping(value="/customer/service_info")
    public String serviceInfo(@RequestParam(value = "telNum", required = false) String telNum,
                              @RequestParam(value = "custNum", required = false) String custNum,
                              @RequestParam(value = "receiveSeq", required = false) String receiveSeq, Model model,
                              HttpSession session) throws Exception{

        log.info("=================================================");
        log.info("[CUSTOMER][CONTROLLER][service_info] >>> Start");
        log.info("=================================================");

        AES256Util aes256 = new AES256Util(aes_key);

        Map<String, Object> map = new HashMap<String, Object>();
        String returnUrl = null;
        String custName  = "";
        String tel_no    = "";
        String hphone_no = "";

        map.put("telNum", telNum);
        map.put("custNum", custNum);
        map.put("receiveSeq", receiveSeq);

        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap = customerService.selectServiceInfo(map);

        //log.info(" resultMap : " + (String) resultMap.toString());

        if(resultMap != null){
            if((String)resultMap.get("IN_USER_NAME") != null){
                custName = (String)resultMap.get("IN_USER_NAME");
            }
            if((String)resultMap.get("TEL_NO") != null){
                tel_no = aes256.dec((String)resultMap.get("TEL_NO"));
            }
            if((String)resultMap.get("HPHONE_NO") != null){
                hphone_no = aes256.dec((String)resultMap.get("HPHONE_NO"));
            }

            resultMap.put("TEL_NO", tel_no);
            resultMap.put("HPHONE_NO", hphone_no);

            log.info("[CUSTOMER][CONTROLLER][service_info] resultMap {} : " + resultMap);

            model.addAttribute("resultMap", resultMap);
            returnUrl = "/web/customer/service_info";
        }else{
            returnUrl = "/web/customer/service_login";
        }

        log.info("[CUSTOMER][CONTROLLER][service_info] >>> END");

        return returnUrl;
    }

    /**
     * 제품 및 이전/설치 서비스
     */
    //erp 구매&렌탈 이력
    @ResponseBody
    @RequestMapping(value ="/customerHelp/selectMyRentalList_Inst")
    public String selectMyRentalList_Inst(HttpServletRequest request) throws Exception{
        JSONArray jsonArray = UtilManager.getJsonArrayFromList(customerService.selectMyRentalList_Inst(request));
        return String.valueOf(jsonArray);
    }

    /**
     * 간편배송 상세화면
     *
     * @param telNum: 전화번호
     * @param custNum:고객번호
     * @return resultMap 수취자명, 수취인연락처, 설치주소, 설치제품, 설치예정일
     * @throws Exception
     */
    @RequestMapping(value={"/customerHelp/delivery_info","/myinfo/delivery_info.bf"})
    public String deliveryInfo(@RequestParam(value = "telNum", required = false) String telNum,
                               @RequestParam(value = "custNum", required = false) String custNum, Model model,
                               HttpSession session) throws Exception{

        log.info("=================================================");
        log.info("[CUSTOMER][CONTROLLER][delivery_info] >>> Start");
        log.info("=================================================");

        Map<String, Object> map = new HashMap<String, Object>();
        String returnUrl = null;
        String custName  = "";
        String tel_no    = "";
        String hphone_no = "";

        map.put("telNum", telNum);
        map.put("custNum", custNum);

        Map<String, Object> resultMap = new HashMap<String, Object>();

//		resultMap = customerService.selectDeliveryInfo(map);
        resultMap = customerService.selectDeliveryInfoUNIERP(map);

        if(resultMap != null){
            if((String)resultMap.get("CUST_NAME") != null){
                custName = (String)resultMap.get("CUST_NAME");
            }
            if((String)resultMap.get("TEL_NO") != null){
                tel_no = (String)resultMap.get("TEL_NO");
            }
            if((String)resultMap.get("HPHONE_NO") != null){
                hphone_no = (String)resultMap.get("HPHONE_NO");
            }

            Map<String, Object> erpMap = new HashMap<String, Object>();
            erpMap.put("custName", custName);
            erpMap.put("telNo", tel_no);
            erpMap.put("hphoneNo", hphone_no);

//			Map<String, Object> giftInfo = customerService.selectGiftInfo(erpMap);
            Map<String, Object> giftInfo = customerService.selectGiftInfoUNIERP(erpMap);

            log.info("[CUSTOMER][CONTROLLER][delivery_info] giftInfo {} : " + giftInfo);
            if(giftInfo != null){
                log.info("[CUSTOMER][CONTROLLER][delivery_info] custName {} : " + custName);
                log.info("[CUSTOMER][CONTROLLER][delivery_info] telNo    {} : " + tel_no);
                log.info("[CUSTOMER][CONTROLLER][delivery_info] hphoneNo {} : " + hphone_no);
                log.info("[CUSTOMER][CONTROLLER][delivery_info] giftInfo {} : " + giftInfo);

                model.addAttribute("giftInfo", giftInfo);
            }
            model.addAttribute("resultMap", resultMap);
            returnUrl = "/web/customer/delivery_info";
        }else{
            returnUrl = "/web/customer/delivery_login";
        }

        log.info("=================================================");
        log.info("[CUSTOMER][CONTROLLER][delivery_info] >>> END");
        log.info("=================================================");

        return returnUrl;
    }

    /**
     * 간편배송 인증
     *
     * @param params 전화번호, 고객번호
     * @return json data
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/customer/deliveryAuth.json")
    public void deliveryAuth(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONObject obj = new JSONObject();

//		int result = customerService.selectDeliveryAuth(params);		//기존ERP
        int result = customerService.selectDeliveryAuthUNIERP(params);	//UNIERP Delivery 간편배송 인증
        try{
            if(result >= 1){
                obj.put("msg", "success");
            }else{
                obj.put("msg", "failed");
            }
        }catch(Exception e){
            obj.put("msg", "error");
        }finally{
            response.getWriter().write(obj.toJSONString());
        }

    }

    /**
     * 배송일 지정 및 주소변경
     *
     * @param params (배송시퀀스NO, 고객번호, 기존주소, 설치주소, 설치희망일)
     * @param response (변경 성공 여부)
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/customerHelp/changeData")
    public void changeData(@RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception {

        JSONObject obj = new JSONObject();

        int result = 0;

        int historyResult = customerService.insertHistoryData(params);

        if(historyResult == 1){

            // 주소 AREA 코드 조회
//			String area = customerService.selectAreaCode(params);
            String area = customerService.selectAreaCodeUNIERP(params);
            params.put("SHIPPING_NO", params.get("shipSeq").toString());
            // 변경내용 이력
            SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
            Date time = new Date();
            String date = dateFormat.format(time);

//			String companyCheck = "<br/><font color='blue'>(" +params.get("custName")+ ")지정일변경("+  date +")</font>";
            String companyCheck = "<br/><font color='blue'>(" +params.get("custName")+ ")지정일변경 ->"+ params.get("expectDt").toString() +"("+  date +")</font>";

            //배송DB 업데이트
            params.put("area", area);					// 구역 코드
            params.put("companyCheck", companyCheck);	// 이력 ex. (변경자명)지정일변경(날짜)

            log.info("[MO][CUSTOMER][CustomerController][changeData] updateCustInfoUNIERP params : " + params.toString());
            customerService.insertHistoryDataUNIERP(params);		//UNIERP
//			result = customerService.updateCustInfo(params);
            result = customerService.updateCustInfoUNIERP(params);
            //로지메이트 전송용 sp 호출
            customerService.LMSendApi(params);
            log.info("[MO][CUSTOMER][CustomerController][changeData] updateCustInfoUNIERP 배송일 지정 및 주소변경 결과 : " + result);

        }else{
            result = 0;
        }

        try{
            if(result == 1){
                obj.put("msg", "success");
            }else{
                obj.put("msg", "failed");
            }
        }catch(Exception e){
            obj.put("msg", "error");
        }finally{
            response.getWriter().write(obj.toJSONString());
        }
    }

    //배송일 지정, 주소 및 사은품 주소 변경
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/customerHelp/updateAddr")
    public void updateAddr(@RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception {

        JSONObject obj = new JSONObject();

        int result = 0;

        int historyResult = customerService.insertHistoryData(params);

        if(historyResult == 1){
            int beforeResult = 0;

            params.put("SHIPPING_NO", params.get("shipSeq").toString());

            // 주소 AREA 코드 조회
//			String area = customerService.selectAreaCode(params);		//구ERP
            String area = customerService.selectAreaCodeUNIERP(params);	//UNIERP

            // 변경내용 이력
            SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
            Date time = new Date();
            String date = dateFormat.format(time);

//			String companyCheck = "<br/><font color='blue'>(" +params.get("custName")+ ")지정일변경"+ date +")</font>";
            String companyCheck = "<br/><font color='blue'>(" +params.get("custName")+ ")지정일변경 ->"+ params.get("expectDt").toString() +"("+  date +")</font>";
            //배송DB 정보 업데이트
            params.put("area", area);					// 구역 코드
            params.put("companyCheck", companyCheck);	// 이력 ex. (변경자명)지정일변경(날짜)

            log.info("[MO][CUSTOMER][CustomerController][updateAddr] updateCustInfoUNIERP params : " + params.toString());

//			beforeResult = customerService.updateCustInfo(params);				//구ERP
            customerService.insertHistoryDataUNIERP(params);		//UNIERP
            beforeResult = customerService.updateCustInfoUNIERP(params);		//UNIERP
            //로지메이트 전송용 sp 호출
            customerService.LMSendApi(params);
            log.info("[MO][CUSTOMER][CustomerController][updateAddr] updateCustInfoUNIERP 배송일 지정, 주소 및 사은품 주소 변경 : " + beforeResult);

            //사은품 주소 변경
            if(beforeResult == 1){

//				params.put("custCode", params.get("bodyNo").toString().replaceAll("-", ""));
                params.put("custCode", params.get("custCode").toString().replaceAll("-", ""));


                log.info("[MO][CUSTOMER][CustomerController][updateAddr] 사은품 주소 변경 params :: " + params);

//				result = customerService.updateGiftAddr(params);
                result = customerService.updateGiftAddrUNIERP(params);
                result = 1;
                log.info("[MO][CUSTOMER][CustomerController][updateAddr] 사은품 주소 변경 성공 상태 값 :: " + result);
            }else{
                result = 0;
                log.info("[MO][CUSTOMER][CustomerController][updateAddr] 사은품 주소 변경 실패 상태 값 :: " + result);
            }
        }else{
            result = 0;
        }


        try{
            if(result >= 1){
                obj.put("msg", "success");
            }else{
                obj.put("msg", "failed");
            }
        }catch(Exception e){
            obj.put("msg", "error");
        }finally{
            response.getWriter().write(obj.toJSONString());
        }
    }


    //국경일 조회
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/customerHelp/findHoliday")
    public void findHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject obj = new JSONObject();

        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String yearAndMonth = year+month;

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=xN0SvX7dqt12fRtdhI%2FlDuoy6JQArNBfBEilAj3mL27W10zVfdSQPvitTLJIfbciQ5RKGGMkyfXbPk%2BVAjJRKw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /**/
        urlBuilder.append("&" + URLEncoder.encode("totalCount","UTF-8") + "=" + URLEncoder.encode("16", "UTF-8")); /**/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        log.info("[CUSTOMER][CONTROLLER][findHoliday] Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        //국경일외에 DB에서 배송 제외일 조회
//        List<Map<String, Object>> list = customerService.selectExceptDay(yearAndMonth);	// 추후 배송DB 확인해서 넣기
        List<Map<String, Object>> list = new ArrayList<>();

        org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());

        try {
            obj.put("data", xmlJSONObj);
            obj.put("dataOne", list);
        } catch (Exception e) {
            e.toString();
        }finally{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(obj.toJSONString());
        }

    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/customerHelp/findErpHoliday")
    public void findErpHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject obj = new JSONObject();

        //국경일외에 DB에서 배송 제외일 조회
        org.json.JSONArray list = customerService.selectExceptDay(request); 	// 추후 배송DB 확인해서 넣기
        try {
            obj.put("data", list);
        } catch (Exception e) {
            e.toString();
        }finally{
            response.getWriter().write(obj.toJSONString());
        }

    }

    //제품리스트 (erp)
    @ResponseBody
    @RequestMapping(value="/customer/getGoodsList.json")
    public List<HashMap<String, String>> getGoodsList(HttpServletRequest request){
        return customerService.getGoodsList(request);
    }

   /* *//**
     * 이전설치 비용 조회
     * @param params
     * @return
     * @throws Exception
     *//*
    @ResponseBody
    @RequestMapping(value="/customer/getTransferPrice.json", method=RequestMethod.POST, produces="application/json")
    public Response getTransferPrice(@RequestBody Map<String, Object> params) throws Exception {
        log.info("[CONTROLLER][CustomerController][getTransferPrice][START]");
        System.out.println("params   2 :: " + params.toString());

        Response res = customerService.getTransferPrice(params);

        log.info("[CONTROLLER][CustomerController][getTransferPrice][END]");

        return res;
    }*/

    //TODO : UtilManager.checkCertOrLogin(session); << 로그인이나 본인인증 했는지 여부 체크하는거... 이거 로직에 추가?
    /**
     * 조립분해 - 신청 step1
     * @return
     */
    @RequestMapping(value="/customer/assemble-and-disassemble_step1")
    public ModelAndView assembleAndDisassembleStep1(HttpSession session, @RequestParam Map<String, Object> params) throws Exception {
        log.info("[CONTROLLER][CustomerController][assembleAndDisassembleStep1][START]");

        ModelAndView mav = new ModelAndView();

        Map<String, Object> certMap = UtilManager.checkCertOrLogin(session);
        log.info("[CUSTOMER][CONTROLLER][CustomerController][assembleAndDisassembleStep1][certMap] : {}", certMap.toString());

        if ((boolean) certMap.get(Consts.CHECK)) {
            mav.addObject("userName", URLDecoder.decode(certMap.get(Consts.USER_NAME).toString(), "UTF-8"));
            mav.addObject("phoneNo", certMap.get(Consts.USER_PHONE));
            mav.addObject("certYn", "Y");
        } else {
            mav.addObject("accessMsg", "잘못된 접근입니다. 정상적인 경로를 통해 본인인증 후 이용해 주세요.");
        }

        mav.setViewName("/web/customer/assemble-and-disassemble_step1");

        log.info("[CONTROLLER][CustomerController][assembleAndDisassembleStep1][END]");
        return mav;
    }

    /**
     * 조립분해 등록
     * @param request
     * @param params
     * @return
     * @throws BFException
     *//*
    @ResponseBody
    @RequestMapping(value ="/customer/insertAssemble.json", method = RequestMethod.POST)
    public Response insertAssemble(HttpServletRequest request, @RequestParam Map<String, Object> params) throws BFException {

        log.info("[CONTROLLER][CustomerController][insertAssemble][START]");

        params.put("device", UtilManager.getDevice(request));

        Response res = customerService.insertAssemble(params, null);

        log.info("[CONTROLLER][CustomerController][insertAssemble][END]");

        return res;
    }

    *//**
     * 분해조립 홈페이지 주문번호 채번
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/customer/getPayOrdNo.json", method = RequestMethod.POST)
    public Response getPayOrdNo(HttpServletRequest request, @RequestParam Map<String, Object> params) throws BFException {
        log.info("[CONTROLLER][CustomerController][getPayOrdNo][START]");

        log.info("[CONTROLLER][CustomerController][getPayOrdNo][params] : {}", params.toString());

        Response res = customerService.getPayOrdNo(request, params);

        log.info("[CONTROLLER][CustomerController][getPayOrdNo][END]");

        return res;
    }

    // 자료실 파일 다운로드 
    @RequestMapping(value="/customer/filedownload")
    public String filedownload(@RequestParam("document_nm") String document_nm
            , HttpSession session
            , HttpServletRequest request
            , HttpServletResponse response
            , ModelAndView mav) throws Throwable {


        int result = 0;
        try {
            String manualFilePath = filePath + "/upload/bbs/append_file/";

            FileDownload fileDown = new FileDownload(); //파일다운로드 객체생성

            log.info("[CUSTOMER][CustomerController][filedownload][req] : {}", request.toString());
            log.info("[CUSTOMER][CustomerController][filedownload][res] : {}", response.toString());

            result = fileDown.filDown(request, response, manualFilePath , document_nm, document_nm); // 파일 다운로드 req, res, 실제 파일 경로, 실제 파일명, 다운로드할 이름

            log.info("[CUSTOMER][CustomerController][filedownload][fileDown] : {}", fileDown);
            //fileDown.filDown(req, res, "/home/hosting_users/body8980/tomcat/webapps/upload/bbs/append_file" + "/" , document_nm, "test.pdf"); //파일다운로드 

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e.toString());
        }
        if(result < 1 ){
            // 다운로드 실패 : 서버에 파일이 없음
            response.setContentType("text/html; charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.println("<script>alert('FAIL : 파일이 존재하지 않습니다. 고객지원을 통해 문의 부탁드립니다.'); location.href='/customer/manual.bf';</script>");
            out.flush();
        }

        return null;
    }*/


}
