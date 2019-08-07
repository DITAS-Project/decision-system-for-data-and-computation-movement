package it.polimi.deib.ds4m.main.model.methodsInput;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSource {
	
	public String dataSource_id;
	public String dataSource_type;
	private ArrayList<Database> database;

	/**
	 * @return the dataSource_id
	 */
	public String getDataSource_id() {
		return dataSource_id;
	}

	/**
	 * @param dataSource_id the dataSource_id to set
	 */
	public void setDataSource_id(String dataSource_id) {
		this.dataSource_id = dataSource_id;
	}

	/**
	 * @return the dataSource_type
	 */
	public String getDataSource_type() {
		return dataSource_type;
	}

	/**
	 * @param dataSource_type the dataSource_type to set
	 */
	public void setDataSource_type(String dataSource_type) {
		this.dataSource_type = dataSource_type;
	}

	/**
	 * @return the database
	 */
	public ArrayList<Database> getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(ArrayList<Database> database) {
		this.database = database;
	}

}
