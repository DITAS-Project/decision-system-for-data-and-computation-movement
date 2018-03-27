package it.polimi.deib.ds4m.main.model.applicationRequirement;

import java.util.Vector;

import com.google.gson.annotations.SerializedName;

public class TreeStructure 
{
	@SerializedName ("type") 
	String type;
	
	
	Vector<TreeStructure> childern;
	
	String goalRef;
	
	/**
	 * @return the decompositionType
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param decompositionType the decompositionType to set
	 */
	public void setType(String type) {
		this.type = type;
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
	public void setChildern(Vector<TreeStructure> childern) 
	{
		
//		if (children instanceOf String)
//			goalRef=children;
//		else
//			Vector<TreeStructure>
		
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
