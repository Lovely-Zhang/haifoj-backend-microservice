package com.haif.haifojuserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.haif.haifojuserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.haif")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.haif.haifojserviceclient.service"})
public class HaifojUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaifojUserServiceApplication.class, args);
	}

}
