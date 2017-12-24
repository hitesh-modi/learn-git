package com.moditraders.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.HSN;
import com.moditraders.entities.InvoiceNumberDetail;
import com.moditraders.entities.Invoicedetail;
import com.moditraders.entities.Invoiceitemdetail;
import com.moditraders.entities.Invoiceitemtaxdetail;
import com.moditraders.entities.Productdetail;
import com.moditraders.entities.SacMaster;
import com.moditraders.models.Consignee;
import com.moditraders.models.Customer;
import com.moditraders.models.Invoice;
import com.moditraders.models.InvoiceItem;
import com.moditraders.models.Product;
import com.moditraders.models.TaxItem;
import com.moditraders.repositories.ConsigneeRepository;
import com.moditraders.repositories.CustomerRepository;
import com.moditraders.repositories.InvoiceNumberRepo;
import com.moditraders.repositories.InvoiceRepository;
import com.moditraders.repositories.ProductRepository;
import com.moditraders.types.TaxType;

@Service("invoiceService")
public class InvoiceService implements IInvoiceService{
	
	private static final Logger LOGGER = Logger.getLogger(InvoiceService.class);
	
	@Resource(name="customerRepository")
	private CustomerRepository customerRepository;
	
	@Resource(name="invoiceNumberRepo")
	private InvoiceNumberRepo invoiceNumberRepo;
	
	@Resource(name="consigneeRepository")
	private ConsigneeRepository consigneeRepo;
	
	@Resource(name="invoiceRepo")
	private InvoiceRepository invoiceRepo;
	
	@Resource(name="productRepository")
	private ProductRepository productRepository;

