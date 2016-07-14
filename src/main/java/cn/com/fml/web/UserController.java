package cn.com.fml.web;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.fml.common.BaseController;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {
	@RequestMapping
	public Map login(Map user){
		Map<String, Object> result = formatReponse();
		Map userFrmCache = userService.getUserInfo(user);
		result.put("user", userFrmCache);
		return result;
	}
	@RequestMapping
	public Map getUserInfo(String userId){
		Map<String, Object> result = formatReponse();
		Map userFrmCache = userService.getUserInfoByUserId(userId);
		result.put("user", userFrmCache);
		return result;
	}
	@RequestMapping
	public Map postUserInfo(Map user){
		Map<String, Object> result = formatReponse();
		userService.setUserInfo(user);
		return result;
	}
	@RequestMapping
	public Map getCash(String userId, String score, String userAccount){
		Map<String, Object> result = formatReponse();
		return result;
	}
}
