package cn.com.fml.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.fml.service.AdService;
import cn.com.fml.service.QuestionService;
import cn.com.fml.service.UserService;
import cn.com.fml.utls.Constants;

public class BaseController {
	@Autowired
	protected QuestionService quService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected AdService adService;
	
	protected Map<String, Object> formatReponse(){
		return formatReponse(null);
	}
	
	protected Map<String, Object> formatReponse(String resultCode){
		Map<String, Object> response = new HashMap<String, Object>();
		if(resultCode == null)
			response.put("resultCode", Constants.RESPONSE_CODE_SUCCESS);
		else
			response.put("resultCode", resultCode);
		return response;
	}
}
