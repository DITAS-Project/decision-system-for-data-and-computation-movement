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
package it.polimi.deib.ds4m.main.model.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Infrastructure implements Serializable
{
	private static final long serialVersionUID = 7239433682393186493L;
	private String name;
	private String type;//cloud edge
	private String id;
	//private ArrayList<Characteristic> characteristics;
	private ExtraPropertiesInfrastructure  extra_properties;
	
	@JsonIgnore
	private Boolean isDataSource = false;
	
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
	 * @return the isDataSource
	 */
	public Boolean getIsDataSource() {
		return isDataSource;
	}
	/**
	 * @param isDataSource the isDataSource to set
	 */
	public void setIsDataSource(Boolean isDataSource) {
		this.isDataSource = isDataSource;
	}
	
	@Override
	public boolean equals(Object o) 
	{
	    // self check
	    if (this == o)
	        return true;
	    
	    // null check
	    if (o == null)
	        return false;
	    
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    
	    Infrastructure other = (Infrastructure) o;
	    
	    // field comparison
	    if (!this.name.equals(other.getName()) ) {
	        return false;
	    }
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    if (!this.isDataSource.equals(other.getIsDataSource()) ) {
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
            append(name).
            append(isDataSource).
            toHashCode();
    }
	/**
	 * @return the extra_properties
	 */
	public ExtraPropertiesInfrastructure getExtra_properties() {
		return extra_properties;
	}
	/**
	 * @param extra_properties the extra_properties to set
	 */
	public void setExtra_properties(ExtraPropertiesInfrastructure extra_properties) {
		this.extra_properties = extra_properties;
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


}
