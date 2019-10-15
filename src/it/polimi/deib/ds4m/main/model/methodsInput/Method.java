package it.polimi.deib.ds4m.main.model.methodsInput;

import java.io.Serializable;
import java.util.ArrayList;

public class Method implements Serializable {
	
	private static final long serialVersionUID = 2746658122122576097L;
	private String method_id;	
	private ArrayList<DataSourceInput> dataSources;

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
	public ArrayList<DataSourceInput> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(ArrayList<DataSourceInput> dataSources) {
		this.dataSources = dataSources;
	}

}
