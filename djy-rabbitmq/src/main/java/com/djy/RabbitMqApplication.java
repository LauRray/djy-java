package com.djy;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableRabbit
@MapperScan("com.djy.sql.mapper")
public class RabbitMqApplication {

    public static void main(String[] args){
        SpringApplication.run(RabbitMqApplication.class,args);
    }
}
