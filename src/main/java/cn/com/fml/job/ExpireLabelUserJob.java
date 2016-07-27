package cn.com.fml.job;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.fml.utls.Constants;
import cn.com.fml.utls.DateUtil;
import cn.com.fml.utls.JedisUtil;
import cn.com.fml.utls.KeyUtils;

/**
 * 定时失效标签对应用户的Map(KEY<label_%s_user_ids>，<user_%s_label_ids>)的过期用户
 * 同步更新用户待回答的问题ids(KEY<user_%s_question_ids>)
 * @author Administrator
 *
 */
@Component
public class ExpireLabelUserJob {
	private static Logger logger = LoggerFactory.getLogger(ExpireLabelUserJob.class);
	
	@Value("${fml.job.expireLabelUser.enable:false}")
	private boolean enable;
	
	@Autowired
	public JedisUtil jedisUtil;
	
	@Scheduled(cron="${fml.job.expireLabelUser.cron:30 10 1 * * ?}")
	public void excute(){
		if(enable){
			List<String> keys = new ArrayList<String>();
			keys.add(KeyUtils.formatUserLabelMap(null).replace("%s", "*"));
			keys.add(KeyUtils.formatLabelUserIdMap(null).replace("%s", "*"));
			keys.add(KeyUtils.formatUserQuIdSet(null));
			keys.add(KeyUtils.formatLabelQuIdSet(null));
			List<String> args = new ArrayList<String>();
			args.add(String.valueOf(DateUtil.getTime()));
			
			Object result = jedisUtil.SCRIPT.evalsha(Constants.SCRIPT_EXPIRE_LABELUSER_CONTENT,
					keys, args);
			if(Constants.BUSI_CODE_SUCCESS.equals(result)){
				logger.info("ExpireLabelUserJob excute sucessfully");
			}else{
				logger.info("ExpireLabelUserJob excution is not complete");
			}
		}else{
			logger.debug("ExpireLabelUserJob is not enable");
		}
	}
}
