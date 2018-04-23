package it.polimi.deib.ds4m.main.model.movement;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Cost 
{
	private String type;
	private String unit;
	private double value;
	
	
	public Cost() 
	{
		
	}
	
	
	public Cost(String type, String unit, double value) 
	{
		this.type=type;
		this.unit=unit;
		this.value=value;
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
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Cost.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Cost other = (Cost) obj;	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.unit.equals(other.getUnit()) ) {
	        return false;
	    }
	    
	    if (this.value!=other.getValue() ) {
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
            append(unit).
            append(value).
            toHashCode();
    }

}
