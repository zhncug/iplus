package com.iplus.wechat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.iplus.wechat.common.CommonDateFormat;
import com.iplus.wechat.common.FullBeanNameGenerator;
import com.iplus.wechat.common.request.RequestContextInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.iplus.wechat.api")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(CommonDateFormat.LONG_DATETIME_FORMAT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(converter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor()).addPathPatterns("/wx/*");
    }
}