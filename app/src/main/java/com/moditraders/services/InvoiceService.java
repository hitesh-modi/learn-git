package com.moditraders.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.HSN;
import com.moditraders.entities.InvoiceNumberDetail;
import com.moditraders.entities.Invoicedetail;
import com.moditraders.entities.Invoiceitemdetail;
import com.moditraders.entities.Invoiceitemtaxdetail;
import com.moditraders.entities.Productdetail;
import com.moditraders.entities.SacMaster;
import com.moditraders.entities.State;
import com.moditraders.entities.User;
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
import com.moditraders.repositories.StateRepository;
import com.moditraders.repositories.UserRepository;
import com.moditraders.types.TaxType;
import com.moditraders.utility.Util;


@Service("invoiceService")
public class InvoiceService implements IInvoiceService{
	
	private static final Logger LOGGER = Logger.getLogger(InvoiceService.class);
	
	@Resource(name="customerRepository")
	private CustomerRepository customerRepository;
	
	@Resource(name="userRepo")
	private UserRepository userRepo;
	
	@Resource(name="invoiceNumberRepo")
	private InvoiceNumberRepo invoiceNumberRepo;
	
	@Resource(name="consigneeRepository")
	private ConsigneeRepository consigneeRepo;
	
	@Resource(name="invoiceRepo")
	private InvoiceRepository invoiceRepo;
	
	@Resource(name="productRepository")
	private ProductRepository productRepository;
	
	@Resource(name="stateRepo")
	private StateRepository stateRepo;
	
	@Value("${invoice.path.pdf}")
	private String invoiceGenerationPath;
	
	private static final String REGULAR_FONT="fonts/Montserrat-Regular.ttf";
	private static final String BOLD_FONT="fonts/Montserrat-Bold.ttf";

	@Override
	@Transactional
	public String createInvoice(final Invoice invoice) {
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
        } else {
        	consigneeEntity = consigneeRepo.findOne(invoice.getConsignee().getConsigneeId());
        }

