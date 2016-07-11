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
	public static Map<String, String> scripts = new HashMap<String, String>();
	static{
		scripts.put(Constants.SCRIPT_INCRE_ANSWER_COUNT, INCRE_ANSWER_COUNT_CONTENT);
	}
}
