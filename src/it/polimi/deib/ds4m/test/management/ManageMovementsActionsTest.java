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
package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.methodsInput.DataSourceInput;
import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;

public class ManageMovementsActionsTest 
{
	
	//paths to blueprint and violations
	//private static String pathCorrectBlueprint="./testResources/test_Blueprint_V6_correct.json";
	private static String pathCorrectBlueprint="./testResources/test_Blueprint_V7.json";
	private static String pathCorrectMovementClasses="./testResources/movementClasses.json";
	
	/**
	 * 
	 * Test if the number of movement action instantiated is correct
	 *  
	 * @throws IOException
	 */
	@Test
	public void instantiateMovementActions_correct_checkSize() throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			concreteBlueprintJSON=Utility.readFile(pathCorrectBlueprint, Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve resources
//		JsonNode resourcesJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
//		ArrayList<Infrastructure> resources;
//		try {
//			resources = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(resourcesJSON, Infrastructure[].class)));
//		}
//		catch (JsonProcessingException e) 
//		{
//			e.printStackTrace();
//			return;			
//		}
//		
//		//retrieve data sources
//		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
//		ArrayList<DataSource> dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
//		
//		//connect data sources with resource
//		for (DataSource dataSource: dataSources)
//		{
//			dataSource.createResource(resources);
//		}
		
		//retrieve resources (infrastucture)
		JsonNode infrastructureJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
		if (infrastructureJSON ==null)
		{
			String message = "manageMovementsActionsTest: the INFRASTRUCURE Section of the Blueprint is empty/n";
        	System.err.println(message);
			return;			
		}
		
		ArrayList<Infrastructure> infrastructures;
		try {
			infrastructures = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(infrastructureJSON, Infrastructure[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "manageMovementsActionsTest: error in parsing the INFRASTRUCURE Section of the Blueprint /n";
        	System.err.println(message);
			return;			
		}
		
		//retrive DALs
		JsonNode DALsJSON = root.get("INTERNAL_STRUCTURE").get("DAL_Images");
		if (DALsJSON ==null)
		{
			String message = "manageMovementsActionsTest: the DAL_Images Section of the Blueprint is empty/n";
        	System.err.println(message);
			return;			
		}
		
		Map<String,DAL> DALs;
		try {
			TypeReference<HashMap<String, DAL>> typeRef = new TypeReference<HashMap<String, DAL>>() {};
			DALs =mapper.readValue(
				    mapper.treeAsTokens(DALsJSON), 
				    mapper.getTypeFactory().constructType(typeRef));

		}
		catch (JsonProcessingException e) 
		{
			String message = "manageMovementsActionsTest: error in parsing the DAL_Images Section of the Blueprint /n";
        	System.err.println(message);
			return;			
		}
		
		//for each data source [DAL] creates a fake (initial) infrastructure for the data source, to allow movement
		//create an array list to it in the VDC object
		ArrayList<DAL> DALsArrayList = new ArrayList<DAL>();
		for (DAL DAL: DALs.values())
		{
			DAL.createResource(infrastructures);
			DALsArrayList.add(DAL);
		}
		

		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile(pathCorrectMovementClasses, Charset.forName("UTF-8"));
	    
	    //instantiate movement classes for each data source 
	    ArrayList<Movement> movements = MovementsActionsManager.instantiateMovementActions(infrastructures,movementsJSON,DALsArrayList);
	    
	    //1 DAL and 2 moment actions classes 4 infrastructure +  1 fake infrastructure for DAL: 16 duplications + 12 movments =28 movements 
 	    assertTrue(movements.size()==28);

 	    
 	    
	}
	
	/**
	 * Test if the movement action find are the correct number
	 * 
	 * @throws IOException
	 */
	@Test
	public void findMovementAction_correct_checkSize () throws IOException
	{
		//setup vdc
		VDC vdc = setUpVDC();
		
		//set violated goals
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		TreeStructure g1 = new TreeStructure(null);
		g1.setID("fastDataProcess");

		violatedGoals.add(g1);
		

		ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);

		//TODO: important: data sources are not filtered by capabilities, so I got 2, with the filtering this might change.
		assertTrue(movementsToBeEnacted.size()==4);//all DataMovement have positive impacts on dataVolume Goal
	}

	/**
	 * Test if the movement action find are the correct number, 
	 * which is 0 since there is only 1 vdc in its original position ( and fake infrastructure) and cannot be moved (but only replicated).
	 * the violated goal has only 1 positive contribution  
	 * 
	 * @throws IOException
	 */
	@Test
	public void findMovementAction_correct_checkSize_0 () throws IOException
	{
		//setup vdc
		VDC vdc = setUpVDC();
		
		//set violated goals
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		TreeStructure g1 = new TreeStructure(null);
		g1.setID("dataVolume");

		violatedGoals.add(g1);
		

		ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);

		//TODO: important: data sources are not filtered by capabilities, so I got 2, with the filtering this might change.
		assertTrue(movementsToBeEnacted.size()==0);//all DataMovement have positive impacts on dataVolume Goal
	}
	
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * 
	 * @throws IOException
	 */
	@Test
	public void findMovementAction_correct_checkContent () throws IOException
	{
		//setup vdc
		VDC vdc = setUpVDC();
	

		//set violated goals		
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		TreeStructure g1 = new TreeStructure(null);
		g1.setID("dataVolume");

		violatedGoals.add(g1);
		
		ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		//this check the type, there will be 16 data movements selected, all instances of a "data movement" class 
		for (Movement movementToBeEnacted : movementsToBeEnacted)
		{
			assertTrue(movementToBeEnacted.getType().equals("DataMovement"));
		}
		
		
	}
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * using monetary strategy 
	 * 
	 * @throws IOException
	 */
	@Test
	public void orderMovementAction_correct_monetary () throws IOException
	{
		//setup vdc
		VDC vdc = setUpVDC();
		
		//set 2 violated goals, in order to have all 4 data movement action selected
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		TreeStructure g1 = new TreeStructure(null);
		g1.setID("dataVolume");
		violatedGoals.add(g1);
		
		TreeStructure g2 = new TreeStructure(null);
		g2.setID("fastDataProcess");
		violatedGoals.add(g1);
		
		ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = MovementsActionsManager.orderMovementAction(movementsToBeEnacted, "MONETARY");
		
		
		int positionCost= -1;
		for (int i = 0; i < movementsToBeEnacted.size(); i++)
		{
			//for the first iteration only inspect the cost and find the right element
			//the first element is supposd to be the bets, so i will not check against other but i save the position of the wanted cost, to be used later for comparison
			if (i==0)
			{
				ArrayList<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("monetary"))
					{
						positionCost=j;
						break;
					}
				}
			}//if only one, automatically true
			else//from the second iteration
			{	
				ArrayList<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("monetary"))
					{
						//check if the order is ascending ( first less expensive [lower] then the last )
						assertTrue (costs.get(j).getValue() >= movementsToBeEnacted.get(i-1).getCosts().get(positionCost).getValue());
						positionCost=j;
						break;//skip the rest since I have already analyzed the type I'm interested in. 
					}
				}
			}
		}
		
	}
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * using time strategy
	 * 
	 * @throws IOException
	 */
	@Test
	public void orderMovementAction_correct_time () throws IOException
	{
		//setup vdc
		VDC vdc = setUpVDC();
		
		//set 2 violated goals, in order to have all 4 data movement action selected
		Set<TreeStructure> violatedGoals = new HashSet<TreeStructure>();
		TreeStructure g1 = new TreeStructure(null);
		g1.setID("dataVolume");
		violatedGoals.add(g1);
		
		TreeStructure g2 = new TreeStructure(null);
		g2.setID("fastDataProcess");
		violatedGoals.add(g1);
		
		ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = MovementsActionsManager.orderMovementAction(movementsToBeEnacted, "TIME");
		
		
		int positionCost= -1;
		for (int i = 0; i < movementsToBeEnacted.size(); i++)
		{
			//for the first iteration only inspect the cost and find the right element
			if (i==0)
			{
				ArrayList<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("time"))
					{
						positionCost=j;
						break;
					}
				}
			}//if inly one, automatically true
			else//from the second iteration
			{	
				ArrayList<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("time"))
					{
						//check if the order is ascending ( first less expensinve [lower] then the last )
						assertTrue (costs.get(j).getValue() >= movementsToBeEnacted.get(i-1).getCosts().get(positionCost).getValue());
						positionCost=j;
						break;//skip the rest since I have already analysed the type I'm interested in. 
					}
				}
			}
		}
		
	}
	
	
	public static VDC setUpVDC()
	{
		//parse blueprint
				String concreteBlueprintJSON;
				
				try 
				{
					concreteBlueprintJSON=Utility.readFile(pathCorrectBlueprint, Charset.forName("UTF-8"));
					
				} catch (IOException e) 
				{
					System.err.println("error in reading file applicationRequiorements");
					return null;
				}
				
				//convert the json in object
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
				mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				
				JsonNode root;
				try {
					root = mapper.readTree(concreteBlueprintJSON);
				} catch (IOException e2) 
				{
					e2.printStackTrace();
					return null;
				}
				
				//retrieve DATA MANAGEMENT
				JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
				if (dataManagementJson ==null)
				{
					String message = "manageMovementsActionsTest: the DATA MANAGEMENT Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;	
				}
				ArrayList<DataManagement> dataManagement; 
				try {
					mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
					dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
				}
				
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the DATA MANAGEMENT Section of the Blueprint /n" + e.getStackTrace().toString();
		        	System.err.println(message);
		        	return null;		
				}
				
				//retrieve ABSTRACT_PROPERTIES
				JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
				if (abstractPropertiesJson ==null)
				{
					String message = "manageMovementsActionsTest: the ABSTRACT PROPERTIES Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;		
				}
				ArrayList<AbstractProperty> abstractProperties; 
				try {
					abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
				}
				
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the ABSTRACT PROPERTIES Section of the Blueprint /n" + e.getStackTrace().toString();
					e.printStackTrace();
		        	System.err.println(message);
		        	return null;			
				}
				
				//retrieve resources (infrastucture)
				JsonNode infrastructureJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
				if (infrastructureJSON ==null)
				{
					String message = "manageMovementsActionsTest: the INFRASTRUCURE Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;		
				}
				
				ArrayList<Infrastructure> infrastructures;
				try {
					infrastructures = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(infrastructureJSON, Infrastructure[].class)));
				}
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the INFRASTRUCURE Section of the Blueprint /n";
		        	System.err.println(message);
		        	return null;		
				}
				
				//retrieve data sources
				JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
				if (dataSourcesJSON ==null)
				{
					String message = "manageMovementsActionsTest: the DATA SOURCES Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;		
				}
				ArrayList<DataSource> dataSources;
				try {
					dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
				}
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the DATA SOURCE Section of the Blueprint /n";
		        	System.err.println(message);
		        	return null;		
				}
				
				//retrive methods input
				JsonNode methodsInputsJSON = root.get("INTERNAL_STRUCTURE").get("Methods_Input").get("Methods");
				if (methodsInputsJSON ==null)
				{
					String message = "manageMovementsActionsTest: the Methods_Input Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;		
				}
				ArrayList<Method> methodsInputs;
				try {
					methodsInputs = new ArrayList<Method>(Arrays.asList(mapper.treeToValue(methodsInputsJSON, Method[].class)));
				}
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the Methods_Input Section of the Blueprint /n";
		        	System.err.println(message);
		        	return null;		
				}
				
				//link data source in method input with data sources already retrieved in the blueprint
				try {
					//for each method in the method input
					for (Method method : methodsInputs)
					{
						//for each data sources input described
						for (DataSourceInput dataSourceInput : method.getDataSources())
						{
							dataSourceInput.linkDatasource(dataSources);
						}
					}
				}
				catch (Exception e) 
				{
					String message = "manageMovementsActionsTest:  the ids in methods input does not match the id in data source: " + e.getMessage();
		        	System.err.println(message);
		        	return null;		
				}
				
				
				//retrive DALs
				JsonNode DALsJSON = root.get("INTERNAL_STRUCTURE").get("DAL_Images");
				if (DALsJSON ==null)
				{
					String message = "manageMovementsActionsTest: the DAL_Images Section of the Blueprint is empty/n";
		        	System.err.println(message);
		        	return null;	
				}
				
				Map<String,DAL> DALs;
				try {
					TypeReference<HashMap<String, DAL>> typeRef = new TypeReference<HashMap<String, DAL>>() {};
					DALs =mapper.readValue(
						    mapper.treeAsTokens(DALsJSON), 
						    mapper.getTypeFactory().constructType(typeRef));

				}
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the DAL_Images Section of the Blueprint /n";
		        	System.err.println(message);
		        	return null;			
				} catch (IOException e) {
					String message = "manageMovementsActionsTest: error in parsing the DAL_Images Section of the Blueprint /n";
		        	System.err.println(message);
		        	return null;	
				}
				
				//for each data source [DAL] creates a fake (initial) infrastructure for the data source, to allow movement
				//create an array list to it in the VDC object
				ArrayList<DAL> DALsArrayList = new ArrayList<DAL>();
				for (DAL DAL: DALs.values())
				{
					DAL.createResource(infrastructures);
					DALsArrayList.add(DAL);
				}
				
				
				
			    //retrieve VDC name
				JsonNode vdcNameJSON = root.get("INTERNAL_STRUCTURE").get("Overview").get("name");
				String vdcName;
				try {
					vdcName = mapper.treeToValue(vdcNameJSON, String.class);
				}
				catch (JsonProcessingException e) 
				{
					String message = "manageMovementsActionsTest: error in parsing the NAME Section of the Blueprint /n" + e.getStackTrace().toString();
		        	System.err.println(message);
		        	return null;			
				}
			
				
				//store info
			    String movementsJSON;
				try {
					movementsJSON = Utility.readFile(pathCorrectMovementClasses, Charset.forName("UTF-8"));
				} catch (IOException e) {
					return null;
				}
				
			    //instantiate movement classes for each data source
				ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(infrastructures,movementsJSON,DALsArrayList); 
			    if (instantiatedMovements==null)
			    {
			    	String message = "manageMovementsActionsTest: error in instantiating the data movement actions";
		        	System.err.println(message);
		        	return null;
			    }

			    
				//set up vdc
				VDC vdc = new VDC();
				vdc.setDataManagement(dataManagement);
				vdc.setAbstractProperties(abstractProperties);
				vdc.setDataSources(dataSources);
				vdc.setMovements(instantiatedMovements);
				vdc.connectAbstractProperties();
				vdc.setResources(infrastructures);
				vdc.setId(vdcName);
				vdc.setMethodsInputs(methodsInputs);
				vdc.setDALs(DALsArrayList);

				
				//OLD VERSION
