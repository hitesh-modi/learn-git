package com.jpmc.rdt.docmgmt.excel.loader.model;

/**
 * 
 * @author Hitesh Modi
 *
 */
public class DBModel {
	private String dbName;
	private String schemaName;
	private String tableName;
	private String value;
	private DbColumn dbColumn;
	/**
	 * @return the dbColumn
	 */
	public DbColumn getDbColumn() {
		return dbColumn;
	}
	/**
	 * @param dbColumn the dbColumn to set
	 */
	public void setDbColumn(DbColumn dbColumn) {
		this.dbColumn = dbColumn;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}
	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
