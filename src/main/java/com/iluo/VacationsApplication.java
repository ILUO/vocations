package com.iluo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@MapperScan("com.iluo.dao")
public class VacationsApplication{
    public static void main(String[] args) {
        SpringApplication.run(VacationsApplication.class, args);
    }
}
