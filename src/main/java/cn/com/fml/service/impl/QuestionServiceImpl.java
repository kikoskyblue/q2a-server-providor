package cn.com.fml.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import cn.com.fml.common.BaseService;
import cn.com.fml.service.QuestionService;
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
		// TODO Auto-generated method stub
		return null;
	}

}
