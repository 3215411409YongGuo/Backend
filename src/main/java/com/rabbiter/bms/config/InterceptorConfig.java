package com.rabbiter.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //Indicates it is a configuration class
public class InterceptorConfig implements WebMvcConfigurer {

    // 所有的页面和请求
    private static final String[] all = {
            "/**/*.html",
            "/bookInfo/**",
            "/bookType/**",
            "/borrow/**",
            "/update/**",
            "/user/**"
    };

    // Interfaces and requests related to login and registration
    public static final String[] aboutLogin = {
            "/",
            "/index.html",
            "/register.html",
            "/user/login",
            "/user/register"
    };

    // Pages and requests related to readers
    public static final String[] aboutReader = {
            "/reader/**/*.html",    // Reader Page
            "/*/reader/**",         // Reader's Request
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Note that due to browser cache issues, it may not be possible to completely block the page, but the function requests will definitely not be processed. Therefore, this does not affect security.

        /**
         * User interceptor concept：
         * 1. Intercept all pages and requests
         * 2. Exclude those related to login and registration
         */
        /*registry.addInterceptor(new UserInterceptor())
                .addPathPatterns(all)
                .excludePathPatterns(aboutLogin);*/

        /**
         * Reader interceptor approach：
         * 1. Intercept all pages and requests
         * 2. Exclude pages and requests related to login
         * 3. Exclude pages and requests related to readers
         */
        /*registry.addInterceptor(new ReaderInterceptor())
                .addPathPatterns(all)
                .excludePathPatterns(aboutLogin)
                .excludePathPatterns(aboutReader);*/
    }
}
