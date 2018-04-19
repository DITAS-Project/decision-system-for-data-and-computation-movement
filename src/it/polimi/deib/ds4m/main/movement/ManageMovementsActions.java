package it.polimi.deib.ds4m.main.movement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class ManageMovementsActions 
{
	public enum Strategy
	{
		COSTS,
		TIME,
		POSITIVEIMPACTS		
	}
	
	public static Boolean instantiateMovementActions(List<DataSource> dataSources, String movementsJSON) 
	{
		//deep copy movements, raggiunto serializzando di volta in volta
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(movementsJSON);
			
			//retrieve movements
			JsonNode movementNode = root.get("movements");		
			Vector<Movement> movements;
			
			//assigned copied 
			for(DataSource dataSource: dataSources)
			{
				movements = new Vector<Movement> (Arrays.asList(mapper.treeToValue(movementNode, Movement[].class)));
				dataSource.setMovements(movements);
				movements = null;
			}
			
			return true;
			
		} catch (IOException e) 
		{
			return false;
		}

	}
	
	public static Vector<Movement> findMovementAction(Set<Goal> violatedGoals, VDC vdc)
	{
		//retrieve the data sources used by the method
		//TODO: to insert binding in blueprint
		Vector<DataSource> dataSources = vdc.getDataSources();
		
		//create vector of movements to be enacted 
		Vector<Movement> movementsToBeEnacted = new Vector<Movement>();
		
		//for each data source involved in the method
		for (DataSource dataSource : dataSources)
		{
			//retrieve the movements
			Vector<Movement> movements = dataSource.getMovements();
			
			//for each movements check if it has a positive impact on the goal
			for (Movement movement : movements)
			{
				//for each impact, check if present in the list of violated goals
				for (String impact : movement.getPositiveImpacts())
				{
					for(Goal goal: violatedGoals)
					{
						if (impact.equals(goal.getID()))
							movementsToBeEnacted.add(movement);
					}
				}
			}
		}
		
		
		return movementsToBeEnacted;
	}
	
	public static Vector<Movement> orderMovementAction(Vector<Movement> movementsToBeEnacted, Strategy strategy)
	{
		
		switch(strategy) {
			case COSTS: 
				Collections.sort(movementsToBeEnacted, new ManageMovementsActions().new MonetaryCostComparator());
				break;
			case TIME: 
				Collections.sort(movementsToBeEnacted, new ManageMovementsActions().new TimeCostComparator());
				break;
			case POSITIVEIMPACTS:
				System.err.println("positive impact ordering non implemented yet");
				break;
			default:
				break;
		
		}

		
		
		return movementsToBeEnacted;
	}
	
	
	//assume same measure unit
	class MonetaryCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	Vector<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("monetary"))
	    			costA=cost;
	    	
	    	Vector<Cost> costsB = b.getCosts();
	    	Cost costB=null; 
	    	for (Cost cost: costsB)
	    		if (cost.getType().equals("monetary"))
	    			costB=cost;
	    	
	    	if (costA.getValue() < costB.getValue())
	    		return -1;
	    	else if (costA.getValue() > costB.getValue())
	    		return 1;
	    	else
	    		return 0;
	    }
	}
	
	//assume same measure unit
	class TimeCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	Vector<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("time"))
	    			costA=cost;
	    	
	    	Vector<Cost> costsB = b.getCosts();
	    	Cost costB=null; 
	    	for (Cost cost: costsB)
	    		if (cost.getType().equals("time"))
	    			costB=cost;
	    	
	    	if (costA.getValue() < costB.getValue())
	    		return -1;
	    	else if (costA.getValue() > costB.getValue())
	    		return 1;
	    	else
	    		return 0;
	    }
	}

}
