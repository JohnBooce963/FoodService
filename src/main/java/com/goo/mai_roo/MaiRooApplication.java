package com.goo.mai_roo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MaiRooApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaiRooApplication.class, args);
	}

	// No cookies are used, so "*" is safe; set CORS_ORIGINS to lock the API to the frontend.
	@Bean
	WebMvcConfigurer cors(@Value("${CORS_ORIGINS:*}") String origins) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(origins.split(",")).allowedMethods("GET", "POST");
			}
		};
	}

}
