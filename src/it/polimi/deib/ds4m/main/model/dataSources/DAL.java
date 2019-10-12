package it.polimi.deib.ds4m.main.model.dataSources;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DAL 
{
	private String original_ip;
	private Infrastructure position;
	private ArrayList<DataSource> dataSources;

	public DAL() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * It creates the resource that represent the data source. 
	 * Notice that the location is set to null since this resource can be only used as source of data movement 
	 * 
	 * @param infrastructures
	 */
	public void createResource(ArrayList<Infrastructure> infrastructures)
	{
		//the new resource has the same parameter of the datasource since it is fake and used as a source for the data movement
		this.position =  new Infrastructure();
		this.position.setName(original_ip); //hinerited from DAL
		this.position.setType("edge"); //edge since it is on premise of data owner
		this.position.setIsDataSource(true);
		
		infrastructures.add(position);//add the newly created resource to the list of resource to later create the movement

	}

	/**
	 * @return the original_ip
	 */
	public String getOriginal_ip() {
		return original_ip;
	}

	/**
	 * @param original_ip the original_ip to set
	 */
	public void setOriginal_ip(String original_ip) {
		this.original_ip = original_ip;
	}

	/**
	 * @return the position
	 */
	public Infrastructure getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Infrastructure position) {
		this.position = position;
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