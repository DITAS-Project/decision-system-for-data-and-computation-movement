package it.polimi.deib.ds4m.main.movement;

import java.util.Vector;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.Violations;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Method;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Metric;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class ManageGoalTree {
	
	public static Goal findViolatedGoals(Violations violations, Vector<VDC> VDCs)
	{
		
		//for the time being select only the first violation
        Violation violation = violations.getViolations().firstElement();
        
        //select the correct VDC
        VDC vdc = null;
        for(VDC vdcExamined : VDCs)
        {
        	if (vdcExamined.getId().equals(violation.getVdcID()))
        		vdc = vdcExamined;
        }
        
        //if not found return null
		if (vdc == null)
			return null;
        
		//retrieve the method that violated from the vdc
		Method method = null;
		for (Method methodExamined : vdc.getDataManagement().getMethods())
		{
			if (methodExamined.getName().equals(violation.getMethodID()))
					method = methodExamined;
		}
		//if not found return null
		if (method == null)
			return null;
		
		//retrieve all violated goals
		Vector<Goal> violatedGoals = new Vector<Goal>();
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
							if (property.getUtilityFunction().equals("Maximize") && property.getMinimum()<violation.getValue())
								violatedGoals.add(goal);//skip duplicated goals??? //no problems for false positives since all properties are in AND.
							else if (property.getUtilityFunction().equals("Minimize") && property.getMaximum()>violation.getValue())
								violatedGoals.add(goal);
				//}
			}
		}
		
		
		
		//retrieve the data sources used by the method
		//TODO: to insert binding in blueprint
		Vector<DataSource> dataSources = vdc.getDataSources();
		
		//for each data source involved in the method
		for (DataSource dataSource : dataSources)
		{
			//retrieve the movements
			Vector<Movement> Movements = dataSource.getMovements();
			
			//for each movements check if it has a positive impact on the goal
		}
		
		return null;
	}

}
