package com.club.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx")
public class WxProperties {

    private String appId;
    private String appSecret;



}
