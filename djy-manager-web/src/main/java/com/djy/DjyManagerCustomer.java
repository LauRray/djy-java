package com.djy;



import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableRabbit
@EnableDubbo
@PropertySource(value = {"classpath:rabbitmq.properties","classpath:base.properties"})
@ImportResource(locations={"classpath:bean.xml"})
public class DjyManagerCustomer {

    public static void main(String [] args){
        SpringApplication.run(DjyManagerCustomer.class,args);
    }
}
