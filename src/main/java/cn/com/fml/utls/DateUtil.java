package cn.com.fml.utls;

import java.util.Date;

public class DateUtil {
	public static final long SECOND = 1000L;
	public static final long MINUTE = 60000L;
	public static final long HOUR = 3600000L;
	public static final long DAY = 86400000L;
	public static final long WEEK = 604800000L;
	public static final long MONTH = 2592000000L;
	public static final long YEAR = 31536000000L;

	public static long getTime() {
		return new Date().getTime();
	}
	public static long getFutureTime(String paramString){
		long future = new Date().getTime()+parseDuration(paramString);
		return future;
	}
	public static long parseDuration(String paramString)
	  {
	    long l = 0L;
	    if ((paramString != null) && (paramString.length() > 0))
	    {
	      char c = paramString.charAt(paramString.length() - 1);
	      String str = paramString.substring(0, paramString.length() - 1);
	      if ((str == null) || (str.length() <= 0))
	        str = "1";
	      double d = Double.parseDouble(str);
	      if ((c == 's') || (c == 'S'))
	        l = (long)(d * SECOND);
	      else if ((c == 'h') || (c == 'H'))
	        l = (long)(d * HOUR);
	      else if ((c == 'd') || (c == 'D'))
	        l = (long)(d * DAY);
	      else if (c == 'm')
	        l = (long)(d * MINUTE);
	      else if ((c == 'w') || (c == 'W'))
	        l = (long)(d * WEEK);
	      else if (c == 'M')
	        l = (long)(d * MONTH);
	      else if ((c == 'y') || (c == 'Y'))
	        l = (long)(d * YEAR);
	      else
	        throw new NumberFormatException("unknown unit " + c);
	    }
	    return l;
	  }
}
