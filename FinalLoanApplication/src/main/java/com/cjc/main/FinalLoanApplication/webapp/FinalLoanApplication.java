package com.cjc.main.FinalLoanApplication.webapp;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinalLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalLoanApplication.class, args);
	
	}
	@Bean
	public Random getRandom()
	{
		return new Random(777);
	}

}
