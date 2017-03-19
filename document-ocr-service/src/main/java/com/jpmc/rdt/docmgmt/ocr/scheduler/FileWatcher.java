package com.jpmc.rdt.docmgmt.ocr.scheduler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ocr.model.OcrDataModel;
import com.jpmc.rdt.docmgmt.ocr.service.IOcrService;
import com.jpmc.rdt.docmgmt.ocr.service.ITAFaxAcknowledgementService;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;

/**
 * 
 * @author Hitesh Modi
 *
 */

@Component("fileWatcher")
@ConditionalOnProperty(name = "application.name", havingValue = "FAX-ACKNOWLEDGE")
public class FileWatcher {
	private static final Logger LOGGER = Logger.getLogger(FileWatcher.class);

	@Value("${faxacknowledge.inputFilePathForEmailMaster}")
	private String inputFilePathForEmailMaster;

	@Value("${faxacknowledge.inputFilePathForPrimaryAccount}")
	private String inputFilePathForPrimaryAccount;

	@Value("${faxacknowledge.processedFolder}")
	private String processedFolder;
	
	@Value("${lux.inputFilePathForBAUValidation}")
	private String inputFilePathForBAUValidation;

	@Value("${lux.inputFilePathForInvestorValidation}")
	private String inputFilePathForInvestorValidation;

	@Value("${hk.inputFilePathForQuickReference}")
	private String inputFilePathForQuickReference;

	@Value("${hk.inputFilePathForDrools}")
	private String inputFilePathForDrools;
	
	@Value("${hk.spooler.faxFolderPath}")
	private String faxPathForHK;
	
	@Value("${lux.spooler.blackrock.fax.path}")
	private String faxPathForLUXBlackRock;
	
	@Value("${lux.spooler.bau.fax.path}")
	private String faxPathForLUXBAU;
	
	@Value("${lux.spooler.supportedFaxFormats}")
	private String supportedFaxFormatsForLux;
	
	@Value("${hk.spooler.supportedFaxFormats}")
	private String supportedFaxFormatsForHK;
	
	@Value("${excel.loader.supported.format}")
	private String excelLoaderSupportedFormat;
	
	@Value("${faxacknowledge.filewatcher.email.cron.enable}")
	private boolean emailWatchEnable;
	
	@Value("${faxacknowledge.filewatcher.primaryaccount.cron.enable}")
	private boolean primaryAccountEnable;
	
	@Value("${lux.filewatcher.bau.cron.enable}")
	private boolean bauCronEnable;
	
	@Value("${lux.filewatcher.investor.cron.enable}")
	private boolean investorCronEnable;
	
	@Value("${hk.filewatcher.quickreference.cron.enable}")
	private boolean quickReferenceCronEnable;
	
	@Value("${hk.filewatcher.drools.cron.enable}")
	private boolean droolsCronEnable;
	
	@Value("${hk.spooler.cron.enable}")
	private boolean hkSpoolerCronEnable;
	
	@Value("${lux.spooler.blackrock.cron.enable}")
	private boolean luxSpoolerBlackrockCronEnable;
	
	@Value("${lux.spooler.bau.cron.enable}")
	private boolean luxSpoolerBAUCronEnable;
	
	private static final String dateFormat = "yyyyMMdd";
	
	@Value("${lux.spooler.dateFolderFormat}")
	private String dateFormatForSpooler;
	
	@Value("${hk.spooler.applicationName}")
	private String hkSpoolerAppName;
	
	@Value("${lux.spooler.blackrock.applicationName}")
	private String luxSpoolerBlackRockAppName;
	
	@Value("${lux.spooler.bau.applicationName}")
	private String luxSpoolerBAUAppName;

	@Resource(name = "faxAcknowledgeService")
	private ITAFaxAcknowledgementService faxAcknowledgeService;
	
	@Resource(name = "ocrServiceTA")
	private IOcrService ocrServiceTa;
	

