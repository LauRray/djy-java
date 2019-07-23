package com.djy;


import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo //开启dubbo注解

@EnableCaching
@MapperScan("com.djy.sql.mapper")
@ImportResource(locations={"classpath:bean.xml"})
public class DjyManagerProviderApplication {

    public static void main(String [] args){
        SpringApplication.run(DjyManagerProviderApplication.class,args);
    }
}
