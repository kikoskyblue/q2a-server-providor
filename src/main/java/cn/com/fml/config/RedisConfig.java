package cn.com.fml.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
	private String port;
	
	public JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		return jedisPoolConfig;
	}
}
