package it.polimi.deib.ds4m.main.model;

import java.util.Vector;

public class TreeStructure 
{
	String decompositionType;
	Vector<TreeStructure> childern;
	String goalRef;
	
	/**
	 * @return the decompositionType
	 */
	public String getDecompositionType() {
		return decompositionType;
	}
	/**
	 * @param decompositionType the decompositionType to set
	 */
	public void setDecompositionType(String decompositionType) {
		this.decompositionType = decompositionType;
	}
	/**
	 * @return the childern
	 */
	public Vector<TreeStructure> getChildern() {
		return childern;
	}
	/**
	 * @param childern the childern to set
	 */
	public void setChildern(Vector<TreeStructure> childern) {
		this.childern = childern;
	}
	/**
	 * @return the goalRef
	 */
	public String getGoalRef() {
		return goalRef;
	}
	/**
	 * @param goalRef the goalRef to set
	 */
	public void setGoalRef(String goalRef) {
		this.goalRef = goalRef;
	}

}
