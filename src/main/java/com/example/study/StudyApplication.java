package com.example.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBootApplicationは@Configuration、@EnableAutoConfiguration、@ComponentScan(basePackages = "com.example.study")をまとめたアノテーション
// NOTE: 本プロジェクトでは、AutoConfigurationは使用していない。
@SpringBootApplication
public class StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
    }
}
