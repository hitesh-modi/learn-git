package com.moditraders.services;

import java.util.Collection;

import org.hibernate.service.spi.ServiceException;

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

public interface IMainService {

	public String[] getProductTypes() throws ServiceExcpetion;

	public long saveProduct(Product product) throws ServiceExcpetion;

	public Collection<Product> getProducts() throws ServiceException;
	
	public String generateInvoiceNumber()throws ServiceException;
	
	public Collection<Customer> getCustomers() throws ServiceException;

	public Collection<Consignee> getConsignees() throws ServiceException;

	public Collection<SacHeadingModel> getHeadingsForAllAccountingCodes() throws ServiceException;

	public Collection<SacGroupModel> getGroupsForHeading(String headingId) throws ServiceException;

	public Collection<SacModel> getSacs(String groupId) throws ServiceException;

	public Collection<HSNSectionModel> getHSNSections() throws ServiceException;

	public Collection<HSNChapterModel> getHSNChapters(String sectionId) throws ServiceException;

	public Collection<HSNModel> getHSNs(String chapterId) throws ServiceException;
	
}
