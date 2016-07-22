package cn.com.fml.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.com.fml.utls.Constants;
import cn.com.fml.utls.KeyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JedisTest {
	@Test
	public void testCreateUser() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String sysId = Constants.DEFAULT_SYS_ID;
			String sysLableId= Constants.DEFAULT_LABLE_ID;
			List<String> keys = new ArrayList<String>();
			String userIndexKey = KeyUtils.formatUserIndex();
			String extSystemUserKey = KeyUtils.formatExtSysUserIdMap(sysId);
			String userKey = KeyUtils.formatUserInfo(null);
			String userLabelKey = KeyUtils.formatUserLabelMap(null);
			String labelUserKey = KeyUtils.formatLabelUserIdMap(sysLableId);
			String userQukey = KeyUtils.formatUserQuIdSet(null);
			String lableQuKey = KeyUtils.formatLabelAdIdSet(sysLableId);
			String userScoreKey = KeyUtils.formatUserScore(null);
			String userCashKey = KeyUtils.formatUserCash(null);
			keys.add(userIndexKey);
			keys.add(extSystemUserKey);
			keys.add(userKey);
			keys.add(userLabelKey);
			keys.add(labelUserKey);
			keys.add(userQukey);
			keys.add(lableQuKey);
			keys.add(userScoreKey);
			keys.add(userCashKey);
			
			Map user = new HashMap();
			user.put("openid", "23123");
			List<String> args = new ArrayList<String>();
			String openId = user.get("openid").toString();
			args.add(openId);
			args.add(sysLableId);
			String userJson = JSON.toJSONString(user);
			args.add(userJson);
			args.add(String.valueOf(Long.MAX_VALUE));
			Object userId = jedis.eval("--新建用户id\n"
				+"local userId = redis.call('incr',KEYS[1])\n"
				+"redis.log(redis.LOG_NOTICE, 'userId:'..userId)\n"
				+"--新建外部系统用户映射关系\n"
				+"redis.call('hset',KEYS[2],ARGV[1],userId)\n"
				+"redis.log(redis.LOG_NOTICE, KEYS[2]..' '..ARGV[1]..' '..'userId:'..userId)\n"
				+"--新建用户信息\n"
				+"local user = cjson.decode(ARGV[3])\n"
				+"user['userid'] = userId\n"
				+"local userKey = string.format(KEYS[3], userId)\n"
				+"redis.log(redis.LOG_NOTICE, 'userKey:'..userKey)\n"
				+"for key, var in pairs(user) do\n"
				+"	redis.log(redis.LOG_NOTICE, 'key:'..key..';var:'..var)\n"
				+"  redis.call('hset',userKey,key,var)\n"
				+"end\n"
				+"--新建用户标签\n"
				+"local userLabelKey = string.format(KEYS[4],userId)\n"
				+"redis.log(redis.LOG_NOTICE, 'userLabelKey:'..userLabelKey)\n"
				+"redis.call('hset',userLabelKey,ARGV[2],ARGV[4])\n"
				+"--新建标签用户\n"
				+"redis.call('hset',KEYS[5],userId,ARGV[4])\n"
				+"--新建用户待回答问题ids\n"
				+"local userQukey = string.format(KEYS[6],userId)\n"
				+"redis.log(redis.LOG_NOTICE, 'userQukey:'..userQukey)\n"
				+"redis.call('sunionstore',userQukey,KEYS[7])\n"
				+"--新建用户积分\n"
				+"local userScoreKey = string.format(KEYS[8],userId)\n"
				+"redis.log(redis.LOG_NOTICE, 'userScoreKey:'..userScoreKey)\n"
				+"redis.call('set',userScoreKey,'0')\n"
				+"local userCashKey = string.format(KEYS[9],userId)\n"
				+"redis.log(redis.LOG_NOTICE, 'userCashKey:'..userCashKey)\n"
				+"redis.call('set',userCashKey,'0')\n"
				+"return userId", keys, args);
			System.out.print(userId);
			jedis.select(4);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		// / ... when closing your application:
		pool.destroy();
	}
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
		  argss.add("3");
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
