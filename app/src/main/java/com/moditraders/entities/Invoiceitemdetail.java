package com.moditraders.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the invoiceitemdetails database table.
 * 
 */
@Entity
@Table(name="invoiceitemdetails")
@NamedQuery(name="Invoiceitemdetail.findAll", query="SELECT i FROM Invoiceitemdetail i")
public class Invoiceitemdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long IID_ItemId;

	@Column(name="iid_cgst")
	private BigDecimal iidCgst;

	@Column(name="iid_igst")
	private BigDecimal iidIgst;

	private BigDecimal IID_ItemDiscount;

	private BigDecimal IID_ItemPrice;

	private String IID_ItemType;

	private int IID_ProductQuantity;

	@Temporal(TemporalType.DATE)
	private Date IID_ServiceEndDate;

	@Temporal(TemporalType.DATE)
	private Date IID_ServiceStartDate;

	@Column(name="iid_sgst")
	private BigDecimal iidSgst;

	@Column(name="iid_taxableamount")
	private BigDecimal iidTaxableamount;

	//bi-directional many-to-one association to Invoicedetail
	@ManyToOne
	@JoinColumn(name="IID_InvoiceId")
	private Invoicedetail invoicedetail;

	//bi-directional many-to-one association to Productdetail
	@ManyToOne
	@JoinColumn(name="IID_ProductId")
	private Productdetail productdetail;

	//bi-directional many-to-one association to Servicedetail
	@ManyToOne
	@JoinColumn(name="IID_ServiceId")
	private Servicedetail servicedetail;

	//bi-directional many-to-one association to Invoiceitemtaxdetail
	@OneToMany(mappedBy="invoiceitemdetail")
	private List<Invoiceitemtaxdetail> invoiceitemtaxdetails;

	public Invoiceitemdetail() {
	}

	public long getIID_ItemId() {
		return this.IID_ItemId;
	}

	public void setIID_ItemId(long IID_ItemId) {
		this.IID_ItemId = IID_ItemId;
	}

	public BigDecimal getIidCgst() {
		return this.iidCgst;
	}

	public void setIidCgst(BigDecimal iidCgst) {
		this.iidCgst = iidCgst;
	}

	public BigDecimal getIidIgst() {
		return this.iidIgst;
	}

	public void setIidIgst(BigDecimal iidIgst) {
		this.iidIgst = iidIgst;
	}

	public BigDecimal getIID_ItemDiscount() {
		return this.IID_ItemDiscount;
	}

	public void setIID_ItemDiscount(BigDecimal IID_ItemDiscount) {
		this.IID_ItemDiscount = IID_ItemDiscount;
	}

	public BigDecimal getIID_ItemPrice() {
		return this.IID_ItemPrice;
	}

	public void setIID_ItemPrice(BigDecimal IID_ItemPrice) {
		this.IID_ItemPrice = IID_ItemPrice;
	}

	public String getIID_ItemType() {
		return this.IID_ItemType;
	}

	public void setIID_ItemType(String IID_ItemType) {
		this.IID_ItemType = IID_ItemType;
	}

	public int getIID_ProductQuantity() {
		return this.IID_ProductQuantity;
	}

	public void setIID_ProductQuantity(int IID_ProductQuantity) {
		this.IID_ProductQuantity = IID_ProductQuantity;
	}

	public Date getIID_ServiceEndDate() {
		return this.IID_ServiceEndDate;
	}

	public void setIID_ServiceEndDate(Date IID_ServiceEndDate) {
		this.IID_ServiceEndDate = IID_ServiceEndDate;
	}

	public Date getIID_ServiceStartDate() {
		return this.IID_ServiceStartDate;
	}

	public void setIID_ServiceStartDate(Date IID_ServiceStartDate) {
		this.IID_ServiceStartDate = IID_ServiceStartDate;
	}

	public BigDecimal getIidSgst() {
		return this.iidSgst;
	}

	public void setIidSgst(BigDecimal iidSgst) {
		this.iidSgst = iidSgst;
	}

	public BigDecimal getIidTaxableamount() {
		return this.iidTaxableamount;
	}

	public void setIidTaxableamount(BigDecimal iidTaxableamount) {
		this.iidTaxableamount = iidTaxableamount;
	}

	public Invoicedetail getInvoicedetail() {
		return this.invoicedetail;
	}

	public void setInvoicedetail(Invoicedetail invoicedetail) {
		this.invoicedetail = invoicedetail;
	}

	public Productdetail getProductdetail() {
		return this.productdetail;
	}

	public void setProductdetail(Productdetail productdetail) {
		this.productdetail = productdetail;
	}

	public Servicedetail getServicedetail() {
		return this.servicedetail;
	}

	public void setServicedetail(Servicedetail servicedetail) {
		this.servicedetail = servicedetail;
	}

	public List<Invoiceitemtaxdetail> getInvoiceitemtaxdetails() {
		return this.invoiceitemtaxdetails;
	}

	public void setInvoiceitemtaxdetails(List<Invoiceitemtaxdetail> invoiceitemtaxdetails) {
		this.invoiceitemtaxdetails = invoiceitemtaxdetails;
	}

	public Invoiceitemtaxdetail addInvoiceitemtaxdetail(Invoiceitemtaxdetail invoiceitemtaxdetail) {
		getInvoiceitemtaxdetails().add(invoiceitemtaxdetail);
		invoiceitemtaxdetail.setInvoiceitemdetail(this);

		return invoiceitemtaxdetail;
	}

	public Invoiceitemtaxdetail removeInvoiceitemtaxdetail(Invoiceitemtaxdetail invoiceitemtaxdetail) {
		getInvoiceitemtaxdetails().remove(invoiceitemtaxdetail);
		invoiceitemtaxdetail.setInvoiceitemdetail(null);

		return invoiceitemtaxdetail;
	}

}