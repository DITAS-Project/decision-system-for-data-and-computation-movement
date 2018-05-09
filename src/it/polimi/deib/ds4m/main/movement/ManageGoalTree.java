package it.polimi.deib.ds4m.main.movement;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Method;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Metric;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

public class ManageGoalTree {
	
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
	public static Set<Goal> findViolatedGoals(Violation violation, VDC violatedVDC)
	{
        
		//retrieve the method that violated from the vdc
		Method method = null;
		for (Method methodExamined : violatedVDC.getDataManagement().getMethods())
		{
			if (methodExamined.getName().equals(violation.getMethodID()))
					method = methodExamined;
		}
		//if not found return null
		if (method == null)
			return null;
		
		//create set of goal add all the violated ones
		Set<Goal> violatedGoals = searchGoal(method, violation, TreeType.DATAUTILITY);
		violatedGoals.addAll(searchGoal(method, violation, TreeType.SECURITY));		
		violatedGoals.addAll(searchGoal(method, violation, TreeType.PRIVACY));

		//at this point the violatedGoals contains a set of violated goals
		
		//TODO: check the tree

		
		return violatedGoals;
	}

	private static Set<Goal> searchGoal(Method method, Violation violation, TreeType treeType)
	{
		
		//retrieve all violated goals
		Set<Goal> violatedGoals = new HashSet<Goal>();//hash set since goal cannot be duplicated// implemented equals for goal and sub classes
		
		Vector<Goal> goals = null;
		
		switch (treeType) {
		case DATAUTILITY:
			goals = method.getConstraints().getDataUtility().getGoals();
			break;
			
		case PRIVACY:
			goals = method.getConstraints().getPrivacy().getGoals();
			break;
			
		case SECURITY:
			goals = method.getConstraints().getSecurity().getGoals();
		}
		
		
		if (goals == null)
			return null;
		
		for (Goal goal : goals)//retrieves all goals f the method
		{
			Vector<Metric> Metrics = goal.getMetrics();//get the metrics on the goal
			for (Metric metric : Metrics)
			{
				//if the metric is the same //skipped this check since violations are not sent grouped by metrics? 
				//if (metric.getName().equals(violation.getMetric()))
				//{
					//if the values are actually violated, the add it
					Vector<Property> properties = metric.getProperties();
					for (Property property : properties)
						if (property.getName().equals(violation.getMetric()))
							//i assume that if ""minimum or maximum are set then value is a number ( Double)
							if (property.getMinimum()!=null && property.getMaximum()==null && property.getMinimum() < Double.parseDouble(violation.getValue()))
								violatedGoals.add(goal);//skip duplicated goals, no problems for false positives since all properties are in AND.
							else if (property.getMinimum()==null && property.getMaximum()!=null && property.getMaximum() > Double.parseDouble(violation.getValue()))
								violatedGoals.add(goal);
							else if (property.getMinimum()!=null && property.getMaximum()!=null && 
									property.getMinimum()< Double.parseDouble(violation.getValue()) 
									&& property.getMaximum() > Double.parseDouble(violation.getValue()))
								violatedGoals.add(goal);
							else if (property.getMinimum()==null && property.getMaximum()==null && property.getValue().equals(violation.getValue()))
								violatedGoals.add(goal);
				//}
			}
		}
		
		
		return null;
	}
	
}
