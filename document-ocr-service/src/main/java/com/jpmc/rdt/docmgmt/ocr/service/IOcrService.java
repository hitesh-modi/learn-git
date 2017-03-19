package com.jpmc.rdt.docmgmt.ocr.service;

import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ocr.model.OcrDataModel;
import com.jpmc.rdt.docmgmt.ocr.model.OcrResponse;
import com.jpmc.rdt.docmgmt.ta.model.FaxFile;

public interface IOcrService {
	
	public OcrResponse processRequest(String sourceSystem, String requestedFile, OcrDataModel data) throws OcrServiceException;
	
	public OcrResponse processResponse() throws OcrServiceException;
	
	public boolean checkIfFileAlreadyProcessed(String fileName, String applicationName) throws OcrServiceException;
	
	public void processFileForLux(FaxFile faxFile);
	
	public void processFileForHK(FaxFile faxFile);

	public FaxFile saveFaxFileToDB(String sourceSystem, String requestedFile, OcrDataModel data);

}