	@Override
	@Transactional
	public void createInvoice(final Invoice invoice) {
		LOGGER.info("Creating invoice " + invoice);
		CustomerDetail customerEntity = null;
		ConsigneeDetail consigneeEntity = null;
		customerEntity = createCustomerEntity(invoice.getCustomer());
		if(invoice.isNewCustomer()) {
			customerRepository.save(customerEntity);
		}
		
        if(invoice.getNewConsignee().equalsIgnoreCase("true")) {
		    consigneeEntity = createConsigneeEntity(invoice.getConsignee());
		    consigneeRepo.save(consigneeEntity);
        } else if(invoice.getNewConsignee().equalsIgnoreCase("SAME_AS_CUSTOMER")){
        	// Check if the Consignee with same name and contact details exists if yes then do not create a new consignee else create it.
        	Consignee consigneeModel = invoice.getConsignee();
        	ConsigneeDetail consigneeDetailFromDb = consigneeRepo.getConsigneeByNameAndMobileNumber(consigneeModel.getName(), consigneeModel.getMobileNo());
        	if(consigneeDetailFromDb == null) {
        		consigneeEntity = createConsigneeEntity(invoice.getConsignee());
        		consigneeRepo.save(consigneeEntity);
        	}
        	else
        		consigneeEntity = consigneeDetailFromDb;
        }

        Collection<InvoiceItem> invoiceItems = invoice.getInvoiceItemDetails();
        Invoicedetail invoiceEntity = createInvoiceEntity(invoice, consigneeEntity, customerEntity);
        List<Invoiceitemdetail> invoiceItemDetailsEntities = new ArrayList<Invoiceitemdetail>();
        for (InvoiceItem currInvoiceItem:
        	invoiceItems) {
        	Invoiceitemdetail invoiceItemDetail = createInvoiceItemEntity(currInvoiceItem, invoiceEntity);
        	invoiceItemDetailsEntities.add(invoiceItemDetail);
        }
        invoiceEntity.setInvoiceitemdetails(invoiceItemDetailsEntities);
        invoiceRepo.save(invoiceEntity);
        
        LOGGER.info("Invoice created successfully with id :" + invoiceEntity.getID_InvoiceId() + ", and Number: " + invoiceEntity.getID_InvoiceNumber());
        Integer seqNum = invoiceNumberRepo.getInvoiceSequenceNumber(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        InvoiceNumberDetail invoiceNumber = new InvoiceNumberDetail();
        if(seqNum == null) {
        	invoiceNumber.setInvoiceDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        	invoiceNumber.setSequenceNo(1);
        } else {
        	invoiceNumber.setInvoiceDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        	invoiceNumber.setSequenceNo(seqNum+1);
        }
        invoiceNumberRepo.save(invoiceNumber);
	}

	private Invoicedetail createInvoiceEntity(Invoice invoice, ConsigneeDetail consigneeDetail, CustomerDetail customerDetail) {
		Invoicedetail invoiceDetail = new Invoicedetail();
		invoiceDetail.setConsigneeDetail(consigneeDetail);
		invoiceDetail.setCustomerDetail(customerDetail);
		invoiceDetail.setID_InvoiceTotalAmount(invoice.getGrandTotal());
		invoiceDetail.setID_InvoicePaidAmount(invoice.getAmountReceived());
		BigDecimal remainingAmount = invoice.getGrandTotal().subtract(invoice.getAmountReceived());
		invoiceDetail.setID_InvoiceBalanceAmount(remainingAmount);
		invoiceDetail.setID_InvoiceDate(invoice.getInvoiceDate());
		invoiceDetail.setID_InvoiceNumber(invoice.getInvoiceNumber());
		invoiceDetail.setID_TaxAmount(invoice.getTotalTax());
		invoiceDetail.setID_GrandTotal(invoice.getNetTotal());
		return invoiceDetail;
	}
	
	
	
	private CustomerDetail createCustomerEntity(Customer customer) {
	    CustomerDetail customerDetail = new CustomerDetail();
	    customerDetail.setCdCustomerId(customer.getCustomerId());
	    customerDetail.setCdCustomerName(customer.getName());
	    customerDetail.setCdCustomerAddress(customer.getAddress());
	    customerDetail.setCdCustomerGSTIN(customer.getGstin());
	    customerDetail.setCdCustomerMobile(customer.getMobileNo());
	    customerDetail.setCdCustomerEmail(customer.getEmail());
	    customerDetail.setCdCustomerPhone(customer.getPhoneNo());
	    customerDetail.setCdCustomerState(customer.getState());
	    customerDetail.setCdCustomerStateCode(customer.getStateCode());
	    return customerDetail;
    }

    private ConsigneeDetail createConsigneeEntity(Consignee consignee) {
        ConsigneeDetail consigneeDetail = new ConsigneeDetail();
        consigneeDetail.setConsigneeName(consignee.getName());
        consigneeDetail.setConsigneeAddress(consignee.getAddress());
        consigneeDetail.setConsigneeGSTIN(consignee.getGstin());
        consigneeDetail.setConsigneeEmail(consignee.getEmail());
        consigneeDetail.setConsigneeMobile(consignee.getMobileNo());
        consigneeDetail.setConsigneePhone(consignee.getPhoneNo());
        consigneeDetail.setConsigneeState(consignee.getState());
        consigneeDetail.setConsigneeStateCode(consignee.getStateCode());
        return  consigneeDetail;
    }

    private Invoiceitemdetail createInvoiceItemEntity(InvoiceItem invoiceItem, Invoicedetail invoiceEntity) {
	    Invoiceitemdetail invoiceitemdetail = new Invoiceitemdetail();
	    Product product = invoiceItem.getProduct();
	    invoiceitemdetail.setProductdetail(createProductEntity(product));
	    invoiceitemdetail.setIID_ProductQuantity(invoiceItem.getQuantity());
	    invoiceitemdetail.setIID_ItemDiscount(invoiceItem.getDiscount());
	    invoiceitemdetail.setIID_ItemPrice(invoiceItem.getTotal());
	    invoiceitemdetail.setIidTaxableamount(invoiceItem.getTaxableValue());
	    Collection<TaxItem> additionalTaxes = getAllTaxesForInvoiceItem(invoiceItem);
	    List<Invoiceitemtaxdetail> taxEntities = createInvoiceTaxItemEntities(additionalTaxes, invoiceitemdetail);
	    invoiceitemdetail.setInvoiceitemtaxdetails(taxEntities);
	    invoiceitemdetail.setInvoicedetail(invoiceEntity);
        return  invoiceitemdetail;
    }
    
    private Collection<TaxItem> getAllTaxesForInvoiceItem(InvoiceItem invoiceItem) {
    	Collection<TaxItem> invoiceTaxes = invoiceItem.getAdditionalTaxes();
    	if((invoiceItem.getCgstRate() != null && invoiceItem.getCgstAmount() != null) && 
    			(!invoiceItem.getCgstRate().equals(BigDecimal.ZERO) && !invoiceItem.getCgstAmount().equals(BigDecimal.ZERO) )) {
    		TaxItem cgstTaxItem = new TaxItem();
    		cgstTaxItem.setAmount(invoiceItem.getCgstAmount());
    		cgstTaxItem.setRate(invoiceItem.getCgstRate());
    		cgstTaxItem.setType(TaxType.CGST);
    		invoiceTaxes.add(cgstTaxItem);
    	}
    	
    	if((invoiceItem.getSgstRate() != null && invoiceItem.getSgstAmount() != null) && 
    			(!invoiceItem.getSgstRate().equals(BigDecimal.ZERO) && !invoiceItem.getSgstAmount().equals(BigDecimal.ZERO) )) {
    		TaxItem sgstTaxItem = new TaxItem();
    		sgstTaxItem.setAmount(invoiceItem.getSgstAmount());
    		sgstTaxItem.setRate(invoiceItem.getSgstRate());
    		sgstTaxItem.setType(TaxType.SGST);
    		invoiceTaxes.add(sgstTaxItem);
    	}
    	
    	if((invoiceItem.getIgstRate() != null && invoiceItem.getIgstAmount() != null) && 
    			(!invoiceItem.getIgstRate().equals(BigDecimal.ZERO) && !invoiceItem.getIgstAmount().equals(BigDecimal.ZERO) )) {
    		TaxItem igstTaxItem = new TaxItem();
    		igstTaxItem.setAmount(invoiceItem.getIgstAmount());
    		igstTaxItem.setRate(invoiceItem.getIgstRate());
    		igstTaxItem.setType(TaxType.IGST);
    		invoiceTaxes.add(igstTaxItem);
    	}
    	return invoiceTaxes;
    }

    private List<Invoiceitemtaxdetail> createInvoiceTaxItemEntities(Collection<TaxItem> taxItems, Invoiceitemdetail invoiceItem) {
    	List<Invoiceitemtaxdetail> taxEntities = new ArrayList<>();
    	for (TaxItem taxItem : taxItems) {
			Invoiceitemtaxdetail taxEntity = new Invoiceitemtaxdetail();
			taxEntity.setITD_taxType(taxItem.getType().getTaxType());
			taxEntity.setITD_taxamount(taxItem.getAmount());
			taxEntity.setITD_taxrate(taxItem.getRate());
			taxEntity.setInvoiceitemdetail(invoiceItem);
			taxEntities.add(taxEntity);
		}
    	return taxEntities;
    }
    
    private Productdetail createProductEntity(Product product) {
		Productdetail productDetail = new Productdetail();
		productDetail.setProductId(product.getProductId());
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
		return productDetail;
	}

}
