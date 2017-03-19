package com.jpmc.rdt.docmgmt.ocr.exception;

public class OcrServiceException extends Exception{

	/**
	 * The exception class that is thrown if something goes wrong while accessing Athenaeum Service.
	 */
	private static final long serialVersionUID = 1L;
	
	public OcrServiceException(String message, Exception e) {
		super(message, e);
	}
	
}
