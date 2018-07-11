package it.polimi.deib.ds4m.main.model;

public class Violation 
{
	public String type;
	public Integer agreementid;
	public String guaranteename;
	public String date;
	public String metric;
	public String value;
	public String methodID;
	public String vdcID;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the agreementid
	 */
	public Integer getAgreementid() {
		return agreementid;
	}
	/**
	 * @param agreementid the agreementid to set
	 */
	public void setAgreementid(Integer agreementid) {
		this.agreementid = agreementid;
	}
	/**
	 * @return the guaranteename
	 */
	public String getGuaranteename() {
		return guaranteename;
	}
	/**
	 * @param guaranteename the guaranteename to set
	 */
	public void setGuaranteename(String guaranteename) {
		this.guaranteename = guaranteename;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the metric
	 */
	public String getMetric() {
		return metric;
	}
	/**
	 * @param metric the metric to set
	 */
	public void setMetric(String metric) {
		this.metric = metric;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
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


}
