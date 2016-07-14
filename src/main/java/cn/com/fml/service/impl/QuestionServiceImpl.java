package cn.com.fml.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import cn.com.fml.common.BaseService;
import cn.com.fml.service.QuestionService;
import cn.com.fml.utls.BusiUtil;
import cn.com.fml.utls.Constants;
import cn.com.fml.utls.KeyUtils;

@Component
public class QuestionServiceImpl extends BaseService implements QuestionService {

	@Override
	public List getQuList(Set ids) {
		List keys = KeyUtils.formatQusInfo(ids);
		List quList = jedisUtil.HASH.hmgetAll(keys);
		return quList;
	}

	@Override
	public Map getQu(String id) {
		String key =  KeyUtils.formatQuInfo(id);
		Map qu = jedisUtil.HASH.hgetAll(key);
		return qu;
	}

	@Override
	public List<Map> getAnswers(String quId) {
		String key = KeyUtils.formatQuInfo(quId);
		String answers = jedisUtil.HASH.hget(key, Constants.QU_ANSWERS_FIELD);
		List<Map> answersList = JSON.parseArray(answers, Map.class);
		return answersList;
	}

	@Override
	public String uploadUserAnswer(String quId, String answerId, String userId) {
		//更新问题的答案选择比例
		List<String> keys = new ArrayList<String>();
		String key = KeyUtils.formatQuInfo(quId);
		keys.add(key);
		List<String> args = new ArrayList<String>();
		args.add(answerId);
		jedisUtil.SCRIPT.evalsha(Constants.SCRIPT_INCRE_ANSWER_COUNT, keys, args);
		//更新用户积分，用户每日回答问题总数
		String userScoreKey = KeyUtils.formatUserScore(userId);
		String userAldyQuCountKey = KeyUtils.formatUserAldyQuCount(userId);
		long userAldyQuCount = jedisUtil.STRINGS.incrBy(userAldyQuCountKey, 1L);
		long score = BusiUtil.getSocre(userAldyQuCount);
		long userScore = jedisUtil.STRINGS.incrBy(userScoreKey, score);
		//更新用户已回答问题ids
		String userAldyQuIdSetKey = KeyUtils.formatUserAldyQuIdSet(userId);
		String userQuIdSetKey = KeyUtils.formatUserQuIdSet(userId);
		jedisUtil.SETS.sadd(userAldyQuIdSetKey, quId);
		jedisUtil.SETS.srem(userQuIdSetKey, quId);
		return String.valueOf(userScore);
	}

	@Override
	public Set getUserQuIds(String userId) {
		String key = KeyUtils.formatUserQuIdSet(userId);
		Set quIds = jedisUtil.SETS.smembers(key);
		return quIds;
	}

	@Override
	public String getUserRandQuId(String userId) {
		String key = KeyUtils.formatUserQuIdSet(userId);
		String quId = jedisUtil.SETS.srandmembers(key);
		return quId;
	}

}
