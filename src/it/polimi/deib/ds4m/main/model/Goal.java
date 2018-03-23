package it.polimi.deib.ds4m.main.model;

import java.util.Vector;

public class Goal 
{
	Vector<Metric> metrics;
	String ID;
	String name;
	Double weight;
	
	/**
	 * @return the metrics
	 */
	public Vector<Metric> getMetrics() {
		return metrics;
	}
	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(Vector<Metric> metrics) {
		this.metrics = metrics;
	}
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
