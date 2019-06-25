package com.iplus.config;

import com.iplus.wechat.common.FullBeanNameGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration(exclude = {
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        CacheAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
        RedisAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class})
@Configuration
@ComponentScan(basePackages = "com.iplus.wechat",
        nameGenerator = FullBeanNameGenerator.class,
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.iplus.wechat.api.*"))
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RootConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsAsyncClientHttpRequestFactory());
    }
}
