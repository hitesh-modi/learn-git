package com.moditraders.services;

import java.util.Collection;

import com.moditraders.models.*;
import org.hibernate.service.spi.ServiceException;

import com.moditraders.exceptions.ServiceExcpetion;

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

    void createInvoice(Invoice invoice);
}
