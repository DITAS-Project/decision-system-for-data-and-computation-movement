package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.movement.ManageMovementsActions;

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
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON);
	    
	    //2 data sources and 2 moment action classes so 4 data movement action instance 
 	    assertTrue(dataSources.get(0).getMovements().size()==2);
 	    assertTrue(dataSources.get(1).getMovements().size()==2);

 	    
 	    
	}
	
	/**
	 * Test if the movement action generated are correct
	 * 
	 * @throws IOException
	 */
	@Test
	public void instantiateMovementActions_correct_checkContent () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON);
	    
 	    
 	    //data movement instance consists of copy  of movement classes
 
		try {
			root = mapper.readTree(movementsJSON);
			
			//retrieve movements
			JsonNode movementNode = root.get("movements");		
 	    
			Vector<Movement> movements = new Vector<Movement> (Arrays.asList(mapper.treeToValue(movementNode, Movement[].class)));
			
			assertTrue(dataSources.get(0).getMovements().equals(movements));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
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
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
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
		

		Vector<Movement> movementsToBeEnacted = ManageMovementsActions.findMovementAction(violatedGoals,  vdc);
		
		
		//TODO: important: not i don't filter data sources so I got 4, with the filtering this might change.
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
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
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
		
		Vector<Movement> movementsToBeEnacted = ManageMovementsActions.findMovementAction(violatedGoals,  vdc);
		
		//important: i statically chose the first movement of the first class, because i know it will be selected
		assertTrue(movementsToBeEnacted.elementAt(0).equals(dataSources.get(0).getMovements().get(0)));
	}
	
	/**
	 * Test if the movement action retrieved is correct. the test is on only one  action
	 * 
	 * @throws IOException
	 */
	@Test
	public void orderMovementAction_correct_monetary () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
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
		
		Vector<Movement> movementsToBeEnacted = ManageMovementsActions.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = ManageMovementsActions.orderMovementAction(movementsToBeEnacted, ManageMovementsActions.Strategy.MONETARY);
		
		
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
	 * 
	 * @throws IOException
	 */
	@Test
	public void orderMovementAction_correct_time () throws IOException
	{
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
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
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
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
		
		Vector<Movement> movementsToBeEnacted = ManageMovementsActions.findMovementAction(violatedGoals,  vdc);
		
		movementsToBeEnacted = ManageMovementsActions.orderMovementAction(movementsToBeEnacted, ManageMovementsActions.Strategy.TIME);
		
		
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
