package com.moditraders.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the invoicedetails database table.
 * 
 */
@Entity
@Table(name="invoicedetails")
@NamedQuery(name="Invoicedetail.findAll", query="SELECT i FROM Invoicedetail i")
public class Invoicedetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int ID_InvoiceId;

	private Timestamp ID_CreationTimestamp;

	private BigDecimal ID_InvoiceBalanceAmount;

	@Temporal(TemporalType.DATE)
	private Date ID_InvoiceDate;

	private BigDecimal ID_InvoicePaidAmount;

	private BigDecimal ID_InvoiceTotalAmount;

	private Timestamp ID_ModificationTimestamp;

	private BigDecimal ID_TaxAmount;

	//bi-directional many-to-one association to CustomerDetail
	@ManyToOne
	@JoinColumn(name="ID_CustomerId")
	private CustomerDetail customerDetail;

	//bi-directional many-to-one association to Taxrate
	@ManyToOne
	@JoinColumn(name="ID_TaxId")
	private Taxrate taxrate;

	//bi-directional many-to-one association to Invoiceitemdetail
	@OneToMany(mappedBy="invoicedetail")
	private List<Invoiceitemdetail> invoiceitemdetails;

	public Invoicedetail() {
	}

	public int getID_InvoiceId() {
		return this.ID_InvoiceId;
	}

	public void setID_InvoiceId(int ID_InvoiceId) {
		this.ID_InvoiceId = ID_InvoiceId;
	}

	public Timestamp getID_CreationTimestamp() {
		return this.ID_CreationTimestamp;
	}

	public void setID_CreationTimestamp(Timestamp ID_CreationTimestamp) {
		this.ID_CreationTimestamp = ID_CreationTimestamp;
	}

	public BigDecimal getID_InvoiceBalanceAmount() {
		return this.ID_InvoiceBalanceAmount;
	}

	public void setID_InvoiceBalanceAmount(BigDecimal ID_InvoiceBalanceAmount) {
		this.ID_InvoiceBalanceAmount = ID_InvoiceBalanceAmount;
	}

	public Date getID_InvoiceDate() {
		return this.ID_InvoiceDate;
	}

	public void setID_InvoiceDate(Date ID_InvoiceDate) {
		this.ID_InvoiceDate = ID_InvoiceDate;
	}

	public BigDecimal getID_InvoicePaidAmount() {
		return this.ID_InvoicePaidAmount;
	}

	public void setID_InvoicePaidAmount(BigDecimal ID_InvoicePaidAmount) {
		this.ID_InvoicePaidAmount = ID_InvoicePaidAmount;
	}

	public BigDecimal getID_InvoiceTotalAmount() {
		return this.ID_InvoiceTotalAmount;
	}

	public void setID_InvoiceTotalAmount(BigDecimal ID_InvoiceTotalAmount) {
		this.ID_InvoiceTotalAmount = ID_InvoiceTotalAmount;
	}

	public Timestamp getID_ModificationTimestamp() {
		return this.ID_ModificationTimestamp;
	}

	public void setID_ModificationTimestamp(Timestamp ID_ModificationTimestamp) {
		this.ID_ModificationTimestamp = ID_ModificationTimestamp;
	}

	public BigDecimal getID_TaxAmount() {
		return this.ID_TaxAmount;
	}

	public void setID_TaxAmount(BigDecimal ID_TaxAmount) {
		this.ID_TaxAmount = ID_TaxAmount;
	}

	public CustomerDetail getCustomerDetail() {
		return this.customerDetail;
	}

	public void setCustomerDetail(CustomerDetail customerDetail) {
		this.customerDetail = customerDetail;
	}

	public Taxrate getTaxrate() {
		return this.taxrate;
	}

	public void setTaxrate(Taxrate taxrate) {
		this.taxrate = taxrate;
	}

	public List<Invoiceitemdetail> getInvoiceitemdetails() {
		return this.invoiceitemdetails;
	}

	public void setInvoiceitemdetails(List<Invoiceitemdetail> invoiceitemdetails) {
		this.invoiceitemdetails = invoiceitemdetails;
	}

	public Invoiceitemdetail addInvoiceitemdetail(Invoiceitemdetail invoiceitemdetail) {
		getInvoiceitemdetails().add(invoiceitemdetail);
		invoiceitemdetail.setInvoicedetail(this);

		return invoiceitemdetail;
	}

	public Invoiceitemdetail removeInvoiceitemdetail(Invoiceitemdetail invoiceitemdetail) {
		getInvoiceitemdetails().remove(invoiceitemdetail);
		invoiceitemdetail.setInvoicedetail(null);

		return invoiceitemdetail;
	}

}