package com.nouah.revlo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Slf4j
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableCaching
public class RevloApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevloApplication.class, args);
		log.info("::;::Revlo Launched::;::");

	}
	}
