package com.bf.web.message.dao;

import com.bf.web.message.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class MessageDao {

    @Autowired MessageMapper messageMapper;

    public HashMap<String, String> selectTemplate(String tmplCd){
        return messageMapper.selectTemplate(tmplCd);
    }

}
