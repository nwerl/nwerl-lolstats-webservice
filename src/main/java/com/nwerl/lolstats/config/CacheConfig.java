package com.nwerl.lolstats.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableCaching
@Configuration
public class CacheConfig {
    private final RedisConnectionFactory connectionFactory;

    public static final long FIRST_MATCH_PAGE_TTL_MINUTES = 60;
    public static final long MATCH_PAGE_TTL_MINUTES = 10;

    @Bean
    public CacheManager redisCacheManager() {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(defaultConfig())
                .withInitialCacheConfigurations(configMap())
                .build();
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    private Map<String, RedisCacheConfiguration> configMap() {
        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
        cacheConfig.put("firstMatchPage", defaultConfig().entryTtl(Duration.ofMinutes(FIRST_MATCH_PAGE_TTL_MINUTES)));
        cacheConfig.put("matchPage", defaultConfig().entryTtl(Duration.ofMinutes(MATCH_PAGE_TTL_MINUTES)));

        return cacheConfig;
    }
}
