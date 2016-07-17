package cn.com.fml.service;

import java.util.Map;
import java.util.Set;

public interface UserService {
	/**
	 * 根据用户id返回问题ids
	 * @param userId 用户id
	 * @return 
	 */
	//public Set getQuIdsByUserId(String userId);
	/**
	 * 根据openid返回用户info
	 * 若不存在，则新建用户info
	 * @param openId 微信用户唯一码
	 * @return
	 */
	public Map getUserInfo(Map user);
	/**
	 * 创建新用户相关信息
	 * 初始化用户id，标签，积分，提现，待回答问题，已回答问题
	 * @param openId
	 * @return
	 */
	public Map createUser(Map user);
	/**
	 * 根据userid返回用户info
	 * @param userId 用户id
	 * @return
	 */
	public Map getUserInfoByUserId(String userId);
	/**
	 * 保存用户info
	 * @param user
	 * @return
	 */
	public String setUserInfo(Map user);
	/**
	 * 提现
	 * @param userId
	 * @param userAccount 用户账户
	 * @param money
	 * @return
	 */
	public long updateCash(String userId, String userAccount, long money);
	/**
	 * 新增用户提现日志
	 * @param userId
	 * @param userAccount
	 * @param money
	 * @return
	 */
	public String insertCashLog(String userId, String userAccount, long money);
	/**
	 * 更新用户积分
	 * @param userId
	 * @param score
	 * @return
	 */
	public long updateScore(String userId, long score);
	/**
	 * 新增用户更新积分日志
	 * @param userId
	 * @param score
	 * @return
	 */
	public String insertScoreLog(String userId, long score);
}
