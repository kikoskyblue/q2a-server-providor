package cn.com.fml.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.fml.common.BaseController;

@RestController
@RequestMapping({"/quServer"})
public class QuestionController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@RequestMapping("/getQu")
	public Map getQu(String userId){
		/*Set quIds = quService.getUserQuIds(userid);
		List quList = quService.getQuList(quIds);*/
		String quId = quService.getUserRandQuId(userId);
		Map quInfo = quService.getQu(quId);
		quInfo.put("questionId", quId);
		Map<String, Object> result = formatReponse();
		result.put("question", quInfo);
		logger.info("User [{}] get qu: {}", userId, result);
		return result;
	}
	
	@RequestMapping(value="uploadAnswer",method=RequestMethod.GET)
	public Map uploadAnswer(String userId, String questionId, String answerId){
		String score = quService.uploadUserAnswer(questionId, answerId, userId);
		Map<String, Object> result = formatReponse();
		result.put("userscore", score);
		return result;
	}
	
	@RequestMapping("getAnswer")
	public Map getAnswer(String questionId){
		List<Map> answers = quService.getAnswers(questionId);
		Map<String, Object> result = formatReponse();
		result.put("answers", answers);
		return result;
	}
}
