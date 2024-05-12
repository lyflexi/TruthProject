package com.hmall.api;

import com.hmall.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
public class HmApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmApiApplication.class, args);
    }

}
