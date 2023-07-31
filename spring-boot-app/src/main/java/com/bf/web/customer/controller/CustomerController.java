package com.bf.web.customer.controller;

import com.bf.common.util.Util;
import com.bf.web.customer.service.CustomerService;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class CustomerController {

    @Resource private CustomerService customerService;

    @ResponseBody
    @RequestMapping(value = "/customer/noticeList")
    public List<NoticeVO> getNoticeList (){

        List<NoticeVO> getNoticeList = customerService.selectNoticeList();
        return getNoticeList;
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

}
