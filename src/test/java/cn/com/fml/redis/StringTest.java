package cn.com.fml.redis;

import org.junit.Assert;
import org.junit.Test;

public class StringTest {
	@Test
	public void formate(){
		String a = String.format("aa_%s", "");
		Assert.assertEquals("aa_%s", a);
	}
}
