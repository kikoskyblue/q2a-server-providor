package cn.com.fml.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import cn.com.fml.common.BaseService;
import cn.com.fml.service.QuestionService;
import cn.com.fml.utls.BusiUtil;
import cn.com.fml.utls.Constants;
import cn.com.fml.utls.DateUtil;
import cn.com.fml.utls.KeyUtils;

import com.alibaba.fastjson.JSON;

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
		return getQu(id, true);
	}
	
	public Map getQu(String id, boolean excludeLabel) {
		String key =  KeyUtils.formatQuInfo(id);
		Map qu = jedisUtil.HASH.hgetAll(key);
		String answersStr = qu.get("answers").toString();
		List<Map> answers = JSON.parseArray(answersStr, Map.class);
		if(excludeLabel){
			for(Map map:answers){
				map.remove("label");
			}
			qu.remove("labels");
		}
		qu.put("answers", answers);
//		String labelsStr = qu.get("labels").toString();
//		List labels = JSON.parseArray(labelsStr, String.class);
//		qu.put("labels", labels);
		return qu;
	}
	@Override
	public List<Map> getAnswers(String quId) {
		String key = KeyUtils.formatQuInfo(quId);
		String answers = jedisUtil.HASH.hget(key, Constants.QU_ANSWERS_FIELD);
		List<Map> answersList = JSON.parseArray(answers, Map.class);
		int count = 0;
		for(Map answer:answersList){
			if(answer.get("percentage") != null){
				count += Integer.valueOf(answer.get("percentage").toString());
			}
		}
		int ratio = 0;
		for(Map answer:answersList){
			if(answer.get("percentage") != null){
				answer.put("percentage", Integer.valueOf(answer.get("percentage").toString())*100/count);
			}else{
				answer.put("percentage", 0);
			}
			
		}
		return answersList;
	}
	
	@Override
	public String uploadUserAnswer(String quId, String answerId, String userId) {
		//更新问题的答案选择比例
		List<String> keys = new ArrayList<String>();
		String quInfokey = KeyUtils.formatQuInfo(quId);
		String userScoreKey = KeyUtils.formatUserScore(userId);
		String userAldyQuCountKey = KeyUtils.formatUserAldyQuCount(userId);
		String userScoreLogKey = KeyUtils.formatUserScoreLogList(userId);
		String userAldyQuIdSetKey = KeyUtils.formatUserAldyQuIdSet(userId);
		String userQuIdSetKey = KeyUtils.formatUserQuIdSet(userId);
		String userLabelKey = KeyUtils.formatUserLabelMap(userId);
		String labelUserKey = KeyUtils.formatLabelUserIdMap(null);
		keys.add(quInfokey);
		keys.add(userScoreKey);
		keys.add(userAldyQuCountKey);
		keys.add(userScoreLogKey);
		keys.add(userAldyQuIdSetKey);
		keys.add(userQuIdSetKey);
		keys.add(userLabelKey);
		keys.add(labelUserKey);
		List<String> args = new ArrayList<String>();
		args.add(answerId);
		jedisUtil.SCRIPT.evalsha(Constants.SCRIPT_INCRE_ANSWER_COUNT, keys, args);
		//更新用户积分，用户每日回答问题总数
		
		long userAldyQuCount = jedisUtil.STRINGS.incrBy(userAldyQuCountKey, 1L);
		long score = BusiUtil.getSocre(userAldyQuCount);
		long userScore = jedisUtil.STRINGS.incrBy(userScoreKey, score);
		//记录用户获取积分的日志
		
		long nowTime = DateUtil.getTime();
		Map<String, Long> scoreLogMap = new HashMap<String, Long>();
		scoreLogMap.put("score", score);
		scoreLogMap.put("time", nowTime);
		String scoreLogStr = JSON.toJSONString(scoreLogMap);
		jedisUtil.LISTS.rpush(userScoreLogKey, scoreLogStr);
		//更新用户已回答问题ids
		
		jedisUtil.SETS.sadd(userAldyQuIdSetKey, quId);
		jedisUtil.SETS.srem(userQuIdSetKey, quId);
		
		Map qu = getQu(quId, false);
		String labelId = qu.get("labels").toString();
		//更新用户标签
		
		jedisUtil.HASH.hset(userLabelKey, labelId, String.valueOf(Long.MAX_VALUE));
		//更新标签用户
		
		jedisUtil.HASH.hset(labelUserKey, userId, String.valueOf(Long.MAX_VALUE));

		return String.valueOf(userScore);
	}
	/*@Override
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
		//记录用户获取积分的日志
		String userScoreLogKey = KeyUtils.formatUserScoreLogList(userId);
		long nowTime = DateUtil.getTime();
		Map<String, Long> scoreLogMap = new HashMap<String, Long>();
		scoreLogMap.put("score", score);
		scoreLogMap.put("time", nowTime);
		String scoreLogStr = JSON.toJSONString(scoreLogMap);
		jedisUtil.LISTS.rpush(userScoreLogKey, scoreLogStr);
		//更新用户已回答问题ids
		String userAldyQuIdSetKey = KeyUtils.formatUserAldyQuIdSet(userId);
		String userQuIdSetKey = KeyUtils.formatUserQuIdSet(userId);
		jedisUtil.SETS.sadd(userAldyQuIdSetKey, quId);
		jedisUtil.SETS.srem(userQuIdSetKey, quId);
		
		Map qu = getQu(quId, false);
		String labelId = qu.get("labels").toString();
		//更新用户标签
		String userLabelKey = KeyUtils.formatUserLabelMap(userId);
		jedisUtil.HASH.hset(userLabelKey, labelId, String.valueOf(Long.MAX_VALUE));
		//更新标签用户
		String labelUserKey = KeyUtils.formatLabelUserIdMap(labelId);
		jedisUtil.HASH.hset(labelUserKey, userId, String.valueOf(Long.MAX_VALUE));

		return String.valueOf(userScore);
	}*/

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
