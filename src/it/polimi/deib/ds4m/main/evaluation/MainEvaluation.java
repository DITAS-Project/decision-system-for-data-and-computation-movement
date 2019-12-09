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
						blueprint.substring(0,blueprint.lastIndexOf(".")), //method id 
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
		
		
		
		//VDC added with the infreastructure that will be shared by all
		//each VDC in placed in one infrstructure described in the blueprint
		ArrayList<Infrastructure_evaluation> infrastructures = new ArrayList<Infrastructure_evaluation>();
		for (VDC_evaluation vdc_evaluation : VDCs)
		{
			for (Infrastructure infra : vdc_evaluation.getInfrastructures())
				infrastructures.add((Infrastructure_evaluation) infra);
			
		}
			
		//connect infrastructures 
		//1- Random
		//2- fully connected
		
		//setup network with infrastructure
		
		//add connection as the number specified
		for (int cnn =0; cnn<connectionNetworkNumber; cnn++)
		{
			//parameter set randomly
			NetworkConnection nc = new NetworkConnection(
					ThreadLocalRandom.current().nextDouble(0.0, 2.0), //latency in seconds
					ThreadLocalRandom.current().nextDouble(95.0, 100.0) //availability
					);
			
			network.add(nc);
			
			//i cycle until i added all the infrastructures to the nc 
			for (int cpn =0; cpn < connectionsPerNetwork && nc.getResourcesConnected().size() < infrastructures.size() ; cpn++)
			{
				Random rand = new Random();
				//select a number
				int pic = rand.nextInt(infrastructures.size());
				
				nc.addResources(infrastructures.get(pic));
			}
		}
	
		//start VDCs
		boolean violationFree=false;
		int turns = 0;
		
		while (!violationFree)
		{
			System.out.println("turn: " + turns);
			
			for (VDC_evaluation vdc_evaluation : VDCs)
			{
				//(new Thread(vdc_evaluation, vdc_evaluation.getId())).start();
				Violation violation = vdc_evaluation.checkViolation();
				
				if (violation!=null) 
				{
					System.out.println("violation found for VDC " + vdc_evaluation.getId());
					
					ArrayList<Violation> violations = new ArrayList<Violation>();
					violations.add(violation);
					
					NotifyViolation_functionality.doPost(VDCs, violations);
				}
				else
					violationFree=true;//end the while cycle
			}
		}

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
	

}
