package com.tui.fly.service;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@Profile("relational")
class Database {

    private DataSource database;

    @PostConstruct
    public void createDatabase() {
        database = new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:mem:mymemdb", "SA", "");
    }

    @Bean
    public JdbcOperations jdbc() {
        return new JdbcTemplate(database);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(database);
    }
}
