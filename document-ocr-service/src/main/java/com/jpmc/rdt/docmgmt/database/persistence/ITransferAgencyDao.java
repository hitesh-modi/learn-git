package com.jpmc.rdt.docmgmt.database.persistence;

import java.sql.SQLException;
import java.util.List;

import com.jpmc.rdt.docmgmt.excel.loader.model.DBModel;
import com.jpmc.rdt.docmgmt.excel.loader.model.DBModels;
import com.jpmc.rdt.docmgmt.excel.loader.model.Mapping;
import com.jpmc.rdt.docmgmt.ta.model.EmailAudit;
import com.jpmc.rdt.docmgmt.ta.model.FaxData;
import com.jpmc.rdt.docmgmt.ta.model.FaxDetails;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;
import com.jpmc.rdt.docmgmt.ta.model.FaxFileDetails;

public interface ITransferAgencyDao {

	public List<FaxDetails> getFaxDetails(String batchId) throws SQLException;
	public void batchInsert(DBModel dbModel, List<Mapping> mappings, List<DBModels> values);
	public void deleteData(final String schema, final String tableName);
	public String getEmail(final String accountNo) throws SQLException;
	public List<FaxData> getBatchList() throws SQLException ;
	public void updateBatchStatus(String batchId, String status) throws SQLException;
	public int checkExceptionBatch(String batchId) throws SQLException;
	public void emailAudit(EmailAudit emailAudit);
	public void updateBatchErrorCode(String batchId, String status, String errorCode, String errorDesc);
	public void updateFaxDataDetails(String batchId, String status);
	public int getEmailsForAccounts(List<String> accounts) throws SQLException;
	public List<String> getAccountsForBatch(String batchId) throws SQLException;
	public long saveFileFax(FaxFile faxFile);
	public void saveFileFaxDetails(FaxFileDetails faxFileDetail);
	public void updateFaxFileStatus(long fileId, String status);
	public void updateFaxFileStatusError(long fileId, String errorCode, String errorDesc, String status);
	public int getFaxFiles(String fileName, String applicationName) throws SQLException;
	public String getPrimaryAccount(String secondaryAccount);
	public String getEmailFromAcct(String accountNo);
	public int updatePrimaryAccountNo(String batchId);
	
}
