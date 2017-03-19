package com.jpmc.rdt.docmgmt.ocr.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.jpmc.rdt.docmgmt.database.persistence.ITransferAgencyDao;
import com.jpmc.rdt.docmgmt.excel.loader.model.Application;
import com.jpmc.rdt.docmgmt.excel.loader.model.DBExcelMap;
import com.jpmc.rdt.docmgmt.excel.loader.model.DBModel;
import com.jpmc.rdt.docmgmt.excel.loader.model.DBModels;
import com.jpmc.rdt.docmgmt.excel.loader.model.Mapping;
import com.jpmc.rdt.docmgmt.excel.loader.model.SheetColumn;
import com.jpmc.rdt.docmgmt.excel.loader.model.SheetsMapping;
import com.jpmc.rdt.docmgmt.excel.loader.util.XmlConfigLoader;
import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ta.model.EmailAudit;
import com.jpmc.rdt.docmgmt.ta.model.FaxData;
import com.jpmc.rdt.docmgmt.ta.model.FaxDetails;

/**
 * @author Hitesh Modi This class acts as a service later implementation for Fax
 *         Acknowledgement Utility.
 */
@Service("faxAcknowledgeService")
public class TAFaxAcknowledgementService implements ITAFaxAcknowledgementService {


	private static final Logger LOGGER = Logger.getLogger(TAFaxAcknowledgementService.class);
	
	@Resource(name="taDao")
	private ITransferAgencyDao faxAcknowledgeDao;
	
	@Value("${faxacknowledge.email.content}")
	private String emailContent;
	
	@Resource(name="mailSender")
	private JavaMailSenderImpl mailSender;
	
	@Value("${faxacknowledge.email.sender}")
	private String sender;
	
	@Value("${faxacknowledge.email.exception}")
	private String exceptionQueueEmail;
	
	@Value("${faxacknowledge.email.subject}")
	private String emailSubject;
	
	@Value("${faxacknowledge.email.cc}")
	private String emailCCAddress;
	
	@Value("${faxacknowledge.email.bcc}")
	private String emailBCCAddress;
	
	@Value("${faxacknowledge.email.content.disclaimer}")
	private String emailDisclaimer;
	
	@Value("${faxacknowledge.email.exception.subject}")
	private String emailSubjectException;
	
	@Value("${faxacknowledge.email.exception.content}")
	private String emailExceptionContent;
	
	@Value("${faxacknowledge.email.exception.emailDoesNotExist}")
	private String emailForExceptionForEmailDoesNotExist;

	@Override
	@Transactional
	public void loadExcelSheetToDatabase(File inputFile, String applicationId) throws OcrServiceException {
		LOGGER.info("Loading file " + inputFile.getAbsolutePath() + " into DB.");
		DBModel dbModel = new DBModel();
		SheetsMapping sheetsMapping = XmlConfigLoader.getSheetMapping();
		List<Mapping> mappings = null;
		int sheetIndex=0;
		for (Application application : sheetsMapping.getApplication()) {
			if (application.getId().equals(applicationId)) {
				dbModel.setDbName(application.getDb());
				dbModel.setSchemaName(application.getSchema());
				dbModel.setTableName(application.getTable());
				 mappings = application.getMapping();
				 sheetIndex=application.getSheetIndex();
			}
		}

		try {
			
			List<DBModels> emailMasterData = getDBModelFromXls(inputFile, dbModel,
					mappings,sheetIndex);
			LOGGER.info("Retrieved values from xls successfully. Number for rows to insert " + emailMasterData.size());
			faxAcknowledgeDao.deleteData(dbModel.getSchemaName(), dbModel.getTableName());
			faxAcknowledgeDao.batchInsert(dbModel, mappings, emailMasterData);
			
			
		} catch (FileNotFoundException e) {
			throw new OcrServiceException("File Not Found Exception in loadExcelSheetToDatabase method", e);
		} catch (IOException e) {
			throw new OcrServiceException("IO Exception in loadExcelSheetToDatabase method", e);
		} catch (EncryptedDocumentException e) {
			throw new OcrServiceException("EncryptedDocumentException Exception in loadExcelSheetToDatabase method", e);
		} catch (InvalidFormatException e) {
			throw new OcrServiceException("InvalidFormatException Exception in loadExcelSheetToDatabase method", e);
		} catch(Exception e) {
			throw new OcrServiceException("Some general Exception in method loadExcelSheetToDatabase", e);
		}
		LOGGER.info("Excel " + inputFile.getAbsolutePath() + " was loaded successfully");
	}


