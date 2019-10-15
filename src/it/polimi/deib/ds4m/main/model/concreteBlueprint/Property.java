/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.io.Serializable;
import java.util.ArrayList;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Property  implements Serializable
{
	private static final long serialVersionUID = -875423316596087778L;
	
	//metrics
	private String name;
	private String unit;
	private Double maximum;
	private Double minimum;
	private ArrayList<String> value;

	
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
	/**
	 * @return the value
	 */
	public ArrayList<String> getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(ArrayList<String> value) {
		this.value = value;
	}

}
