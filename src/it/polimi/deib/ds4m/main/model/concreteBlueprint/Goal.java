package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.Vector;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

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

	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Goal.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Goal other = (Goal) obj;	    
	    if (!this.ID.equals(other.getID()) ) {
	        return false;
	    }
	    
	    if (!this.name.equals(other.getName()) ) {
	        return false;
	    }
	    
	    if (!this.weight.equals(other.getWeight()) ) {
	        return false;
	    }
	    
	    if (!this.metrics.equals(other.getMetrics()) ) {
	        return false;
	    }
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(name).
            append(ID).
            append(weight).
            append(metrics).
            toHashCode();
    }
	
	
}
