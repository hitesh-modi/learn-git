package com.jpmc.rdt.docmgmt.ocr.model;

import java.util.HashMap;
import java.util.Map;

public class OcrDataModel {
	
	private Map<String, Object> ordData;

	public Object getOrdData(String key) {
		return ordData.get(key);
	}

	public void addOrdData(String key, Object value) {
		if (this.ordData == null) ordData = new HashMap<String, Object>();
		
		ordData.put(key, value);
	}
	
	
	
}
