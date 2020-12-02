package com.gdula.funTradeApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class }) //security disabled
@EnableSwagger2
public class FunTradeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunTradeAppApplication.class, args);
	}

}
