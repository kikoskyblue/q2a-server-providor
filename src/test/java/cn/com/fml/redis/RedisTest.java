package cn.com.fml.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import redis.clients.jedis.JedisPoolConfig;

public class RedisTest {
	public static void main(String[] args){
      JedisConnectionFactory factory = new JedisConnectionFactory();
      factory.setHostName("localhost");
      factory.setPort(6379);
      factory.setPoolConfig(new JedisPoolConfig());
      factory.setDatabase(4);
      factory.afterPropertiesSet();
      final RedisTemplate template = new RedisTemplate();
      template.setConnectionFactory(factory);
      template.setEnableTransactionSupport(true);
      template.afterPropertiesSet();
      HashOperations opers =  template.opsForHash();
      opers.put("map1", "key1", "fffffff");
      String a = (String) opers.get( "map1", "key1");
      String nickname = (String) opers.get( "user_10000_info", "nickname");
      
	}
}
