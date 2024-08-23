package dev.byli.gateway.demo.config.routes.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.byli.gateway.demo.config.routes.filters.CacheFilter;
@Configuration
@Slf4j
public class CacheConfig {
  @Bean
  CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("myCache");
  }

  @Bean("redisCache")
  Cache manageCache(CacheManager cacheManager) {
    return cacheManager.getCache("myCache");
  }

  @Bean
  public CacheFilter cachedGatewayFilter(Cache cache) {
    return new CacheFilter(cache);
  }
}
