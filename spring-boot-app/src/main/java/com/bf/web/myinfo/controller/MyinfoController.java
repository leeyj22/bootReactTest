package com.bf.web.myinfo.controller;

import com.bf.common.BFException;
import com.bf.common.Constants;
import com.bf.common.ResultCodes;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.Sha256;
import com.bf.common.util.Util;
import com.bf.common.util.UtilManager;
import com.bf.web.myinfo.service.MyinfoService;
import com.bf.web.myinfo.vo.Myinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MyinfoController {

    @Value(value = "${system.domain}")
    String systemDomain;

    @Autowired
    private MyinfoService myinfoService;

    /**
     * 주문/배송 조회
     * @param session
     * @param params myinfo
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="/myinfo/selectOrderList.json")
    public Map selectOrderList(HttpSession session, @RequestParam Map<String, Object> params) {

        Map resultMap = new HashMap();

        // 회원
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            params.put("userIdx", (String)session.getAttribute(Constants.SESSION_USER_IDX));
            return myinfoService.selectOrderList(params);
            // 비회원
        } else if(session.getAttribute(Constants.SESSION_MEMBER_TYPE) != null && session.getAttribute(Constants.SESSION_MEMBER_TYPE).equals("n")) {
            return myinfoService.selectOrderList(params);
            // 세션 만료
        } else {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");
            return resultMap;
        }
    }

    /**
     * 주문 상세 내역 조회
     * @param session
     * @param params myinfo
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="/myinfo/selectOrderDetail.json")
    public Map selectOrderDetail(HttpSession session, @RequestParam Map<String, Object> params) {

        Map resultMap = new HashMap();

        // 회원
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            params.put("userIdx", (String)session.getAttribute(Constants.SESSION_USER_IDX));
            return myinfoService.selectOrderDetail(params);
            // 비회원
        } else if(session.getAttribute(Constants.SESSION_MEMBER_TYPE) != null && session.getAttribute(Constants.SESSION_MEMBER_TYPE).equals("n")) {
            params.put("memberType", "n");
            params.put("userName", params.get("user_name"));
            return myinfoService.selectOrderDetail(params);
        } else {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");
            return resultMap;
        }
    }

    /**
     * 주문/배송 조회
     * @param session
     * @param params myinfo
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="/myinfo/selectCancelOrderList.json")
    public Map selectCancelOrderList(HttpSession session, @RequestParam Map<String, Object> params) {

        Map resultMap = new HashMap();

        // 회원
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            params.put("userIdx", (String)session.getAttribute(Constants.SESSION_USER_IDX));
            return myinfoService.selectCancelOrderList(params);
            // 비회원
        } else if(session.getAttribute(Constants.SESSION_MEMBER_TYPE) != null && session.getAttribute(Constants.SESSION_MEMBER_TYPE).equals("n")) {
            return myinfoService.selectCancelOrderList(params);
            // 세션 만료
        } else {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");
            return resultMap;
        }
    }

    /**
     * 취소/반품/교환 변경
     * @param session
     * @param params myinfo
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="/myinfo/ordCancel.json")
    public Map ordCancel(HttpSession session, @RequestParam Map<String, Object> params){

        // 회원
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            String userId = (String)session.getAttribute(Constants.SESSION_USER_ID);
            params.put("userId", userId);
            params.put("changeBy", params.get("userName")+"("+userId+")");
            myinfoService.insertStatusLog(params);
            return myinfoService.updateTranState(params);
            // 비회원
        } else if(session.getAttribute(Constants.SESSION_MEMBER_TYPE) != null && session.getAttribute(Constants.SESSION_MEMBER_TYPE).equals("n")) {
            params.put("changeBy", params.get("userName")+"(비회원)");
            myinfoService.insertStatusLog(params);
            return myinfoService.updateTranState(params);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/getNomemberOrderList.json")
    public Map getNomemberOrderList(HttpSession session, Myinfo myinfo){
        return myinfoService.getNomemberOrderList(myinfo);
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/selectQnaList.json")
    public Map selectQnaList(HttpSession session, Myinfo myinfo){
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));

            return myinfoService.selectQnaList(myinfo);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/selectQnaDetail.json")
    public Map selectQnaDetail(HttpSession session, @RequestParam(value = "groups") Integer groups){

        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            Myinfo myinfo = new Myinfo();
            myinfo.setGroups(groups);
            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));
            return myinfoService.selectQnaDetail(myinfo);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/insertQna.json")
    public Map insertQna(HttpServletRequest request, HttpSession session, Myinfo myinfo){
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));
            myinfo.setWriter((String)session.getAttribute(Constants.SESSION_NAME));
            try {
                myinfo.setIp(Util.ipaddr(request));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myinfoService.insertQna(myinfo);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/selectPersonalList.json")
    public Map selectPersonalList(HttpSession session, Myinfo myinfo){
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));

            return myinfoService.selectPersonalList(myinfo);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @ResponseBody
    @RequestMapping(value="/myinfo/updatePersonal.json")
    public Map updatePersonal(HttpServletRequest request, HttpSession session, Myinfo myinfo){

        Sha256 sha256 = new Sha256();
        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            myinfo.setUserIdx((String)session.getAttribute(Constants.SESSION_USER_IDX));
            myinfo.setWriter((String)session.getAttribute(Constants.SESSION_NAME));
            String pwd = myinfo.getPwd();
            if(!pwd.equals("N")){
                myinfo.setPwd(sha256.enc(pwd));
            }

            try {
                myinfo.setIp(Util.ipaddr(request));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myinfoService.updatePersonal(myinfo);
        } else {
            Map resultMap = new HashMap();
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "세션 만료. 로그인이 필요합니다.");

            return resultMap;
        }
    }

    @RequestMapping(value="/myinfo/nomember_info.bf")
    public ModelAndView nomember_info(HttpSession session, Model model){
        ModelAndView mav = new ModelAndView();

        if (session.getAttribute(Constants.SESSION_MEMBER_TYPE) == null) {
            mav.setViewName("/web/member/login");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date(System.currentTimeMillis());
            mav.addObject("today", sdf.format(now));
            model.addAttribute("memberType", "n");
            mav.setViewName("/web/myinfo/member_info");
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value ="/myinfo/selectAfterService.json")
    public List<HashMap<String, String>> selectAfterService(@RequestParam(value="user_idx", required=true) String userIdx
            , HttpServletRequest request){
        return myinfoService.selectAfterService(userIdx);
    }

    //본인인증 여부 확인
    @ResponseBody
    @RequestMapping(value ="/myinfo/checkCertify.json")
    public HashMap<String, String> checkCertify(HttpServletRequest request){
        return myinfoService.checkCertify(request);
    }

    //본인인증 업데이트
    @ResponseBody
    @RequestMapping(value ="/myinfo/updateMemberCertYn.json")
    public int updateMemberCertYn(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", request.getParameter("userId"));
        params.put("birthDay", request.getParameter("birthDay"));
        return myinfoService.updateMemberCertYn(params);
    }

    //erp 구매&렌탈 이력(사용자만 조회)
    @ResponseBody
    @RequestMapping(value ="/myinfo/selectMyRentalList_Inst.json")
    public List<HashMap<String, String>> selectMyRentalList_Inst(HttpServletRequest request) throws Exception{
        return myinfoService.selectMyRentalList_Inst(request);
    }

    /**
     * <상품평 유효제품 조회>
     * 1. 로그인 시 가져옴
     * 2. ERP 구매&렌탈 이력 조회
     * 3. 위의 list를 Map으로 넘겨서 홈페이지 DB의 goods_review 테이블에서 조회
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value ="/myinfo/selectMyRentalList_Review.json")
    public List<HashMap<String, String>> selectMyRentalList_Review(HttpServletRequest request) throws Exception{
        return myinfoService.selectMyRentalList_Review(request);
    }

    /**
     * SERVICE 가접수 내역 건수 조회
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/myinfo/selectTemporarySCVCount.json")
    public int selectTemporarySCVCount(HttpServletRequest request) throws Exception{
        return myinfoService.selectTemporarySCVCountUNIERP(request);
    }

    /**
     * 제품 조회(분해조립/마이페이지>제품사용정보)
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="/myinfo/getMyAllProductList.json", method= RequestMethod.GET, produces="application/json")
    public Response getMyAllProductList(HttpServletRequest request, @RequestParam Map<String, Object> param) throws Exception{
        return myinfoService.getMyAllProductList(request, param);
    }

    /**
     * 분해조립 목록 조회
     * @param request
     * @param params
     * @return
     * @throws BFException
     */
    @ResponseBody
    @RequestMapping(value ="/myinfo/getAssembleList.json", method = RequestMethod.GET)
    public Response getAssembleList(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params) throws BFException {

        log.info("[MYINFO][CONTROLLER][MyinfoController][getAssembleList][START]");
        Response res = myinfoService.getAssembleList(session, params);
        log.info("[MYINFO][CONTROLLER][MyinfoController][getAssembleList][END]");

        return res;
    }

    /**
     * 분해조립 취소
     * @param session
     * @param request
     * @param params
     * @return
     * @throws BFException
     */
    @ResponseBody
    @RequestMapping(value ="/myinfo/cancelAssemble.json", method = RequestMethod.POST)
    public Response cancelAssemble(HttpSession session, HttpServletRequest request, @RequestBody Map<String, Object> params) throws BFException {

        log.info("[MYINFO][CONTROLLER][MyinfoController][cancelAssemble][START]");

        Response res = null;

        if (session.getAttribute(Constants.SESSION_USER_IDX) != null) {
            res = myinfoService.cancelAssemble(params);
        } else if (session.getAttribute(Constants.SESSION_MEMBER_TYPE).equals("n")) {
            res = myinfoService.cancelAssemble(params);
        } else {
            res = new BFResponse(ResultCodes.ERR_NOT_LOGIN);
        }

        log.info("[MYINFO][CONTROLLER][MyinfoController][cancelAssemble][END]");

        return res;
    }

    /**
     * 분해조립 목록 조회
     * @param request
     * @param payOrdNo
     * @return
     * @throws BFException
     */
    @ResponseBody
    @RequestMapping(value ="/myinfo/getAssembleInfo.json", method = RequestMethod.GET)
    public Response getAssembleInfo(HttpSession session, HttpServletRequest request, @RequestParam(required = true) String payOrdNo) throws BFException {

        log.info("[MYINFO][CONTROLLER][MyinfoController][getAssembleInfo][START]");

        Response res = myinfoService.getAssembleInfo(session, payOrdNo);

        log.info("[MYINFO][CONTROLLER][MyinfoController][getAssembleInfo][END]");

        return res;
    }

    /**
     * 제품사용정보
     * @param request
     * @return
     * @throws BFException
     */
    @RequestMapping(value ="/myinfo/product_info", method = RequestMethod.GET)
    public ModelAndView productInfo(HttpSession session, HttpServletRequest request) throws BFException {

        log.info("[MYINFO][CONTROLLER][MyinfoController][productInfo][START]");

        ModelAndView mav = new ModelAndView();

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", session.getAttribute(Constants.SESSION_NAME));
            params.put("phone", session.getAttribute(Constants.SESSION_PHONE));
            mav.addObject("productList", myinfoService.getMyAllProductList(request, params).getData().get("productList")) ;

        } catch (Exception e) {
            // TODO: handle exception
        }
        mav.setViewName("/web/myinfo/product_info");

        log.info("[MYINFO][CONTROLLER][MyinfoController][productInfo][END]");

        return mav;
    }

}
