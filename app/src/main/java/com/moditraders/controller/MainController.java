package com.moditraders.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moditraders.exceptions.ServiceExcpetion;
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
	
	@ResponseBody
	@RequestMapping(value="/createProduct", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String createProduct(@Valid @RequestBody String productJson) {
		LOGGER.info("Request received for Product Creation with data " + productJson);
		try {
			Product product = new ObjectMapper().readValue(productJson, Product.class);
			LOGGER.info(product);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
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
	
}
