package com.bf.web.customer.mapper;

import com.bf.web.customer.vo.FaqVO;
import com.bf.web.customer.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {

    public List<NoticeVO> selectNoticeList();

    public List<FaqVO> selectFaqList(FaqVO paramVO);

}
