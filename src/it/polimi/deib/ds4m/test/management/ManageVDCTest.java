package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.GoalTree;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Metric;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;

public class ManageVDCTest {
	
	/**
	 * Test if it returns the correct id
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("availability");
		violation.setValue("90.0");
		violation.setMethodID("1");
		violation.setVdcID("01");
		

		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		
		//identify VDC
        VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC.getId().equals("01"));
		
		
	}
	
	/**
	 * test if the method returns null if no VDC are found
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_noResults() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("availability");
		violation.setValue("90.0");
		violation.setMethodID("1");
		violation.setVdcID("10");

		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		
		//identify VDC
        VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC==null);
		
		
	}

	/**
	 * test if the function returns the movement to be enacted list untouched, if the movements are not present in other VDC  
	 * 
	 * @throws IOException
	 */
	//@Test
    public void chechOtherVDC_Correct_noMovementsCommon() throws IOException 
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
	    MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON);
		
		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("01");

		
		//create a collection of vdcs
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		Vector<Movement> movementsToBeEnacted = new Vector<Movement>();
		
		//set up the first movement
		Vector<Cost> costs = new Vector<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(15.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setFrom("CCC");//no movement with this parameter in other VDC
		movement1.setTo("DDD");//no movement with this parameter in other VDC
		movement1.setRestTime(12.);
		movement1.setType("ComputationMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals])
		Movement movement2 =  new Movement();
		movement2.setFrom("AAA");//no movement with this parameter in other VDC
		movement2.setTo("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movment
		
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==0);
        assertTrue(movementsToBeEnacted.indexOf(movement2)==1);
		
		
	}
	
	/**
	 * test if the function correctly move 1 movement from the first position, behind.
	 * 
	 * @throws IOException
	 */
	//@Test
    public void chechOtherVDC_CorrectFirstPosition() throws IOException 
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
	    MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON);
		
		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("01");

		
		//create a collection of vdcs
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		Vector<Movement> movementsToBeEnacted = new Vector<Movement>();
		
		//set up the first movement
		//the other VDC contain a movement similar to this one ( the same except the impacts) so this movement is selected on the other VDC (in set movementsToBeEnactedOtherVDC)
		//the other movement as an impact on a goal (id=4) that has no positive linked associated, so this data movement will be moved back in the list
		
		Vector<String> positiveImpacts1 = new Vector<String>();
		positiveImpacts1.add("1");//positive impacts are not considered for the movement to be enacted 
		
		Vector<String> negativeImpacts1 = new Vector<String>();
		negativeImpacts1.add("4");
		
		Vector<Cost> costs = new Vector<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(15.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);
		movement1.setNegativeImpacts(negativeImpacts1);
		movement1.setFrom("Edge");
		movement1.setTo("Cloud");
		movement1.setRestTime(12.);
		movement1.setType("ComputationMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals])
		Movement movement2 =  new Movement();
		movement2.setPositiveImpacts(positiveImpacts1);
		movement2.setNegativeImpacts(negativeImpacts1);
		movement2.setFrom("AAA");//no movement with this parameter in other VDC
		movement2.setTo("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movment
		
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==1);
		
	}
	
	/**
	 * test if the function correctly move 1 movement from the middle of the array of movements, behind.
	 * 
	 * @throws IOException
	 */
	//@Test
    public void chechOtherVDC_CorrectMiddlePosiotion() throws IOException 
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
	    MovementsActionsManager.instantiateMovementActions(dataSources,movementsJSON);
		
		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("01");

		
		//create a collection of vdcs
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		Vector<Movement> movementsToBeEnacted = new Vector<Movement>();
		
		//set up the first movement
		//the other VDC contain a movement similar to this one ( the same except the impacts) so this movement is selected on the other VDC (in set movementsToBeEnactedOtherVDC)
		//the other movement as an impact on a goal (id=4) that has no positive linked associated, so this data movement will be moved back in the list
		
		Vector<String> positiveImpacts1 = new Vector<String>();
		positiveImpacts1.add("1");//positive impacts are not considered for the movement to be enacted 
		
		Vector<String> negativeImpacts1 = new Vector<String>();
		negativeImpacts1.add("4");
		
		Vector<Cost> costs = new Vector<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(15.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);
		movement1.setNegativeImpacts(negativeImpacts1);
		movement1.setFrom("Edge");
		movement1.setTo("Cloud");
		movement1.setRestTime(12.);
		movement1.setType("ComputationMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals])
		Movement movement2 =  new Movement();
		movement2.setPositiveImpacts(positiveImpacts1);
		movement2.setNegativeImpacts(negativeImpacts1);
		movement2.setFrom("AAA");//no movement with this parameter in other VDC
		movement2.setTo("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movment
		
		//setup the third movement
		Movement movement0 =  new Movement();
		movement0.setPositiveImpacts(positiveImpacts1);
		movement0.setNegativeImpacts(negativeImpacts1);
		movement0.setFrom("AAA");//no movement with this parameter in other VDC
		movement0.setTo("BBB");//no movement with this parameter in other VDC
		movement0.setRestTime(12.);
		movement0.setType("ComputationMovement");
		movement0.setCosts(costs);
		//end setup
		
		movementsToBeEnacted.add(movement0);
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==2);
		
		
	}

}
