package com.bf.config;

import com.bf.common.Constants;
import com.bf.common.util.ConfigUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.DefaultVFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(annotationClass = Mapper.class
        ,sqlSessionTemplateRef = "bfSqlSessionTemplate"
        ,basePackages = { Constants.WEB_PACKAGE }
)
public class DataBaseConfig {

    @Value(value="${spring.datasource.bf.driver-class-name}") private String jdbc_driver;
    @Value(value="${spring.datasource.bf.jdbc-url}") private String jdbc_url;
    @Value(value="${spring.datasource.bf.username}") private String jdbc_username;
    @Value(value="${spring.datasource.bf.password}") private String jdbc_password;

    @Bean(name = "bfDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.bf")
    public BasicDataSource bfDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.jdbc_driver);
        dataSource.setUrl( this.jdbc_url );
        dataSource.setUsername( this.jdbc_username );
        dataSource.setPassword( this.jdbc_password );

        return dataSource;
    }

    @Bean(name = "bfSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionFactory(@Qualifier("bfDatasource") DataSource bfDatasource) throws Exception{
        /** SESSION FACTORY 생성 **/
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource( bfDatasource );
        sqlSessionFactoryBean.setConfigLocation( ConfigUtil.getResource( Constants.MYBATIS_CONFIG_LOCATION  ) );
        sqlSessionFactoryBean.setMapperLocations( ConfigUtil.getResources( Constants.MYBATIS_MAPPER_LOCATIONS ) );
        sqlSessionFactoryBean.setVfs( DefaultVFS.class );

        sqlSessionFactoryBean.afterPropertiesSet();
        return new SqlSessionTemplate( sqlSessionFactoryBean.getObject() );
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("bfDatasource") DataSource bfDatasource ) {
        /** API TRANSACTION MANAGER **/
        DataSourceTransactionManager transaction = new DataSourceTransactionManager( bfDatasource );
        return transaction;
    }

}
