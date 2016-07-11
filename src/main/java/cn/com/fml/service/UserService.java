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
	 * 若不存在，则新建用户info（初始化用户id，标签，积分，待回答问题，已回答问题）
	 * @param openId 微信用户唯一码
	 * @return
	 */
	public Map getUserInfo(Map user);
	/**
	 * 创建新用户相关信息
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
}
