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
package it.polimi.deib.ds4m.main.movement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polimi.deib.ds4m.main.model.Metric;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Attribute;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

public class GoalTreeManager {
	
	//definition of possible strategies
	public String TreeType = ""; //DATAUTILITY, SECURITY, PRIVACY before was an enum but modified because on issues of Eclipse
	
	//examine 1 volation at time.
	//Multiple violations refer to multiple method, with diffrent goal threes
	/**
	 * The method examines the goal three of the method who violated the requirements, and retrieves the set of violated goals. 
	 * 
	 * @param violation the violation reported by the SLA manager
	 * @param violatedVDC the violated VDC
	 * @return the set of violated goals, null if method is not found
	 */
	public static Set<TreeStructure> findViolatedGoals(Violation violation, VDC violatedVDC)
	{
        
		//retrieve the method that violated from the vdc
		AbstractProperty abstractProperty = null;
		
		//find the method that violated the requirements
		for (AbstractProperty abstractPropertyExamined : violatedVDC.getAbstractProperties())
		{
			if (abstractPropertyExamined.getMethod_id().equals(violation.getMethodId()))//I take the first item, all violations have the same method ID for previous filtering 
				abstractProperty = abstractPropertyExamined;
		}
		//if not found return null
		if (abstractProperty == null)
			return null;
		
		//create set of goal add all the violated ones
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		
		//retrieve the violated goals for all violations
		for (Metric metric: violation.getMetrics())
		{
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, metric, "DATAUTILITY"));
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, metric, "SECURITY"));		
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, metric, "PRIVACY"));
			
			//TODO: check the goal tree
			
		}

		//at this point the violatedGoals contains a set of violated goals

		
		return violatedGoals;
	}

	/**
	 * returns all leaf goals that are violated
	 * 
	 * @param abstractProperty the goal model
	 * @param violation the set of attributes violated ( violation)
	 * @param treeType the type of three to inspect ( of type treeType, i.e., DataUtility, Security, Privacy)
	 * @return the set of violated goal
	 */
	@SuppressWarnings("unlikely-arg-type")
	private static Set<TreeStructure> searchViolatedGoal(AbstractProperty abstractProperty, Metric metric, String treeType)
	{
		
		//retrieve all violated goals
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();//hash set since goal cannot be duplicated// implemented equals for goal and sub classes
		
		ArrayList<TreeStructure> leaves = new ArrayList<TreeStructure>();
		
		//collect all leaf goals
		switch (treeType) {
		case "DATAUTILITY":
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getDataUtility(),leaves);
			break;
			
		case "PRIVACY":
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getPrivacy(),leaves);
			break;
			
		case "SECURITY":
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getSecurity(),leaves);
		}
		
		if (leaves.size() == 0)
			return violatedGoals;
		
		for (TreeStructure leaf : leaves)//retrieves all goals f the method
		{
			ArrayList<Attribute> attributes = leaf.getAttributesLinked();//get the metrics on the goal
			for (Attribute attribute : attributes)
			{
				for (Map.Entry<String, Property>  property : attribute.getProperties().entrySet())
				{
					if (property.getKey().toString().toLowerCase().equals(metric.getKey().toLowerCase()))//check the violation name is the same. 
						//TODO: when i will include multiple violations  before this i will need a "foreach" for each violation 
						//i assume that if ""minimum or maximum are set then value is a number ( Double)
						if (property.getValue().getMinimum()!=null && property.getValue().getMaximum()==null && property.getValue().getMinimum() >= Double.parseDouble(metric.getValue()))
							violatedGoals.add(leaf);//skip duplicated goals, no problems for false positives since all properties are in AND.
						else if (property.getValue().getMinimum()==null && property.getValue().getMaximum()!=null && property.getValue().getMaximum() <= Double.parseDouble(metric.getValue()))
							violatedGoals.add(leaf);
						else if (property.getValue().getMinimum()!=null && property.getValue().getMaximum()!=null && 
								(
										property.getValue().getMinimum()< Double.parseDouble(metric.getValue()) // if it is out of range below OR 
										|| property.getValue().getMaximum() > Double.parseDouble(metric.getValue())) //it is out of range above
								)
							violatedGoals.add(leaf); //then it it is violated
						else if (property.getValue().getMinimum()==null && property.getValue().getMaximum()==null && !property.getValue().equals(metric.getValue()))
							violatedGoals.add(leaf);
					
				}
			}
		}
		
		
		return violatedGoals;
	}
	
}
