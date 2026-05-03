package com.rabbiter.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //Do you want to send cookies?
                .allowCredentials(true)
                //Set which original domains are allowed  In SpringBoot 2.4.4 and lower versions, use "allowedOrigins("*")"
                .allowedOriginPatterns("*")
                //Which request methods are allowed to be processed?
                .allowedMethods("GET","POST","PUT","DELETE")
                //.allowedMethods("*") //Or allow all to pass.
                //Expose which original request header information
                .allowedHeaders("*");
    }
}
