package it.polimi.deib.ds4m.main.model.methodsInput;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Database {
	
	private String database_id;
	private ArrayList<Table> tables;

	/**
	 * @return the database_id
	 */
	public String getDatabase_id() {
		return database_id;
	}

	/**
	 * @param database_id the database_id to set
	 */
	public void setDatabase_id(String database_id) {
		this.database_id = database_id;
	}

	/**
	 * @return the tables
	 */
	public ArrayList<Table> getTables() {
		return tables;
	}

	/**
	 * @param tables the tables to set
	 */
	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

}
