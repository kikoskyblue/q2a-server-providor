package cn.com.fml.web;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.fml.common.BaseController;
import cn.com.fml.utls.BusiUtil;

@RestController
@RequestMapping("userServer")
public class UserController extends BaseController {
	@RequestMapping("login")
	public Map login(Map user){
		Map<String, Object> result = formatReponse();
		Map userFrmCache = userService.getUserInfo(user);
		result.put("user", userFrmCache);
		return result;
	}
	@RequestMapping("getUserInfo")
	public Map getUserInfo(String userId){
		Map<String, Object> result = formatReponse();
		Map userFrmCache = userService.getUserInfoByUserId(userId);
		result.put("user", userFrmCache);
		return result;
	}
	@RequestMapping("postUserInfo")
	public Map postUserInfo(Map user){
		Map<String, Object> result = formatReponse();
		userService.setUserInfo(user);
		return result;
	}
	@RequestMapping("getCash")
	public Map getCash(String userId, long score, String userAccount){
		Map<String, Object> result = formatReponse();
		long money = BusiUtil.getCashFromScore(score);
		userService.updateCash(userId, userAccount, money);
		userService.insertCashLog(userId, userAccount, money);
		userService.updateScore(userId, -score);
		return result;
	}
}
