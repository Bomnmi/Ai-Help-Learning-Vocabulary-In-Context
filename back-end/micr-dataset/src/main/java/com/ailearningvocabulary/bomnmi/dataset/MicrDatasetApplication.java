package com.ailearningvocabulary.bomnmi.dataset;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.ailearningvocabulary.bomnmi.dataset.mappers")
@EnableDubbo
@SpringBootApplication
@EnableCaching
public class MicrDatasetApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrDatasetApplication.class, args);
    }

}
