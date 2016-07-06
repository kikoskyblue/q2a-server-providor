package cn.com.fml.service;

import java.util.Set;

public interface LabelService {
	/**
	 * 获取标签相应的问题ids
	 * @param id 标签id
	 * @return
	 */
	public Set<String> getLabelQuIds(String id);
	/**
	 * 获取标签相应的广告ids
	 * @param id 标签id
	 * @return
	 */
	public Set<String> getLabelAdIds(String id);
	/**
	 * 随机获取标签相应的广告id
	 * @param id 标签id
	 * @return
	 */
	public String getLabelRandAdId(String id);
}
