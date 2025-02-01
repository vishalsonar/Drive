package com.sonar.vishal.drive.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CacheController extends CacheLoader<String, byte[]> {

    @Autowired
    private byte[] bytes;

    @Autowired
    private Logger logger;

    @Value("${drive.cache.initial.capacity}")
    private int initialCapacity;

    @Value("${drive.cache.expire.time.milli.second}")
    private int cacheExpireTime;

    private LoadingCache<String, byte[]> loadingCache;

    @PostConstruct
    public void init() {
        loadingCache = CacheBuilder.newBuilder()
                .initialCapacity(initialCapacity)
                .expireAfterAccess(Duration.of(cacheExpireTime, ChronoUnit.MILLIS))
                .build(this);
    }

    @Override
    public byte[] load(String key) throws Exception {
        return bytes;
    }

    public void put(String key, byte[] data) {
        loadingCache.put(key, data);
    }

    public boolean has(String key) {
        return get(key).length > 0;
    }

    public byte[] get(String key) {
        byte[] data = null;
        try {
            data = loadingCache.get(key);
        } catch (ExecutionException executionException) {
            logger.error("Failed to load data from cache :: " + key, executionException);
        }
        return data;
    }
}
