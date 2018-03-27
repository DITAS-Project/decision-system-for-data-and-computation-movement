package it.polimi.deib.ds4m.main.model.applicationRequirement;

import java.util.Vector;

public class ApplicationsRequirements 
{	
	//vector of requirements
	private Vector<ApplicationRequirements> appRequirements;
	
	
	
	//protected constructor for singketon
	public ApplicationsRequirements() 
	{
		appRequirements = new Vector<ApplicationRequirements>();
	}	

	/**
	 * @return the appRequirements
	 */
	public Vector<ApplicationRequirements> getApplicationsRequirements() {
		return appRequirements;
	}

	/**
	 * @param appRequirements the appRequirements to set
	 */
	
	public void addApplicationRequirement(ApplicationRequirements applicationRequirements) 
	{
		this.appRequirements.addElement(applicationRequirements);
	}

	
	
	
}
