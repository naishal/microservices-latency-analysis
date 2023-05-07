package com.dis.productservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

//    @Value("${spring.data.mongodb.connection-minimum-idle-size:0}")
//    private Integer minIdle;
//
//    @Value("${spring.data.mongodb.connection-maximum-pool-size:100}")
//    private Integer maxPoolSize;
//
//    @Value("${spring.data.mongodb.connection-max-idle-time:0}")
//    private Integer maxIdleTimeMS;
//
//    @Value("${spring.data.mongodb.connection-max-life-time:0}")
//    private Integer maxLifeTime;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(uri);
//        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
//                .minSize(minIdle)
//                .maxSize(maxPoolSize)
//                .maxIdleTimeMS(maxIdleTimeMS)
//                .maxLifeTime(maxLifeTime)
//                .build();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder
                        .maxSize(200)
                        .minSize(40)
                        .maxWaitTime(3000, TimeUnit.MILLISECONDS)
                        .maxConnectionIdleTime(30000, TimeUnit.MILLISECONDS)
                        .maxConnectionLifeTime(60000, TimeUnit.MILLISECONDS)
                        .build())
                .applyToSocketSettings(builder -> builder.connectTimeout(3000, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), "product-service");
    }
}
