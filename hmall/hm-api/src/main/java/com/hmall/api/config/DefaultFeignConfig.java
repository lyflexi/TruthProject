package com.hmall.api.config;

/**
 * @Author: ly
 * @Date: 2024/1/25 20:33
 */

import feign.Logger;
import org.springframework.context.annotation.Bean;

/*-
Feign默认的日志级别就是NONE，所以默认我们看不到请求日志。
- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。
*/
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }
}