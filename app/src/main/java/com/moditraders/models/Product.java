package com.moditraders.models;

import java.math.BigDecimal;
import java.util.Date;

import com.moditraders.types.ProductType;

public class Product {
	private long productId;
	private ProductType type;
	private String company;
	private Date agencyStartDate;
	private BigDecimal depositAmount;
	public ProductType getType() {
		return type;
	}
	public void setType(ProductType productType) {
		this.type = productType;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Date getAgencyStartDate() {
		return agencyStartDate;
	}
	public void setAgencyStartDate(Date agencyStartDate) {
		this.agencyStartDate = agencyStartDate;
	}
	public BigDecimal getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(BigDecimal securityDeposit) {
		this.depositAmount = securityDeposit;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	@Override
	public String toString() {
		return "Product[ productId: " + productId + ", productType: "+ type +", companyName: "+company+", agencyStartDate:"+agencyStartDate+", securityDeposit:"+depositAmount+" ]";
				
	}
	
}
