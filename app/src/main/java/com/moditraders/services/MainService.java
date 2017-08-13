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
import com.moditraders.entities.ChapterSectionMap;
import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.HSN;
import com.moditraders.entities.HsnChapterMap;
import com.moditraders.entities.Productdetail;
import com.moditraders.entities.SacAccountingcodeGroupMap;
import com.moditraders.entities.SacGroupHeadinMap;
import com.moditraders.entities.SacHeadingMaster;
import com.moditraders.entities.SectionMaster;
import com.moditraders.models.Consignee;
import com.moditraders.models.Customer;
import com.moditraders.models.HSNChapterModel;
import com.moditraders.models.HSNModel;
import com.moditraders.models.HSNSectionModel;
import com.moditraders.models.Product;
import com.moditraders.models.SacGroupModel;
import com.moditraders.models.SacHeadingModel;
import com.moditraders.models.SacModel;
import com.moditraders.repositories.ConsigneeRepository;
import com.moditraders.repositories.CustomerRepository;
import com.moditraders.repositories.HSNChapterRepository;
import com.moditraders.repositories.HSNRepository;
import com.moditraders.repositories.HSNSectionRepository;
import com.moditraders.repositories.InvoiceNumberRepo;
import com.moditraders.repositories.ProductRepository;
import com.moditraders.repositories.SacGroupRepository;
import com.moditraders.repositories.SacHeadingRepository;
import com.moditraders.repositories.SacRepository;
import com.moditraders.types.ProductType;
import com.moditraders.utility.Util;

@Service("mainService")
public class MainService implements IMainService{
	
	private static final Logger LOGGER = Logger.getLogger(MainService.class);
	
	@Resource(name="productRepository")
	private ProductRepository productRepository;
	
	@Resource(name="invoiceNumberRepo")
	private InvoiceNumberRepo invoiceNumberRepo;
	
	@Resource(name="customerRepository")
	private CustomerRepository customerRepository;
	
	@Resource(name="consigneeRepository")
	private ConsigneeRepository consigneeRepo;
	
	@Resource(name="sacHeadingRepo")
	private SacHeadingRepository sacHeadingRepo;
	
	@Resource(name="sacGroupRepo")
	private SacGroupRepository sacGroupRepo;
	
	@Resource(name="sacRepo")
	private SacRepository sacRepo;
	
	@Resource(name="hsnSectionRepo")
	private HSNSectionRepository hsSectionRepo;
	
	@Resource(name="hsnChapterRepo")
	private HSNChapterRepository hsnChapterRepo;
	
	@Resource(name="hsnRepo")
	private HSNRepository hsnRepo;
	
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
	public Collection<SacHeadingModel> getHeadingsForAllAccountingCodes() {
		LOGGER.info("Getting All Headings for SACs");
		Collection<SacHeadingModel> sacHeadings = new ArrayList<SacHeadingModel>();
		Iterable<SacHeadingMaster> allSacHeadings = sacHeadingRepo.findAll();
		for (SacHeadingMaster sacHeadingMaster : allSacHeadings) {
			SacHeadingModel model = new SacHeadingModel();
			model.setHeadingCode(sacHeadingMaster.getHeadingId());
			model.setHeadingDesc(sacHeadingMaster.getHeadingDesc());
			sacHeadings.add(model);
		}
		LOGGER.info("Got " + sacHeadings.size() + " Headings from database");
		return sacHeadings;
	}
	
	@Override
	public Collection<SacGroupModel> getGroupsForHeading(String headingId) {
		LOGGER.info("Getting SAC groups for Heading : " + headingId);
		Collection<SacGroupHeadinMap> groupHeadingMap = sacGroupRepo.getGroupsByHeading(headingId);
		Collection<SacGroupModel> groups = new ArrayList<SacGroupModel>();
		for(SacGroupHeadinMap sacGroupHeadinMap : groupHeadingMap) {
			SacGroupModel groupModel = new SacGroupModel();
			groupModel.setGroupCode(sacGroupHeadinMap.getSacGroupMaster().getGroupId());
			groupModel.setGroupDesc(sacGroupHeadinMap.getSacGroupMaster().getGroupDesc());
			groups.add(groupModel);
		}
		LOGGER.info("Received "+ groups.size() +" Groups from Database.");
		return groups;
	}
	
	@Override
	public Collection<SacModel> getSacs(String groupId) {
		LOGGER.info("Getting SAC for group id  : " + groupId);
		Collection<SacAccountingcodeGroupMap> accountingCodes = sacRepo.getSacByGroupId(groupId);
		Collection<SacModel> sacModels = new ArrayList<SacModel>();
		
		for(SacAccountingcodeGroupMap sacFromDB : accountingCodes) {
			SacModel accountingCodeModel = new SacModel();
			accountingCodeModel.setSacCode(sacFromDB.getSacMaster().getSacId());
			accountingCodeModel.setSacDesc(sacFromDB.getSacMaster().getSacDesc());
			sacModels.add(accountingCodeModel);
		}
		LOGGER.info("Received " + sacModels.size() + " from database");
		return sacModels;
	}
	
	@Override
	public Collection<HSNSectionModel> getHSNSections() {
		LOGGER.info("Getting list of all HSN Sections");
		Iterable<SectionMaster> sections = hsSectionRepo.findAll();
		Collection<HSNSectionModel> hsnAllSections = new ArrayList<HSNSectionModel>();
		for(SectionMaster secMaster : sections) {
			HSNSectionModel sectionModel = new HSNSectionModel();
			sectionModel.setSectionCode(secMaster.getSectionId());
			sectionModel.setSectionDesc(secMaster.getSectionDesc());
			hsnAllSections.add(sectionModel);
		}
		LOGGER.info("Retreived " + hsnAllSections.size() + " sections from the database.");
		return hsnAllSections;
	}
	
	@Override
	public Collection<HSNChapterModel> getHSNChapters(String sectionId) {
		LOGGER.info("Getting HSN Chapters for section id : " + sectionId);
		Collection<ChapterSectionMap> hsnChapters = hsnChapterRepo.getChaptersBySectionId(sectionId);
		Collection<HSNChapterModel> chapters = new ArrayList<HSNChapterModel>();
		for(ChapterSectionMap chapter : hsnChapters) {
			HSNChapterModel chapterModel = new HSNChapterModel();
			chapterModel.setChapterId(chapter.getChapterId());
			chapterModel.setChapterDesc(chapter.getHsnchapterMaster().getHsnchapterDesc());
			chapters.add(chapterModel);
		}
		LOGGER.info("Retreived " + chapters.size() + " chapters from database.");
		return chapters;
	}
	
	@Override
	public Collection<HSNModel> getHSNs(String chapterId) {
		LOGGER.info("Getting HSN Chapters for chapter id : " + chapterId);
		Collection<HsnChapterMap> hsns = hsnRepo.getHSNByChapter(chapterId);
		Collection<HSNModel> hsnModels = new ArrayList<HSNModel>();
		for(HsnChapterMap hsnEntity : hsns) {
			HSNModel hsnModel = new HSNModel();
			hsnModel.setHsnCode(hsnEntity.getHsncode());
			hsnModel.setHsnDesc(hsnEntity.getHsnmaster().getHsndesc());
			hsnModels.add(hsnModel);
		}
		LOGGER.info("Retreived "+ hsnModels.size() + " from database.");
		return hsnModels;
	}
	
}
