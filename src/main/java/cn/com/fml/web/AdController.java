package cn.com.fml.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.fml.common.BaseController;

@RestController
@RequestMapping("adService")
public class AdController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AdController.class);
	
	@RequestMapping
	public Map getAd(String userid){
		Map reponse = formatReponse();
		String labelId = "100000";//默认system标签，后期改造
		String adId = adService.getLabelRandAdId(labelId);
		Map adInfo = adService.getAdInfo(adId);
		reponse.put("AD", adInfo);
		return reponse;
	}
}
