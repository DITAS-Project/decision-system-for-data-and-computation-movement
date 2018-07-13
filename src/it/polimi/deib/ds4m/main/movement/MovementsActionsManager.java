package it.polimi.deib.ds4m.main.movement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Resource;

/**
 * @author Mattia Salnitri
 * 
 * The class manages the movement actions
 *
 */
public class MovementsActionsManager 
{

	//definition of possible strategies
	public enum Strategy
	{
		MONETARY,
		TIME,
		POSITIVEIMPACTS		
	}
	
	/**
	 * The method instantiate the movement actions given a set of movement classes a set of data sources. 
	 * In particular, it creates a deep copy, by de-serializing the JSON of the movement classes, and assigning it to each data source 
	 * 
	 * @param dataSources set of data sources
	 * @param movementsJSON JSON representing the movement action classes
	 * @return The array list of instantiated movements, null if problems arise
	 */
	public static ArrayList<Movement> instantiateMovementActions(List<Resource> resources, String movementsJSON) 
	{
		//the container for the instantiated movement
		ArrayList<Movement> movements = new ArrayList<Movement>();
		
		//deep copy movements, done be de-serializing time by time 
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		
		try {
			root = mapper.readTree(movementsJSON);
			
			//retrieve movements
			JsonNode movementNode = root.get("movements");
			
			//assigned copied 
			for(Resource resource_source: resources)
			{
				for(Resource resource_target: resources)
				{
					//if it is the same resource don't instantiate data movement action
					if (resource_source.equals(resource_target))
						continue;

					//for a copy of resource add the data movement compatible 
					//this is the (deep copy), i create 1 new object for each new movement
					ArrayList<Movement> newMovements = new ArrayList<Movement> (Arrays.asList(mapper.treeToValue(movementNode, Movement[].class)));
					//set target and source
					for (Movement movement: newMovements)
					{
						if (
								movement.getToType().toLowerCase().equals(resource_target.getLocation().toLowerCase()) && //if it matches the location target (cloud/edge)
								movement.getFromType().toLowerCase().equals(resource_source.getLocation().toLowerCase()) &&//if it matches the location source (cloud/edge)
								resource_source.getType().equals(resource_target.getType()) //the type of the source and target should be the same
								
								)
						{
							//set the targets
							movement.setFromLinked(resource_source);
							movement.setToLinked(resource_target);
							
							//add to list of data movement action 
							movements.add(movement);
						}
					}
				}
			}
			
			return movements;
			
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * the method finds the movement actions that impact positively on a violated set of goals
	 * 
	 * @param violatedGoals the violated set of goals
	 * @param vdc the VDC that contains the method that violated the goals 
	 * @return the movements to be enacted that impact positively on the violated goals. a movement might impact on a subset of goals
	 */
	public static ArrayList<Movement> findMovementAction(Set<Goal> violatedGoals, VDC vdc)
	{
		//retrieve the data sources used by the method
		//TODO: to insert binding in blueprint
		//TODO: to consider transformations
		
		//create arrayList of movements to be enacted 
		ArrayList<Movement> movementsToBeEnacted = new ArrayList<Movement>();
		
		//for each movements check if it has a positive impact on the goal
		for (Movement movement : vdc.getMovements())
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
		
		return movementsToBeEnacted;
	}
	
	/**
	 * The method order a set of movements based on a given strategy. 
	 * 
	 * @param movementsToBeEnacted the set of movements actions to be ordered
	 * @param strategy the ordering strategy
	 * @return the ordered movement actions
	 */
	public static ArrayList<Movement> orderMovementAction(ArrayList<Movement> movementsToBeEnacted, Strategy strategy)
	{
		
		switch(strategy) {
			case MONETARY: 
				Collections.sort(movementsToBeEnacted, new MovementsActionsManager().new MonetaryCostComparator());
				break;
			case TIME: 
				Collections.sort(movementsToBeEnacted, new MovementsActionsManager().new TimeCostComparator());
				break;
			case POSITIVEIMPACTS:
				System.err.println(" 'positive impact' ordering non implemented yet");
				break;
			default:
				break;
		
		}

		return movementsToBeEnacted;
	}
	
	
	/**
	 * @author Mattia Salnitri
	 * 
	 * The class implements comparator, and it is used to order the movements with the cost strategy. It assumes that the measure units are the same for all movements
	 * attention: the comparator, used with collection.sort, will order the list with ascending costs
	 * form documentation of sort: The sort order is always ascending, where the Comparator defines which items are larger than others.  
	 *
	 */
	class MonetaryCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	ArrayList<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("monetary"))
	    			costA=cost;
	    	
	    	ArrayList<Cost> costsB = b.getCosts();
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
	/**
	 * @author Mattia Salnitri
	 *
	 * The class implements comparator, and it is used to order the movements with the cost strategy. It assumes that the measure units are the same for all movements  
	 *
	 */
	class TimeCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	ArrayList<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("time"))
	    			costA=cost;
	    	
	    	ArrayList<Cost> costsB = b.getCosts();
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
