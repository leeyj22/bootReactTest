package com.bf.web.certify.controller;

import com.bf.common.BFException;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.UtilManager;
import com.bf.web.certify.service.CertifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class CertifyController {
	@Autowired
	CertifyService certifyService;

	@Value(value = "${system.kmcert.urlcode.common}")
	String kmcertUrlcode;
	
	/**
	 * 본인인증 정보 생성
	 * @param session
	 * @param request
	 * @param params
	 * @return
	 * @throws BFException
	 */
	@ResponseBody
	@RequestMapping(value = "/certify/requestCertification", method = RequestMethod.POST, produces="application/json")
	public Response requestCertification(HttpSession session, HttpServletRequest request, @RequestBody Map<String, Object> params) throws BFException {
		
		log.info("[CONTROLLER][CerfifyController][requestCertification][START]");

		Response response = certifyService.requestCertification(session, request, params);

		log.info("[CONTROLLER][CerfifyController][requestCertification][END]");
		
		return response;
	}
	
	/**
	 * 본인 인증 화면 이동
	 * @param 
	 * @return
	 * @throws 
	 */
//	@ResponseBody
//	@RequestMapping(value="/certify/certifyCommon", method=RequestMethod.POST)
//	public ModelAndView certifyCommon(HttpServletRequest request) throws BFException {
//		log.info("[CONTROLLER][CerfifyController][certifyCommon][START]");
//
//		ModelAndView mav = new ModelAndView();
//
//		mav.addObject("params", request.getParameter("params"));
//
//		mav.setViewName("/web/certify/certify-common");
//
//		log.info("[CONTROLLER][CerfifyController][certifyCommon][END]");
//		return mav;
//	}
	
	/**
	 * 본인 인증 결과
	 * @param 
	 * @return
	 * @throws 
	 */
	@RequestMapping(value="/certify/certifyResult")
	public String certifyResult(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttr) throws BFException {
		log.info("[CONTROLLER][CerfifyController][certifyResult][START]");

		Response response = new BFResponse();


		Map<String, Object> resultMap = certifyService.responseCertification(request);
		
		// 추가 정보 세팅
		String plusInfo = resultMap.get("plusInfo").toString();
		Map<String, Object> plusMap = certifyService.plusInfoSetting(plusInfo);

		plusMap.put("gender"	, resultMap.get("gender"));
		plusMap.put("name"		, resultMap.get("name"));
		plusMap.put("phoneNo"	, resultMap.get("phoneNo"));
		plusMap.put("birthDay"	, resultMap.get("birthDay"));

		response.setData(plusMap);
		redirectAttr.addAttribute("data", plusMap.toString());
		log.info("[CONTROLLER][CerfifyController][certifyResult][END]");

		return "redirect:http://localhost:3000/certify/certify_result";
	}
	
	/**
	 * 본인인증
	 * @param pageType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/certify/{pageType}")
	public Response certify(@PathVariable String pageType) {
		log.info("[CONTROLLER][CerfifyController][certify][START]");
		
		String urlCode = kmcertUrlcode;

		Response response = new BFResponse();
		Map<String, Object> map = new HashMap<>();
		map.put("pageType", pageType);
		map.put("urlCode", urlCode);
		response.setData(map);
	    
	    log.info("[CONTROLLER][CerfifyController][certify][END]");
	    return response;
	}
}
