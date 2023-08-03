package com.bf.web.myinfo.dao;

import com.bf.web.marketing.vo.MarketingAgreeVO;
import com.bf.web.myinfo.mapper.MyinfoMapper;
import com.bf.web.myinfo.vo.Myinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MyinfoDao {

    @Autowired private MyinfoMapper myinfoMapper;

    public int getCoupon(Myinfo myinfo){
        return myinfoMapper.getCoupon(myinfo);
    }

    public int getCouponCount(Myinfo myinfo) {
        return myinfoMapper.getCouponCount(myinfo);
    }

    public List<HashMap<String, String>> selectCouponList(Myinfo myinfo){
        return myinfoMapper.selectCouponList(myinfo);
    }

    public int getPoint(Myinfo myinfo) {
        return myinfoMapper.getPoint(myinfo);
    }

    public int getPointCount(Myinfo myinfo) {
        return myinfoMapper.getPointCount(myinfo);
    }

    public List<HashMap<String, String>> selectPointList(Myinfo myinfo) {
        return myinfoMapper.selectPointList(myinfo);
    }

    public int getRentalOrderCount(Map<String, Object> params) {
        return myinfoMapper.getRentalOrderCount(params);
    }

    public int getOrderCount(Map<String, Object> params){
        return myinfoMapper.getOrderCount(params);
    }

    public List<HashMap<String, Object>> selectOrderList(Map<String, Object> params){
        return myinfoMapper.selectOrderList(params);
    }

    public List<HashMap<String, Object>> selectOrderDetail(Map<String, Object> params) {
        return myinfoMapper.selectOrderDetail(params);
    }

    public List<Map<String, Object>> selectOptionList(HashMap<String, Object> orderInfo) {
        return myinfoMapper.selectOptionList(orderInfo);
    }

    public int getCancelOrderCount(Map<String, Object> params){
        return myinfoMapper.getCancelOrderCount(params);
    }

    public List<HashMap<String, Object>> selectCancelOrderList(Map<String, Object> params){
        return myinfoMapper.selectCancelOrderList(params);
    }

    public int getQnaCount(Myinfo myinfo){
        return myinfoMapper.getQnaCount(myinfo);
    }

    public List<HashMap<String, String>> selectQnaList(Myinfo myinfo){
        return myinfoMapper.selectQnaList(myinfo);
    }

    public int getQnaGroups(){
        return myinfoMapper.getQnaGroups();
    }

    public int insertQna(Myinfo myinfo){
        return myinfoMapper.insertQna(myinfo);
    }

    public HashMap<String, String> selectPersonalList(Myinfo myinfo){
        return myinfoMapper.selectPersonalList(myinfo);
    }

    public Myinfo getMyinfo(Myinfo myinfo){
        return myinfoMapper.getMyinfo(myinfo);
    }

    public int updatePersonal(Myinfo myinfo){
        return myinfoMapper.updatePersonal(myinfo);
    }

    public List<HashMap<String, String>> selectQnaDetail(Myinfo myinfo){
        return myinfoMapper.selectQnaDetail(myinfo);
    }

    public List<HashMap<String, String>> getNomemberOrderList(Myinfo myinfo){
        return myinfoMapper.getNomemberOrderList(myinfo);
    }

    public int insertStatusLog(Map<String, Object> params){
        return  myinfoMapper.insertStatusLog(params);
    }

    public int updateTranState(Map<String, Object> params){
        return myinfoMapper.updateTranState(params);
    }

    public HashMap<String, String> selectCouponConfirm(String number){
        return myinfoMapper.selectCouponConfirm(number);
    }

    public int updateCouponInfo(Map params){
        return myinfoMapper.updateCouponInfo(params);
    }

    public void insertCouponAdd(Map params){
        myinfoMapper.insertCouponAdd(params);
    }

    public void insertCouponAdd2(Map params){
        myinfoMapper.insertCouponAdd2(params);
    }

    public void insertCouponAdd3(Map params){
        myinfoMapper.insertCouponAdd3(params);
    }

    public HashMap<String, String> selectCouponYNChk(String number){
        return myinfoMapper.selectCouponYNChk(number);
    }

    public List<HashMap<String, String>> selectAfterService(String serviceIdx){
        return myinfoMapper.selectAfterService(serviceIdx);
    }

    public int updateMemberCertYn(Map<String, Object> params){
        return myinfoMapper.updateMemberCertYn(params);
    }

    public HashMap<String, String> checkCertify(String userId){
        return myinfoMapper.checkCertify(userId);
    }

    public List<HashMap<String, String>> getReviewWriteInfo(Map subParams){
        return myinfoMapper.getReviewWriteInfo(subParams);
    }

    public String getGoodsImgPath(String modelCode){
        return myinfoMapper.getGoodsImgPath(modelCode);
    }

    public int selectTemporarySCVCount(Map params){
        return myinfoMapper.selectTemporarySCVCount(params);
    }

    public Myinfo getUserInfoForMembership (String userIdx){
        return myinfoMapper.getUserInfoForMembership(userIdx);
    }

    public int updateMemberMarketingAgreeAll (MarketingAgreeVO vo){
        return myinfoMapper.updateMemberMarketingAgreeAll(vo);
    }

}
