package com.jpmc.rdt.docmgmt.ocr.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.jpmc.rdt.docmgmt.database.persistence.ITransferAgencyDao;
import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ocr.model.OcrDataModel;
import com.jpmc.rdt.docmgmt.ocr.model.OcrResponse;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;
import com.jpmc.rdt.docmgmt.ta.model.FaxFileDetails;
import com.jpmc.rdt.docmgmt.ta.util.FaxInstructionScanOperation;

@Service("ocrServiceTA")
public class OcrServiceTA implements IOcrService {

	@Value("${faxacknowledge.email.content}")
	private String emailContent;

	@Value("${lux.spooler.processedFolderPath}")
	private String luxSpoolerProcessedFolerPath;

	@Value("${lux.spooler.faxTempPath}")
	private String luxSpoolerProcessedTempPath;

	@Value("${datacap.wTMURL}")
	private String datacapBatchCreateURL;

	@Value("${lux.spooler.job}")
	private String luxSpoolerJobName;

	@Value("${lux.spooler.contentType}")
	private String luxSpoolerContentType;

	@Value("${lux.spooler.docIdUrl}")
	private String luxSpoolerDocIdURL;

	@Value("${lux.spooler.docIdUrl.userName}")
	private String luxSpoolerDocIdUserName;

	@Value("${lux.spooler.docIdUrl.password}")
	private String luxSpoolerDocIdPassword;
	
	@Value("${hk.spooler.job}")
	private String hkSpoolerJob;
	
	@Value("${hk.spooler.processedFolderPath}")
	private String hkProcessedFolderPath;
	
	@Value("${hk.spooler.faxTempPath}")
	private String hkSpoolerTempPath;
	
	@Value("${hk.spooler.contentType}")
	private String hkSpoolerContentType;
	
	@Value("${lux.spooler.dealDateTimeZone}")
	private String luxDealDateTimeZone;
	
	@Value("${hk.spooler.dealDateTimeZone}")
	private String hkDealDateTimeZone;

	@Resource(name = "taDao")
	private ITransferAgencyDao taDao;

	@Resource(name = "faxInstructionScanOperation")
	private FaxInstructionScanOperation scanService;

	@Resource
	private ThreadPoolTaskExecutor taskExecutor;

	private static final Logger LOGGER = Logger.getLogger(OcrServiceTA.class);

	@Override
	public OcrResponse processRequest(String sourceSystem, String requestedFile, OcrDataModel data)
			throws OcrServiceException {

		return null;
	}

	@Override
	public OcrResponse processResponse() throws OcrServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Async("luxSpoolerTaskExecutor")
	@Transactional
	public void processFileForLux(FaxFile faxFile) {
		LOGGER.info("Processing for file + " + faxFile.getFileName() + " started.");
		taDao.updateFaxFileStatus(faxFile.getFileID(), "PROCESSING");
		String clientType = "";
		String applicationName = "";
		try {

			String docId = scanService.getUniqueDocID(luxSpoolerDocIdURL, luxSpoolerDocIdUserName,
					luxSpoolerDocIdPassword);
			String[] applicationNameArr = StringUtils.split(faxFile.getApplicationName(), "-");
			if (applicationNameArr != null && applicationNameArr.length != 2) {
				LOGGER.info("Please configure application name correctly");
			} else if (applicationNameArr != null && applicationNameArr.length == 2) {
				clientType = applicationNameArr[1];
				applicationName = applicationNameArr[0];

				FaxFileDetails faxFileDetails = scanService.uploadDocumentLux(datacapBatchCreateURL, applicationName,
						new File(faxFile.getFilePath()), luxSpoolerJobName, luxSpoolerProcessedTempPath, docId,
						clientType, luxSpoolerContentType, luxSpoolerProcessedFolerPath, faxFile.getFileID(),
						luxDealDateTimeZone);
				LOGGER.info("Saving fax file details in DB " + faxFileDetails);
				taDao.saveFileFaxDetails(faxFileDetails);
				LOGGER.info("Updating the status of the fax file to BATCH_CREATED for file " + faxFile.getFileName());
				taDao.updateFaxFileStatus(faxFile.getFileID(), "BATCH_CREATED");
				LOGGER.info("Processing for file completed successfully , details are : " + faxFile);
			}
		} catch (Exception e) {
			LOGGER.error("Updating the status for the file : " + faxFile.getFileID());
			taDao.updateFaxFileStatusError(faxFile.getFileID(), "SPOOL1001", e.getMessage(), "ERROR");
			e.printStackTrace();
		}

	}
	
