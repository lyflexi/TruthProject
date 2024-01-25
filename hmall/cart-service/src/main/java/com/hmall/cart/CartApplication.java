package com.hmall.cart;

import com.hmall.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;



//feign客户端被抽取成了公共模块hm-api，
//尽管当前微服务cart-service引入了公共模块hm-api，但是CartApplication的默认扫描规则是当前包
//所以这里要手动开启feign客户端扫描@EnableFeignClients(basePackages = "com.hmall.api.client")
@EnableFeignClients(basePackages = "com.hmall.api.client",defaultConfiguration = DefaultFeignConfig.class)//feign日志全局生效
@MapperScan("com.hmall.cart.mapper")
@SpringBootApplication
public class CartApplication {
	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}
}
