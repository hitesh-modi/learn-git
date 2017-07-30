package com.moditraders.services;

import java.util.Collection;

import org.hibernate.service.spi.ServiceException;

import com.moditraders.exceptions.ServiceExcpetion;
import com.moditraders.models.Consignee;
import com.moditraders.models.Customer;
import com.moditraders.models.HSNModel;
import com.moditraders.models.Product;

public interface IMainService {

	public String[] getProductTypes() throws ServiceExcpetion;

	public long saveProduct(Product product) throws ServiceExcpetion;

	public Collection<Product> getProducts() throws ServiceException;
	
	public String generateInvoiceNumber()throws ServiceException;
	
	public Collection<Customer> getCustomers() throws ServiceException;

	public Collection<Consignee> getConsignees() throws ServiceException;

	public Collection<HSNModel> getHSNCodes(String keyword) throws ServiceException;
	
}
