package com.bf.web.member.controller;

import com.bf.common.CommonCodes;
import com.bf.common.Constants;
import com.bf.common.element.Response;
import com.bf.common.util.Sha256;
import com.bf.common.util.Util;
import com.bf.web.member.bean.Member;
import com.bf.web.member.bean.SiteInfo;
import com.bf.web.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class MemberController {

    @Resource private MemberService memberService;

    @Value(value="${system.auth.url}")
    public static String systemAuthUrl;
    @Value(value="${system.auth.client}")
    public static String systemAuthClient;
    @Value(value="${system.host.name}")
    public static String systemHostName;
    @Value(value="${system.admin.url}")
    public static String systemAdminUrl;

    Sha256 sha256 = new Sha256();

    @RequestMapping(value="/member/login")
    public ModelAndView login(HttpSession session, HttpServletRequest request, @RequestParam Map<String, Object> params){

        ModelAndView mav = new ModelAndView();
        String loginUrl = systemAuthUrl + "/auth/common/login?client_id=" + systemAuthClient + "&redirect_uri=https://" + systemHostName + "/member/login_return";

        String refer = request.getHeader("referer");
        String returnUrl = "/";

        if(!Util.isEmptyOrNull(params.get("returnUrl"))){
            returnUrl = String.valueOf(params.get("returnUrl"));
        }else if(!Util.isEmptyOrNull(refer)){
            returnUrl = refer;
        }

        mav.addObject("returnUrl", returnUrl);
        mav.setViewName("redirect:" + loginUrl);

        return mav;
    }

    @RequestMapping(value="/member/login_old")
    public ModelAndView login_old(){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/web/member/login_old");

        return mav;
    }

    /**
     * 통합로그인 returnUrl 처리용 빈 페이지
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/member/login_return", method = RequestMethod.GET)
    public Response login_return(HttpSession session, @RequestParam Map<String, Object> params
            , HttpServletResponse response
            , @CookieValue(value= CommonCodes.CookieCode.ACCESS_TOKEN, required = false) Cookie token
            , @CookieValue(value=CommonCodes.CookieCode.ACCESS_TOKEN_RT, required = false) Cookie tokenRt
    ) throws Exception{

        if (null == session.getAttribute(Constants.SESSION_SKIN_MODE)) {
            session.setAttribute(Constants.SESSION_SKIN_MODE, "basic");
        }
        log.info("[MEMBER][CONTROLLER][MemberController][login_return][START]");

        Response res = memberService.checkLoginTk(session, token, response);

        //토큰 만료 시 재생성 및 로그인 처리
        if (!CommonCodes.ResultCode.SUCCESS.equals(res.getStatus().get("code"))) {
            params.put("tokenRt", tokenRt);
            System.out.println(">>>> params tokenRt c : " + params.get("params"));

            if("ERR_MS_4001".equals(res.getStatus().get("code"))) {
//                mav.setViewName("redirect:/member/loginFail");
                return res;
            }

            Response reToken = memberService.checkLoginRt(session, params);

            if (null==reToken.getData() || "ERR_MS_5000".equals(reToken.getData().get("code"))) {
//                mav.setViewName("redirect:/member/loginFail");
                return res;
            }

            token = new Cookie("bftk", reToken.getData().get("accessToken").toString());

            res = memberService.checkLoginTk(session, token, response);

        }

        log.info("[MEMBER][CONTROLLER][MemberController][login_return][END]");

        // URL 처리용 빈 페이지
        // 화면에서 로컬 스토리지 - returnUrl 받아서 요청한 페이지로 이동해줌
//        mav.setViewName("/web/member/login_return");
        return res;
    }

    // 비회원 주문확인 로그인 페이지
    @RequestMapping(value="/member/login_nomember")
    public ModelAndView loginNomember(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/web/member/login_nomember");

        return mav;
    }

    @RequestMapping(value="/member/loginSocial")
    public ModelAndView loginSocial(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/web/member/login_social");

        return mav;
    }

    @RequestMapping(value="/member/socialCallback")
    public ModelAndView socialCallback(){

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/web/member/social_callback");

        return mav;
    }

    @RequestMapping(value="/member/memberOut")
    public ModelAndView memberOut(HttpSession session){
        ModelAndView mav = new ModelAndView();

        if (session.getAttribute(Constants.SESSION_USER_IDX) == null) {
            mav.setViewName("/web/member/login");
        } else {
            mav.addObject("memberType", "y");
            mav.addObject("user_name", session.getAttribute(Constants.SESSION_NAME));
            mav.addObject("user_phone", session.getAttribute(Constants.SESSION_PHONE));
            mav.addObject("user_level_name", session.getAttribute(Constants.SESSION_LEVEL_NAME));
            mav.setViewName("/web/member/memberOut");
        }

        return mav;
    }

    @RequestMapping(value="/member/memberOutCheck")
    public ModelAndView memberOutCheck(HttpSession session){
        ModelAndView mav = new ModelAndView();

        if (session.getAttribute(Constants.SESSION_USER_IDX) == null) {
            mav.setViewName("/web/member/login");
        } else {
            mav.addObject("memberType", "y");
            mav.addObject("user_name", session.getAttribute(Constants.SESSION_NAME));
            mav.addObject("user_phone", session.getAttribute(Constants.SESSION_PHONE));
            mav.addObject("user_level_name", session.getAttribute(Constants.SESSION_LEVEL_NAME));
            mav.setViewName("/web/member/memberOutCheck");
        }

        return mav;
    }

    //회원탈퇴
    @ResponseBody
    @RequestMapping(value="/member/memberOutCheck.json")
    public Map memberOutCheck(HttpServletRequest request, Member member, HttpSession session){
        Map resultMap =  new HashMap();
        if(!Util.isEmpty(member.getPwd())) {
            resultMap = memberService.loginCheck(member);
            if(resultMap.get(Constants.RESULT_CODE).equals(Constants.SUCCESS)){
                memberService.deleteMemberInfo(member);
                //세션 비움
                session.invalidate();
            }
        } else {
            memberService.deleteMemberInfo(member);
            //세션 비움
            session.invalidate();
        }

        return resultMap;
    }

    @RequestMapping(value="/member/logout")
    public ModelAndView logout(HttpSession session, HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        if(request.getParameter("levelCode") != null && request.getParameter("levelCode").equals("99")) {
//            mav.setViewName("redirect:/admin/login");
            mav.setViewName("redirect:" + systemAdminUrl + "/login.bf");
        } else {
            mav.setViewName("redirect:/");
        }

        session.invalidate();

        return mav;
    }


    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/member/loginCheck", method = RequestMethod.POST)
    public ModelAndView loginCheck(HttpSession session, HttpServletRequest request, Member member,
                                   @RequestParam(value = "token", required = true) String token,
                                   @RequestParam(value = "type", required = true) String type) throws Exception {

        Map resultMap =  new HashMap();
        ModelAndView mav = new ModelAndView();
        if(type.equals("USER")){
            resultMap = memberService.loginCheck(member);
        }else{
            resultMap = memberService.loginCheck_social(member.getUserId());
            if(resultMap.get(Constants.RESULT_CODE).equals(Constants.SUCCESS)){
//                memberService.updateTbSocial(member.getUserId(), member.getEmail(), token, type);
            }
        }

        if(resultMap.get(Constants.RESULT_CODE).equals(Constants.SUCCESS)){
            Member memberData = new Member();
            memberData = (Member) resultMap.get(Constants.RESULT_DATA);

            if (memberData != null && "1".equals(memberData.getApproval())) {
                String authorizationIp 	= memberData.getAuthorizationIp();
                String remoteAddr		= Util.ipaddr(request);

                if (!Util.isEmpty(authorizationIp)) {
                    System.out.println("authorizationIp : " + authorizationIp);
                    System.out.println("remoteAddr : " + remoteAddr);

                    if (!remoteAddr.equals(authorizationIp)) {
//                        mav.setViewName("redirect:/admin/login?result=55");
                        mav.setViewName("redirect:" + systemAdminUrl + "/login.bf?result=55");
                        return mav;
                    }
                }

                // 승인된 회원
                session.setMaxInactiveInterval(60*60);	//초 단위, 1시간
                session.setAttribute(Constants.SESSION_USER_ID, memberData.getUserId());
                session.setAttribute(Constants.SESSION_USER_IDX, memberData.getUserIdx());
                session.setAttribute(Constants.SESSION_LEVEL_CODE, memberData.getLevelCode());
                session.setAttribute(Constants.SESSION_LEVEL_NAME, memberData.getLevelName());
                session.setAttribute(Constants.SESSION_ADDR_1, memberData.getAddr1());
                session.setAttribute(Constants.SESSION_ADDR_2, memberData.getAddr2());
                session.setAttribute(Constants.SESSION_NAME, memberData.getName());
                session.setAttribute(Constants.SESSION_EMAIL, memberData.getEmail());
                session.setAttribute(Constants.SESSION_MEMBER_TYPE, "y");
                session.setAttribute(Constants.SESSION_PHONE, memberData.getPhone());
                session.setAttribute(Constants.SESSION_TYPE, type);
                // 장바구니 업데이트
                memberData.setSessionId(session.getId());
                System.out.println("========== session.getId(): "+session.getId());
//                /* 데이터베이스 세션아이디 타입이 숫자 형식이라서 비교가 안 됨.. */
//                memberService.updateBasketUser(memberData);
//
//                // 최종 로그인 시간, 방문회수, ip 업데이트
//                memberData.setIp(Util.ipaddr(request));
//                memberService.updateMemberVisit(memberData);

                mav.addObject("name", memberData.getName());

                if("99".equals(member.getLevelCode())) {
//                    mav.setViewName("redirect:/admin/main");
                    mav.setViewName("redirect:" + systemAdminUrl + "/main");

                } else {
                    mav.setViewName("redirect:/member/login?result=1");
                }

            } else {
                // 미승인회원
//                SiteInfo siteInfo = memberService.getSiteInfo();
//                mav.addObject("siteTel", siteInfo.getSiteTel());
                mav.setViewName("redirect:/member/login?result=99");
            }
        } else {
            if("99".equals(member.getLevelCode())) {
//                mav.setViewName("redirect:/admin/login?result=0");
                mav.setViewName("redirect:" + systemAdminUrl + "/login.bf?result=0");
            } else {
                mav.setViewName("redirect:/member/login?result=0");
            }
        }

        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/member/socialMemberCheck.json", method = RequestMethod.POST)
    public int socialMemberCheck(@RequestParam(value = "socialId", required = true) String socialId) {
//        return memberService.socialMemberCheck(socialId);
        return 0;
    }


}
