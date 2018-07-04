package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;

public class VDC 
{	
	//vector of requirements
	private ArrayList<DataManagement> dataManagement;
	private Vector<DataSource> DataSources;
	private String id;
	
	
	/**
	 * @return the dataSources
	 */
	public Vector<DataSource> getDataSources() {
		return DataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(Vector<DataSource> dataSources) {
		DataSources = dataSources;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the dataManagement
	 */
	public ArrayList<DataManagement> getDataManagement() {
		return dataManagement;
	}

	/**
	 * @param dataManagement the dataManagement to set
	 */
	public void setDataManagement(ArrayList<DataManagement> dataManagement) {
		this.dataManagement = dataManagement;
	}


	
	
	
}
