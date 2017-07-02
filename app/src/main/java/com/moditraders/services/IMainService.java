package com.moditraders.services;

import com.moditraders.exceptions.ServiceExcpetion;
import com.moditraders.models.Product;

public interface IMainService {

	public String[] getProductTypes() throws ServiceExcpetion;

	public long saveProduct(Product product) throws ServiceExcpetion;
	
}
