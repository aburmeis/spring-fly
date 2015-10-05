package com.tui.fly.service;

import liquibase.integration.spring.SpringLiquibase;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@Profile("relational")
@EnableTransactionManagement
@EnableCaching
class Database {

    private DataSource database;

    @PostConstruct
    public void createDatabase() {
        database = new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:mem:mymemdb", "SA", "");
    }
    
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(net.sf.ehcache.CacheManager.newInstance());
    }

    @Bean
    public JdbcOperations jdbc() {
        return new JdbcTemplate(database);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(database);
    }

/*    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(database);
        liquibase.setChangeLog("classpath:liquibase.xml");
        return liquibase;

    }
    */
}
