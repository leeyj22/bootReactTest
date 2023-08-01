package com.bf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins(
                        "http://localhost:3000"
                        , "http://localhost:8080"
                        , "http://172.30.40.34:8080"
                        , "http://172.30.40.34:3000"
                        , "http://localhost:4343"
                        , "https://t.bodyfriend.com"
                        , "https://tauth.bodyfriend.com/"
                );
    }
}