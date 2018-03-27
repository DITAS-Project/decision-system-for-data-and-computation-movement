package it.polimi.deib.ds4m.main.model.movement;

import java.util.Vector;

public class Movement 
{
	private String type;
	private String from;
	private String to;
	
	private Vector<String> positiveImpacts;
	private Vector<String> negativeImpacts;
	
	private Vector<Transformation> transformations;
	
	private Vector<Cost> costs;

	public Movement(String type, String from, String to, Vector<String> positiveImpacts, Vector<String> negativeImpacts, Vector<Transformation> transformations, Vector<Cost> costs ) 
	{
		this.type=type;
		this.from=from;
		this.to=to;;
		this.positiveImpacts=positiveImpacts;
		this.negativeImpacts=negativeImpacts;
		this.transformations=transformations;
		this.costs=costs;
	}
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
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
	 * @return the transformations
	 */
	public Vector<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(Vector<Transformation> transformations) {
		this.transformations = transformations;
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
