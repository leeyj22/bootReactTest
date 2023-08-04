package com.bf.web.message.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface MessageMapper {

    // 알림톡 템플릿 코드 조회
    public HashMap<String, String> selectTemplate(String tmplCd);

}
