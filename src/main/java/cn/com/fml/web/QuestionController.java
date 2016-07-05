package cn.com.fml.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.fml.service.QuestionService;

@RestController
@RequestMapping({"/qu"})
public class QuestionController {
	private Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private QuestionService quService;
	
	
}
