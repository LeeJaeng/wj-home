package org.woojeong.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(
            build("banners",    1440),  // 메인 배너 (공개) — 24h
            build("allBanners", 1440),  // 메인 배너 (관리자용 전체) — 24h
            build("histories",  10080), // 교회 역사 — 7일
            build("staff",      1440),  // 섬기는 사람들 — 24h
            build("deptList",   1440),  // 교육부서 목록 — 24h
            build("deptDetail", 1440)   // 교육부서 상세 (dept_key 별) — 24h
        ));
        return manager;
    }

    private CaffeineCache build(String name, int ttlMinutes) {
        return new CaffeineCache(name,
            Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(ttlMinutes, TimeUnit.MINUTES)
                .build());
    }
}
