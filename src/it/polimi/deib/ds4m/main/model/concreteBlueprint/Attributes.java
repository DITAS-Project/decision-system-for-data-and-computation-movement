package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.Vector;

public class Attributes 
{
	Vector<Attribute> dataUtility;
	Vector<Attribute> security;
	Vector<Attribute> privacy;
	
	/**
	 * @return the dataUtility
	 */
	public Vector<Attribute> getDataUtility() {
		return dataUtility;
	}
	/**
	 * @param dataUtility the dataUtility to set
	 */
	public void setDataUtility(Vector<Attribute> dataUtility) {
		this.dataUtility = dataUtility;
	}
	/**
	 * @return the security
	 */
	public Vector<Attribute> getSecurity() {
		return security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(Vector<Attribute> security) {
		this.security = security;
	}
	/**
	 * @return the privacy
	 */
	public Vector<Attribute> getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(Vector<Attribute> privacy) {
		this.privacy = privacy;
	}

}
