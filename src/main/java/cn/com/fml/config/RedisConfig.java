package cn.com.fml.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
	@Value("${fml.redis.maxTotal:1000}")
    private int maxTotal;

    @Value("${fml.redis.maxIdle:1000}")
    private int maxIdle;

    @Value("${fml.redis.maxWaitMillis:3000}")
    private long maxWaitMillis;

    @Value("${fml.redis.testOnBorrow:true}")
	private boolean testOnBorrow;
    
    @Value("${fml.redis.host:localhost}")
    private String host;
    
    @Value("${fml.redis.port:6379}")
    private int port;
    
    @Value("${fml.redis.timeout:1000}")
    private int timeout;
    
	@Bean
	public JedisPoolConfig jedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}
	
	@Bean
	public JedisPool jedisPool(@Qualifier("jedisPoolConfig")JedisPoolConfig jedisPoolConfig){
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);
		return jedisPool;
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(@Qualifier("jedisPoolConfig")JedisPoolConfig jedisPoolConfig){
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(host);
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setTimeout(timeout);
		jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
		return jedisConnectionFactory;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean(name="redisTemplate")
	@Primary
	public RedisTemplate redisTemplate(@Qualifier("jedisConnectionFactory")JedisConnectionFactory jedisConnectionFactory){
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
}
