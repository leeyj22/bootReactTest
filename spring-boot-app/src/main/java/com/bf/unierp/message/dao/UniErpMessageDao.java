package com.bf.unierp.message.dao;

import com.bf.unierp.message.mapper.UniErpMessageMapper;
import com.bf.web.message.vo.AlimTalkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UniErpMessageDao {

    @Autowired
    UniErpMessageMapper uniErpMessageMapper;

    public int sendAlimTalk(AlimTalkVo vo) {
        return uniErpMessageMapper.sendAlimTalk(vo);
    }

    public int sendSms(Map<String, Object> params) {
        return uniErpMessageMapper.sendSms(params);
    }

}
