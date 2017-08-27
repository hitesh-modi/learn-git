package com.moditraders.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages={"com.moditraders"})
@EntityScan(basePackages={"com.moditraders.entities"})
@EnableJpaRepositories(basePackages={"com.moditraders.repositories"})
@EnableWebMvc
@Import(ShiroConfig.class)
public class MainApp {
	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}
}
