package com.bf.web.customer.service;

import com.bf.web.customer.mapper.CustomerMapper;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements CustomerMapper{

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public List<NoticeVO> selectNoticeList() {
        return customerMapper.selectNoticeList();
    }

    @Override
    public List<FaqVO> selectFaqList(FaqVO paramVO) {
        return customerMapper.selectFaqList(paramVO);
    }

}
