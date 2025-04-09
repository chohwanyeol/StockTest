package com.stocktest.stocktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@EnableRetry
@SpringBootApplication
public class StocktestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocktestApplication.class, args);
	}

}
