package it.polimi.deib.ds4m.main.model.movement;

import java.util.Vector;

public class Transformation 
{
	private String type;
	
	private Vector<String> positiveImpacts;
	private Vector<String> negativeImpacts;
	
	private Vector<Cost> costs;
	
	public Transformation(String type, Vector<String> positiveImpacts, Vector<String> negativeImpacts, Vector<Cost> costs)
	{
		this.type=type;
		this.positiveImpacts=positiveImpacts;
		this.negativeImpacts=negativeImpacts;
		this.costs=costs;
	}
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the positiveImpacts
	 */
	public Vector<String> getPositiveImpacts() {
		return positiveImpacts;
	}

	/**
	 * @param positiveImpacts the positiveImpacts to set
	 */
	public void setPositiveImpacts(Vector<String> positiveImpacts) {
		this.positiveImpacts = positiveImpacts;
	}

	/**
	 * @return the negativeImpacts
	 */
	public Vector<String> getNegativeImpacts() {
		return negativeImpacts;
	}

	/**
	 * @param negativeImpacts the negativeImpacts to set
	 */
	public void setNegativeImpacts(Vector<String> negativeImpacts) {
		this.negativeImpacts = negativeImpacts;
	}

	/**
	 * @return the costs
	 */
	public Vector<Cost> getCosts() {
		return costs;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(Vector<Cost> costs) {
		this.costs = costs;
	}

}
