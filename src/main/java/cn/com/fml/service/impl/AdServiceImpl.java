package cn.com.fml.service.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import cn.com.fml.common.BaseService;
import cn.com.fml.service.AdService;
import cn.com.fml.utls.KeyUtils;

@Component
public class AdServiceImpl extends BaseService implements AdService {

	@Override
	public Map getAdInfo(String id) {
		String key = KeyUtils.formatAdInfo(id);
		Map adInfo = jedisUtil.HASH.hgetAll(key);
		return adInfo;
	}

	@Override
	public String getLabelRandAdId(String labelId) {
		String key = KeyUtils.formatLabelAdIdSet(labelId);
		String adId = jedisUtil.SETS.srandmembers(key);
		return adId;
	}

}
