package com.jpmc.rdt.docmgmt.database.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jpmc.rdt.docmgmt.excel.loader.model.DBModel;
import com.jpmc.rdt.docmgmt.excel.loader.model.DBModels;
import com.jpmc.rdt.docmgmt.excel.loader.model.Mapping;
import com.jpmc.rdt.docmgmt.ta.model.EmailAudit;
import com.jpmc.rdt.docmgmt.ta.model.FaxData;
import com.jpmc.rdt.docmgmt.ta.model.FaxDetails;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;
import com.jpmc.rdt.docmgmt.ta.model.FaxFileDetails;

@Repository("taDao")
public class TransferAgencyDao implements ITransferAgencyDao {

	private static final Logger LOGGER = Logger.getLogger(TransferAgencyDao.class);

	public static final String BATCHSTATUSOPEN = "OPEN";
	public static final String BATCHSTATUSINPROGRESS = "INPROGRESS";
	public static final String BATCHSTATUSCOMPLETE = "COMPLETE";
	public static final String BATCHSTATUSFAILURE = "FAILURE";

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void init() {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public String getPrimaryAccount(final String secondaryAccount) {
		String primaryAccount = jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(
						"select primaryAccountNo from datacap.acct_mapping where secondaryAccoutNo = ?");
				ps.setString(1, secondaryAccount);
				return ps;
			}

		}, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if (rs.next()) {
					return rs.getString(1);
				} else {
					return null;
				}
			}
		});
		
		return primaryAccount;
		
	}
	
	@Override
	public String getEmail(final String accountNo) throws SQLException {

		String primaryAccount = jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(
						"select primaryAccountNo from datacap.acct_mapping where secondaryAccoutNo = ?");
				ps.setString(1, accountNo);
				return ps;
			}

		}, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if (rs.next()) {
					return rs.getString(1);
				} else {
					return null;
				}
			}
		});

		if (primaryAccount != null) {
			LOGGER.info("Got primary account "+ primaryAccount +" for secondary account " + accountNo);
			return getEmailFromAcct(primaryAccount);
		} else {
			return getEmailFromAcct(accountNo);
		}

	}

	@Override
	public String getEmailFromAcct(final String accountNo) {

		String emailAddress = jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn
						.prepareStatement("select emailid from datacap.fax_acct_email_master where accountno = ?");
				ps.setString(1, accountNo);
				return ps;
			}
		}, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				pstmt.execute();
				ResultSet rs = pstmt.getResultSet();
				if(rs.next())
					return rs.getString(1);
				else throw new SQLException("No email id found for account " + accountNo);
			}
		});

		LOGGER.info("EMail address for account " + accountNo + " is " + emailAddress);
		
		return emailAddress;
	}

	@Override
	public List<FaxData> getBatchList() throws SQLException {

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			String query = "select BATCHID, countofpages from DATACAP.FAX_DATA fd where STATUS= ? order by creationdate";

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
				ps.setString(1, BATCHSTATUSOPEN);
				return ps;
			}
		};

		List<FaxData> faxData = jdbcTemplate.query(creator, new RowMapper<FaxData>() {

			@Override
			public FaxData mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				FaxData faxDataRow = new FaxData();
				faxDataRow.setBatchId(resultSet.getString(1));
				faxDataRow.setCountOfPages(resultSet.getInt(2));
				return faxDataRow;
			}
		});
		return faxData;
	}

	@Override
	public void updateBatchStatus(String batchId, String status) throws SQLException {

		String query = "update datacap.fax_data set status = ? where batchid = ? ";
		int i = jdbcTemplate.update(query, status, batchId);
		if (i == 0) {
			throw new SQLException("No rows updated in fax_data for batchId " + batchId + " status " + status);
		}
	}

	@Override
	public void updateBatchErrorCode(String batchId, String status, String errorCode, String errorDesc) {

		String query = "update datacap.fax_data set status = ?, ErrorCode = ?, ErrorDescription = ? where batchid = ? ";
		jdbcTemplate.update(query, status, errorCode, errorDesc, batchId);
	}
	
	@Override
	public void updateFaxDataDetails(String batchId, String status) {

		String query = "update DATACAP.FAX_DATA_DETAILS set isProcessed = ? where batchid = ? ";
		jdbcTemplate.update(query, status, batchId);
	}
	
	@Override
	public int checkExceptionBatch(final String batchId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			PreparedStatementCreator creator = new PreparedStatementCreator() {
				String query = "select count(1) from DATACAP.FAX_DATA_DETAILS where BATCHID=? and isValidPage='N' ";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
					ps.setString(1, batchId);
					return ps;
				}
			};
			ps = creator.createPreparedStatement(jdbcTemplate.getDataSource().getConnection());
			rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	/*@Override
	public List<String> getAccountsForBatch(final String batchId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<String> accounts = new ArrayList<>();
		try {

			PreparedStatementCreator creator = new PreparedStatementCreator() {
				String query = "select distinct AccountNo from DATACAP.FAX_DATA_DETAILS where BATCHID=? and isValidPage='Y' and LEN(RTRIM(AccountNo)) > 0 ";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
					ps.setString(1, batchId);
					return ps;
				}
			};
			ps = creator.createPreparedStatement(jdbcTemplate.getDataSource().getConnection());
			rs = ps.executeQuery();
			while (rs.next()) {
				accounts.add(rs.getString(1));
			}
			return accounts;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}*/
	
	@Override
	public List<String> getAccountsForBatch(final String batchId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("batchId", batchId);
			RowMapper<String> rowMapper = new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int arg1) throws SQLException {
					return rs.getString(1);
				}
			};
			//List<Integer> count = getNamedJdbcTemplate().query("select count(distinct AccountNo) from DATACAP.FAX_ACCT_EMAIL_MASTER where AccountNo IN (:accounts)", parameters, rowMapper);
			
			List<String> accounts = getNamedJdbcTemplate().query("select distinct AccountNo from DATACAP.FAX_DATA_DETAILS where BATCHID=(:batchId) and isValidPage='Y' and LEN(RTRIM(AccountNo)) > 0 ", parameters, rowMapper);
			
			return accounts;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	
	@Override
	public int getEmailsForAccounts(final List<String> accounts) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("accounts", accounts);
			RowMapper<Integer> rowMapper = new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
					return rs.getInt(1);
				}
			};
			List<Integer> count = getNamedJdbcTemplate().query("select count(distinct AccountNo) from DATACAP.FAX_ACCT_EMAIL_MASTER where AccountNo IN (:accounts)", parameters, rowMapper);
			
			return count.get(0);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	@Override
	public List<FaxDetails> getFaxDetails(final String batchId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("batchId", batchId);
			RowMapper<FaxDetails> rowMapper = new RowMapper<FaxDetails>() {
				
				@Override
				public FaxDetails mapRow(ResultSet rs, int arg1) throws SQLException {
					FaxDetails faxDetailRow = new FaxDetails();
					faxDetailRow.setBatchId(rs.getString(1));
					faxDetailRow.setPageNumber(rs.getInt(2));
					faxDetailRow.setAccountNumber(rs.getString(3));
					faxDetailRow
							.setCoverPage((rs.getString(4) != null && rs.getString(4).trim().equals("Y"))
									? true : false);
					faxDetailRow.setValidAccountNumber(
							(rs.getString(5) != null && rs.getString(5).trim().equals("Y")) ? true
									: false);
					return faxDetailRow;
				}
			};
			List<FaxDetails> faxDetails = getNamedJdbcTemplate().query("select BATCHID, PAGENUMBER, ACCOUNTNO, ISCOVERPAGE, ISVALIDPAGE from DATACAP.FAX_DATA_DETAILS where BATCHID=(:batchId) and ISPROCESSED='N' order by pagenumber", parameters, rowMapper);
			
			return faxDetails;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	/*@Override
	public List<FaxDetails> getFaxDetails(final String batchId) throws SQLException {

		PreparedStatement ps = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				String query = "select BATCHID, PAGENUMBER, ACCOUNTNO, ISCOVERPAGE, ISVALIDPAGE from DATACAP.FAX_DATA_DETAILS where BATCHID=? and ISPROCESSED='N' order by pagenumber";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
					ps.setString(1, batchId);
					return ps;
				}
			};
			ps = creator.createPreparedStatement(jdbcTemplate.getDataSource().getConnection());

			List<FaxDetails> faxDetails = jdbcTemplate.query(creator, new RowMapper<FaxDetails>() {

				@Override
				public FaxDetails mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					FaxDetails faxDetailRow = new FaxDetails();
					faxDetailRow.setBatchId(resultSet.getString(1));
					faxDetailRow.setPageNumber(resultSet.getInt(2));
					faxDetailRow.setAccountNumber(resultSet.getString(3));
					faxDetailRow
							.setCoverPage((resultSet.getString(4) != null && resultSet.getString(4).trim().equals("Y"))
									? true : false);
					faxDetailRow.setValidAccountNumber(
							(resultSet.getString(5) != null && resultSet.getString(5).trim().equals("Y")) ? true
									: false);
					return faxDetailRow;
				}
			});
			return faxDetails;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}*/

	@Override
	public void batchInsert(DBModel dbModel, List<Mapping> mappings, final List<DBModels> values) {
		LOGGER.info("Insertion into DB started...");
		String sql = prepareInsertQuery(dbModel, mappings);
		//LOGGER.info("Insert query used : " + sql);
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int arg1) throws SQLException {
				DBModels dbModels = values.get(arg1);
				int index = 1;
				List<DBModel> dbColumns = dbModels.getDbModels();
				
				for (DBModel dbModel : dbColumns) {

					String value = "";
					String dataType = dbModel.getDbColumn().getDataType();
					switch (dataType) {
					case "varchar":
						if (dbModel.getValue() == null)
							ps.setString(index, "");
						else
							LOGGER.debug(dbModel.getDbColumn().getValue() + " : " + dbModel.getValue().length());
							ps.setString(index, dbModel.getValue());
							break;
					case "int":
						LOGGER.debug(dbModel.getDbColumn().getValue() + " : " + dbModel.getValue().length());
						value = dbModel.getValue();
						Integer valueInt = Integer.parseInt(value);
						ps.setInt(index, valueInt);
						break;
					case "date":
						// TODO: Convert the input date into sql date
						break;
					case "long":
						LOGGER.debug(dbModel.getDbColumn().getValue() + " : " + dbModel.getValue().length());
						value = dbModel.getValue();
						Long longValue = Long.getLong(value);
						ps.setLong(index, longValue);
						break;
					case "decimal":
						LOGGER.debug(dbModel.getDbColumn().getValue() + " : " + dbModel.getValue().length());
						value = dbModel.getValue();
						Double doubleValue = Double.parseDouble(value);
						ps.setDouble(index, doubleValue);
						break;
					case "timestamp":
						// TODO: Covert input string to sql timestamp
						break;
					}
					index++;
				}
			}

			@Override
			public int getBatchSize() {
				return values.size();
			}
		});
	}

	@Override
	public void emailAudit(EmailAudit emailAudit) {

		String sql = "insert into DATACAP.EMAIL_AUDIT (Accountno, emailid, emailsubject, emailbody, datesent, batchid) "
				+ " values (?,?,?,?,?,?) ";

		jdbcTemplate.update(sql, emailAudit.getAccountNo(), emailAudit.getEmailId(), emailAudit.getEmailSubject(),
				emailAudit.getEmailBody(), emailAudit.getDateSent(), emailAudit.getBatchId());

	}

	@Override
	public void deleteData(final String schema, final String tableName) {
		LOGGER.info("Deleting data from table" + tableName);
		jdbcTemplate.execute("delete from " + schema + "." + tableName);
	}

	/**
	 * Prepares the insert query to be used for {@link PreparedStatement}.
	 * 
	 * @param dbModel
	 *            Contains details for database like schema and table name.
	 * @param mappings
	 *            List containing mapping details i.e. XLS Column to DB Column
	 *            map.
	 * @return The insert query.
	 */
	private String prepareInsertQuery(DBModel dbModel, List<Mapping> mappings) {
		String query = "insert into " + dbModel.getSchemaName() + "." + dbModel.getTableName() + "(";
		StringBuilder queryStr = new StringBuilder(query);
		int count = 1;

		for (Mapping mapping : mappings) {
			queryStr.append(mapping.getDbColumn().getValue());
			if (count < mappings.size())
				queryStr.append(",");
			count++;
		}

		queryStr.append(") values (");

		for (int i = 0; i < mappings.size(); i++) {
			queryStr.append("?");
			if (i + 1 < mappings.size())
				queryStr.append(",");
		}
		queryStr.append(")");
		return queryStr.toString();
	}

	
	@Override
	public long saveFileFax(final FaxFile faxFile) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into DATACAP.FAX_FILE (APPLICATION_NAME, FILE_NAME, FILE_PATH, FILE_CREATION_DATE, STATUS, FILE_MODIFICATION_DATE) values (?, ?, ?, ?, ?, ?) ";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, faxFile.getApplicationName());
				ps.setString(2, faxFile.getFileName());
				ps.setString(3, faxFile.getFilePath());
				ps.setTimestamp(4, faxFile.getFileCreationDate());
				ps.setString(5, faxFile.getStatus());
				ps.setTimestamp(6, faxFile.getFileModificationDate());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
		
	}
	
	@Override
	public void saveFileFaxDetails(final FaxFileDetails faxFileDetail) {
		final String sql = "insert into DATACAP.FAX_FILE_DETAILS (FILE_ID, BATCH_ID, DOCUMENT_ID, APPLICATION_NAME, PAGE_COUNT) values (?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1, faxFileDetail.getFileID());
				preparedStatement.setString(2, faxFileDetail.getBatchId());
				preparedStatement.setString(3, faxFileDetail.getDocumentId());
				preparedStatement.setString(4, faxFileDetail.getApplicationName());
				preparedStatement.setInt(5, faxFileDetail.getPageCount());
				return preparedStatement;
			}
		});
	}
	
	@Override
	public void updateFaxFileStatus(long fileId, String status) {

		String query = "update DATACAP.FAX_FILE set status = ? where FILE_ID = ? ";
		jdbcTemplate.update(query, status, fileId);
	}
	
	@Override
	public void updateFaxFileStatusError(long fileId, String errorCode, String errorDesc, String status) {

		String query = "update DATACAP.FAX_FILE set ERROR_CODE = ?, ERROR_DESC=?, STATUS=? where FILE_ID = ? ";
		jdbcTemplate.update(query, errorCode, errorDesc, status, fileId);
	}
	
	@Override
	public int getFaxFiles(final String fileName, final String applicationName) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("fileName", fileName);
			parameters.addValue("applicationName", applicationName);
			RowMapper<Integer> rowMapper = new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
					return rs.getInt(1);
				}
			};
			List<Integer> count = getNamedJdbcTemplate().query("select count(1) from DATACAP.FAX_FILE where FILE_NAME = (:fileName) and APPLICATION_NAME=(:applicationName) and CONVERT(date, CREATION_TIMESTAMP) = CONVERT(date, GETDATE()) and STATUS in ('OPEN','PROCESSING','BATCH_CREATED')", parameters, rowMapper);
			
			return count.get(0);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	@Override
	public int updatePrimaryAccountNo(final String batchId) {
		String sql = "update DATACAP.FAX_DATA_DETAILS "
						+ "	set AccountNo = (select PrimaryAccountNo from DATACAP.ACCT_MAPPING where SecondaryAccoutNo = accountno ) "
						+ " where batchid= ? and exists (select 1 from DATACAP.ACCT_MAPPING where SecondaryAccoutNo = accountno)";
		return jdbcTemplate.update(sql, batchId);
	}
	
	
}
