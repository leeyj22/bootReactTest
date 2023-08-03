package com.bf.config;

import com.bf.common.Constants;
import com.bf.common.util.ConfigUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.io.DefaultVFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(sqlSessionTemplateRef = "svcSqlSessionTemplate"
        ,basePackages = { Constants.SVC_PACKAGE }
)
public class SVCDataBaseConfig {

    @Value(value="${spring.datasource.svc.driver-class-name}") private String jdbc_driver;
    @Value(value="${spring.datasource.svc.jdbc-url}") private String jdbc_url;
    @Value(value="${spring.datasource.svc.username}") private String jdbc_username;
    @Value(value="${spring.datasource.svc.password}") private String jdbc_password;

    @Bean(name = "svcDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.svc")
    public BasicDataSource svcDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.jdbc_driver);
        dataSource.setUrl( this.jdbc_url );
        dataSource.setUsername( this.jdbc_username );
        dataSource.setPassword( this.jdbc_password );

        return dataSource;
    }

    @Bean(name = "svcSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionFactory(@Qualifier("svcDatasource") DataSource svcDatasource) throws Exception{
        /** SESSION FACTORY 생성 **/
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource( svcDatasource );
        sqlSessionFactoryBean.setConfigLocation( ConfigUtil.getResource( Constants.MYBATIS_CONFIG_LOCATION  ) );
        sqlSessionFactoryBean.setMapperLocations( ConfigUtil.getResources( Constants.MYBATIS_MAPPER_LOCATIONS ) );
        sqlSessionFactoryBean.setVfs( DefaultVFS.class );

        sqlSessionFactoryBean.afterPropertiesSet();
        return new SqlSessionTemplate( sqlSessionFactoryBean.getObject() );
    }

    @Primary
    @Bean(name = "svcTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("svcDatasource") DataSource svcDatasource ) {
        /** TRANSACTION MANAGER **/
        DataSourceTransactionManager transaction = new DataSourceTransactionManager( svcDatasource );
        return transaction;
    }

}
