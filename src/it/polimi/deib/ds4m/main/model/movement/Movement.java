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
package it.polimi.deib.ds4m.main.model.movement;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Movement implements Serializable
{
	private static final long serialVersionUID = -453139816550873541L;
	private String type; //DataMovement,DataDuplication, ComputationMovement, ComputationDuplication
	private String fromType;//cloud/edge
	private String toType;//cloud/esdge
	private double restTime;
	
	//linked to data source class, when the movement is instantiated
	@JsonIgnore
	private Infrastructure fromLinked;
	@JsonIgnore
	private Infrastructure toLinked;
	@JsonIgnore
	private DAL dalToMove;
	
	private ArrayList<String> positiveImpacts; //list of IDs of goals with a positive impact
	private ArrayList<String> negativeImpacts; //list of IDs of goals with a negative impact
	
	private ArrayList<Transformation> transformations;
	
	private ArrayList<Cost> costs;
	
	

	//default creator
	public Movement()
	{
		positiveImpacts = new ArrayList<String>();
		negativeImpacts = new ArrayList<String>();
		
		transformations = new ArrayList<Transformation>();
		
	}
	
	public Movement(String type, String fromType, String toType, ArrayList<String> positiveImpacts, ArrayList<String> negativeImpacts, ArrayList<Transformation> transformations, ArrayList<Cost> costs, double restTime ) 
	{
		this.type=type;
		this.fromType=fromType;
		this.toType=toType;
		this.positiveImpacts=positiveImpacts;
		this.negativeImpacts=negativeImpacts;
		this.transformations=transformations;
		this.costs=costs;
		this.restTime=restTime;
	}

	/**
	 * @return the positiveImpacts
	 */
	public ArrayList<String> getPositiveImpacts() {
		return positiveImpacts;
	}

	/**
	 * @param positiveImpacts the positiveImpacts to set
	 */
	public void setPositiveImpacts(ArrayList<String> positiveImpacts) {
		this.positiveImpacts = positiveImpacts;
	}

	/**
	 * @return the negativeImpacts
	 */
	public ArrayList<String> getNegativeImpacts() {
		return negativeImpacts;
	}

	/**
	 * @param negativeImpacts the negativeImpacts to set
	 */
	public void setNegativeImpacts(ArrayList<String> negativeImpacts) {
		this.negativeImpacts = negativeImpacts;
	}

	/**
	 * @return the transformations
	 */
	public ArrayList<Transformation> getTransformations() {
		return transformations;
	}

	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(ArrayList<Transformation> transformations) {
		this.transformations = transformations;
	}

	/**
	 * @return the costs
	 */
	public ArrayList<Cost> getCosts() {
		return costs;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(ArrayList<Cost> costs) {
		this.costs = costs;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set (movement, replication)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the restTime
	 */
	public double getRestTime() {
		return restTime;
	}

	/**
	 * @param restTime the restTime to set
	 */
	public void setRestTime(double restTime) {
		this.restTime = restTime;
	}
	
	
	//ATTENTION: the comparison does not consider the impacts 
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Movement.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Movement other = (Movement) obj;	    
	    if (!this.type.equals(other.getType()) ) {
	        return false;
	    }
	    
	    if (!this.fromType.equals(other.getFromType()) ) {
	        return false;
	    }
	    
	    if (!this.toType.equals(other.getToType()) ) {
	        return false;
	    }
	    
	    if (this.restTime!=other.getRestTime() ) {
	        return false;
	    }
	    
//	    if (!this.positiveImpacts.equals(other.getPositiveImpacts()) ) {
//	        return false;
//	    }
//	    
//	    if (!this.negativeImpacts.equals(other.getNegativeImpacts()) ) {
//	        return false;
//	    }
	    
	    if (!this.costs.equals(other.getCosts()) ) {
	        return false;
	    }
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(type).
            append(toType).
            append(fromType).
            append(restTime).
//            append(positiveImpacts).
//            append(negativeImpacts).
            append(costs).
            toHashCode();
    }


	/**
	 * @return the fromType
	 */
	public String getFromType() {
		return fromType;
	}

	/**
	 * @param fromType the fromType to set
	 */
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	/**
	 * @return the toType
	 */
	public String getToType() {
		return toType;
	}

	/**
	 * @param toType the toType to set
	 */
	public void setToType(String toType) {
		this.toType = toType;
	}

	/**
	 * @return the fromLinked
	 */
	public Infrastructure getFromLinked() {
		return fromLinked;
	}

	/**
	 * @param fromLinked the fromLinked to set
	 */
	public void setFromLinked(Infrastructure fromLinked) {
		this.fromLinked = fromLinked;
	}

	/**
	 * @return the toLinked
	 */
	public Infrastructure getToLinked() {
		return toLinked;
	}

	/**
	 * @param toLinked the toLinked to set
	 */
	public void setToLinked(Infrastructure toLinked) {
		this.toLinked = toLinked;
	}

	/**
	 * @return the dalToMove
	 */
	public DAL getDalToMove() {
		return dalToMove;
	}

	/**
	 * @param dalToMove the dalToMove to set
	 */
	public void setDalToMove(DAL dalToMove) {
		this.dalToMove = dalToMove;
	}
	

}
