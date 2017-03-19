package com.jpmc.rdt.docmgmt.ta.model;
/**
 * 
 * @author Hitesh Modi
 *
 */
public class FaxDetails {
	private String batchId;
	private int pageNumber;
	private String accountNumber;
	private boolean isCoverPage;
	private boolean isValidAccountNumber;
	private boolean isProcessed;
	/**
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}
	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the isCoverPage
	 */
	public boolean isCoverPage() {
		return isCoverPage;
	}
	/**
	 * @param isCoverPage the isCoverPage to set
	 */
	public void setCoverPage(boolean isCoverPage) {
		this.isCoverPage = isCoverPage;
	}
	/**
	 * @return the isValidAccountNumber
	 */
	public boolean isValidAccountNumber() {
		return isValidAccountNumber;
	}
	/**
	 * @param isValidAccountNumber the isValidAccountNumber to set
	 */
	public void setValidAccountNumber(boolean isValidAccountNumber) {
		this.isValidAccountNumber = isValidAccountNumber;
	}
	/**
	 * @return the isProcessed
	 */
	public boolean isProcessed() {
		return isProcessed;
	}
	/**
	 * @param isProcessed the isProcessed to set
	 */
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	
	
}
