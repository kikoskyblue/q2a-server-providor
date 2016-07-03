package cn.com.fml.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 配置
 * @author krad
 *
 */
@Configuration
public class RedisConfig {
	@Value("${fml.redis.ip:localhost}")
	private String ip;
	
	@Value("${fml.redis.port:6379}")
	private int port;
	
	@Bean
	public JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		return jedisPoolConfig;
	}
	
	@Bean
	public JedisConnectionFactory jedisFactory(@Qualifier("jedisPoolConfig") JedisPoolConfig poolConfig){
		JedisConnectionFactory jedisFactory = new JedisConnectionFactory(poolConfig);
		jedisFactory.setHostName(ip);
		jedisFactory.setPort(port);
		JedisPool jedisPool = new JedisPool();
		return jedisFactory;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean
	public RedisTemplate redisTemplate(@Qualifier("jedisFactory") JedisConnectionFactory jedisFactory){
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisFactory);
		return redisTemplate;
	}
}
