package com.bf.common.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * ROOT Dao를 상속하고 있으며 일반 DAO작성시 이 클래스를 상속받는다.
 * <p>특정 기능이 필요한 경우 각 DAO에서 별도 함수를 작성하면 된다.
 * @author 
 *
 */
public abstract class GenericMyBatisDao<T> extends GenericMyBatisDaoRoot<T> {

	/**
	 * config.xml 의 sqlSessionTemplate을 세팅해 준다.
	 * @param sqlSessionTemplateMysql
	 */

	@Resource
	public void init (SqlSessionTemplate sqlSessionTemplateMysql) {
		setSqlSessionTemplate(sqlSessionTemplateMysql);
	}
}
