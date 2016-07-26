package cn.com.fml.utls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class LuaScripts {
	private static Logger logger = LoggerFactory.getLogger(LuaScripts.class);
	
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
			+"	if lable ~= nil and lable ~= '' then\n"
			+"    redis.log(redis.LOG_NOTICE, 'labelId:'..lable)\n"
			+"    redis.call('hset', KEYS[7], lable, ARGV[5])\n"
			+"    local labelUserKey = string.format(KEYS[8], lable)\n"
			+"    redis.log(redis.LOG_NOTICE, 'labelUserKey:'..labelUserKey)\n"
			+"    redis.call('hset', labelUserKey, ARGV[6], ARGV[5])\n"
			+"  end\n"
			+"  return userScore";
	public static final String CREATE_USER_CONTENT ="--新建用户id\n"
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
			+"return userId";
	public static Map<String, String> scripts = new HashMap<String, String>();
	static{
		//scripts.put(Constants.SCRIPT_INCRE_ANSWER_COUNT, INCRE_ANSWER_COUNT_CONTENT);
//		scripts.put(Constants.SCRIPT_UPLOAD_ANSWER_CONTENT, UPLOAD_ANSWER_CONTENT);
//		scripts.put(Constants.SCRIPT_CREATE_USER_CONTENT, CREATE_USER_CONTENT);
		load();
	}
	/**
	 * 从lua文件加载脚本
	 * @throws IOException 
	 */
	public static void load() {
		String[] type = {"lua"};
		String path = LuaScripts.class.getClassLoader().getResource("lua").getPath();
		File directory = new File(path);
		if(directory.isDirectory()){
			Collection<File> files = FileUtils.listFiles(directory, type, false);
			for(File file : files){
				String fileName = file.getName();
				logger.info("Start to read file:{}", fileName);
				String content = "";
				try {
					content = FileUtils.readFileToString(file, "UTF-8");
				} catch (IOException e) {
					logger.error("Error when read file "+fileName, e);
				}
				if(!StringUtils.isEmpty(content))
					scripts.put(fileName.replace(".lua", ""), content);
			}
			logger.info("Loaded all files:{}", scripts.size());
		}
	}
}
