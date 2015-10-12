package com.tui.fly.service;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("database")
@EnableTransactionManagement
@EnableCaching
class Database {

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(net.sf.ehcache.CacheManager.newInstance());
    }
}
