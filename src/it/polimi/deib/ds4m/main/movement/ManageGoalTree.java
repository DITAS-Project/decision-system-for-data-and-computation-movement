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
	
	//examine 1 volation at time.
	//multipole violation reres to multiole method, with diffrente goal threes
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
		
		//retrieve all violated goals
		Set<Goal> violatedGoals = new HashSet<Goal>();//hashset since goal cannot be duplicated// implemented equals for goal and sub classes
		for (Goal goal : method.getConstraints().getDataUtility().getGoals())
		{
			Vector<Metric> Metrics = goal.getMetrics();
			for (Metric metric : Metrics)
			{
				//if the metric is the same //skipped this chke since violations are not sent grouped by metrics? 
				//if (metric.getName().equals(violation.getMetric()))
				//{
					//if the values are actually violated, the add it
					Vector<Property> properties = metric.getProperties();
					for (Property property : properties)
						if (property.getName().equals(violation.getMetric()))
							//i assume that if ""minimum or maximum are set then value is a number ( Duble)
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
		
		//at this point the violatedGoals should contains a set of violated goals
		
		//TODO: check the tree

		
		return violatedGoals;
	}

}
