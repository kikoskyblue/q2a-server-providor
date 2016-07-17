package cn.com.fml.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QuestionService {
	/**
	 * 获取问题列表
	 * @param ids 问题id集合
	 * @return
	 */
	public List getQuList(Set ids);
	/**
	 * 获取问题
	 * @param id 问题id
	 * @return
	 */
	public Map getQu(String id);
	/**
	 * 获取问题答案比例
	 * @param quId 问题id
	 * @return
	 */
	public List<Map> getAnswers(String quId);
	/**
	 * 更新问题答案比例
	 * 更新用户已回答问题ids,用户积分
	 * @param quId
	 * @param answerId
	 * @param userId
	 * @return
	 */
	public String uploadUserAnswer(String quId, String answerId, String userId);
	/**
	 * 获取用户的待回答的问题ids
	 * @param userId 用户id
	 * @return
	 */
	public Set getUserQuIds(String userId);
	/**
	 * 随机获取用户的待回答的问题id
	 * @param userId
	 * @return
	 */
	public String getUserRandQuId(String userId);
//	/**
//	 * 更新用户已回答问题ids
//	 * @param quId 问题id
//	 */
//	public void updateUserAldyQuIds(String userId, String quId);
}
