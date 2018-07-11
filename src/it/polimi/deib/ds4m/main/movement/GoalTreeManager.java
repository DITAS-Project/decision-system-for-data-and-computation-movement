package it.polimi.deib.ds4m.main.movement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Attribute;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

public class GoalTreeManager {
	
	//definition of possible strategies
	public enum TreeType
	{
		DATAUTILITY,
		SECURITY,
		PRIVACY
	}
	
	//examine 1 volation at time.
	//Multiple violations refer to multiple method, with diffrent goal threes
	/**
	 * The method examines the goal three of the method who violated the requirements, and retrieves the set of violated goals. 
	 * 
	 * @param violation the violation reported by the SLA manager
	 * @param violatedVDC the violated VDC
	 * @return the set of violated goals, null if method is not found
	 */
	public static Set<Goal> findViolatedGoals(ArrayList<Violation> violations, VDC violatedVDC)
	{
        
		//retrieve the method that violated from the vdc
		AbstractProperty abstractProperty = null;
		
		//find the method that violated the requirements
		for (AbstractProperty abstractPropertyExamined : violatedVDC.getAbstractProperties())
		{
			if (abstractPropertyExamined.getMethod_id().equals(violations.get(0).getMethodID()))//I take the first item, all violations have the seame method ID for previous filtering 
				abstractProperty = abstractPropertyExamined;
		}
		//if not found return null
		if (abstractProperty == null)
			return null;
		
		//create set of goal add all the violated ones
		Set<Goal> violatedGoals = new HashSet<Goal>();
		
		//retrieve the violated goals for all violations
		for (Violation violation: violations)
		{
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, violation, TreeType.DATAUTILITY));
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, violation, TreeType.SECURITY));		
			violatedGoals.addAll(searchViolatedGoal(abstractProperty, violation, TreeType.PRIVACY));
			
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
	private static Set<Goal> searchViolatedGoal(AbstractProperty abstractProperty, Violation violation, TreeType treeType)
	{
		
		//retrieve all violated goals
		Set<Goal> violatedGoals = new HashSet<Goal>();//hash set since goal cannot be duplicated// implemented equals for goal and sub classes
		
		ArrayList<Goal> leaves = new ArrayList<Goal>();
		
		//collect all leaf goals
		switch (treeType) {
		case DATAUTILITY:
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getDataUtility(),leaves);
			break;
			
		case PRIVACY:
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getPrivacy(),leaves);
			break;
			
		case SECURITY:
			TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getSecurity(),leaves);
		}
		
		if (leaves.size() == 0)
			return null;
		
		for (Goal leaf : leaves)//retrieves all goals f the method
		{
			ArrayList<Attribute> attributes = leaf.getAttributesLinked();//get the metrics on the goal
			for (Attribute attribute : attributes)
			{
				for (Map.Entry<String, Property>  property : attribute.getProperties().entrySet())
				{
					if (property.getKey().toString().toLowerCase().equals(violation.getMetric().toLowerCase()))//check the violation name is the same. TODO: when i will include multiple violation  before this i will need a "foreach" for each violation 
						//i assume that if ""minimum or maximum are set then value is a number ( Double)
						if (property.getValue().getMinimum()!=null && property.getValue().getMaximum()==null && property.getValue().getMinimum() >= Double.parseDouble(violation.getValue()))
							violatedGoals.add(leaf);//skip duplicated goals, no problems for false positives since all properties are in AND.
						else if (property.getValue().getMinimum()==null && property.getValue().getMaximum()!=null && property.getValue().getMaximum() <= Double.parseDouble(violation.getValue()))
							violatedGoals.add(leaf);
						else if (property.getValue().getMinimum()!=null && property.getValue().getMaximum()!=null && 
								(
										property.getValue().getMinimum()< Double.parseDouble(violation.getValue()) // if it is out of range below OR 
										|| property.getValue().getMaximum() > Double.parseDouble(violation.getValue())) //it is out of range above
								)
							violatedGoals.add(leaf); //then it it is violated
						else if (property.getValue().getMinimum()==null && property.getValue().getMaximum()==null && !property.getValue().equals(violation.getValue()))
							violatedGoals.add(leaf);
					
				}
			}
		}
		
		
		return violatedGoals;
	}
	
}
