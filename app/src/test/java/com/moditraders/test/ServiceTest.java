package com.moditraders.test;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.moditraders.models.Product;
import com.moditraders.services.IMainService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { TestConfiguration.class })
@WebAppConfiguration
public class ServiceTest {

	@Resource
	private IMainService service;
	
	@Test
	public void testGetDetails() {
		Collection<Product> products = service.getProducts();
	}
	
}
