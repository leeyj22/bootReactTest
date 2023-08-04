package com.bf.web.certify.service;

import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.*;
import com.bf.common.util.UtilManager;
import com.icert.comm.secu.IcertSecuManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class CertifyService {

//	@Autowired
//	RentalService rentalService;
	
//	@Autowired
//	MyinfoService myinfoService;
	
	/**
	 * 모바일 본인인증 호출 파라미터 설정
	 * @param session 
	 * @param params
	 * @return
	 */
	public Response requestCertification(HttpSession session, HttpServletRequest request, Map<String, Object> params) {
		log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][START]");
		
		BFResponse res;
		
		try {

			Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"urlCode", "callbackUrl"});
			if (!(boolean) paramsCheck.get(Consts.CHECK)) {
				throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
			}
			
			Map<String, Object> resMap = new HashMap<String, Object>();
			
			Calendar today 			= Calendar.getInstance();
			SimpleDateFormat sdf 	= new SimpleDateFormat("yyyyMMddHHmmss");
			String day 				= sdf.format(today.getTime());
			
			Random ran 				= new Random();
			int numLength 			= 6;		// 랜덤 문자 길이
			String randomStr 		= "";
			
			for (int i = 0; i < numLength; i += 1) {
				randomStr += ran.nextInt(10);	// 0 ~ 9 랜덤 숫자 생성
			}
			
			String reqNum = day + randomStr;	// reqNum은 최대 40byte 까지 사용 가능
			
			/***************************************************
			 * 본인인증 Parameter
			 **************************************************/
			String urlCode		= params.get("urlCode").toString();
			String plusInfo		= "";
			String cpId			= "BODM1001";        			// 회원사ID
			String certNum		= reqNum;  						// 요청번호
			String date			= day;        					// 요청일시
			String certMet		= "M";     						// 본인인증방법
			String name			= "";        					// 성명
			String phoneNo		= "";							// 휴대폰번호
			String phoneCorp	= "";							// 이동통신사
			String birthDay		= "";							// 생년월일
			String extendVar	= "0000000000000000";			// 확장변수
			String protocol 	= request.isSecure() ? "https://" : "http://";
			String domain		= request.getServerName();
			String port			= String.valueOf(request.getServerPort());
			String tr_url		= protocol + domain + ":" + port + params.get("callbackUrl").toString();
