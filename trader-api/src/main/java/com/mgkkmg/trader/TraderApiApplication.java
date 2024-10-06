package com.mgkkmg.trader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TraderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraderApiApplication.class, args);
	}

}
