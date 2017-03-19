/**
 * 
 */
package com.jpmc.rdt.docmgmt.ocr.model;

/**
 * @author Hitesh Modi
 *
 */
public class OcrResponse {

	private OcrDataModel data;
	
	private String responseCode;

	private String responseDescription;
	
	private String responseStatus;

	public OcrDataModel getData() {
		return data;
	}

	public void setData(OcrDataModel data) {
		this.data = data;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseDescription
	 */
	public String getResponseDescription() {
		return responseDescription;
	}

	/**
	 * @param responseDescription
	 *            the responseDescription to set
	 */
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	@Override
	public String toString() {
		return "Response Code : " + responseCode + ", Response Message : " + responseDescription + ", Response Status : " + responseStatus;
	}
}
