/*
package com.bf.web.member.dao;

import com.bf.common.dao.GenericMyBatisDao;
import com.bf.web.member.bean.Member;
import com.bf.web.member.bean.MemberVO;
import com.bf.web.member.bean.Orders;
import com.bf.web.member.bean.SiteInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao extends GenericMyBatisDao<Member> {

    @Resource(name="sqlSessionTemplateMysql")
    SqlSessionTemplate sqlSession;

    public SiteInfo getSiteInfo() {
        return sqlSession.selectOne("MemberDao.getSiteInfo");
    }

    public Orders loginCheckNomember(Map<String, Object> params) {
        return sqlSession.selectOne("MemberDao.loginCheckNomember", params);
    }

    public int updateMemberPoint(Map params) {
        return sqlSession.update("MemberDao.updateMemberPoint", params);
    }

    public int insertPointLog(Map params) {
        return sqlSession.insert("MemberDao.insertPointLog", params);
    }

    public int insertBasket(Map params) {
        return sqlSession.insert("MemberDao.insertBasket", params);
    }

    public int insertBasketOption(Map params) {
        return sqlSession.insert("MemberDao.insertBasketOption", params);
    }

    public HashMap<String, String> checkEmail(String email) {
        return sqlSession.selectOne("MemberDao.chkEmail", email);
    }

    public HashMap<String, String> checkId(String id) {
        return sqlSession.selectOne("MemberDao.checkId", id);
    }

    public HashMap<String, String> outCheckId(String id) {
        return sqlSession.selectOne("MemberDao.outCheckId", id);
    }

    @SuppressWarnings("rawtypes")
    public int join(Member member){
        return sqlSession.insert("MemberDao.join", member);
    }

    public HashMap<String, String> checkCompany(HashMap<String, String> comNo) {
        return sqlSession.selectOne("MemberDao.checkCompany", comNo);
    }

    public void insertSocialLogin(Map param) {
        sqlSession.insert("MemberDao.insertSocialLogin", param);

    }

    public HashMap<String, String> findIdFromPhone(HashMap<String, String> memberVo) {
        return sqlSession.selectOne("MemberDao.findIdFromPhone", memberVo);

    }

    //핸드폰인증 - 아이디찾기
    public List<HashMap<String, String>> findIdPhone(HashMap<String, String> memberVo) {
        return sqlSession.selectList("MemberDao.findIdFromPhone", memberVo);
    }

    public HashMap<String, String> checkJoin(HashMap<String, String> memberVo) {
        return sqlSession.selectOne("MemberDao.checkJoin", memberVo);
    }

    public HashMap<String, String> findPwChkId(HashMap<String, String> data) {
        return sqlSession.selectOne("MemberDao.findPwChkId", data);
    }

    public int replacePw(HashMap<String, String> data) {
        return sqlSession.update("MemberDao.replacePw", data);
    }

    public HashMap<String, String> findIdFromEmail(HashMap<String, String> data) {
        return sqlSession.selectOne("MemberDao.findIdFromEmail", data);
    }

    public HashMap<String, String> updatePw(HashMap<String, String> data) {
        return sqlSession.selectOne("MemberDao.updatePw", data);
    }

    public HashMap<String, String> findPwCheck(HashMap<String, String> data) {
        return sqlSession.selectOne("MemberDao.findPwCheck", data);
    }

    @SuppressWarnings("rawtypes")
    public int socialMemberCheck(String socialId){
        return sqlSession.selectOne("MemberDao.socialMemberCheck", socialId);
    }

    @SuppressWarnings("rawtypes")
    public String checkTbMember(Map map){
        return sqlSession.selectOne("MemberDao.checkTbMember", map);
    }

    @SuppressWarnings("rawtypes")
    public int insertTbSocial(Map map){
        return sqlSession.insert("MemberDao.insertTbSocial", map);
    }

    @SuppressWarnings("rawtypes")
    public void updateTbSocial(Map map){
        sqlSession.insert("MemberDao.updateTbSocial", map);
    }

    @SuppressWarnings("rawtypes")
    public Member loginCheck_social(Map map){
        return sqlSession.selectOne("MemberDao.loginCheck_social", map);
    }

    @SuppressWarnings("rawtypes")
    public int insertTbMember(MemberVO memberVO){
        return sqlSession.insert("MemberDao.insertTbMember", memberVO);
    }

    public int deleteTbSocial(MemberVO memberVO){
        return sqlSession.delete("MemberDao.socialHistoryDel", memberVO);
    }

    @SuppressWarnings("rawtypes")
    public HashMap<String, Object> selectUserInfo(Map params){
        return sqlSession.selectOne("MemberDao.selectUserInfo", params);
    }

}
*/
