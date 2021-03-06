package cn.com.fml.config;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
@Configuration
public class FmlConfig {
	@Bean
	public HttpMessageConverters customConverters() {
		FastJsonHttpMessageConverter localFastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//		localFastJsonHttpMessageConverter.getFastJsonConfig().setSerializerFeatures(new SerializerFeature[] { SerializerFeature.WriteClassName });
//		localFastJsonHttpMessageConverter
//				.setFeatures(new SerializerFeature[] { SerializerFeature.WriteClassName });
		FastJsonConfig fastJsonConfig = localFastJsonHttpMessageConverter.getFastJsonConfig();
		fastJsonConfig.setFeatures(Feature.DisableSpecialKeyDetect);
		localFastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(
				new HttpMessageConverter[] { localFastJsonHttpMessageConverter });
	}
}
