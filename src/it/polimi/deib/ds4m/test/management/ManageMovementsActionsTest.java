package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;

public class ManageMovementsActionsTest 
{
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
			concreteBlueprintJSON=Utility.readFile("./testResources/example_V2_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./testResources/movementClasses.json", Charset.forName("UTF-8"));
	    
	    //instantiate movement classes for each data source 
	    ArrayList<Movement> movements = MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON);
	    
	    //2 data sources and 2 moment action classes so 4 data movement action instances 
 	    assertTrue(movements.size()==4);

 	    
 	    
	}
	
	/**
	 * Test if the movement action find are the correct number
	 * 
	 * @throws IOException
	 */
	@Test
	public void findMovementAction_correct_checkSize () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_V2_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		ArrayList<DataManagement> dataManagement; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}
		
		//retrieve ABSTRACT_PROPERTIES
		JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
		ArrayList<AbstractProperty> abstractProperties; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}		
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources;
		try {
			dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./testResources/movementClasses.json", Charset.forName("UTF-8"));
		
	    //instantiate movement classes for each data source
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON.toString()); 
	    if (instantiatedMovements==null)
	    {
			return;
	    }
	    
	    
	    //retrieve VDC name
		JsonNode vdcNameJSON = root.get("INTERNAL_STRUCTURE").get("Overview").get("name");
		String vdcName;
		try {
			vdcName = mapper.treeToValue(vdcNameJSON, String.class);
		}
		catch (JsonProcessingException e) 
		{
			return;			
		}
		
		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setAbstractProperties(abstractProperties);
		vdc.setDataSources(dataSources);
		vdc.setMovements(instantiatedMovements);
		vdc.connectAbstractProperties();
		vdc.setId(vdcName);
	    

		//set violated goals
		
		Set<Goal> violatedGoals = new HashSet<Goal>();
		Goal g1 = new Goal();
		g1.setID("dataVolume");

		violatedGoals.add(g1);
		

		Vector<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);

		//TODO: important: data sources are not filtered by capabilities, so I got 4, with the filtering this might change.
		assertTrue(movementsToBeEnacted.size()==4);
	}
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * 
	 * @throws IOException
	 */
	@Test
	public void findMovementAction_correct_checkContent () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_V2_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		ArrayList<DataManagement> dataManagement; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}
		
		//retrieve ABSTRACT_PROPERTIES
		JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
		ArrayList<AbstractProperty> abstractProperties; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}		
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources;
		try {
			dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return;			
		}
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./testResources/movementClasses.json", Charset.forName("UTF-8"));
		
	    //instantiate movement classes for each data source
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON.toString()); 
	    if (instantiatedMovements==null)
	    {
			return;
	    }
	    
	    
	    //retrieve VDC name
		JsonNode vdcNameJSON = root.get("INTERNAL_STRUCTURE").get("Overview").get("name");
		String vdcName;
		try {
			vdcName = mapper.treeToValue(vdcNameJSON, String.class);
		}
		catch (JsonProcessingException e) 
		{
			return;			
		}
		
		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setAbstractProperties(abstractProperties);
		vdc.setDataSources(dataSources);
		vdc.setMovements(instantiatedMovements);
		vdc.connectAbstractProperties();
		vdc.setId(vdcName);
	
		
		//set violated goals
		
		Set<Goal> violatedGoals = new HashSet<Goal>();
		Goal g1 = new Goal();
		g1.setID("1");

		violatedGoals.add(g1);
		
		Vector<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		//important: i statically chose the first movement of the first class, because i know it will be selected
		assertTrue(movementsToBeEnacted.elementAt(0).equals(dataSources.get(0).getMovements().get(0)));
	}
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * using monetary strategy 
	 * 
	 * @throws IOException
	 */
	//@Test
	public void orderMovementAction_correct_monetary () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_V2_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		DataManagement dataManagement = mapper.treeToValue(dataManagementJson, DataManagement.class);
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./testResources/movementClasses.json", Charset.forName("UTF-8"));
		
	    //instantiate movement classes for each data source 
	    MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("ID");
		
		//set violated goals
		
		Set<Goal> violatedGoals = new HashSet<Goal>();
		Goal g1 = new Goal();
		g1.setID("1");

		violatedGoals.add(g1);
		
		Vector<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = MovementsActionsManager.orderMovementAction(movementsToBeEnacted, MovementsActionsManager.Strategy.MONETARY);
		
		
		int positionCost= -1;
		for (int i = 0; i < movementsToBeEnacted.size(); i++)
		{
			//for the first iteration only inspect the cost and find the right element
			if (i==0)
			{
				Vector<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("monetary"))
					{
						positionCost=j;
						break;
					}
				}
			}//if inly one, automatically true
			else//from the second interation
			{	
				Vector<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("monetary"))
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
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * using time strategy
	 * 
	 * @throws IOException
	 */
	//@Test
	public void orderMovementAction_correct_time () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_V2_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		DataManagement dataManagement = mapper.treeToValue(dataManagementJson, DataManagement.class);
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./testResources/movementClasses.json", Charset.forName("UTF-8"));
		
	    //instantiate movement classes for each data source 
	    MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("ID");
		
		//set violated goals
		
		Set<Goal> violatedGoals = new HashSet<Goal>();
		Goal g1 = new Goal();
		g1.setID("1");

		violatedGoals.add(g1);
		
		Vector<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = MovementsActionsManager.orderMovementAction(movementsToBeEnacted, MovementsActionsManager.Strategy.TIME);
		
		
		int positionCost= -1;
		for (int i = 0; i < movementsToBeEnacted.size(); i++)
		{
			//for the first iteration only inspect the cost and find the right element
			if (i==0)
			{
				Vector<Cost> costs= movementsToBeEnacted.get(i).getCosts();
				for (int j =0; j < costs.size(); j++ )
				{
					if (costs.get(j).getType().equals("time"))
					{
						positionCost=j;
						break;
					}
				}
			}//if inly one, automatically true
			else//from the second interation
			{	
				Vector<Cost> costs= movementsToBeEnacted.get(i).getCosts();
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
	
	

}