//				//retrieve DATA MANAGEMENT
//				JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
//				ArrayList<DataManagement> dataManagement; 
//				try {
//					mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
//					dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
//				}
//				
//				catch (JsonProcessingException e) 
//				{
//					e.printStackTrace();
//					return null;			
//				}
//				
//				
//				
//				//retrieve ABSTRACT_PROPERTIES
//				JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
//				ArrayList<AbstractProperty> abstractProperties; 
//				try {
//					mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
//					abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
//				}
//				
//				catch (JsonProcessingException e) 
//				{
//					e.printStackTrace();
//					return null;			
//				}		
//				
//				//retrieve resources
//				JsonNode resourcesJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
//				ArrayList<Infrastructure> resources;
//				try {
//					resources = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(resourcesJSON, Infrastructure[].class)));
//				}
//				catch (JsonProcessingException e) 
//				{
//					e.printStackTrace();
//					return null;			
//				}
//				
//				//retrieve data sources
//				JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
//				ArrayList<DataSource> dataSources;
//				try {
//					dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
//				} catch (JsonProcessingException e2) 
//				{
//					e2.printStackTrace();
//					return null;
//				}
//				
//				//connect data sources with resource
//				for (DataSource dataSource: dataSources)
//				{
//					dataSource.createResource(resources);
//				}
//				
//				
//				//retrieve movement classes
//			    String movementsJSON;
//				try {
//					movementsJSON = Utility.readFile(pathCorrectMovementClasses, Charset.forName("UTF-8"));
//				} catch (IOException e1) 
//				{
//					e1.printStackTrace();
//					return null;
//				}
//				
//			    //instantiate movement classes for each data source
//				ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(resources,movementsJSON.toString(), DALS); 
//			    if (instantiatedMovements==null)
//			    {
//			    	return null;
//			    }
//			    
//			    
//			    //retrieve VDC name
//				JsonNode vdcNameJSON = root.get("INTERNAL_STRUCTURE").get("Overview").get("name");
//				String vdcName;
//				try {
//					vdcName = mapper.treeToValue(vdcNameJSON, String.class);
//				}
//				catch (JsonProcessingException e) 
//				{
//					return null;			
//				}
//				
//				
//				//set up vdc
//				VDC vdc = new VDC();
//				vdc.setDataManagement(dataManagement);
//				vdc.setAbstractProperties(abstractProperties);
//				vdc.setDataSources(dataSources);
//				vdc.setMovements(instantiatedMovements);
//				vdc.connectAbstractProperties();
//				vdc.setId(vdcName);
				
				return vdc;
	}
	

}
