package it.polimi.deib.ds4m.main.model.dataSources;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.polimi.deib.ds4m.main.model.resources.Characteristic;
import it.polimi.deib.ds4m.main.model.resources.Resource;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSource 
{
	private String id;
	private String type;
	private String classDataSource;
	private String description;
	//private Parameters parameters;
	private String location;
	private ArrayList<Characteristic> resourceUsed;//this represent the resource used in terms of how many GB and CPU% the Data Source uses 
	
	@JsonIgnore
	private Resource resourceUsedLinked;// this is the link to the actual resource that host the DS. in here the parameters represent what the resource has available a nd in total.
	
	/**
	 * It creates the resource that represent the data source. please notice that the locatioj is set tu null since this resource can be olty used as source of data movement 
	 * 
	 * @param resources
	 */
	public void createResource(ArrayList<Resource> resources)
	{
		this.resourceUsedLinked =  new Resource();
		this.resourceUsedLinked.setCharacteristics(resourceUsed);
		this.resourceUsedLinked.setName(id);
		this.resourceUsedLinked.setType(type);
		this.resourceUsedLinked.setLocation(location);
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
	public Resource getResourceUsedLinked() {
		return resourceUsedLinked;
	}


	/**
	 * @param resourceUsedLinked the resourceUsedLinked to set
	 */
	public void setResourceUsedLinked(Resource resourceUsedLinked) {
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
