package com.moditraders.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.HSN;
import com.moditraders.entities.Invoicedetail;
import com.moditraders.entities.Invoiceitemdetail;
import com.moditraders.entities.Invoiceitemtaxdetail;
import com.moditraders.entities.InvoiceitemtaxdetailPK;
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
import com.moditraders.types.TaxType;

@Service("invoiceService")
public class InvoiceService implements IInvoiceService{
	
	private static final Logger LOGGER = Logger.getLogger(InvoiceService.class);
	
	@Resource(name="customerRepository")
	private CustomerRepository customerRepository;
	
	@Resource(name="consigneeRepository")
	private ConsigneeRepository consigneeRepo;

	@Override
	public long createInvoice(final Invoice invoice) {
		LOGGER.info("Creating invoice " + invoice);
		if(invoice.isNewCustomer()) {
		    // Save customer

        }
        if(invoice.getNewConsignee().equalsIgnoreCase("true")) {
		    // Save Consignee
        } else if(invoice.getNewConsignee().equalsIgnoreCase("SAME_AS_CUSTOMER")){
        	// Check if the Consignee with same name and contact details exists if yes then do not create a new consignee else create it.
        }

        Collection<InvoiceItem> invoiceItems = invoice.getInvoiceItemDetails();
        for (InvoiceItem currInvoiceItem:
             invoiceItems) {
            // Save each invoice item
        }
        return 0;
	}

	private CustomerDetail createCustomerEntity(Customer customer) {
	    CustomerDetail customerDetail = new CustomerDetail();
	    customerDetail.setCdCustomerName(customer.getName());
	    customerDetail.setCdCustomerAddress(customer.getAddress());
	    customerDetail.setCdCustomerGSTIN(customer.getGstin());
	    customerDetail.setCdCustomerMobile(customer.getMobileNo());
	    customerDetail.setCdCustomerEmail(customer.getEmail());
	    customerDetail.setCdCustomerPhone(customer.getPhoneNo());
	    customerDetail.setCdCustomerState(customer.getState());
	    customerDetail.setCdCustomerStateCode(customer.getStateCode());
	    customerRepository.save(customerDetail);
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
        consigneeRepo.save(consigneeDetail);
        return  consigneeDetail;
    }

    private Invoiceitemdetail createInvoiceItemEntity(InvoiceItem invoiceItem) {
	    Invoiceitemdetail invoiceitemdetail = new Invoiceitemdetail();
	    Product product = invoiceItem.getProduct();
	    invoiceitemdetail.setProductdetail(createProductEntity(product));
	    String productType = product.isGood() ? "Good" : "Service";
	    invoiceitemdetail.setIID_ItemType(productType);
	    invoiceitemdetail.setIID_ProductQuantity(invoiceItem.getQuantity());
	    invoiceitemdetail.setIID_ItemDiscount(invoiceItem.getDiscount());
	    invoiceitemdetail.setIID_ItemPrice(invoiceItem.getTotal());
	    invoiceitemdetail.setIidTaxableamount(invoiceItem.getTaxableValue());
	    Collection<TaxItem> additionalTaxes = getAllTaxesForInvoiceItem(invoiceItem);
	    List<Invoiceitemtaxdetail> taxEntities = createInvoiceTaxItemEntities(additionalTaxes, invoiceitemdetail);
	    invoiceitemdetail.setInvoiceitemtaxdetails(taxEntities);
	    
	    
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
			InvoiceitemtaxdetailPK primaryKey = new InvoiceitemtaxdetailPK();
			primaryKey.setITD_taxtype(taxItem.getType().getTaxType());
			taxEntity.setId(primaryKey);
			taxEntity.setInvoiceitemdetail(invoiceItem);
			taxEntity.setITD_taxamount(taxItem.getAmount());
			taxEntity.setITD_taxrate(taxItem.getRate());
			taxEntities.add(taxEntity);
		}
    	return taxEntities;
    }
    
    private Productdetail createProductEntity(Product product) {
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
		return productDetail;
	}

    private Invoicedetail createInvoiceItem(Invoice invoice) {
        Invoicedetail invoicedetail = new Invoicedetail();
        // TODO: Convert invoice entity.

        return invoicedetail;
    }

}
