package cn.com.fml.utls;

public class BusiUtil {
	/**
	 * 通过当天回答问题的总数，返回当前问题的积分
	 * <=10, 10分
	 * <=20, 5分
	 * 其余1分
	 * @param num 
	 * @return
	 */
	public static long getSocre(long num){
		if(num > 0 && num <= 10){
			return 10;
		}else if (num <= 20){
			return 5;
		}else{
			return 1;
		}
	}
	/**
	 * 积分转换为金额（单位：厘）
	 * @param score
	 * @return
	 */
	public static long getCashFromScore(long score){
		return score*10;
	}
}
