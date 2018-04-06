package it.polimi.deib.ds4m.main.model.applicationRequirement;

import java.util.Vector;

public class ApplicationRequirements 
{
	FunctionalProperties functionalProperties;
	Vector<Method> methods;
	

	

	/**
	 * @return the functionalProperties
	 */
	public FunctionalProperties getFunctionalProperties() {
		return functionalProperties;
	}
	/**
	 * @param functionalProperties the functionalProperties to set
	 */
	public void setFunctionalProperties(FunctionalProperties functionalProperties) {
		this.functionalProperties = functionalProperties;
	}
	/**
	 * @return the methods
	 */
	public Vector<Method> getMethods() {
		return methods;
	}
	/**
	 * @param methods the methods to set
	 */
	public void setMethods(Vector<Method> methods) {
		this.methods = methods;
	}


}
