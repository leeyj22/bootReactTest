package com.bf.unierp.message.mapper;

import com.bf.web.message.vo.AlimTalkVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UniErpMessageMapper {

    //알림톡 발송
    public int sendAlimTalk(AlimTalkVo vo);

    //SMS 발송
    public int sendSms(Map<String, Object> params);

}
