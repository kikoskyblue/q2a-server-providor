package cn.com.fml.common;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.fml.utls.JedisUtil;

public class BaseService {
	@Autowired
	public JedisUtil jedisUtil;
}