	@Async("hkSpoolerTaskExecutor")
	@Transactional
	public void processFileForHK(FaxFile faxFile) {
		LOGGER.info("Processing for file + " + faxFile.getFileName() + " started.");
		taDao.updateFaxFileStatus(faxFile.getFileID(), "PROCESSING");
		String applicationName = faxFile.getApplicationName();
		try {
			 
			String docId = scanService.getUniqueDocID(luxSpoolerDocIdURL, luxSpoolerDocIdUserName,
					luxSpoolerDocIdPassword);
			
				
				FaxFileDetails faxFileDetails = scanService.uploadDocumentHK(applicationName,
						hkSpoolerJob, datacapBatchCreateURL, new File(faxFile.getFilePath()), docId, hkSpoolerTempPath, hkSpoolerContentType, hkProcessedFolderPath, faxFile.getFileID(), hkDealDateTimeZone);
				LOGGER.info("Saving fax file details in DB " + faxFileDetails);
				taDao.saveFileFaxDetails(faxFileDetails);
				LOGGER.info(
						"Updating the status of the fax file to BATCH_CREATED for file " + faxFile.getFileName());
				taDao.updateFaxFileStatus(faxFile.getFileID(), "BATCH_CREATED");
				LOGGER.info("Processing for file completed successfully , details are : " + faxFile);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Updating the status for the file : " + faxFile.getFileID());
			taDao.updateFaxFileStatusError(faxFile.getFileID(), "SPOOL1001", e.getMessage(), "ERROR");
		}

	}

	/**
	 * This method saves the FaxFile object into database table FAX_FILE in
	 * DATACAP schema.
	 * 
	 * @param sourceSystem
	 *            Name of the source system.
	 * @param requestedFile
	 *            File whose details needs to be saved.
	 * @param data
	 *            meta data for the file.
	 * @return Object of FaxFile class which will contain the primary key as
	 *         well.
	 */
	@Override
	public FaxFile saveFaxFileToDB(String sourceSystem, String requestedFile, OcrDataModel data) {
		FaxFile faxFile = new FaxFile();
		faxFile.setApplicationName(sourceSystem);
		faxFile.setFileName((String)data.getOrdData("fileName"));
		faxFile.setFilePath(requestedFile);
		faxFile.setFileCreationDate((Timestamp) data.getOrdData("fileCreationDate"));
		faxFile.setFileModificationDate((Timestamp) data.getOrdData("fileModificationDate"));
		faxFile.setStatus("OPEN");
		LOGGER.info("Saving : " + faxFile);
		long generatedPrimaryKey = taDao.saveFileFax(faxFile);
		faxFile.setFileID(generatedPrimaryKey);
		LOGGER.info("Saved : " + faxFile);
		return faxFile;
	}
	
	@Override
	public boolean checkIfFileAlreadyProcessed(String fileName, String applicationName) throws OcrServiceException{
		LOGGER.info("Checking if the file " + fileName + " exists in the database.");
		int fileCount=0;
		try {
			fileCount = taDao.getFaxFiles(fileName, applicationName);
			LOGGER.info("There are " + fileCount + " exists in the database.");
		} catch (SQLException e) {
			throw new OcrServiceException("SQLException while getting the file details", e);
		}
		if(fileCount > 0) {
			return true;
		}
		return false;
	}

}
