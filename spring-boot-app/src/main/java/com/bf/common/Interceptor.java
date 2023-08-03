package com.bf.common;

import com.bf.common.util.UtilManager;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@Component
public class Interceptor extends HandlerInterceptorAdapter {

//	@Autowired
//	MarketingService marketingService;

	@Value(value="${system.auth.url}")
	public static String systemAuthUrl;
	@Value(value="${system.auth.client}")
	public static String systemAuthClient;
	@Value(value="${system.host.name}")
	public static String systemHostName;
	@Value(value="${system.admin.url}")
	public static String systemAdminUrl;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final HttpSession session = request.getSession(false);
		final String requestURI = request.getRequestURI();
		
		if (requestURI.contains(".css") || 
				requestURI.contains(".js") || 
				requestURI.contains(".html") ||
				requestURI.contains(".otf") ||
				requestURI.contains(".jpg") ||
				requestURI.contains(".png") ||
				requestURI.contains(".gif") ||
				requestURI.contains(".woff2") ||
				requestURI.contains(".ico") ||
				requestURI.contains(".mp4")) {
			return true; // API요청이 아니면 넘긴다.
		}
		
		// 금액 인상 (이전설치/분해조립) 2023-02-13
		if (requestURI.indexOf("customer/assemble-and-disassemble") >= 0		        
		        || requestURI.contains("/api/pay/getPayHashString")
		        || requestURI.indexOf("/svcTrans") >= 0) {
		    if (this.increasePriceCheck()) {
		        request.setAttribute("dPrice", 60000);
	            request.setAttribute("aPrice", 60000);
	            request.setAttribute("increasePriceCheck", true);
		    } else {
		        request.setAttribute("dPrice", 50000);
	            request.setAttribute("aPrice", 50000);
	            request.setAttribute("increasePriceCheck", false);
		    }
		}   
		
		// UNIERP 연동 페이지
		if (requestURI.indexOf("assemble-and-disassemble") >= 0				// 분해조립
				|| requestURI.indexOf("assembleOrderComplete") >= 0			// 분해조립 결제 완료
				|| requestURI.indexOf("svcTrans") >= 0						// 이전설치
				|| requestURI.indexOf("myinfo/member_info") >= 0			// 마이페이지
				|| requestURI.indexOf("customer/afterService") >= 0			// 서비스 접수
				|| requestURI.indexOf("customerHelp/delivery_login") >= 0	// 간편 배송 조회
				|| requestURI.indexOf("/rental/agree") >= 0					// 모바일 전자 계약				
				|| requestURI.indexOf("/company/storeSearch") >= 0			// 전시장 체험
				|| requestURI.indexOf("/event/rovo") >= 0					// 로보이벤트
				|| requestURI.indexOf("/marketing/coupon") >= 0				// 체험 예약 쿠폰
				) {
			if (this.limitTimeCheck()) {
				response.sendRedirect("/html/system/00.system.html");
				return false;
			}
		}

		if (requestURI.indexOf("productOrderComplete") >= 0) {
			Enumeration<String> paramNames = request.getParameterNames();
			JSONObject json = new JSONObject();
			log.debug("======================================          NICEPAY START         ======================================");
			while (paramNames.hasMoreElements()) {
				String key = (String) paramNames.nextElement();
				String value = request.getParameter(key);
				json.put(key, value);
			}

			log.info("[NICEPAY][WEB][PRODUCTORDERCOMPLETE][REQUEST] " + json.toJSONString());

			log.info("======================================           NICEPAY END         ======================================");
		}
		
		boolean returnResult = true;
		
		if (requestURI.contains("/api/myinfo/")
		        || requestURI.contains("/myinfo/delivery_info")) {
			
		} else if (requestURI.indexOf("mypage") > -1 // 마이페이지, 고객정보 등 개인 정보 조회 페이지
				|| requestURI.indexOf("/myinfo/") > -1
				|| requestURI.indexOf("/customerHelp/serviceTrancefer") > -1) {
			
			if (session != null && session.getAttribute(Constants.SESSION_USER_ID) != null) {
				
				if (requestURI.indexOf("/order") > -1) {
					if (!session.getAttribute(Constants.SESSION_TYPE).equals("USER")) {
						response.sendRedirect(request.getContextPath() + "/home/?result=0");
						return false;
					}
				} else if (requestURI.indexOf("/consult") > -1) {
					if (!session.getAttribute(Constants.SESSION_TYPE).equals("USER")) {
						response.sendRedirect(request.getContextPath() + "/home/?result=1");
						return false;
					}
				}
				
				returnResult = true;
			} else {
				String url = URLEncoder.encode(getUrl(request), "UTF-8");
				
				log.info("redirect > login");
				log.info("redirect url > " + url);
				log.info("admin > " + requestURI.indexOf("/admin"));
				log.info("/admin/login.bf > " + requestURI.indexOf("/admin/login.bf"));
				
				if (requestURI.indexOf("/admin") > -1) {
					if (requestURI.indexOf("/admin/login.bf") > -1) {
						return true;
					} else {
						response.sendRedirect(request.getContextPath() + "/admin/login.bf?redirectUrl=" + url);
					}
				} else {
					response.sendRedirect(request.getContextPath() + systemAuthUrl
							+ "/auth/common/login?client_id=" + systemAuthClient
							+ "&redirect_uri=https://" + systemHostName + "/member/login_return");
				}
				
				returnResult = false;
			}
		}
		
		// 나이스페이 인입 - 분해조립 결제 완료 페이지
		if (requestURI.indexOf("/assembleOrderComplete") > -1) {
			request.setCharacterEncoding("euc-kr");
		}
		
		if (requestURI.indexOf("/order/savePayResult") > -1) {
            request.setCharacterEncoding("utf-8");
        }
		
