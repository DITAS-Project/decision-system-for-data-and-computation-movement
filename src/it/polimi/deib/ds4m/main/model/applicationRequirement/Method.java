package it.polimi.deib.ds4m.main.model.applicationRequirement;

public class Method 
{
	String name;
	GoalTrees constraints;
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
	 * @return the constraints
	 */
	public GoalTrees getConstraints() {
		return constraints;
	}
	/**
	 * @param constraints the constraints to set
	 */
	public void setConstraints(GoalTrees constraints) {
		this.constraints = constraints;
	}


}
