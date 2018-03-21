package it.polimi.deib.ds4m.main.model;

public class Violation 
{
	public Violation(String type,
	Integer agreementid,
	String guaranteename,
	String date,
	String metric,
	Double value)
	{
		this.type=type;
		this.agreementid = agreementid;
		this.guaranteename = guaranteename;
		this.date = date;
		this.metric = metric;
		this.value = value;
	}
	
	public String type;
	public Integer agreementid;
	public String guaranteename;
	public String date;
	public String metric;
	public Double value;

}
