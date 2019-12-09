/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.functionalities;


import java.util.ArrayList;

import it.polimi.deib.ds4m.main.evaluation.NetworkConnection;
import it.polimi.deib.ds4m.main.evaluation.VDC_evaluation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;

/**
 * implementation class AddVDC
 */
public class AddVDC_functionality {
	private static final long serialVersionUID = 1L;
       
 
	/**
	 * @param VDCs the list of VDCs
	 * @param VDCID the id of the VDC to add
	 * @param concreteBlueprintJSON the blueprint of the VDC to add
	 */
	public static void doFunctionality(ArrayList<VDC_evaluation> VDCs, String VDCID, String concreteBlueprintJSON, Integer diskSpaceUsed, Integer RAMUsed, Integer cpuUsed, String methodID, ArrayList<NetworkConnection> network )  
	{
		//retrieve the list of VDCs
		//if it is not set create a collection of VDCs
		if  (VDCs == null)
		{
			VDCs = new ArrayList<VDC_evaluation>();
		}
		
		
		
		//if the VDC already exists the skip it
		for (VDC vdc : VDCs)
		{
			if (vdc.getId().equals(VDCID))
			{
				System.out.println("AddVDC: VDC already present, ignored");
				return;
			}
				
		}	

		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		String movementsJSON;
		try {
			movementsJSON = MovementsActionsManager.loadMovementClass(null);
		} catch (Exception e) 
		{
			System.err.println("AddVDC: " + e.getMessage());
			return;
		}
		
		System.out.println("ADDVDC: parsed movements");

		//created a VDC from the json files
		VDC vdc;
		try 
		{
			vdc = VDCManager.createVDC(concreteBlueprintJSON, movementsJSON);
		} catch (Exception e) 
		{
			System.err.println("AddVDC: " + e.getMessage());
			return;
		}
		
		
		// i change the name with the name in the parameter
		if (VDCID!=null)//this check is for test environment(integration)
			vdc.setId(VDCID);
		
		VDC_evaluation vdc_Eval = new VDC_evaluation(VDCs, vdc, diskSpaceUsed, RAMUsed, cpuUsed, methodID, network);	
		
		//add the VDC
		VDCs.add(vdc_Eval);
	
	
	}

}
