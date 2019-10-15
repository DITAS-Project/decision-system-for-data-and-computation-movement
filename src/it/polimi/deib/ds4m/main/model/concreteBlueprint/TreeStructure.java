/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(value = { "attributesLinked" })
public class TreeStructure  implements Serializable
{
	
	private static final long serialVersionUID = 3205680962179501758L;
	//tree structure
	String type;//type of decoposition
	ArrayList<TreeStructure> leaves; //the leaves of the goal
	ArrayList<TreeStructure> children;	//the children ( decomposed goals, the goal structure)
	
	//goal part
	ArrayList<String> attributes;
	ArrayList<Attribute> attributesLinked;
	String ID;
	String description;
	Double weight;
	
	
	/**
	 * recursive function that returns all leaf goals
	 * 
	 * @param treeStructure
	 * @param leaves the returns set of leaves. a array list ( empty must be passed)
	 */
	public static void getAllLeaves(TreeStructure treeStructure, ArrayList<TreeStructure> leaves)
	{
		
		if (treeStructure.getChildren()!=null)//if the are children, then inspect the tree
			for (TreeStructure cildren : treeStructure.getChildren())
				getAllLeaves(cildren, leaves);
		
		//when you come back from iteration add all leaves 
		if (treeStructure.getLeaves()!=null)
			leaves.addAll(treeStructure.getLeaves());
				
	}
	
	public static TreeStructure getLeafByID(TreeStructure treeStructure, String ID)
	{
		if (treeStructure== null)
				return null;
		
		//retrieve all leaves
		ArrayList<TreeStructure> leaves = new ArrayList<TreeStructure>();
		TreeStructure.getAllLeaves(treeStructure, leaves);
		
		//search for the id
		for(TreeStructure leaf: leaves )
		{
			if (leaf.getID().equals(ID))
				return leaf;
		}
		
		
		//if this point is reached it means no goal has been found. 
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!TreeStructure.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final TreeStructure other = (TreeStructure) obj;	    
	    if (!this.ID.equals(other.getID()) ) {
	        return false;
	    }
	    
	    if (!this.description.equals(other.getDescription()) ) {
	        return false;
	    }
	    
	    if (!this.weight.equals(other.getWeight()) ) {
	        return false;
	    }
	    
	    if (!this.attributes.equals(other.getAttributes()) ) {
	        return false;
	    }
	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.leaves.equals(other.getLeaves()) ) {
	        return false;
	    }
	    
	    if (!this.children.equals(other.getChildren()) ) {
	        return false;
	    }
	    
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(description).
            append(ID).
            append(weight).
            append(attributes).
            append(attributesLinked).
            append(type).
            append(leaves).
            append(children).
            toHashCode();
    }
	
	
	//recursive declaration
    //specify the argument to null, if i want to create a goal instead of part of a strucyure
	@JsonCreator
	public TreeStructure(@JsonProperty("children") ArrayList<TreeStructure> childern) {
		this.children=childern;
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
	 * @return the childern
	 */
	public ArrayList<TreeStructure> getChildren() {
		return children;
	}

	/**
	 * @param childern the childern to set
	 */
	public void setChildren(ArrayList<TreeStructure> children) {
		this.children = children;
	}


	/**
	 * @return the leaves
	 */
	public ArrayList<TreeStructure> getLeaves() {
		return leaves;
	}

	/**
	 * @param leaves the leaves to set
	 */
	public void setLeaves(ArrayList<TreeStructure> leaves) {
		this.leaves = leaves;
	}

	/**
	 * @return the attributes
	 */
	public ArrayList<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(ArrayList<String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the attributesLinked
	 */
	public ArrayList<Attribute> getAttributesLinked() {
		
		if (attributesLinked==null)
			attributesLinked = new ArrayList<Attribute>();
		
		return attributesLinked;
	}

	/**
	 * @param attributesLinked the attributesLinked to set
	 */
	public void setAttributesLinked(ArrayList<Attribute> attributesLinked) {
		this.attributesLinked = attributesLinked;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	

}
