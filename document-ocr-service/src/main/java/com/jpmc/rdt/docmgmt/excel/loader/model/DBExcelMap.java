package com.jpmc.rdt.docmgmt.excel.loader.model;

public class DBExcelMap {
	private Integer indexOfExcelColumn;
	private DbColumn databaseColumn;
	private SheetColumn sheetColumn;
	
	public Integer getIndexOfExcelColumn() {
		return indexOfExcelColumn;
	}
	/**
	 * @param indexOfExcelColumn the indexOfExcelColumn to set
	 */
	public void setIndexOfExcelColumn(Integer indexOfExcelColumn) {
		this.indexOfExcelColumn = indexOfExcelColumn;
	}
	/**
	 * @return the databaseColumn
	 */
	public DbColumn getDatabaseColumn() {
		return databaseColumn;
	}
	/**
	 * @param databaseColumn the databaseColumn to set
	 */
	public void setDatabaseColumn(DbColumn databaseColumn) {
		this.databaseColumn = databaseColumn;
	}
	
	public SheetColumn getSheetColumn() {
		return sheetColumn;
	}
	public void setSheetColumn(SheetColumn sheetColumn) {
		this.sheetColumn = sheetColumn;
	}
	/**
	 * @return the indexOfExcelColumn
	 */
	
}
