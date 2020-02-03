/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.movement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movementEnaction.MovementEnaction;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

/**
 * @author Mattia Salnitri
 * 
 * The class manages the movement actions
 *
 */
public class MovementsActionsManager 
{

	//definition of possible strategies
	public String Strategy = ""; //MONETARY, TIME, POSITIVEIMPACTS before was an enum but changed because of eclipse problems
	
	/**
	 * The method instantiate the movement actions given a set of movement classes a set of data sources. 
	 * In particular, it creates a deep copy, by de-serializing the JSON of the movement classes, and assigning it to each data source 
	 * 
	 * @param dataSources set of data sources
	 * @param movementsJSON JSON representing the movement action classes
	 * @return The array list of instantiated movements, null if problems arise
	 */
	public static ArrayList<Movement> instantiateMovementActions(List<Infrastructure> infrastructures, String movementsJSON, ArrayList<DAL> DALs) 
	{
		//the container for the instantiated movement
		ArrayList<Movement> movements = new ArrayList<Movement>();
		
		//deep copy movements, done be de-serializing time by time 
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		
		try {
			root = mapper.readTree(movementsJSON);
			
			//retrieve movements
			JsonNode movementNode = root.get("movements");
			
			//assigned copied 
			for(Infrastructure infrastrucure_source: infrastructures)
			{
				for(Infrastructure infrastructure_target: infrastructures)
				{
					//if it is the same resource don't instantiate data movement action
					if (infrastrucure_source.equals(infrastructure_target))
						continue;

					//for a copy of resource add the data movement compatible 
					//this is the (deep copy), i create 1 new object for each new movement
					ArrayList<Movement> newMovements = new ArrayList<Movement> (Arrays.asList(mapper.treeToValue(movementNode, Movement[].class)));
					//set target and source
					for (Movement movement: newMovements)
					{
						//computation movement:
						if (movement.getType().equalsIgnoreCase("computationmovement") || movement.getType().equalsIgnoreCase("computationduplication") )
						{
							//i create the mobvement only if both infrastructures are not dummy ( created as placeholder for original DAL)
							if (infrastructure_target.getId()!=null && infrastrucure_source.getId()!=null)
							{
								movement.setFromLinked(infrastrucure_source);
								movement.setToLinked(infrastructure_target);
								movements.add(movement);
							}
						}
						
						
						//data movement:						
						if (movement.getType().equalsIgnoreCase("dataduplication") || movement.getType().equalsIgnoreCase("datamovement") || 
								movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement") || movement.getType().equalsIgnoreCase("dataMovementComputationMovement"))
						{
							//for each DAL
							for (DAL dal :DALs) 
							{
								//TODO: this check is to exclude streaming VDC, but needs to be improved since we needa new variable "type" in the blueprint, to dfine that a dal is streaming 
								if (dal.getId().equalsIgnoreCase("streaming-dal"))
									continue;
								
								if (		
										(!(infrastrucure_source.getIsDataSource()) || movement.getType().equalsIgnoreCase("dataduplication") || movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement")) && //if the source infrastructure is a data source, i can only duplicate from it 
										
										(!(infrastructure_target.getIsDataSource()) ) //&& //The target infrastructure is not a data source (i cannot move to a data source, this infrastructure is dummy, only to represent the data source)
										
//remove distinction between edge and cloud										
//										movement.getToType().toLowerCase().equals(infrastructure_target.getType().toLowerCase()) && //if it matches the location target (cloud/edge)
//										(movement.getFromType().toLowerCase().equals(infrastrucure_source.getType().toLowerCase()) ) //&&//if it matches the location source (cloud/edge)
										//infrastrucure_source.getType().equals(infrastructure_target.getType()) //the type of the source and target should be the same
										
										)
								{
									//set the targets
									movement.setFromLinked(infrastrucure_source);
									movement.setToLinked(infrastructure_target);
									movement.setDalToMove(dal);
									
									
									//if it is a mormal data movement/duplication
									if (movement.getType().equalsIgnoreCase("dataduplication") || movement.getType().equalsIgnoreCase("datamovement"))
									{
										//add to list of data movement action 
										movements.add(movement);
									}
									else 
									//if it is a composite movement (dataComputationMovement, i.e., first data and then computation movement)
									//i create multiple movements for the combination of the possible computation movement 
									if (movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement") || movement.getType().equalsIgnoreCase("dataMovementComputationMovement"))
									{
										for(Infrastructure infrastrucure_sourceCM: infrastructures)
										{
											for(Infrastructure infrastructure_targetCM: infrastructures)
											{
												//if it is the same resource don't instantiate data movement action
												if (infrastrucure_sourceCM.equals(infrastructure_targetCM))
													continue;
												
												//skip dummy infrastructuyre cretaed for DAL movement
												if (infrastructure_targetCM.getId()!=null && infrastrucure_sourceCM.getId()!=null)
												{
													//create a new data movement as the main one
													Movement mainMovement = new Movement(movement);
													
													//create the subsequent computation movement
													Movement nextMovement = new Movement(
															"ComputationMovement", 
															null,//from (string in the constructor, set later) 
															null, //to (string in the constructor, set later)
															null,//positive impacts [to null because it is defined in the main movement]
															null,//negative impacts [to null because it is defined in the main movement]
															null,//transformations [to null because not supported]
															null,//costs [to null because it is defined in the main movement]
															0.0//rest time [to null because it is defined in the main movement]
															);
													
													nextMovement.setFromLinked(infrastrucure_sourceCM);
													nextMovement.setToLinked(infrastructure_targetCM);
													
													//set the subsequent computation movement in the main data movement
													
													mainMovement.setNextMovement(nextMovement);
													
													//add the main data movement
													movements.add(mainMovement);
												}
											}
										}
										
									}
									
									
									
								}
								
							}
						
						}
						

					}
				}
			}
			
			return movements;
			
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * the method finds the movement actions that impact positively on a violated set of goals
	 * 
	 * @param violatedGoals the violated set of goals
	 * @param vdc the VDC that contains the method that violated the goals 
	 * @return the movements to be enacted that impact positively on the violated goals. a movement might impact on a subset of goals
	 */
	public static ArrayList<Movement> findMovementAction(Set<TreeStructure> violatedGoals, VDC vdc)
	{
		
		//create arrayList of movements to be enacted 
		ArrayList<Movement> movementsToBeEnacted = new ArrayList<Movement>();
		
		//for each movements check if it has a positive impact on the goal
		for (Movement movement : vdc.getMovements())
		{
			//for each impact, check if the goal is  present in the list of violated goals
			for (String impact : movement.getPositiveImpacts())
			{
				for(TreeStructure goal: violatedGoals)
				{
					
					if (impact.equals(goal.getID()) &&// check if the goal is present
							(	//in case the movement is a data movement
									!(movement.getType().toLowerCase().equals("dataduplication") || movement.getType().toLowerCase().equals("datamovement")) ||
									movementEnactable(movement, vdc) //	(dataduplication || datamovement) -> movementEnactable
									//in case the movement is a computation movement
						)
							&&
							( !(movement.getType().toLowerCase().equals("computationmovement") || movement.getType().toLowerCase().equals("computationduplication")) ||
									vdc.getCurrentInfrastructure().equals(movement.getFromLinked())) // (computationmovement ||computationduplication) -> the source infrastructure is the current one
							&&
							( !(movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement") || movement.getType().equalsIgnoreCase("dataMovementComputationMovement")) ||
									(
											movementEnactable(movement, vdc) &&
											vdc.getCurrentInfrastructure().equals(movement.getNextMovement().getFromLinked())
											
											)
									) // (dataDuplicationComputationMovement ||dataMovementComputationMovement) -> the data movement is enactable and for the next movement the source infrastructure is the current one
							
						)
						
						movementsToBeEnacted.add(movement);
				}
			}
		}
		
		return movementsToBeEnacted;
	}
	
	/**
	 * The method order a set of movements based on a given strategy. 
	 * 
	 * @param movementsToBeEnacted the set of movements actions to be ordered
	 * @param strategy the ordering strategy
	 * @return the ordered movement actions
	 */
	public static ArrayList<Movement> orderMovementAction(ArrayList<Movement> movementsToBeEnacted, String strategy)
	{
		
		switch(strategy) {
			case "MONETARY": 
				Collections.sort(movementsToBeEnacted, new MovementsActionsManager().new MonetaryCostComparator());
				break;
			case "TIME": 
				Collections.sort(movementsToBeEnacted, new MovementsActionsManager().new TimeCostComparator());
				break;
			case "POSITIVEIMPACTS":
				System.err.println(" 'positive impact' ordering non implemented yet");
				break;
			default:
				break;
		
		}

		return movementsToBeEnacted;
	}
	
	
	/**
	 * the method returns true if the DAL to be moved is in the infrastructure defined in the source  
	 * 
	 * @param movement
	 * @param vdc
	 * @return true if there is a DAL in the infrastructure that is defined in the source of the movement
	 */
	public static boolean movementEnactable(Movement movement, VDC vdc)
	{
		for (DAL dal : vdc.getDALs())
		{
			if (movement.getFromLinked().equals(dal.getPosition()))
				return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * @author Mattia Salnitri
	 * 
	 * The class implements comparator, and it is used to order the movements with the cost strategy. It assumes that the measure units are the same for all movements
	 * attention: the comparator, used with collection.sort, will order the list with ascending costs
	 * form documentation of sort: The sort order is always ascending, where the Comparator defines which items are larger than others.  
	 *
	 */
	class MonetaryCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	ArrayList<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("monetary"))
	    			costA=cost;
	    	
	    	ArrayList<Cost> costsB = b.getCosts();
	    	Cost costB=null; 
	    	for (Cost cost: costsB)
	    		if (cost.getType().equals("monetary"))
	    			costB=cost;
	    	
	    	if (costA.getValue() < costB.getValue())
	    		return -1;
	    	else if (costA.getValue() > costB.getValue())
	    		return 1;
	    	else
	    		return 0;
	    }
	}
	
	//assume same measure unit
	/**
	 * @author Mattia Salnitri
	 *
	 * The class implements comparator, and it is used to order the movements with the cost strategy. It assumes that the measure units are the same for all movements  
	 *
	 */
	class TimeCostComparator implements Comparator<Movement> {
	    @Override
	    public int compare(Movement a, Movement b) 
	    {
	    	ArrayList<Cost> costsA = a.getCosts();
	    	Cost costA=null; 
	    	for (Cost cost: costsA)
	    		if (cost.getType().equals("time"))
	    			costA=cost;
	    	
	    	ArrayList<Cost> costsB = b.getCosts();
	    	Cost costB=null; 
	    	for (Cost cost: costsB)
	    		if (cost.getType().equals("time"))
	    			costB=cost;
	    	
	    	if (costA.getValue() < costB.getValue())
	    		return -1;
	    	else if (costA.getValue() > costB.getValue())
	    		return 1;
	    	else
	    		return 0;
	    }
	}
	
	/**
	 * returns the movement class json
	 * 
	 * @param call the servlet object, to be used to read from the web inf in case the persisten volume is not available, if it is null it will no try to read the web inf 
	 * @return the Movement class json
	 * @throws Exception
	 */
	public static String loadMovementClass(HttpServlet call) throws Exception
	{
		String movementsJSON=null;
		
		if ((new File(PathSetting.movementClassJson)).exists())
		{
			try {
				BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(PathSetting.movementClassJson));
				movementsJSON= movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
				movementClassesJSONBR.close();
				
			} catch (FileNotFoundException e1) 
			{
				throw new Exception("movement class file not found");

			} catch (IOException e) {
				throw new Exception("failed closing the BufferedReader for movement classes");
			}
		}
		else//if the volume is not mounted, i retrieve it for test from web-inf folder
			if (call != null)//if it is null i will skip try to read from web inf
		{
			System.out.println("persistent volume for configuration not found, will load configuration from web inf");
			try {
				//retrieve movement classes
				InputStream inputstream = call.getServletConfig().getServletContext().getResourceAsStream(PathSetting.movementClassWEBINFJson);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
				StringBuilder movementsJSONBuilder = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			    	movementsJSONBuilder.append(line);
			    }
			    reader.close();
			    movementsJSON = movementsJSONBuilder.toString();
			    
			}
			catch (IOException  e) 
			{
				throw new Exception("Error in loading the movement action configuration file /n" + e.getStackTrace().toString());
	        	
			}
		}
		
		return movementsJSON;
	}
	
	@SuppressWarnings("deprecation")
	public static String DMECall(MovementEnaction movementEnaction)
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        
        //call to movement enactors
        HttpClient client = HttpClientBuilder.create().build();
        
        //call to dma in kubernetes
        HttpPost post = new HttpPost("http://localhost:30030/dme/init_movement/");
        
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        String jsonBody;
		try {
			jsonBody = mapper.writeValueAsString(movementEnaction);
			StringEntity entity = new StringEntity(jsonBody);
			post.setEntity(entity);
			System.out.println("DME call: "+jsonBody);
		} 
		catch (JsonProcessingException e1) 
		{
			System.err.println("DMECall: error in parsing the call to DME");
			jsonBody="";
		} catch (UnsupportedEncodingException e) 
		{ 
			System.err.println("DMECall: error in encoding the call to DME");
		}
        
        try {
			HttpResponse responseDE = client.execute(post);//response empty

            //Print out the response of the data movement
            System.out.println("Answer of DME: "+EntityUtils.toString(responseDE.getEntity()));
            
            return EntityUtils.toString(responseDE.getEntity());
            
            
        } catch (IOException e) 
        {
        	System.err.println("DMECall: error in calling to DME");
            return "";
        }
        
	}

}
