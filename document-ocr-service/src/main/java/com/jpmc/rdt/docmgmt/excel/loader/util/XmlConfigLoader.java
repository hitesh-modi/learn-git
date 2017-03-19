package com.jpmc.rdt.docmgmt.excel.loader.util;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.jpmc.rdt.docmgmt.excel.loader.model.SheetsMapping;
/**
 * 
 * @author Hitesh Modi
 *
 */
public class XmlConfigLoader {
	
	private static final Logger LOGGER = Logger.getLogger(XmlConfigLoader.class);
	
	private static SheetsMapping sheetMapping;
	
	static {
		sheetMapping = loadXmlConfiguration();
	}

	private static SheetsMapping loadXmlConfiguration() {
		LOGGER.info("Reading excel sheet mapping configuration");
		InputStream is = XmlConfigLoader.class.getResourceAsStream("/excel-loader-config.xml");

		JAXBContext context;
		SheetsMapping application = null;
		try {
			context = JAXBContext.newInstance(SheetsMapping.class);

			Unmarshaller unmarshaller;

			unmarshaller = context.createUnmarshaller();
			application = (SheetsMapping) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			LOGGER.error("Error while reading sheet mapping configuration. " + e);
		}

		return application;

	}
	
	public static SheetsMapping getSheetMapping() {
		return sheetMapping;
	}
}
