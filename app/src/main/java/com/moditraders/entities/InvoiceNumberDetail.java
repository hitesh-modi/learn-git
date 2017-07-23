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
@Table(name="invoicenumberdetails")
@NamedQuery(name="InvoiceNumberDetail.findAll", query="SELECT i FROM InvoiceNumberDetail i")
public class InvoiceNumberDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="invoicedate")
	private Date invoiceDate;
	
	@Column(name="sequenceno")
	private int sequenceNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
}