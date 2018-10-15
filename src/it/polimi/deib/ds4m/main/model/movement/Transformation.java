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

import java.util.ArrayList;

public class Transformation 
{
	private String type;
	
	private ArrayList<String> positiveImpacts;
	private ArrayList<String> negativeImpacts;
	
	private ArrayList<Cost> costs;
	
	public Transformation() 
	{
		positiveImpacts = new ArrayList<String>();
		negativeImpacts = new ArrayList<String>();
		
		costs = new ArrayList<Cost>();
	}
	
	public Transformation(String type, ArrayList<String> positiveImpacts, ArrayList<String> negativeImpacts, ArrayList<Cost> costs)
	{
		this.type=type;
		this.positiveImpacts=positiveImpacts;
		this.negativeImpacts=negativeImpacts;
		this.costs=costs;
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

}
