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
package it.polimi.deib.ds4m.main.model.movementEnaction;

import java.util.ArrayList;

import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movement.Transformation;

public class MovementEnaction 
{
	private String from;
	private String to;
	private ArrayList<String> transformations;
	private String type;
	private String DALid;
	private  ArrayList<Method> methodsInputs;
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the transformations
	 */
	public ArrayList<String> getTransformations() {
		return transformations;
	}
	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(ArrayList<String> transformations) {
		this.transformations = transformations;
	}
	
	public void importMovement (Movement movement, ArrayList<Method> methodsInputs) 
	{
		this.from = movement.getFromLinked().getName();
		this.to = movement.getToLinked().getName();
		this.type = movement.getType();
		this.DALid = movement.getDalToMove().getOriginal_ip();
		this.methodsInputs=methodsInputs;
		
		
		if (transformations==null)
			transformations = new ArrayList<String>();
		
		if (movement.getTransformations()!=null)	
			for(Transformation transformationMovement : movement.getTransformations())
			{
				transformations.add(transformationMovement.getType());
			}
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
	 * @return the dALid
	 */
	public String getDALid() {
		return DALid;
	}
	/**
	 * @param dALid the dALid to set
	 */
	public void setDALid(String dALid) {
		DALid = dALid;
	}


}
