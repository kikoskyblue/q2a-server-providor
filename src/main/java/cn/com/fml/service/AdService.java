package cn.com.fml.service;

import java.util.Map;

public interface AdService {
	/**
	 * 获取广告info
	 * @param id 广告id
	 * @return
	 */
	public Map getAdInfo(String id);
	/**
	 * 获取标签对应随机广告id
	 * @return
	 */
	public String getLabelRandAdId(String labelId);
}
