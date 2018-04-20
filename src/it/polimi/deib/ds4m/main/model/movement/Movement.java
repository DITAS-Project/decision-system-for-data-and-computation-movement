package it.polimi.deib.ds4m.main.model.movement;

import java.util.Vector;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Movement 
{
	private String type;
	private String from;
	private String to;
	private double restTime;
	
	private Vector<String> positiveImpacts;
	private Vector<String> negativeImpacts;
	
	private Vector<Transformation> transformations;
	
	private Vector<Cost> costs;

	public Movement(String type, String from, String to, Vector<String> positiveImpacts, Vector<String> negativeImpacts, Vector<Transformation> transformations, Vector<Cost> costs, double restTime ) 
	{
		this.type=type;
		this.from=from;
		this.to=to;;
		this.positiveImpacts=positiveImpacts;
		this.negativeImpacts=negativeImpacts;
		this.transformations=transformations;
		this.costs=costs;
		this.restTime=restTime;
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
	 * @return the restTime
	 */
	public double getRestTime() {
		return restTime;
	}

	/**
	 * @param restTime the restTime to set
	 */
	public void setRestTime(double restTime) {
		this.restTime = restTime;
	}
	
	
	//ATTENTION: the comparison does not consider the impacts 
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Movement.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Movement other = (Movement) obj;	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.from.equals(other.getFrom()) ) {
	        return false;
	    }
	    
	    if (!this.to.equals(other.getTo()) ) {
	        return false;
	    }
	    
	    if (this.restTime!=other.getRestTime() ) {
	        return false;
	    }
	    
//	    if (!this.positiveImpacts.equals(other.getPositiveImpacts()) ) {
//	        return false;
//	    }
//	    
//	    if (!this.negativeImpacts.equals(other.getNegativeImpacts()) ) {
//	        return false;
//	    }
	    
	    if (!this.costs.equals(other.getCosts()) ) {
	        return false;
	    }
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(type).
            append(to).
            append(from).
            append(restTime).
//            append(positiveImpacts).
//            append(negativeImpacts).
            append(costs).
            toHashCode();
    }
	

}
