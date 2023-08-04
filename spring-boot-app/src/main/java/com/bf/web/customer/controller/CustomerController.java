package com.bf.web.customer.controller;

import com.bf.common.util.AES256Util;
import com.bf.common.util.RequestUtil;
import com.bf.common.util.Util;
import com.bf.common.util.UtilManager;
import com.bf.web.customer.service.CustomerService;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import com.bf.web.member.vo.Orders;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class CustomerController {

    @Resource private CustomerService customerService;

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



}
