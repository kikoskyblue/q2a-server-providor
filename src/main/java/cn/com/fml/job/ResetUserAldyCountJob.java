package cn.com.fml.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.fml.utls.Constants;
import cn.com.fml.utls.JedisUtil;
import cn.com.fml.utls.KeyUtils;

/**
 * @author 叶新华 E-mail:yexh@bsfit.com.cn
 * @version 创建时间：2016年7月27日 上午9:41:43
 * 类说明
 * 每天凌晨重置用户当天回答问题数
 */
@Component
public class ResetUserAldyCountJob {
	private static Logger logger = LoggerFactory.getLogger(ResetUserAldyCountJob.class);
	
	@Value("${fml.job.resetUserAldyCount.enable:false}")
	private boolean enable;
	
	@Autowired
	public JedisUtil jedisUtil;
	
	@Scheduled(cron="${fml.job.resetUserAldyCount.cron:30 10 1 * * ?}")
	public void excute(){
		if(enable){
			List<String> keys = new ArrayList<String>();
			keys.add(KeyUtils.formatUserAldyQuCount(null).replace("%s", "*"));
			
			Object result = jedisUtil.SCRIPT.evalsha(Constants.SCRIPT_RESET_USER_ALDY_COUNT,
					keys, null);
			if(Constants.BUSI_CODE_SUCCESS.equalsIgnoreCase((String) result)){
				logger.info("ResetUserAldyCountJob excute sucessfully");
			}else{
				logger.info("ResetUserAldyCountJob excution is not complete");
			}
		}else{
			logger.debug("ResetUserAldyCountJob is not enable");
		}
	}
}
