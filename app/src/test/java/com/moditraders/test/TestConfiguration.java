package com.moditraders.test;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages={"com.moditraders"})
@EntityScan(basePackages={"com.moditraders.entities"})
@EnableJpaRepositories(basePackages={"com.moditraders.repositories"})
public class TestConfiguration {

}
