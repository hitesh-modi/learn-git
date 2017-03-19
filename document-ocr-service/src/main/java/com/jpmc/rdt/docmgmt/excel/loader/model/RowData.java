package com.jpmc.rdt.docmgmt.excel.loader.model;

import java.util.Collection;

public class RowData {
	
	Collection<DBModel> rowValues;

	/**
	 * @return the rowValues
	 */
	public Collection<DBModel> getRowValues() {
		return rowValues;
	}

	/**
	 * @param rowValues the rowValues to set
	 */
	public void setRowValues(Collection<DBModel> rowValues) {
		this.rowValues = rowValues;
	}
	
}
