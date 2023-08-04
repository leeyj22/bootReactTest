package com.bf.web.message.controller;

import com.bf.common.BFException;
import com.bf.common.element.Response;
import com.bf.web.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("web.MessageApiController")
@Slf4j
public class MessageApiController {

    @Autowired
    MessageService messageService;

    /**
     * 알림톡 발송
     * @param request
     * @param params, msg 개행 \n으로 전송
     * @return
     * @throws BFException
     */
    @ResponseBody
    @PostMapping(value="/api/message/sendAlimTalk", produces="application/json")
    public Response sendAlimTalk(
            HttpServletRequest request, @RequestBody Map<String, Object> params) throws BFException {

        log.info("[MESSAGE][CONTROLLER][MessageApiController][sendAlimTalk][START]");

        Response response = messageService.sendAlimTalk(request, params);

        log.info("[MESSAGE][CONTROLLER][MessageApiController][sendAlimTalk][END]");
        return response;
    }

    /**
     * SMS 발송
     * @param request
     * @param params, msg 개행 \n으로 전송
     * @return
     * @throws BFException
     */
    @ResponseBody
    @RequestMapping(value="/api/message/sendSms", method=RequestMethod.POST, produces="application/json")
    public Response sendSms(HttpServletRequest request,
                            @RequestBody Map<String, Object> params) throws BFException {

        log.info("[MESSAGE][CONTROLLER][MessageApiController][sendSms][START]");
        Response response = messageService.sendSms(request, params);
        log.info("[MESSAGE][CONTROLLER][MessageApiController][sendSms][END]");
        return response;
    }

    // 템플릿 코드 조회
    @ResponseBody
    @RequestMapping(value="/api/message/alimtalkTemplate", method=RequestMethod.GET, produces="application/json")
    public HashMap<String, String> selectTemplate(String tmplCd) {
        return messageService.selectTemplate(tmplCd);
    }

}
