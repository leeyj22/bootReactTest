package com.bf.web.marketing.controller;

import com.bf.web.marketing.service.MarketingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class MarketingController {

    @Autowired
    MarketingService marketingService;

    @ResponseBody
    @RequestMapping(value = "/marketing/getAgreeInfo")
    public Map<String, Object> getMarketingAgree(HttpSession session){
        Map<String, Object> map = new HashMap<>();
        map.put("marketingAgree", marketingService.checkMarketingAgree(session));
        return map;
    }

}
