package it.polimi.deib.ds4m.main.model.dataSources;


import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class DataSource 
{
	private String name;
	private String type;
	private Parameters parameters;
 
    
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

}
