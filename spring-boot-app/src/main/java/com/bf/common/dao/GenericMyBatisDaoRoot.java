package com.bf.common.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 루트 DAO
 */
public abstract class GenericMyBatisDaoRoot<T> extends SqlSessionDaoSupport {
	private String daoName = this.getClass().getSimpleName();

	/**
	 * 타입 파라미터에 해당하는 List를 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public List<T> selectList (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}

	public List<T> bizMsgReqRespList (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	public List<T> productNameList (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	public List<Map> selectRefurbish (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	public List<Map> getList (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	public List<Map> get (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	public List<Map> getMyList (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * 타입 파라미터에 매핑되는 데이터 한로우를 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T selectOne (String sqlMap, Object parameter) {
		return (T) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	public long next (String sqlMap, Object parameter) {
		getSqlSession().clearCache();
		return (Long) getSqlSession().selectOne (daoName + "." + sqlMap, null);
	}
	
	/**
	 * Long 타입의 데이터 한건을 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public Long selectOneLong (String sqlMap, Object parameter) {
		return (Long) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * Int 타입의 데이터 한건을 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public Integer selectOneInt (String sqlMap, Object parameter) {
		return (Integer) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * String 타입의 데이터 한건을 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public String selectOneString (String sqlMap, Object parameter) {
		return (String) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * Object 타입의 데이터 한건을 리턴
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public Object selectOneObject (String sqlMap, Object parameter) {
		return (Object) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * Long 타입의 리스트를 리턴한다.
	 * <P> 3.0.6버전에서는 제네릭 파라미터 타입이 없었는데 3.1 버전에서는 추가되었다.
	 * <p> 어떤 영향을 주게될런지는 모르겠음. 실제 적용하여 테스트 해봐야겠음. TODO
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public List<Long> selectListLong (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * String 타입의 리스트를 리턴한다.
	 * <P> 3.0.6버전에서는 제네릭 파라미터 타입이 없었는데 3.1 버전에서는 추가되었다.
	 * <p> 어떤 영향을 주게될런지는 모르겠음. 실제 적용하여 테스트 해봐야겠음. TODO
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public List<String> selectListString (String sqlMap, Object parameter) {
		return getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * 특정 Object 타입의 리스트를 리턴한다.
	 * <P> 3.0.6버전에서는 제네릭 파라미터 타입이 없었는데 3.1 버전에서는 추가되었다.
	 * <p> 어떤 영향을 주게될런지는 모르겠음. 실제 적용하여 테스트 해봐야겠음. TODO
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public List<?> selectListObject (String sqlMap, Object parameter) {
		return (List<?>) getSqlSession().selectList (daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * 일반 등록
	 * <p>TODO 리턴하는 값이 명확한지 확인해야 함.
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public int insert (String sqlMap, Object parameter) {
		return getSqlSession().insert (daoName + "." + sqlMap, parameter);
	}
	
	//pk가 없으면 insert, 있으면 update
	public int insertUpdate (String sqlMap, Object parameter) {
		return getSqlSession().insert (daoName + "." + sqlMap, parameter);
	}

	public int insertExcel (String sqlMap, Object parameter) {
		return getSqlSession().insert(daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * 일반 수정
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public int update (String sqlMap, Object parameter) {
		return getSqlSession().update(daoName + "." + sqlMap, parameter);
	}
	
	/**
	 * 일반 삭제
	 * <p>TODO 리턴하는 값이 명확한지 확인해야 함.
	 * @param sqlMap
	 * @param parameter
	 * @return
	 */
	public int delete (String sqlMap, Object parameter) {
		int deleted = getSqlSession().delete (daoName + "." + sqlMap, parameter);
		return deleted < 0 ? 0 : deleted;
	}
	
	/**
	 * 기대값이 하나인 결과물 요청 ( 결과물이 없거나, 하나이상의 결과물이 되돌아 올때 )
	 * @param sqlMap 
	 * @param parameter
	 * @return Map (rs, msg, result)
	 */
	public Map<Object, Object> selectListOneCk(String sqlMap, Object parameter) {
		Map<Object, Object> rsMap = new HashMap<Object, Object>();
		List<T> rs = getSqlSession().selectList (daoName + "." + sqlMap, parameter);
		if(rs.size() == 1){
			// 결과가 하나.. 정상
			rsMap.put("rs", rs.get(0));
			rsMap.put("msg", "성공");
			rsMap.put("result", true);
		}else if(rs.size() > 1){
			// 결과가 하나 이상 중복 데이터 발생
			rsMap.put("rs", rs.get(0));
			rsMap.put("msg", "중복데이터 존재함");
			rsMap.put("result", false);
		}else{
			// 결과가 없다
			rsMap.put("rs", null);
			rsMap.put("msg", "데이터 누락");
			rsMap.put("result", false);
		}
		return rsMap;
	}

	public int refurbishExcelInsert(String sqlMap, Map pMap) {
		return insert(daoName + "." + sqlMap, pMap);
	}
	
	public String productPrice (String sqlMap, Object parameter) {
		return (String) getSqlSession().selectOne (daoName + "." + sqlMap, parameter);
	}
	
}