	/**
	 * This method will poll the file system for input files. Files should be
	 * present in current day folder inside any directory. This directory path
	 * is configurable through property
	 * "faxacknowledge.inputFilePathForEmailMaster".
	 */
	@Scheduled(cron = "${faxacknowledge.filewatcher.email.cron}")
	public void pollFileSystemForEmailDataFile() {
		if(!emailWatchEnable) {
			LOGGER.info("Polling for updating email master file (Fax Acknowledgement) has been disabled");
			return;
		}
		String directoryNameWithCurrentDate = getDateForDirectory(dateFormat);
		LOGGER.info("File polling started for files related to Email master data.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForEmailMaster + File.separator
				+ directoryNameWithCurrentDate);

		try {
			File inputFile = getFilesFromPath(
					inputFilePathForEmailMaster + File.separator + directoryNameWithCurrentDate, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "fax_acknowledgement_email_master");
				moveTheFileToProcessing(inputFile, inputFilePathForEmailMaster + File.separator
						+ directoryNameWithCurrentDate + File.separator + processedFolder);
			} else {
				LOGGER.info("No or more than one email master input file was present in the path : "
						+ inputFilePathForEmailMaster + File.separator + directoryNameWithCurrentDate);
			}

		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for email master.", e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for email master.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for email master.", e);
		}
	}

	/**
	 * This method will poll the file system for input files. Files should be
	 * present in current day folder inside any directory. This directory path
	 * is configurable through property
	 * "faxacknowledge.inputFilePathForPrimaryAccount".
	 */
	@Scheduled(cron = "${faxacknowledge.filewatcher.primaryaccount.cron}")
	public void pollFileSystemForPrimaryAccountDataFile() {
		if(!primaryAccountEnable) {
			LOGGER.info("Polling for primary account mapping file (Fax Acknowledgement) has been disabled");
			return;
		}
		String directoryNameWithCurrentDate = getDateForDirectory(dateFormat);
		LOGGER.info("File polling started for files related to Primary Account data.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForPrimaryAccount + File.separator
				+ directoryNameWithCurrentDate);

		try {
			File inputFile = getFilesFromPath(
					inputFilePathForPrimaryAccount + File.separator + directoryNameWithCurrentDate, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile,
						"fax_acknowledgement_primary_account_mapping");
				moveTheFileToProcessing(inputFile, inputFilePathForPrimaryAccount + File.separator
						+ directoryNameWithCurrentDate + File.separator + processedFolder);
			} else {
				LOGGER.info("No or more than one primary account input file was present in the path : "
						+ inputFilePathForPrimaryAccount + File.separator + directoryNameWithCurrentDate);
			}
		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for email master.", e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for email master.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for email master.", e);
		}
	}
	// This method will poll the data from LUX_BAU_Validation sheets.

	@Scheduled(cron = "${lux.filewatcher.bau.cron}")
	public void pollFileSystemForBAUFile() {
		
		if(!bauCronEnable) {
			LOGGER.info("Polling for LUX BAU File has been disabled");
			return;
		}
		
		LOGGER.info("File polling started for files related to BAU Validation Sheet.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForBAUValidation);

		try {
			String directoryWithCurrentDate = getDateForDirectory(dateFormat);
			File inputFile = getFilesFromPath(inputFilePathForBAUValidation, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "LUX_BAU_VALIDATION");
				moveTheFileToProcessing(inputFile, inputFilePathForBAUValidation + File.separator + processedFolder
						+ File.separator + directoryWithCurrentDate);
			} else {
				LOGGER.info("No or more than one primary account input file was present in the path : "
						+ inputFilePathForBAUValidation);
			}
		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for BAU Sheet.", e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for BAU Sheet.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for BAU Sheet.", e);
		}
	}

	// This method will poll the data from Private Investors sheets.

	@Scheduled(cron = "${lux.filewatcher.investor.cron}")
	public void pollFileSystemForPrivateInvestorFile() {
		
		if(!investorCronEnable) {
			LOGGER.info("Polling for LUX Investor File has been disabled");
			return;
		}
		
		LOGGER.info("File polling started for files related to Private Investors sheet.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForInvestorValidation);

		try {
			String directoryWithCurrentDate = getDateForDirectory(dateFormat);
			File inputFile = getFilesFromPath(inputFilePathForInvestorValidation, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "LUX_INVESTORS_VALIDATION");
				moveTheFileToProcessing(inputFile, inputFilePathForInvestorValidation + File.separator + processedFolder
						+ File.separator + directoryWithCurrentDate);
			} else {
				LOGGER.info("No or more than one primary account input file was present in the path : "
						+ inputFilePathForInvestorValidation);
			}
		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for Private Investors sheet.",
					e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for Private Investors sheet.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for Private Investors sheet.",
					e);
		}
	}

	// This method will poll the data from AGENT_MAPPING Quick Reference Sheet.

	@Scheduled(cron = "${hk.filewatcher.quickreference.cron}")
	public void pollFileSystemForQuickReferenceFile() {
		
		if(!quickReferenceCronEnable) {
			LOGGER.info("Polling for Quick Reference File has been disabled");
			return;
		}
		
		LOGGER.info("File polling started for files related to Quick Reference Sheet.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForQuickReference);

		try {
			String directoryWithCurrentDate = getDateForDirectory(dateFormat);
			File inputFile = getFilesFromPath(inputFilePathForQuickReference, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "QUICK_REFERENCE");
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "FUND_MAPPING");

				moveTheFileToProcessing(inputFile, inputFilePathForQuickReference + File.separator + processedFolder
						+ File.separator + directoryWithCurrentDate);

			} else {
				LOGGER.info("No or more than one primary account input file was present in the path : "
						+ inputFilePathForQuickReference);
			}
		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for  Quick Reference Sheet.",
					e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for  Quick Reference Sheet.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for  Quick Reference Sheet.",
					e);
		}
	}

	@Scheduled(cron = "${hk.filewatcher.drools.cron}")
	public void pollFileSystemForDroolsFile() {
		
		if(!droolsCronEnable) {
			LOGGER.info("Polling for Quick Reference File has been disabled");
			return;
		}
		
		LOGGER.info("File polling started for files related to Drools Sheet.");
		LOGGER.info("Started polling the directory for input files. " + inputFilePathForDrools);

		try {
			String directoryWithCurrentDate = getDateForDirectory(dateFormat);
			File inputFile = getFilesFromPath(inputFilePathForDrools, excelLoaderSupportedFormat.split(","));
			if (inputFile != null) {
				faxAcknowledgeService.loadExcelSheetToDatabase(inputFile, "DROOLS_VALIDATION");
				moveTheFileToProcessing(inputFile, inputFilePathForDrools + File.separator + processedFolder
						+ File.separator + directoryWithCurrentDate);
			} else {
				LOGGER.info("No or more than one primary account input file was present in the path : "
						+ inputFilePathForDrools + File.separator + getDateForDirectory(dateFormat));
			}
		} catch (OcrServiceException e) {
			LOGGER.error("Ocr Service Excpetion while loading excel sheet into database for Drools Sheet.", e);
		} catch (IOException e) {
			LOGGER.error("IO Excpetion while loading excel sheet into database for  Drools Sheet.", e);
		} catch (Exception e) {
			LOGGER.error("Some general Exception while loading excel sheet into database for Drools Sheet.", e);
		}
	}
	
	/**
	 * Two scedulers are added since HK has to be scheduled from 02:00 AM to 06:30 AM from MON-FRI
	 * Since single cron expression can only schedule it to 02:00 AM to 06:00 AM from MON-FRI, hence two cron expressions are used.
	 * One for scheduling it from 02:00 AM to 06:00 AM and the second from 06:00 to 06:30 AM MON-FRI.
	 */
	@Scheduled(cron = "${hk.spooler.cron1}")
	@Scheduled(cron = "${hk.spooler.cron2}")
	public void pollFileSystemForHKFaxes() {
		
		if(!hkSpoolerCronEnable) {
			LOGGER.info("Spooler service for Hong Kong has been disabled.");
			return;
		}
		
		LOGGER.info("Started polling file system for Faxes, path : " + faxPathForHK);
		try {
			File[] faxes = getFaxesForProcessing(faxPathForHK, supportedFaxFormatsForHK.split(","));
			
			LOGGER.info("Faxes found for processing for HongKong : " + faxes.length);
			
			for(File fax : faxes) {
				/*if(ocrServiceTa.checkIfFileAlreadyProcessed(fax.getName(), hkSpoolerAppName)) {
					LOGGER.info("File entry already exists in the database : " + fax.getName() + ", hence not sending it for processing.");
					continue;
				}*/
				OcrDataModel ocrDataModel = new OcrDataModel();
				Path path = Paths.get(fax.getAbsolutePath());
				BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
				FileTime time = attributes.creationTime();
				ocrDataModel.addOrdData("fileName", fax.getName());
				ocrDataModel.addOrdData("fileCreationDate", new Timestamp(time.toMillis()));
				ocrDataModel.addOrdData("fileModificationDate", new Timestamp(fax.lastModified()));
				
				FaxFile faxFile = ocrServiceTa.saveFaxFileToDB(hkSpoolerAppName, fax.getAbsolutePath(), ocrDataModel);
				ocrServiceTa.processFileForHK(faxFile);
				LOGGER.info("File sent for processing successfully, file: " + fax.getAbsolutePath());
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while processing fax for HK", e);
		}
	}

	@Scheduled(cron = "${lux.spooler.blackrock.cron}")
	public void pollFileSystemForLUXFaxesBlackRock() {
		
		if(!luxSpoolerBlackrockCronEnable) {
			LOGGER.info("Spooler Service for Luxembourg-BlackRock is disabled");
			return;
		}
		
		LOGGER.info("Started polling file system for Faxes, path : " + faxPathForLUXBlackRock);
		try {
			
			String directoryWithCurrentDate = getDateForDirectory(dateFormatForSpooler);
			
			File[] faxes = getFaxesForProcessing(faxPathForLUXBlackRock + File.separator + directoryWithCurrentDate, supportedFaxFormatsForLux.split(","));
			
			LOGGER.info("Faxes found for processing for LUX BlackRock : " + faxes.length);
			
			for(File fax : faxes) {
				
				if(ocrServiceTa.checkIfFileAlreadyProcessed(fax.getName(), luxSpoolerBlackRockAppName)) {
					LOGGER.info("File entry already exists in the database : " + fax.getName() + ", hence not sending it for processing.");
					continue;
				}
				
				OcrDataModel ocrDataModel = new OcrDataModel();
				Path path = Paths.get(fax.getAbsolutePath());
				BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
				FileTime time = attributes.creationTime();
				ocrDataModel.addOrdData("fileName", fax.getName());
				ocrDataModel.addOrdData("fileCreationDate", new Timestamp(time.toMillis()));
				ocrDataModel.addOrdData("fileModificationDate", new Timestamp(fax.lastModified()));
				
				FaxFile faxFile = ocrServiceTa.saveFaxFileToDB(luxSpoolerBlackRockAppName, fax.getAbsolutePath(), ocrDataModel);
				ocrServiceTa.processFileForLux(faxFile);
				LOGGER.info("File sent for processing successfully, file: " + fax.getAbsolutePath());
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while processing fax for LUX Black Rock", e);
		}
	}
	
	
	@Scheduled(cron = "${lux.spooler.bau.cron}")
	public void pollFileSystemForLUXFaxesBau() {
		
		if(!luxSpoolerBAUCronEnable) {
			LOGGER.info("Spooler Service for Luxembourg BAU is disabled");
			return;
		}
		
		LOGGER.info("Started polling file system for Faxes, path : " + faxPathForLUXBAU);
		try {
			
			String directoryWithCurrentDate = getDateForDirectory(dateFormatForSpooler);
			
			File[] faxes = getFaxesForProcessing(faxPathForLUXBAU + File.separator + directoryWithCurrentDate, supportedFaxFormatsForLux.split(","));
			
			LOGGER.info("Faxes found for processing for LUX BAU : " + faxes.length);
			
			for(File fax : faxes) {
				
				if(ocrServiceTa.checkIfFileAlreadyProcessed(fax.getName(), luxSpoolerBAUAppName)) {
					LOGGER.info("File entry already exists in the database : " + fax.getName() + ", hence not sending it for processing.");
					continue;
				}
				
				OcrDataModel ocrDataModel = new OcrDataModel();
				Path path = Paths.get(fax.getAbsolutePath());
				BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
				FileTime time = attributes.creationTime();
				ocrDataModel.addOrdData("fileName", fax.getName());
				ocrDataModel.addOrdData("fileCreationDate", new Timestamp(time.toMillis()));
				ocrDataModel.addOrdData("fileModificationDate", new Timestamp(fax.lastModified()));
				
				LOGGER.info("Saving the file into the Database");
				
				FaxFile faxFile = ocrServiceTa.saveFaxFileToDB(luxSpoolerBAUAppName, fax.getAbsolutePath(), ocrDataModel);
				ocrServiceTa.processFileForLux(faxFile);
				LOGGER.info("File sent for processing successfully, file: " + fax.getAbsolutePath());
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while processing fax for LUX BAU", e);
		}
	}
	
	/**
	 * Returns a string that is formed using the current date. The format is
	 * yyyyMMdd. For e.g. for date 08th Feb 2017 it will return 20170208
	 * 
	 * @return current date in string (yyyyMMdd) format.
	 */
	private static String getDateForDirectory(String format) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * Moves the file to the destination that is provided as a parameter.
	 * 
	 * @param file
	 *            {@link File} to move.
	 * @param destination
	 *            Destination path.
	 * @throws IOException
	 *             If anything goes wrong during moving the file.
	 */
	private static void moveTheFileToProcessing(File file, String destination) throws IOException {
		LOGGER.info("Moving the file " + file.getName() + " to path " + destination);

		File dir = new File(destination);
		if (dir.exists()) {
			File[] filesInDir = dir.listFiles();
			int i = 0;
			for (File files : filesInDir) {
				i++;
				String fileName = file.getName();
				String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);
				String fileExtension = FilenameUtils.getExtension(fileName);
				String newName = fileNameWithoutExtension + "_" + i + "." + fileExtension;
				String newPath = destination + File.separator + newName;
				files.renameTo(new File(newPath));
			}
		}

		FileUtils.moveFileToDirectory(file, new File(destination), true);
		LOGGER.info("Moved the file " + file.getName() + " to path " + destination);
	}

	/**
	 * Returns the xls file that needs to be loaded into DB. This method expects
	 * only one file at the path. If the path contains no or more than one file
	 * it returns null.
	 * 
	 * @param path
	 *            Path of the xls file that needs to be loaded.
	 * @return {@link File} that needs to be loaded into the Database.
	 *         <code>null</code> If no or more than one file found at the Path.
	 */
	private static File getFilesFromPath(final String path, final String[] extensions) throws Exception{

		File file = new File(path);

		FilenameFilter fileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				boolean fileExtensionMatched = false;
				for(String extension : extensions) {
					if(name.endsWith(extension)) {
						fileExtensionMatched = true;
						break;
					}
				}
				
				return fileExtensionMatched;
			}
		};

		File[] files = file.listFiles(fileNameFilter);
		if (files != null && files.length > 1) {
			LOGGER.info("Found two xls file in the input path : " + path);
		} else if (files != null && files.length == 0) {
			LOGGER.info("Found no xls file in the input path : " + path);
		} else if (files == null) {
			LOGGER.info("Found no xls file in the input path : " + path);
		} else {
			return files[0];
		}

		return null;

	}
	
	private static File[] getFaxesForProcessing(final String path, final String[] extensions) throws Exception{
		File file = new File(path);
		FilenameFilter fileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				boolean fileExtensionMatched = false;
				for(String extension : extensions) {
					if(name.toUpperCase().endsWith(extension.toUpperCase())) {
						fileExtensionMatched = true;
						break;
					}
				}
				
				return fileExtensionMatched;
			}
		};
		
		File[] files = file.listFiles(fileNameFilter);
		if(files == null) {
			return new File[]{};
		}
		return files;
	}

}
