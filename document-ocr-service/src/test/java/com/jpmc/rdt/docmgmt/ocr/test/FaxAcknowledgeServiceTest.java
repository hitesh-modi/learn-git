package com.jpmc.rdt.docmgmt.ocr.test;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jpmc.rdt.docmgmt.database.persistence.ITransferAgencyDao;
import com.jpmc.rdt.docmgmt.ocr.service.ITAFaxAcknowledgementService;
import com.jpmc.rdt.docmgmt.ocr.test.spring.OcrTestSpringConfiguration;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;


/**
 * Test Class for testing Fax Acknowledge Service
 * @author Hitesh Modi
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { OcrTestSpringConfiguration.class })
@ActiveProfiles(profiles = "local" )
@WebAppConfiguration
public class FaxAcknowledgeServiceTest {
	
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="taDao")
	private ITransferAgencyDao faxAckDao;
	
	@Autowired
	public void init(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Resource(name="faxAcknowledgeService")
	private ITAFaxAcknowledgementService faxAcknowledgeService;
	
	@Test
	public void testEmailMasterFileProcessing() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/EmailMaster1.xlsx");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"fax_acknowledgement_email_master");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.FAX_ACCT_EMAIL_MASTER");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{1019}, new int[]{rowCountInserted});
	}
	
	@Test
	public void testPrimaryAccountFileProcessing() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/PrimaryAccountData.xlsx");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"fax_acknowledgement_primary_account_mapping");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.ACCT_MAPPING");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{30}, new int[]{rowCountInserted});
	}
	
	@Test
	public void testBAUValidationSheet() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/LUX_BAU_Validation.xls");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"LUX_BAU_VALIDATION");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.LUX_BAU_VALIDATION");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{1571}, new int[]{rowCountInserted});
	}
	
	@Test
	public void testInvestorValidation() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/Investor.xls");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"LUX_INVESTORS_VALIDATION");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.Lux_Inverstors_Validation");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{2239}, new int[]{rowCountInserted});
	}
	
	
	
	@Test
	public void testQuickReference() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/Quick Reference Mapping File.xls");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"QUICK_REFERENCE");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.QUICK_REFERENCE");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{3349}, new int[]{rowCountInserted});
	
	}
	
	
	@Test
	public void testFundMapping() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();	
		URL url = classLoader.getResource("testFiles/Quick Reference Mapping File.xls");
		File file = new File(url.toURI());
		faxAcknowledgeService.loadExcelSheetToDatabase(file,"FUND_MAPPING");
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) from DATACAP.FUND_MAPPING");
		rowSet.next();
		Integer rowCountInserted = rowSet.getInt(1);
		Assert.assertArrayEquals(new int[]{2420}, new int[]{rowCountInserted});
	
	}
	
	@Test
	public void testfaxAcknowledge() throws Exception{
		faxAcknowledgeService.processEmails("20170316.000008");
	}
	
}
