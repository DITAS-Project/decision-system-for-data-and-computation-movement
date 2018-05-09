package it.polimi.deib.ds4m.main.movement;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Method;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class ManageVDC 
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
	
	public static Vector<Movement> chechOtherVDC(Vector<Movement> movementsToBeEnacted, Vector<VDC> VDCs, VDC VDCselected)
	{
		//create an hasSet to collect movement to be moved behind in the
		//ATTENTION: the comparison does not consider the impacts.
		Set<Movement> movementsToBeMovedBehind = new HashSet<Movement>();
		
		for (VDC vdc : VDCs)
		{
			//skip the vdc of the violated method
			if (vdc.getId().equals(VDCselected.getId()))
				continue;
			
			Vector<Method> methods = vdc.getDataManagement().getMethods();
			for (Method method : methods)
			{
				
				Vector<DataSource> dataSources = vdc.getDataSources();
				for (DataSource dataSource : dataSources)
				{
					Vector<Movement> movements = dataSource.getMovements();
					
					//create a copy to be modified
					//ATTENTION: the elements inside are still copied by reference, it is a shallow copy
					Vector <Movement> movementsToBeEnactedOtherVDC = new Vector<>(movements);
					
					for (Movement movement : movements)
					{
						//remove movements that will not be enacted
						for (Movement movementToBeEnacted : movementsToBeEnacted )
						{
							//TODO:check if is enough 
							if (!movement.getType().equals(movementToBeEnacted.getType()))
								movementsToBeEnactedOtherVDC.remove(movement);
						}
					}
					
					//check if there is a goal that has only negative impact for a movement, if yes put the movement in the back
					for (Movement movement : movementsToBeEnactedOtherVDC) 
					{
						//shallow copy of goals vector
						Vector<Goal> goals = new Vector<>(method.getConstraints().getDataUtility().getGoals());
						
						//suppose a movement does not have both a positive and negative impact on the same goal
						Vector<String> negativeImpacts = movement.getNegativeImpacts();
						//follow the negative impact of the data movement to be enacted in the other VDC
						for (String negativeImpact : negativeImpacts)
						{
							//search for the goal
							for (Goal goal : method.getConstraints().getDataUtility().getGoals())
							{
								if (goal.getID().equals(negativeImpact))
								{
									//once i found the goal check if has any positive impacts
									for (Movement movementforPositiveImpact : movements)
									{
										//if there is a positive impact then remove the goal
										if (movementforPositiveImpact.getPositiveImpacts().contains(goal.getID()))
										{
											goals.remove(goal);
											break;
										}
									}
									break;
								}
							}
							
							//here i have all goals with no positive impacts
							//if the re at least 1 goal, then put the movement back
							if (goals.size()>0)
							{
								//set
								movementsToBeMovedBehind.add(movement);
								
							}
						}
					}
				}

				
			}
		}
		
		//here i collect all movement of all method of all VDCs that have a negative impact on goals that have no positive impact.
		//i move scu movement behon in the vectopr of movement to be enacted.
		//if i move all of them, the initial order is given back
		
		//move element at the end of the vector
		for (Movement movement : movementsToBeMovedBehind)
		{
			int posMovement = movementsToBeEnacted.lastIndexOf(movement);//movement derive from the list of do, whgile movement to be enacted is a parameter//use equals (overridden method)
			Movement movementTmp=movementsToBeEnacted.get(posMovement);
			movementsToBeEnacted.remove(posMovement);
			movementsToBeEnacted.add(movementTmp);
		}

		
		return movementsToBeEnacted;
	} 
	

}
