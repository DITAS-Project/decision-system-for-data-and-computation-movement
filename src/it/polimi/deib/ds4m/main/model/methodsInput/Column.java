package it.polimi.deib.ds4m.main.model.methodsInput;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Column {
	
	private String column_id;
	
	/**
	 * @return the column_id
	 */
	public String getColumn_id() {
		return column_id;
	}

	/**
	 * @param column_id the column_id to set
	 */
	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}

}
