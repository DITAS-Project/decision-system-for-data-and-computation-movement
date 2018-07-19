package it.polimi.deib.ds4m.main.model;

import java.util.ArrayList;

public class Violation 
{
	
	public ArrayList<Metric> metrics;
	public String methodId;
	public String vdcId;
	

	/**
	 * @return the methodID
	 */
	public String getMethodId() {
		return methodId;
	}
	/**
	 * @param methodID the methodID to set
	 */
	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}
	/**
	 * @return the vdcID
	 */
	public String getVdcId() {
		return vdcId;
	}
	/**
	 * @param vdcID the vdcID to set
	 */
	public void setVdcId(String vdcId) {
		this.vdcId = vdcId;
	}
	/**
	 * @return the metrics
	 */
	public ArrayList<Metric> getMetrics() {
		return metrics;
	}
	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(ArrayList<Metric> metrics) {
		this.metrics = metrics;
	}


}
