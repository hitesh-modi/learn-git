package com.moditraders.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
        .addResourceHandler("/services/**")
        .addResourceLocations("classpath:static/js/", "classpath:static/css/", "classpath:static/views/", "classpath:static/images/"); 
		
		registry
        .addResourceHandler("/**")
        .addResourceLocations("classpath:static/js/", "classpath:static/css/",  "classpath:static/images/"); 
	}
}
