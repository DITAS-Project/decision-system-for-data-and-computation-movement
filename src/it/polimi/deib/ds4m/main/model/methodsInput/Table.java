package it.polimi.deib.ds4m.main.model.methodsInput;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Table implements Serializable
{

	private static final long serialVersionUID = 5404781628373855590L;
	private String table_id;
	private ArrayList<Column> columns;

	/**
	 * @return the table_id
	 */
	public String getTable_id() {
		return table_id;
	}

	/**
	 * @param table_id the table_id to set
	 */
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}

	/**
	 * @return the columns
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}



}
