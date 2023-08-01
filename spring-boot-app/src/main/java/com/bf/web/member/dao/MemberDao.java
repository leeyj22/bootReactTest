package com.bf.web.member.dao;

import com.bf.common.dao.GenericMyBatisDao;
import com.bf.web.member.mapper.MemberMapper;
import com.bf.web.member.vo.Member;
import com.bf.web.member.vo.MemberVO;
import com.bf.web.member.vo.Orders;
import com.bf.web.member.vo.SiteInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    @Autowired
    @Resource(name = "bfSqlSessionTemplate")
    private SqlSessionTemplate bfSqlSessionTemplate;

    @Autowired MemberMapper memberMapper;

    public SiteInfo getSiteInfo() {
        return memberMapper.getSiteInfo();
    }

    public Member loginCheck(Member member) {
        return memberMapper.loginCheck(member);
    }

    public Orders loginCheckNomember(Map<String, Object> params) {
        return memberMapper.loginCheckNomember(params);
    }

    public int updateMemberPoint(Map params) {
        return memberMapper.updateMemberPoint(params);
    }

    public int insertPointLog(Map params) {
        return memberMapper.insertPointLog(params);
    }

    public HashMap<String, String> chkEmail(String email) {
        return memberMapper.chkEmail(email);
    }

    public HashMap<String, String> checkId(String id) {
        return memberMapper.checkId(id);
    }

    public HashMap<String, String> outCheckId(String id) {
        return memberMapper.outCheckId(id);
    }

    public int join(Member member) {
        return memberMapper.join(member);
    }

    public HashMap<String, String> checkCompany(HashMap<String, String> comNo) {
        return memberMapper.checkCompany(comNo);
    }

    public void insertSocialLogin(Map param) {
        memberMapper.insertSocialLogin(param);
    }

    public HashMap<String, String> findIdFromPhone(HashMap<String, String> memberVo) {
        return memberMapper.findIdFromPhone(memberVo);
    }

    public HashMap<String, String> checkJoin(HashMap<String, String> memberVo) {
        return memberMapper.checkJoin(memberVo);
    }

    public HashMap<String, String> findPwChkId(HashMap<String, String> data) {
        return memberMapper.findPwChkId(data);
    }

    public int replacePw(HashMap<String, String> data) {
        return memberMapper.replacePw(data);
    }

    public HashMap<String, String> findIdFromEmail(HashMap<String, String> data) {
        return memberMapper.findIdFromEmail(data);
    }

    
    public HashMap<String, String> updatePw(HashMap<String, String> data) {
        return memberMapper.updatePw(data);
    }

    
    public HashMap<String, String> findPwCheck(HashMap<String, String> data) {
        return memberMapper.findPwCheck(data);
    }

    
    public int socialMemberCheck(String socialId) {
        return memberMapper.socialMemberCheck(socialId);
    }

    
    public String checkTbMember(Map map) {
        return memberMapper.checkTbMember(map);
    }

    
    public int insertTbSocial(Map map) {
        return memberMapper.insertTbSocial(map);
    }

    
    public void updateTbSocial(Map map) {
        memberMapper.updateTbSocial(map);
    }

    
    public Member loginCheck_social(Map map) {
        return memberMapper.loginCheck_social(map);
    }

    
    public int insertTbMember(MemberVO memberVO) {
        return memberMapper.insertTbMember(memberVO);
    }

    
    public int deleteTbSocial(MemberVO memberVO) {
        return memberMapper.deleteTbSocial(memberVO);
    }

    public HashMap<String, Object> selectUserInfo(Map params) {
        return memberMapper.selectUserInfo(params);
    }

}
