package it.polimi.deib.ds4m.main.model.movement;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Movement 
{
	private String type;
	private String from;
	private String to;
	private double restTime;
	
	
	//linked to data source class, when the movement is instantiated
	@JsonIgnore
	private DataSource fromLinked;
	@JsonIgnore
	private DataSource toLinked;
	
	private ArrayList<String> positiveImpacts; //list of IDs of goals with a positive impact
	private ArrayList<String> negativeImpacts; //list of IDs of goals with a negative impact
	
	private ArrayList<Transformation> transformations;
	
	private ArrayList<Cost> costs;

	//default creator
	public Movement()
	{
		positiveImpacts = new ArrayList<String>();
		negativeImpacts = new ArrayList<String>();
		
		transformations = new ArrayList<Transformation>();
		
	}
	
	public Movement(String type, String from, String to, ArrayList<String> positiveImpacts, ArrayList<String> negativeImpacts, ArrayList<Transformation> transformations, ArrayList<Cost> costs, double restTime ) 
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
	 * @return the transformations
	 */
	public ArrayList<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(ArrayList<Transformation> transformations) {
		this.transformations = transformations;
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

	/**
	 * @return the fromLinked
	 */
	public DataSource getFromLinked() {
		return fromLinked;
	}

	/**
	 * @param fromLinked the fromLinked to set
	 */
	public void setFromLinked(DataSource fromLinked) {
		this.fromLinked = fromLinked;
	}

	/**
	 * @return the toLinked
	 */
	public DataSource getToLinked() {
		return toLinked;
	}

	/**
	 * @param toLinked the toLinked to set
	 */
	public void setToLinked(DataSource toLinked) {
		this.toLinked = toLinked;
	}
	

}
