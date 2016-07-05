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
	 * @return List<question_%s_info>
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
	 * @return List<user_%s_question_ids>
	 */
	public static List<String> formatUsersQuIdSet(Set<String> ids){
		List<String> keys = new ArrayList<String>();
		for(String id : ids){
			keys.add(formatUserQuIdSet(id));
		}
		return keys;
	}
	/**
	 * 组装标签对应的用户列表的KEY
	 * @param id 标签id
	 * @return label_%s_user_ids
	 */
	public static String formatLabelUserIdMap(String id){
		String key = String.format(Constants.LABEL_PREFIX_KEY+Constants.USER_ID_MAP_SUFFIX_KEY,id);
		return key;
	}
	
	/**
	 * 组装标签对应的用户列表的KEYS
	 * @param ids 标签ids
	 * @return List<label_%s_user_ids>
	 */
	public static List<String> formatLabelsUserIdMap(Set<String> ids){
		List<String> keys = new ArrayList<String>();
		for(String id : ids){
			keys.add(formatQuInfo(id));
		}
		return keys;
	}
	/**
	 * 组装用户对应已经回答的问题ids
	 * @param id 用户id
	 * @return user_%s_question_aldy_ids
	 */
	public static String formateUserAldyQuIdSet(String id){
		String key = String.format(Constants.USER_PREFIX_KEY+Constants.QU_ALDY_ID_SET_SUFFIX_KEY, id);
		return key;
	}
}
