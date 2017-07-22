package com.moditraders.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.moditraders.entities.Productdetail;
import com.moditraders.models.Product;
import com.moditraders.repositories.ProductRepository;
import com.moditraders.types.ProductType;

@Service("mainService")
public class MainService implements IMainService{
	
	private static final Logger LOGGER = Logger.getLogger(MainService.class);
	
	@Resource(name="productRepository")
	ProductRepository productRepository;
	
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
			products.add(product);
		}
		return products;
	}
	
}
