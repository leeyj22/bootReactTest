package com.bf.web.customer.service;

import com.bf.web.customer.dao.CustomerDao;
import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerService{

    @Autowired
    private CustomerDao customerDao;

    private final int LIST_SIZE = 15;
    private final static String aes_key = "bfservicekey!@12";
    private static final String baseUrl = "https://erp.bodyfriend.co.kr";
    private static final String secretKey = "34587180942444ee9e21180e6a12e941";

    public List<NoticeVO> selectNoticeList() {
        return customerDao.selectNoticeList();
    }

    public List<NoticeVO> selectNoticeNormalList(NoticeVO noticeVO) {
        return customerDao.selectNoticeNormalList(noticeVO);
    }

    public List<FaqVO> selectFaqList(FaqVO paramVO) {
        return customerDao.selectFaqList(paramVO);
    }

}
