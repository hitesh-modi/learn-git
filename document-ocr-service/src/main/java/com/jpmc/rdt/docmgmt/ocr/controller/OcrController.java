package com.jpmc.rdt.docmgmt.ocr.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.rdt.docmgmt.ocr.scheduler.FileWatcher;

/**
 * @author Hitesh Modi
 */
@RestController
@RequestMapping("/services/ocrService")
public class OcrController {
	
	private static final Logger LOGGER = Logger.getLogger(OcrController.class);
	
	@Resource(name="fileWatcher")
	private FileWatcher fileWatcher;
	
	@RequestMapping(value = "/process", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public String processRequest(HttpServletRequest httprequest) throws Exception {
		LOGGER.info("Process method started");
		String jsonResponse = "Success";
		return jsonResponse;
	}
	
	@RequestMapping(value = "/processHKFaxes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public String processHkFaxes(HttpServletRequest httprequest) throws Exception {
		LOGGER.info("Process method started");
		fileWatcher.pollFileSystemForHKFaxes();
		String jsonResponse = "Success";
		return jsonResponse;
	}
	
	@RequestMapping(value = "/processLUXFaxes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	public String processLuxFaxes(HttpServletRequest httprequest) throws Exception {
		LOGGER.info("Process method started");
		fileWatcher.pollFileSystemForLUXFaxesBau();
		fileWatcher.pollFileSystemForLUXFaxesBlackRock();
		String jsonResponse = "Success";
		return jsonResponse;
	}
	
}
