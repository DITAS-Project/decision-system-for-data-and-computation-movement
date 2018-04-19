package it.polimi.deib.ds4m.main.movement;

import java.util.Vector;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class ManageVDC 
{
	public static VDC findViolatedVDC(Violation violation, Vector<VDC> VDCs)
	{
        
        for(VDC vdcExamined : VDCs)
        {
    		if (vdcExamined.getId().equals(violation.getVdcID()))
    			return vdcExamined;
        }
        
        return null;

	}
	
	public static Vector<Movement> chechOtherVDC(Vector<Movement> movementToBeEnacted, Vector<VDC> VDCs, VDC VDCselected)
	{
		for (VDC vdc : VDCs)
		{
			if (vdc.getId().equals(VDCselected.getId()))
				continue;
			
			
			movementToBeEnacted.get(0)
			movementToBeEnacted.remove(0);
			movementToBeEnacted.add()
		}
		
		return null;
	}
	

}
