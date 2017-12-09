package com.moditraders.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the invoiceitemtaxdetails database table.
 * 
 */
@Embeddable
public class InvoiceitemtaxdetailPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private long ITD_itemid;

	private String ITD_taxtype;

	public InvoiceitemtaxdetailPK() {
	}
	public long getITD_itemid() {
		return this.ITD_itemid;
	}
	public void setITD_itemid(long ITD_itemid) {
		this.ITD_itemid = ITD_itemid;
	}
	public String getITD_taxtype() {
		return this.ITD_taxtype;
	}
	public void setITD_taxtype(String ITD_taxtype) {
		this.ITD_taxtype = ITD_taxtype;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InvoiceitemtaxdetailPK)) {
			return false;
		}
		InvoiceitemtaxdetailPK castOther = (InvoiceitemtaxdetailPK)other;
		return 
			this.ITD_itemid == castOther.ITD_itemid
			&& this.ITD_taxtype.equals(castOther.ITD_taxtype);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = (int) (hash * prime + this.ITD_itemid);
		hash = hash * prime + this.ITD_taxtype.hashCode();
		
		return hash;
	}
}