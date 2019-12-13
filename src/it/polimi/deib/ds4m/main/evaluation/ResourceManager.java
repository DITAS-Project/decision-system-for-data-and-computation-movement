package it.polimi.deib.ds4m.main.evaluation;

import java.util.UUID;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;
import wiremock.com.jayway.jsonpath.PathNotFoundException;

public class ResourceManager 
{
	/**
	 * moves a VDC in another resources ( and deletes the VDC for the original resource)
	 * 
	 * @param vdcToMove
	 * @param targetResource
	 */
	public static void moveVDC(VDC_evaluation vdcToMove, Infrastructure_evaluation targetResource)
	{
		vdcToMove.setCurrentInfrastructure(targetResource);
	}
	
	/**
	 * moves a VDC in another resources 
	 * 
	 * @param vdcToDuplicate
	 * @param targetResource
	 */
	public static void duplicateVDC(VDC_evaluation vdcToDuplicate, Infrastructure_evaluation targetResource)
	{
		//create a new VDC
		
//		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
//		String movementsJSON;
//		try {
//			movementsJSON = MovementsActionsManager.loadMovementClass(null);
//		} catch (Exception e) 
//		{
//			System.err.println("AddVDC: " + e.getMessage());
//			return;
//		}
//		
//		System.out.println("ADDVDC: parsed movements");
//
//		//created a VDC from the json files
//		VDC vdc;
//		try 
//		{
//			vdc = VDCManager.createVDC(concreteBlueprintJSON, movementsJSON);
//		} catch (Exception e) 
//		{
//			System.err.println("AddVDC: " + e.getMessage());
//			return;
//		}
//		
//		//set new VDC infrastructure
		
		
		
		//*********************
		//doesn't make much sense: when i duplicate a VDC the old one will keep triggering violations
		//*********************
	}

	/**
	 * moves a DAL in another resources ( and deletes the DAL for the original resource)
	 * 
	 * @param dalToMove
	 * @param targetResource
	 */
	public static void moveDAL(DAL_evaluation dalToMove, Infrastructure_evaluation targetResource)
	{
		
		dalToMove.setPosition(targetResource);
	}
	
	/**
	 * moves a DAL in another resources 
	 * 
	 * @param dalToDuplicate
	 * @param targetResource
	 */
	public static void duplicateDAL(VDC_evaluation violatedVDC, DAL_evaluation dalToDuplicate, Infrastructure_evaluation targetInfrastructure)
	{
		// ***update VDC with new DAL
		// if there is a movement to perform after movement, I set it


		// create DAL
		DAL duplicatedDAL = new DAL();
		duplicatedDAL.setPosition(targetInfrastructure);
		duplicatedDAL.setDataSources(dalToDuplicate.getDataSources());
		
		// set new id DAL
		String newDALID = UUID.randomUUID().toString();

		// create the unique identifier for the new DAL
		duplicatedDAL.setId(newDALID);

		// add dal to vdc
		violatedVDC.getDALs().add(duplicatedDAL);

		// update data movements (a new DAL means new data movements)
		String concreteBlueprintJSON = null;
		try {
			// the function needs the name of the file
			concreteBlueprintJSON = VDCManager.loadConcreteBlueprint(PathSetting.test_pv + "/" + violatedVDC.getId() + ".json");
		} catch (PathNotFoundException pnfE) {
			System.err.println("NofifyViolation: " + pnfE.getMessage());
			System.err.println("NofifyViolation: skip update VDC");
		} catch (Exception e) {
			System.err.println("NotifyViolation: " + e.getMessage());
			return;
		}

		// i skip the update if i failed to load the movement class.
		// this control is needed because of the PathNotFoundException: i still continue
		// even if the share volume is not accessible
		if (concreteBlueprintJSON != null) {
			// check if the persistent volume folder exists. if not, it is not mounted (it
			// is a junit test execution) and skip the save
			String movementsJSON;
			try {
				movementsJSON = MovementsActionsManager.loadMovementClass(null);
			} catch (Exception e) {
				System.err.println("NotifyViolation: " + e.getMessage());
				System.err.println("NotifyViolation: skip update VDC");
				return;
			}

			// update the VDC
			try {
				VDCManager.updateVDCmovements(concreteBlueprintJSON, movementsJSON, violatedVDC);
			} catch (Exception e) {
				System.err.println("NotifyViolation: " + e.getMessage());
			}

			System.out.println("VDC updated");
		}
		
	}
	

	/**
	 * check if there is enough resources in a resource to store the VDC
	 * 
	 * @param vdcToMove
	 * @param targetResource
	 * @return
	 */
	private boolean checkResourcesForVDC(VDC_evaluation vdcToMove, Infrastructure_evaluation targetResource)
	{
		return true;
	}
	
	/**
	 * check if there is enough resources in a resource to store the DAL
	 * 
	 * @param DALToMove
	 * @param targetResource
	 * @return
	 */
	private boolean checkResourcesForDAL(DAL_evaluation DALToMove, Infrastructure_evaluation targetResource)
	{
		return true;
	}

}
