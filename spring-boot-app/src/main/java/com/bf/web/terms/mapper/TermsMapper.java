package com.bf.web.terms.mapper;

import com.bf.web.terms.vo.ServiceTermsVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TermsMapper {

    // 서비스 약관 조회
    public ServiceTermsVO getServiceTerms(int termsIdx);

}