//			String tr_url		= "https://" + domain + params.get("callbackUrl").toString();
			String tr_add		= "Y";
			
			if (!UtilManager.isEmptyOrNull(params.get("plusInfo"))) {
				plusInfo = params.get("plusInfo").toString();
			}
			
			if (!UtilManager.isEmptyOrNull(params.get("name"))) {
				name = params.get("name").toString();
			}
			
			if (!UtilManager.isEmptyOrNull(params.get("phoneNo"))) {
				phoneNo = params.get("phoneNo").toString();
			}
			
			if (!UtilManager.isEmptyOrNull(params.get("phoneCorp"))) {
				phoneCorp = params.get("phoneCorp").toString();
			}
			
			if (!UtilManager.isEmptyOrNull(params.get("birthDay"))) {
				birthDay = params.get("birthDay").toString();
			}
			
			if (!UtilManager.isEmptyOrNull(params.get("trAdd"))) {
				tr_add = params.get("trAdd").toString();
			}
			String tr_cert			= cpId +"/"+ urlCode +"/"+ certNum +"/"+ date +"/"+ certMet +"/"+ birthDay +"//"+ name +"/"+ phoneNo +"/"+ phoneCorp +"//"+ plusInfo +"/"+ extendVar;
			
			IcertSecuManager seed 	= new IcertSecuManager();
			
			String enc_tr_cert		= seed.getEnc(tr_cert, "");											// 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
			String hmacMsg			= seed.getMsg(enc_tr_cert);											// 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
			tr_cert  				= seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");	// 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)		
			
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][urlCode] {}"	, urlCode);
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][plusInfo] {}"	, plusInfo);
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][tr_cert] {}"	, tr_cert);
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][tr_url] {}"	, tr_url);
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][tr_add] {}"	, tr_add);
			
			resMap.put("tr_cert", tr_cert);
			resMap.put("tr_url", tr_url);
			resMap.put("tr_add", tr_add);
			
			res = new BFResponse(ResultCodes.RET_SUCCESS, resMap);
			
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][END]");
		}catch (Exception e){
			log.info("[CERTIFY][SERVICE][CertifyService][requestCertification][ERROR] {}", e.getMessage());
			res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
		}

		return res;
	}

	/**
	 * 본인인증 결과값 수신
	 * @param request
	 * @return
	 */
	public Map<String, Object> responseCertification(HttpServletRequest request) {
		
		log.info("[CERTIFY][SERVICE][CertifyService][responseCertification][START]");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String msg = "";
		String rec_cert     = "";  			// 결과값(암호화)
		String certNum		= "";			// 요청번호
	    String date			= "";			// 요청일시
		String CI	    	= "";			// 연계정보(CI)
		String DI	    	= "";			// 중복가입확인정보(DI)
	    String phoneNo		= "";			// 휴대폰번호
		String phoneCorp	= "";			// 이동통신사
		String birthDay		= "";			// 생년월일
		String birthDay_Y	= "";			// 생년월일(년)
		String birthDay_M	= "";			// 생년월일(월)
		String birthDay_D	= "";			// 생년월일(일)
		String gender		= "";			// 성별
		String nation		= "";			// 내국인
		String name			= "";			// 성명
		String M_name		= "";			// 미성년자 성명
		String M_birthDay	= "";			// 미성년자 생년월일
		String M_Gender		= "";			// 미성년자 성별
		String M_nation		= "";			// 미성년자 내외국인
	    String result		= "";			// 결과값

	    String certMet		= "";			// 인증방법
	    String ip			= "";			// ip주소
		String plusInfo		= "";
		
		String encPara		= "";
		String encMsg1		= ""; 
		String encMsg2		= "";
		String msgChk       = "";
		
		IcertSecuManager seed  = new IcertSecuManager();
		
	    rec_cert = request.getParameter("rec_cert").trim();
		certNum  = request.getParameter("certNum").trim();
		
		if (rec_cert.length() == 0 || certNum.length() == 0) {
			msg = "결과값 비정상";
		}
		
		rec_cert  = seed.getDec(rec_cert, certNum);
		
		int inf1 = rec_cert.indexOf("/",0);
        int inf2 = rec_cert.indexOf("/",inf1+1);

		encPara  = rec_cert.substring(0,inf1);         //암호화된 통합 파라미터
        encMsg1  = rec_cert.substring(inf1+1,inf2);    //암호화된 통합 파라미터의 Hash값

		//04. 위변조 검증
		encMsg2  = seed.getMsg(encPara);

        if(encMsg2.equals(encMsg1)) {
            msgChk="Y";
        }
        
        if(msgChk.equals("N")) {
        	msg = "비정상적인 접근입니다.";
        }
        
        rec_cert  = seed.getDec(encPara, certNum);
        
        int info1  = rec_cert.indexOf("/", 0);
        int info2  = rec_cert.indexOf("/", info1 + 1);
        int info3  = rec_cert.indexOf("/", info2 + 1);
        int info4  = rec_cert.indexOf("/", info3 + 1);
        int info5  = rec_cert.indexOf("/", info4 + 1);
        int info6  = rec_cert.indexOf("/", info5 + 1);
        int info7  = rec_cert.indexOf("/", info6 + 1);
        int info8  = rec_cert.indexOf("/", info7 + 1);
		int info9  = rec_cert.indexOf("/", info8 + 1);
		int info10 = rec_cert.indexOf("/", info9 + 1);
		int info11 = rec_cert.indexOf("/", info10 + 1);
		int info12 = rec_cert.indexOf("/", info11 + 1);
		int info13 = rec_cert.indexOf("/", info12 + 1);
		int info14 = rec_cert.indexOf("/", info13 + 1);
		int info15 = rec_cert.indexOf("/", info14 + 1);
		int info16 = rec_cert.indexOf("/", info15 + 1);
		int info17 = rec_cert.indexOf("/", info16 + 1);
		int info18 = rec_cert.indexOf("/", info17 + 1);

        certNum		= rec_cert.substring(0, info1);
        date		= rec_cert.substring(info1 + 1, info2);
        CI			= rec_cert.substring(info2 + 1, info3);
        phoneNo		= rec_cert.substring(info3 + 1, info4);
        phoneCorp	= rec_cert.substring(info4 + 1, info5);
        birthDay	= rec_cert.substring(info5 + 1, info6);
        birthDay_Y	= rec_cert.substring(info5 + 1, info5 + 5);
        birthDay_M	= rec_cert.substring(info5 + 5, info5 + 7);
        birthDay_D	= rec_cert.substring(info5 + 7, info6);
        gender		= rec_cert.substring(info6 + 1, info7);
        nation		= rec_cert.substring(info7 + 1, info8);
		name		= rec_cert.substring(info8 + 1, info9);
		result		= rec_cert.substring(info9 + 1, info10);
		certMet		= rec_cert.substring(info10 + 1, info11);
		ip			= rec_cert.substring(info11 + 1, info12);
		M_name		= rec_cert.substring(info12 + 1, info13);
		M_birthDay	= rec_cert.substring(info13 + 1, info14);
		M_Gender	= rec_cert.substring(info14 + 1, info15);
		M_nation	= rec_cert.substring(info15 + 1, info16);
		plusInfo	= rec_cert.substring(info16 + 1, info17);
		DI      	= rec_cert.substring(info17 + 1, info18);
		
		CI  = seed.getDec(CI, certNum);
        DI  = seed.getDec(DI, certNum);
        
        String regex = "";

        if (certNum.length() == 0 || certNum.length() > 40) {
        	msg = "요청번호 비정상";
		}

		regex = "[0-9]*";
		if (date.length() != 14 || !UtilManager.paramChk(regex, date)) {
			msg = "요청일시 비정상";
		}

		regex = "[A-Z]*";
		if (certMet.length() != 1 || !UtilManager.paramChk(regex, certMet)) {
			msg = "본인인증방법 비정상"+ certMet;
		}

		regex = "[0-9]*";
		if ((phoneNo.length() != 10 && phoneNo.length() != 11) || !UtilManager.paramChk(regex, phoneNo)) {
			msg = "휴대폰번호 비정상";
		}
		
		regex = "[A-Z]*";
		if (phoneCorp.length() != 3 || !UtilManager.paramChk(regex, phoneCorp)) {
			msg = "이동통신사 비정상";
		}

		regex = "[0-9]*";
		if (birthDay.length() != 8 || !UtilManager.paramChk(regex, birthDay)) {
			msg = "생년월일 비정상";
		}

		regex = "[0-9]*";
		if (gender.length() != 1 || !UtilManager.paramChk(regex, gender)) {
			msg = "성별 비정상";
		}

		regex = "[0-9]*";
		if (nation.length() != 1 || !UtilManager.paramChk(regex, nation)) {
			msg = "내/외국인 비정상";
		}
		
		regex = "[\\sA-Za-z가-�R.,-]*";
		if (name.length() > 60 || !UtilManager.paramChk(regex, name)) {
			msg = "성명 비정상";
		}
		
		regex = "[A-Z]*";
		if (result.length() != 1 || !UtilManager.paramChk(regex, result)) {
			msg = "결과값 비정상";
		}
		
		regex = "[\\sA-Za-z가-�R.,-]*";
		if (M_name.length() != 0) {
			if (M_name.length() > 60 || !UtilManager.paramChk(regex, M_name)) {
				msg = "미성년자 성명 비정상";
			}
		}
		
		regex = "[0-9]*";
		if (M_birthDay.length() != 0) {
			if (M_birthDay.length() != 8 || !UtilManager.paramChk(regex, M_birthDay)) {
				msg = "미성년자 생년월일 비정상";
			}
		}

		regex = "[0-9]*";
		if (M_Gender.length() != 0) {
			if (M_Gender.length() != 1 || !UtilManager.paramChk(regex, M_Gender)) {
				msg = "미성년자 성별 비정상";
			}
		}

		regex = "[0-9]*";
		if (M_nation.length() != 0) {
			if (M_nation.length() != 1 || !UtilManager.paramChk(regex, M_nation)) {
				msg = "미성년자 내/외국인 비정상";
			}
		}
        
		resultMap.put("certNum",	certNum);
		resultMap.put("date",		date);
		resultMap.put("CI",			CI);
		resultMap.put("phoneNo",	phoneNo);
		resultMap.put("phoneCorp",	phoneCorp);
		resultMap.put("birthDay",	birthDay);
		resultMap.put("birthDay_Y",	birthDay_Y);
		resultMap.put("birthDay_M",	birthDay_M);
		resultMap.put("birthDay_D",	birthDay_D);
		resultMap.put("gender",		gender);
		resultMap.put("nation",		nation);
		resultMap.put("name",		name);
		resultMap.put("result",		result);
		resultMap.put("certMet",	certMet);
		resultMap.put("ip",			ip);
		resultMap.put("M_name",		M_name);
		resultMap.put("M_birthDay",	M_birthDay);
		resultMap.put("M_Gender",	M_Gender);
		resultMap.put("M_nation",	M_nation);
		resultMap.put("plusInfo",	plusInfo);
		resultMap.put("DI",			DI);
		
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
		String strCurrentTime = formatter.format(new Date());
		
		Date toDate;
		try {
			toDate = formatter.parse(strCurrentTime);
			Date fromDate = formatter.parse(date);
			long timediff = toDate.getTime() - fromDate.getTime();
			if (timediff < (-30 * 60 * 1000) || (30 * 60 * 100) < timediff) {		
				msg = "비정상적인 접근입니다. (요청시간경과)";
			}
			
			log.info("[CERTIFY][SERVICE][CertifyService][responseCertification][responseCertificationSuccess]");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		
		// 페이지별 추가 정보 처리
		String pagePlusInfo = this._setPageInfo(session, plusInfo, resultMap);
		if (!UtilManager.isEmptyOrNull(pagePlusInfo)) {
		    resultMap.put("plusInfo", pagePlusInfo);
		}
		
		session.setAttribute("phone"	, phoneNo);
		session.setAttribute("custName"	, name);
		session.setAttribute("birthDay"	, birthDay);
		
		if (result.equals("Y")) {
			session.setMaxInactiveInterval(60*60);	//초 단위, 1시간
			session.setAttribute(Constants.SESSION_CERT, "Y");
			session.setAttribute(Constants.SESSION_CERT_NAME, name);
			session.setAttribute(Constants.SESSION_CERT_PHONE, phoneNo);
		}
		
		log.info("[CERTIFY][SERVICE][CertifyService][responseCertification][END]");
		
		return resultMap;
	}
	
	/**
	 * 본인인증시 전달된 변경 파라미터 세팅
	 * @param plusInfo
	 * @return
	 */
	public Map<String, Object> plusInfoSetting(String plusInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		try {
			Properties props = new Properties();
			props.load(new StringReader(plusInfo.substring(1, plusInfo.length() - 1).replace(", ", "\n")));
			
			for (Map.Entry<Object, Object> e : props.entrySet()) {
				params.put((String)e.getKey(), (String)e.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return params;
	}
	
	/**
	 * 페이지에 따른 추가 정보 세팅
	 * @param session
	 * @param plusInfo
	 * @param resultMap
	 * @return
	 */
	public String _setPageInfo(HttpSession session, String plusInfo, Map<String, Object> resultMap) {
	    
	    try {
	        
    	    Map<String, Object> plusInfoMap = this.plusInfoSetting(plusInfo);
    	    if (plusInfoMap.containsKey("pageType")) {
    	        String pageType = plusInfoMap.get("pageType").toString();
    	        String gender = resultMap.get("gender").toString();
                String name = resultMap.get("name").toString();
                String phoneNo = resultMap.get("phoneNo").toString();
                String birthDay = resultMap.get("birthDay").toString();
                
    	        switch (pageType) {
    	            // 전자계약
                    case "RENTAL":                    
//                        Map<String, Object> params = new HashMap<String, Object>();
//                        int userKeyResult = rentalService.insertUserKey(params);
//                        if (userKeyResult > 0) {
//                            plusInfoMap.put("userKey", params.get("userKey"));
//                            plusInfoMap.put("DI", resultMap.get("DI"));
//                            log.info("[CERTIFY][SERVICE][CertifyService][_setPageInfo][RENTAL][userKey] ::: " + params.get("userKey").toString());
//
//                            return plusInfoMap.toString();
//                        }
                        break;
                        
                    // 명의 변경
                    case "CHANGE_NAME":           
                        gender = gender.equals("0") ? "1" : "2"; // ERP 설정으로 수정
                        resultMap.put("gender", gender);
                        
                        session.setAttribute("name",        name);
                        session.setAttribute("phoneNo",     phoneNo);
                        session.setAttribute("gender",      gender);
                        session.setAttribute("birthDay",    birthDay);
                        session.setAttribute("plusInfo",    plusInfo);
                        session.setAttribute("userDI",      resultMap.get("DI").toString());
                        session.setAttribute("userCI",      resultMap.get("CI").toString());
                        
                        plusInfoMap.put("name",       name);
                        plusInfoMap.put("phoneNo",    phoneNo);
                        plusInfoMap.put("gender",     gender);
                        plusInfoMap.put("birthDay",   birthDay);
                        plusInfoMap.put("transRcptNo", session.getAttribute("transRcptNo"));
                        plusInfoMap.put("transRcptNoEnc", session.getAttribute("transRcptNoEnc"));
                        
                        log.info("[CERTIFY][SERVICE][CertifyService][_setPageInfo][CHANGE_NAME][plusInfoMap] : {}", plusInfoMap.toString());
                        
                        return plusInfoMap.toString();                        
                    // 고객 번호 변경
                    case "CHANGE_PHONE":
//                        Map<String, Object> certUserInfo = new HashMap<String, Object>();
//                        certUserInfo.put("certName", name);
//                        certUserInfo.put("certPhoneNo", phoneNo);
//
//                        Response res = myinfoService.updatePhone(session, certUserInfo);
//
//                        plusInfoMap.put("statusCode", res.getStatus().get("code"));
//                        plusInfoMap.put("statusMessage", res.getStatus().get("message"));
//
//                        return plusInfoMap.toString();
					// 서비스 접수
					case "AFTER_SERVICE":
						session.setAttribute("name",        name);
						session.setAttribute("phoneNo",     phoneNo);
						session.setAttribute("gender",      gender);
						session.setAttribute("birthDay",    birthDay);
						session.setAttribute("plusInfo",    plusInfo);
						session.setAttribute("userDI",      resultMap.get("DI").toString());
						session.setAttribute("userCI",      resultMap.get("CI").toString());
						plusInfoMap.put("userDI", session.getAttribute("userDI"));

						log.info("[CERTIFY][SERVICE][CertifyService][_setPageInfo][AFTER_SERVICE][plusInfoMap] : {}", plusInfoMap.toString());
						return plusInfoMap.toString();
					// 이전/설치 접수
					case "TRANSFER":
						session.setAttribute("name",        name);
						session.setAttribute("phoneNo",     phoneNo);
						session.setAttribute("gender",      gender);
						session.setAttribute("birthDay",    birthDay);
						session.setAttribute("plusInfo",    plusInfo);
						session.setAttribute("userDI",      resultMap.get("DI").toString());
						session.setAttribute("userCI",      resultMap.get("CI").toString());
						plusInfoMap.put("userDI", session.getAttribute("userDI"));

						log.info("[CERTIFY][SERVICE][CertifyService][_setPageInfo][TRANSFER][plusInfoMap] : {}", plusInfoMap.toString());
						return plusInfoMap.toString();
					// 분해/조립 접수
					case "ASSEMBLE":
						session.setAttribute("name",        name);
						session.setAttribute("phoneNo",     phoneNo);
						session.setAttribute("gender",      gender);
						session.setAttribute("birthDay",    birthDay);
						session.setAttribute("plusInfo",    plusInfo);
						session.setAttribute("userDI",      resultMap.get("DI").toString());
						session.setAttribute("userCI",      resultMap.get("CI").toString());
						plusInfoMap.put("userDI", session.getAttribute("userDI"));

						log.info("[CERTIFY][SERVICE][CertifyService][_setPageInfo][ASSEMBLE][plusInfoMap] : {}", plusInfoMap.toString());
						return plusInfoMap.toString();
				}
    	    }
    	    
	    } catch (Exception e) {
	        log.error("[CERTIFY][SERVICE][CertifyService][_setPageInfo][ERROR] : {}", e.getMessage());
        }    	   
    	    
	    return null;
	}
}
