package com.bf.web.marketing.service;

import com.bf.common.BFException;
import com.bf.common.Constants;
import com.bf.common.Consts;
import com.bf.common.ResultCodes;
import com.bf.common.element.BFResponse;
import com.bf.common.element.Response;
import com.bf.common.util.UtilManager;
import com.bf.web.marketing.dao.MarketingDao;
import com.bf.web.marketing.vo.MarketingAgreeVO;
import com.bf.web.myinfo.dao.MyinfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
@Slf4j
public class MarketingService {

    @Autowired
    MarketingDao marketingDao;
    @Autowired
    MyinfoDao myinfoDao;

    /**
     * 마케팅 동의 확인
     * @param request
     * @param params
     * @return
     * @throws BFException
     */
    public Response insertMarketingAgree(HttpServletRequest request, Map<String, Object> params)  throws BFException {

        log.info("[MARKETING][SERVICE][MarketingService][insertMarketingAgree][START]");

        BFResponse res;
        try {
            // 필수 파라미터 체크
            Map paramsCheck = UtilManager.checkMandantoryWithReturn(params, new String[] {"name", "phone", "agreeStatus", "refPageIdx"});
            if (!(boolean) paramsCheck.get(Consts.CHECK)) {
                throw new BFException(ResultCodes.ERR_PARAM_NOT_FOUND);
            }

            MarketingAgreeVO vo = new MarketingAgreeVO();
            vo.setName(params.get("name").toString());
            vo.setPhone(params.get("phone").toString());
            vo.setAgreeStatus(Integer.parseInt(params.get("agreeStatus").toString()));
            vo.setRefPageIdx(Integer.parseInt(params.get("refPageIdx").toString()));

            int result = 0;
            if (!UtilManager.isEmptyOrNull(params.get("updateMember")) && params.get("updateMember").toString().equals("Y")) {
                result = this.insertMarketingAgree(request.getSession(), vo);
            } else {
                result = marketingDao.insertMarketingAgree(vo);
            }

            if (result > 0) {
                res = new BFResponse(ResultCodes.RET_SUCCESS);
            } else {
                res = new BFResponse(ResultCodes.ERR_DB_INSERT_FAILURE);
            }
        } catch (BFException e) {
            log.error("[MARKETING][SERVICE][MarketingService][insertMarketingAgree][ERROR] : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[MARKETING][SERVICE][MarketingService][insertMarketingAgree][ERROR] : {}", e.getMessage());
            res = new BFResponse(ResultCodes.ERR_NOT_DEFINED);
        }

        log.info("[MARKETING][SERVICE][MarketingService][insertMarketingAgree][END]");
        return res;
    }

    public int insertMarketingAgree(HttpSession session, MarketingAgreeVO vo) {

        Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
        if ((boolean) paramsCheck.get(Consts.CHECK)) {
            vo.setUserIdx(session.getAttribute(Constants.SESSION_USER_IDX).toString());
            vo.setName(session.getAttribute(Constants.SESSION_NAME).toString());
            vo.setPhone(session.getAttribute(Constants.SESSION_PHONE).toString());
        }
        // 로그인의 경우 로그인 회원 정보 업데이트, 미로그인의 경우 이름+전화번호 조회하여 상태 업데이트
        myinfoDao.updateMemberMarketingAgreeAll(vo);

        return marketingDao.insertMarketingAgree(vo);
    }

    public int checkMarketingAgree(HttpSession session) {
        Map paramsCheck = UtilManager.checkMandantoryWithReturn(session, new String[] {Constants.SESSION_USER_IDX});
        if (!(boolean) paramsCheck.get(Consts.CHECK)) {
            return 0;
        }

        String userIdx = (String) session.getAttribute(Constants.SESSION_USER_IDX);
        return marketingDao.checkMarketingAgree(userIdx);
    }

}
