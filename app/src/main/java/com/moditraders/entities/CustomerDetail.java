package com.moditraders.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the customer_details database table.
 * 
 */
@Entity
@Table(name="customer_details")
@NamedQuery(name="CustomerDetail.findAll", query="SELECT c FROM CustomerDetail c")
public class CustomerDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CD_CUSTOMER_ID")
	private int cdCustomerId;

	@Column(name="CD_CREATIONTIMESTAMP")
	private Timestamp cdCreationtimestamp;

	@Column(name="CD_CUSTOMER_ADDRESS")
	private String cdCustomerAddress;

	@Column(name="CD_CUSTOMER_EMAIL")
	private String cdCustomerEmail;

	@Column(name="CD_CUSTOMER_FIRSTNAME")
	private String cdCustomerFirstname;

	@Column(name="CD_CUSTOMER_LASTNAME")
	private String cdCustomerLastname;

	@Column(name="CD_CUSTOMER_MIDDLENAME")
	private String cdCustomerMiddlename;

	@Column(name="CD_CUSTOMER_MOBILE")
	private String cdCustomerMobile;

	@Column(name="CD_CUSTOMER_PHONE")
	private String cdCustomerPhone;

	@Column(name="CD_MODIFICATIONTIMESTAMP")
	private Timestamp cdModificationtimestamp;
	
	@Column(name="CD_CUSTOMER_STATE")
	private String cdCustomerState;
	
	@Column(name="CD_CUSTOMER_STATE_CODE")
	private String cdCustomerStateCode;
	
	@Column(name="CD_GSTIN")
	private String cdCustomerGSTIN;

	public String getCdCustomerState() {
		return cdCustomerState;
	}

	public void setCdCustomerState(String cdCustomerState) {
		this.cdCustomerState = cdCustomerState;
	}

	public String getCdCustomerStateCode() {
		return cdCustomerStateCode;
	}

	public void setCdCustomerStateCode(String cdCustomerStateCode) {
		this.cdCustomerStateCode = cdCustomerStateCode;
	}

	public String getCdCustomerGSTIN() {
		return cdCustomerGSTIN;
	}

	public void setCdCustomerGSTIN(String cdCustomerGSTIN) {
		this.cdCustomerGSTIN = cdCustomerGSTIN;
	}

	//bi-directional many-to-one association to Invoicedetail
	@OneToMany(mappedBy="customerDetail")
	private List<Invoicedetail> invoicedetails;

	public CustomerDetail() {
	}

	public int getCdCustomerId() {
		return this.cdCustomerId;
	}

	public void setCdCustomerId(int cdCustomerId) {
		this.cdCustomerId = cdCustomerId;
	}

	public Timestamp getCdCreationtimestamp() {
		return this.cdCreationtimestamp;
	}

	public void setCdCreationtimestamp(Timestamp cdCreationtimestamp) {
		this.cdCreationtimestamp = cdCreationtimestamp;
	}

	public String getCdCustomerAddress() {
		return this.cdCustomerAddress;
	}

	public void setCdCustomerAddress(String cdCustomerAddress) {
		this.cdCustomerAddress = cdCustomerAddress;
	}

	public String getCdCustomerEmail() {
		return this.cdCustomerEmail;
	}

	public void setCdCustomerEmail(String cdCustomerEmail) {
		this.cdCustomerEmail = cdCustomerEmail;
	}

	public String getCdCustomerFirstname() {
		return this.cdCustomerFirstname;
	}

	public void setCdCustomerFirstname(String cdCustomerFirstname) {
		this.cdCustomerFirstname = cdCustomerFirstname;
	}

	public String getCdCustomerLastname() {
		return this.cdCustomerLastname;
	}

	public void setCdCustomerLastname(String cdCustomerLastname) {
		this.cdCustomerLastname = cdCustomerLastname;
	}

	public String getCdCustomerMiddlename() {
		return this.cdCustomerMiddlename;
	}

	public void setCdCustomerMiddlename(String cdCustomerMiddlename) {
		this.cdCustomerMiddlename = cdCustomerMiddlename;
	}

	public String getCdCustomerMobile() {
		return this.cdCustomerMobile;
	}

	public void setCdCustomerMobile(String cdCustomerMobile) {
		this.cdCustomerMobile = cdCustomerMobile;
	}

	public String getCdCustomerPhone() {
		return this.cdCustomerPhone;
	}

	public void setCdCustomerPhone(String cdCustomerPhone) {
		this.cdCustomerPhone = cdCustomerPhone;
	}

	public Timestamp getCdModificationtimestamp() {
		return this.cdModificationtimestamp;
	}

	public void setCdModificationtimestamp(Timestamp cdModificationtimestamp) {
		this.cdModificationtimestamp = cdModificationtimestamp;
	}

	public List<Invoicedetail> getInvoicedetails() {
		return this.invoicedetails;
	}

	public void setInvoicedetails(List<Invoicedetail> invoicedetails) {
		this.invoicedetails = invoicedetails;
	}

	public Invoicedetail addInvoicedetail(Invoicedetail invoicedetail) {
		getInvoicedetails().add(invoicedetail);
		invoicedetail.setCustomerDetail(this);

		return invoicedetail;
	}

	public Invoicedetail removeInvoicedetail(Invoicedetail invoicedetail) {
		getInvoicedetails().remove(invoicedetail);
		invoicedetail.setCustomerDetail(null);

		return invoicedetail;
	}

}