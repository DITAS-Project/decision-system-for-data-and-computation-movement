package it.polimi.deib.ds4m.main.evaluation;

import it.polimi.deib.ds4m.main.model.dataSources.DAL;

public class DAL_evaluation extends DAL
{
	//characteristics of DAL
	private Integer diskSpaceUsed;
	private Integer RAMUsed;
	private Integer cpuUsed;
	
	
	public DAL_evaluation(DAL dal, Integer diskSpaceUsed, Integer RAMUsed, Integer cpuUsed) 
	{
		this.setDataSources(dal.getDataSources());
		this.setId(dal.getId());
		this.setOriginal_ip(dal.getId());
		this.setPosition(dal.getPosition());
		
		
		this.diskSpaceUsed = diskSpaceUsed;
		this.RAMUsed = RAMUsed;
		this.cpuUsed = cpuUsed;
	}
	
	/**
	 * @return the diskSpaceUsed
	 */
	public Integer getDiskSpaceUsed() {
		return diskSpaceUsed;
	}
	/**
	 * @param diskSpaceUsed the diskSpaceUsed to set
	 */
	public void setDiskSpaceUsed(Integer diskSpaceUsed) {
		this.diskSpaceUsed = diskSpaceUsed;
	}
	/**
	 * @return the rAMUsed
	 */
	public Integer getRAMUsed() {
		return RAMUsed;
	}
	/**
	 * @param rAMUsed the rAMUsed to set
	 */
	public void setRAMUsed(Integer rAMUsed) {
		RAMUsed = rAMUsed;
	}
	/**
	 * @return the cpuUsed
	 */
	public Integer getCpuUsed() {
		return cpuUsed;
	}
	/**
	 * @param cpuUsed the cpuUsed to set
	 */
	public void setCpuUsed(Integer cpuUsed) {
		this.cpuUsed = cpuUsed;
	}

}
