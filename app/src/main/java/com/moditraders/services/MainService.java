package com.moditraders.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.HSN;
import com.moditraders.entities.Productdetail;
import com.moditraders.models.Consignee;
import com.moditraders.models.Customer;
import com.moditraders.models.HSNModel;
import com.moditraders.models.Product;
import com.moditraders.repositories.ConsigneeRepository;
import com.moditraders.repositories.CustomerRepository;
import com.moditraders.repositories.HSNRepository;
import com.moditraders.repositories.InvoiceNumberRepo;
import com.moditraders.repositories.ProductRepository;
import com.moditraders.types.ProductType;
import com.moditraders.utility.Util;

@Service("mainService")
public class MainService implements IMainService{
	
	private static final Logger LOGGER = Logger.getLogger(MainService.class);
	
	@Resource(name="productRepository")
	ProductRepository productRepository;
	
	@Resource(name="invoiceNumberRepo")
	InvoiceNumberRepo invoiceNumberRepo;
	
	@Resource(name="customerRepository")
	CustomerRepository customerRepository;
	
	@Resource(name="consigneeRepository")
	ConsigneeRepository consigneeRepo;
	
	@Resource(name="hsnRepo")
	HSNRepository hsnRepo;
	
	@Override
	public String[] getProductTypes() {
		LOGGER.info("Getting Product types");
		List<String> listOfTypes = new ArrayList<String>();
		for(ProductType type : ProductType.values()) {
			listOfTypes.add(type.getType());
		}
		return listOfTypes.toArray(new String[listOfTypes.size()]);
	}
	
	@Override
	public long saveProduct(Product product) {
		LOGGER.info("Saving Product");
		Productdetail productDetail = new Productdetail();
		productDetail.setProductType(product.getType().getType());
		productDetail.setAgencySecurityDeposit(product.getDepositAmount());
		productDetail.setAgencyStartDate(product.getAgencyStartDate());
		productDetail.setProductCompany(product.getCompany());
		HSN hsn = new HSN();
		hsn.setHsnCode(product.getHsnCode());
		productDetail.setProductHSN(hsn);
		Productdetail savedProduct = productRepository.save(productDetail);
		LOGGER.info("Saved Product with ID " + savedProduct.getProductId());
		return savedProduct.getProductId();
	}
	
	@Override
	public Collection<Product> getProducts() {
		LOGGER.info("Get all the products");
		Collection<Product> products = new ArrayList<Product>();
		Iterable<Productdetail> productsFromDB = productRepository.findAll();
		LOGGER.info("Found " + Iterables.size(productsFromDB) + " from Database.");
		for (Productdetail productdetail : productsFromDB) {
			Product product = new Product();
			product.setProductId(productdetail.getProductId());
			product.setType(ProductType.valueOf(productdetail.getProductType().toUpperCase()));
			product.setAgencyStartDate(productdetail.getAgencyStartDate());
			product.setCompany(productdetail.getProductCompany());
			product.setDepositAmount(productdetail.getAgencySecurityDeposit());
			product.setHsnCode(productdetail.getProductHSN().getHsnCode());
			products.add(product);
		}
		return products;
	}
	
	@Override
	public String generateInvoiceNumber() {
		LOGGER.info("Generating invoice number");
		String invoiceNumber = "";
		Integer sequenceNo = invoiceNumberRepo.getInvoiceSequenceNumber(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
		if(sequenceNo != null) {
			invoiceNumber = Util.getInvoiceNumber(sequenceNo);
		}
		else {
			invoiceNumber = Util.getInvoiceNumber(0);
		}
		LOGGER.info("Generated invoice number : " + invoiceNumber);
		return invoiceNumber;
	}

	@Override
	public Collection<Customer> getCustomers() throws ServiceException {
		LOGGER.info("Getting list of customers.");
		Iterable<CustomerDetail> cutomersFromDb = customerRepository.findAll();
		List<Customer> customers = new ArrayList<Customer>();
		LOGGER.info("Received " + Iterables.size(cutomersFromDb) + " cutomers from DB");
		for (CustomerDetail customerDetail : cutomersFromDb) {
			Customer customer = new Customer();
			customer.setName(customerDetail.getCdCustomerName());
			customer.setAddress(customerDetail.getCdCustomerAddress());
			customer.setState(customerDetail.getCdCustomerState());
			customer.setStateCode(customerDetail.getCdCustomerStateCode());
			customer.setGstin(customerDetail.getCdCustomerGSTIN());
			customer.setEmail(customerDetail.getCdCustomerEmail());
			customer.setMobileNo(customerDetail.getCdCustomerMobile());
			customer.setPhoneNo(customerDetail.getCdCustomerPhone());
			customers.add(customer);
		}
		return customers;
	}
	
	@Override
	public Collection<Consignee> getConsignees() throws ServiceException {
		LOGGER.info("Getting list of consignees.");
		Iterable<ConsigneeDetail> cutomersFromDb = consigneeRepo.findAll();
		List<Consignee> consignees = new ArrayList<Consignee>();
		LOGGER.info("Received " + Iterables.size(cutomersFromDb) + " cutomers from DB");
		for (ConsigneeDetail consigneeDetail : cutomersFromDb) {
			Consignee customer = new Consignee();
			customer.setName(consigneeDetail.getConsigneeName());
			customer.setAddress(consigneeDetail.getConsigneeAddress());
			customer.setState(consigneeDetail.getConsigneeAddress());
			customer.setStateCode(consigneeDetail.getConsigneeStateCode());
			customer.setGstin(consigneeDetail.getConsigneeGSTIN());
			customer.setEmail(consigneeDetail.getConsigneeEmail());
			customer.setMobileNo(consigneeDetail.getConsigneeMobile());
			customer.setPhoneNo(consigneeDetail.getConsigneePhone());
			consignees.add(customer);
		}
		return consignees;
	}
	
	@Override
	public Collection<HSNModel> getHSNCodes(final String keyword) {
		LOGGER.info("Getting HSN For keyword : " + keyword);
		List<HSNModel> hsnModels = new ArrayList<HSNModel>();
		List<HSN> hsns = hsnRepo.getHSNForCodes(keyword.toUpperCase());
		for(HSN hsn : hsns) {
			HSNModel hsnModel = new HSNModel();
			hsnModel.setHsnCode(hsn.getHsnCode());
			hsnModel.setHsnDesc(hsn.getHsnDesc());
			hsnModels.add(hsnModel);
		}
		LOGGER.info("Got " + hsnModels.size() + " HSN Codes for keyword: " + keyword);
		return hsnModels;
	}
	
	
}
