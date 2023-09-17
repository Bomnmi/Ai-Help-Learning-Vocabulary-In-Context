package com.ailearningvocabulary.bomnmi.web.settings;

import com.ailearningvocabulary.bomnmi.web.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/1 16:44
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    //Cross region request handle
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //addMapping: handle the mapping address
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        String[] addPath = {"/v1/article/*", "/v1/setting/*"};
        String[] excludePath = {"/v1/user/*"};
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(addPath)
                .excludePathPatterns(excludePath);
    }
}
