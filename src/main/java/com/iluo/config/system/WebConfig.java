package com.iluo.config.system;

import com.iluo.access.AccessInterceptor;
import com.iluo.redis.RedisService;
import com.iluo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Yang Xing Luo on 2019/12/18.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AccessInterceptor accessInterceptor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(accessInterceptor);
    }
}
