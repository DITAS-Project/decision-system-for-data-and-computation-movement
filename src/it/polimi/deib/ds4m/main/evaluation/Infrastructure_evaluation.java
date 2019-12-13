package it.polimi.deib.ds4m.main.evaluation;

import java.util.ArrayList;

import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

public class Infrastructure_evaluation extends Infrastructure
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7607611455685895724L;
	
	private ArrayList<DAL_evaluation> DALsHosted;
	private ArrayList<VDC_evaluation> VDCsHosted;
	
	//characteristics of resource
	private Integer diskSpaceAvailable;
	private Integer RAMAvailable;
	private Integer cpuAvailable;
	private Double availability;
	
	public Infrastructure_evaluation(Infrastructure infra, 	Integer diskSpaceAvailable, Integer RAMAvailable, Integer cpuAvailable, Double availability) 
	{
		this.setExtra_properties(infra.getExtra_properties());
		this.setId(infra.getId());
		this.setIsDataSource(infra.getIsDataSource());
		this.setName(infra.getName());
		this.setType(infra.getType());
		
		this.diskSpaceAvailable = diskSpaceAvailable;
		this.cpuAvailable = cpuAvailable;
		this.RAMAvailable = RAMAvailable;
		this.availability = availability;
		
	}

	/**
	 * @return the dALsHosted
	 */
	public ArrayList<DAL_evaluation> getDALsHosted() {
		return DALsHosted;
	}

	/**
	 * @param dALsHosted the dALsHosted to set
	 */
	public void setDALsHosted(ArrayList<DAL_evaluation> dALsHosted) {
		DALsHosted = dALsHosted;
	}

	/**
	 * @return the vDCsHosted
	 */
	public ArrayList<VDC_evaluation> getVDCsHosted() {
		return VDCsHosted;
	}

	/**
	 * @param vDCsHosted the vDCsHosted to set
	 */
	public void setVDCsHosted(ArrayList<VDC_evaluation> vDCsHosted) {
		VDCsHosted = vDCsHosted;
	}

	/**
	 * @return the diskSpaceAvailable
	 */
	public Integer getDiskSpaceAvailable() {
		return diskSpaceAvailable;
	}

	/**
	 * @param diskSpaceAvailable the diskSpaceAvailable to set
	 */
	public void setDiskSpaceAvailable(Integer diskSpaceAvailable) {
		this.diskSpaceAvailable = diskSpaceAvailable;
	}

	/**
	 * @return the rAMAvailable
	 */
	public Integer getRAMAvailable() {
		return RAMAvailable;
	}

	/**
	 * @param rAMAvailable the rAMAvailable to set
	 */
	public void setRAMAvailable(Integer rAMAvailable) {
		RAMAvailable = rAMAvailable;
	}

	/**
	 * @return the cpuAvailable
	 */
	public Integer getCpuAvailable() {
		return cpuAvailable;
	}

	/**
	 * @param cpuAvailable the cpuAvailable to set
	 */
	public void setCpuAvailable(Integer cpuAvailable) {
		this.cpuAvailable = cpuAvailable;
	}

	/**
	 * @return the availability
	 */
	public Double getAvailability() {
		return availability;
	}

	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(Double availability) {
		this.availability = availability;
	}

}
