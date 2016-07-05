package cn.com.fml.redis;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JedisTest {

	public static void main(String[] args) {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
		Jedis jedis = null;
		try {
		  jedis = pool.getResource();
		  jedis.select(4);
		  /// ... do stuff here ... for example
		  jedis.set("foo", "bar");
		  String foobar = jedis.get("foo");
		  /*jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike"); 
		  Set<String> sose = jedis.zrange("sose", 0, -1);*/
		  String nickname = jedis.hget("user_10000_info", "nickname");
		  System.out.println(nickname);
		  
		  String answers = jedis.hget("question_10007_info", "answers");
		  System.out.println(answers);
		  List<Map> ans = JSON.parseArray(answers, Map.class);
		  
		  for(Map an : ans){
			  System.out.println(an);
		  }
		} finally {
		  if (jedis != null) {
		    jedis.close();
		  }
		}
		/// ... when closing your application:
		pool.destroy();
	}

}
