package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.Vector;

public class Attributes 
{
	Vector<Metric> dataUtility;
	Vector<Metric> security;
	Vector<Metric> privacy;
	
	/**
	 * @return the dataUtility
	 */
	public Vector<Metric> getDataUtility() {
		return dataUtility;
	}
	/**
	 * @param dataUtility the dataUtility to set
	 */
	public void setDataUtility(Vector<Metric> dataUtility) {
		this.dataUtility = dataUtility;
	}
	/**
	 * @return the security
	 */
	public Vector<Metric> getSecurity() {
		return security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(Vector<Metric> security) {
		this.security = security;
	}
	/**
	 * @return the privacy
	 */
	public Vector<Metric> getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(Vector<Metric> privacy) {
		this.privacy = privacy;
	}

}
