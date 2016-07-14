package cn.com.fml.service.impl;

import java.util.Map;

import cn.com.fml.common.BaseService;
import cn.com.fml.service.UserService;
import cn.com.fml.utls.KeyUtils;

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
	}

	@Override
	public String cash(String userId, String userAccount, long money) {
		String cashKey = KeyUtils.formatUserCash(userId);
		
		return null;
	}

}
