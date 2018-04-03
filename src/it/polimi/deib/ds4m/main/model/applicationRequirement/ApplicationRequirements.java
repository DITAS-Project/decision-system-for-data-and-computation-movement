package it.polimi.deib.ds4m.main.model.applicationRequirement;

public class ApplicationRequirements 
{
	ResourcesDataAdmin resourcesDataAdmin;
	GoalTrees goalTrees;
	

	
	/**
	 * @return the goalTrees
	 */
	public GoalTrees getGoalTrees() {
		return goalTrees;
	}
	/**
	 * @param goalTrees the goalTrees to set
	 */
	public void setGoalTrees(GoalTrees goalTrees) {
		this.goalTrees = goalTrees;
	}
	/**
	 * @return the resourcesDataAdmin
	 */
	public ResourcesDataAdmin getResourcesDataAdmin() {
		return resourcesDataAdmin;
	}
	/**
	 * @param resourcesDataAdmin the resourcesDataAdmin to set
	 */
	public void setResourcesDataAdmin(ResourcesDataAdmin resourcesDataAdmin) {
		this.resourcesDataAdmin = resourcesDataAdmin;
	}

}
