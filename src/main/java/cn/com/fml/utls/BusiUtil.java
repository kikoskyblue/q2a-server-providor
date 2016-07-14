package cn.com.fml.utls;

public class BusiUtil {
	public static long getSocre(long num){
		if(num > 0 && num <= 10){
			return 10;
		}else if (num <= 20){
			return 5;
		}else{
			return 1;
		}
	}
}
