package com.bf.web.terms.dao;

import com.bf.web.terms.mapper.TermsMapper;
import com.bf.web.terms.vo.ServiceTermsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TermsDao {

    @Autowired
    TermsMapper termsMapper;

    // 서비스 약관 조회
    public ServiceTermsVO getServiceTerms(int termsIdx){
        return termsMapper.getServiceTerms(termsIdx);
    }

}
