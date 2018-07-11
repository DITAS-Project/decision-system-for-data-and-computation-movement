package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;

public class Attributes 
{
	ArrayList<Attribute> dataUtility;
	ArrayList<Attribute> security;
	ArrayList<Attribute> privacy;
	
	/**
	 * @return the dataUtility
	 */
	public ArrayList<Attribute> getDataUtility() {
		return dataUtility;
	}
	/**
	 * @param dataUtility the dataUtility to set
	 */
	public void setDataUtility(ArrayList<Attribute> dataUtility) {
		this.dataUtility = dataUtility;
	}
	/**
	 * @return the security
	 */
	public ArrayList<Attribute> getSecurity() {
		return security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(ArrayList<Attribute> security) {
		this.security = security;
	}
	/**
	 * @return the privacy
	 */
	public ArrayList<Attribute> getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(ArrayList<Attribute> privacy) {
		this.privacy = privacy;
	}

}
