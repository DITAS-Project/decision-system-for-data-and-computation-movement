package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.GoalTree;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Attribute;
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
	@Test
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
	@Test
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
	@Test
    public void chechOtherVDC_Correct_noMovementsCommon() throws IOException 
	{
		//*** set up other VDC ( it takes the complete one, not necessary)
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
			e.printStackTrace();
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
		//*** end set up other VDC 

		
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
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals)
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
		//*** set up other VDC ( it takes the complete one, not necessary)
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
			e.printStackTrace();
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
		//*** end set up other VDC

		
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
		//the data movement selected (movements to be enacted) are two. 
		//movement1 has a negative impact to "id=completeness" so the function "checkOtherVDC" checks if the movement action has impact on other vdc. in this case the set of "other VDC" is composed of
		//the "complete VDC" set up in the beginning of this test. such VDC has a similar data movement with only negative impact in the goal "id=completeness". so the movement 1 should be moved behind.
		//movemen2 should be left untouched since there is no similar data movement
		
		Vector<String> positiveImpacts1 = new Vector<String>();
		positiveImpacts1.add("AAA");//positive impacts are not considered for the movement to be enacted 
		
		Vector<String> negativeImpacts1 = new Vector<String>();
		negativeImpacts1.add("completeness");
		
		Vector<Cost> costs = new Vector<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(50.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setNegativeImpacts(negativeImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setFrom("Edge");
		movement1.setTo("Cloud");
		movement1.setRestTime(12.);
		movement1.setType("DataMovement");
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
		//end setup movement
		
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
	@Test
    public void chechOtherVDC_CorrectMiddlePosiotion() throws IOException 
	{
		
		//*** set up other VDC ( it takes the complete one, not necessary)
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
			e.printStackTrace();
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
		//*** end set up other VDC

		
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
		//the data movement selected (movements to be enacted) are two. 
		//movement1 has a negative impact to "id=completeness" so the function "checkOtherVDC" checks if the movement action has impact on other vdc. in this case the set of "other VDC" is composed of
		//the "complete VDC" set up in the beginning of this test. such VDC has a similar data movement with only negative impact in the goal "id=completeness". so the movement 1 should be moved behind.
		//movemen2 should be left untouched since there is no similar data movement
		
		Vector<String> positiveImpacts1 = new Vector<String>();
		positiveImpacts1.add("AAA");//positive impacts are not considered for the movement to be enacted 
		
		Vector<String> negativeImpacts1 = new Vector<String>();
		negativeImpacts1.add("completeness");
		
		Vector<Cost> costs = new Vector<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(50.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setNegativeImpacts(negativeImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setFrom("Edge");
		movement1.setTo("Cloud");
		movement1.setRestTime(12.);
		movement1.setType("DataMovement");
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
		movement0.setFrom("CCC");//no movement with this parameter in other VDC
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