		if (requestURI.indexOf("/myinfo/paymentComplete") > -1) {
            request.setCharacterEncoding("euc-kr");
        }
		
		log.info("======================================          START         ======================================");
		log.info("[INTERCEPTOR][requestURI] : {}", request.getRequestURI());
		
		Enumeration<String> paramNames 	= request.getParameterNames();
		log.info("******************************************************");
		while (paramNames.hasMoreElements()) {
			String key 		= (String) paramNames.nextElement();
			String value 	= request.getParameter(key);
			
			if ("_".equals(key)) continue;
			log.info("[INTERCEPTOR][PARAMETER][{}] : {}", key, value);
		}
		log.info("******************************************************");
		
		log.info("====================================================================================================");
		
		// 마케팅 동의
        if (requestURI.equals("/customer/assemble-and-disassemble_step2")
                || requestURI.equals("/showroom/reserve")              
                || requestURI.equals("/product/productOrder")
                || requestURI.equals("/rental/agree")               
                || requestURI.equals("/svcTrans2")               
                || requestURI.equals("/customer/after-service")) {
//            _checkMarketing(request, session);
        }
		
		return returnResult;
	}

	public String getUrl(HttpServletRequest request) {
		String parameterList = "";
		String ret_url = request.getRequestURI(); // No Parameter url

		int k = 0;

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); k++) {
			String name = (String) e.nextElement();
			String[] value = request.getParameterValues(name);

			if (k == 0)
				ret_url = ret_url + "?";
			else if (k > 0)
				ret_url = ret_url + "&";
			parameterList = parameterList + "&";

			for (int q = 0; q < value.length; q++) {
				if (q > 0) {
					ret_url = ret_url + "&";
					parameterList = parameterList + "&";
				}
				ret_url = ret_url + name + "=" + value[q];
				parameterList = parameterList + name + "=" + value[q];
			}

		}

		String result = ret_url;
		return result;
	}
	
	// 제한 날짜 체크
	public boolean limitTimeCheck () {
		Date now = new Date();
		long ntime = now.getTime();
        
		log.info("======== 현재 날짜 :: {}", ntime + " ==========");
		
		Calendar cal = Calendar.getInstance();
		cal.set(2022, Calendar.NOVEMBER, 10, 20, 0, 0);	// 2022-11-10 20:00:00
		cal.set(Calendar.MILLISECOND, 0);
		Date limitStartDate1= cal.getTime();
		long limitStartTime1 = limitStartDate1.getTime();
		
		cal.set(2022, Calendar.NOVEMBER, 11, 9, 0, 0); 	// 2022-11-11 09:00:00
		cal.set(Calendar.MILLISECOND, 0);
		Date limitEndDate1 = cal.getTime();
		long limitEndTime1 = limitEndDate1.getTime();
		
		cal.set(2022, Calendar.JUNE, 14, 20, 0, 0);	// 2022-06-14 20:00:00
		cal.set(Calendar.MILLISECOND, 0);
		Date limitStartDate2 = cal.getTime();
		long limitStartTime2 = limitStartDate2.getTime();
		
		cal.set(2022, Calendar.JUNE, 15, 5, 0, 0); 	// 2022-06-15 05:00:00
		cal.set(Calendar.MILLISECOND, 0);
		Date limitEndDate2 = cal.getTime();
		long limitEndTime2 = limitEndDate2.getTime();
		
		Timestamp tss1 = new Timestamp(limitStartTime1);
		Timestamp tse1 = new Timestamp(limitEndTime1);
		Timestamp tss2 = new Timestamp(limitStartTime2);
		Timestamp tse2 = new Timestamp(limitEndTime2);
		
		log.info("======== 제한 시작 시간1 :: {}", limitStartTime1 + " ==========");
		log.info("======== 제한 종료 시간1 :: {}", limitEndTime1 + " ==========");
		
		log.info("======== 제한 시작 날짜1 :: {}", tss1 + " ==========");
		log.info("======== 제한 종료 날짜1 :: {}", tse1 + " ==========");
		
		log.info("======== 제한 시작 시간2 :: {}", limitStartTime2 + " ==========");
		log.info("======== 제한 종료 시간2 :: {}", limitEndTime2 + " ==========");
		
		log.info("======== 제한 시작 날짜2 :: {}", tss2 + " ==========");
		log.info("======== 제한 종료 날짜2 :: {}", tse2 + " ==========");
		
		if (ntime >= limitStartTime1 && ntime <= limitEndTime1) {
			log.info("======== restrict !! ==========");
			return true;
		} else if (ntime >= limitStartTime2 && ntime <= limitEndTime2) {
			log.info("======== restrict !! ==========");
			return true;
		} else {
			return false;
		}
	}
	
	// 금액 인상 확인
	private boolean increasePriceCheck() {
	    
	    Date now = new Date();
        long ntime = now.getTime();
        
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.FEBRUARY, 13, 0, 0, 0); // 2023-02-13 00:00:00
        cal.set(Calendar.MILLISECOND, 0);
        Date limitStartDate1= cal.getTime();
        long limitStartTime1 = limitStartDate1.getTime();
        
        Timestamp tss1 = new Timestamp(limitStartTime1);
        
        log.info("======== priceCheck 제한 시작 시간 :: {}", limitStartTime1 + " ==========");
        log.info("======== priceCheck 제한 시작 날짜 :: {}", tss1 + " ==========");
        
        if (ntime >= limitStartTime1) {
            return true;
        } else {
            return false;
        }
	}
	
//	private void _checkMarketing(HttpServletRequest request, HttpSession session) {
//	    request.setAttribute("marketingAgree", marketingService.checkMarketingAgree(session));
//	}
}