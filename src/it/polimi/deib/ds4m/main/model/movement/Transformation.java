package it.polimi.deib.ds4m.main.model.movement;

import java.util.ArrayList;

public class Transformation 
{
	private String type;
	
	private ArrayList<String> positiveImpacts;
	private ArrayList<String> negativeImpacts;
	
	private ArrayList<Cost> costs;
	
	public Transformation() 
	{
		positiveImpacts = new ArrayList<String>();
		negativeImpacts = new ArrayList<String>();
		
		costs = new ArrayList<Cost>();
	}
	
	public Transformation(String type, ArrayList<String> positiveImpacts, ArrayList<String> negativeImpacts, ArrayList<Cost> costs)
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
	public ArrayList<String> getPositiveImpacts() {
		return positiveImpacts;
	}

	/**
	 * @param positiveImpacts the positiveImpacts to set
	 */
	public void setPositiveImpacts(ArrayList<String> positiveImpacts) {
		this.positiveImpacts = positiveImpacts;
	}

	/**
	 * @return the negativeImpacts
	 */
	public ArrayList<String> getNegativeImpacts() {
		return negativeImpacts;
	}

	/**
	 * @param negativeImpacts the negativeImpacts to set
	 */
	public void setNegativeImpacts(ArrayList<String> negativeImpacts) {
		this.negativeImpacts = negativeImpacts;
	}

	/**
	 * @return the costs
	 */
	public ArrayList<Cost> getCosts() {
		return costs;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(ArrayList<Cost> costs) {
		this.costs = costs;
	}

}
