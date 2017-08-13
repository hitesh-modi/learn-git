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
import com.moditraders.models.Consignee;
import com.moditraders.models.Customer;
import com.moditraders.models.HSNChapterModel;
import com.moditraders.models.HSNModel;
import com.moditraders.models.HSNSectionModel;
import com.moditraders.models.Product;
import com.moditraders.models.SacGroupModel;
import com.moditraders.models.SacHeadingModel;
import com.moditraders.models.SacModel;
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
	
	@ResponseBody
	@RequestMapping(value="/getConsignees", method=RequestMethod.GET)
	public Collection<Consignee> getConsignees() {
		LOGGER.info("Getting product types from service");
		Collection<Consignee> consignees = null;
			try {
				consignees = mainService.getConsignees();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return consignees;
	}

	@ResponseBody
	@RequestMapping(value="/getSacHeadings", method=RequestMethod.GET)
	public Collection<SacHeadingModel> getSacHeadings() {
		LOGGER.info("Request received for getting Headings for Service accounting codes");
		Collection<SacHeadingModel> sacHeadings = null;
			try {
				sacHeadings = mainService.getHeadingsForAllAccountingCodes();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sacHeadings;
	}
	
	@ResponseBody
	@RequestMapping(value="/getGroupsForHeading", method=RequestMethod.GET)
	public Collection<SacGroupModel> getSacGroup(@RequestParam String headingId) {
		LOGGER.info("Request received for getting Groups for Heading : " + headingId);
		Collection<SacGroupModel> sacHeadings = null;
			try {
				sacHeadings = mainService.getGroupsForHeading(headingId);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sacHeadings;
	}
	
	@ResponseBody
	@RequestMapping(value="/getSacsFromGroupId", method=RequestMethod.GET)
	public Collection<SacModel> getSacs(@RequestParam String groupId) {
		LOGGER.info("Request received for getting Sacs for Group Id : " + groupId);
		Collection<SacModel> sacs = null;
			try {
				sacs = mainService.getSacs(groupId);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sacs;
	}
	
	@ResponseBody
	@RequestMapping(value="/getHSNSections", method=RequestMethod.GET)
	public Collection<HSNSectionModel> getAllHSNSections() {
		LOGGER.info("Request received for getting HSN-Sections");
		Collection<HSNSectionModel> hsnSections = null;
			try {
				hsnSections = mainService.getHSNSections();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hsnSections;
	}
	
	@ResponseBody
	@RequestMapping(value="/getHsnChapter", method=RequestMethod.GET)
	public Collection<HSNChapterModel> getHsnChapter(@RequestParam String sectionId) {
		LOGGER.info("Request received for getting HSN-Chapters for section id " + sectionId);
		Collection<HSNChapterModel> hsnChapters = null;
			try {
				hsnChapters = mainService.getHSNChapters(sectionId);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hsnChapters;
	}
	
	@ResponseBody
	@RequestMapping(value="/getHsn", method=RequestMethod.GET)
	public Collection<HSNModel> getHsns(@RequestParam String chapterId) {
		LOGGER.info("Request received for getting HSN-Chapters for section id " + chapterId);
		Collection<HSNModel> hsns = null;
			try {
				hsns = mainService.getHSNs(chapterId);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hsns;
	}
	
	
	private String convertToJson(Object object) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(object);
		return json;
	}
	
}
