package it.polimi.deib.ds4m.main.evaluation;

import java.util.ArrayList;

public class NetworkConnection 
{
	//resources connected
	ArrayList<Infrastructure_evaluation> resourcesConnected;
	
	//network characteristics
	Double latency;
	Double availability;
	
	public NetworkConnection(Double latency, Double availability)
	{
		this.resourcesConnected = new ArrayList<Infrastructure_evaluation>();
		this.latency = latency;
		this.availability=availability;
		
	}

	/**
	 * @param resourcesConnected the resourcesConnected to set
	 */
	public void addResources(Infrastructure_evaluation resource) 
	{
		this.resourcesConnected.add(resource);
	}
	
	
	/**
	 * @return the resourcesConnected
	 */
	public ArrayList<Infrastructure_evaluation> getResourcesConnected() {
		return resourcesConnected;
	}

	/**
	 * @param resourcesConnected the resourcesConnected to set
	 */
	public void setResourcesConnected(ArrayList<Infrastructure_evaluation> resourcesConnected) {
		this.resourcesConnected = resourcesConnected;
	}

	/**
	 * @return the latency
	 */
	public Double getLatency() {
		return latency;
	}

	/**
	 * @param latency the latency to set
	 */
	public void setLatency(Double latency) {
		this.latency = latency;
	}

	/**
	 * @return the availability
	 */
	public Double getAvailability() {
		return availability;
	}

	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(Double availability) {
		this.availability = availability;
	}


}
