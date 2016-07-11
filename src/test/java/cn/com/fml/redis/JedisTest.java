package cn.com.fml.redis;

import java.util.ArrayList;
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
		  
		  
		  List<String> keys = new ArrayList<String>();
		  keys.add("question_10007_info");
		  keys.add("answers");
		  
		  List<String> argss = new ArrayList<String>();
		  argss.add("2");
		  String abc = jedis.scriptLoad("local answers = redis.call('hget',KEYS[1],KEYS[2])\n"
				  +"local lua_value = cjson.decode(answers)\n"
				  +"for key, var in ipairs(lua_value) do\n"
				  +"    print( var)\n"
				  +"if var['id'] == tonumber(ARGV[1]) then\n"
				  +"	if var['percentage'] == nil then\n"
				  +"		var['percentage'] = 1"
				  +"	else\n"
				  +"		var['percentage'] = var['percentage'] + 1\n"
				  +"	end\n"
				  +"end\n"
				  +"end\n"
				  +"local result = cjson.encode(lua_value)\n"
				  +"redis.call('hset',KEYS[1],KEYS[2], result)\n"
				  +"redis.log(redis.LOG_NOTICE, 'Incre answer`s count success')\n"
				  +"return 'OK'");
		  Object aa = jedis.evalsha(abc, keys, argss);//"8370b4ac42f3eafb120f7b7bfc026e6ed64b71c0"
		  /*Object aa = jedis.eval(
				  "local answers = redis.call('hget',KEYS[1],KEYS[2])\n"
				  +"local lua_value = cjson.decode(answers)\n"
				  +"for key, var in ipairs(lua_value) do\n"
				  +"    print( var)\n"
				  +"if var['id'] == tonumber(ARGV[1]) then\n"
				  +"	if var['percentage'] == nil then\n"
				  +"		var['percentage'] = 1"
				  +"	else\n"
				  +"		var['percentage'] = var['percentage'] + 1\n"
				  +"	end\n"
				  +"end\n"
				  +"end\n"
				  +"local result = cjson.encode(lua_value)\n"
				  +"redis.call('hset',KEYS[1],KEYS[2], result)\n"
				  +"redis.log(redis.LOG_NOTICE, 'Incre answer`s count success')\n"
				  +"return 'OK'", keys, argss);*/
		  System.out.println(aa);
		  
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
