package com.srmasset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ThCepDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThCepDetailsApplication.class, args);
	}

}
