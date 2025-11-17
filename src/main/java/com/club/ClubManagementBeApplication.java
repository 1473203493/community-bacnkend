package com.club;

import com.club.properties.AliOssProperties;
import com.club.properties.WxProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = { AliOssProperties.class, WxProperties.class})
public class ClubManagementBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubManagementBeApplication.class, args);
    }

}