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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.methodsInput.DataSourceInput;
import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;

public class VDCManager 
{
	
	/**
	 * the function find the violated VDC given the violation (with inside the ID) and given the list of VDC managed by the VDM  
	 * 
	 * @param violation the violation received by the SLA manager
	 * @param VDCs the list of VDC
	 * @return the violated VDC or null, if it is not found
	 */
	public static VDC findViolatedVDC(Violation violation, ArrayList<VDC> VDCs)
	{
		if  (VDCs==null)
			return null;
		
        //find the first VDC (all violation at this stage will refer to the same VDC)
        for(VDC vdcExamined : VDCs)
        {
    		if (vdcExamined.getId().equals(violation.getVdcId()))
    			return vdcExamined;
        }
        
        return null;

	}
	
	/**
	 * Controls if the movement to be enacted have negative impacts on goals that don't have other positive impact associated, in other VDCs. 
	 * ATTENTION: the selection of movements of other VDC does not consider the impacts. ( see implementation of "equals) of movements"
	 * 
	 * ATTENTION: suppose a goal does not receive both positive and negative links from the same movement action 
	 * 
	 * @param movementsToBeEnacted the list of movements to be enacted
	 * @param VDCs the other VDCs
	 * @param VDCselected the VDC selected (and that violated the requirements)
	 * @return the movement to be enacted but with the movements that negatively impact goal with no other positive impact, back behind in the list 
	 */
	public static ArrayList<Movement> chechOtherVDC(ArrayList<Movement> movementsToBeEnacted, ArrayList<VDC> VDCs, VDC VDCselected)
	{
		//create an hasSet to collect movement to be moved behind in the
		Set<Movement> movementsToBeMovedBehind = new HashSet<Movement>();
		
		for (VDC vdc : VDCs)
		{
			//skip the vdc of the violated method
			if (vdc.getId().equals(VDCselected.getId()))
				continue;
			
			
			//for all goal models, .i.e., abstract property, [one for each method] of each VDC	
			//if the method is available but has not been selected during the selection phase performed by RE and DURE, then the abstract property for that method simply does not exists
			for (AbstractProperty abstractProperty: vdc.getAbstractProperties())
			{
				//set of movement ( hash set since i need only 1 movement per type in 1 method ( if the input is well formed, there shouldn't be more then 1)
				HashSet <Movement> movementsToBeEnactedOtherVDC = new HashSet <>();
				
				//select all possible data movements ( which are stored in the data sources objects)
				
				//remove, from all possible movement of the data source, of the VDC, all movements that will not be enacted (vhecking if the name)
				//iterate over the names and check if the selected movement is equal to the movement to be enacted 
				for (Movement movement : vdc.getMovements())
				{
					for (Movement movementToBeEnacted : movementsToBeEnacted )
					{
						//if the movement of the data source of the other VDC correspond to a movement that will be enacted, then it will be considered 
						if (movement.equals(movementToBeEnacted))
							movementsToBeEnactedOtherVDC.add(movement);
					}
				}
				//at this point all movements that corresponds to the ones that can be enacted in the original VDC, are selected.  
				
				//check if there is a goal that has only negative impact for a movement, if yes put the movement in the back
				for (Movement movement : movementsToBeEnactedOtherVDC) 
				{
					//obtain all leaves
					ArrayList<TreeStructure> leaves = new ArrayList<TreeStructure>();
					TreeStructure.getAllLeaves(abstractProperty.getGoalTrees().getDataUtility(), leaves);
					
					//suppose a movement does not have both a positive and negative impact on the same goal
					ArrayList<String> negativeImpacts = movement.getNegativeImpacts();
					
					//follow the negative impact of the data movement to be enacted in the other VDC
					for (String negativeImpact : negativeImpacts)
					{
						//check if there is any data movement that has any positive impacts
						for (Movement movementforPositiveImpact : vdc.getMovements())
						{
							//if there is a positive impact then remove the goal
							if (movementforPositiveImpact.getPositiveImpacts().contains(negativeImpact))
							{
								leaves.remove(TreeStructure.getLeafByID(abstractProperty.getGoalTrees().getDataUtility(), negativeImpact));
								break;
							}
						}
						
						//here i have all goals with no positive impacts
						//if the re at least 1 goal, then put the movement back
						if (leaves.size()>0)
						{
							//set
							movementsToBeMovedBehind.add(movement);
							
						}
					}
				}

				
			}
		}
		
		//here i collect all movement of all method of all VDCs that have a negative impact on goals that have no positive impact.
		//i move such movements behind in the set of movements to be enacted.
		//if i move all of them, the initial order is given back
		//move element at the end of the set
		for (Movement movement : movementsToBeMovedBehind)
		{
			int posMovement = movementsToBeEnacted.lastIndexOf(movement);//movement derive from the list of do, while movement to be enacted is a parameter//use equals (overridden method)
			Movement movementTmp=movementsToBeEnacted.get(posMovement);
			movementsToBeEnacted.remove(posMovement);
			movementsToBeEnacted.add(movementTmp);
		}

		
		return movementsToBeEnacted;
	} 
	
