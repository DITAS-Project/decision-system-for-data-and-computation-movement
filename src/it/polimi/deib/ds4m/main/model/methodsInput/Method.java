package it.polimi.deib.ds4m.main.model.methodsInput;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Method {
	
	private String method_id;	
	private ArrayList<DataSource> dataSources;

	/**
	 * @return the method_id
	 */
	public String getMethod_id() {
		return method_id;
	}

	/**
	 * @param method_id the method_id to set
	 */
	public void setMethod_id(String method_id) {
		this.method_id = method_id;
	}

	/**
	 * @return the dataSources
	 */
	public ArrayList<DataSource> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(ArrayList<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

}
