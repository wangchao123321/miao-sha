package com.wangchao.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MiaoShaApplication
//		extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(MiaoShaApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(MiaoShaApplication.class);
//	}
}
