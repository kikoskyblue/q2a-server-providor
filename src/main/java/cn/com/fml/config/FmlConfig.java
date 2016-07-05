package cn.com.fml.config;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FmlConfig {
	@Bean
	public HttpMessageConverters customConverters() {
		FastJsonHttpMessageConverter localFastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		localFastJsonHttpMessageConverter.getFastJsonConfig().setSerializerFeatures(new SerializerFeature[] { SerializerFeature.WriteClassName });
//		localFastJsonHttpMessageConverter
//				.setFeatures(new SerializerFeature[] { SerializerFeature.WriteClassName });
		return new HttpMessageConverters(
				new HttpMessageConverter[] { localFastJsonHttpMessageConverter });
	}
}
