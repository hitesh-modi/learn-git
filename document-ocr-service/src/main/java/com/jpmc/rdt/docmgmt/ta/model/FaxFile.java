package com.jpmc.rdt.docmgmt.ta.model;

import java.sql.Timestamp;

public class FaxFile {

	private long fileID;

	private String applicationName;
	
	private String fileName;
	
	private String filePath;
	
	private Timestamp fileCreationDate;
	
	private Timestamp fileModificationDate;
	
	private String status;

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
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the fileCreationDate
	 */
	public Timestamp getFileCreationDate() {
		return fileCreationDate;
	}

	/**
	 * @param fileCreationDate the fileCreationDate to set
	 */
	public void setFileCreationDate(Timestamp fileCreationDate) {
		this.fileCreationDate = fileCreationDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the fileModificationDate
	 */
	public Timestamp getFileModificationDate() {
		return fileModificationDate;
	}

	/**
	 * @param fileModificationDate the fileModificationDate to set
	 */
	public void setFileModificationDate(Timestamp fileModificationDate) {
		this.fileModificationDate = fileModificationDate;
	}
	
	@Override
	public String toString() {
		return "FaxFile[ fileID : " + fileID + ", applicationName : " + applicationName + ", fileName : " +fileName+ ", filePath : " + filePath + ", fileCreationDate : " + fileCreationDate + ", fileModificationDate : " + fileModificationDate +", status : " + status + " ]";
	}
	
}
