package com.bf.web.myinfo.controller;

import com.bf.common.AuthType;
import com.bf.common.BFException;
import com.bf.common.element.Response;
import com.bf.web.myinfo.service.MyinfoService;
import com.bf.web.myinfo.vo.Myinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
public class MyinfoApiController {

    @Autowired
    MyinfoService myinfoService;

    @ResponseBody
    @RequestMapping(value = "/api/myinfo/getAfterServiceList", method = RequestMethod.GET, produces = "application/json")
    public Response getAfterServiceList(HttpSession session, @RequestParam Map<String, Object> params) throws BFException {
        return myinfoService.getAfterServiceList(session, params);
    }

    @ResponseBody
    @RequestMapping(value = "/api/myinfo/getAfterServiceInfo", method = RequestMethod.GET, produces = "application/json")
    public Response getAfterServiceInfo(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> params) throws BFException {
        return myinfoService.getAfterServiceInfo(request, session, params);
    }

    @ResponseBody
    @RequestMapping(value = "/api/myinfo/getMyAllProductList", method = RequestMethod.GET, produces = "application/json")
    public Response getMyAllProductList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws BFException{
        return myinfoService.getMyAllProductList(request, params);
    }

    // 쿠폰
    @ResponseBody
    @RequestMapping(value="/api/myinfo/selectCouponList", method = RequestMethod.GET, produces = "application/json")
    public Response selectCouponList(HttpSession session, Myinfo myinfo) throws BFException {
        return myinfoService.selectCouponList(session, myinfo);
    }

    // 찜한 상품 목록 조회
    @ResponseBody
    @RequestMapping(value="/api/myinfo/favorite", method = RequestMethod.GET, produces = "application/json")
    public Response getFavoriteList(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> params) throws BFException {
//        return productService.getGoodsFavoriteList(request, session, params);
        return null;
    }

    // 납부 정보 조회
    @ResponseBody
    @RequestMapping(value="/api/myinfo/payment/{custCode}", method = RequestMethod.GET, produces = "application/json")
    public Response getPaymentDetailsByLogin(HttpServletRequest request, HttpSession session, @PathVariable(name = "custCode", required = true) String custCode) throws BFException {
        return myinfoService.getPaymentDetails(session, custCode, AuthType.LOGIN);
    }

    @ResponseBody
    @RequestMapping(value="/api/cert/payment/{custCode}", method = RequestMethod.GET, produces = "application/json")
    public Response getPaymentDetailsByCert(HttpServletRequest request, HttpSession session, @PathVariable(name = "custCode", required = true) String custCode) throws BFException {
        return myinfoService.getPaymentDetails(session, custCode, AuthType.CERT);
    }

    @ResponseBody
    @RequestMapping(value="/api/myinfo/updatePhone", method = RequestMethod.POST, produces = "application/json")
    public Response updatePhone(HttpSession session, @RequestBody Map<String, Object> params) throws BFException {
        return myinfoService.updatePhone(session, params);
    }

    // 결제 정보 조회 (미납/잔여요금 납부)
    @ResponseBody
    @RequestMapping(value = "/api/myinfo/getPaymentInfo", method = RequestMethod.GET, produces = "application/json")
    public Response getPaymentInfo(HttpServletRequest request, HttpSession session,  @RequestParam String custCode) throws BFException {
        return myinfoService.getPaymentInfo(session, custCode);
    }

}
