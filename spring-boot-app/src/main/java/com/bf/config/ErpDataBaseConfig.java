package com.bf.config;

import com.bf.common.Constants;
import com.bf.common.util.ConfigUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.DefaultVFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(sqlSessionTemplateRef = "erpSqlSessionTemplate"
        ,basePackages = { Constants.ERP_PACKAGE }
)
public class ErpDataBaseConfig {

    @Value(value="${spring.datasource.erp.driver-class-name}") private String jdbc_driver;
    @Value(value="${spring.datasource.erp.jdbc-url}") private String jdbc_url;
    @Value(value="${spring.datasource.erp.username}") private String jdbc_username;
    @Value(value="${spring.datasource.erp.password}") private String jdbc_password;

    @Bean(name = "erpDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.erp")
    public BasicDataSource erpDataSource(){
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(this.jdbc_driver);
    dataSource.setUrl( this.jdbc_url );
    dataSource.setUsername( this.jdbc_username );
    dataSource.setPassword( this.jdbc_password );

    return dataSource;
    }

    @Bean(name = "erpSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionFactory(@Qualifier("erpDatasource") DataSource erpDatasource) throws Exception{
    /** SESSION FACTORY 생성 **/
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource( erpDatasource );
    sqlSessionFactoryBean.setConfigLocation( ConfigUtil.getResource( Constants.MYBATIS_CONFIG_LOCATION  ) );
    sqlSessionFactoryBean.setMapperLocations( ConfigUtil.getResources( Constants.MYBATIS_MAPPER_LOCATIONS ) );
    sqlSessionFactoryBean.setVfs( DefaultVFS.class );

    sqlSessionFactoryBean.afterPropertiesSet();
    return new SqlSessionTemplate( sqlSessionFactoryBean.getObject() );
    }

    @Bean(name = "erpTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("erpDatasource") DataSource erpDatasource ) {
    /** ERP TRANSACTION MANAGER **/
    DataSourceTransactionManager transaction = new DataSourceTransactionManager( erpDatasource );
    return transaction;
    }

}

