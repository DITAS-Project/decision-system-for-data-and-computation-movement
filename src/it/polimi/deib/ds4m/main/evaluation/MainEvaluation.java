package it.polimi.deib.ds4m.main.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.functionalities.AddVDC_functionality;
import it.polimi.deib.ds4m.main.functionalities.NotifyViolation_functionality;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;
import wiremock.com.jayway.jsonpath.PathNotFoundException;

public class MainEvaluation 
{
	
	private static int connectionNetworkNumber=1;
	private static int connectionsPerNetwork=3;

	
	public static void main(String[] args) 
	{		
		//network
		ArrayList<NetworkConnection> network = new ArrayList<NetworkConnection>();
		
		
		//setup VDCs
		ArrayList<VDC_evaluation> VDCs = new ArrayList<VDC_evaluation>();
		
		//add VDCs
		
		// read all concrete blueprints and instantiate the vdcs
		Set<String> listBlueprints;
		
		try {
			listBlueprints = Utility.listFilesUsingFileWalk(PathSetting.test_pv, 1);
			
			for (String blueprint : listBlueprints) 
			{
				String concreteBlueprintJSON = loadConcreteBlueprint(blueprint);
				
				AddVDC_functionality.doFunctionality(
						VDCs, //list of VDCs to add the new one
						blueprint.substring(0,blueprint.lastIndexOf(".")), //VDC iD 
						concreteBlueprintJSON, //concrete blueprint
						100, 5, 1, 
						//blueprint.substring(0,blueprint.lastIndexOf("."))
						blueprint.substring(0,blueprint.lastIndexOf(".")).equalsIgnoreCase("batch")? "GetSimplifiedDiagnostic" : "GetStreamingData"
						
						, //method id 
						network
						);
			}	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		System.out.println("Added " + VDCs.size() + " VDCs");
		
		
		//VDC added with the infrastructure that will be shared by all
		//each VDC is placed in one infrastructure (DITAS default property) described in the blueprint
		ArrayList<Infrastructure_evaluation> infrastructures = new ArrayList<Infrastructure_evaluation>();
		for (VDC_evaluation vdc_evaluation : VDCs)
		{
			for (Infrastructure infra : vdc_evaluation.getInfrastructures())
				infrastructures.add((Infrastructure_evaluation) infra);
			
		}
			
		System.out.println("Added " + infrastructures.size() + " infrastructures");
		
		//for each infrastructure add availability
		
		//1- infr-cloudsigma-batch     4aaf7a50-ea9c-4525-8980-d05a2a692663
		//2- spart-fog-infrastructure  762a7547-ff4f-4f8d-b925-e3664003debc
		//3- spart-edge-infrastructure 8022066a-7947-43a6-a823-a33cf34679b4
		//4- infr-cloudsigma-stream    876a7464-6f75-4edd-a559-afd8bc48836c
		
		(searchInfrastructureByID(infrastructures, "4aaf7a50-ea9c-4525-8980-d05a2a692663")).setAvailability(80.0);
		(searchInfrastructureByID(infrastructures, "762a7547-ff4f-4f8d-b925-e3664003debc")).setAvailability(80.0);//default store VDC -> violation
		(searchInfrastructureByID(infrastructures, "8022066a-7947-43a6-a823-a33cf34679b4")).setAvailability(99.0);
		(searchInfrastructureByID(infrastructures, "876a7464-6f75-4edd-a559-afd8bc48836c")).setAvailability(99.0);
		
		//connect infrastructures 
		//1- Random
		
//		setup network with infrastructure		
//		add connection as the number specified
//		for (int cnn =0; cnn<connectionNetworkNumber; cnn++)
//		{
//			//parameter set randomly
//			NetworkConnection nc = new NetworkConnection(
//					ThreadLocalRandom.current().nextDouble(0.0, 2.0), //latency in seconds
//					ThreadLocalRandom.current().nextDouble(95.0, 100.0) //availability
//					);
//			
//			network.add(nc);
//			
//			//i cycle until i added all the infrastructures to the nc 
//			for (int cpn =0; cpn < connectionsPerNetwork && nc.getResourcesConnected().size() < infrastructures.size() ; cpn++)
//			{
//				Random rand = new Random();
//				//select a number
//				int pic = rand.nextInt(infrastructures.size());
//				
//				nc.addResources(infrastructures.get(pic));
//			}
//		}
	
		//2- fixed per case study
		
//		NetworkConnection nc = new NetworkConnection(
//				ThreadLocalRandom.current().nextDouble(0.0, 2.0), //latency in seconds
//				ThreadLocalRandom.current().nextDouble(95.0, 100.0) //availability
//				);
		
		NetworkConnection nc = new NetworkConnection(3.0);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "762a7547-ff4f-4f8d-b925-e3664003debc")); //spart-fog-infrastructure
		nc.addResources(searchInfrastructureByID(infrastructures, "4aaf7a50-ea9c-4525-8980-d05a2a692663")); //infr-cloudsigma-batch
		network.add(nc);
		
		
		nc = new NetworkConnection(
				ThreadLocalRandom.current().nextDouble(0.0, 2.0) //latency in seconds
				);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "762a7547-ff4f-4f8d-b925-e3664003debc")); //spart-fog-infrastructure
		nc.addResources(searchInfrastructureByID(infrastructures, "876a7464-6f75-4edd-a559-afd8bc48836c")); //infr-cloudsigma-stream
		network.add(nc);
		
		nc = new NetworkConnection(
				ThreadLocalRandom.current().nextDouble(0.0, 2.0) //latency in seconds
				);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "762a7547-ff4f-4f8d-b925-e3664003debc")); //spart-fog-infrastructure
		nc.addResources(searchInfrastructureByID(infrastructures, "8022066a-7947-43a6-a823-a33cf34679b4")); //spart-edge-infrastructure
		network.add(nc);
		
		nc = new NetworkConnection(
				ThreadLocalRandom.current().nextDouble(0.0, 2.0) //latency in seconds
				);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "4aaf7a50-ea9c-4525-8980-d05a2a692663")); //infr-cloudsigma-batch
		nc.addResources(searchInfrastructureByID(infrastructures, "876a7464-6f75-4edd-a559-afd8bc48836c")); //infr-cloudsigma-stream
		network.add(nc);
		
		
		// add connection dal (dall can be retrieved only by name)
		nc = new NetworkConnection(
				ThreadLocalRandom.current().nextDouble(0.0, 2.0) //latency in seconds
				);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "762a7547-ff4f-4f8d-b925-e3664003debc")); //spart-fog-infrastructure
		nc.addResources(searchInfrastructureByName(infrastructures, "212.8.121.134") ); //DAL influxdb-dal-local
		network.add(nc);
		
		nc = new NetworkConnection(
				ThreadLocalRandom.current().nextDouble(0.0, 2.0) //latency in seconds
				);
		
		nc.addResources(searchInfrastructureByID(infrastructures, "876a7464-6f75-4edd-a559-afd8bc48836c")); //infr-cloudsigma-stream
		nc.addResources(searchInfrastructureByName(infrastructures, "212.8.121.135") ); //DAL streaming-dal //remember to change the ip in the blueprint (before ssame IP) 
		network.add(nc);
		
		
		//start VDCs
		boolean violationFree=false;
		boolean nextTurn=false;
		boolean violationFound=false;
		
		
		int turns = 0;
		
		while (!violationFree)
		{
			System.out.println("turn: " + turns);
			
			for (VDC_evaluation vdc_evaluation : VDCs) //il provblema con la seconda iterazione, problemi con casting DAL? magari creazione DAL duplicatio..args serve duplicare il dsal o basta anche qui spostarlo? tanto quando lo duplico poi l'altro lo cancello
			{
				//(new Thread(vdc_evaluation, vdc_evaluation.getId())).start();
				Violation violation = vdc_evaluation.checkViolation();
				
				if (violation!=null) 
				{
					System.out.println("Violation found for VDC " + vdc_evaluation.getId());
					
					ArrayList<Violation> violations = new ArrayList<Violation>();
					violations.add(violation);
					
					NotifyViolation_functionality.doPost(VDCs, violations);
					
					nextTurn=false;
					violationFound=true;
					
					turns++;
					
					break;//if there is a violation, fix it and repeat the while cycle
					
				}
			}
			
			if (nextTurn)
				violationFree=true;//end the while cycle
			
			if (violationFound)
				nextTurn=true;
		}
		

		
		System.out.println("System corrected in " + (turns + 1) + " turns");
		
		

	}
	
	public MainEvaluation() 
	{

		
	}
	
	/**
	 * given a VDC id, it search the abstract blueprint in the persistent volumes, if it exists it returns it. 
	 * 
	 * @param VDC the name of the file 
	 * @return
	 * @throws Exception 
	 */
	public static String loadConcreteBlueprint(String VDC) throws Exception
	{
		String blueprintJson=null;
		
		if ((new File(PathSetting.test_pv)).exists())
		{		
			String path = PathSetting.test_pv + "/" + VDC;
			//System.out.println(path);
			File blueprintJSONFile = new File(path);
			
			try(BufferedReader blueprintJSONBR = new BufferedReader(new FileReader(blueprintJSONFile)))
			{
				blueprintJson = blueprintJSONBR.lines().collect(Collectors.joining("\n"));		
				blueprintJSONBR.close();
				
			} catch (FileNotFoundException e) 
			{
				throw new Exception("file not found");
			} catch (IOException e) 
			{
				throw new Exception("problem in reading the concrete blueprint at "+path);
			}
		}
		else
		{
			throw new PathNotFoundException("Persisten volume not found, load concrete blueprint");
		}


		return blueprintJson ;
	}
	
	public static Infrastructure_evaluation searchInfrastructureByID(ArrayList<Infrastructure_evaluation> infrastructures, String infrastructureID)
	{
		for (Infrastructure infra : infrastructures)
		{
			if (infra.getId()!=null && infra.getId().equalsIgnoreCase(infrastructureID))
				return (Infrastructure_evaluation) infra;
		}
		
		return null;
	}
	
	public static Infrastructure_evaluation searchInfrastructureByName(ArrayList<Infrastructure_evaluation> infrastructures, String infrastructureName)
	{
		for (Infrastructure infra : infrastructures)
		{
			if (infra.getName()!=null && infra.getName().equalsIgnoreCase(infrastructureName))
				return (Infrastructure_evaluation) infra;
		}
		
		return null;
	}
	

}