	private List<DBModels> getDBModelFromXls(File emailMasterFile, DBModel dbModel,
			List<Mapping> mappings , int sheetIndex)
			throws Exception {

		LOGGER.info("Reading values from xls and then mapping it to the DB Columns.");
		Workbook workbook = WorkbookFactory.create(emailMasterFile);



		Sheet sheet = workbook.getSheetAt(sheetIndex);
		Iterator<Row> rowIterator = sheet.rowIterator();
		Iterator<Cell> cellIterator;
		
		Row row;
		List<DBModels> valuesToInsert = new ArrayList<>();
		List<DBExcelMap> dbExcelMappingList = new ArrayList<>();
		while (rowIterator.hasNext()) {

			row = rowIterator.next();
			if(row == null) {
				continue;
			}
			List<DBModel> dbModelsForInsert = new ArrayList<>();

			if (row.getRowNum() == 0) {
				
				for(Mapping mapping : mappings) {
					cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						if(cell == null) {
							continue;
						} 
						String columnHeader = mapping.getSheetColumn().getValue();
						if(StringUtils.isNotEmpty(columnHeader)){
							columnHeader=columnHeader.replace(" ", "").toUpperCase().trim();
						}
						
						DBExcelMap dbExcelMap = new DBExcelMap();
						String excelValue=cell.getStringCellValue();
						
						if(StringUtils.isNotEmpty(excelValue)){
							 excelValue=cell.getStringCellValue().toUpperCase().replace("\n", " ").replace(" ", "").trim();
						}
						
						if (StringUtils.isNotEmpty(columnHeader)&&columnHeader.equals(excelValue)) {

							//sheetToDBMap.put(cell.getColumnIndex(), mapping.getDbColumn());

							LOGGER.debug("Adding column :  " + columnHeader);
							dbExcelMap.setDatabaseColumn(mapping.getDbColumn());
							dbExcelMap.setIndexOfExcelColumn(cell.getColumnIndex());
							dbExcelMap.setSheetColumn(mapping.getSheetColumn());
							dbExcelMappingList.add(dbExcelMap);
							break;
						} else if(StringUtils.isEmpty(columnHeader)) {
							// Sheet Mapping will be empty for the columns which we need to populate default values.
							// Put column index as -1 for which we need to populate default value.
							dbExcelMap.setDatabaseColumn(mapping.getDbColumn());
							dbExcelMap.setIndexOfExcelColumn(-1);
							dbExcelMappingList.add(dbExcelMap);
							break;
						}
					}
				}
				
			}
			else {
				for(DBExcelMap dbExcelMap : dbExcelMappingList) {
					DBModel dbModelForInsert = new DBModel();
					Integer columnIndex = dbExcelMap.getIndexOfExcelColumn();
					SheetColumn sheetColumn = dbExcelMap.getSheetColumn();
					if(columnIndex == -1) {
						dbModelForInsert.setValue(dbExcelMap.getDatabaseColumn().getDefaultValue());
					}
					else {
						Cell cell = row.getCell(columnIndex);
						if(cell != null) {
							switch (cell.getCellType()) {
			                case Cell.CELL_TYPE_STRING:
			                	dbModelForInsert.setValue(cell.getStringCellValue());
			                    break;
			                case Cell.CELL_TYPE_NUMERIC:
			                    if (DateUtil.isCellDateFormatted(cell)) {
			                    	dbModelForInsert.setValue(cell.getDateCellValue().toString());
			                    } else {
									if(sheetColumn.getDataType().equals("string")) {
			                    		cell.setCellType(Cell.CELL_TYPE_STRING);
			                    		String cellValue = cell.getStringCellValue();
			                    		dbModelForInsert.setValue(cellValue);
			                    	}
			                    		
			                    	else {
			                    		double cellValue = cell.getNumericCellValue();
			                    		dbModelForInsert.setValue(String.format("%1$.2f", cellValue));
			                    	}
			                    		
			                    }
			                    break;
			                case Cell.CELL_TYPE_BOOLEAN:
			                	dbModelForInsert.setValue(Boolean.toString(cell.getBooleanCellValue()));
			                    break;
			                default:
			            }
						}
						
					}
					
					
					dbModelForInsert.setDbColumn(dbExcelMap.getDatabaseColumn());
					dbModelsForInsert.add(dbModelForInsert);
				}
				DBModels dbModels = new DBModels();
				dbModels.setDbModels(dbModelsForInsert);
				valuesToInsert.add(dbModels);
			}
			
			
			
		}
		workbook.close();
		LOGGER.info("Created mapping from excel to DB successfully");
		return valuesToInsert;

	}
	
	@Override
	@Transactional
	public void updateStatusOftheBatch(String batchId, String status) throws OcrServiceException {
		try {
			LOGGER.info("Updating the status of batch : " + batchId + " to " + status);
			faxAcknowledgeDao.updateBatchStatus(batchId, status); 
			LOGGER.info("Status of the batch " + batchId + " updated successfully to " + status);
		} catch (SQLException e) {
			throw new OcrServiceException("SQL Exception while executing update status", e);
		} catch (Exception e) {
			throw new OcrServiceException("General exception occurred while executing update status", e);
		}
	}
	
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public List<FaxData> getFaxDataInOpenState() throws OcrServiceException{
		List<FaxData> faxData = new ArrayList<>();
		try {
			faxData = faxAcknowledgeDao.getBatchList();
		} catch (SQLException e) {
			throw new OcrServiceException("SQL Exception while fetching fax details", e);
		} catch (Exception e) {
			throw new OcrServiceException("General Exception while fetching fax details", e);
		}
		return faxData;
	}
	
	
	@Override
	@Async("taskExecutor")
	@Transactional
	public void processEmails(String batchId) throws OcrServiceException{
		
		try {
			int count = faxAcknowledgeDao.checkExceptionBatch(batchId);
			
			LOGGER.info("Updating primary account numbers in the database table");
			int updateCount = updatePrimaryAccount(batchId);
			LOGGER.info(updateCount + " Rows updated for primary account number.");
			
			List<String> accounts = faxAcknowledgeDao.getAccountsForBatch(batchId);
			int emailIdCount = 0;
			if(accounts != null && !accounts.isEmpty()) {
				emailIdCount = faxAcknowledgeDao.getEmailsForAccounts(accounts);
			}
			
			if (count > 0){
				LOGGER.info("Since batch " + batchId + " contains one or more invalid page, hence sending it to exception mails list.");
				sendEmailToAccounts(batchId, "", 0, false, true, emailExceptionContent);
			}
			else if(accounts != null && accounts.size() != emailIdCount) {
				LOGGER.info("Email id does not exists for any one account in the batch, hence sending to exception mail");
				sendEmailToAccounts(batchId, "", 0, false, true, emailForExceptionForEmailDoesNotExist);
			}
			else {
				
				List<FaxDetails> faxDetails = faxAcknowledgeDao.getFaxDetails(batchId);
				boolean isCoverPage = faxDetails.get(0).isCoverPage();
				
				LinkedMap accountNoPage = new LinkedMap();
				String previousAccount = null;
				for(FaxDetails fd: faxDetails){	
					
					if(isCoverPage && fd.getPageNumber() ==1){ // to check if there is cover page
						continue;
					} else {
						Integer pageCount = 0;
						
						if(!StringUtils.isEmpty(fd.getAccountNumber())){
							
							previousAccount = fd.getAccountNumber();
							pageCount = (Integer) accountNoPage.get(fd.getAccountNumber());
							
							if(pageCount == null){
								
								if(isCoverPage && fd.getPageNumber()==2){
									accountNoPage.put(fd.getAccountNumber(), 2); //if coversheet then add 2pages
								} else {
									accountNoPage.put(fd.getAccountNumber(), 1);// if no coversheet then only 1 page
								}
							} else {
								++pageCount;
								// Replace existing value
								accountNoPage.put(fd.getAccountNumber(),pageCount);					
							}
							
						} else {
							pageCount = (Integer) accountNoPage.get(previousAccount);
							++pageCount;
							// Replace existing value
							accountNoPage.put(fd.getAccountNumber(),pageCount);	
						}			
						
					}			
				}
				accountNoPage.size();
				MapIterator iterator = accountNoPage.mapIterator();
				int mapCount = 0;
				while(iterator.hasNext()) {
					String accountNumber = (String)iterator.next();
					Integer numberOfPages = (Integer)iterator.getValue();
					if(mapCount == 0 && isCoverPage) {
						sendEmailToAccounts(batchId, accountNumber, numberOfPages, true, false, "");
					}
					else {
						sendEmailToAccounts(batchId, accountNumber, numberOfPages, false, false, "");
					}
					mapCount++;
				}
			}
			
			
			updateStatusOftheBatch(batchId, "COMPLETE");
			LOGGER.info("Updating isProcessed flag in FAX_DATA_DETAILS to Y for batch " + batchId);
			faxAcknowledgeDao.updateFaxDataDetails(batchId, "Y");
			
		} catch (SQLException e) {
			faxAcknowledgeDao.updateBatchErrorCode(batchId, "FAILURE", "FACK001", e.getMessage());
			try {
				sendEmailToAccounts(batchId, "", 0, false, true, emailExceptionContent);
			} catch (Exception e1) {
				LOGGER.info("Exception while sending email");
			}
			throw new OcrServiceException("SQL Exception in processEmails for batch " + batchId, e);
		} catch(Exception e) {
			faxAcknowledgeDao.updateBatchErrorCode(batchId, "FAILURE", "FACK002", e.getMessage());
			try {
				sendEmailToAccounts(batchId, "", 0, false, true, emailExceptionContent);
			} catch (Exception e1) {
				LOGGER.info("Exception while sending email");
			}
			throw new OcrServiceException("General Exception in processEmails for batch " + batchId, e);
		}	
	}
	
	@Transactional
	private int updatePrimaryAccount(String batchId) throws Exception{
		return faxAcknowledgeDao.updatePrimaryAccountNo(batchId);
	}
	
	@Transactional
	private void sendEmailToAccounts(String batchId, String accountNumber, int numberOfPages, boolean isCoverPage, boolean isExceptionEmail, String exceptionContent) throws SQLException, MessagingException {
		LOGGER.info("Fetching email id for account number : " + accountNumber);
		String[] emailAddress = null;
		if (!isExceptionEmail) {
			emailAddress = new String[1];
			emailAddress[0] = faxAcknowledgeDao.getEmailFromAcct(accountNumber);
		} 
		else {
			emailAddress = exceptionQueueEmail.split(",");
		}
		LOGGER.info("Email address for account number " + accountNumber + " is " + Arrays.asList(emailAddress));
		String numberString = "";
		String subject="";
		String coverPage = "";
		if(numberOfPages > 1) {
			numberString = "(s)";
		} 
		if(isCoverPage)
			coverPage = "includes cover sheet";
		
		String contentForEmail = "";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(sender);
		if (!isExceptionEmail) {
			subject = emailSubject;
			contentForEmail = String.format(emailContent,Integer.toString(numberOfPages), numberString, coverPage);
		} else {
			subject = String.format(emailSubjectException, batchId);
			contentForEmail = exceptionContent;
		}
		
		StringBuilder contentBuilder = new StringBuilder(contentForEmail);
		contentBuilder.append("\n");
		contentBuilder.append("\n");
		contentBuilder.append("\n");
		contentBuilder.append(emailDisclaimer);
		
		LOGGER.info("Email Address : " + Arrays.asList(emailAddress));
		LOGGER.info("Content : " + contentBuilder);
		LOGGER.info("Subject : " + subject);
		mailMessage.setTo(emailAddress);
		mailMessage.setText(contentBuilder.toString());
		if(emailCCAddress != null && emailCCAddress.trim().length() > 0) {
			String[] ccAddresses = emailCCAddress.split(",");
			LOGGER.info("Setting CC email Address : " + emailCCAddress);
			mailMessage.setCc(ccAddresses);
		}
		if(emailBCCAddress != null && emailBCCAddress.trim().length() > 0){
			String[] bccAddresses = emailBCCAddress.split(",");
			LOGGER.info("Setting BCC email Address : " + emailBCCAddress);
			mailMessage.setBcc(bccAddresses);
		}
		
		mailMessage.setSubject(subject);
		mailSender.send(mailMessage);
		LOGGER.info("Mail sent successfully to account number " + accountNumber);
		LOGGER.info("Saving the mail details into EMAIL_AUDIT");
		EmailAudit emailAudit = new EmailAudit();
		emailAudit.setAccountNo(accountNumber);
		emailAudit.setEmailBody(contentForEmail);
		emailAudit.setEmailId(Arrays.asList(emailAddress).toString());
		emailAudit.setEmailSubject(subject);
		emailAudit.setDateSent(new java.sql.Date(new Date().getTime()));
		emailAudit.setBatchId(batchId);
		faxAcknowledgeDao.emailAudit(emailAudit);
	}

}
