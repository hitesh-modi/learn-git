package com.moditraders.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the invoiceitemtaxdetails database table.
 * 
 */
@Entity
@Table(name="invoiceitemtaxdetails")
@NamedQuery(name="Invoiceitemtaxdetail.findAll", query="SELECT i FROM Invoiceitemtaxdetail i")
public class Invoiceitemtaxdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InvoiceitemtaxdetailPK id;

	private BigDecimal ITD_taxamount;

	private BigDecimal ITD_taxrate;

	//bi-directional many-to-one association to Invoiceitemdetail
	@ManyToOne
	@JoinColumn(name="ITD_itemid")
	private Invoiceitemdetail invoiceitemdetail;

	public Invoiceitemtaxdetail() {
	}

	public InvoiceitemtaxdetailPK getId() {
		return this.id;
	}

	public void setId(InvoiceitemtaxdetailPK id) {
		this.id = id;
	}

	public BigDecimal getITD_taxamount() {
		return this.ITD_taxamount;
	}

	public void setITD_taxamount(BigDecimal ITD_taxamount) {
		this.ITD_taxamount = ITD_taxamount;
	}

	public BigDecimal getITD_taxrate() {
		return this.ITD_taxrate;
	}

	public void setITD_taxrate(BigDecimal ITD_taxrate) {
		this.ITD_taxrate = ITD_taxrate;
	}

	public Invoiceitemdetail getInvoiceitemdetail() {
		return this.invoiceitemdetail;
	}

	public void setInvoiceitemdetail(Invoiceitemdetail invoiceitemdetail) {
		this.invoiceitemdetail = invoiceitemdetail;
	}

}