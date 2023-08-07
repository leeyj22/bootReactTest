package com.bf.web.terms.service;

import com.bf.common.util.UtilManager;
import com.bf.web.terms.dao.TermsDao;
import com.bf.web.terms.vo.ServiceTermsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TermsService {

    @Autowired
    TermsDao termsDao;

    /**
     * 약관 내용 조회
     * @param termsIdx
     * @return
     */
    public String getContent(int termsIdx) {
        String content = "";
        ServiceTermsVO vo = termsDao.getServiceTerms(termsIdx);
        if (!UtilManager.isEmptyOrNull(vo)) {
            content = vo.getContent();
        }

        return content;
    }

}
