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
package it.polimi.deib.ds4m.main.model.dataSources;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.polimi.deib.ds4m.main.model.resources.Characteristic;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSource 
{
	private String id;
	private String type;
	private String classDataSource;
	private String description;
	//private Parameters parameters;
	private String location;//cloud or edge
	private ArrayList<Characteristic> resourceUsed;//this represent the resource used in terms of how many GB and CPU% the Data Source uses 
	
	@JsonIgnore
	private Infrastructure resourceUsedLinked;// this is the link to the actual resource that host the DS. in here the parameters represent what the resource has available and in total.
	
	/**
	 * It creates the resource that represent the data source. 
	 * Notice that the location is set to null since this resource can be only used as source of data movement 
	 * 
	 * @param resources
	 */
	public void createResource(ArrayList<Infrastructure> resources)
	{
		//the new resource has the dame parameter of the datasource since it is fake and used as a source for the data movement
		this.resourceUsedLinked =  new Infrastructure();
		this.resourceUsedLinked.setCharacteristics(resourceUsed);
		this.resourceUsedLinked.setName(id);
		this.resourceUsedLinked.setType(location);
		this.resourceUsedLinked.setIsDataSource(true);
		
		resources.add(resourceUsedLinked);//add the newly created resource to the list of resource to later create the movement

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
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!DataSource.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final DataSource other = (DataSource) obj;	  
	    
	    if (!this.id.equals(other.getId()) ) {
	        return false;
	    }
	    
	    if (!this.classDataSource.equals(other.getClassDataSource()) ) {
	        return false;
	    }
	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(id).
            append(type).
            append(classDataSource).
            toHashCode();
    }


	/**
	 * @return the resourceUsed
	 */
	public ArrayList<Characteristic> getResourceUsed() {
		return resourceUsed;
	}


	/**
	 * @param resourceUsed the resourceUsed to set
	 */
	public void setResourceUsed(ArrayList<Characteristic> resourceUsed) {
		this.resourceUsed = resourceUsed;
	}


	/**
	 * @return the resourceUsedLinked
	 */
	public Infrastructure getResourceUsedLinked() {
		return resourceUsedLinked;
	}


	/**
	 * @param resourceUsedLinked the resourceUsedLinked to set
	 */
	public void setResourceUsedLinked(Infrastructure resourceUsedLinked) {
		this.resourceUsedLinked = resourceUsedLinked;
	}


	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}


	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
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
	 * @return the classDataSource
	 */
	@JsonProperty("class")
	public String getClassDataSource() {
		return classDataSource;
	}


	/**
	 * @param classDataSource the classDataSource to set
	 */
	@JsonProperty("class")
	public void setClassDataSource(String classDataSource) {
		this.classDataSource = classDataSource;
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


}
