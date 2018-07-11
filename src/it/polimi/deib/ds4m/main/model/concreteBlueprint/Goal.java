package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(value = { "attributesLinked" })
public class Goal 
{
	ArrayList<String> attributes;
	ArrayList<Attribute> attributesLinked;
	String ID;
	String description;
	Double weight;
	

	/**
	 * @return the ID
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
	    
	    if (!this.description.equals(other.getDescription()) ) {
	        return false;
	    }
	    
	    if (!this.weight.equals(other.getWeight()) ) {
	        return false;
	    }
	    
	    if (!this.attributes.equals(other.getAttributes()) ) {
	        return false;
	    }
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(description).
            append(ID).
            append(weight).
            append(attributes).
            append(attributesLinked).
            toHashCode();
    }
	/**
	 * @return the attributes
	 */
	public ArrayList<String> getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the attributesLinked
	 */
	public ArrayList<Attribute> getAttributesLinked() 
	{
		if (attributesLinked==null)
			attributesLinked = new ArrayList<Attribute>();
		
		return attributesLinked;
	}
	/**
	 * @param attributesLinked the attributesLinked to set
	 */
	public void setAttributesLinked(ArrayList<Attribute> attributesLinked) {
		this.attributesLinked = attributesLinked;
	}
	
	
}
