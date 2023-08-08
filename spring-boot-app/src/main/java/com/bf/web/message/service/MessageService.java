package com.bf.web.message.service;

import com.bf.common.BFException;
import com.bf.common.Consts;
import com.bf.common.RestAdapter;
import com.bf.common.ResultCodes;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.UtilManager;
import com.bf.unierp.message.dao.UniErpMessageDao;
import com.bf.web.message.dao.MessageDao;
import com.bf.web.message.vo.AlimTalkVo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    UniErpMessageDao uniErpMessageDao;

    @Value(value = "${system.unierp.secretKey}")
    String secretKey;

    /**
     * 알림톡 발송
     * @param request
     * @param params, msg 개행 \n으로 전송, date 2021-04-01 13:50 예약발송 가능
     * @return
     * @throws BFException
     */
    public Response sendAlimTalk(HttpServletRequest request, Map<String, Object> params) throws BFException {
        Response res;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {
                    "tmplCd", "phone", "msg"
            });
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            String tmplCd = params.get("tmplCd").toString();	// 알림톡 템플릿 코드
            String phone = params.get("phone").toString();		// 전화번호
            String msg = params.get("msg").toString();			// 내용
            String tranButton = "";								// 버튼

            if(null!=params.get("tranButton")){
                tranButton = params.get("tranButton").toString();
            }

            AlimTalkVo vo = new AlimTalkVo(tmplCd, phone, msg, tranButton);

            if (params.containsKey("date")) {
                String date = params.get("date").toString();	// 발송일시
                vo.setDate(date);
            }

            log.info("[MESSAGE][SERVICE][MessageService][sendAlimTalk][vo] {}"		, vo.toString());

            int result = uniErpMessageDao.sendAlimTalk(vo);

            if (result > 0) {
                res = new BFResponse(ResultCodes.RET_SUCCESS);
            } else {
                res = new BFResponse(ResultCodes.ERR_DB_INSERT_FAILURE);
            }

        } catch (BFException be) {
            log.error("[MESSAGE][SERVICE][MessageService][sendAlimTalk][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            log.error("[MESSAGE][SERVICE][MessageService][sendAlimTalk][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        return res;
    }


    /**
     * 멤버십 API 알림톡 전송
     * @param jsonObject
     */
    public boolean sendAlimTalk(JSONObject jsonObject) {
        boolean isSuccess = false;
        try {

            log.info("[MESSAGE][SERVICE][MessageService][sendAlimTalk][phone] : {}", jsonObject.get("celNo").toString());

            // tranButton 필수
            if (!jsonObject.has("tranButton")) {
                jsonObject.put("tranButton", false);
            }

            // API 파라미터 (알림톡 전송은 개발이 없음 : erp 개발db에 알림톡관련 테이블이 따로 없어서?)
            String callUrl = "https://erp.bodyfriend.co.kr/api/v1/common/send/alimtalk";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            httpHeaders.setContentType(new MediaType(Consts.APPLICATION, Consts.JSON, Charset.forName(Consts.UTF_8)));
            httpHeaders.add("serviceCode", "ERP");
            httpHeaders.add("secretKey", "34587180942444ee9e21180e6a12e941");
//            httpHeaders.add("secretKey", secretKey);

            JSONObject jsonResult = RestAdapter.callRestApi(httpHeaders, callUrl, HttpMethod.POST, jsonObject);
            JSONObject statusJson = jsonResult.getJSONObject("status");
            String statusCode = statusJson.get("code").toString();

            isSuccess = statusCode.equals("200");

        } catch (Exception e) {
            log.error("[MESSAGE][SERVICE][MessageService][sendAlimTalk][ERROR] : {}", e.getMessage());
        }

        return isSuccess;
    }

    /**
     *
     * @param request
     * @param params
     * @return
     * @throws BFException
     */
    public Response sendSms(HttpServletRequest request, Map<String, Object> params) throws BFException {
        Response res;

        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {
                    "msg", "phone"
            });
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            String phone = params.get("phone").toString();	// 전화번호
            String msg = params.get("msg").toString();		// 내용
            String userId = "";								// 사용자 ID
            String date = "";								// 발송일시

            if (params.containsKey("userId")) {
                userId = params.get("userId").toString();
            }

            Map<String, Object> smsParams = new HashMap<String, Object>();
            smsParams.put("phone"	, phone);
            smsParams.put("msg"		, msg);
            smsParams.put("userId"	, userId);
            smsParams.put("date"	, date);

            log.info("[MESSAGE][SERVICE][MessageService][sendSms][params] {}"		, smsParams);

            int result = uniErpMessageDao.sendSms(smsParams);

            if (result > 0) {
                res = new BFResponse(ResultCodes.RET_SUCCESS);
            } else {
                res = new BFResponse(ResultCodes.ERR_DB_INSERT_FAILURE);
            }

        } catch (BFException be) {
            log.error("[MESSAGE][SERVICE][MessageService][sendSms][ERROR] : {}", be.getMessage());
            throw be;
        } catch (Exception e) {
            log.error("[MESSAGE][SERVICE][MessageService][sendSms][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }


        return res;
    }


    // 템플릿 코드 조회
    public HashMap<String, String> selectTemplate(String tmplCd) {
        return messageDao.selectTemplate(tmplCd);
    }

}
