package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;

public class GoalTree 
{
	ArrayList<Goal> goals;
	TreeStructure treeStructure;
	
	/**
	 * @return the goals
	 */
	public ArrayList<Goal> getGoals() {
		return goals;
	}
	/**
	 * @param goals the goals to set
	 */
	public void setGoals(ArrayList<Goal> goals) {
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