        Collection<InvoiceItem> invoiceItems = invoice.getInvoiceItemDetails();
        Invoicedetail invoiceEntity = createInvoiceEntity(invoice, consigneeEntity, customerEntity);
        Set<Invoiceitemdetail> invoiceItemDetailsEntities = new HashSet<>();
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
        return invoiceEntity.getID_InvoiceId();
	}

	private Invoicedetail createInvoiceEntity(Invoice invoice, ConsigneeDetail consigneeDetail, CustomerDetail customerDetail) {
		Invoicedetail invoiceDetail = new Invoicedetail();
		invoiceDetail.setConsigneeDetail(consigneeDetail);
		invoiceDetail.setCustomerDetail(customerDetail);
		invoiceDetail.setID_InvoiceTotalAmount(invoice.getGrandTotal());
		invoiceDetail.setID_InvoicePaidAmount(invoice.getAmountReceived());
		BigDecimal remainingAmount = invoice.getNetTotal().subtract(invoice.getAmountReceived());
		invoiceDetail.setID_InvoiceBalanceAmount(remainingAmount);
		invoiceDetail.setID_InvoiceDate(invoice.getInvoiceDate());
		invoiceDetail.setID_InvoiceNumber(invoice.getInvoiceNumber());
		invoiceDetail.setID_InvoiceDueDate(invoice.getInvoiceDueDate());
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
	    State state = stateRepo.findOne(customer.getStateCode());
	    customerDetail.setState(state);
	   
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
        State state = stateRepo.findOne(consignee.getStateCode());
        consigneeDetail.setState(state);
        return  consigneeDetail;
    }

    private Invoiceitemdetail createInvoiceItemEntity(InvoiceItem invoiceItem, Invoicedetail invoiceEntity) {
	    Invoiceitemdetail invoiceitemdetail = new Invoiceitemdetail();
	    Product product = invoiceItem.getProduct();
	    invoiceitemdetail.setProductdetail(createProductEntity(product));
	    invoiceitemdetail.setIID_ProductQuantity(invoiceItem.getQuantity());
	    invoiceitemdetail.setIID_ItemDiscount(invoiceItem.getDiscount());
	    invoiceitemdetail.setIID_ItemUnit(invoiceItem.getUnit());
	    invoiceitemdetail.setIID_ItemTotalAmount(invoiceItem.getTotal());
	    invoiceitemdetail.setIID_ItemPrice(invoiceItem.getRate());
	    invoiceitemdetail.setIidTaxableamount(invoiceItem.getTaxableValue());
	    Collection<TaxItem> additionalTaxes = getAllTaxesForInvoiceItem(invoiceItem);
	    Set<Invoiceitemtaxdetail> taxEntities = createInvoiceTaxItemEntities(additionalTaxes, invoiceitemdetail);
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

    private Set<Invoiceitemtaxdetail> createInvoiceTaxItemEntities(Collection<TaxItem> taxItems, Invoiceitemdetail invoiceItem) {
    	Set<Invoiceitemtaxdetail> taxEntities = new HashSet<>();
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
    
    @Override
    public File createInvoicePDF(final String invoiceId, final String userId) throws DocumentException, MalformedURLException, IOException {
    	User user = userRepo.getUser(userId);
    	String logoPath = user.getLogopath();
    	
    	Invoicedetail invoiceModel = invoiceRepo.findOne(invoiceId);
    	
    	Document invoiceDoc = new Document();
    	File file = new File(invoiceGenerationPath+File.separator+invoiceModel.getID_InvoiceNumber().replaceAll("/", "")+".pdf");
    	file.createNewFile();
    	PdfWriter.getInstance(invoiceDoc, new FileOutputStream(file));
    	
    	invoiceDoc.open();
    	// Add Logo to the invoice
    	Image image = Image.getInstance(logoPath);
    	invoiceDoc.add(image);
    	
    	// Add header to the invoice
    	PdfPTable headerTable = new PdfPTable(3);
    	headerTable.setWidthPercentage(100);
    	PdfPCell taxInvoiceCell = new PdfPCell(new Phrase("TAX INVOICE", getBoldFont()));
    	taxInvoiceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    	headerTable.addCell(taxInvoiceCell);
    	
    	PdfPCell invoiceNumberCell = new PdfPCell(new Phrase(invoiceModel.getID_InvoiceNumber(), getBoldFont()));
    	invoiceNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	headerTable.addCell(invoiceNumberCell);
    	
    	PdfPCell invoiceDateCell = new PdfPCell(new Phrase(Util.getDateInPrintableFormat(invoiceModel.getID_InvoiceDate()), getBoldFont()));
    	invoiceDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    	headerTable.addCell(invoiceDateCell);
    	
    	invoiceDoc.add(headerTable);
    	
    	// Add Traders Details
    	PdfPTable traderTable = new PdfPTable(2);
    	traderTable.setWidthPercentage(100);
    	
    	PdfPCell sellerDetailsCell = new PdfPCell(new Phrase("Seller's Details", getBoldFont()));
    	sellerDetailsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	sellerDetailsCell.setColspan(2);
    	
    	traderTable.addCell(sellerDetailsCell);
    	
    	PdfPCell traderNameCell = new PdfPCell();
    	
    	StringBuilder nameBuilder = new StringBuilder();
    	String traderNameStr = user.getBusinessname();
    	nameBuilder.append(traderNameStr);
    	nameBuilder.append("\n");
    	String gstin = "GSTIN: " + user.getGstin();
    	nameBuilder.append(gstin);
    	traderNameCell.setPhrase(new Phrase(nameBuilder.toString(), getRegularFont()));
    	
    	traderTable.addCell(traderNameCell);
    	
    	StringBuilder addressBuilder = new StringBuilder();
    	String address = user.getAddress();
    	addressBuilder.append(address);
    	String stateName = user.getState().getStatename();
    	String stateCode = user.getState().getStatecode();
    	
    	addressBuilder.append("\n");
    	addressBuilder.append(stateName);
    	addressBuilder.append(" ("+stateCode+")");
    	
    	PdfPCell traderAddressCell = new PdfPCell(new Phrase(addressBuilder.toString(), getRegularFont()));
    	traderTable.addCell(traderAddressCell);
    	
    	invoiceDoc.add(traderTable);
    	
    	
    	// Add Customer Detail
    	PdfPTable customerDetailHeaderTable = new PdfPTable(2);
    	customerDetailHeaderTable.setWidthPercentage(100);
    	String billingDetails = "Billing Details";
    	PdfPCell headerBillingCell = new PdfPCell(new Phrase(billingDetails, getBoldFont()));
    	headerBillingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	customerDetailHeaderTable.addCell(headerBillingCell);
    	
    	String shippingDetails = "Shipping Details";
    	PdfPCell headerShippingCell = new PdfPCell(new Phrase(shippingDetails, getBoldFont()));
    	headerShippingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	customerDetailHeaderTable.addCell(headerShippingCell);
    	
    	invoiceDoc.add(customerDetailHeaderTable);
    	
    	// Add Customer Details to the invoice
    	
    	PdfPTable customerDetailsTable = new PdfPTable(2);
    	customerDetailsTable.setWidthPercentage(100);
    	StringBuilder customerBuilder = new StringBuilder();
    	CustomerDetail customer = invoiceModel.getCustomerDetail();
    	customerBuilder.append(customer.getCdCustomerName());
    	customerBuilder.append("\n");
    	customerBuilder.append(customer.getCdCustomerAddress());
    	customerBuilder.append("\n");
    	customerBuilder.append("State: " + customer.getState().getStatename() + "("+customer.getState().getStatecode()+")");
    	customerBuilder.append("\n");
    	customerBuilder.append("GSTIN: " + customer.getCdCustomerGSTIN());
    	PdfPCell customerDetailCell = new PdfPCell(new Phrase(customerBuilder.toString(), getRegularFont()));
    	customerDetailsTable.addCell(customerDetailCell);
    	
    	StringBuilder consgineeBuilder = new StringBuilder();
    	ConsigneeDetail consignee = invoiceModel.getConsigneeDetail();
    	consgineeBuilder.append(consignee.getConsigneeName());
    	consgineeBuilder.append("\n");
    	consgineeBuilder.append(consignee.getConsigneeAddress());
    	consgineeBuilder.append("\n");
    	consgineeBuilder.append("State: " + consignee.getState().getStatename() + "("+consignee.getState().getStatecode()+")");
    	consgineeBuilder.append("\n");
    	consgineeBuilder.append("GSTIN: " + consignee.getConsigneeGSTIN());
    	PdfPCell consigneeDetailsCell = new PdfPCell(new Phrase(consgineeBuilder.toString(), getRegularFont()));
    	customerDetailsTable.addCell(consigneeDetailsCell);
    	
    	invoiceDoc.add(customerDetailsTable);
    	
    	// Create header for invoice items:
    	PdfPTable invoiceItemTables = new PdfPTable(15);
    	invoiceItemTables.setWidthPercentage(100);
    	
    	String snLabel = "S.No.";
    	PdfPCell serialNoCell = new PdfPCell(new Phrase(snLabel, getBoldFont()));
    	serialNoCell.setRowspan(2);
    	
    	String productLabel = "Product";
    	PdfPCell productCell = new PdfPCell(new Phrase(productLabel, getBoldFont()));
    	productCell.setRowspan(2);
    	
    	String accountCodeLabel = "HSN/SAC";
    	PdfPCell accountCodeCell = new PdfPCell(new Phrase(accountCodeLabel, getBoldFont()));
    	accountCodeCell.setRowspan(2);
    	
    	String rateLabel = "Rate";
    	PdfPCell rateCell = new PdfPCell(new Phrase(rateLabel, getBoldFont()));
    	rateCell.setRowspan(2);
    	
    	String unitsLabel = "Units";
    	PdfPCell unitsCell = new PdfPCell(new Phrase(unitsLabel, getBoldFont()));
    	unitsCell.setRowspan(2);
    	
    	String quanitityLabel = "Quant.";
    	PdfPCell quatityCell = new PdfPCell(new Phrase(quanitityLabel, getBoldFont()));
    	quatityCell.setRowspan(2);
    	
    	String totalLabel = "Total";
    	PdfPCell totalCell = new PdfPCell(new Phrase(totalLabel, getBoldFont()));
    	totalCell.setRowspan(2);
    	
    	String discLabel = "Disc.";
    	PdfPCell discCell = new PdfPCell(new Phrase(discLabel, getBoldFont()));
    	discCell.setRowspan(2);
    	
    	String taxableValueLabel = "Taxable Value";
    	PdfPCell taxableValueCell = new PdfPCell(new Phrase(taxableValueLabel, getBoldFont()));
    	taxableValueCell.setRowspan(2);
    	
    	String cgstParentLabel = "CGST";
    	PdfPCell cgstParentCell = new PdfPCell(new Phrase(cgstParentLabel, getBoldFont()));
    	cgstParentCell.setColspan(2);
    	
    	String sgstParentLabel = "SGST";
    	PdfPCell sgstParentCell = new PdfPCell(new Phrase(sgstParentLabel, getBoldFont()));
    	sgstParentCell.setColspan(2);
    	
    	String igstParentLabel = "IGST";
    	PdfPCell igstParentCell = new PdfPCell(new Phrase(igstParentLabel, getBoldFont()));
    	igstParentCell.setColspan(2);
    	
    	invoiceItemTables.addCell(serialNoCell);
    	invoiceItemTables.addCell(productCell);
    	invoiceItemTables.addCell(accountCodeCell);
    	invoiceItemTables.addCell(rateCell);
    	invoiceItemTables.addCell(unitsCell);
    	invoiceItemTables.addCell(quatityCell);
    	invoiceItemTables.addCell(totalCell);
    	invoiceItemTables.addCell(discCell);
    	invoiceItemTables.addCell(taxableValueCell);
    	invoiceItemTables.addCell(cgstParentCell);
    	invoiceItemTables.addCell(sgstParentCell);
    	invoiceItemTables.addCell(igstParentCell);
    	
    	String cgstRate = "Rate";
    	PdfPCell cgstRateCell = new PdfPCell(new Phrase(cgstRate, getBoldFont()));
    	
    	String cgstAmount = "Amount";
    	PdfPCell cgstAmountCell = new PdfPCell(new Phrase(cgstAmount, getBoldFont()));
    	
    	String sgstRate = "Rate";
    	PdfPCell sgstRateCell = new PdfPCell(new Phrase(sgstRate, getBoldFont()));
    	
    	String sgstAmount = "Amount";
    	PdfPCell sgstAmountCell = new PdfPCell(new Phrase(sgstAmount, getBoldFont()));
    	
    	String igstRate = "Rate";
    	PdfPCell igstRateCell = new PdfPCell(new Phrase(igstRate, getBoldFont()));
    	
    	String igstAmount = "Amount";
    	PdfPCell igstAmountCell = new PdfPCell(new Phrase(igstAmount, getBoldFont()));
    	
    	invoiceItemTables.addCell(cgstRateCell);
    	invoiceItemTables.addCell(cgstAmountCell);
    	invoiceItemTables.addCell(sgstRateCell);
    	invoiceItemTables.addCell(sgstAmountCell);
    	invoiceItemTables.addCell(igstRateCell);
    	invoiceItemTables.addCell(igstAmountCell);
    	
    	Collection<Invoiceitemdetail> invoiceItems = invoiceModel.getInvoiceitemdetails();
    	int serialNumber = 1;
    	for (Invoiceitemdetail invoiceitemdetail : invoiceItems) {
			String invoiceSN = ""+serialNumber++;
			PdfPCell iSerialNumberCell = new PdfPCell(new Phrase(invoiceSN, getRegularFont()));
			
			String iProduct = invoiceitemdetail.getProductdetail().getProductName();
			PdfPCell iProductCell = new PdfPCell(new Phrase(iProduct, getRegularFont()));
			
			String iAccntCode = "";
			if(invoiceitemdetail.getProductdetail().getProductServiceOrGood().equals("G"))
				iAccntCode = invoiceitemdetail.getProductdetail().getProductHSN().getHsnCode();
			else
				iAccntCode = invoiceitemdetail.getProductdetail().getProductSac().getSacId();
			PdfPCell iAccntCodeCell = new PdfPCell(new Phrase(iAccntCode, getRegularFont()));
			
			String iRate = invoiceitemdetail.getIID_ItemPrice().toPlainString();
			PdfPCell iRateCell = new PdfPCell(new Phrase(iRate, getRegularFont()));
			
			String iUnits = invoiceitemdetail.getIID_ItemUnit() == null ? "" : invoiceitemdetail.getIID_ItemUnit();
			PdfPCell iUnitsCell = new PdfPCell(new Phrase(iUnits, getRegularFont()));
			
			Integer iQuant = invoiceitemdetail.getIID_ProductQuantity();
			PdfPCell iQuantCell = new PdfPCell(new Phrase(iQuant.toString(), getRegularFont()));
			
			String iTotal = invoiceitemdetail.getIID_ItemTotalAmount().toPlainString();
			PdfPCell iTotalCell = new PdfPCell(new Phrase(iTotal, getRegularFont()));
			
			String iDiscount = invoiceitemdetail.getIID_ItemDiscount() == null ? "": invoiceitemdetail.getIID_ItemDiscount().toPlainString();
			PdfPCell iDiscountCell = new PdfPCell(new Phrase(iDiscount, getRegularFont()));
			
			String iTaxableValue = invoiceitemdetail.getIidTaxableamount().toPlainString();
			PdfPCell iTaxableValueCell= new PdfPCell(new Phrase(iTaxableValue, getRegularFont()));
			
			Set<Invoiceitemtaxdetail> taxesForInvoice = invoiceitemdetail.getInvoiceitemtaxdetails();
			
			TaxItem cgstDetails = getCGSTDetails(taxesForInvoice);
			TaxItem sgstDetails = getSGSTDetails(taxesForInvoice);
			TaxItem igstDetails = getIGSTDetails(taxesForInvoice);
			
			String iCgstRate = cgstDetails != null ? cgstDetails.getRate().toPlainString() : "-";
			PdfPCell iCgstRateCell= new PdfPCell(new Phrase(iCgstRate, getRegularFont()));
			
			String iCgstAmount = cgstDetails != null ? cgstDetails.getAmount().toPlainString() : "-";
			PdfPCell iCgstAmountCell = new PdfPCell(new Phrase(iCgstAmount, getRegularFont()));
			
			String iSgstRate = sgstDetails != null ? sgstDetails.getRate().toPlainString() : "-";
			PdfPCell iSgstRateCell= new PdfPCell(new Phrase(iSgstRate, getRegularFont()));
			
			String iSgstAmount = sgstDetails != null ? sgstDetails.getAmount().toPlainString() : "-";
			PdfPCell iSgstAmountCell = new PdfPCell(new Phrase(iSgstAmount, getRegularFont()));
			
			
			String iIgstRate = igstDetails != null ? igstDetails.getRate().toPlainString() : "-";
			PdfPCell iIgstRateCell= new PdfPCell(new Phrase(iIgstRate, getRegularFont()));
			
			String iIgstAmount = igstDetails != null ? igstDetails.getAmount().toPlainString() : "-";
			PdfPCell iIgstAmountCell = new PdfPCell(new Phrase(iIgstAmount, getRegularFont()));
			
			invoiceItemTables.addCell(iSerialNumberCell);
			invoiceItemTables.addCell(iProductCell);
			invoiceItemTables.addCell(iAccntCodeCell);
			invoiceItemTables.addCell(iRateCell);
			invoiceItemTables.addCell(iUnitsCell);
			invoiceItemTables.addCell(iQuantCell);
			invoiceItemTables.addCell(iTotalCell);
			invoiceItemTables.addCell(iDiscountCell);
			invoiceItemTables.addCell(iTaxableValueCell);
			invoiceItemTables.addCell(iCgstRateCell);
			invoiceItemTables.addCell(iCgstAmountCell);
			invoiceItemTables.addCell(iSgstRateCell);
			invoiceItemTables.addCell(iSgstAmountCell);
			invoiceItemTables.addCell(iIgstRateCell);
			invoiceItemTables.addCell(iIgstAmountCell);
		}
    	
    	// Loop and add the invoice Items to the PDF
    	
    	invoiceDoc.add(invoiceItemTables);
    	
    	PdfPTable totalSectionTable = new PdfPTable(4);
    	totalSectionTable.setWidthPercentage(100);
    	PdfPCell signatureCell = new PdfPCell();
    	signatureCell.setColspan(2);
    	signatureCell.setRowspan(5);
    	
    	totalSectionTable.addCell(signatureCell);
    	
    	String totalAmountLabel = "Total Amount:";
    	String totalAmountValue = invoiceModel.getID_InvoiceTotalAmount().toPlainString();
    	PdfPCell totalAmountLabelCell = new PdfPCell(new Phrase(totalAmountLabel, getBoldFont()));
    	PdfPCell totalAmountValueCell = new PdfPCell(new Phrase(totalAmountValue, getRegularFont()));
    	
    	totalSectionTable.addCell(totalAmountLabelCell);
    	totalSectionTable.addCell(totalAmountValueCell);
    	
    	String totalTaxLabel = "Total Tax:";
    	String totalTaxValue = invoiceModel.getID_TaxAmount().toPlainString();
    	
    	PdfPCell totalTaxLabelCell = new PdfPCell(new Phrase(totalTaxLabel, getBoldFont()));
    	PdfPCell totalTaxValueCell = new PdfPCell(new Phrase(totalTaxValue, getRegularFont()));
    	
    	totalSectionTable.addCell(totalTaxLabelCell);
    	totalSectionTable.addCell(totalTaxValueCell);
    	
    	
    	String grandTotalLabel = "Grand Total";
    	String grandTotalValue = invoiceModel.getID_GrandTotal().toPlainString();
    	
    	PdfPCell grandTotalLabelCell = new PdfPCell(new Phrase(grandTotalLabel, getBoldFont()));
    	PdfPCell grandTotalValueCell = new PdfPCell(new Phrase(grandTotalValue, getRegularFont()));
    	
    	totalSectionTable.addCell(grandTotalLabelCell);
    	totalSectionTable.addCell(grandTotalValueCell);
    	
    	String totalPaidLabel = "Amount Paid";
    	String totalPaidValue = invoiceModel.getID_InvoicePaidAmount().toPlainString();
    	
    	PdfPCell amountPaidLabelCell = new PdfPCell(new Phrase(totalPaidLabel, getBoldFont()));
    	PdfPCell amountPaidValueCell = new PdfPCell(new Phrase(totalPaidValue, getRegularFont()));
    	
    	totalSectionTable.addCell(amountPaidLabelCell);
    	totalSectionTable.addCell(amountPaidValueCell);
    	
    	String balAmountLabel = "Balance Amount";
    	String balAmountValue = invoiceModel.getID_InvoiceBalanceAmount().toPlainString();
    	
    	PdfPCell balAmountLabelCell = new PdfPCell(new Phrase(balAmountLabel, getBoldFont()));
    	PdfPCell balAmountValueCell = new PdfPCell(new Phrase(balAmountValue, getRegularFont()));
    	
    	totalSectionTable.addCell(balAmountLabelCell);
    	totalSectionTable.addCell(balAmountValueCell);
    	
    	
    	invoiceDoc.add(totalSectionTable);
    	
    	PdfPTable signatureNameTable = new PdfPTable(4);
    	signatureNameTable.setWidthPercentage(100);
    	StringBuilder signNameBuilder = new StringBuilder();
    	signNameBuilder.append("For ");
    	signNameBuilder.append(user.getBusinessname());
    	signNameBuilder.append(" (Seal and Sign in above box)");
    	
    	PdfPCell signatureNameCell = new PdfPCell(new Phrase(signNameBuilder.toString(), getRegularFont()));
    	signatureNameCell.setColspan(2);
    	signatureNameTable.addCell(signatureNameCell);
    	
    	
    	PdfPCell dueDateLabel = new PdfPCell(new Phrase("Due Date:", getBoldFont()));
    	signatureNameTable.addCell(dueDateLabel);
    	
    	PdfPCell dueDateValue = new PdfPCell(new Phrase(Util.getDateInPrintableFormat(invoiceModel.getID_InvoiceDueDate()), getBoldFont()));
    	signatureNameTable.addCell(dueDateValue);
    	
    	invoiceDoc.add(signatureNameTable);
    	
    	PdfPTable termsAndConditionTable = new PdfPTable(1);
    	termsAndConditionTable.setWidthPercentage(100);
    	String termsAndCondition = "Terms and Conditions:";
    	PdfPCell termsAndConditionCell = new PdfPCell(new Phrase(termsAndCondition, getRegularFont()));
    	termsAndConditionTable.addCell(termsAndConditionCell);
    	
    	invoiceDoc.add(termsAndConditionTable);
    	
    	invoiceDoc.close();
    	return file;
    }
    
    private TaxItem getCGSTDetails(Set<Invoiceitemtaxdetail> taxDetails) {
    	for (Invoiceitemtaxdetail invoiceitemtaxdetail : taxDetails) {
			if(invoiceitemtaxdetail.getITD_taxType().equals("Central GST")) {
				TaxItem taxItem = new TaxItem();
				taxItem.setRate(invoiceitemtaxdetail.getITD_taxrate());
				taxItem.setAmount(invoiceitemtaxdetail.getITD_taxamount());
				return taxItem;
			}
		}
    	return null;
    }

    private TaxItem getSGSTDetails(Set<Invoiceitemtaxdetail> taxDetails) {
    	for (Invoiceitemtaxdetail invoiceitemtaxdetail : taxDetails) {
			if(invoiceitemtaxdetail.getITD_taxType().equals("State GST")) {
				TaxItem taxItem = new TaxItem();
				taxItem.setRate(invoiceitemtaxdetail.getITD_taxrate());
				taxItem.setAmount(invoiceitemtaxdetail.getITD_taxamount());
				return taxItem;
			}
		}
    	return null;
    }
    
    private TaxItem getIGSTDetails(Set<Invoiceitemtaxdetail> taxDetails) {
    	for (Invoiceitemtaxdetail invoiceitemtaxdetail : taxDetails) {
			if(invoiceitemtaxdetail.getITD_taxType().equals("Integrated GST")) {
				TaxItem taxItem = new TaxItem();
				taxItem.setRate(invoiceitemtaxdetail.getITD_taxrate());
				taxItem.setAmount(invoiceitemtaxdetail.getITD_taxamount());
				return taxItem;
			}
		}
    	return null;
    }
    
    private Font getRegularFont() {
    	Font font = FontFactory.getFont(REGULAR_FONT, "Cp1252", true);
    	font.setSize(8);
    	return font;
    }
    
    private Font getBoldFont() {
    	Font font = FontFactory.getFont(BOLD_FONT, "Cp1252", true);
    	font.setSize(8);
    	return font;
    }
    
}
