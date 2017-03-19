package com.jpmc.rdt.docmgmt.ta.model;

import java.sql.Date;

public class FaxData {
	
	String batchId;
	int countOfPages;
	Date creationDate;
	String status;
	String errorCode;
	String errorDescription;
	
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public int getCountOfPages() {
		return countOfPages;
	}
	public void setCountOfPages(int countOfPages) {
		this.countOfPages = countOfPages;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
	

}
