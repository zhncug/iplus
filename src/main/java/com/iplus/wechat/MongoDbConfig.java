package com.iplus.wechat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoDbConfig {
    private static final String MONGOURL_TEMPLATE = "mongodb://%s:%s@%s:%s";

    @Bean
    @Primary
    public MongoClient mongoClient(@Value("${mongodb.ip}") String ip,
                                   @Value("${mongodb.port}") int port,
                                   @Value("${mongodb.username}") String username,
                                   @Value("${mongodb.password}") String password,
                                   @Value("${mongo.connection.maxidle.time}") Integer maxIdleTime) {

        String mongourl = String.format(MONGOURL_TEMPLATE,username,password,ip,port);
        MongoClientURI uri = new MongoClientURI(mongourl, MongoClientOptions.builder()
                .maxConnectionIdleTime(maxIdleTime));
        return new MongoClient(uri);
    }

    @Bean
    public MongoDbFactory mongoDbFactory(MongoClient mongoClient,@Value("${mongo.wxdb}") String wxDb){
        return new SimpleMongoDbFactory(mongoClient,wxDb);

    }
}
