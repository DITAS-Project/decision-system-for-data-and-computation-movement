package it.polimi.deib.ds4m.main.evaluation;

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
	}
	
	/**
	 * moves a VDC in another resources 
	 * 
	 * @param vdcToDuplicate
	 * @param targetResource
	 */
	public static void duplicateVDC(VDC_evaluation vdcToDuplicate, Infrastructure_evaluation targetResource)
	{}

	/**
	 * moves a DAL in another resources ( and deletes the DAL for the original resource)
	 * 
	 * @param dalToMove
	 * @param targetResource
	 */
	public static void moveDAL(DAL_evaluation dalToMove, Infrastructure_evaluation targetResource)
	{}
	
	/**
	 * moves a DAL in another resources 
	 * 
	 * @param dalToDuplicate
	 * @param targetResource
	 */
	public static void duplicateDAL(DAL_evaluation dalToDuplicate, Infrastructure_evaluation targetResource)
	{}
	

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
