package com.moditraders.services;

import java.util.Collection;

import org.hibernate.service.spi.ServiceException;

import com.moditraders.exceptions.ServiceExcpetion;
import com.moditraders.models.Product;

public interface IMainService {

	public String[] getProductTypes() throws ServiceExcpetion;

	public long saveProduct(Product product) throws ServiceExcpetion;

	public Collection<Product> getProducts() throws ServiceException;
	
}
