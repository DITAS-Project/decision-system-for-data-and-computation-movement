package it.polimi.deib.ds4m.main.movement;

import java.io.IOException;
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

/**
 * @author Mattia Salnitri
 * 
 * The class manages the movement actions
 *
 */
public class ManageMovementsActions 
{

	//definition of possible strategies
	public enum Strategy
	{
		COSTS,
		TIME,
		POSITIVEIMPACTS		
	}
	
	/**
	 * The method instantiate the movement actions given a set of movement classes a set of data sources. 
	 * In particular, it creates a deep copy, by de-serializing the JSON of the movement classes, and assigning it to eachdata source 
	 * 
	 * @param dataSources set of data sources
	 * @param movementsJSON JSON representing the movement action classes
	 * @return true if the instantiation was correct, false otherwise
	 */
	public static Boolean instantiateMovementActions(List<DataSource> dataSources, String movementsJSON) 
	{
		//deep copy movements, done be de-serializing time by time 
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
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * the method finds the movement actions that impact positively on a violated set of goals
	 * 
	 * @param violatedGoals the violated set of goals
	 * @param vdc the VDC that contains the method that violated the goals 
	 * @return the movements to be enacted that impact positively on the violated goals. a movement might impact on a subset of goals
	 */
	public static Vector<Movement> findMovementAction(Set<Goal> violatedGoals, VDC vdc)
	{
		//retrieve the data sources used by the method
		//TODO: to insert binding in blueprint
		//TODO: to consider transformations
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
	
	/**
	 * The method order a set of movements based on a given strategy. 
	 * 
	 * @param movementsToBeEnacted the set of movements actions to be ordered
	 * @param strategy the ordering strategy
	 * @return the ordered movement actions
	 */
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
	
	
	/**
	 * @author Mattia Salnitri
	 * 
	 * The class implements comparator, and it is used to order the movements with the cost strategy. It assumes that the measure units are the same for all movements  
	 *
	 */
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
