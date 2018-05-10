package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TreeStructure 
{
	String type;//type of decoposition
	Vector<String> leaves; //the leaves of the goal
	Vector<TreeStructure> childern;	//the children ( decomposed goals)
	String goalRef; //to be removed?
	
	@JsonCreator
	public TreeStructure(@JsonProperty("children") Vector<TreeStructure> childern) {
		this.childern=childern;
	} 

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the leaves
	 */
	public Vector<String> getLeaves() {
		return leaves;
	}

	/**
	 * @param leaves the leaves to set
	 */
	public void setLeaves(Vector<String> leaves) {
		this.leaves = leaves;
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
