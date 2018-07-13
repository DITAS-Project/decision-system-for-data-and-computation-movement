package it.polimi.deib.ds4m.main.model.dataSources;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.resources.Resource;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class DataSource 
{
	private String name;
	private String type;
	private Parameters parameters;
	private Resource resourceUsed;//this represent the resource used in terms of how many GB and CPU% the Data Source uses 
	
	@JsonIgnore
	private Resource resourceUsedLinked;// this is the link to the actual resource that host the DS. in here the parameters represent what the resource has available a nd in total.
	
	public void connectWithResource(ArrayList<Resource> resources)
	{
		for (Resource resource : resources)
		{
			if (resource.getName().equals(resourceUsed.getName()))
			{
				resourceUsedLinked = resource;
				break;
			}
		}
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
	 * @return the parameters
	 */
	public Parameters getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
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
	    
	    if (!this.name.equals(other.getName()) ) {
	        return false;
	    }
	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.parameters.equals(other.getParameters()) ) {
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
            append(type).
            append(parameters).
            toHashCode();
    }
	/**
	 * @return the resourceUsed
	 */
	public Resource getResourceUsed() {
		return resourceUsed;
	}
	/**
	 * @param resourceUsed the resourceUsed to set
	 */
	public void setResourceUsed(Resource resourceUsed) {
		this.resourceUsed = resourceUsed;
	}

}
