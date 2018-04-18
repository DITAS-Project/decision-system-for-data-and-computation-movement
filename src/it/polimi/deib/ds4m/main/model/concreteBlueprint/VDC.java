package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.List;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;

public class VDC 
{	
	//vector of requirements
	private Vector<DataManagement> appRequirements;
	private List<DataSource> DataSources;
	private String id;
	
	
	
	//protected constructor for singleton
	public VDC() 
	{
		appRequirements = new Vector<DataManagement>();
	}	

	/**
	 * @return the appRequirements
	 */
	public Vector<DataManagement> getApplicationsRequirements() {
		return appRequirements;
	}

	/**
	 * @param appRequirements the appRequirements to set
	 */
	
	public void addApplicationRequirement(DataManagement applicationRequirements) 
	{
		this.appRequirements.addElement(applicationRequirements);
	}

	/**
	 * @return the dataSources
	 */
	public List<DataSource> getDataSources() {
		return DataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(List<DataSource> dataSources) {
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

	
	
	
}
