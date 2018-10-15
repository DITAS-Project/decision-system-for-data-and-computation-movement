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

import java.util.Map;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Attribute 
{
	private String id;
	private String description;
	private String type;
	Map<String, Property> properties;

	
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the properties
	 */

	/**
	 * @return the properties
	 */
	public Map<String, Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Attribute.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Attribute other = (Attribute) obj;	    
	    if (!this.id.equals(other.getId()) ) {
	        return false;
	    }
	    
	    if (!this.description.equals(other.getDescription()) ) {
	        return false;
	    }
	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.properties.equals(other.getProperties()) ) {
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
            append(id).
            append(type).
            append(properties).
            toHashCode();
    }



}
