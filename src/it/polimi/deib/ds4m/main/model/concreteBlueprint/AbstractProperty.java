package it.polimi.deib.ds4m.main.model.concreteBlueprint;

public class AbstractProperty 
{
	String method_id;
	GoalTrees goalTrees;
	/**
	 * @return the method_id
	 */
	public String getMethod_id() {
		return method_id;
	}
	/**
	 * @param method_id the method_id to set
	 */
	public void setMethod_id(String method_id) {
		this.method_id = method_id;
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
