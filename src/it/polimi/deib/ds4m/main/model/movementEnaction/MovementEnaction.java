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

import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.methodsInput.DataSourceInput;
import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class MovementEnaction 
{
	private String from;
	private String to;
	//private ArrayList<String> transformations;
	private String type;
	private String DALid;
	//private  ArrayList<Method> methodsInputs;
	private ArrayList<DataSourceInput> dataSources;
	private String VDCid;
	
	//datasources
	//private ArrayList<DataSource> dataSourcesDAL;
	
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

	
	public void importMovement (Movement movement, VDC violatedVDC) 
	{
		this.from = movement.getFromLinked().getId();
		this.to = movement.getToLinked().getId();
		this.type = movement.getType();		
		this.VDCid= violatedVDC.getId();

		//this.setMethodsInputs(methodsInputs);
		
		//select the method which passed and is in the abstract properties of the concrete blueprint
		for (Method method : violatedVDC.getMethodsInputs())
		{
			//TODO:this selects and support only 1 method, it it is more than one only the first one will be selected
			if (method.getMethod_id().equalsIgnoreCase(violatedVDC.getAbstractProperties().get(0).getMethod_id()))
			{
				this.setDataSources(method.getDataSources());
				break;
			}
		}
		
		//If it's a computation movement there is no reference to DAL
		if (movement.getDalToMove()!=null)
			this.DALid = movement.getDalToMove().getId();
		
		
//		if (transformations==null)
//			transformations = new ArrayList<String>();
//		
//		if (movement.getTransformations()!=null)	
//			for(Transformation transformationMovement : movement.getTransformations())
//			{
//				transformations.add(transformationMovement.getType());
//			}
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
	public ArrayList<DataSourceInput> getDataSources() {
		return dataSources;
	}
	public void setDataSources(ArrayList<DataSourceInput> dataSources) {
		this.dataSources = dataSources;
	}
	/**
	 * @return the vDCid
	 */
	public String getVDCid() {
		return VDCid;
	}
	/**
	 * @param vDCid the vDCid to set
	 */
	public void setVDCid(String vDCid) {
		VDCid = vDCid;
	}

}
