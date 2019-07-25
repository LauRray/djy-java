package com.djy.manager.config;

import com.djy.res.Result;
import com.djy.sql.pojo.SysRole;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Date;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        MyObjectMapper objectMapper = new MyObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;

    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        MyObjectMapper objectMapper = new MyObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
        return cacheManager;

    }
//    @Bean(name="redisTemplate")
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        template.setConnectionFactory(factory);
//        //key序列化方式
//        template.setKeySerializer(redisSerializer);
//        //value序列化
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        //value hashmap序列化
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        return template;
//    }



//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory factory){
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//        //生成一个默认配置,通过config对象即可对缓存进行自定义配置
//        config = config.entryTtl(Duration.ofMinutes(-1)).disableCachingNullValues()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));
//        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config)
//                .build();
//        //RedisCacheManager redisCacheManager = RedisCacheManager.create(factory);
//        return cacheManager;
//    }
//
//    private RedisSerializer<String> keySerializer(){
//        return new StringRedisSerializer();
//    }
//
//    private RedisSerializer<Object> valueSerializer(){
//        return new Jackson2JsonRedisSerializer(Object.class);
//    }
}