	/**
	 * creates a new vdc from scratch
	 * 
	 * @param concreteBlueprintJSON the concrete blueprint where the DAL is describe
	 * @param movementsJSON the movement action to instantiate
	 * @return the instantiated DAL
	 * @throws Exception
	 */
	public static VDC createVDC(String concreteBlueprintJSON, String movementsJSON) throws Exception
	{
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		JsonNode root; 
		try {
			root = mapper.readTree(concreteBlueprintJSON);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
//			String message = ;
//        	System.err.println(message);
        	
        	throw new Exception("error in reading the tree of the Blueprint");
        	
        	//response.getWriter().println(message);
			//response.setStatus(HttpStatus.SC_BAD_REQUEST);
        	
		}
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		if (dataManagementJson ==null)
		{
			throw new Exception("the DATA MANAGEMENT Section of the Blueprint is empty");

		}
		ArrayList<DataManagement> dataManagement; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the DATA MANAGEMENT Section of the Blueprint " + e.getStackTrace().toString());	
		}
		
		//retrieve ABSTRACT_PROPERTIES
		JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
		if (abstractPropertiesJson ==null)
		{
			throw new Exception("the ABSTRACT PROPERTIES Section of the Blueprint is empty");	
		}
		ArrayList<AbstractProperty> abstractProperties; 
		try {
			abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the ABSTRACT PROPERTIES Section of the Blueprint" + e.getStackTrace().toString());
		}
		
		//retrieve resources (infrastucture)
		JsonNode infrastructureJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
		if (infrastructureJSON ==null)
		{
			throw new Exception("the INFRASTRUCURE Section of the Blueprint is empty");
		}
		
		ArrayList<Infrastructure> infrastructures;
		try {
			infrastructures = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(infrastructureJSON, Infrastructure[].class)));
		}
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the INFRASTRUCURE Section of the Blueprint");
		}
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		if (dataSourcesJSON ==null)
		{
			throw new Exception("the DATA SOURCES Section of the Blueprint is empty");
		}
		ArrayList<DataSource> dataSources;
		try {
			dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the DATA SOURCE Section of the Blueprint");
		}
		
		//retrive methods input
		JsonNode methodsInputsJSON = root.get("INTERNAL_STRUCTURE").get("Methods_Input").get("Methods");
		if (methodsInputsJSON ==null)
		{
			throw new Exception("the Methods_Input Section of the Blueprint is empty");
		}
		ArrayList<Method> methodsInputs;
		try {
			methodsInputs = new ArrayList<Method>(Arrays.asList(mapper.treeToValue(methodsInputsJSON, Method[].class)));
		}
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the Methods_Input Section of the Blueprint");
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
			throw new Exception(" the ids in methods input does not match the id in data source: " + e.getMessage());
		}
		
		
		//retrive DALs
		JsonNode DALsJSON = root.get("INTERNAL_STRUCTURE").get("DAL_Images");
		if (DALsJSON ==null)
		{
			throw new Exception("the DAL_Images Section of the Blueprint is empty");			
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
			throw new Exception("error in parsing the DAL_Images Section of the Blueprint");
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
			throw new Exception("error in parsing the NAME Section of the Blueprint" + e.getStackTrace().toString());
		}
	
		
	    //instantiate movement classes for each data source
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(infrastructures,movementsJSON,DALsArrayList); 
	    if (instantiatedMovements==null)
	    {
	    	throw new Exception("error in instantiating the data movement actions");
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
		
		return vdc;
	}
	
	/**
	 * update the instantiated movement actions when the list of DAL changes (one or more DAL is added or removed)
	 * ATTENTION: the list of DALs in vdc must be updated  
	 * 
	 * @param concreteBlueprintJSON the concrete blueprint of the VDC
	 * @param movementsJSON the movement to be instantiated
	 * @param vdc the VDC to be updated
	 * @throws Exception
	 */
	public static void updateVDCmovements(String concreteBlueprintJSON, String movementsJSON, VDC vdc) throws Exception
	{
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		JsonNode root; 
		try {
			root = mapper.readTree(concreteBlueprintJSON);
		}
		catch (Exception e)
		{
			e.printStackTrace();        	
        	throw new Exception("error in reading the tree of the Blueprint");
		}
		
		//retrieve resources (infrastucture)
		JsonNode infrastructureJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
		if (infrastructureJSON ==null)
		{
			throw new Exception("the INFRASTRUCURE Section of the Blueprint is empty");
		}
		
		ArrayList<Infrastructure> infrastructures;
		try {
			infrastructures = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(infrastructureJSON, Infrastructure[].class)));
		}
		catch (JsonProcessingException e) 
		{
			throw new Exception("error in parsing the INFRASTRUCURE Section of the Blueprint");
		}
		

		
	    //instantiate movement classes for each data source
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(infrastructures,movementsJSON,vdc.getDALs()); 
	    if (instantiatedMovements==null)
	    {
	    	throw new Exception("error in instantiating the data movement actions");
	    }

	    
		//update up vdc
		vdc.setMovements(instantiatedMovements);

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
		String path = PathSetting.blueprints_pv + "/" + VDC;
		//System.out.println(path);
		File blueprintJSONFile = new File(path);

		String blueprintJson=null;
		
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


		return blueprintJson ;
	}
	

}
