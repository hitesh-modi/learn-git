package com.moditraders.controller;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.moditraders.exceptions.ServiceExcpetion;
import com.moditraders.models.Customer;
import com.moditraders.models.Product;
import com.moditraders.services.IMainService;

@Controller
public class MainController {
	
	private static Logger LOGGER = Logger.getLogger(MainController.class);
	
	@Resource(name="mainService")
	private IMainService mainService;
	
	@RequestMapping("/greeting")
	public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	
	@RequestMapping(value="/createProduct", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String createProduct(@Valid @RequestBody String productJson) throws JsonProcessingException {
		LOGGER.info("Request received for Product Creation with data " + productJson);
		Long id = -1L;
		try {
			Product product = new ObjectMapper().readValue(productJson, Product.class);
			id = mainService.saveProduct(product);
			LOGGER.info(product);
		} catch (JsonParseException e) {
			LOGGER.info("JsonParseException during service call", e);
		} catch (JsonMappingException e) {
			LOGGER.info("JsonMappingException during service call", e);
		} catch (IOException e) {
			LOGGER.info("IOException during service call", e);
		} catch (ServiceExcpetion e) {
			LOGGER.info("ServiceExcpetion during service call", e);
		}
		return convertToJson(id);
	} 
	
	@ResponseBody
	@RequestMapping(value="/getProductTypes", method=RequestMethod.GET)
	public String[] getProductTypes() {
		LOGGER.info("Getting product types from service");
		String[] productTypes = null;
		try {
			productTypes = mainService.getProductTypes();
		} catch (ServiceExcpetion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productTypes;
	}
	
	@ResponseBody
	@RequestMapping(value="/getProducts", method=RequestMethod.GET)
	public Collection<Product> getProducts() {
		LOGGER.info("Getting product types from service");
		Collection<Product> products = null;
			try {
				products = mainService.getProducts();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return products;
	}
	
	@ResponseBody
	@RequestMapping(value="/getInvoiceNumber", method=RequestMethod.GET)
	public String getInvoiceNumber() throws ServiceException, JsonProcessingException {
		LOGGER.info("Request received for generating the invoice number");
		return convertToJson(mainService.generateInvoiceNumber());
	}
	
	@ResponseBody
	@RequestMapping(value="/getCustomers", method=RequestMethod.GET)
	public Collection<Customer> getCustomers() {
		LOGGER.info("Getting product types from service");
		Collection<Customer> customers = null;
			try {
				customers = mainService.getCustomers();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return customers;
	}
	
	private String convertToJson(Object object) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(object);
		return json;
	}
	
}
