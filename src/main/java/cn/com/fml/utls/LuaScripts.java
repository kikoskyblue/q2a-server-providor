package cn.com.fml.utls;

import java.util.HashMap;
import java.util.Map;

public class LuaScripts {
	public static final String INCRE_ANSWER_COUNT_CONTENT =
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
			  +"return 'OK'";
	public static final String UPLOAD_ANSWER_CONTENT = 
			"  --更新问题的答案选择比例 \n"
			+"  local answerStr = redis.call('hget',KEYS[1],ARGV[1])\n"
			+"  local answers = cjson.decode(answerStr)\n"
			+"  local lable\n"
			+"  for key, var in ipairs(answers) do\n"
			+"    if var['id'] == tonumber(ARGV[2]) then\n"
			+"	  if var['percentage'] == nil then\n"
			+"		var['percentage'] = 1\n"
			+"	  else\n"
			+"	    var['percentage'] = var['percentage'] + 1\n"
			+"	    lable = var['label']\n"
			+"	  end\n"
			+"	end\n"
			+"  end\n"
			+"  answerStr = cjson.encode(answers)\n"
			+"  redis.call('hset',KEYS[1],ARGV[1],answerStr)\n"
			+"  redis.log(redis.LOG_NOTICE, 'Incre answer`s count success')\n"
			+"  --更新用户积分，用户每日回答问题总数\n"
			+"  local userAldyQuCount = redis.call('incr',KEYS[3])\n"
			+"  redis.log(redis.LOG_NOTICE, 'userAldyQuCount:'..userAldyQuCount)\n"
			+"  --local score = getSocre(userAldyQuCount)\n"
			+"	local score\n"
			+"	if userAldyQuCount >= 0 and userAldyQuCount <= 10 then\n"
			+"    score = 10\n"
			+"  elseif userAldyQuCount <= 20 then\n"
			+"    score = 5\n"
			+"  else\n"
			+"    score = 1\n"
			+"  end\n"
			+"  redis.log(redis.LOG_NOTICE, 'score:'..score)\n"
			+"  local userScore = redis.call('incrBy',KEYS[2],score)\n"
			+"  redis.log(redis.LOG_NOTICE, 'userScore:'..userScore)\n"
			+"  --记录用户获取积分的日志\n"
			+"  local scoreLog = {}\n"
			+"  scoreLog['score'] = score\n"
			+"  scoreLog['time'] = ARGV[3]\n"
			+"  local scoreLogStr = cjson.encode(scoreLog)\n"
			+"  redis.log(redis.LOG_NOTICE, 'scoreLogStr:'..scoreLogStr)\n"
			+"  redis.call('rpush',KEYS[4],scoreLogStr)\n"
			+"  --更新用户已回答问题ids\n"
			+"  redis.call('sadd',KEYS[5],ARGV[4])\n"
			+"  redis.log(redis.LOG_NOTICE, KEYS[5]..' add '..ARGV[4])\n"
			+"  redis.call('srem',KEYS[6],ARGV[4])\n"
			+"  redis.log(redis.LOG_NOTICE, KEYS[4]..' remove '..ARGV[4])\n"
			+"  --更新用户标签,标签用户\n"
			+"  --local lable = redis.call('hget',KEYS[6],'labels')\n"
			+"  redis.log(redis.LOG_NOTICE, 'labelId:'..lable)\n"
			+"  redis.call('hset', KEYS[7], lable, ARGV[5])\n"
			+"  local labelUserKey = string.format(KEYS[8], lable)\n"
			+"  redis.log(redis.LOG_NOTICE, 'labelUserKey:'..labelUserKey)\n"
			+"  redis.call('hset', labelUserKey, ARGV[6], ARGV[5])\n"
			+"  return userScore";
	public static Map<String, String> scripts = new HashMap<String, String>();
	static{
		//scripts.put(Constants.SCRIPT_INCRE_ANSWER_COUNT, INCRE_ANSWER_COUNT_CONTENT);
		scripts.put(Constants.SCRIPT_UPLOAD_ANSWER_CONTENT, UPLOAD_ANSWER_CONTENT);
	}
}
