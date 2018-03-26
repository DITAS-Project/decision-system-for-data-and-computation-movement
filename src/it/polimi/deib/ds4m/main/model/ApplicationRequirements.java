package it.polimi.deib.ds4m.main.model;

public class ApplicationRequirements 
{
	Resource resource;
	GoalTrees goalTrees;

	//singleton 
	private static ApplicationRequirements instance = null;
	
	//singleton protected constructor
	protected ApplicationRequirements() 
	{}
	
	//singleton, get instance,
	//it creates a new instance the first time it is called
	public static ApplicationRequirements getInstance() 
	{
	      if(instance == null) 
	      {
	         instance = new ApplicationRequirements();
	      }
	      return instance;
	   }
	
	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}
	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}
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

}
