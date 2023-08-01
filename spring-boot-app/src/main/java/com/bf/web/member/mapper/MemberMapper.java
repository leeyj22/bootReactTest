package com.bf.web.member.mapper;

import com.bf.web.member.vo.Member;
import com.bf.web.member.vo.MemberVO;
import com.bf.web.member.vo.Orders;
import com.bf.web.member.vo.SiteInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface MemberMapper {

    public SiteInfo getSiteInfo();

    public Member loginCheck(Member member);

    public Orders loginCheckNomember(Map<String, Object> params);

    public int updateMemberPoint(Map params);

    public int insertPointLog(Map params);

    public HashMap<String, String> chkEmail(String email);

    public HashMap<String, String> checkId(String id);

    public HashMap<String, String> outCheckId(String id);

    public int join(Member member);

    public HashMap<String, String> checkCompany(HashMap<String, String> comNo);

    public void insertSocialLogin(Map param);

    public HashMap<String, String> findIdFromPhone(HashMap<String, String> memberVo);

    public HashMap<String, String> checkJoin(HashMap<String, String> memberVo);

    public HashMap<String, String> findPwChkId(HashMap<String, String> data);

    public int replacePw(HashMap<String, String> data);

    public HashMap<String, String> findIdFromEmail(HashMap<String, String> data);

    public HashMap<String, String> updatePw(HashMap<String, String> data);

    public HashMap<String, String> findPwCheck(HashMap<String, String> data);

    public int socialMemberCheck(String socialId);

    public String checkTbMember(Map map);

    public int insertTbSocial(Map map);

    public void updateTbSocial(Map map);

    public Member loginCheck_social(Map map);

    public int insertTbMember(MemberVO memberVO);

    public int deleteTbSocial(MemberVO memberVO);

    public HashMap<String, Object> selectUserInfo(Map params);

}
