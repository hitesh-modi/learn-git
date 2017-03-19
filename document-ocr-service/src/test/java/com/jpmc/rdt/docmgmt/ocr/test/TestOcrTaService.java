package com.jpmc.rdt.docmgmt.ocr.test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jpmc.rdt.docmgmt.database.persistence.ITransferAgencyDao;
import com.jpmc.rdt.docmgmt.ocr.model.OcrDataModel;
import com.jpmc.rdt.docmgmt.ocr.service.IOcrService;
import com.jpmc.rdt.docmgmt.ocr.test.spring.OcrTestSpringConfiguration;


/**
 * Test Class for testing Fax Acknowledge Service
 * @author Hitesh Modi
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { OcrTestSpringConfiguration.class })
@ActiveProfiles(profiles = "local" )
@WebAppConfiguration
public class TestOcrTaService {
	
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="taDao")
	private ITransferAgencyDao faxAckDao;
	
	@Autowired
	public void init(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Resource(name="ocrServiceTA")
	private IOcrService service;
	
	@Test
	public void testSpoolerServiceLUX() throws Exception{
		String sourceSystem = "LuxembourgTA-BlackRock";
		String requestedFile = "C:\\Hitesh\\application-input-files\\LUX-Spooler\\LUX-BlackRock\\ALLQ000054.tif";
		File file = new File(requestedFile);
		OcrDataModel ocrDataModel = new OcrDataModel();
		
		Path path = Paths.get(requestedFile);
		BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		FileTime time = attributes.creationTime();
		
		ocrDataModel.addOrdData("fileCreationDate", new Date(time.toMillis()));
		service.processRequest(sourceSystem, requestedFile, ocrDataModel);
	}
	
	@Test
	public void testSpoolerServiceHK() throws Exception{
		String sourceSystem = "JPMCHongKongDealing";
		String requestedFile = "C:\\Hitesh\\application-input-files\\HK-Spooler\\ALLQ000054.tif";
		File file = new File(requestedFile);
		OcrDataModel ocrDataModel = new OcrDataModel();
		
		Path path = Paths.get(requestedFile);
		BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		FileTime time = attributes.creationTime();
		
		ocrDataModel.addOrdData("fileCreationDate", new Date(time.toMillis()));
		service.processRequest(sourceSystem, requestedFile, ocrDataModel);
	}
	
	
	@Test
	public void testFileExistence() throws Exception{
		String fileName = "20170208.000020_20170208.000002_CHIA100129.ACK.tif";
		boolean fileExists = service.checkIfFileAlreadyProcessed(fileName, "JPMCHongKongDealing");
		Assert.assertTrue(fileExists);
				
	}
	
}
