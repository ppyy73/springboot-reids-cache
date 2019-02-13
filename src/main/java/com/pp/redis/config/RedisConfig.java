package com.pp.redis.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author pp
 * @Date 2019年2月13日下午3:50:55
 * @Description redis 缓存配置
 *
 */
@Configuration
@EnableCaching
public class RedisConfig {
	// 如使用注解的话需要配置cacheManager
	@Bean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

		// 默认缓存配置 有效时间 1 天
		config = config.entryTtl(Duration.ofDays(1))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
				.disableCachingNullValues();

		// 缓存名称
		Set<String> cacheName = new HashSet<>();
		cacheName.add("cache_test");
		cacheName.add("user");

		// 缓存设置
		Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
		// 单独设置 缓存 cache_test 缓存时间为 1 分钟
		configMap.put("cache_test", config.entryTtl(Duration.ofMinutes(1)));
		// 设置 User 为 默认缓存 设置
		configMap.put("user", config);

		RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory).initialCacheNames(cacheName)
				.withInitialCacheConfigurations(configMap).build();
		return cacheManager;
	}

	/**
	 * json序列化
	 * 
	 * @return
	 */
	@Bean
	public RedisSerializer<Object> jackson2JsonRedisSerializer() {
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);
		return serializer;
	}

	// 以下两种redisTemplate自由根据场景选择
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		template.setValueSerializer(serializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
}
