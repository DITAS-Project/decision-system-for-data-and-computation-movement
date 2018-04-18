package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.Vector;

public class GoalTree 
{
	Vector<Goal> goals;
	TreeStructure treeStructure;
	
	/**
	 * @return the goals
	 */
	public Vector<Goal> getGoals() {
		return goals;
	}
	/**
	 * @param goals the goals to set
	 */
	public void setGoals(Vector<Goal> goals) {
		this.goals = goals;
	}
	/**
	 * @return the treeStructure
	 */
	public TreeStructure getTreeStructure() {
		return treeStructure;
	}
	/**
	 * @param treeStructure the treeStructure to set
	 */
	public void setTreeStructure(TreeStructure treeStructure) {
		this.treeStructure = treeStructure;
	}

}
