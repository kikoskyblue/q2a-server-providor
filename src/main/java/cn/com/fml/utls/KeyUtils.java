package cn.com.fml.utls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 组装KEY的工具类
 * @author Administrator
 *
 */
public class KeyUtils {
	/**
	 * 组装问题的KEY
	 * @param id 问题id
	 * @return question_%s_info
	 */
	public static String formatQuInfo(String id){
		String key = String.format(Constants.QU_PREFIX_KEY+Constants.INFO_SUFFIX_KEY,id);
		return key;
	}
	/**
	 * 组装所有问题的KEYS
	 * @param id 问题ids
	 * @return List question_%s_info
	 */
	public static List<String> formatQusInfo(Set<String> ids){
		List<String> keys = new ArrayList<String>();
		for(String id : ids){
			keys.add(formatQuInfo(id));
		}
		return keys;
	}
	/**
	 * 组装用户待回答的问题ID列表的KEY
	 * @param id 用户id
	 * @return user_%s_question_ids
	 */
	public static String formatUserQuIdSet(String id){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.QU_ID_SET_SUFFIX_KEY,id);
		return key;
	}
	/**
	 * 组装所有用户待回答的问题ID列表的KEYS
	 * @param ids 用户ids
	 * @return List user_%s_question_ids
	 */
	public static List<String> formatUsersQuIdSet(Set<String> ids){
		List<String> keys = new ArrayList<String>();
		for(String id : ids){
			keys.add(formatUserQuIdSet(id));
		}
		return keys;
	}
	/**
	 * 组装用户对应已经回答的问题ids的KEY
	 * @param id 用户id
	 * @return user_%s_question_aldy_ids
	 */
	public static String formatUserAldyQuIdSet(String id){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.QU_ALDY_ID_SET_SUFFIX_KEY, id);
		return key;
	}
	/**
	 * 组装用户每日回答问题总数的KEY
	 * @param userId
	 * @return
	 */
	public static String formatUserAldyQuCount(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.QU_ALDY_COUNT_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户index的KEY
	 * @return user_id_index
	 */
	public static String formatUserIndex(){
		return Constants.USER_INDEX_KEY;
	}
	/**
	 * 组装用户info的KEY
	 * @param userId
	 * @return user_%s_info
	 */
	public static String formatUserInfo(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.INFO_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户积分KEY
	 * @param userId 用户id
	 * @return user_%s_score
	 */
	public static String formatUserScore(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.SCORE_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户获取积分log的KEY
	 * @param userId 用户id
	 * @return user_%s_score_log
	 */
	public static String formatUserScoreLogList(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.SCORE_SUFFIX_KEY+Constants.LOG_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户提现KEY
	 * @param userId 用户id
	 * @return user_%s_cash
	 */
	public static String formatUserCash(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.CASH_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户提现log的KEY
	 * @param userId
	 * @return
	 */
	public static String formatUserCashLogList(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.CASH_SUFFIX_KEY+Constants.LOG_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装用户对应的标签ids的KEY
	 * @param userId 用户id
	 * @return user_%s_lable_ids
	 */
	public static String formatUserLabelMap(String userId){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.LABEL_ID_SET_SUFFIX_KEY, userId);
		return key;
	}
	/**
	 * 组装标签对应的用户列表的KEY
	 * @param id 标签id
	 * @return label_%s_user_ids
	 */
	public static String formatLabelUserIdMap(String id){
		String format = Constants.LABEL_PREFIX_KEY+Constants.USER_ID_MAP_SUFFIX_KEY;
		if(id == null)
			return format;
		String key = String.format(format,id);
		return key;
	}
	/**
	 * 组装标签对应的用户列表的KEYS
	 * @param ids 标签ids
	 * @return List label_%s_user_ids
	 */
	public static List<String> formatLabelsUserIdMap(Set<String> ids){
		List<String> keys = new ArrayList<String>();
		for(String id : ids){
			keys.add(formatQuInfo(id));
		}
		return keys;
	}
	/**
	 * 组装标签对应广告ids的KEY
	 * @param labelId 标签id
	 * @return label_%s_ad_ids
	 */
	public static String formatLabelAdIdSet(String labelId){
		String key = String.format(Constants.LABEL_PREFIX_KEY+Constants.AD_ID_SET_SUFFIX_KEY, labelId);
		return key;
	}
	/**
	 * 组装标签对应的问题ids的KEY
	 * @param labelId 标签id
	 * @return label_%s_question_ids
	 */
	public static String formatLabelQuIdSet(String labelId){
		String key = String.format(Constants.LABEL_PREFIX_KEY+Constants.QU_ID_SET_SUFFIX_KEY, labelId);
		return key;
	}
	/**
	 * 组装广告info的KEY
	 * @param adId 广告id
	 * @return ad_%s_info
	 */
	public static String formatAdInfo(String adId){
		String key = String.format(Constants.AD_PREFIX_KEY+Constants.INFO_SUFFIX_KEY, adId);
		return key;
	}
	/**
	 * 组装外部系统对应用户ids的KEY
	 * @param openId
	 * @return ext_system_%s_user_ids
	 */
	public static String formatExtSysUserIdMap(String openId){
		String key = String.format(Constants.EXT_SYSTEM_PREFIX_KEY+Constants.USER_ID_MAP_SUFFIX_KEY, openId);
		return key;
	}
	
}
