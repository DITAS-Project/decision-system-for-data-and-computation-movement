package it.polimi.deib.ds4m.main.movement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class VDCManager 
{
	
	/**
	 * the function find the violated VDC given the violation (with inside the ID) and given the list of VDC managed by the VDM  
	 * 
	 * @param violation the violation received by the SLA manager
	 * @param VDCs the list of VDC
	 * @return the violated VDC or null, if it is not found
	 */
	public static VDC findViolatedVDC(Violation violation, Vector<VDC> VDCs)
	{
        
        for(VDC vdcExamined : VDCs)
        {
    		if (vdcExamined.getId().equals(violation.getVdcID()))
    			return vdcExamined;
        }
        
        return null;

	}
	
	/**
	 * Controls if the movement to be enacted have negative impacts on goals that don't have other positive impact associated, in other VDCs. 
	 * ATTENTION: the selection of movements of other VDC does not consider the impacts. ( see implementation of "equals) of movements"
	 * 
	 * ATTENTION: suppose a goal does not receive both positive and negative links from the same movement action 
	 * 
	 * @param movementsToBeEnacted the list of movements to be enacted
	 * @param VDCs the other VDCs
	 * @param VDCselected the VDC selected (and that violated the requirements)
	 * @return the movement to be enacted but with the movements that negatively impact goal with no other positive impact, back behind in the list 
	 */
	public static Vector<Movement> chechOtherVDC(Vector<Movement> movementsToBeEnacted, Vector<VDC> VDCs, VDC VDCselected)
	{
		//create an hasSet to collect movement to be moved behind in the
		Set<Movement> movementsToBeMovedBehind = new HashSet<Movement>();
		
		for (VDC vdc : VDCs)
		{
			//skip the vdc of the violated method
			if (vdc.getId().equals(VDCselected.getId()))
				continue;
			
			
			//for all goal models, .i.e., abstract property, [one for each method] of each VDC	
			//if the method is available but has not been selected during the selection phase performed by RE and DURE, then the abstract property for that method simply does not exists
			for (AbstractProperty abstractProperty: vdc.getAbstractProperties())
			{
				//set of movement ( hash set since i need only 1 movement per type in 1 method ( if the input is well formed, there shouldn't be more then 1)
				HashSet <Movement> movementsToBeEnactedOtherVDC = new HashSet <>();
				
				//set of all movement for all the data sources
				ArrayList<Movement> allMovements = new ArrayList<Movement>();
				
				//select all possible data movements ( which are stored in the data sources objects)
				for (DataSource dataSource : vdc.getDataSources())
				{
					Vector<Movement> movements = dataSource.getMovements();
					
					//add to the list of all movement
					allMovements.addAll(movements);
					
					//remove, from all possible movement of the data source, of the VDC, all movements that will not be enacted (vhecking if the name)
					//iterate over the names and check if the selected movement is equal to the movement to be enacted 
					for (Movement movement : movements)
					{
						for (Movement movementToBeEnacted : movementsToBeEnacted )
						{
							//if the movement of the data source of the other VDC correspond to a movement that will be enacted, then it will be considered 
							if (movement.equals(movementToBeEnacted))
								movementsToBeEnactedOtherVDC.add(movement);
						}
					}
				}
					
				//at this point all movements that corresponds to the ones that can be enacted in the original VDC, are selected.  
				
				//check if there is a goal that has only negative impact for a movement, if yes put the movement in the back
				for (Movement movement : movementsToBeEnactedOtherVDC) 
				{
					//obtain all leaves
					ArrayList<Goal> leaves = new ArrayList<Goal>();
					TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getDataUtility(), leaves);
					
					//suppose a movement does not have both a positive and negative impact on the same goal
					Vector<String> negativeImpacts = movement.getNegativeImpacts();
					
					//follow the negative impact of the data movement to be enacted in the other VDC
					for (String negativeImpact : negativeImpacts)
					{
						//check if there is any data movement that has any positive impacts
						for (Movement movementforPositiveImpact : allMovements)
						{
							//if there is a positive impact then remove the goal
							if (movementforPositiveImpact.getPositiveImpacts().contains(negativeImpact))
							{
								leaves.remove(TreeStructure.getLeafByID(abstractProperty.getGoalTrees().getDataUtility(), negativeImpact));
								break;
							}
						}
						
						//here i have all goals with no positive impacts
						//if the re at least 1 goal, then put the movement back
						if (leaves.size()>0)
						{
							//set
							movementsToBeMovedBehind.add(movement);
							
						}
					}
				}

				
			}
		}
		
		//here i collect all movement of all method of all VDCs that have a negative impact on goals that have no positive impact.
		//i move such movements behind in the vector of movements to be enacted.
		//if i move all of them, the initial order is given back
		//move element at the end of the vector
		for (Movement movement : movementsToBeMovedBehind)
		{
			int posMovement = movementsToBeEnacted.lastIndexOf(movement);//movement derive from the list of do, while movement to be enacted is a parameter//use equals (overridden method)
			Movement movementTmp=movementsToBeEnacted.get(posMovement);
			movementsToBeEnacted.remove(posMovement);
			movementsToBeEnacted.add(movementTmp);
		}

		
		return movementsToBeEnacted;
	} 
	

}