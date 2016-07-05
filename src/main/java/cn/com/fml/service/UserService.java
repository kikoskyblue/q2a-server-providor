package cn.com.fml.service;

import java.util.Map;
import java.util.Set;

public interface UserService {
	/**
	 * 根据用户id返回问题ids
	 * @param userId 用户id
	 * @return 
	 */
	public Set getQuIdsByUserId(String userId);
	/**
	 * 根据openid返回用户info
	 * @param openId 微信用户唯一码
	 * @return
	 */
	public Map getUserInfoByOpenId(String openId);
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
