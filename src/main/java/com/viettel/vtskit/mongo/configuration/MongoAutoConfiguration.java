package com.viettel.vtskit.mongo.configuration;

import com.viettel.vtskit.mongo.MongoService;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MongoAutoConfiguration {
    @Bean
    @ConfigurationProperties(
            prefix = "mongodb"
    )
    @Primary
    public MongoProperties properties(){
        return new CustomMongoProperties();
    };
    @Bean
    MongoService mongoService(){
        return new MongoService();
    }
}
