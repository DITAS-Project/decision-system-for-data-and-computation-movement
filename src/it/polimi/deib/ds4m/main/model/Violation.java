package it.polimi.deib.ds4m.main.model;

import java.util.ArrayList;

public class Violation 
{
	
	public ArrayList<Metric> metrics;
	public String methodID;
	public String vdcID;
	

	/**
	 * @return the methodID
	 */
	public String getMethodID() {
		return methodID;
	}
	/**
	 * @param methodID the methodID to set
	 */
	public void setMethodID(String methodID) {
		this.methodID = methodID;
	}
	/**
	 * @return the vdcID
	 */
	public String getVdcID() {
		return vdcID;
	}
	/**
	 * @param vdcID the vdcID to set
	 */
	public void setVdcID(String vdcID) {
		this.vdcID = vdcID;
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
