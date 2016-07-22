package cn.com.fml.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.com.fml.common.BaseService;
import cn.com.fml.service.UserService;
import cn.com.fml.utls.Constants;
import cn.com.fml.utls.DateUtil;
import cn.com.fml.utls.KeyUtils;

import com.alibaba.fastjson.JSON;

@Component
public class UserServiceImpl extends BaseService implements UserService {

	@Override
	public Map getUserInfo(Map user) {
		String extSystemUserKey = KeyUtils.formatExtSysUserIdMap("10000");//默认是微信，后期改造
		String openId = user.get("openid").toString();
		if(jedisUtil.HASH.hexists(extSystemUserKey, openId)){
			String userId = jedisUtil.HASH.hget(extSystemUserKey, openId);
			String userKey = KeyUtils.formatUserInfo(userId);
			Map userInfp = jedisUtil.HASH.hgetAll(userKey);
			return user;
		}else{
			return createUser(user);
		}
	}

	@Override
	public Map getUserInfoByUserId(String userId) {
		String key = KeyUtils.formatUserInfo(userId);
		Map userInfo = jedisUtil.HASH.hgetAll(key);
		return userInfo;
	}

	@Override
	public String setUserInfo(Map user) {
		String key = KeyUtils.formatUserInfo(user.get("userId").toString());
		String result = jedisUtil.HASH.hmset(key, user);
		return result;
	}

	@Override
	public Map createUser(Map user) {
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
		
		List<String> args = new ArrayList<String>();
		String openId = user.get("openid").toString();
		args.add(openId);
		args.add(sysLableId);
		String userJson = JSON.toJSONString(user);
		args.add(userJson);
		args.add(String.valueOf(Long.MAX_VALUE));
		
		Object userId = jedisUtil.SCRIPT.evalsha(Constants.SCRIPT_CREATE_USER_CONTENT, keys, args);
		user.put("userid", userId);
		return user;
	}
	
	/*@Override
	public Map createUser(Map user) {
		String userIndexKey = KeyUtils.formatUserIndex();
		String userId = String.valueOf(jedisUtil.STRINGS.incrBy(userIndexKey, 1L));
		String sysLableId = "10000";
		String extSystemUserKey = KeyUtils.formatExtSysUserIdMap(sysLableId);
		String openId = user.get("openid").toString();
		//新建外部系统用户映射关系
		jedisUtil.HASH.hset(extSystemUserKey, openId, userId);
		//新建用户信息
		user.put("userid", userId);
		String userKey = KeyUtils.formatUserInfo(userId);
		jedisUtil.HASH.hsetall(userKey, user);
		//新建用户标签
		String userLabelKey = KeyUtils.formatUserLabelMap(userId);
		jedisUtil.HASH.hset(userLabelKey, sysLableId, String.valueOf(Long.MAX_VALUE));
		//新建标签用户
		String labelUserKey = KeyUtils.formatLabelUserIdMap(sysLableId);
		jedisUtil.HASH.hset(labelUserKey, userId, String.valueOf(Long.MAX_VALUE));
		//新建用户待回答问题ids
		String userQukey = KeyUtils.formatUserQuIdSet(userId);
		String lableQuKey = KeyUtils.formatLabelAdIdSet(sysLableId);
		jedisUtil.SETS.sunionstore(userQukey, lableQuKey);
		
		//新建用户积分
		String userScoreKey = KeyUtils.formatUserScore(userId);
		jedisUtil.STRINGS.set(userScoreKey, "0");
		//新建用户提现
		String userCashKey = KeyUtils.formatUserCash(userId);
		jedisUtil.STRINGS.set(userCashKey, "0");
		
		return user;
	}*/

	@Override
	public long updateCash(String userId, String userAccount, long money) {
		String cashKey = KeyUtils.formatUserCash(userId);
		long totalMoney = jedisUtil.STRINGS.incrBy(cashKey, money);
		return totalMoney;
	}

	@Override
	public String insertCashLog(String userId, String userAccount, long money) {
		String cashLogkey = KeyUtils.formatUserCashLogList(userId);
		Map<String, Object> cashLogMap = new HashMap<String, Object>();
		cashLogMap.put("money", money);
		cashLogMap.put("time", DateUtil.getTime());
		String cashLogStr = JSON.toJSONString(cashLogMap);
		jedisUtil.LISTS.rpush(cashLogkey, cashLogStr);
		return null;
	}

	@Override
	public long updateScore(String userId, long score) {
		String userScoreKey = KeyUtils.formatUserScore(userId);
		long userScore = jedisUtil.STRINGS.incrBy(userScoreKey, score);
		return userScore;
	}

	@Override
	public String insertScoreLog(String userId, long score) {
		String userScoreLogKey = KeyUtils.formatUserScoreLogList(userId);
		long nowTime = DateUtil.getTime();
		Map<String, Long> scoreLogMap = new HashMap<String, Long>();
		scoreLogMap.put("score", score);
		scoreLogMap.put("time", nowTime);
		String scoreLogStr = JSON.toJSONString(scoreLogMap);
		jedisUtil.LISTS.rpush(userScoreLogKey, scoreLogStr);
		return Constants.BUSI_CODE_SUCCESS;
	}

}
