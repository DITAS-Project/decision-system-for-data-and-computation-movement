package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Property 
{
	private String name;
	private String unit;
	private Double maximum;
	private Double minimum;
	private String value;

	
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the maximum
	 */
	public Double getMaximum() {
		return maximum;
	}
	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
	/**
	 * @return the minimum
	 */
	public Double getMinimum() {
		return minimum;
	}
	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Property.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Property other = (Property) obj;	    
	    if (!this.unit.equals(other.getUnit()) ) {
	        return false;
	    }
	    
	    if (!this.name.equals(other.getName()) ) {
	        return false;
	    }
	    
	    if (!this.maximum.equals(other.getMaximum()) ) {
	        return false;
	    }
	    
	    if (!this.minimum.equals(other.getMinimum()) ) {
	        return false;
	    }
	    
	    if (!this.value.equals(other.getValue()) ) {
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
            append(unit).
            append(maximum).
            append(minimum).
            append(value).
            toHashCode();
    }

}
