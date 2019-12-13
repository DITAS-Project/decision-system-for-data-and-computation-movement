package it.polimi.deib.ds4m.main.evaluation;

import java.util.ArrayList;
import java.util.Map;

import it.polimi.deib.ds4m.main.functionalities.NotifyViolation_functionality;
import it.polimi.deib.ds4m.main.model.Metric;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Attribute;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;

/**
 * this class extends the VDC used in the DS4M and implements runnable since it will be used as thread
 * 
 * @author Mattia Salnitri
 *
 */
public class VDC_evaluation  extends it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC 
{

	private static final long serialVersionUID = 5878305364939675322L;
	
	//characteristics of VDC
	Integer diskSpaceUsed;
	Integer RAMUsed;
	Integer cpuUsed;
	String methodID;
	
	
	ArrayList<VDC_evaluation> VDCs;
	ArrayList<NetworkConnection> network;
	
	public VDC_evaluation(ArrayList<VDC_evaluation> VDCs, VDC vdc, Integer diskSpaceUsed, Integer RAMUsed, Integer cpuUsed, String methodID, ArrayList<NetworkConnection> network) 
	{
		//VDC properties
		this.setAbstractProperties(vdc.getAbstractProperties());
		this.setCurrentInfrastructure(vdc.getCurrentInfrastructure());
		this.setDALs(vdc.getDALs());
		this.setDataManagement(vdc.getDataManagement());
		this.setDataSources(vdc.getDataSources());
		this.setId(vdc.getId());
		this.setInfrastructures(vdc.getInfrastructures());
		this.setMethodsInputs(vdc.getMethodsInputs());
		this.setMovements(vdc.getMovements());
		this.setNextMovement(vdc.getNextMovement());
		
		//extra properties
		this.diskSpaceUsed = diskSpaceUsed;
		this.RAMUsed = RAMUsed;
		this.cpuUsed = cpuUsed;
		this.methodID = methodID;//used?
		this.network = network;
		this.VDCs=VDCs;
		
		
	}

	
	//resource that host
	Infrastructure_evaluation currentResource;
	
	//dals connected
	ArrayList<DAL_evaluation> DALsUsed;
	
	
	//reference to DS4M, for the call for the violation
	

	/**
	 * main method ton check violations in a VDC
	 */
	public Violation checkViolation() 
	{
			//check violation 
			//VDC - DAL  -> check if the connection to each dal do not violate QOS
			//VDC - client -> not implemented yet
			
			for ( DAL dal : this.getDALs())
			{
				//cast it
				DAL_evaluation dal_evaluation = (DAL_evaluation) dal;
				
				//only 1 method per VDC
				TreeStructure tree = this.getAbstractProperties().get(0).getGoalTrees().getDataUtility();

				//inspect the tree, check if requirement are satisfied
				Violation violation = checkRequirements(tree, dal_evaluation);
				if (violation!= null)
					return violation;	
				
			}
			
			return null;
		
	}
	
	
	/**
	 * check that the requirement are satisfied for each DAL,
	 * if not returns a violation
	 * 
	 * @return null if no violation are identified, the violation in the other case
	 */
	public Violation checkRequirements(TreeStructure tree, DAL_evaluation dal_evaluation)
	{
		//for all leaf nodes
		if (tree.getLeaves()!=null)
		{
			for (TreeStructure leaves : tree.getLeaves())
			{
				//for all attributes in a leaf node
				for ( Attribute attribute : leaves.getAttributesLinked())
				{
					//look for the network connection that connects the the dal with this VDC
					ArrayList<NetworkConnection> network_VDC_DAL = new ArrayList<NetworkConnection>();
					for (NetworkConnection nc : network)
					{
						//check if the network connection can be selected and added to the network connections that link the VDC to the DAL
						if (nc.getResourcesConnected().contains(this.getCurrentInfrastructure()) && nc.getResourcesConnected().contains(dal_evaluation.getPosition()))
						{
							network_VDC_DAL.add(nc);
						}
					}
					
					//for each network connection slected, check if al least one satisfy requirements of this childern
					for(NetworkConnection nc : network_VDC_DAL)
					{
						return checkAttributerequirements(nc, attribute);
					}
				}
			}
		}
		
		
		//recursive part
		if (tree.getChildren()!=null)
		{
			for(TreeStructure children : tree.getChildren())
			{
				Violation violation = checkRequirements(children, dal_evaluation);
				
				//exit if a violation has been found
				if (violation!=null)
					return violation;
			}
		}
		return null;
	}
	
	private Violation checkAttributerequirements(NetworkConnection nc, Attribute attribute)
	{
		//get the first item
		Property property = attribute.getProperties().entrySet().iterator().next().getValue();
		
		switch(attribute.getType().toLowerCase())
		{
			case "latency"://latency is a resource to be chechked in the network 
				{
					//compare it with requirements
					if(
							(property.getMaximum()!=null && property.getMaximum()<=nc.getLatency())  ||
							(property.getMinimum()!=null && property.getMinimum()>=nc.getLatency())
							)
					{
						Violation violation = new Violation();
						violation.setMethodId(methodID);
						violation.setVdcId(this.getId());
						
						ArrayList<Metric> metrics = new ArrayList<Metric>();
						Metric metric = new Metric();
						metric.setKey("latency");
						metrics.add(metric);
						
						violation.setMetrics(metrics);
						
						return violation;
					}
						
						
				}
				break;
			case "availability"://avilability is a resource to be checked in VDC
				{
					//compare it with requirements of resopurces where the vdc is stored
					if(
							(property.getMaximum()!=null && property.getMaximum()<= ((Infrastructure_evaluation)this.getCurrentInfrastructure()).getAvailability() )  ||
							(property.getMinimum()!=null && property.getMinimum()>= ((Infrastructure_evaluation)this.getCurrentInfrastructure()).getAvailability() )
							)
					{
						Violation violation = new Violation();
						violation.setMethodId(methodID);
						violation.setVdcId(this.getId());
						
						ArrayList<Metric> metrics = new ArrayList<Metric>();
						Metric metric = new Metric();
						metric.setKey("availability");
						metric.setValue(((Infrastructure_evaluation)this.getCurrentInfrastructure()).getAvailability().toString());
						metrics.add(metric);
						
						violation.setMetrics(metrics);
						
						return violation;
					}
				}
				break;
			
			//if the type has not been recognized
			default:
				return null;
		}
		
		return null;
	}
	
	

}
