package it.polimi.deib.ds4m.main.model.applicationRequirement;

public class GoalTrees 
{
	GoalTree dataUtility;
	GoalTree security;
	GoalTree privacy;
	
	/**
	 * @return the dataUtility
	 */
	public GoalTree getDataUtility() {
		return dataUtility;
	}
	/**
	 * @param dataUtility the dataUtility to set
	 */
	public void setDataUtility(GoalTree dataUtility) {
		this.dataUtility = dataUtility;
	}
	/**
	 * @return the security
	 */
	public GoalTree getSecurity() {
		return security;
	}
	/**
	 * @param security the security to set
	 */
	public void setSecurity(GoalTree security) {
		this.security = security;
	}
	/**
	 * @return the privacy
	 */
	public GoalTree getPrivacy() {
		return privacy;
	}
	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacy(GoalTree privacy) {
		this.privacy = privacy;
	}

}
