package com.moditraders.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import com.moditraders.entities.*;
import com.moditraders.models.*;
import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
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
		productDetail.setProductType(product.getType());
		productDetail.setAgencySecurityDeposit(product.getDepositAmount());
		productDetail.setAgencyStartDate(product.getAgencyStartDate());
		productDetail.setProductCompany(product.getCompany());
		productDetail.setProductTaxRate(product.getTaxRate());
		if(product.isGood()) {
			HSN hsn = new HSN();
			hsn.setHsnCode(product.getHsnCode());
			productDetail.setProductServiceOrGood("G");
			productDetail.setProductHSN(hsn);
		}
		else {
			SacMaster sac = new SacMaster();
			sac.setSacId(product.getHsnCode());
			productDetail.setProductSac(sac);
			productDetail.setProductServiceOrGood("S");
		}
		productDetail.setProductName(product.getName());
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
		String goodsOrService = "";
		for (Productdetail productdetail : productsFromDB) {
			Product product = new Product();
			product.setProductId(productdetail.getProductId());
			product.setType(productdetail.getProductType().toUpperCase());
			product.setAgencyStartDate(productdetail.getAgencyStartDate());
			product.setCompany(productdetail.getProductCompany());
			product.setDepositAmount(productdetail.getAgencySecurityDeposit());
			
			goodsOrService = productdetail.getProductServiceOrGood();
			
			if(goodsOrService.equalsIgnoreCase("G")) {
				product.setHsnCode(productdetail.getProductHSN().getHsnCode());
				product.setAccountingCodeDesc(productdetail.getProductHSN().getHsnDesc());
			}
			else if(goodsOrService.equalsIgnoreCase("S")) {
				product.setHsnCode(productdetail.getProductSac().getSacId());
				product.setAccountingCodeDesc(productdetail.getProductSac().getSacDesc());
			}
			product.setName(productdetail.getProductName());
			product.setTaxRate(productdetail.getProductTaxRate());
			product.setGood(goodsOrService.equalsIgnoreCase("G")?true:false);
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
			customer.setCustomerId(customerDetail.getCdCustomerId());
			customer.setName(customerDetail.getCdCustomerName());
			customer.setAddress(customerDetail.getCdCustomerAddress());
			customer.setState(customerDetail.getState().getStatename());
			customer.setStateCode(customerDetail.getState().getStatecode());
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
			Consignee consignee = new Consignee();
			consignee.setConsigneeId(consigneeDetail.getConsigneeId());
			consignee.setName(consigneeDetail.getConsigneeName());
			consignee.setAddress(consigneeDetail.getConsigneeAddress());
			consignee.setState(consigneeDetail.getState().getStatename());
			consignee.setStateCode(consigneeDetail.getState().getStatecode());
			consignee.setGstin(consigneeDetail.getConsigneeGSTIN());
			consignee.setEmail(consigneeDetail.getConsigneeEmail());
			consignee.setMobileNo(consigneeDetail.getConsigneeMobile());
			consignee.setPhoneNo(consigneeDetail.getConsigneePhone());
			consignees.add(consignee);
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
