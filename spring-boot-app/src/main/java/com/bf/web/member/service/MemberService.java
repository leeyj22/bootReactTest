package com.bf.web.member.service;

import com.bf.common.*;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.AES256Util;
import com.bf.common.util.Sha256;
import com.bf.common.util.UtilManager;
import com.bf.web.member.dao.MemberDao;
import com.bf.web.member.vo.Member;
import com.bf.web.member.vo.MemberVO;
import com.bf.web.member.vo.Orders;
import com.bf.web.member.vo.SiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MemberService {

    @Value(value="${system.auth.url}")
    public String systemAuthUrl;

    @Value(value = "${system.withdraw.url}")
    String withdrawUrl;
    @Value(value = "${system.withdraw.secretKey}")
    String withdrawKey;

    @Resource
    private MemberDao memberDao;

    private final static String aes_key = "a&9fql3@jDAE2f8#";

    // 통합 로그인 API 호출- accessToken 만료 전
    public Response checkLoginTk(HttpSession session, Cookie token, HttpServletResponse response) throws BFException{
        BFResponse res;
        try{
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][START]");

            String accessToken = "";
            if(!UtilManager.isEmptyOrNull(token)){
                accessToken = token.getValue();
            }
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][accessToken]    : {}", accessToken);
            String resultCode = null;

            // 토큰 복호화
            JSONObject jsonObject = new JSONObject();
            AES256Util aes256 = new AES256Util(aes_key);
            jsonObject.put("accessToken", aes256.dec(accessToken));

            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][jsonObject] : {}", jsonObject.toString());

            // 헤더값
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));

            // 통합 로그인 URL
            String callUrl = systemAuthUrl + "/api/auth/token";

            JSONObject resultJson = null;
            resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonObject);

            if (null!=resultJson.getJSONObject("status")) {
                // 조회 결과 데이터
                JSONObject resultStatus = resultJson.getJSONObject("status");
                resultCode = resultStatus.get("code").toString();

                if ("200".equals(resultCode)) {
                    JSONObject resultData   = resultJson.getJSONObject("data");

                    log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][resultData]          : {}", resultData.toString());
                    log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][resultJson]   status : {}", resultJson.get("status").toString());
                    log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][resultStatus] result : {}", resultStatus.get("code").toString());

                    String phone = "";
                    String email = "";

                    // 핸드폰 번호 세팅
                    phone = resultData.get("cell1").toString() + resultData.get("cell2").toString() + resultData.get("cell3").toString();

                    // 이메일 파라미터 체크
                    if(resultData.has("email") && resultData.get("email") != null) {
                        email = resultData.get("email").toString();
                    }

                    // 세션처리
                    session.setMaxInactiveInterval(60*60);	//초 단위, 1시간
                    session.setAttribute(Constants.SESSION_USER_ID, resultData.get("userId").toString());
                    session.setAttribute(Constants.SESSION_USER_IDX, resultData.get("userIdx").toString());
                    session.setAttribute(Constants.SESSION_LEVEL_CODE, resultData.get("authCode").toString());
                    session.setAttribute(Constants.SESSION_LEVEL_NAME, "회원");	// 통합로그인 토큰 조회 api return 파라미터 추가해주셔야함
                    session.setAttribute(Constants.SESSION_NAME, resultData.get("userName").toString());
                    session.setAttribute(Constants.SESSION_EMAIL, email);
                    session.setAttribute(Constants.SESSION_MEMBER_TYPE, "y");
                    session.setAttribute(Constants.SESSION_PHONE, phone);
                    session.setAttribute(Constants.SESSION_TYPE, "USER");

                    System.out.println("통합 로그인 API 조회 성공");
                    res = new BFResponse(ResultCodes.RET_SUCCESS);
                } else {
                    System.out.println("통합 로그인 API 조회 실패");
                    res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
                }
            } else {
                res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            }

        }catch (Exception e){
            log.error("[MEMBER][SERVICE][MemberService][login_return][checkLoginTk][ERROR] : {}", e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            e.printStackTrace();
        }
        return res;
    }


    // 통합 로그인 API 호출- accessToken 만료 후 재발급
    public Response checkLoginRt(HttpSession session, @RequestParam Map<String, Object> params) throws BFException {
        BFResponse res;

        try{
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][START]");
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][params] params : {}" + params.toString());
            String refreshToken = "";
            String clientId = "";

            if(null == params.get("tokenRt")) {
                res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
                return res;
            }

            refreshToken = params.get("tokenRt").toString();
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][params] refreshToken : {}", refreshToken.toString());

            JSONObject jsonObject = new JSONObject();
            AES256Util aes256 = new AES256Util(aes_key);

            if (!UtilManager.isEmptyOrNull(refreshToken)) {
                refreshToken = aes256.dec(refreshToken);
            }
            jsonObject.put("refreshToken", refreshToken);
            log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][jsonObject] : {}", jsonObject.toString());

            // 헤더값
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));

            // 통합 로그인 URL
            String callUrl = systemAuthUrl + "/api/auth/token/refresh";

            JSONObject resultJson = null;
            resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonObject);
            if (!UtilManager.isEmptyOrNull(resultJson)) {
                // 조회 결과 데이터
                log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][resultData]          : {}", resultJson.toString());
                String accessToken = resultJson.get("access_token").toString();
                params.put("accessToken", aes256.enc(accessToken));
                log.info("[MEMBER][SERVICE][MemberService][login_return][checkLoginRt][resultJson]   status : {}", resultJson.get("access_token").toString());
            }

            if (!UtilManager.isEmptyOrNull(resultJson)) {
                System.out.println("통합 로그인 API 조회 성공");
                res = new BFResponse(ResultCodes.RET_SUCCESS, params);
            } else {
                System.out.println("통합 로그인 API 조회 실패");
                res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            }

        }catch (Exception e){
            log.error("[MEMBER][SERVICE][MemberService][login_return][loginCheck_Api][ERROR] : {}", e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            e.printStackTrace();
        }
        return res;
    }

    @Transactional
    public Map loginCheck(Member member){
        Sha256 sha256 = new Sha256();
        Map resultMap = new HashMap();

        member.setPwd(sha256.enc(member.getPwd()));

        try {
            member = memberDao.loginCheck(member);

            if(member != null){
                resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
                resultMap.put(Constants.RESULT_DATA, member);
            }else{
                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            }

        } catch (Exception e) {
            resultMap.put(Constants.RESULT_CODE, Constants.ERROR);
            e.printStackTrace();
        }

        return resultMap;
    }

    public Map loginCheck_social(String userId) {

        Map resultMap = new HashMap();
        Map dataMap = new HashMap();
        Map paramMap = new HashMap();
        paramMap.put("userId", userId);

        try {
            Member member = memberDao.loginCheck_social(paramMap);

            if(dataMap != null){
                resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
                resultMap.put(Constants.RESULT_DATA, member);
            }else{
                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            }

        } catch (Exception e) {
            resultMap.put(Constants.RESULT_CODE, Constants.ERROR);
            e.printStackTrace();
        }

        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map loginCheckNomember(Map<String, Object> params) {
        Map resultMap = new HashMap();

        try {
            Orders order = memberDao.loginCheckNomember(params);
            if(!UtilManager.isEmptyOrNull(order)){
                resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
                resultMap.put(Constants.RESULT_DATA, order);
            } else {
                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            }
        } catch (Exception e) {
            resultMap.put(Constants.RESULT_CODE, Constants.ERROR);
            e.printStackTrace();
        }

        return resultMap;
    }

    /*
     * 회원 쿠폰 삭제 (DELETE)
     * TABLE : COUPON_ISSUE
     */
        public int memberCouponDel(Member member) {
//        return memberDao.delete("memberCouponDel", member);
        return 0;
        }

    /*
     * 회원 후기 삭제 (DELETE)
     * TABLE : goods_review
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public int memberReviewDel(Member member) {
        return 0;
//        return memberDao.delete("memberReviewDel", member);
    }

    /*
     * 회원 1:1문의 삭제 (DELETE)
     * TABLE : COUPON_ISSUE
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public int memberQnaDel(Member member) {
        return 0;
//        return memberDao.delete("memberQnaDel", member);
    }

    /*
     * 회원 서비스접수 삭제 (DELETE)
     * TABLE : online_service
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public int memberOnServiceDel(Member member) {
        return 0;
//        return memberDao.delete("memberOnServiceDel", member);
    }

    /*
     * 소셜 가입 히스토리 삭제 (DELETE)
     * TABLE : SOCIAL_HISTORY
     */
    public int socialHistoryDel(Member member) {
        return 0;
//        return memberDao.delete("socialHistoryDel", member);
    }

    /*
     * 회원 탈퇴 (Delete)
     * TABLE : MEMBER
     */
    @SuppressWarnings({"rawtypes", "unchecked", "rawtypes", "unchecked"})
    @Transactional
    public Map memberOutCheck(Member member) {

        Map resultMap = new HashMap();
        try {
            // 회원 탈퇴 처리
//            member = memberDao.selectOne("memberOutCheck", member);
//            if(member != null){
//                resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
//                resultMap.put(Constants.RESULT_DATA, member);
//            }else{
//                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
//            }

        } catch (Exception e) {
            resultMap.put(Constants.RESULT_CODE, Constants.ERROR);
            e.printStackTrace();
        }

        return resultMap;
    }

    public SiteInfo getSiteInfo() {
        return memberDao.getSiteInfo();
    }

    public int updateBasketUser(Member member) {
        return 0;
//        return memberDao.update("updateBasketUser", member);
    }

    public int updateMemberVisit(Member member) {
        return 0;
//        return memberDao.update("updateMemberVisit", member);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getUserId(Member member) {
        Map resultMap = new HashMap();
//        Member m = memberDao.selectOne("getUserId", member);
//
//        if (m != null) {
//            resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
//            resultMap.put(Constants.RESULT_DATA, m);
//            resultMap.put(Constants.RESULT_MSG, m.getName()+"님의 아이디는 "+m.getUserId()+" 입니다.");
//        } else {
//            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
//            resultMap.put(Constants.RESULT_MSG, "고객님의 일치하는 회원정보가 없습니다.");
//        }

        return resultMap;
    }

    //TODO : 사용하는지 확인 필요
    @Transactional
    public Map savePointModule(Map params) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();
        Map resultMap = new HashMap<>();
        int updateMemberPoint = 0;
        int insertPointLog = 0;

        if( params.get("section")!=null ) {
            switch ((String)params.get("section")) {
                case "2":
                    params.put("log", "회원가입 축하");
                    break;
                case "4":
                    params.put("log", "상품취소완료");
                    break;
                case "7":
                    params.put("log", "상품후기 작성");
                    break;
                case "9":
                    params.put("log", "상품구매완료");
                    break;
                default:
                    resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
                    resultMap.put(Constants.RESULT_MSG, "저장가능한 포인트 종류가 아닙니다.");
                    break;
            }
        } else {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "포인트 종류를 입력해주세요(파라미터명 : section).");
            return resultMap;
        }

        if((params.get("section").equals("4")||params.get("section").equals("9"))&&params.get("ord_nbr")==null) {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "주문번호를 입력해주세요(파라미터명 : ord_nbr).");
            return resultMap;
        }

        if(params.get("point")!=null) {
            int point = 0;
            try {
                point = Integer.parseInt(params.get("point").toString());
            } catch (Exception e) {
                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
                resultMap.put(Constants.RESULT_MSG, "입력된 포인트가 숫자 형식이 아닙니다.");
            }
            if(point<0) {
                resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
                resultMap.put(Constants.RESULT_MSG, "적립 시에는 입력된 포인트가 0보다 커야합니다.");
            }
        } else {
            resultMap.put(Constants.RESULT_CODE, Constants.FAIL);
            resultMap.put(Constants.RESULT_MSG, "포인트를 입력해주세요(파라미터명 : point).");
            return resultMap;
        }

        if(resultMap.get(Constants.RESULT_CODE)==null) {
            params.put("user_idx", session.getAttribute(Constants.SESSION_USER_IDX));
            insertPointLog = memberDao.insertPointLog(params);
            if(insertPointLog>0) {
                updateMemberPoint = memberDao.updateMemberPoint(params);
            }
            if(updateMemberPoint>0) {
                resultMap.put(Constants.RESULT_CODE, Constants.SUCCESS);
                resultMap.put(Constants.RESULT_MSG, Constants.Message.INSERT_SUCCESS);
            }
        }
        return resultMap;
    }

    public HashMap<String, String> checkId(String id) {
        return memberDao.checkId(id);
    }

    public String getClientIP(HttpServletRequest request) {

        String ip = request.getHeader("X-FORWARDED-FOR");

        if(ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
        }
        if(ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }
        return ip;
    }
    public void insertSocialLogin(String userId, String email, String token, String type) {
        Map param = new HashMap();

        param.put("userId", userId);
        param.put("email", email);
        param.put("token", token);
        param.put("type", type);

        memberDao.insertSocialLogin(param);
    }

    public HashMap<String, String> findIdFromPhone(HashMap<String, String> memberVo) {
        return memberDao.findIdFromPhone(memberVo);
    }

    public HashMap<String, String> checkJoin(HashMap<String, String> memberVo) {
        return memberDao.checkJoin(memberVo);
    }

    public HashMap<String, String> findPwChkId(HashMap<String, String> data) {
        return memberDao.findPwChkId(data);
    }

    public int replacePw(HashMap<String, String> data) {
        return memberDao.replacePw(data);
    }

    public HashMap<String, String> findIdFromEmail(HashMap<String, String> data) {
        return memberDao.findIdFromEmail(data);
    }

    public HashMap<String, String> updatePw(HashMap<String, String> data) {
        return memberDao.updatePw(data);
    }

    public HashMap<String, String> findPwCheck(HashMap<String, String> data) {
        return memberDao.findPwCheck(data);
    }

    @SuppressWarnings({ "rawtypes" })
    public int socialMemberCheck(String socialId) {

        return memberDao.socialMemberCheck(socialId);
    }

    @SuppressWarnings({ "rawtypes" })
    public String checkTbMember(HttpServletRequest request) {

        Map paramMap = new HashMap();

        paramMap.put("name", request.getParameter("name"));
        paramMap.put("cell1", request.getParameter("cell1"));
        paramMap.put("cell2", request.getParameter("cell2"));
        paramMap.put("cell3", request.getParameter("cell3"));

        return memberDao.checkTbMember(paramMap);
    }

    @SuppressWarnings({ "rawtypes" })
    public int insertTbSocial(HttpServletRequest request, String userIdx) {

        Map paramMap = new HashMap();

        paramMap.put("userId", request.getParameter("userId"));
        paramMap.put("email", request.getParameter("email"));
        paramMap.put("token", request.getParameter("token"));
        paramMap.put("type", request.getParameter("type"));
        paramMap.put("userIdx", userIdx);

        return memberDao.insertTbSocial(paramMap);

    }

    @SuppressWarnings({ "rawtypes" })
    public String insertTbMember(HttpServletRequest request, String userIdx) {

        MemberVO memberVO = new MemberVO();
        Sha256 sha256 = new Sha256();

        memberVO.setName(request.getParameter("name"));
        memberVO.setCell1(request.getParameter("cell1"));
        memberVO.setCell2(request.getParameter("cell2"));
        memberVO.setCell3(request.getParameter("cell3"));
        memberVO.setEmail(request.getParameter("email"));
        memberVO.setSex("1");
        memberVO.setLevelCode("1");
        memberVO.setRealNameCertType("0");
        memberVO.setBirthOp("1");
        memberVO.setEmailState("0");
        memberVO.setSmsState("0");
        memberVO.setMarried("0");
        memberVO.setVisitCnt("0");
        memberVO.setApproval("1");
        memberVO.setPurchasePrice("0");
        memberVO.setPoint("0");
        memberVO.setDeposit("0");
        memberVO.setIp(getClientIP(request));
        memberVO.setPwd(sha256.enc(request.getParameter("cell1")+request.getParameter("cell2")+request.getParameter("cell3")));

        memberDao.insertTbMember(memberVO);

        return memberVO.getUserIdx();
    }

    @SuppressWarnings({ "rawtypes" })
    public void updateTbSocial(String userId, String email, String token, String type) {

        Map param = new HashMap();

        param.put("userId", userId);
        param.put("email", email);
        param.put("token", token);
        param.put("type", type);

        memberDao.updateTbSocial(param);

    }

    @Transactional
    public void deleteMemberInfo(Member member) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userIdx", member.getUserIdx());

        System.out.println("params>>>" + params.toString());
        HashMap<String, Object> userInfo = memberDao.selectUserInfo(params);
        System.out.println(userInfo.toString());

        // 회원 탈퇴 API 호출
        Map<String, Object> memberParams = new HashMap<String, Object>();
        memberParams.put("userIdx", userInfo.get("userIdx"));
        memberParams.put("userName", userInfo.get("name"));
        memberParams.put("phone", userInfo.get("mobile1").toString() + userInfo.get("mobile2").toString() + userInfo.get("mobile3").toString());
        memberParams.put("bfYn", "Y");

        try{
            withdrawal(memberParams);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 회원탈퇴 API 호출
     * @param params (userIdx, userName, phone, bfYn=Y)
     * @return
     * @throws BFException
     */
    public BFResponse withdrawal(Map<String, Object> params) throws BFException {

        BFResponse res;

        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            JSONObject resultJson = null;
            log.info("[MEMBER][SERVICE][MemberService][withdrawal][START]");
            log.info("[MEMBER][SERVICE][MemberService][withdrawal][params][mobile] : {}", params.toString());

            String paramStr = "";
            paramStr += "userIdx="+params.get("userIdx").toString();
            paramStr += "&userName="+params.get("userName").toString();
            paramStr += "&phone="+params.get("phone").toString();
            paramStr += "&bfYn="+params.get("bfYn").toString();

            String serviceCode = "ERP";
            String callUrl = withdrawUrl + paramStr;
            String secretKey = withdrawKey;

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
            httpHeaders.add("serviceCode", serviceCode);
            httpHeaders.add("secretKey", secretKey);
            resultJson = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, "");

            log.debug("=========================================================");
            log.debug("[MEMBER][SERVICE][MemberService][withdrawal][result][mobile] : {}", resultJson.toString());
            log.debug("=========================================================");

            if ("success".equals(resultJson.get("status"))) {
                res = new BFResponse(ResultCodes.RET_SUCCESS, resultMap);
            } else {
                res = new BFResponse(ResultCodes.ERR_NOT_DEFINED, resultMap);
            }
        } catch (Exception e) {
            log.error("[MEMBER][SERVICE][MemberService][withdrawal][ERROR] : {}", e.getStackTrace());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
            e.printStackTrace();
        }
        log.info("[MEMBER][SERVICE][MemberService][withdrawal][END]");

        return res;
    }


}
