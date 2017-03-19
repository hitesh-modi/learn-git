package com.jpmc.rdt.docmgmt.ocr.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ta.model.FaxData;

/**
 * 
 * @author Hitesh Modi
 *
 */
public interface ITAFaxAcknowledgementService {
	/**
	 * This method processes input xls file that is provided to it as a
	 * parameter. This method is responsible for loading the input xls into the
	 * database table.
	 * 
	 * @param inputFile
	 *            Input file that contains data to be loaded.
	 * @param applicationId
	 *            Application id from the excel-loader-config.xml
	 * @throws OcrServiceException
	 *             If anything goes wrong.
	 */
	public void loadExcelSheetToDatabase(File inputFile, String applicationId) throws OcrServiceException;

	/**
	 * Updates the status of the batch in the database.
	 * 
	 * @param batchId
	 *            Batch for which status needs to be updated
	 * @param status
	 *            Status that the batch should have.
	 * @throws OcrServiceException
	 *             If anything goes wrong.
	 */
	public void updateStatusOftheBatch(String batchId, String status) throws OcrServiceException;

	/**
	 * Send the batch for process, so that emails can be sent.
	 * This method is annotated with {@link Async}, hence it is executed in a separate thread asynchronously.
	 * 
	 * @param batchId
	 *            Batch to process.
	 * @throws SQLException
	 *             If any database exception occurs.
	 */
	public void processEmails(String batchId) throws OcrServiceException;
	
	/**
	 * Get the all fax data to Process from Database.
	 * @return
	 * @throws OcrServiceException
	 */
	public List<FaxData> getFaxDataInOpenState() throws OcrServiceException;

}
