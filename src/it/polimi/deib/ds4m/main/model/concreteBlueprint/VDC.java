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

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

public class VDC implements Serializable
{	
	private static final long serialVersionUID = 3890941145555125425L;
	
	//vector of requirements
	private ArrayList<DataManagement> dataManagement;
	private ArrayList<AbstractProperty> abstractProperties;
	private ArrayList<DataSource> dataSources;
	private String id;
	private ArrayList<Infrastructure> infrastructures;
	private Infrastructure currentInfrastructure;
	
	private  ArrayList<Method> methodsInputs;
	private ArrayList<DAL> DALs;
	
    @JsonIgnore
    private ArrayList<Movement> movements;
    
    @JsonIgnore
    private Movement nextMovement;
	
	
	/**
	 * @return the dataSources
	 */
	public ArrayList<DataSource> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(ArrayList<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the dataManagement
	 */
	public ArrayList<DataManagement> getDataManagement() {
		return dataManagement;
	}

	/**
	 * @param dataManagement the dataManagement to set
	 */
	public void setDataManagement(ArrayList<DataManagement> dataManagement) {
		this.dataManagement = dataManagement;
	}

	/**
	 * @return the abstractProperties
	 */
	public ArrayList<AbstractProperty> getAbstractProperties() {
		return abstractProperties;
	}

	/**
	 * @param abstractProperties the abstractProperties to set
	 */
	public void setAbstractProperties(ArrayList<AbstractProperty> abstractProperties) {
		this.abstractProperties = abstractProperties;
	}

	/**
	 * connects the attributes in the abstract properties with the attributes in the data management, using the id link.
	 * it populates the  attribute linked of the leaves in the abstract property objects.
	 */
	public void connectAbstractProperties()
	{
		for (AbstractProperty abstractProperty : abstractProperties)
		{
			//select the data management with correspondent methodID
			DataManagement selectedDM = null;
			for(DataManagement dm : dataManagement)
			{
				if (abstractProperty.getMethod_id().equals(dm.getMethod_id()))
				{
					selectedDM = dm;
					break;
				}
			}
			
			if (selectedDM == null)
				return;
			
			//for each tree
			//navigate at the end of the tree
			
			//dataUtility
			connect(abstractProperty.getGoalTrees().getDataUtility(), selectedDM.getAttributes().dataUtility);

			//TODO fix array
//			//privacy
//			connect(abstractProperty.getGoalTrees().getPrivacy(), selectedDM.getAttributes().privacy);
//			
//			//security
//			connect(abstractProperty.getGoalTrees().getSecurity(), selectedDM.getAttributes().security);

			
		}
	}
	
	/**
	 * recursive function that inspect the tree structure, examine the IDs and connects the attributes of absract properties wityh data management.
	 * 
	 * @param treeStructure
	 * @param attributes
	 */
	private void connect(TreeStructure treeStructure, ArrayList<Attribute> attributes)
	{
		//if (treeStructure.getLeaves()==null)
		if (treeStructure.getChildren()!=null)
			for (TreeStructure cildren : treeStructure.getChildren())
				connect(cildren, attributes);
		else
			for (TreeStructure leaf : treeStructure.getLeaves())
				for (String attributeLeaf: leaf.getAttributes())
					for (Attribute attributeDM : attributes)
						if (attributeDM.getId().equals(attributeLeaf))  
							leaf.getAttributesLinked().add(attributeDM);
							
				
	}

	
	/**
	 * @return the movements
	 */
	public ArrayList<Movement> getMovements() {
		return movements;
	}
	/**
	 * @param movements the movements to set
	 */
	public void setMovements(ArrayList<Movement> movements) {
		this.movements = movements;
	}

	/**
	 * @return the methodsInputs
	 */
	public ArrayList<Method> getMethodsInputs() {
		return methodsInputs;
	}

	/**
	 * @param methodsInputs the methodsInputs to set
	 */
	public void setMethodsInputs(ArrayList<Method> methodsInputs) {
		this.methodsInputs = methodsInputs;
	}

	/**
	 * @return the dALs
	 */
	public ArrayList<DAL> getDALs() {
		return DALs;
	}

	/**
	 * @param dALs the dALs to set
	 */
	public void setDALs(ArrayList<DAL> dALs) {
		DALs = dALs;
	}

	/**
	 * @return the currentInfrastructure
	 */
	public Infrastructure getCurrentInfrastructure() {
		return currentInfrastructure;
	}

	/**
	 * @param currentInfrastructure the currentInfrastructure to set
	 */
	public void setCurrentInfrastructure(Infrastructure currentInfrastructure) {
		this.currentInfrastructure = currentInfrastructure;
	}

	/**
	 * @return the infrastructures
	 */
	public ArrayList<Infrastructure> getInfrastructures() {
		return infrastructures;
	}

	/**
	 * @param infrastructures the infrastructures to set
	 */
	public void setInfrastructures(ArrayList<Infrastructure> infrastructures) {
		this.infrastructures = infrastructures;
	}

	/**
	 * @return the nextMovement
	 */
	public Movement getNextMovement() {
		return nextMovement;
	}

	/**
	 * @param nextMovement the nextMovement to set
	 */
	public void setNextMovement(Movement nextMovement) {
		this.nextMovement = nextMovement;
	}
	
	
	
}
