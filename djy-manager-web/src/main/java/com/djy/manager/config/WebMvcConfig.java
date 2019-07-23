package com.djy.manager.config;

import com.djy.manager.service.UserInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    @Bean
    MvcInter localInterceptor() {
        return new MvcInter();
    }


    private String path="/security-path-djy";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration =
                registry.addInterceptor(localInterceptor());
        InterceptorRegistration interceptorRegistration1 =    interceptorRegistration.addPathPatterns(path+"/**");
        List<String> list=new ArrayList<>();
        list.add(path+"/user/login");
        interceptorRegistration1.excludePathPatterns(list);
    }
}
