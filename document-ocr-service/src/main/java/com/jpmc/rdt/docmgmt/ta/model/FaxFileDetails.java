package com.jpmc.rdt.docmgmt.ta.model;

public class FaxFileDetails {

	private long fileID;

	private String batchId;
	
	private String fileName;
	
	private String documentId;
	
	private String applicationName;
	
	private int pageCount;

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the fileID
	 */
	public long getFileID() {
		return fileID;
	}

	/**
	 * @param fileID the fileID to set
	 */
	public void setFileID(long fileID) {
		this.fileID = fileID;
	}

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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	@Override
	public String toString() {
		return "FaxFileDetails [ fileID : "+ fileID +", batchId : "+batchId+", fileName : "+fileName+", documentId : "+documentId+", applicationName : " + applicationName +", pageCount : "+pageCount+" ]";
	}

	
}
