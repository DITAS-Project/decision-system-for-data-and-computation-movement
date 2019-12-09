package it.polimi.deib.ds4m.main.evaluation;

import java.util.ArrayList;

import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

public class Infrastructure_evaluation extends Infrastructure
{
	ArrayList<DAL_evaluation> DALsHosted;
	ArrayList<VDC_evaluation> VDCsHosted;
	
	//characteristics of resource
	Integer diskSpaceAvailable;
	Integer RAMAvailable;
	Integer cpuAvailable;
	
	public Infrastructure_evaluation(Infrastructure infra, 	Integer diskSpaceAvailable, Integer RAMAvailable, Integer cpuAvailable) 
	{
		this.setExtra_properties(infra.getExtra_properties());
		this.setId(infra.getId());
		this.setIsDataSource(infra.getIsDataSource());
		this.setName(infra.getName());
		this.setType(infra.getType());
		
		this.diskSpaceAvailable = diskSpaceAvailable;
		this.cpuAvailable = cpuAvailable;
		this.RAMAvailable = RAMAvailable;
		
	}

}
