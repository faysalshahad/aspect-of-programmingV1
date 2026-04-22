package com.aspectofprogramming.cachingsecurityjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
public class CachingsecurityjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingsecurityjwtApplication.class, args);
		System.out.println("\n\n\nPort 8080.\n\n\n Application is running successfully!");
	}

}
